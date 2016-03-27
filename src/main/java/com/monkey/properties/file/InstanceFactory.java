package com.monkey.properties.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class InstanceFactory {
	
	private static InstanceFactory defaultInstance;
	private List<Properties> propertiesList = new ArrayList<>();
	private static List<Exception> exceptions = new ArrayList<>();
	
	private InstanceFactory(String... propertiesFiles) {
		this.init(propertiesFiles);
	}
	
	/**
	 * create a factory instance
	 * 
	 * @param propertiesFiles list of properties file
	 * @return: an instance of factory
	 */
	public static InstanceFactory newFactory(String... propertiesFiles) {
		return new InstanceFactory(propertiesFiles);
	}
	
	private static InstanceFactory getInstance(String... propertiesFiles) {
		if(defaultInstance == null) {
			synchronized (InstanceFactory.class) {
				if(defaultInstance == null) {
					defaultInstance = new InstanceFactory(propertiesFiles);
				}
			}
		}
		
		return defaultInstance;
	}
	
	/**
	 * Initialize factory with list of properties files
	 * 
	 * @param propertiesFiles list of properties file
	 */
	public static void initialize(String...propertiesFiles) {
		getInstance(propertiesFiles);
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
		String implementationClassName = null;
		for(Properties prop : propertiesList) {
			implementationClassName = prop.getProperty(clazz.getName());
			if(implementationClassName == null) 
				continue;
			break;
		}
		try {
			return clazz.cast(Class.forName(implementationClassName)
						.newInstance());
		} catch (InstantiationException 
				| IllegalAccessException 
				| ClassNotFoundException e) {
			String msg = "Can not get instance of class " + clazz.getName();
			addException(new PropertiesFileException(msg, e));
		}
		return null;
	}
	
	private void init(String... propertiesFiles) {
		for(String file : propertiesFiles) {
			Properties properties =  readPropertiesFile(file);
			if(properties != null) 
				propertiesList.add(properties);
		}
	}
	
	private Properties readPropertiesFile(String file) {
		Properties properties = null;
		try {
			properties = PropertiesFileReader.read(file);
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
