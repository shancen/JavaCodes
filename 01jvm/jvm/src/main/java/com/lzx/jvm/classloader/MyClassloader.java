package com.lzx.jvm.classloader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class MyClassloader extends ClassLoader {


    public static void main(String[] args) throws Exception {
        String name = "Hello";
        Class<?> xClass = new MyClassloader().findClass(name);
        Method[] declaredMethods = xClass.getDeclaredMethods();
        Object o = xClass.newInstance();
        for (Method method : declaredMethods) {
            System.out.println("=============" + method);
            method.invoke(o);
        }
    }

    @Override
    public Class<?> findClass(String name) {
        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(name + ".xlass")) {
            // inputStream 转byte
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.getClass();
    }
}
