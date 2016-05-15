package com.tvd12.properties.file.struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
     */
	public ClassWrapper(Class<?> clazz) {
        super(clazz);
    }
	
	/**
	 * Initialize SetterMethod object with java field 
	 */
	@Override
    protected MethodStruct initWithField(Field field) {
        MethodStruct cover = new SetterMethod();
        cover.initWithField(clazz, field);
        return cover;
    }

	/**
	 * Initialize SetterMethod object with java method
	 */
    @Override
    protected MethodStruct initWithMethod(Method method) {
        MethodStruct cover = new SetterMethod();
        cover.initWithMethod(clazz, method);
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
