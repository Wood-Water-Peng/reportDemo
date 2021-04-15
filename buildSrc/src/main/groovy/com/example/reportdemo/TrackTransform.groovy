package com.example.reportdemo

import com.android.build.api.transform.Context
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Status
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

import java.util.concurrent.Callable
import java.util.logging.Logger

class TrackTransform extends Transform{
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
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        Collection<TransformInput> transformInputs = transformInvocation.getInputs();
        for (TransformInput jarInput : transformInputs) {
            //遍历 jar
            forEachJar(transformInvocation.incremental, jarInput, transformInvocation.outputProvider, transformInvocation.context)
        }
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
        if (isIncremental) {
            Status status = jarInput.getStatus()
            switch (status) {
                case Status.NOTCHANGED:
                    break
                case Status.ADDED:
                case Status.CHANGED:
                    Logger.info("jar status = $status:$destFile.absolutePath")
                    transformJar(destFile, jarInput, context)
                    break
                case Status.REMOVED:
                    Logger.info("jar status = $status:$destFile.absolutePath")
                    if (destFile.exists()) {
                        FileUtils.forceDelete(destFile)
                    }
                    break
                default:
                    break
            }
        } else {
            transformJar(destFile, jarInput, context)
        }
    }
    void transformJar(File dest, JarInput jarInput, Context context) {
        def modifiedJar = null
        if (!transformHelper.extension.disableJar || jarInput.file.absolutePath.contains('SensorsAnalyticsSDK')) {
            Logger.info("开始遍历 jar：" + jarInput.file.absolutePath)
            modifiedJar = modifyJarFile(jarInput.file, context.getTemporaryDir())
            Logger.info("结束遍历 jar：" + jarInput.file.absolutePath)
        }
        if (modifiedJar == null) {
            modifiedJar = jarInput.file
        }
        FileUtils.copyFile(modifiedJar, dest)
    }
}