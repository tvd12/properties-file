package com.tvd12.properties.file.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class ReflectionClassUtil {

    private ReflectionClassUtil() {}

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(
                "Can not create instance of classes: " + clazz.getName(),
                e
            );
        }
    }

    public static Object newInstance(
        Constructor<?> constructor,
        Object... args
    ) {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            throw new IllegalStateException(
                "Can not create instance of constructor: " + constructor,
                e
            );
        }
    }

    public static Set<Field> getValidFields(Class<?> clazz) {
        Class<?> current = clazz;
        Set<Field> answer = new HashSet<>();
        while (current != Object.class) {
            Field[] fields = current.getDeclaredFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if ((modifiers & Modifier.STATIC) != 0) {
                    continue;
                }
                answer.add(field);
            }
            current = current.getSuperclass();
        }
        return answer;
    }

    public static Set<Field> getFieldsWithAnnotations(
        Class<?> clazz,
        List<Class> annotationClasses
    ) {
        Set<Field> answer = new HashSet<>();
        for (Class annotationClass : annotationClasses) {
            answer.addAll(getFieldsWithAnnotation(clazz, annotationClass));
        }
        return answer;
    }

    public static Set<Field> getFieldsWithAnnotation(
        Class<?> clazz,
        Class<? extends Annotation> annotationClass
    ) {
        Set<Class> allClasses = new HashSet<>();
        Set<Class> superClasses = flatSuperClasses(clazz);
        allClasses.add(clazz);
        allClasses.addAll(superClasses);
        Set<Field> answer = new HashSet<>();
        for (Class item : allClasses) {
            Field[] fields = item.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(annotationClass)) {
                    answer.add(field);
                }
            }
        }
        return answer;
    }

    public static Set<Method> getPublicMethods(Class<?> clazz) {
        Class<?> current = clazz;
        Set<Method> answer = new HashSet<>();
        while (current != Object.class) {
            Method[] methods = current.getDeclaredMethods();
            for (Method method : methods) {
                int modifiers = method.getModifiers();
                if ((modifiers & Modifier.PUBLIC) == 0) {
                    continue;
                }
                if ((modifiers & Modifier.ABSTRACT) != 0) {
                    continue;
                }
                answer.add(method);
            }
            current = current.getSuperclass();
        }
        return answer;
    }

    public static Set<Method> getMethodsWithAnnotations(
        Class<?> clazz,
        List<Class> annotationClasses
    ) {
        Set<Method> answer = new HashSet<>();
        for (Class annotationClass : annotationClasses) {
            answer.addAll(getMethodsWithAnnotation(clazz, annotationClass));
        }
        return answer;
    }

    public static Set<Method> getMethodsWithAnnotation(
        Class<?> clazz,
        Class<? extends Annotation> annotationClass
    ) {
        Set<Class> allClasses = new HashSet<>();
        Set<Class> superAndInterfaceClasses = flatSuperAndInterfaceClasses(clazz);
        allClasses.add(clazz);
        allClasses.addAll(superAndInterfaceClasses);
        Set<Method> answer = new HashSet<>();
        for (Class item : allClasses) {
            Method[] methods = item.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(annotationClass)) {
                    answer.add(method);
                }
            }
        }
        return answer;
    }

    public static Set<Class> flatSuperClasses(Class clazz) {
        return flatSuperClasses(clazz, false);
    }

    public static Set<Class> flatSuperClasses(
        Class clazz,
        boolean includeObject
    ) {
        Set<Class> classes = new HashSet<>();
        Class superClass = clazz.getSuperclass();
        while (superClass != null) {
            if (superClass.equals(Object.class) && !includeObject) {
                break;
            }
            classes.add(superClass);
            superClass = superClass.getSuperclass();
        }
        return classes;
    }

    public static Set<Class> flatInterfaces(Class clazz) {
        Class[] interfaces = clazz.getInterfaces();
        Set<Class> classes = new HashSet<>(Arrays.asList(interfaces));
        for (Class itf : interfaces) {
            classes.addAll(flatInterfaces(itf));
        }
        return classes;
    }

    public static Set<Class> flatSuperAndInterfaceClasses(Class clazz) {
        return flatSuperAndInterfaceClasses(clazz, false);
    }

    public static Set<Class> flatSuperAndInterfaceClasses(
        Class clazz,
        boolean includeObject
    ) {
        Set<Class> interfaces = flatInterfaces(clazz);
        Set<Class> superClasses = flatSuperClasses(clazz, includeObject);
        Set<Class> classes = new HashSet<>(interfaces);
        for (Class superClass : superClasses) {
            Set<Class> superAndInterfaceClasses = flatSuperAndInterfaceClasses(superClass, includeObject);
            classes.add(superClass);
            classes.addAll(superAndInterfaceClasses);
        }
        return classes;
    }

    public static Constructor getNoArgsDeclaredConstructor(Class clazz) {
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }

    public static Constructor getMaxArgsDeclaredConstructor(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Constructor max = constructors[0];
        for (int i = 1; i < constructors.length; ++i) {
            if (constructors[i].getParameterCount() > max.getParameterCount()) {
                max = constructors[i];
            }
        }
        return max;
    }
}
