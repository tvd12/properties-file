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
    
}
