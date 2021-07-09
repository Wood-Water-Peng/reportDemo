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
        nameDesc = name + descriptor;
        //如果该方法为接口中的方法
        if (TrackUtil.isInstanceOfFragment(mSuperName) && TrackHookConfig.FRAGMENT_METHODS.containsKey(nameDesc)) {
            System.out.println("TrackClassVisitor isInstanceOfFragment className:" + mClassName + "---superName:" + mSuperName + "---nameDesc:" + nameDesc);
            //fragment的有效方法
            return new TrackFragmentMethodVisitor(mv, access, name, descriptor, mClassName)
        }


        if (TrackUtil.isInstanceOfActivity(mSuperName) && mClassName == TrackHookConfig.TRACK_BASE_REPORT_ACTIVITY && TrackHookConfig.ACTIVITY_METHODS.containsKey(nameDesc)) {
            System.out.println("TrackClassVisitor isInstanceOfActivity className:" + mClassName + "---superName:" + mSuperName + "---nameDesc:" + nameDesc);
            //fragment的有效方法
            return new TrackActivityMethodVisitor(mv, access, name, descriptor, mClassName)
        }


        return mv
    }


    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
