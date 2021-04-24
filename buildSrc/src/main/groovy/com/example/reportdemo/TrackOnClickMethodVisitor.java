package com.example.reportdemo;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
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
//        variableID = newLocal(Type.getObjectType("java/lang/Integer"));
        //非静态方法，第0个如参数是对象的引用
        //将栈帧中本地变量表中的第1个入参加载进来
        mv.visitVarInsn(ALOAD, 1);
////        mv.visitVarInsn(ASTORE, variableID);
////        mv.visitVarInsn(ALOAD, variableID);
//
//
        mv.visitMethodInsn(Opcodes.INVOKESTATIC,  TrackHookConfig.TRACK_API, "onViewClicked", "(Landroid/view/View;)V", false);
//        mv.visitInsn(Opcodes.POP);
        //
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
