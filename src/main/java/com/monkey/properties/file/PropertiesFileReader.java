package com.monkey.properties.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileReader {
	
	private PropertiesFileReader() {}
	
	/**
	 * Read properties file in a path
	 * 
	 * @param propertiesFilePath
	 * @return
	 * @throws PropertiesFileException
	 */
	public static Properties read(String propertiesFilePath) 
			throws PropertiesFileException {
		Properties prop = null;
		try {
			
			InputStream inputStream = getResourceAsStream(propertiesFilePath); 
			if(inputStream == null)
				inputStream = getInputStreamByAbsolutePath(propertiesFilePath);
			
			if(inputStream != null) 
				(prop = new Properties()).load(inputStream);
				
			return prop;
			
		} 
		catch (IOException e) {
			String msg = "Can not read properties file in path " + propertiesFilePath;
			throw new PropertiesFileException(msg, e);
		}
	}
	
	/**
	 * Get a input stream from a file if it exists and be readable 
	 * 
	 * @param propertiesFilePath
	 * @return
	 * @throws FileNotFoundException
	 */
	private static InputStream getInputStreamByAbsolutePath(String propertiesFilePath) 
			throws FileNotFoundException {
		File file = new File(propertiesFilePath);
		InputStream inputStream = null;
		if(file.exists()) 
			inputStream = new FileInputStream(file);
		return inputStream;
	}
	
	/**
	 * Get a input stream from a file in project
	 * 
	 * @param propertiesFilePath
	 * @return
	 * @throws IOException 
	 */
	private static InputStream getResourceAsStream(String propertiesFilePath) 
			throws IOException {
		InputStream ip = PropertiesFileReader
				.class
				.getClassLoader()
				.getResourceAsStream(propertiesFilePath);
		if(ip == null)
			throw new IOException("Please check file " 
					+ propertiesFilePath
					+ " in classpath again");
		return ip;
	}
	
}
