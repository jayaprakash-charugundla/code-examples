package com.jc.java.assist.classloading;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class ReflectionExample {
    public static void main(String[] args) throws Exception {
        Class clazz = Car.class;
        //printMethods(clazz);
        //printSuperClass(clazz);
        //printConstructors(clazz);
        //printFields(clazz);
        //invokeMethod(clazz);
        settingValToField(clazz);
    }

    private static void settingValToField(Class clazz) throws NoSuchFieldException,
        IllegalAccessException, InstantiationException {
        Car car = (Car) clazz.newInstance();
        Field f = clazz.getDeclaredField("speed");
        Object value = f.get(car);
        System.out.println(value);

        car.drive('D', 33);
        value = f.get(car);
        System.out.println(value);

        f.set(car, 44);
        value = f.get(car);
        System.out.println(value);
    }

    private static void invokeMethod(Class clazz) throws NoSuchMethodException,
        IllegalAccessException, InstantiationException, InvocationTargetException {
        Car car = (Car) clazz.newInstance();
        Method method = clazz.getDeclaredMethod("drive", char.class, int.class);
        method.invoke(car, 'D', 6);

        method = clazz.getDeclaredMethod("print");
        method.invoke(null);

        method = clazz.getDeclaredMethod("accelerate");
        method.setAccessible(true);
        method.invoke(car);

    }

    private static void printFields(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            System.out.printf("%s (%s)\r\n", field.getName(), field.getType());
        }
    }

    private static void printConstructors(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor c : constructors) {
            System.out.printf("%s ", c.getName());
            System.out.printf("%d", c.getParameterCount());
            Parameter[] parameters = c.getParameters();
            for (Parameter parameter : parameters) {
                System.out.printf("%s - %s", parameter.getName(), parameter.getType());
            }
            System.out.println();
        }
    }

    private static void printSuperClass(Class clazz) {
        Class superClazz = clazz.getSuperclass();
        while (superClazz != null) {
            System.out.println(superClazz.getName());
            superClazz = superClazz.getSuperclass();
        }
    }

    private static void printMethods(Class clazz) {
        //Will return public methods
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            System.out.printf("%s", method.getName());
            int count = method.getParameterCount();
            System.out.printf(" (%d) ", count);
            System.out.println();
        }

        System.out.println();
        //Will return private methods
        methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.printf("%s", method.getName());
            int count = method.getParameterCount();
            System.out.printf(" (%d) ", count);
            System.out.println();
        }
    }
}
