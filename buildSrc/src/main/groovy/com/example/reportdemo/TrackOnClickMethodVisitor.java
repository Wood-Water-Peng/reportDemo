package com.example.reportdemo;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @Author jacky.peng
 * @Date 2021/4/16 10:07 AM
 * @Version 1.0
 */
public class TrackOnClickMethodVisitor extends AdviceAdapter {
    private String className;
    private String methodName;
    private int variableID;


    public TrackOnClickMethodVisitor(MethodVisitor mv, int access, String name, String desc, String className) {
        super(Opcodes.ASM6, mv, access, name, desc);
        this.className = className;
        this.methodName = name;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        //希望调用
        mv.visitVarInsn(ALOAD, 1);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,  TrackHookConfig.TRACK_API, "onViewClicked", "(Landroid/view/View;)V", false);


        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className + "======>" + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }

}
