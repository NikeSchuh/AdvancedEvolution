package de.philipp.advancedevolution.nms;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;

public final class ReflectionUtil {
    private static String version;

    private static int major;

    private static int minor;

    private static int r;

    private static final Map<String, Class<?>> nmsClasses = new HashMap<>();

    private static final Map<String, Class<?>> ocbClasses = new HashMap<>();

    private static final Map<Class<?>, Map<String, Class<?>>> declaredClasses = new HashMap<>();

    private static final Map<Class<?>, Map<String, Method>> cachedMethods = new HashMap<>();

    public static String getVersion() {
        if (version == null) {
            String declaration = Bukkit.getServer().getClass().getPackage().getName();
            version = declaration.substring(declaration.lastIndexOf('.') + 1);
            String[] pts = version.substring(1).split("_");
            major = Integer.parseInt(pts[0]);
            minor = Integer.parseInt(pts[1]);
            r = Integer.parseInt(pts[2].substring(1));
            version += '.';
        }
        return version;
    }

    public static int getMajor() {
        if (version == null)
            getVersion();
        return major;
    }

    public static int getMinor() {
        if (version == null)
            getVersion();
        return minor;
    }

    public static int getR() {
        if (version == null)
            getVersion();
        return r;
    }

    public static Class<?> getNMSClass(String localPackage) {
        Class<?> clazz;
        if (nmsClasses.containsKey(localPackage))
            return nmsClasses.get(localPackage);
        String declaration = "net.minecraft.server." + getVersion() + localPackage;
        try {
            clazz = Class.forName(declaration);
        } catch (Throwable e) {
            e.printStackTrace();
            return nmsClasses.put(localPackage, null);
        }
        nmsClasses.put(localPackage, clazz);
        return clazz;
    }

    public static Class<?> getOCBClass(String localPackage) {
        Class<?> clazz;
        if (ocbClasses.containsKey(localPackage))
            return ocbClasses.get(localPackage);
        String declaration = "org.bukkit.craftbukkit." + getVersion() + localPackage;
        try {
            clazz = Class.forName(declaration);
        } catch (Throwable e) {
            e.printStackTrace();
            return ocbClasses.put(localPackage, null);
        }
        ocbClasses.put(localPackage, clazz);
        return clazz;
    }

    public static Class<?> getDeclaredClass(Class<?> origin, String className) {
        if (!declaredClasses.containsKey(origin))
            declaredClasses.put(origin, new HashMap<>());
        Map<String, Class<?>> classMap = declaredClasses.get(origin);
        if (classMap.containsKey(className))
            return classMap.get(className);
        for (Class<?> clazz : origin.getDeclaredClasses()) {
            if (clazz.getSimpleName().equals(className)) {
                classMap.put(className, clazz);
                declaredClasses.put(origin, classMap);
                return clazz;
            }
        }
        classMap.put(className, null);
        declaredClasses.put(origin, classMap);
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!cachedMethods.containsKey(clazz))
            cachedMethods.put(clazz, new HashMap<>());
        Map<String, Method> methods = cachedMethods.get(clazz);
        if (methods.containsKey(methodName))
            return methods.get(methodName);
        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            cachedMethods.put(clazz, methods);
            return method;
        } catch (Throwable e) {
            e.printStackTrace();
            methods.put(methodName, null);
            cachedMethods.put(clazz, methods);
            return null;
        }
    }
}