package com.example.reportdemo

import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * @Author jacky.peng* @Date 2021/4/16 10:06 AM
 * @Version 1.0
 */
class TrackClassVisitor extends ClassVisitor {
    private String className;
    private String superName;
    private String[] interfaces;
    private boolean hasImplOnClick = false;


    TrackClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);

    }

    //类被访问
    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
        //获取类所实现的所有接口
        this.interfaces = interfaces;
        if (interfaces != null) {
//            for (int i = 0; i < interfaces.length; i++) {
//                if (interfaces[i].equals("android/view/View$OnClickListener")) {
//                    System.out.println("TrackClassVisitor visit className:" + className + "实现了OnClickListener");
//                    hasImplOnClick = true;
//                }
//            }
        }
        String topSuperName = TrackUtil.findTopParent(className)
        if (topSuperName != null) {
            superName = topSuperName;
        }
        System.out.println("TrackClassVisitor visit className:" + className + "---superName:" + superName);
    }

    String nameDesc;

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        //'onClick(Landroid/view/View;)V'
        nameDesc = name + descriptor;
//        System.out.println("TrackClassVisitor visitMethod className:" + className + "---nameDesc:" + nameDesc);
//        System.out.println("TrackClassVisitor visitMethod className:" + className + "---nameDesc:" + nameDesc);

        //如果该方法为接口中的方法
        if (hasImplOnClick) {
            System.out.println("TrackClassVisitor visitMethod hasImplOnClick:" + name);
            if ("onClick(Landroid/view/View;)V".equals(nameDesc)) {
                return new TrackOnClickMethodVisitor(mv, access, name, descriptor, className);
            }
        } else if (TrackUtil.isInstanceOfFragment(superName) && TrackHookConfig.FRAGMENT_METHODS.containsKey(nameDesc)) {
            //fragment的有效方法
            TrackMethodCell cell = TrackHookConfig.FRAGMENT_METHODS.get(nameDesc);
            List<Object> localIds = new ArrayList<>()
            Type[] types = Type.getArgumentTypes(descriptor)
//            for (int i = 1; i < cell.paramsCount; i++) {
//                int localId = newLocal(types[i - 1])
//                mv.visitVarInsn(cell.opcodes.get(i), i)
//                mv.visitVarInsn(cell.convertOpcodes(cell.opcodes.get(i)), localId)
//                localIds.add(localId)
//            }
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitVarInsn(Opcodes.ALOAD, 1)
            mv.visitVarInsn(Opcodes.ALOAD, 2)
            return new TrackFragmentMethodVisitor(mv, access, name, descriptor, className, localIds, cell);
        }

        if ("androidx/appcompat/app/AppCompatActivity".equals(superName)) {
            if (name.startsWith("onCreate")) {
                // 处理onCreate方法
                return new TrackMethodVisitor(mv, className, name);
            }
        }
        return mv;
    }


    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
