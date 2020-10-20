package com.cwf.resource.jvm;

import java.io.FileInputStream;

public class MyClassLoader extends ClassLoader {
    // 自定义加载路径
    private String classPath;
    //构造方法，初始化加载路径
    public MyClassLoader( String classPath) {
        this.classPath = classPath;
    }

    /*
    在自定义路径下，加载字节码文件到内存中
     */
    private byte[] loadByte(String className) throws Exception{
        className=className.replaceAll("\\.","/");
        FileInputStream fis=new FileInputStream(classPath+"/"+className+".class");
        int len=fis.available();
        byte[] data= new byte[len];
        fis.read(data);
        fis.close();
        return data;
    }

    /*
    重写findClass方法
     */
    protected Class<?> findClass(String name)throws ClassNotFoundException {
        try {
            byte[] data = loadByte(name);
            //defineClass这个方法会将一个字节数组转为Class对象。
            return defineClass(name, data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    /*
    重写loadClass 打破双亲委派机制
     */
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException
    {
        synchronized (getClassLoadingLock(name)) {
            // First, check if the class has already been loaded
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                long t0 = System.nanoTime();
                // If still not found, then invoke findClass in order
                // to find the class.
                long t1 = System.nanoTime();
                if(!name.contains("com.cwf.resource.jvm")){
                    c = this.getParent().loadClass(name);
                }else {
                    c = findClass(name);
                }
                // this is the defining class loader; record the stats
                sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                sun.misc.PerfCounter.getFindClasses().increment();
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    public static void main(String[] args) throws Exception {
        MyClassLoader loader = new MyClassLoader("F:\\class");
        Class<?> userClass = loader.loadClass("com.cwf.resource.jvm.User");
        System.out.println(userClass.getClassLoader().getClass().getName());
    }
}
