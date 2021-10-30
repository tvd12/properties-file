package com.tvd12.properties.file.struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.mapping.MappingLevel;

import lombok.Getter;

/**
 * 
 * Holds all SetterMethod objects
 * 
 * @author tavandung12
 *
 */

@Getter
public class ClassWrapper extends ClassStruct {
    
    /**
     * Construct with java class
     * 
     * @param clazz java class
     * @param mappingLevel the mapping level
     * @param propertyAnnotations the properties annotation filter
     */
	public ClassWrapper(
			Class<?> clazz, 
			MappingLevel mappingLevel,
			PropertyAnnotations propertyAnnotations) {
        super(clazz, mappingLevel, propertyAnnotations);
    }
	
	/**
	 * Initialize SetterMethod object with java field 
	 */
	@Override
    protected MethodStruct initWithField(Field field) {
        MethodStruct cover = new SetterMethod(propertyAnnotations);
        cover.initWithField(clazz, field);
        return cover;
    }

	/**
	 * Initialize SetterMethod object with java method
	 */
    @Override
    protected MethodStruct initWithMethod(Method method) {
        MethodStruct cover = new SetterMethod(propertyAnnotations);
        cover.initWithMethod(method);
        return cover;
    }
    
    /**
     * Validate method, only accept setter method
     */
    @Override
    protected boolean validateMethod(Method method) {
        return method.getParameterTypes().length == 1
                && method.getReturnType() == Void.TYPE;
    }
}
