package com.tvd12.properties.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.tvd12.properties.file.exception.PropertiesFileException;

public final class InputStreamUtil {

	private InputStreamUtil() {}
	
	/**
	 * Get a input stream from a file
	 * Find in class loader first, if not found, find in find system 
	 * 
	 * @param classLoader the class loader
	 * @param filePath the file path
	 * @return an InputStream object
	 */
	public static InputStream getInputStream(
			ClassLoader classLoader, String filePath) {
		InputStream inputStream = getResourceAsStream(classLoader, filePath); 
        if(inputStream == null)
        		inputStream = getResourceAsStream(classLoader, "/" + filePath);
        if(inputStream == null)
            inputStream = getInputStreamByAbsolutePath(filePath);
        if(inputStream == null)
            throw new PropertiesFileException("Can not read file in path " + filePath);
        return inputStream;
	}
	
	/**
	 * Get a input stream from a file in project
	 * 
	 * @param classLoader the class loader
	 * @param filePath the file path in class path
	 * @return an InputStream object
	 */
	private static InputStream getResourceAsStream(ClassLoader classLoader,
			String filePath) {
		ClassLoader acceptClassLoader = classLoader;
		if(acceptClassLoader == null)
			acceptClassLoader = getDefaultClassLoader();
		InputStream ip = acceptClassLoader.getResourceAsStream(filePath);
		if(ip == null)
			ip = ClassLoader.getSystemResourceAsStream(filePath);
		return ip;
	}
	
	/**
	 * Get a input stream from a file if it exists and be readable 
	 * 
	 * @param filePath the file path
	 * @return an input stream object
	 */
	private static InputStream getInputStreamByAbsolutePath(String filePath) {
		return getInputStreamByAbsolutePath(new File(filePath));
	}
	
	/**
	 * Get a input stream from a file if it exists and be readable 
	 * 
	 * @param file the file
	 * @return an input stream object
	 */
	public static InputStream getInputStreamByAbsolutePath(File file) {
		try {
        	return new FileInputStream(file);
        } 
        catch (FileNotFoundException e) {
            return null;
        }
	}
	
	/**
	 * Get default class loader
	 * 
	 * @return the current thread's class loader
	 */
	public static ClassLoader getDefaultClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
}
