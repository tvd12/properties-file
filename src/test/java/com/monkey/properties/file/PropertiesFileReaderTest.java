package com.monkey.properties.file;

import static org.junit.Assert.*;

import org.junit.Test;

import com.tvd12.properties.file.PropertiesFileException;
import com.tvd12.properties.file.PropertiesFileReader;

public class PropertiesFileReaderTest {

	@Test
	public void testWithValidData() throws PropertiesFileException {
		assertNotNull(PropertiesFileReader.read(getClass(), "classes.properties"));
	}
	
	@Test(expected = PropertiesFileException.class)
	public void testWithInvalidData() throws PropertiesFileException {
		PropertiesFileReader.read(getClass(), "classes1.properties");
	}
	
}
