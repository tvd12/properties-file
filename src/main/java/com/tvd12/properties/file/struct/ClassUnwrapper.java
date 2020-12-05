package com.tvd12.properties.file.struct;

import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.mapping.MappingLevel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Holds all GetterMethod objects
 * 
 * @author tavandung12
 *
 */
public class ClassUnwrapper extends ClassStruct {
    
    /**
     * Construct with java class
     * 
     * @param clazz java class
     * @param mappingLevel the mapping level
     */
    public ClassUnwrapper(
    		Class<?> clazz, 
    		MappingLevel mappingLevel,
    		PropertyAnnotations propertyAnnotations) {
        super(clazz, mappingLevel, propertyAnnotations);
    }
	
    /**
     * Initialize GetterMethod object from java field
     */
	@Override
    protected MethodStruct initWithField(Field field) {
        MethodStruct cover = new GetterMethod(propertyAnnotations);
        cover.initWithField(clazz, field);
        return cover;
    }

	/**
	 * Initialize GetterMethod object from java method
	 */
    @Override
    protected MethodStruct initWithMethod(Method method) {
        MethodStruct cover = new GetterMethod(propertyAnnotations);
        cover.initWithMethod(method);
        return cover;
    }
    
    /**
     * Validate method, only accept valid getter method
     */
    @Override
    protected boolean validateMethod(Method method) {
        return method.getParameterTypes().length == 0
                && method.getReturnType() != Void.TYPE;
    }
    
}
