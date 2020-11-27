package com.tvd12.properties.file.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public final class ReflectionClassUtils {

	private ReflectionClassUtils() {}
	
	public static Object newInstance(Class<?> clazz) {
	    try {
            return clazz.newInstance();
        } catch (Exception e) {
           throw new IllegalStateException("Can not create instance of class " + clazz, e);
        }
	}
	
	public static Set<Field> getValidFields(Class<?> clazz) {
		Class<?> current = clazz;
		Set<Field> answer = new HashSet<>();
		while(current != Object.class) {
			Field[] fields = current.getDeclaredFields();
			for(Field field : fields) {
				int modifiers = field.getModifiers();
				if((modifiers & Modifier.STATIC) != 0)
					continue;
				answer.add(field);
			}
			current = current.getSuperclass();
		}
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Field> getFieldsWithAnnotation(
			Class<?> clazz, 
			Class<? extends Annotation> annotationClass) {
		Set<Class> allClasses = new HashSet<>();
		Set<Class> superClasses = flatSuperClasses(clazz);
		allClasses.add(clazz);
		allClasses.addAll(superClasses);
		Set<Field> answer = new HashSet<>();
		for(Class item : allClasses) {
			Field[] fields = item.getDeclaredFields();
			for(Field field : fields) {
				if(field.isAnnotationPresent(annotationClass))
					answer.add(field);
			}
		}
		return answer;
	}
	
	public static Set<Method> getPublicMethods(Class<?> clazz) {
		Class<?> current = clazz;
		Set<Method> answer = new HashSet<>();
		while(current != Object.class) {
			Method[] methods = current.getDeclaredMethods();
			for(Method method : methods) {
				int modifiers = method.getModifiers();
				if((modifiers & Modifier.PUBLIC) == 0)
					continue;
				if((modifiers & Modifier.ABSTRACT) != 0)
					continue;
				answer.add(method);
			}
			current = current.getSuperclass();
		}
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Method> getMethodsWithAnnotation(
			Class<?> clazz, 
			Class<? extends Annotation> annotationClass) {
		Set<Class> allClasses = new HashSet<>();
		Set<Class> superAndInterfaceClasses = flatSuperAndInterfaceClasses(clazz);
		allClasses.add(clazz);
		allClasses.addAll(superAndInterfaceClasses);
		Set<Method> answer = new HashSet<>();
		for(Class item : allClasses) {
			Method[] methods = item.getDeclaredMethods();
			for(Method method : methods) {
				if(method.isAnnotationPresent(annotationClass))
					answer.add(method);
			}
		}
		return answer;
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperClasses(Class clazz) {
		return flatSuperClasses(clazz, false);
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperClasses(Class clazz, boolean includeObject) {
		Set<Class> classes = new HashSet<>();
		Class superClass = clazz.getSuperclass();
		while(superClass != null) {
			if(superClass.equals(Object.class) && !includeObject )
				break;
			classes.add(superClass);
			superClass = superClass.getSuperclass();
		}
		return classes;
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatInterfaces(Class clazz) {
		Set<Class> classes = new HashSet<>();
		Class[] interfaces = clazz.getInterfaces();
		for(Class itf : interfaces)
			classes.add(itf);
		for(Class itf : interfaces)
			classes.addAll(flatInterfaces(itf));
		return classes;
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperAndInterfaceClasses(Class clazz) {
		return flatSuperAndInterfaceClasses(clazz, false);
	}
	
	@SuppressWarnings("rawtypes")
	public static Set<Class> flatSuperAndInterfaceClasses(Class clazz, boolean includeObject) {
		Set<Class> classes = new HashSet<>();
		Set<Class> interfaces = flatInterfaces(clazz);
		Set<Class> superClasses = flatSuperClasses(clazz, includeObject);
		classes.addAll(interfaces);
		for(Class superClass : superClasses) {
			Set<Class> superAndInterfaceClasses = flatSuperAndInterfaceClasses(superClass, includeObject);
			classes.add(superClass);
			classes.addAll(superAndInterfaceClasses);
		}
		return classes;
	}
	
}
