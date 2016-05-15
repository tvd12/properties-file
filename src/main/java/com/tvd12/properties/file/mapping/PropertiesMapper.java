package com.tvd12.properties.file.mapping;

import static com.tvd12.properties.file.util.PropertiesUtil.toProperties;

import java.util.Map;
import java.util.Properties;

import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.reader.FileReader;
import com.tvd12.properties.file.struct.PropertiesBean;

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
    
    //class to get resource as stream
    private Class<?> context;
    
    //properties object to map
    private Properties properties;
    
    //properties file path to read and map
    private String propertiesFile;
    
    //properties file reader
    private FileReader reader;
    
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
     * @param context class to get resource as tream
     * @return this pointer
     */
    public PropertiesMapper context(Class<?> context) {
        this.context = context;
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
     * map properties to object
     * 
     * @return object after mapped
     */
    public <T> T map() {
        PropertiesBean answer = 
                new PropertiesBean(newBeanInstance());
        answer.putAll(getProperties());
        return answer.getObject();
    }
    
    /**
     * create object to hold data 
     * 
     * @return
     */
    private Object newBeanInstance() {
        try {
            if(bean == null)
                bean = clazz.newInstance();
            clazz = bean.getClass();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Can not create new bean instance", e);
        }
        return bean;
    }
    
    /**
     * read properties file if properties object null
     * 
     * @return properties object
     */
    private Properties getProperties() {
        if(context == null)
            context = clazz;
        try {
            if(properties == null)
                properties = reader.read(context, propertiesFile);
        } catch (PropertiesFileException e) {
            throw new IllegalStateException(e);
        }
        return properties;
    }

}
