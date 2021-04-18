package com.example.reportdemo


import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * @Author jacky.peng* @Date 2021/4/16 10:06 AM
 * @Version 1.0*
 * 第一次遍历，用于存储一些继承关系数据
 */
class TrackFirstVisitor extends ClassVisitor {
    private String className;
    private String superName;


    TrackFirstVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);

    }

    //类被访问
    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
        if (className != null && !superName.equals("java/lang/Object")) {
            TrackUtil.childAndParents.put(className, superName)
            System.out.println("TrackFirstVisitor visit className:" + className + "---superName:" + superName);
        }
    }


}
