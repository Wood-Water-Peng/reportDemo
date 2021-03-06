package com.example.reportdemo;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @Author jacky.peng
 * @Date 2021/4/16 10:07 AM
 * @Version 1.0
 */
public class TrackMethodVisitor extends MethodVisitor {
    private String className;
    private String methodName;


    public TrackMethodVisitor( MethodVisitor methodVisitor, String className, String methodName) {
        super(Opcodes.ASM6, methodVisitor);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("MethodVisitor visitCode ----------------");
        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className + "----->" + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }
}
