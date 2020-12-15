package com.tvd12.properties.file.util;

import java.util.HashMap;
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
    
    public static String getPropertyNameInDotCase(String key) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0 ; i < key.length() ; ++i) {
			char ch = key.charAt(i);
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
