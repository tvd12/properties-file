package com.tvd12.properties.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class InstanceFactory {
	
	private static InstanceFactory defaultInstance;
	private List<Properties> propertiesList = new ArrayList<>();
	private Map<Class<?>, Object> singletons = new HashMap<>();
	private static List<Exception> exceptions = new ArrayList<>();
	
	public static final int SINGLETON = 0x1;
	public static final int PROTOTYPE = 0x2;
	
	private InstanceFactory(Class<?> entry, String... propertiesFiles) {
		this.init(entry, propertiesFiles);
	}
	
	/**
	 * Create a factory instance
	 * 
	 * @param entry which class you want to create a factory instance, 
	 * because maybe InstanceFactory and properties file that you want 
	 * to read be loaded from difference class loader, so we need
	 * this param to connect to propertiesFiles's class loader 
	 * @param propertiesFiles list of properties file
	 * @return: an instance of factory
	 */
	public static InstanceFactory create(Class<?> entry, String... propertiesFiles) {
		return new InstanceFactory(entry, propertiesFiles);
	}
	
	private static InstanceFactory getInstance(Class<?> entry, String... propertiesFiles) {
		if(defaultInstance == null) {
			synchronized (InstanceFactory.class) {
				if(defaultInstance == null) {
					defaultInstance = new InstanceFactory(entry, propertiesFiles);
				}
			}
		}
		
		return defaultInstance;
	}
	
	/**
	 * Initialize factory with list of properties files
	 * 
	 * * @param entry which class you want to init a factory instance, 
     * because maybe InstanceFactory and properties file that you want 
     * to read be loaded from difference class loader, so we need
     * this param to connect to propertiesFiles's class loader
	 * 
	 * @param propertiesFiles list of properties file
	 */
	public static void initialize(Class<?> entry, String...propertiesFiles) {
		getInstance(entry, propertiesFiles);
	}
	
	/**
	 * Create new instance of class or interface
	 * 
	 * @param clazz type of class of interface
	 * @return an instance
	 */
	public static <T> T newInstance(Class<T> clazz) {
		return defaultInstance.getInstance(clazz);
	}
	
	/**
	 * Factory never throws exceptions, but it save errors
	 * that's generated in creating instance
	 * 
	 * @return list of errors
	 */
	public static List<Exception> getErrors() {
		return defaultInstance.getExceptions();
	}
	
	/**
	 * Create new instance of class or interface
	 * 
	 * @param clazz type of class or interface
	 * @return an instance
	 */
	public <T> T getInstance(Class<T> clazz) {
		return getInstance(clazz, PROTOTYPE);
	}
	
	/**
	 * get singleton instance
	 * 
	 * @param clazz interface or class
	 * @return a singleton instance
	 */
	public static <T> T getSingleton(Class<T> clazz) {
		return defaultInstance.getSingletonInstance(clazz);
	}
	
	/**
	 * get singleton instance
	 * 
	 * @param clazz interface or class
	 * @return a singleton instance
	 */
	public <T> T getSingletonInstance(Class<T> clazz) {
		Object result = checkSingleton(clazz);
		return clazz.cast((result == null)
				? getInstance(clazz, SINGLETON)
				: result);
	}
	
	private <T> Object checkSingleton(Class<T> clazz) {
		return singletons.get(clazz);
	}
	
	private <T> T getInstance(Class<T> clazz, int type) {
		String implementationClassName = null;
		for(Properties prop : propertiesList) {
			implementationClassName = prop.getProperty(clazz.getName());
			if(implementationClassName == null) 
				continue;
			break;
		}
		try {
			Class<?> implClazz = Class.forName(implementationClassName);
			return clazz.cast((type == SINGLETON) 
					? createSingleton(clazz, implClazz)
					: implClazz.newInstance());
			
						
		} catch (InstantiationException 
				| IllegalAccessException 
				| ClassNotFoundException e) {
			String msg = "Can not get instance of class " + clazz.getName();
			addException(new PropertiesFileException(msg, e));
		}
		return null;
	}
	
	private <T> Object createSingleton(Class<T> baseClazz, Class<?> implClazz) 
			throws InstantiationException, IllegalAccessException {
		Object object = singletons.get(baseClazz);
		if(object == null) {
			synchronized (implClazz) {
				object = singletons.get(baseClazz);
				if(object == null) {
					object = implClazz.newInstance();
					singletons.put(baseClazz, object);
				}
			}
		}
		return object;
	}
	
	private void init(Class<?> entry, String... propertiesFiles) {
		for(String file : propertiesFiles) {
			Properties properties =  readPropertiesFile(entry, file);
			if(properties != null) 
				propertiesList.add(properties);
		}
	}
	
	private Properties readPropertiesFile(Class<?> entry, String file) {
		Properties properties = null;
		try {
			properties = PropertiesFileReader.read(entry, file);
		} catch (PropertiesFileException e) {
			addException(e);
		}
		return properties;
	}
	
	private void addException(PropertiesFileException exception) {
		if(exceptions.size() > 5) 
			exceptions.clear();
		exceptions.add(exception);
	}
	
	/**
	 * Factory never throws exceptions, but it save errors
	 * that's generated in creating instance
	 * 
	 * @return list of errors
	 */
	public List<Exception> getExceptions() {
		return exceptions;
	}
}
