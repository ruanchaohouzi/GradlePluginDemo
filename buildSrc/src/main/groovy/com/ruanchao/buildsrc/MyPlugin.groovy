package  com.ruanchao.buildsrc

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin implements Plugin<Project> {

    void apply(Project project) {

        project.extensions.create('pluginSrc', Extension)
        project.task("testTask").doLast {
            System.out.println("========================");
            System.out.println("这是第二个插件!");
            System.out.println("========================");
            System.out.println("读取外界传递到gradle插件的参数:" + project.pluginSrc.message)
        }

        //注册trnsform
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(new MyTransform(project))
        //获取传入参数
        project.extensions.create('testCreateJavaConfig', MyTestClassExtension)
        //生成一个类
        if (project.plugins.hasPlugin(AppPlugin)) {
            //获取到Extension，Extension就是 build.gradle中的{}闭包
            android.applicationVariants.all { variant ->
                //获取到scope,作用域
                def variantData = variant.variantData
                def scope = variantData.scope

                //拿到build.gradle中创建的Extension的值
                def config = project.extensions.getByName("testCreateJavaConfig")

                //创建一个task
                def createTaskName = scope.getTaskName("testTask", "myTestPlugin")
                def createTask = project.task(createTaskName)
                //设置task要执行的任务
                createTask.doLast {
                    //生成java类
                    createJavaTest(variant, config)
                }
                //设置task依赖于生成BuildConfig的task，然后在生成BuildConfig后生成我们的类
                String generateBuildConfigTaskName = variant.getVariantData().getTaskContainer().getGenerateBuildConfigTask().name
                def generateBuildConfigTask = project.tasks.getByName(generateBuildConfigTaskName)
                if (generateBuildConfigTask) {
                    createTask.dependsOn generateBuildConfigTask
                    generateBuildConfigTask.finalizedBy createTask
                }
            }
        }
    }

    static void createJavaTest(variant, config) {
        //要生成的内容
        def content = """package com.ruanchao.gradleplugindemo;
                         
                        public class MyPluginTestClass {
                            public static final String str = "${config.message}";
                        }
                        """

        //获取到BuildConfig类的路径
        File outputDir = variant.getVariantData().getScope().getBuildConfigSourceOutputDir()

        def javaFile = new File(outputDir, "MyPluginTestClass.java")

        javaFile.write(content, 'UTF-8')
    }

}