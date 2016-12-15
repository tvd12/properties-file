package com.tvd12.properties.file.reader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.properties.file.exception.PropertiesFileException;

/**
 * Support for reading properties file and store the data to properties object
 * 
 * @author tavandung12
 *
 */
public class BaseFileReader implements FileReader {
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.FileReader#read(java.lang.Class, java.util.Collection)
     */
    @Override
    public List<Properties> read(Class<?> context, Collection<String> propertiesFiles)
            throws PropertiesFileException {
        return read(context, propertiesFiles.toArray(new String[propertiesFiles.size()]));
    }
	
	/* 
     * @see com.tvd12.properties.file.reader.FileReader#read(java.lang.Class, java.lang.String)
     */
	@Override
    public Properties read(Class<?> context, String propertiesFile) 
			throws PropertiesFileException {
        InputStream inputStream = getResourceAsStream(context, propertiesFile); 
        if(inputStream == null)
            inputStream = getInputStreamByAbsolutePath(propertiesFile);
        if(inputStream == null)
            throw new PropertiesFileException("Can not read properties file in path " 
                    + propertiesFile); 
        return loadInputStream(inputStream);
				
	}
	
	/* 
     * @see com.tvd12.properties.file.reader.FileReader#read(java.lang.Class, java.lang.String)
     */
    @Override
    public List<Properties> read(Class<?> context, String... propertiesFiles) 
            throws PropertiesFileException {
        List<Properties> result = new ArrayList<>();
        for(String file : propertiesFiles)
            result.add(read(context, file));
        return result;
    }
    
    /* 
     * @see com.tvd12.properties.file.reader.FileReader#read(java.lang.String)
     */
    @Override
    public Properties read(String propertiesFile) throws PropertiesFileException {
        return read(getClass(), propertiesFile);
    }
    
    /* 
     * @see com.tvd12.properties.file.reader.FileReader#read(java.lang.String)
     */
    @Override
    public List<Properties> read(String... propertiesFiles) throws PropertiesFileException {
        return read(getClass(), propertiesFiles);
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.FileReader#read(java.io.File)
     */
    @Override
    public Properties read(File propertiesFile) throws PropertiesFileException {
        return loadInputStream(getInputStreamByAbsolutePath(propertiesFile));
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.FileReader#read(java.io.File[])
     */
    @Override
    public List<Properties> read(File... propertiesFiles) throws PropertiesFileException {
        List<Properties> result = new ArrayList<>();
        for(File file : propertiesFiles)
            result.add(read(file));
        return result;
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.reader.FileReader#read(java.util.Collection)
     */
    @Override
    public List<Properties> read(Collection<File> propertiesFiles) throws PropertiesFileException {
        return read(propertiesFiles.toArray(new File[propertiesFiles.size()]));
    }
    
	/* 
     * @see com.tvd12.properties.file.reader.FileReader#loadInputStream(java.io.InputStream)
     */
	@Override
    public Properties loadInputStream(InputStream inputStream) 
	        throws PropertiesFileException {
	    Properties properties = new Properties();
	    try {
	        properties.load(new ByteArrayInputStream(decode(inputStream)));
	    } catch (IOException e) {
            throw new PropertiesFileException("Can not read properties file", e);
        }
	    return properties;
	}
	
	/* (non-Javadoc)
	 * @see com.tvd12.properties.file.reader.FileReader#loadInputStreams(java.io.InputStream[])
	 */
	@Override
	public List<Properties> loadInputStreams(InputStream... inputStreams)
	        throws PropertiesFileException {
	    List<Properties> result = new ArrayList<>();
        for(InputStream inputStream : inputStreams)
            result.add(loadInputStream(inputStream));
        return result;
	}
	
	/* (non-Javadoc)
	 * @see com.tvd12.properties.file.reader.FileReader#loadInputStreams(java.util.Collection)
	 */
	@Override
	public List<Properties> loadInputStreams(Collection<InputStream> inputStreams)
	        throws PropertiesFileException {
	    return loadInputStreams(inputStreams.toArray(new InputStream[inputStreams.size()]));
	}
	
	/**
	 * Get the contents of an InputStream as a byte[] and decode them.
	 * 
	 * @param inputStream an input stream
	 * @return byte array
	 * @throws IOException if an I/O error occurs
	 */
	protected byte[] decode(InputStream inputStream) throws IOException {
	    return IOUtils.toByteArray(inputStream);
	}
	
	/**
	 * Get a input stream from a file if it exists and be readable 
	 * 
	 * @param propertiesFile properties file path
	 * @return an input stream object
	 */
	private InputStream getInputStreamByAbsolutePath(String propertiesFile) {
		return getInputStreamByAbsolutePath(new File(propertiesFile));
	}
	
	/**
	 * 
	 * Get a input stream from a file if it exists and be readable
	 * 
	 * @param file properties file
	 * @return input stream
	 */
	private InputStream getInputStreamByAbsolutePath(File file) {
        InputStream inputStream = null;
        try {
            if(file.exists())
                inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            getLogger().error("can't read file " + file, e);
        }
        
        return inputStream;
    }
	
	/**
	 * Get a input stream from a file in project
	 * 
	 * @param propertiesFile properties file in class path
	 * @return an inputstream object
	 */
	private InputStream getResourceAsStream(Class<?> context,
			String propertiesFile) {
		InputStream ip = context
				.getClassLoader()
				.getResourceAsStream(propertiesFile);
		if(ip == null)
			ip = context.getResourceAsStream(propertiesFile);
		return ip;
	}
	
	private Logger getLogger() {
	    return LoggerFactory.getLogger(getClass());
	}
	
}
