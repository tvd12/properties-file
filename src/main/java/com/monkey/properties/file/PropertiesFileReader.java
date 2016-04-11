package com.monkey.properties.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesFileReader {
	
	private PropertiesFileReader() {}
	
	/**
	 * Read properties file in a path
	 * 
	 * @param propertiesFile properties file path
	 * @return properties object
	 * @param entry which class loads properties
	 * @throws PropertiesFileException when properties file not exists
	 */
	public static Properties read(Class<?> entry, String propertiesFile) 
			throws PropertiesFileException {
		Properties prop = null;
		try {
			
			InputStream inputStream = getResourceAsStream(entry, propertiesFile); 
			if(inputStream == null)
				inputStream = getInputStreamByAbsolutePath(propertiesFile);
			
			if(inputStream != null) 
				(prop = new Properties()).load(inputStream);
				
			return prop;
			
		} 
		catch (IOException e) {
			String msg = "Can not read properties file in path " + propertiesFile;
			throw new PropertiesFileException(msg, e);
		}
	}
	
	/**
	 * Read properties files
	 * 
	 * @param propertiesFiles list of properties files
	 * @return list of properties object
	 * @param entry which class loads properties
	 * @throws PropertiesFileException when properties file not exists
	 */
	public static List<Properties> read(Class<?> entry, String... propertiesFiles) 
			throws PropertiesFileException {
		List<Properties> result = new ArrayList<>();
		for(String file : propertiesFiles) {
			result.add(read(entry, file));
		}
		return result;
	}
	
	/**
	 * Get a input stream from a file if it exists and be readable 
	 * 
	 * @param propertiesFile properties file path
	 * @return an input stream object
	 * @throws FileNotFoundException when properties file not exists
	 */
	private static InputStream getInputStreamByAbsolutePath(String propertiesFile) 
			throws FileNotFoundException {
		File file = new File(propertiesFile);
		InputStream inputStream = null;
		if(file.exists()) 
			inputStream = new FileInputStream(file);
		return inputStream;
	}
	
	/**
	 * Get a input stream from a file in project
	 * 
	 * @param propertiesFile properties file in class path
	 * @return an inputstream object
	 * @throws IOException when properties file not exists 
	 */
	private static InputStream getResourceAsStream(Class<?> entry,
			String propertiesFile) 
					throws IOException {
		InputStream ip = entry
				.getClassLoader()
				.getResourceAsStream(propertiesFile);
		if(ip == null)
			ip = entry
			.getResourceAsStream(propertiesFile);
		if(ip == null)
			throw new IOException("Please check file " 
					+ propertiesFile
					+ " in classpath again");
		return ip;
	}
	
}
