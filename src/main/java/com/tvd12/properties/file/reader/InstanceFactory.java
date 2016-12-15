package com.tvd12.properties.file.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.tvd12.properties.file.exception.PropertiesFileException;

/**
 * A factory class support for reading properties files and save the data to list of properties objects
 * After reading, create prototype or singleton instance of class that related to key and value in 
 * properties objects if anyone request
 * 
 * 
 * @author tavandung12
 *
 */
public class InstanceFactory {
	
    // default instance, init one time and use forever
	private static InstanceFactory defaultInstance;
	
	// list of properties objects to hold data
	private List<Properties> propertiesList = new ArrayList<>();
	
	// list of singleton instances
	private Map<Class<?>, Object> singletons = new HashMap<>();
	
	// list of exceptions thrown in runtime
	private static List<Exception> exceptions = new ArrayList<>();
	
	public static final int SINGLETON = 0x1;
	public static final int PROTOTYPE = 0x2;
	
	// prevent new instance
	private InstanceFactory() {}
	
	/**
	 * Construct object with class to get resource as stream and array of properties file path
	 * 
	 * @param context class to get resource as stream
	 * @param propertiesFiles array properties file path
	 */
	private InstanceFactory(Class<?> context, String... propertiesFiles) {
		this.init(context, propertiesFiles);
	}
	
	/**
	 * Create a factory instance
	 * 
	 * @param context class to get resource as stream
	 * @param propertiesFiles array of properties file
	 * @return an instance of factory
	 */
	public static InstanceFactory create(Class<?> context, String... propertiesFiles) {
		return new InstanceFactory(context, propertiesFiles);
	}
	
	/**
     * Create a factory instance
     * 
     * @param propertiesFiles array of properties file
     * @return an instance of factory
     */
    public static InstanceFactory create(String... propertiesFiles) {
        return create(InstanceFactory.class, propertiesFiles);
    }
	
	public synchronized static InstanceFactory defaultInstance() {
	    if(defaultInstance == null) 
	        defaultInstance = new InstanceFactory();
	    return defaultInstance;
	}
	
	/**
	 * Initialize factory with list of properties file paths
	 * 
	 * @param context which class to get resource as stream
	 * @param propertiesFiles list of properties file
	 */
	public synchronized void initialize(Class<?> context, String...propertiesFiles) {
	    defaultInstance().init(context, propertiesFiles);
	}
	
	/**
     * Initialize factory with list of properties file paths
     * 
     * @param propertiesFiles list of properties file
     */
    public synchronized void initialize(String...propertiesFiles) {
        initialize(getClass(), propertiesFiles);
    }
	
	/**
	 * Create new instance of class or interface
	 * 
	 * @param <T> the type of object
	 * @param clazz type of class or interface
	 * @return an instance
	 */
	public <T> T getPrototype(Class<T> clazz) {
		return newClassInstance(clazz, PROTOTYPE);
	}
	
	/**
	 * get singleton instance
	 * 
	 * @param <T> the type of object
	 * @param clazz interface or class
	 * @return a singleton instance
	 */
	public <T> T getSingleton(Class<T> clazz) {
		Object result = checkSingleton(clazz);
		return clazz.cast((result == null)
				? newClassInstance(clazz, SINGLETON)
				: result);
	}
	
	/**
	 * check whether has singleton instance related to @param class 
	 * 
	 * @param <T> the type of class
	 * @param clazz class to check
	 * @return an object or null
	 */
	private <T> Object checkSingleton(Class<T> clazz) {
		return singletons.get(clazz);
	}
	
	/**
	 * Get implementation class of @param clazz and create new an instance
	 * 
	 * @param <T> the type of class
	 * @param clazz class to get implementation class
	 * @param type prototype or singleton
	 * @return an object
	 */
	private <T> T newClassInstance(Class<T> clazz, int type) {
		String implementationClassName = null;
		for(Properties prop : propertiesList) {
			implementationClassName = prop.getProperty(clazz.getName());
			if(implementationClassName == null) 
				continue;
			break;
		}
		if(implementationClassName == null)   return null;
		Class<?> implClazz = forName(implementationClassName);
		return clazz.cast((type == SINGLETON) 
					? createSingleton(clazz, implClazz)
					: newImplInstance(implClazz));
	}
	
	/**
	 * create a singleton instance of @param baseClazz and save that instance to singletons set
	 * 
	 * @param <T> the type of class
	 * @param baseClazz class to get implementation class
	 * @param implClazz implementation
	 * @return an object
	 */
	private <T> Object createSingleton(Class<T> baseClazz, Class<?> implClazz) {
	    Object object = newImplInstance(implClazz);
	    singletons.put(baseClazz, object);
		return object;
	}
	
	/**
	 * Returns the Class object associated with the class or interface with the given string name
	 * 
	 * @param className class name
	 * @return a Class object
	 */
	private Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            String msg = "Not found " + className;
            addException(new PropertiesFileException(msg, e));
        }
        return null;
    }
	
	/**
	 * create new instance for implementation class
	 * 
	 * @param <T> the type of class
	 * @param implClazz implementation class
	 * @return an object
	 */
	private <T> Object newImplInstance(Class<T> implClazz) {
	    try {
            return implClazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            String msg = "Can not create new instance of class " + implClazz.getName();
            addException(new PropertiesFileException(msg, e));
        }
	    return null;
	}
	
	/**
	 * initialize InstanceFactory object (clear all old data and init new data)
	 * 
	 * @param context class to get resource as stream
	 * @param propertiesFiles array of properities file paths
	 */
	private void init(Class<?> context, String... propertiesFiles) {
	    propertiesList.clear();
		for(String file : propertiesFiles) {
			Properties properties =  readPropertiesFile(context, file);
			if(properties != null) 
				propertiesList.add(properties);
		}
	}
	
	/**
	 * read data from properties file and store the data to properties object
	 * 
	 * @param context class to get resource as stream
	 * @param file properties file path
	 * @return properties object
	 */
	private Properties readPropertiesFile(Class<?> context, String file) {
		Properties properties = null;
		try {
			properties = new BaseFileReader().read(context, file);
		} catch (PropertiesFileException e) {
			addException(e);
		}
		return properties;
	}
	
	/**
	 * add exception to list
	 * 
	 * @param exception exception
	 */
	private void addException(PropertiesFileException exception) {
		exceptions.add(exception);
	}
	
	/**
	 * Factory never throws exceptions, but it save errors
	 * that's generated in creating instance
	 * 
	 * @return list of errors
	 */
	public List<Exception> getErrors() {
		return exceptions;
	}
}
