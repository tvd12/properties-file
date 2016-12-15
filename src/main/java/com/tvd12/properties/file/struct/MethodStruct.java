package com.tvd12.properties.file.struct;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.text.WordUtils;

import com.tvd12.properties.file.annotation.Property;

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
	
	//key is value of @Property annotation or field name or method name
	protected String key;
	
	/**
	 * Initialize with java method, get key and type from java method
	 * 
	 * @param clazz the class to parse
	 * @param meth the method of class
	 */
	public void initWithMethod(Class<?> clazz, Method meth) {
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
            throw new IllegalStateException(e);
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
	 * If method annotated with @Property annotation then return value of @Property annotation.
	 * If key still null then return field name related to method.
	 * If key still null then return method name
	 * 
	 * @param method java method object
	 * @return key as string
	 */
	protected String getKey(Method method) {
		String mname = "";
		Property property = method
		        .getAnnotation(Property.class);
		if(property != null)
		    mname = property.value().trim();
		if(mname.length() > 0)    return mname;
		mname = method.getName();
		if(mname.startsWith("get")
				|| mname.startsWith("set")
				|| mname.startsWith("has"))
			mname = mname.substring(3);
		if(mname.startsWith("is"))
			mname = mname.substring(2);
		return WordUtils.uncapitalize(mname);
	}
	
	/**
     * Get key related to field.
     * If method annotated with @Property annotation then return value of @Property annotation.
     * If key still null then return field name
     * 
     * @param field java field object
     * @return key as string
     */
	protected String getKey(Field field) {
	    String mname = "";
	    Property property = field
	            .getAnnotation(Property.class);
        if(property != null)
            mname = property.value().trim();
        if(mname.length() > 0)
            return mname;
        return field.getName();
	}
	
}
