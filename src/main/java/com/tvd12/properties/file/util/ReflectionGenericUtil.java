package com.tvd12.properties.file.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public final class ReflectionGenericUtil {

	private ReflectionGenericUtil() {}
	
	public static Class[] getTwoGenericClassArguments(Type genericType)  {
		return getGenericClassArguments(genericType, 2);
	}
	
	public static Class[] getGenericClassArguments(Type genericType, int size)  {
		if (!(genericType instanceof ParameterizedType))
			return new Class[size];
		List<Class> answer = new ArrayList<>();
		Type[] types = ((ParameterizedType)genericType).getActualTypeArguments();
		if(types.length != size)
			return new Class[size];
		for(Type type : types) {
			if(type instanceof Class)
				answer.add((Class)type);
			else
				answer.add(null);
		}
		return answer.toArray(new Class[answer.size()]);
	}
}
