package com.tvd12.properties.file.struct;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.io.DefaultValueConverter;
import com.tvd12.properties.file.io.ValueConverter;
import com.tvd12.properties.file.mapping.MappingLevel;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.util.Logger;
import com.tvd12.properties.file.util.PropertiesUtil;
import com.tvd12.properties.file.util.ReflectionClassUtil;

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
    private final PropertyAnnotations propertyAnnotations;
    
    public PropertiesBean(Class<?> clazz) {
        this(clazz, new PropertyAnnotations());
    }
    
    public PropertiesBean(
            Class<?> clazz, 
            PropertyAnnotations propertyAnnotations) {
        this(
            ReflectionClassUtil.newInstance(clazz),
            propertyAnnotations
        );
    }
    
    public PropertiesBean(
            Object bean, 
            PropertyAnnotations propertyAnnotations) {
        this(
            bean, 
            MappingLevel.ALL,
            DefaultValueConverter.getInstance(),
            propertyAnnotations,
            null
        );
    }
    
    public PropertiesBean(
            Object bean, 
            MappingLevel mappingLevel,
            ValueConverter valueConverter,
            PropertyAnnotations propertyAnnotations,
            ClassLoader classLoader) {
        this.bean = bean;
        this.classLoader = classLoader;
        this.propertyAnnotations = propertyAnnotations;
        this.wrapper = new ClassWrapper(bean.getClass(), mappingLevel, propertyAnnotations);
        this.valueConverter = valueConverter != null ? valueConverter : DefaultValueConverter.getInstance();
    }
    
    public PropertiesBean(
            Class<?> clazz,
            Properties properties,
            MappingLevel mappingLevel,
            ValueConverter valueConverter,
            PropertyAnnotations propertyAnnotations,
            ClassLoader classLoader) {
        this.wrapper = new ClassWrapper(clazz, mappingLevel, propertyAnnotations);
        this.valueConverter = valueConverter != null ? valueConverter : DefaultValueConverter.getInstance();
        this.classLoader = classLoader;
        this.propertyAnnotations = propertyAnnotations;
        this.bean = createBean(properties);
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
                if(Modifier.isFinal(field.getModifiers()))
                    return;
                if(!Modifier.isPublic(field.getModifiers()))
                    field.setAccessible(true);
                field.set(bean, argument);
            }
        }
        catch (Exception e) {
            printError("put value: " + value + " with key: " + key + " error", e);
            return;
        }
    }
    
    public void putAll(Properties properties) {
        for(Object key : wrapper.keySet()) {
            Object value = PropertiesUtil.getValue(properties, key);
            put(key, value, properties);
        }
    }
    
    private Class getWriteArgumentType(MethodStruct methodStruct) {
        if(methodStruct.getField() != null)
            return methodStruct.getField().getType();
        return methodStruct.getMethod().getParameterTypes()[0];
    }
    
    private Object transform(
            MethodStruct methodStruct, 
            Object value, Properties properties) {
        boolean guessPrefix = properties != null && value == null;
        String prefix = methodStruct.getPropertyPrefix(guessPrefix);
        Class argumentType = getWriteArgumentType(methodStruct);
        if(properties == null || prefix.isEmpty()) {
            if(value == null)
                return null;
            Object v = value;
            if(v instanceof String)
                v = ((String) value).trim();
            return transform(v, argumentType);
        }
        return transformValue(
                properties, 
                prefix, 
                argumentType, 
                methodStruct.getGenericType()
        );
    }
    
    private Object transform(Object value, Class newType) {
        return valueConverter.convert(value, newType);
    }
    
    private Object createBean(Properties properties) {
        Constructor constructor = wrapper.getNoArgsDeclaredConstructor();
        if(constructor != null)
            return wrapper.newObjectInstance();
        return createBeanByMaxArgsConstructor(properties);
    }
    
    private Object createBeanByMaxArgsConstructor(Properties properties) {
        Constructor constructor = wrapper.getMaxArgsDeclaredConstructor();
        Parameter[] parameters = constructor.getParameters();
        List<MethodStruct> declaredFieldStructs = wrapper.declaredFieldStructs;
        Object[] args = new Object[constructor.getParameterCount()];
        for(int i = 0 ; i < args.length ; ++i) {
            Class<?> parameterType = parameters[i].getType();
            if(i >= declaredFieldStructs.size()) {
                args[i] = PropertiesUtil.defaultValueOf(parameterType);
                continue;
            }
            MethodStruct fieldStruct = declaredFieldStructs.get(i);
            Class<?> fieldType = fieldStruct.getType();
            String key = fieldStruct.getKey();
            args[i] = getAndTransform(properties, key, parameterType);
            if(args[i] == null) {
                args[i] = transformValue(
                        properties, 
                        fieldStruct.guestPropertyPrefix(), 
                        fieldType, 
                        fieldStruct.getGenericType()
                );
            }
        }
        return ReflectionClassUtil.newInstance(constructor, args);
    }
    
    private Object transformValue(
            Properties properties,
            String prefix,
            Class<?> valueType,
            Type genericType
    ) {
        try {
            if(PropertiesUtil.containsPrefix(properties, prefix)) {
                return new PropertiesMapper()
                    .data(properties)
                    .classLoader(classLoader)
                    .propertyPrefix(prefix)
                    .valueConverter(valueConverter)
                    .propertyAnnotations(propertyAnnotations)
                    .map(valueType, genericType);
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private Object getAndTransform(Properties properties, String key, Class newType) {
        Object value = PropertiesUtil.getValue(properties, key);
        if(value != null)
            return transform(value, newType);
        return PropertiesUtil.defaultValueOf(newType);
    }
    
    protected void printError(String message, Throwable throwable) {
        Logger.print(message, throwable);
    }
    
}
