package com.balarawool.vtdemo.util;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Executor;

/**
 * This class helps with some virtual threads stuff.
 * 3 reasons not to do this in your app:
 * - It accesses a constructor of VirtualThread that is not supposed to be accessed.
 * - It uses reflection.
 * - It requires to be run with this: --add-opens=java.base/java.lang=ALL-UNNAMED
 */
public class VirtualThreadHelper {

    public static Thread createNewVirtualThread(Executor executor, Runnable runnable) {
        try {
            var clazz = Class.forName("java.lang.VirtualThread");
            var cons = clazz.getDeclaredConstructors();
            var constructor = cons[0]; // There is only one constructor

            constructor.setAccessible(true);
            return (Thread) constructor.newInstance(executor, "MyVirtualThread", 0, runnable);

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}