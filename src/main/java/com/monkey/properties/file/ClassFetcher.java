package com.monkey.properties.file;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ClassFetcher {

	private List<Properties> propertiesList;
	
	private ClassFetcher(String... propertiesFiles) 
			throws PropertiesFileException {
		propertiesList = PropertiesFileReader.read(propertiesFiles);
	}
	
	/**
	 * Get an instance
	 * 
	 * @param propertiesFiles list of properties files
	 * @return an instance
	 * @throws PropertiesFileException when can not read a properties file
	 */
	public static ClassFetcher newInstance(String... propertiesFiles) 
			throws PropertiesFileException {
		return new ClassFetcher(propertiesFiles);
	}
	
	/**
	 * Read a value by key and return a class that has name is the value
	 * 
	 * @param T type of class
	 * @param key corresponding class name (value in properties file)
	 * @return a Class object
	 * @throws PropertiesFileException when have no classes 
	 * that's corresponding with key or class not exists
	 */
	public <T> Class<T> getClass(Object key) throws PropertiesFileException {
		String className = null;
		for(Properties prop : propertiesList) {
			className = prop.getProperty(key.toString());
			if(className == null) 
				continue;
			return getClass(prop, key);
		}
		return null;
	}
	
	/**
	 * Read all key-value pairs and return classes 
	 * that're corresponding with values
	 * 
	 * @return a Class object
	 * @throws PropertiesFileException when have no classes 
	 * that's corresponding with key or class not exists
	 */
	public <T> Map<Object, Class<T>> getClasses() throws PropertiesFileException {
		Map<Object, Class<T>> result = new HashMap<>();
		for(Properties prop : propertiesList) {
			for(Object key : prop.keySet()) {
				Class<T> clazz = getClass(prop, key);
				result.put(key, clazz);
			}
		}
		
		return result;
	}
	
	/**
	 * Read a value by key and return a class that has name is the value
	 * 
	 * @param prop properties object
	 * @param key key
	 * @return a Class object
	 * @throws PropertiesFileException when have no classes 
	 * that's corresponding with key or class not exists
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> getClass(Properties prop, Object key) 
			throws PropertiesFileException {
		String className = prop.getProperty(key.toString());
		if(className == null)
			throw new PropertiesFileException("Has no classes with key " + key);
		try {
			return (Class<T>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			String msg = "Can not get class " + className;
			throw new PropertiesFileException(msg, e);
		}
	}
}
