package com.ruanchao.buildsrc;

import com.android.build.api.transform.*;
import com.android.build.gradle.internal.pipeline.TransformManager;

import org.gradle.api.Project;

import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException;
import java.util.Set;

/**
 * Created by yuzhenbei on 2018/7/15 08:54
 * <p>
 * Email aoaoyi.com@gmail.com
 */
public class MyTransform extends Transform {

    private Project mProject;

    public MyTransform(Project p) {
        this.mProject = p;
    }

    /**
     * transform的名称
     * transformClassesWithMyClassTransformForDebug 运行时的名字
     * transformClassesWith + getName() + For + Debug或Release
     *
     * @return String
     */
    @Override
    public String getName() {
        return "MyTransform";
    }

    /**
     * 需要处理的数据类型，有两种枚举类型
     * CLASSES和RESOURCES，CLASSES代表处理的java的class文件，RESOURCES代表要处理java的资源
     *
     * @return
     */
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    /**
     * 指Transform要操作内容的范围，官方文档Scope有7种类型：
     * EXTERNAL_LIBRARIES        只有外部库
     * PROJECT                   只有项目内容
     * PROJECT_LOCAL_DEPS        只有项目的本地依赖(本地jar)
     * PROVIDED_ONLY             只提供本地或远程依赖项
     * SUB_PROJECTS              只有子项目。
     * SUB_PROJECTS_LOCAL_DEPS   只有子项目的本地依赖项(本地jar)。
     * TESTED_CODE               由当前变量(包括依赖项)测试的代码
     *
     * Returns the scope(s) of the Transform. This indicates which scopes the transform consumes.
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    /**
     * 指明当前Transform是否支持增量编译
     * If it does, then the TransformInput may contain a list of changed/removed/added files, unless
     * something else triggers a non incremental run.
     */
    @Override
    public boolean isIncremental() {
        return false;
    }

    /**
     * Transform中的核心方法
     * transformInvocation.getInputs() 中是传过来的输入流，其中有两种格式，一种是jar包格式一种是目录格式。
     * transformInvocation.getOutputProvider() 获取到输出目录，最后将修改的文件复制到输出目录，这一步必须做不然编译会报错
     *
     * @param transformInvocation
     * @throws TransformException
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation);

//        Context context = transformInvocation.getContext()
//        Collection inputs = transformInvocation.getInputs()
//        Collection referenceInputs = transformInvocation.getReferencedInputs()
//        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
//        boolean isIncremental = transformInvocation.isIncremental()
//        outputProvider.deleteAll()
//        inputs.each {
//            TransformInput input ->
//                input.directoryInputs.each {
//                    DirectoryInput directoryInput ->
//                        //注入代码
//                        MyInjects.inject(directoryInput.file.absolutePath, mProject)
//                        // 获取output目录
//                        def dest = outputProvider.getContentLocation(directoryInput.name,
//                                directoryInput.contentTypes, directoryInput.scopes,
//                                Format.DIRECTORY)
//
//                        println("" + directoryInput.file + " transform" + dest);
//                        // 将input的目录复制到output指定目录
//                        FileUtils.copyFile(directoryInput.file, dest)
//                }
//
//                input.jarInputs.each {
//                    JarInput jarInput ->
//                        //jar文件一般是第三方依赖库jar文件
//
//                        // 重命名输出文件（同目录copyFile会冲突）
//                        def jarName = jarInput.name
//                        def md5Name = md5Hex(jarInput.file.getAbsolutePath())
//                        if (jarName.endsWith(".jar")) {
//                            jarName = jarName.substring(0, jarName.length() - 4)
//                        }
//                        //生成输出路径
//                        def dest = outputProvider.getContentLocation(jarName + md5Name,
//                                jarInput.contentTypes, jarInput.scopes, Format.JAR)
//
//                        println("jar " + jarInput.file + " transform " + dest)
//                        //将输入内容复制到输出
//                        FileUtils.copyFile(jarInput.file, dest)
//                }
//
//        }

    }

    public static String md5Hex(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

}