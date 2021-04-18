package com.example.reportdemo;

import org.gradle.internal.impldep.aQute.bnd.osgi.OpCodes;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @Author jacky.peng
 * @Date 2021/4/16 10:06 AM
 * @Version 1.0
 */
public class TrackClassVisitor extends ClassVisitor {
    private String className;
    private String superName;
    private String[] interfaces;
    private boolean hasImplOnClick = false;

    public TrackClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM6, classVisitor);

    }

    //类被访问
    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.className = name;
        this.superName = superName;
        //获取类所实现的所有接口
        this.interfaces = interfaces;
        if (interfaces != null) {
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i].equals("android/view/View$OnClickListener")) {
                    System.out.println("TrackClassVisitor visit className:" + className + "实现了OnClickListener");
                    hasImplOnClick = true;
                }
            }
        }
    }

    String nameDesc;

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        //'onClick(Landroid/view/View;)V'
        nameDesc = name + descriptor;
        System.out.println("TrackClassVisitor visitMethod className:" + className + "---methodName:" + name);

        //如果该方法为接口中的方法
        if (hasImplOnClick) {
            System.out.println("TrackClassVisitor visitMethod hasImplOnClick:" + name);
            if ("onClick(Landroid/view/View;)V".equals(nameDesc)) {
                return new TrackOnClickMethodVisitor(mv, access, name, descriptor, className);
            }
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
