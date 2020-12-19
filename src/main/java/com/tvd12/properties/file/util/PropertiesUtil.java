package com.tvd12.properties.file.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * 
 * Support to convert properties to map or map to properties
 * 
 * @author tavandung12
 *
 */
public class PropertiesUtil {

    //prevent new instance
    private PropertiesUtil() {}
    
    /**
     * Convert properties to map
     * 
     * @param properties properties object to convert
     * @return a map object
     */
    public static Map<String, Object> toMap(Properties properties) {
        Map<String, Object> map = new HashMap<>();
        for(Entry<Object, Object> entry : properties.entrySet())
            map.put(entry.getKey().toString(), entry.getValue());
        return map;
    }
    
    /**
     * Convert map to properties
     * 
     * @param map map object to convert
     * @return a properties object
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Properties toProperties(Map map) {
        Set<Entry> entries = map.entrySet();
        Properties answer = new Properties();
        for(Entry entry : entries) {
            answer.put(entry.getKey(), entry.getValue());
        }
        return answer;
    }
    
    /**
     * Get all properties have key start with prefix
     * 
     * @param properties the properties all
     * @param prefix the key prefix
     * @return a properties object
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Properties getPropertiesByPrefix(Map properties, String prefix) {
    	Properties answer = new Properties();
    	if(prefix.isEmpty()) {
    		answer.putAll(properties);
    		return answer;
    	}
        for(Object key : properties.keySet()) {
        	String keyString = key.toString();
            if(keyString.startsWith(prefix)) {
            	String newKey = "";
            	if(keyString.length() > prefix.length() + 1)
            		newKey = keyString.substring(prefix.length() + 1);
            	answer.put(newKey, properties.get(key));
            }
        }
        return answer;
    }
    
    /**
     * 
     * Split a properties to a map of properties by first keys
     * Example, properties(
     * 	main_datasource.url=main
     * 	main_datasource.username=main username
     * 	main_datasource.password=main password
     * 	test_datasource.url=test
     * 	test_datasource.username=test username
     * 	test_datasource.password=test password
     * )
     * Will be splitted to:
     * main_datasource: (
     * 	url=main
     * 	username=main username
     * 	password=main password
     * )
     * test_datasource: (
     * 	url=test
     * 	username=test username
     * 	password=test password
     * )
     *  
     * @param properties 
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static Map<String, Properties> getPropertiesMap(Map properties) {
    	Set<String> firstKeys = getFirstPropertyKeys(properties);
    	Map<String, Properties> answer = new HashMap<>();
    	for(String firstKey : firstKeys) {
    		answer.put(firstKey, getPropertiesByPrefix(properties, firstKey));
    	}
    	return answer;
    }
    
    
    /**
     * 
     * Get first property keys of a properties
     * Example properties(
     * 	main_datasource.url=main
     * 	main_datasource.username=main username
     * 	main_datasource.password=main password
     * 	test_datasource.url=test
     * 	test_datasource.username=test username
     * 	test_datasource.password=test password
     * )
     * has first property keys main_datasource and test_datasource 
     * 
     * @param properties
     * @return
     */
    @SuppressWarnings("rawtypes")
	public static Set<String> getFirstPropertyKeys(Map properties) {
    	Set<String> answer = new HashSet<>();
    	for(Object key : properties.keySet()) {
    		String keyString = key.toString();
    		int firstDotIndex = keyString.indexOf('.');
    		String firstKey = keyString;
    		if(firstDotIndex > 0)
    			firstKey = keyString.substring(0, firstDotIndex);
    		answer.add(firstKey);
    	}
    	return answer;
    }
    
    /**
     * 
     * Check whether a properties contains a prefix key or not
     * Example, properties (admin.name.first=Foo)
     * Will contain prefix: admin and amdin.name
     * But doesn't contain admin.name.first 
     * 
     * @param properties the properties
     * @param prefix the prefix to check
     * @return contains or not
     */
    @SuppressWarnings("rawtypes")
	public static boolean containsPrefix(Map properties, String prefix) {
    	for(Object key : properties.keySet()) {
        	String keyString = key.toString();
            if(keyString.startsWith(prefix) && 
            		keyString.length() > prefix.length()) {
            	return true;
            }
    	}
    	return false;
    }
    
    /**
     * Decorate properties, add new keys (replace _ and - by .) 
     * 
     * @param properties the properties
     */
    public static void decorateProperties(Properties properties) {
    	Map<String, Object> newKeyValues = new HashMap<>();
    	for(Object key : properties.keySet()) {
    		String newKey = key.toString()
    				.toLowerCase()
    				.replace('-', '.')
    				.replace('_', '.');
    		if(!properties.containsKey(newKey))
    			newKeyValues.put(newKey, properties.get(key));
    	}
    	properties.putAll(newKeyValues);
    }
    
    /**
     * 
     * Convert property in camel case to to case
     * Example: helloWorld will convert to hello.world
     * 
     * @param propertyName the property name in camel case
     * @return the property name in dot case
     */
    public static String getPropertyNameInDotCase(String propertyName) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < propertyName.length() ; ++i) {
			char ch = propertyName.charAt(i);
			if(Character.isUpperCase(ch) && i > 0)
				builder.append(".");
			builder.append(Character.toLowerCase(ch));
		}
		return builder.toString();
	}
    
    public static Object getValue(Properties properties, Object key) {
		Object value = properties.get(key);
		if(value == null) {
			String keyInDotCase = getPropertyNameInDotCase(key.toString());
			value = properties.get(keyInDotCase);
		}
		return value;
	}
    
    /**
     * Get default value of type
     * 
     * @param type the type
     * @return the default value of the type
     */
    public static Object defaultValueOf(Class<?> type) {
    	if(type == boolean.class)
    		return false;
    	if(type == byte.class)
    		return (byte)0;
    	if(type == char.class)
    		return (char)0;
    	if(type == double.class)
    		return 0.0D;
    	if(type == float.class)
    		return 0.0F;
    	if(type == int.class)
    		return 0;
    	if(type == long.class)
    		return 0L;
    	if(type == short.class)
    		return (short)0;
    	return null;
    }
    
}
