package com.tvd12.properties.file.reader;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
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
     * Read properties file in a path
     *
     * @param propertiesFile properties file path
     * @param classLoader the class loader
     * @return properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    Properties read(ClassLoader classLoader, String propertiesFile) throws PropertiesFileException;
    
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
     * Read properties files in multiple paths
     * 
     * @param propertiesFiles list of properties files
     * @param classLoader the class loader 
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(ClassLoader classLoader, String... propertiesFiles) throws PropertiesFileException;
    
    /**
     * Read properties files in multiple paths
     * 
     * @param propertiesFiles list of properties files
     * @param context which class to get resource as stream
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(Class<?> context, Collection<String> propertiesFiles) throws PropertiesFileException;
    
    /**
     * Read properties files in multiple paths
     * 
     * @param propertiesFiles list of properties files
     * @param classLoader the class loader
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(ClassLoader classLoader, Collection<String> propertiesFiles) throws PropertiesFileException;
    
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
     * @return array of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(String... propertiesFiles) throws PropertiesFileException;

    /**
     * Load an input stream and read properties
     * 
     * @param inputStream the input stream
     * @return properties the properties
     * @throws PropertiesFileException when can't load input stream or can't read properties
     */
    Properties loadInputStream(InputStream inputStream) throws PropertiesFileException;
    
    /**
     * Read properties files
     * 
     * @param inputStreams array of properties files
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> loadInputStreams(InputStream... inputStreams) throws PropertiesFileException;
    
    /**
     * Read properties files
     * 
     * @param inputStreams array of properties files
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> loadInputStreams(Collection<InputStream> inputStreams) throws PropertiesFileException;
    
    /**
     * Read properties file
     * 
     * @param propertiesFile properties file
     * @return properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    Properties read(File propertiesFile) throws PropertiesFileException;
    
    /**
     * Read properties files
     * 
     * @param propertiesFiles array of properties files
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(File... propertiesFiles) throws PropertiesFileException;
    
    /**
     * Read properties files
     * 
     * @param propertiesFiles array of properties files
     * @return list of properties object
     * @throws PropertiesFileException when properties file not exists or can't read properties
     */
    List<Properties> read(Collection<File> propertiesFiles) throws PropertiesFileException;
    

}