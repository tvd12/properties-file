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
    
}
