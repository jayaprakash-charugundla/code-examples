package com.jc.java.assist.classloading;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class SimpleClassLoader {
    public static void main(String[] args) {
        URL url;
        try {
            url = new URL("file:////C:/Workspace/Updated/java-assist/lib/quoter.jar");
            URLClassLoader ucl = new URLClassLoader(new URL[]{url});
            Class clazz = ucl.loadClass("com.mantiso.Quote");
            Object o = clazz.newInstance();
            System.out.println(o.toString());
        } catch (MalformedURLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }
}
