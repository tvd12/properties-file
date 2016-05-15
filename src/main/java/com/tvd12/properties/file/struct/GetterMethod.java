package com.tvd12.properties.file.struct;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * 
 * Support for holding structure of java getter method.
 * 
 * @author tavandung12
 *
 */
public class GetterMethod extends MethodStruct {
    
    /**
     * Get java getter method
     */
	@Override
	protected Method getMethod(PropertyDescriptor descriptor) {
	    return descriptor.getReadMethod();
	}
	
	/**
	 * Get return type of java method
	 */
	@Override
	protected Class<?> getTypeFromMethod(Method method) {
		return method.getReturnType();
	}
	
	/**
	 * Invoke java method
	 * 
	 * @param obj the object the underlying method is invoked from
	 * @return the result of invoking method
	 */
	public Object invoke(Object obj) {
		try {
			return method.invoke(obj);
		} catch (Exception e) {
			throw new IllegalStateException(
			        "Can not invoke getter method " 
			        + getMethodName()
			        + " on class " + obj.getClass(), e);
		}
	}
	
}
