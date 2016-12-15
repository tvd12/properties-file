package com.tvd12.properties.file.struct;

import static com.tvd12.properties.file.util.PropertiesUtil.registerDateConverter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.Transformer;

import com.google.common.collect.Sets;
import com.tvd12.properties.file.util.PropertiesUtil;

import lombok.Setter;

/**
 * 
 * Holds structure of java bean class, map object to properties object
 * and also  
 * @see org.apache.commons.beanutils.BeanMap
 * 
 * @author tavandung12
 *
 */
public class PropertiesBean extends BeanMap {

    //holds all setter methods
    @Setter
    private ClassWrapper wrapper;
    
    //holds all getter methods
    @Setter
    private ClassUnwrapper unwrapper;
    
    //map of type and transformer
    private Map<Class<?>, Transformer> transformers
            = new HashMap<>();
    
    //default constructor
    public PropertiesBean() {}
    
    /**
     * Construct with mapped class to hold data
     * 
     * @see org.apache.commons.beanutils.BeanMap#BeanMap(java.lang.Object)
     * 
     * @param clazz mapped class
     */
    public PropertiesBean(Class<?> clazz) {
        init(clazz);
        setBean(unwrapper.newInstance());
    }
    
    /**
     * Construct with mapped object to hold data
     * 
     * @see org.apache.commons.beanutils.BeanMap#BeanMap(java.lang.Object)
     * 
     * @param bean mapped object
     */
    public PropertiesBean(Object bean) {
        init(bean.getClass());
        setBean(bean);
    }
    
    /**
     * 
     * Initialize with mapped class to hold data
     * 
     * @param clazz mapped class to hold data
     */
    public void init(Class<?> clazz) {
        wrapper = new ClassWrapper(clazz);
        unwrapper = new ClassUnwrapper(clazz);
        createTransformers();
    }
    
    /*
     * Prevent re-initialize from parent
     * 
     * @see org.apache.commons.beanutils.BeanMap#reinitialise()
     */
    @Override
    protected void reinitialise() {
    }
    
    /* (non-Javadoc)
     * @see org.apache.commons.beanutils.BeanMap#getType(java.lang.String)
     */
    @Override
    public Class<?> getType(String key) {
        Method method = wrapper.getMethod(key);
        if(method != null)
            return method.getParameterTypes()[0];
        return null;
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#getBean()
     * 
     * @param <T> the type of bean object
     * @return mapped object
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject() {
        return (T)getBean();
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#getReadMethod(java.lang.Object)
     * 
     * Get getter method by key
     */
    @Override
    protected Method getReadMethod(Object key) {
        return getReadMethod(key.toString());
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#getReadMethod(java.lang.String)
     * 
     * get getter method by key
     */
    @Override
    public Method getReadMethod(String key) {
        return unwrapper.getMethod(key.toString());
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#getWriteMethod(java.lang.String)
     * 
     * get setter method
     */
    @Override
    protected Method getWriteMethod(Object key) {
        return getWriteMethod(key.toString());
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#getWriteMethod(java.lang.String)
     * 
     * get setter method
     */
    @Override
    public Method getWriteMethod(String key) {
        return wrapper.getMethod(key);
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return wrapper.containsKey(key.toString())
                || unwrapper.containsKey(key.toString());
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#put(Object, Object)
     */
    @Override
    public Object put(Object key, Object value)
            throws IllegalArgumentException, ClassCastException {
        if(getWriteMethod(key) == null)
            return null;
        if(value instanceof String)
            value = ((String) value).trim();
        return super.put(key, value);
    }
    
    /**
     * Put all keys and values from properties object to mapped object
     * 
     * @param properties properties object
     */
    public void putAll(Properties properties) {
        putAll(PropertiesUtil.toMap(properties));
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#get(Object)
     */
    @Override
    public Object get(Object key) {
        if(getReadMethod(key) == null)
            return null;
        return super.get(key);
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#keySet()
     */
    @Override
    public Set<Object> keySet() {
        return Collections.unmodifiableSet(unwrapper.keySet());
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#convertType(java.lang.Class, java.lang.Object)
     */
    @Override
    protected Object convertType(Class<?> newType, Object value) 
            throws  InstantiationException, IllegalAccessException, 
                    IllegalArgumentException, InvocationTargetException {
        try {
            return super.convertType(newType, value);
        } 
        catch(Exception e) {
            return transform(newType, value);
        }
    }
    
    /**
     * Transform value to value of new type
     * 
     * @param newType new type
     * @param value value to transform
     * @return value of new type
     */
    private Object transform(Class<?> newType, Object value) {
        Transformer transformer = getTypeTransformer( newType );
        if ( transformer != null ) {
            return transformer.transform( value );
        }
        return value;
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#keyIterator()
     */
    @Override
    public Iterator<String> keyIterator() {
        Set<Object> keys = keySet();
        return Sets.newHashSet(keys.toArray(new String[keys.size()])).iterator();
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#entrySet()
     */
    @Override
    public Set<Map.Entry<Object, Object>> entrySet() {
        return Collections.unmodifiableSet(new AbstractSet<Map.Entry<Object, Object>>() {
            @Override
            public Iterator<Map.Entry<Object, Object>> iterator() {
                return entryIterator();
            }
            @Override
            public int size() {
              return unwrapper.methodCount();
            }
        });
    }
    
    /**
     * Convert object to map object
     * 
     * @return a map object
     */
    public Map<Object, Object> toMap() {
        Map<Object, Object> answer = new HashMap<>();
        for(Map.Entry<Object, Object> entry : entrySet()) {
            answer.put(entry.getKey(), entry.getValue());
        }
        return answer;
    }
    
    /**
     * Convert object to properties object
     *  
     * @return properties object
     */
    public Properties toProperties() {
        return PropertiesUtil.toProperties(toMap());
    }
    
    /**
     * @see org.apache.commons.beanutils.BeanMap#getTypeTransformer(java.lang.Class)
     */
    @Override
    protected Transformer getTypeTransformer(Class<?> aType) {
        Transformer transformer = transformers.get(aType);
        if(transformer != null)
            return transformer;
        return super.getTypeTransformer(aType);
    }
    
    /**
     * Create type transformers
     */
    private void createTransformers() {
        transformers.put(Date.class, new Transformer() {
            @Override
            public Object transform(Object value) {
                registerDateConverter();
                return ConvertUtils.convert(value, Date.class);
            }
        });
        
        transformers.put(Class.class, new Transformer() {
            @Override
            public Object transform(Object value) {
                try {
                    String string = value.toString();
                    if(string.startsWith("class ")) 
                        string = string.substring("class ".length()).trim();
                    return Class.forName(string);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }
    
}
