package com.tvd12.properties.file.struct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.mapping.MappingLevel;
import com.tvd12.properties.file.util.ReflectionClassUtil;

/**
 * Support for holding structure of which classes contain annotated fields,
 * annotated methods, setter methods, getter.
 *
 * @author tavandung12
 */
public abstract class ClassStruct {

    //java class to parse
    protected final Class<?> clazz;

    protected final MappingLevel mappingLevel;

    protected final MethodFilter methodFilter;

    //map of method structure and key
    protected final Map<String, MethodStruct> methods;

    protected final List<MethodStruct> declaredFieldStructs;

    protected final PropertyAnnotations propertyAnnotations;

    /**
     * construct with java class.
     *
     * @param clazz               java class
     * @param mappingLevel        the mapping level
     * @param propertyAnnotations the properties annotation filter
     */
    public ClassStruct(
        Class<?> clazz,
        MappingLevel mappingLevel,
        PropertyAnnotations propertyAnnotations) {
        this.clazz = clazz;
        this.mappingLevel = mappingLevel;
        this.propertyAnnotations = propertyAnnotations;
        this.methods = new HashMap<>();
        this.declaredFieldStructs = new ArrayList<>();
        this.methodFilter = methodFilter();
        this.initWithFields();
        this.initWithMethods();
        this.initWithDeclaredFields();
    }


    /**
     * get all annotated fields and get their reader or setter method.
     */
    private void initWithFields() {
        Set<Field> fields = getAcceptedFields();
        for (Field field : fields) {
            addMethod(initWithField(field));
        }
    }

    /**
     * get all annotated methods and filter duplicated method.
     */
    private void initWithMethods() {
        Set<Method> methods = getAcceptedMethods();
        for (Method method : methods) {
            if (!methodFilter.filter(method)) {
                continue;
            }
            addMethod(initWithMethod(method));
        }
    }

    private void initWithDeclaredFields() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                declaredFieldStructs.add(initWithField(field));
            }
        }
    }

    /**
     * Initialize a MethodStruct object of java field.
     *
     * @param field java field
     * @return a MethodStruct object
     */
    protected abstract MethodStruct initWithField(Field field);

    /**
     * Initialize a MethodStruct object of java field.
     *
     * @param method java method
     * @return a MethodStruct object
     */
    protected abstract MethodStruct initWithMethod(Method method);

    /**
     * validate method, if method is setter it must return void and has only parameter,
     * if method is getter it must return non-void and has no parameters.
     *
     * @param method java method
     * @return true or false
     */
    protected abstract boolean validateMethod(Method method);

    protected boolean containsKey(String key) {
        return methods.containsKey(key);
    }

    /**
     * Check duplicated method.
     *
     * @param method java method to check
     * @return true or false
     */
    protected boolean containsMethod(Method method) {
        for (MethodStruct cover : methods.values()) {
            if (cover.getMethod() != null && cover.getMethod().equals(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get method structure by key.
     *
     * @param key key
     * @return a method structure or null
     */
    protected MethodStruct getMethodStruct(String key) {
        return methods.get(key);
    }

    /**
     * add a MethodStruct object to list.
     *
     * @param method MethodStruct object to add
     */
    protected void addMethod(MethodStruct method) {
        methods.put(method.getKey(), method);
    }

    /**
     * If mappingLevel == MappingLevel.ALL then return all fields in class.
     * If mappingLevel == MappingLevel.ANNOTATION any fields and methods annotated with.
     * PropertyForTest annotation then return annotated fields.
     *
     * @return set of java fields
     */
    protected Set<Field> getAcceptedFields() {
        Set<Field> fields;
        if (mappingLevel == MappingLevel.ALL) {
            fields = ReflectionClassUtil.getValidFields(clazz);
        } else {
            fields = ReflectionClassUtil.getFieldsWithAnnotations(
                clazz,
                propertyAnnotations.getAnnotationClasses()
            );
        }
        return fields;
    }

    /**
     * If mappingLevel == MappingLevel.ALL then return all methods in class.
     * If mappingLevel == MappingLevel.ANNOTATION any fields and methods annotated with.
     * PropertyForTest annotation then return annotated methods.
     *
     * @return set of java methods
     */
    protected Set<Method> getAcceptedMethods() {
        Set<Method> methods;
        if (mappingLevel == MappingLevel.ALL) {
            methods = ReflectionClassUtil.getPublicMethods(clazz);
        } else {
            methods = ReflectionClassUtil.getMethodsWithAnnotations(
                clazz,
                propertyAnnotations.getAnnotationClasses()
            );
        }
        return methods;
    }

    /**
     * Get ket set of methods.
     *
     * @return set of keys
     */
    public Set<String> keySet() {
        return methods.keySet();
    }

    /**
     * Get method count.
     *
     * @return number of methods
     */
    public int methodCount() {
        return methods.size();
    }

    /**
     * Filter to get invalid methods.
     *
     * @return a filter object to filter invalid method
     */
    protected MethodFilter methodFilter() {
        return method -> validateMethod(method) && !containsMethod(method);
    }

    @SuppressWarnings("rawtypes")
    public Constructor getNoArgsDeclaredConstructor() {
        return ReflectionClassUtil.getNoArgsDeclaredConstructor(clazz);
    }

    public Constructor<?> getMaxArgsDeclaredConstructor() {
        return ReflectionClassUtil.getMaxArgsDeclaredConstructor(clazz);
    }

    public Object newObjectInstance() {
        return ReflectionClassUtil.newInstance(clazz);
    }
}
