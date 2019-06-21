package com.ruanchao.buildsrc

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project

/**
 * 利用javassist实现代码的注入，创建MyInjects.groovy类，
 * 在这个类中我们传入了两个参数，一个是当前变量的文件夹，一个是当前的工程对象
 */
class MyInjects {
    //初始化类池
    private final static ClassPool pool = ClassPool.getDefault()

    static void inject(String path,Project project) {
        //将当前路径加入类池,不然找不到这个类
        pool.appendClassPath(path)
        //project.android.bootClasspath 加入android.jar，不然找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        //引入android.os.Bundle包，因为onCreate方法参数有Bundle
        pool.importPackage("android.os.Bundle")

        File dir = new File(path)
        if (dir.isDirectory()) {
            //遍历文件夹
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath
                println("filePath = " + filePath)
                if ("MainActivity.class".equals(file.getName())) {

                    //获取MainActivity.class
                    CtClass ctClass = pool.getCtClass("com.ruanchao.gradleplugindemo.MainActivity")
                    println("ctClass = " + ctClass)
                    //解冻
                    if (ctClass.isFrozen())
                        ctClass.defrost()

                    //获取到OnCreate方法
                    CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")

                    println("方法名 = " + ctMethod)

                    String insetBeforeStr = """ android.widget.Toast.makeText(this,"我是被插入的Toast代码~!!",android.widget.Toast.LENGTH_SHORT).show();
                                                """
                    //在方法开头插入代码
                    ctMethod.insertBefore(insetBeforeStr)
                    ctClass.writeFile(path)
                    ctClass.detach()//释放
                }
            }
        }

    }
}