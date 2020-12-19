package com.tvd12.properties.file.mapping;

import static com.tvd12.properties.file.util.PropertiesUtil.toProperties;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tvd12.properties.file.annotation.PropertyAnnotation;
import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.io.ValueConverter;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.reader.FileReader;
import com.tvd12.properties.file.struct.PropertiesBean;
import com.tvd12.properties.file.util.PropertiesUtil;
import com.tvd12.properties.file.util.ReflectionGenericUtil;

/**
 * Support for mapping a properties object or properties file to object
 * 
 * @author tavandung12
 *
 */
public class PropertiesMapper {
    
    //object to hold data
    private Object bean;
    
    //mapped class to hold data
    private Class<?> clazz;
    
    //properties object to map
    private Properties properties;
    
    //properties file path to read and map
    private String propertiesFile;
    
    //properties file reader
    private FileReader reader;
    
    //class to get resource as stream
    private ClassLoader classLoader;
    
    // property prefix
    private String propertyPrefix;
    
    // mapping level
    private MappingLevel mappingLevel = MappingLevel.ALL;
    
    // the value converter
    private ValueConverter valueConverter;
    
    private PropertyAnnotations propertyAnnotations;
    
    /**
     * set mapped class
     * 
     * @param clazz mapped class 
     * @return this pointer
     */
    public PropertiesMapper clazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }
    
    /**
     * 
     * set class that used to get resource as stream
     * 
     * @param context class to get resource as stream
     * @return this pointer
     */
    public PropertiesMapper context(Class<?> context) {
        return classLoader(context.getClassLoader());
    }
    
    /**
     * 
     * set classLoader that used to get resource as stream
     * 
     * @param classLoader class to get resource as stream
     * @return this pointer
     */
    public PropertiesMapper classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }
    
    /**
     * 
     * set object to hold data
     * 
     * @param bean object to hold data
     * @return this pointer
     */
    public PropertiesMapper bean(Object bean) {
        this.bean = bean;
        return this;
    }
    
    /**
     * set data to map
     * 
     * @param properties data to map
     * @return this pointer
     */
    public PropertiesMapper data(Properties properties) {
        this.properties = properties;
        return this;
    }
    
    /**
     * set data as map for mapping
     * 
     * @param map data
     * @return this pointer
     */
    @SuppressWarnings("rawtypes")
    public PropertiesMapper data(Map map) {
        this.properties = toProperties(map);
        return this;
    }
    
    /**
     * Set property prefix
     * 
     * @param propertyPrefix the property prefix
     * @return this pointer
     */
    public PropertiesMapper propertyPrefix(String propertyPrefix) {
    	this.propertyPrefix = propertyPrefix;
    	return this;
    }
    
    /**
     * Set mapping level
     * 
     * @param mappingLevel the mapping level
     * @return this pointer
     */
    public PropertiesMapper mappingLevel(MappingLevel mappingLevel) {
    	this.mappingLevel = mappingLevel;
    	return this;
    }
    
    /**
     * Add an property annotation
     * 
     * @param annotation the property annotation
     * @return this pointer
     */
    public PropertiesMapper addPropertyAnnotation(PropertyAnnotation annotation) {
    	if(propertyAnnotations == null)
    		propertyAnnotations = new PropertyAnnotations();
    	this.propertyAnnotations.addPropertyAnnotation(annotation);
    	return this;
    }
    
    /**
     * Set PropertyAnnotations
     * 
     * @param propertyAnnotations the PropertyAnnotations
     * @return this pointer
     */
    public PropertiesMapper propertyAnnotations(PropertyAnnotations propertyAnnotations) {
    	this.propertyAnnotations = propertyAnnotations;
    	return this;
    }
    
    /**
     * set properties file that contains data to map
     * 
     * @param propertiesFilePath properties file path
     * @return this pointer
     */
    public PropertiesMapper file(String propertiesFilePath) {
        this.propertiesFile = propertiesFilePath;
        return this;
    }
    
    /**
     * set properties file reader
     * 
     * @param reader properties file reader
     * @return this pointer
     */
    public PropertiesMapper reader(FileReader reader) {
        this.reader = reader;
        return this;
    }
    
    /**
     * set value converter
     * 
     * @param valueConverter the value converter
     * @return this pointer
     */
    public PropertiesMapper valueConverter(ValueConverter valueConverter) {
        this.valueConverter = valueConverter;
        return this;
    }
    
    /**
     * map properties to object
     * 
     * @param <T> the type of object
     * @return object after mapped
     */
    @SuppressWarnings("unchecked")
	public <T> T map() {
    	if(bean == null && clazz == null)
    		this.clazz = Properties.class;
        return (T) map(clazz);
    }
    
    /**
     * map properties to object
     * 
     * @param clazz mapped class
     * @param <T> the type of object
     * @return object after mapped
     */
	public <T> T map(Class<T> clazz) {
    	return map(clazz, null);
    }
    
    @SuppressWarnings({ "unchecked" })
	public <T> T map(Class<T> clazz, Type genericType) {
    	if(clazz == null && bean == null)
    		throw new IllegalArgumentException("there is nothing to map, plase provice clazz, or bean");
    	this.clazz(clazz);
    	this.readProperties();
    	if(propertyAnnotations == null)
    		propertyAnnotations = new PropertyAnnotations();
    	if(clazz == Map.class)
    		return (T)doMapToMapValue(genericType);
    	else if(clazz == Properties.class)
    		return (T)properties;
    	else
    		return (T)doMapValue();
    	
    }
    
    private Object doMapValue() {
    	if(bean != null) {
	        return map(
	        		new PropertiesBean(
		        		bean, 
		        		mappingLevel, 
		        		valueConverter, 
		        		propertyAnnotations,
		        		classLoader
		        	));
    	}
    	else {
    		return map(
	        		new PropertiesBean(
		        		clazz,
		        		properties,
		        		mappingLevel, 
		        		valueConverter, 
		        		propertyAnnotations,
		        		classLoader
		        	));
    	}
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
   	private Map doMapToMapValue(Type genericType) {
    	if(genericType == null)
			return properties;
		Class<?> mapValueType = ReflectionGenericUtil
				.getTwoGenericClassArguments(genericType)[1];
		if(mapValueType == null)
    		return properties;
    	Map answer = new HashMap<>();
    	Map<String, Properties> propertiesMap = PropertiesUtil.getPropertiesMap(properties);
    	for(String key : propertiesMap.keySet()) {
    		Object value = new PropertiesMapper()
    				.data(properties)
    				.propertyPrefix(key)
	        		.classLoader(classLoader)
	        		.valueConverter(valueConverter)
	        		.propertyAnnotations(propertyAnnotations)
	        		.map(mapValueType);
    		answer.put(key, value);
    	}
    	return answer;
    }
    
    /**
     * map properties to object
     * 
     * @param <T> the type of object
     * @param mapping a custom of properties bean mapping
     * @return object after mapped
     */
    private <T> T map(PropertiesBean mapping) {
		mapping.putAll(properties);
        T answer = mapping.getObject();
        return answer;
    		
    }
    
    /**
     * read properties file if properties object null
     * 
     */
    private void readProperties() {
        try {
        	if(reader == null)
        		reader = new BaseFileReader();
            if(properties == null)
            	properties = new Properties();
            if(propertiesFile != null)
            	properties.putAll(reader.read(classLoader, propertiesFile));
            if(propertyPrefix != null)
            	properties = PropertiesUtil.getPropertiesByPrefix(properties, propertyPrefix);
        } catch (PropertiesFileException e) {
            throw new IllegalStateException(e);
        }
        PropertiesUtil.decorateProperties(properties);
    }
    
}
