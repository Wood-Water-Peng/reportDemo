package com.example.reportdemo

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.io.FileType
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class FirstTransform extends Transform {
    private static final String TAG = "FirstTransform";

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
        return TransformManager.SCOPE_FULL_PROJECT;
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
//            遍历 jar
            input.jarInputs.each { JarInput jarInput ->
                forEachJar(jarInput,outputProvider, transformInvocation.context)
            }

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
                ClassVisitor firstVisitor=new TrackFirstVisitor(classWriter)
//                ClassVisitor classVisitor = new TrackClassVisitor(firstVisitor)
                // 依次调用ClassVisitor接口的各个方法
                classReader.accept(firstVisitor, ClassReader.EXPAND_FRAMES)
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

    void forEachJar(JarInput jarInput, TransformOutputProvider outputProvider, Context context) {
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
        modifiedJar = modifyJarFile(jarInput.file, context.getTemporaryDir())
        println("结束遍历 jar：" + jarInput.file.absolutePath)
        if (modifiedJar == null) {
            modifiedJar = jarInput.file
        }
        FileUtils.copyFile(modifiedJar, dest)
    }

    /**
     * 修改 jar 文件中对应字节码
     */
    private File modifyJarFile(File jarFile, File tempDir) {
        if (jarFile) {
            return modifyJar(jarFile, tempDir, true)

        }
        return null
    }

    private File modifyJar(File jarFile, File tempDir, boolean isNameHex) {
        //FIX: ZipException: zip file is empty
        if (jarFile == null || jarFile.length() == 0) {
            return null
        }
        //取原 jar, verify 参数传 false, 代表对 jar 包不进行签名校验
        def file = new JarFile(jarFile, false)
        //设置输出到的 jar
        def tmpNameHex = ""
        if (isNameHex) {
            tmpNameHex = DigestUtils.md5Hex(jarFile.absolutePath).substring(0, 8)
        }
        def outputJar = new File(tempDir, tmpNameHex + jarFile.name)
        JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(outputJar))
        Enumeration enumeration = file.entries()

        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            InputStream inputStream
            try {
                inputStream = file.getInputStream(jarEntry)
            } catch (Exception e) {
                IOUtils.closeQuietly(inputStream)
                e.printStackTrace()
                return null
            }
            String entryName = jarEntry.getName()
            if (entryName.endsWith(".DSA") || entryName.endsWith(".SF")) {
                //ignore
            } else {
                String className
                JarEntry entry = new JarEntry(entryName)
                byte[] modifiedClassBytes = null
                byte[] sourceClassBytes
                try {
                    jarOutputStream.putNextEntry(entry)
                    sourceClassBytes = TrackUtil.toByteArrayAndAutoCloseStream(inputStream)
                } catch (Exception e) {
                    Logger.error("Exception encountered while processing jar: " + jarFile.getAbsolutePath())
                    IOUtils.closeQuietly(file)
                    IOUtils.closeQuietly(jarOutputStream)
                    e.printStackTrace()
                    return null
                }
                if (!jarEntry.isDirectory() && entryName.endsWith(".class")) {
                    className = entryName.replace("/", ".").replace(".class", "")
//                    ClassNameAnalytics classNameAnalytics = transformHelper.analytics(className)
//                    if (classNameAnalytics.isShouldModify) {
                        modifiedClassBytes = modifyClass(sourceClassBytes, className)
//                    }
                }
                if (modifiedClassBytes == null) {
                    jarOutputStream.write(sourceClassBytes)
                } else {
                    jarOutputStream.write(modifiedClassBytes)
                }
                jarOutputStream.closeEntry()
            }
        }
        jarOutputStream.close()
        file.close()
        return outputJar
    }
    /**
     * 真正修改类中方法字节码
     */
    private byte[] modifyClass(byte[] srcClass, String className) {
        try {
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS)
            ClassVisitor firstVisitor=new TrackFirstVisitor(classWriter)
//            ClassVisitor classVisitor = new TrackClassVisitor(firstVisitor)
            ClassReader cr = new ClassReader(srcClass)
            cr.accept(firstVisitor, ClassReader.EXPAND_FRAMES + ClassReader.SKIP_FRAMES)
            return classWriter.toByteArray()
        } catch (Exception ex) {
            Logger.error("$className 类执行 modifyClass 方法出现异常")
            ex.printStackTrace()
            return srcClass
        }
    }
}