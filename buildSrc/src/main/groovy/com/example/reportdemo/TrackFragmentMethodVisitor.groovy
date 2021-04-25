package com.example.reportdemo

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * @Author jacky.peng* @Date 2021/4/16 10:07 AM
 * @Version 1.0
 */
class TrackFragmentMethodVisitor extends AdviceAdapter {
    private String className;
    private String methodName;
    private List<Object> variableID;
    private TrackMethodCell cell;
    String nameDesc

    TrackFragmentMethodVisitor(MethodVisitor mv, int access, String name, String desc, String className) {
        super(Opcodes.ASM6, mv, access, name, desc);
        this.className = className;
        this.methodName = name;
        this.nameDesc = name + desc
        cell = TrackHookConfig.FRAGMENT_METHODS.get(nameDesc)
//        variableID = new ArrayList<>()
//        Type[] types = Type.getArgumentTypes(desc)
//        for (int i = 1; i < cell.paramsCount; i++) {
//            int localId = newLocal(types[i - 1])
//            mv.visitVarInsn(cell.opcodes.get(i), i)
//            mv.visitVarInsn(TrackUtil.convertOpcodes(cell.opcodes.get(i)), localId)
//        }
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        //希望调用
        mv.visitVarInsn(ALOAD, 0);
        for (int i = 1; i < cell.paramsCount; i++) {
            mv.visitVarInsn(cell.opcodes.get(i), i);
        }
        System.out.println("MethodVisitor onMethodEnter ----------------name:" + className + "--agentName:" + cell.agentName + "--agentDesc:" + cell.agentDesc);
        mv.visitMethodInsn(INVOKESTATIC, TrackHookConfig.TRACK_API, cell.agentName, cell.agentDesc, false);


        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className + "======>" + methodName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }

    @Override
    void visitCode() {
        super.visitCode();
    }
}
