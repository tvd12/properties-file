package com.monkey.properties.file;

import static org.junit.Assert.*;

import org.junit.Test;

import com.monkey.properties.file.PropertiesFileException;
import com.monkey.properties.file.PropertiesFileReader;

public class PropertiesFileReaderTest {

	@Test
	public void testWithValidData() throws PropertiesFileException {
		assertNotNull(PropertiesFileReader.read("classes.properties"));
	}
	
	@Test(expected = PropertiesFileException.class)
	public void testWithInvalidData() throws PropertiesFileException {
		PropertiesFileReader.read("classes1.properties");
	}
	
}
