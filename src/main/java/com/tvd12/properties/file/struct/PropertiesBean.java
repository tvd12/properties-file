package com.tvd12.properties.file.struct;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.io.DefaultValueConverter;
import com.tvd12.properties.file.io.ValueConverter;
import com.tvd12.properties.file.mapping.MappingLevel;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reflect.ReflectionClassUtils;
import com.tvd12.properties.file.util.Logger;
import com.tvd12.properties.file.util.PropertyAnnotations;

/**
 * 
 * Holds structure of java bean class, map object to properties object
 * and also  
 * 
 * @author tavandung12
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PropertiesBean {

	private final Object bean;
    private final ClassWrapper wrapper;
    private final ClassLoader classLoader;
    private final ValueConverter valueConverter;
    
    public PropertiesBean(Class<?> clazz) {
        this(ReflectionClassUtils.newInstance(clazz));
    }
    
    public PropertiesBean(Object bean) {
        this(
    		bean, 
    		MappingLevel.ALL,
    		DefaultValueConverter.getInstance(),
    		null
        );
    }
    
    public PropertiesBean(
    		Object bean, 
    		MappingLevel mappingLevel,
    		ValueConverter valueConverter,
    		ClassLoader classLoader) {
    	this.bean = bean;
    	this.classLoader = classLoader;
    	this.wrapper = new ClassWrapper(bean.getClass(), mappingLevel);
    	this.valueConverter = valueConverter != null ? valueConverter : DefaultValueConverter.getInstance();
    }
    
    public <T> T getObject() {
        return (T)bean;
    }

    protected MethodStruct getWriteMethodStruct(Object key) {
    	return wrapper.getMethodStruct(key.toString());
    }

    public void put(Object key, Object value) {
    	put(key, value, null);
    }
    
    public void put(Object key, Object value, Properties properties) {
    	MethodStruct methodStruct = getWriteMethodStruct(key);
        if(methodStruct == null)
            return;
        try {
    		Object argument = transform(methodStruct, value, properties);
    		if(argument == null)
    			return;
    		if(methodStruct.getMethod() != null) {
    			methodStruct.getMethod().invoke(bean, argument);
    		}
    		else {
    			Field field = methodStruct.getField();
    			if(!Modifier.isPublic(field.getModifiers()))
    				field.setAccessible(true);
    			field.set(bean, argument);
    		}
        }
        catch (Exception e) {
    		printError("put value: " + value + " with key: " + key + " error", e);
    		e.printStackTrace();
			return;
		}
    }
    
    public void putAll(Properties properties) {
        for(Object key : wrapper.keySet()) {
    		Object value = properties.get(key);
    		put(key, value, properties);
        }
    }
    
    protected Class getWriteArgumentType(MethodStruct methodStruct) {
    	if(methodStruct.getField() != null)
    		return methodStruct.getField().getType();
		return methodStruct.getMethod().getParameterTypes()[0];
    }
    
    protected Object transform(
    		MethodStruct methodStruct, Object value, Properties properties) {
    	String prefix = "";
    	Property propertyAnno = methodStruct.getAnnotation(Property.class);
    	if(propertyAnno != null)
    		prefix = PropertyAnnotations.getPrefix(propertyAnno);
        Class argumentType = getWriteArgumentType(methodStruct);
        if(properties == null || prefix.isEmpty()) {
        	if(value == null)
        		return null;
        	Object v = value;
        	if(v instanceof String)
                v = ((String) value).trim();
    		return transform(argumentType, v);
        }
        return new PropertiesMapper()
        		.data(properties)
        		.classLoader(classLoader)
        		.propertyPrefix(prefix)
        		.valueConverter(valueConverter)
        		.map(argumentType);
    }
    
	protected Object transform(Class newType, Object value) {
		return valueConverter.convert(value, newType);
	}
    
    
    protected void printError(String message, Throwable throwable) {
    	Logger.print(message, throwable);
    }
    
}
