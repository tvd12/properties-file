package com.tvd12.properties.file.struct;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.tvd12.properties.file.annotation.PropertyAnnotations;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Support for holding structure of java method.
 * 
 * @author tavandung12
 *
 */
@Getter
public abstract class MethodStruct {

    //type of return type of getter method or parameter of setter method
	protected Class<?> type;

	//java method
	protected Method method;
	
	//java field to get setter or getter method
	protected Field field;
	
	//key is value of @PropertyForTest annotation or field name or method name
	protected String key;
	
	@Getter(AccessLevel.NONE)
	protected final PropertyAnnotations propertyAnnotations;
	
	public MethodStruct(PropertyAnnotations propertyAnnotations) {
		this.propertyAnnotations = propertyAnnotations;
	}
	
	/**
	 * Initialize with java method, get key and type from java method
	 * 
	 * @param meth the method of class
	 */
	public void initWithMethod(Method meth) {
		this.method = meth;
		this.key = getKey(method);
		this.type = getTypeFromMethod(method);
	}
	
	/**
	 * Initialize with java field, get setter or getter from java field object,
	 * get type and key from java field object
	 * 
	 * @param clazz which class contains field
	 * @param field java field 
	 */
	public void initWithField(Class<?> clazz, Field field) {
	    this.field = field;
		this.type = field.getType();
		this.key = getKey(field);
		this.method = getMethodByField(clazz, field);
	}
	
	/**
	 * Get setter or getter method of java field
	 * 
	 * @param clazz which class contains field
	 * @param field java field
	 * @return a java method object
	 */
	protected Method getMethodByField(Class<?> clazz, Field field) {
        try {
            String name = (field.getName());
            name = (name.startsWith("is")) 
                    ? name.substring(2) : name;
            return getMethod(new PropertyDescriptor(
                    name, clazz));
        } catch (IntrospectionException e) {
            return null;
        }
    }
	
	/**
	 * Get setter or getter method from PropertyDescriptor object
	 * 
	 * @param descriptor PropertyDescriptor object
	 * @return a java method
	 */
	protected abstract Method getMethod(PropertyDescriptor descriptor);
	
	/**
	 * Get return type or parameter type of method
	 * 
	 * @param method java method object to get type
	 * @return a Class (type) object
	 */
	protected abstract Class<?> getTypeFromMethod(Method method);
	
	public String getMethodName() {
	    return method.getName();
	}
	
	/**
	 * Get key related to method.
	 * If method annotated with @PropertyForTest annotation then return value of @PropertyForTest annotation.
	 * If key still null then return field name related to method.
	 * If key still null then return method name
	 * 
	 * @param method java method object
	 * @return key as string
	 */
	protected String getKey(Method method) {
		String mname = propertyAnnotations.getPropertyName(method);
		if(!mname.isEmpty())
			return mname;
		mname = method.getName();
		if(mname.startsWith("get")
				|| mname.startsWith("set")
				|| mname.startsWith("has"))
			mname = mname.substring(3);
		if(mname.startsWith("is"))
			mname = mname.substring(2);
		if(mname.length() < 2)
			return mname;
		return mname.substring(0, 1).toLowerCase() + mname.substring(1);
	}
	
	/**
     * Get key related to field.
     * If method annotated with @PropertyForTest annotation then return value of @PropertyForTest annotation.
     * If key still null then return field name
     * 
     * @param field java field object
     * @return key as string
     */
	protected String getKey(Field field) {
	    String mname = propertyAnnotations.getPropertyName(field);
        if(!mname.isEmpty())
            return mname;
        return field.getName();
	}
	
	protected String getPropertyName() {
		String propertyName = "";
		if(field != null)
			propertyName = propertyAnnotations.getPropertyName(field);
		if(propertyName.isEmpty() && method != null)
			propertyName = propertyAnnotations.getPropertyName(method);
		return propertyName;
	}
	
	protected String getPropertyPrefix(boolean guess) {
		String prefix = "";
		String propertyName = getPropertyName();
		if(propertyName.isEmpty()) {
			if(field != null)
				prefix = propertyAnnotations.getPropertyPrefix(field);
			if(prefix.isEmpty() && method != null)
				prefix = propertyAnnotations.getPropertyPrefix(method);
			if(prefix.isEmpty() && guess)
				prefix = guestPropertyPrefix();
		}
		return prefix;
	}
	
	protected String guestPropertyPrefix() {
		String key = getKey();
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < key.length() ; ++i) {
			char ch = key.charAt(i);
			if(Character.isUpperCase(ch) && i > 0)
				builder.append(".");
			builder.append(Character.toLowerCase(ch));
		}
		return builder.toString();
	}
	
}
