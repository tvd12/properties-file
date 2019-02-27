package com.tvd12.properties.file.reader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.tvd12.properties.file.exception.PropertiesFileException;

/**
 * Support for reading properties file and store the data to properties object. 
 * After reading, get class object associated with the class or interface with the given string name 
 * in values of properties object
 * 
 * @author tavandung12
 *
 */
public abstract class AbstractClassFetcher {
    
    //properties file reader
    protected FileReader reader;
    
    //list of properties object to hold data
    protected List<Properties> propertiesList;
    
    /**
     * Construct object with builder
     * 
     * @param builder builder
     */
    protected AbstractClassFetcher(AbstractBuilder builder) {
    		this.reader = builder.reader;
        this.init(builder);
        this.load();
    }
    
    /**
     * Initialize properties
     * 
     * @param builder the builder
     */
    protected void init(AbstractBuilder builder) {
    }
    
    /**
     * read data from properties files and store them to properties objects
     */
    protected void load() {
        try {
            propertiesList = loadPropertiesList();
        } catch (PropertiesFileException e) {
            throw new IllegalStateException(e);
        }
    }
    
    protected abstract List<Properties> loadPropertiesList();
    
	/**
	 * Read a value by key and return a class that has name is the value
	 * 
	 * @param <T> the type of class
	 * @param key corresponding class name (value in properties file)
	 * @return a Class object
	 * @throws PropertiesFileException when have no classes 
	 * that's corresponding with key or class not exists
	 */
	public <T> Class<T> fetch(Object key) throws PropertiesFileException {
		String className = null;
		for(Properties prop : propertiesList) {
			className = prop.getProperty(key.toString());
			if(className == null) 
				continue;
			return fetch(prop, key);
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
	public Map<Object, Class<?>> fetch() throws PropertiesFileException {
		Map<Object, Class<?>> result = new HashMap<>();
		for(Properties prop : propertiesList) {
			for(Object key : prop.keySet()) {
				result.put(key, fetch(prop, key));
			}
		}
		
		return result;
	}
	
	/**
	 * Read a value by key and return a class that has name is the value
	 * 
	 * @param <T> the type class
	 * @param prop properties object
	 * @param key key
	 * @return a Class object
	 * @throws PropertiesFileException when have no classes 
	 * that's corresponding with key or class not exists
	 */
	@SuppressWarnings("unchecked")
	protected <T> Class<T> fetch(Properties prop, Object key) 
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
	
	/**
	 * Support to build ClassFetcher object
	 * 
	 * @author tavandung12
	 *
	 */
	public abstract static class AbstractBuilder {
	    
	    // properties file reader
	    protected FileReader reader;
	    
	    /**
	     * set properties file reader
	     * 
	     * @param reader properties file reader
	     * @return this pointer
	     */
	    public AbstractBuilder reader(FileReader reader) {
	        this.reader = reader;
	        return this;
	    }
	    
	    /**
	     * build a ClassFetcher object
	     * 
	     * @return ClassFetcher object
	     */
	    public abstract AbstractClassFetcher build();
	}
}
