package com.tvd12.properties.file.struct;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.tvd12.properties.file.mapping.MappingLevel;
import com.tvd12.properties.file.reflect.ReflectionClassUtils;

/**
 * 
 * Support for holding structure of which classes contain annotated fields, annotated methods,
 * setter methods, getter 
 * 
 * @author tavandung12
 *
 */
public abstract class ClassStruct {
	
    //java class to parse
	protected Class<?> clazz;
	
	protected MappingLevel mappingLevel;
	
	protected Set<Field> annotatedFields;
	protected Set<Method> annotatedMethods;
	
	//map of method structure and key
	protected Map<String, MethodStruct> methods = new HashMap<>();
	
	/**
	 * construct with java class
	 * 
	 * @param clazz java class
	 * @param mappingLevel the mapping level
	 */
    public ClassStruct(Class<?> clazz, MappingLevel mappingLevel) {
    	this.clazz = clazz;
    	this.mappingLevel = mappingLevel;
		this.initWithFields();
        this.initWithMethods();
    }
	
	
	/**
	 * get all annotated fields and get their reader or setter method
	 */
    private void initWithFields() {
        Set<Field> fields = getAnnotatedFields();
        for(Field field : fields) {
            addMethod(initWithField(field));
        }
    }
    
    /**
     * get all annotated methods and filter duplicated method
     */
    private void initWithMethods() {
        Set<Method> methods = getAnnotatedMethods();
        for(Method method : methods) {
            if(!methodFilter().filter(method))
                continue;
            addMethod(initWithMethod(method));
        }
    }

    /**
     * Initialize a MethodStruct object of java field
     * 
     * @param field java field
     * @return a MethodStruct object
     */
    protected abstract MethodStruct initWithField(Field field);
    
    /**
     * Initialize a MethodStruct object of java field
     * 
     * @param method java method
     * @return a MethodStruct object
     */
    protected abstract MethodStruct initWithMethod(Method method);
    
    /**
     * validate method, if method is setter it must return void and has only parameter,
     * if method is getter it must return non-void and has no parameters
     * 
     * @param method java method
     * @return true or false
     */
    protected abstract boolean validateMethod(Method method);
    
    protected boolean containsKey(String key) {
    	return methods.containsKey(key);
    }
    
    /**
     * Check duplicated method
     * 
     * @param method java method to check
     * @return true or false
     */
    protected boolean containsMethod(Method method) {
        for(MethodStruct cover : methods.values())  {
            if(cover.getMethod() != null && cover.getMethod().equals(method))
                return true;
        }
        return false;
    }
    
    /**
     * Get method structure by key
     *  
     * @param key key
     * @return a method structure or null
     */
    protected MethodStruct getMethodStruct(String key) {
    	return methods.get(key);
    }

    /**
     * add a MethodStruct object to list
     * 
     * @param method MethodStruct object to add
     */
    protected void addMethod(MethodStruct method) {
        methods.put(method.getKey(), method);
    }
    
    /**
     * If class annotated with PropertyWrapper annotation then return all fields in class.
     * If class not annotated with PropertyWrapper annotation and has no fields or methods annotated
     * with Property annotation then return all fields
     * If class not annotated with PropertyWrapper and has any fields and methods annotated with
     * Property annotation then return annotated fields 
     * 
     * @return set of java fields
     */
    protected Set<Field> getAnnotatedFields() {
		Set<Field> fields = null;
	    if(mappingLevel == MappingLevel.ALL)
	        fields = ReflectionClassUtils.getValidFields(clazz);
	    else
    		fields = annotatedFields;
	    return fields;
	}
	
	/**
     * If class annotated with PropertyWrapper annotation then return all methods in class.
     * If class not annotated with PropertyWrapper annotation and has no fields or methods annotated
     * with Property annotation then return all methods
     * If class not annotated with PropertyWrapper and has any fields and methods annotated with
     * Property annotation then return annotated methods 
     * 
     * @return set of java methods
     */
    protected Set<Method> getAnnotatedMethods() {
		Set<Method> methods = null;
		if(mappingLevel == MappingLevel.ALL)
    		methods = ReflectionClassUtils.getPublicMethods(clazz);
	    else
    		methods = annotatedMethods;
	    return methods;
	}
	
	/**
	 * 
	 * @return set of keys
	 */
	public Set<String> keySet() {
	    return methods.keySet();
	}
	
	/**
	 * 
	 * @return number of methods
	 */
	public int methodCount() {
	    return methods.size();
	}
	
	/**
	 * @return a filter object to filter invalid method
	 */
	protected MethodFilter methodFilter() {
	    return new MethodFilter() {
            @Override
            public boolean filter(Method method) {
                return !containsMethod(method)
                        && validateMethod(method);
            }
        };
	}

}
