package com.example.reportdemo


import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @Author jacky.peng* @Date 2021/4/16 10:06 AM
 * @Version 1.0*
 * 第一次遍历，用于存储一些继承关系数据
 */
class TrackFirstVisitor extends ClassVisitor {
    private String mClassName;
    private String mSuperName;


    TrackFirstVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);

    }

    //类被访问
    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.mClassName = name
        this.mSuperName = superName;
        if (mClassName != null && !(superName == "java/lang/Object")) {
            TrackUtil.childAndParents.put(mClassName, mSuperName)
            System.out.println("TrackFirstVisitor visit className:" + mClassName + "---superName:" + mSuperName);
        }
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i] == 'android/view/View$OnClickListener') {
                    System.out.println("TrackClassVisitor visit className:" + mClassName + "实现了OnClickListener");
                    hasImplOnClick = true;
                }
            }
        }

    }
    String nameDesc
    private boolean hasImplOnClick = false
    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions)
        nameDesc = name + desc
        if (hasImplOnClick) {
            System.out.println("TrackFirstVisitor visitMethod hasImplOnClick:" + nameDesc);
            if ("onClick(Landroid/view/View;)V" == nameDesc) {
                MethodVisitor onClickMethodVisitor = new TrackOnClickMethodVisitor(mv, access, name, desc, mClassName)
                return onClickMethodVisitor
            }
        }
        return mv
    }
}
