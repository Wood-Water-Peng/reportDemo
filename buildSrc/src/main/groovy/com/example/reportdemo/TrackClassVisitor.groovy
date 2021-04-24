package com.example.reportdemo

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * @Author jacky.peng* @Date 2021/4/16 10:06 AM
 * @Version 1.0
 */
class TrackClassVisitor extends ClassVisitor {
    private String mClassName;
    private String mSuperName;
    private String[] interfaces;
    private boolean hasImplOnClick = false;


    TrackClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);

    }

    //类被访问
    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.mClassName = name;
        this.mSuperName = superName;
        //获取类所实现的所有接口
        this.interfaces = interfaces;
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i] == 'android/view/View$OnClickListener') {
                    System.out.println("TrackClassVisitor visit className:" + mClassName + "实现了OnClickListener");
                    hasImplOnClick = true;
                }
            }
        }
        String topSuperName = TrackUtil.findTopParent(mClassName)
        if (topSuperName != null) {
            this.mSuperName = topSuperName;
        }
        System.out.println("TrackClassVisitor visit className:" + mClassName + "---superName:" + mSuperName);

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
//        if (hasImplOnClick) {
//            System.out.println("TrackClassVisitor visitMethod hasImplOnClick:" + name);
//            if ("onClick(Landroid/view/View;)V".equals(nameDesc)) {
//                MethodVisitor onClickMethodVisitor = new TrackOnClickMethodVisitor(mv, access, name, descriptor, mClassName)
////                mv.visitVarInsn(Opcodes.ALOAD, 1)
//                return onClickMethodVisitor;
//            }
//        } else
        if (TrackUtil.isInstanceOfFragment(mSuperName) && TrackHookConfig.FRAGMENT_METHODS.containsKey(nameDesc)) {
            System.out.println("TrackClassVisitor isInstanceOfFragment className:" + mClassName + "---superName:" + mSuperName + "---nameDesc:" + nameDesc);
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

            return new TrackFragmentMethodVisitor(mv, access, name, descriptor, mClassName, localIds, cell);
        }

//        if ("androidx/appcompat/app/AppCompatActivity".equals(mSuperName)) {
//            if (name.startsWith("onCreate")) {
//                // 处理onCreate方法
//                return new TrackMethodVisitor(mv, mClassName, name);
//            }
//        }
        return mv;
    }


    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
