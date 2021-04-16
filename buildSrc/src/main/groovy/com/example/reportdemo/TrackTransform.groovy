package com.example.reportdemo

import com.android.build.api.transform.*
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import com.android.build.gradle.internal.pipeline.TransformManager
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import groovy.io.FileType

class TrackTransform extends Transform {
    private static final String TAG = "TrackTransform";

    @Override
    String getName() {
        return TAG
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY;
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        long startTime = System.currentTimeMillis()
        Collection<TransformInput> transformInputs = transformInvocation.getInputs();
        TransformOutputProvider outputProvider= transformInvocation.getOutputProvider();
        transformInputs.each { TransformInput input ->
            //遍历 jar
//            input.jarInputs.each { JarInput jarInput ->
//                forEachJar(transformInvocation.incremental, jarInput,outputProvider, transformInvocation.context)
//            }

            //遍历目录
            input.directoryInputs.each { DirectoryInput directoryInput ->
                forEachDirectory(directoryInput, outputProvider)
            }
        }

        println("[SensorsAnalytics]: 此次编译共耗时:${System.currentTimeMillis() - startTime}毫秒")

    }

    void forEachDirectory(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        File dir = directoryInput.file
        File dest = outputProvider.getContentLocation(directoryInput.getName(),
                directoryInput.getContentTypes(), directoryInput.getScopes(),
                Format.DIRECTORY)
//        FileUtils.forceMkdir(dest)
        String srcDirPath = dir.absolutePath
        String destDirPath = dest.absolutePath
//        FileUtils.copyDirectory(dir, dest)
        println "srcDir:${dir}, desDir:${dest}"
        //遍历目录中的所有.class文件
        if (dir) {
            dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) { File file ->
                System.out.println("find class: " + file.name)

                ClassReader classReader = new ClassReader(file.bytes)

                // 对class文件的写入
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                // 访问class文件相应的内容，解析到某一个结构就会通知到ClassVisitor的相应方法
                ClassVisitor classVisitor = new TrackClassVisitor(classWriter)
                // 依次调用ClassVisitor接口的各个方法
                classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                // toByteArray方法会将最终修改的字节码以byte数组形式返回
                byte[] bytes = classWriter.toByteArray()
                // 通过文件流写入方式覆盖掉原先的内容，实现class文件的改写
                FileOutputStream outputStream = new FileOutputStream(file.path)
                outputStream.write(bytes)
                outputStream.close()
            }
        }
        // 处理完传输文件后，把输出传给下一个文件
        FileUtils.copyDirectory(dir, dest)

    }

    void forEachJar(boolean isIncremental, JarInput jarInput, TransformOutputProvider outputProvider, Context context) {
        String destName = jarInput.file.name
        //截取文件路径的 md5 值重命名输出文件，因为可能同名，会覆盖
        def hexName = DigestUtils.md5Hex(jarInput.file.absolutePath).substring(0, 8)
        if (destName.endsWith(".jar")) {
            destName = destName.substring(0, destName.length() - 4)
        }
        //获得输出文件
        File destFile = outputProvider.getContentLocation(destName + "_" + hexName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
        transformJar(destFile, jarInput, context)
    }

    void transformJar(File dest, JarInput jarInput, Context context) {
        def modifiedJar = null
        println("开始遍历 jar：" + jarInput.file.absolutePath)
//        modifiedJar = modifyJarFile(jarInput.file, context.getTemporaryDir())
//        println("结束遍历 jar：" + jarInput.file.absolutePath)
        if (modifiedJar == null) {
            modifiedJar = jarInput.file
        }
        FileUtils.copyFile(modifiedJar, dest)
    }
}