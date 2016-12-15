package com.tvd12.properties.file.struct;

import static org.reflections.ReflectionUtils.withModifier;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.reflections.ReflectionUtils;

import com.google.common.base.Predicates;
import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.annotation.PropertyWrapper;

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
	
	//whether class annotated with @PropertyWrapper annotation
	//true if class annotated with @PropertyWrapper annotation or has no @Property annotation in class
	protected boolean isWrapper;
	
	//list of method structure
	protected List<MethodStruct> methods
        = new ArrayList<>();
	
	//set of keys
	protected Set<Object> keys 
	        = new HashSet<>();
	
	/**
	 * construct with java class
	 * 
	 * @param clazz java class
	 */
    public ClassStruct(Class<?> clazz) {
        init(clazz);
    }
	
    /**
     * initialize with java class
     * 
     * @param clazz the class to parse
     */
	protected void init(Class<?> clazz) {
		this.clazz = clazz;
		this.checkIsWrapper();
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
     * check whether class annotated with @PropertyWrapper annotation
     * or class has any fields or methods annotated with @Property annotation
     * 
     */
    protected void checkIsWrapper() {
        isWrapper = clazz
                .isAnnotationPresent(PropertyWrapper.class);
        if(isWrapper)   return;
        List<Field> fields = FieldUtils
                .getFieldsListWithAnnotation(clazz, Property.class);
        List<Method> methods = MethodUtils
                .getMethodsListWithAnnotation(clazz, Property.class);
        isWrapper = fields.isEmpty() && methods.isEmpty();
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
        for(MethodStruct cover : methods)  {
            if(cover.getKey().equals(key))
                return true;
        }
        return false;
    }
    
    /**
     * Check duplicated method
     * 
     * @param method java method to check
     * @return true or false
     */
    protected boolean containsMethod(Method method) {
        for(MethodStruct cover : methods)  {
            if(cover.getMethod().equals(method))
                return true;
        }
        return false;
    }
    
    /**
     * Get method by key
     *  
     * @param key key
     * @return a method object or null
     */
    protected Method getMethod(String key) {
        for(MethodStruct cover : methods) {
            if(cover.getKey().equals(key))
                return cover.getMethod();
        }
        return null;
    }

    /**
     * add a MethodStruct object to list
     * 
     * @param method MethodStruct object to add
     */
    protected void addMethod(MethodStruct method) {
        methods.add(method);
        addKey(method);
    }
    
    /**
     * add key to key set
     * 
     * @param method which MethodStruct object related to key
     */
    protected void addKey(MethodStruct method) {
        keys.add(method.getKey());
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
	@SuppressWarnings("unchecked")
    protected Set<Field> getAnnotatedFields() {
	    if(isWrapper)
	        return ReflectionUtils
	                .getAllFields(clazz, Predicates.not(
	                        withModifier(Modifier.FINAL)));
	    return new HashSet<>(FieldUtils
	            .getFieldsListWithAnnotation(clazz, Property.class));
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
	@SuppressWarnings("unchecked")
    protected Set<Method> getAnnotatedMethods() {
	    if(isWrapper)
            return ReflectionUtils
                    .getAllMethods(clazz, withModifier(Modifier.PUBLIC));
        return new HashSet<>(MethodUtils
                .getMethodsListWithAnnotation(clazz, Property.class));
	}
	
	/**
	 * create new an instance of java class
	 * 
	 * @return an object
	 */
	public Object newInstance() {
	    try {
            return clazz.newInstance();
        } catch (Exception e) {
           throw new IllegalStateException("Can not create instance of class " + clazz, e);
        }
	}
	
	/**
	 * 
	 * @return set of keys
	 */
	public Set<Object> keySet() {
	    return keys;
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
