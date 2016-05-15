package com.tvd12.properties.file.reader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.tvd12.properties.file.exception.PropertiesFileException;

public interface FileReader {

    /**
     * Read properties file in a path
     * 
     * @param propertiesFile properties file path
     * @param context which class to get resource as stream
     * @return properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    Properties read(Class<?> context, String propertiesFile) throws PropertiesFileException;
    
    /**
     * Read properties files in multiple paths
     * 
     * @param propertiesFiles list of properties files
     * @param context which class to get resource as stream
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(Class<?> context, String... propertiesFiles) throws PropertiesFileException;
    
    /**
     * Read properties file in a path
     * 
     * @param propertiesFile properties file path
     * @return properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    Properties read(String propertiesFile) throws PropertiesFileException;
    
    /**
     * Read properties files in multiple paths
     * 
     * @param propertiesFiles list of properties files
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(String... propertiesFiles) throws PropertiesFileException;

    /**
     * Load an input stream and read properties
     * 
     * @param inputStream
     * @return properties object
     * @throws PropertiesFileException when can't load input stream or can't read properties
     */
    Properties loadInputStream(InputStream inputStream) throws PropertiesFileException;

}