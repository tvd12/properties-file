package com.tvd12.properties.file.reader;

import java.io.InputStream;
import java.util.Properties;

import com.tvd12.properties.file.constant.Constants;
import com.tvd12.properties.file.util.InputStreamUtil;

/**
 * Support for reading properties file and store the data to properties object
 * 
 * @author tavandung12
 *
 */
public class BaseFileReader implements FileReader {
    
    
	/* 
     * @see com.tvd12.properties.file.reader.FileReader#loadInputStream(java.io.InputStream, java.lang.String)
     */
	@Override
    public Properties loadInputStream(InputStream inputStream, String contentType) {
		String realContentType = contentType;
		if(contentType == null)
			realContentType = InputStreamUtil.guessContentType(inputStream);
		if(realContentType.equals(Constants.FILE_EXTENSION_PROPERTIES)) {
			InputStreamReader reader = new PropertiesInputStreamReader();
	    	return reader.readInputStream(inputStream);
		}
		else {
	    	InputStreamReader reader = new YamlFileReader();
	    	return reader.readInputStream(inputStream);
		}
	}
	
}