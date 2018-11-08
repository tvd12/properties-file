package com.tvd12.properties.file.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public final class ReflectionClassUtils {

	private ReflectionClassUtils() {
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
	
}
