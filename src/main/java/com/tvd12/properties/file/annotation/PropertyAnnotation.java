package com.tvd12.properties.file.annotation;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import lombok.Getter;

public class PropertyAnnotation {
	
	@Getter
	private final Class<?> annotationClass;
	private final Function<Annotation, String> propertyNameFetcher;
	private final Function<Annotation, String> propertyPrefixFetcher;
	
	public PropertyAnnotation(
			Class<?> annotationClass,
			Function<Annotation, String> propertyNameFetcher,
			Function<Annotation, String> propertyPrefixFetcher) {
		this.annotationClass = annotationClass;
		this.propertyNameFetcher = propertyNameFetcher;
		this.propertyPrefixFetcher = propertyPrefixFetcher;
	}
	
	public String getPropertyName(Annotation annotation) {
		return propertyNameFetcher.apply(annotation);
	}
	
	public String getPropertyPrefix(Annotation annotation) {
		return propertyPrefixFetcher.apply(annotation);
	}
	
}
