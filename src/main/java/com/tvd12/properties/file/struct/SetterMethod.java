package com.tvd12.properties.file.struct;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.tvd12.properties.file.annotation.PropertyAnnotations;

/**
 * 
 * Support for holding structure of java setter method.
 * 
 * @author tavandung12
 *
 */
public class SetterMethod extends MethodStruct {
    
    public SetterMethod(PropertyAnnotations propertyAnnotations) {
        super(propertyAnnotations);
    }

    /**
     * Get java setter method
     */
    @Override
    protected Class<?> getTypeFromMethod(Method method) {
        return method.getParameterTypes()[0];
    }
    
    /**
     * Get parameter type of java method
     */
    @Override
    protected Method getMethod(PropertyDescriptor descriptor) {
        return descriptor.getWriteMethod();
    }
    
    /**
     * Invoke java method
     * 
     * @param obj the object the underlying method is invoked from
     * @param value value to set
     */
    public void invoke(Object obj, Object value) {
        try {
            method.invoke(obj, value);
        } catch (Exception e) {
            throw new IllegalStateException("Can not call setter method " 
                    + getMethodName()
                    + " on class " + obj.getClass(), e);
        }
    }
}
