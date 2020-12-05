package com.tvd12.properties.file.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@SuppressWarnings("rawtypes")
public class PropertyAnnotations {

	@Getter
	private final List<Class> annotationClasses;
	private final Map<Class<?>, PropertyAnnotation> annotations;
	
	public PropertyAnnotations() {
		this.annotations = new HashMap<>();
		this.annotationClasses = new ArrayList<>();
		this.addPropertyAnnotation(new PropertyAnnotation(
				Property.class, 
				a -> ((Property)a).value(),
				a -> ((Property)a).prefix()
			)
		);
	}
	

	@SuppressWarnings("unchecked")
	public String getPropertyName(Method method) {
		for(Class annotationClass : annotationClasses) {
			Annotation annotation = method.getAnnotation(annotationClass);
			if(annotation != null) {
				PropertyAnnotation ann = annotations.get(annotationClass);
				return ann.getPropertyName(annotation);
			}
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public String getPropertyPrefix(Method method) {
		for(Class annotationClass : annotationClasses) {
			Annotation annotation = method.getAnnotation(annotationClass);
			if(annotation != null) {
				PropertyAnnotation ann = annotations.get(annotationClass);
				return ann.getPropertyPrefix(annotation);
			}
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public String getPropertyName(Field field) {
		for(Class annotationClass : annotationClasses) {
			Annotation annotation = field.getAnnotation(annotationClass);
			if(annotation != null) {
				PropertyAnnotation ann = annotations.get(annotationClass);
				return ann.getPropertyName(annotation);
			}
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public String getPropertyPrefix(Field field) {
		for(Class annotationClass : annotationClasses) {
			Annotation annotation = field.getAnnotation(annotationClass);
			if(annotation != null) {
				PropertyAnnotation ann = annotations.get(annotationClass);
				return ann.getPropertyPrefix(annotation);
			}
		}
		return "";
	}


	public void addPropertyAnnotation(PropertyAnnotation annotation) {
		if(annotations.containsKey(annotation.getAnnotationClass()))
			return;
		annotationClasses.add(annotation.getAnnotationClass());
		annotations.put(annotation.getAnnotationClass(), annotation);
	}
	
}
