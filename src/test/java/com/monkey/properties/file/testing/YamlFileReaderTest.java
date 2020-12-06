package com.monkey.properties.file.testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.exception.YamlInvalidSyntaxException;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.reader.YamlFileReader;
import com.tvd12.properties.file.util.InputStreamUtil;

public class YamlFileReaderTest {

	@Test
	public void readSpaces() {
		BaseFileReader reader = new BaseFileReader();
		Properties properties = reader.read("application.yaml");
		System.out.println(properties);
		assert properties.get("cors.allow_orgin").equals("*");
		assert properties.get("cors.enable").equals("true");
		assert properties.get("server.port").equals("3005");
		assert properties.get("server.host").equals("0.0.0.0");
		assert properties.get("server.admin.username").equals("admin");
		assert properties.get("server.admin.password").equals("123456");
		assert properties.get("hello").equals("world");
		assert properties.get("foo").equals("bar");
		properties = reader.read("hello_yaml");
		assert properties.get("hello").equals("world");
	}
	
	@Test
	public void readTabs() {
		BaseFileReader reader = new BaseFileReader();
		Properties properties = reader.read("application2_yaml.txt");
		System.out.println(properties);
		assert properties.get("cors.allow_orgin").equals("*");
		assert properties.get("cors.enable").equals("true");
		assert properties.get("server.port").equals("3005");
		assert properties.get("server.host").equals("0.0.0.0");
		assert properties.get("server.admin.User-Name1").equals("admin");
		assert properties.get("server.admin.password2").equals("123456");
		assert properties.get("hello").equals("world");
		assert properties.get("foo").equals("bar");
	}
	
	@Test(expectedExceptions = YamlInvalidSyntaxException.class)
	public void testNoContainsSeparateChar() {
		YamlFileReader reader = new YamlFileReader();
		reader.readInputStream(InputStreamUtil.getInputStream(
				getClass().getClassLoader(), "invalid_yaml1.txt"));
	}
	
	@Test(expectedExceptions = YamlInvalidSyntaxException.class)
	public void testEmptyKey() {
		BaseFileReader reader = new BaseFileReader();
		reader.read("invalid_yaml2.txt");
	}
	
	@Test(expectedExceptions = PropertiesFileException.class)
	public void testIOException() {
		YamlFileReader reader = new YamlFileReader() {
			protected Properties read(BufferedReader reader) throws IOException {
				throw new IOException("just test");
			}
		};
		reader.readInputStream(InputStreamUtil.getInputStream(getClass().getClassLoader(), "application2_yaml.txt"));
	}
	
	@Test
	public void testInvalidSpaces() {
		BaseFileReader reader = new BaseFileReader();
		Properties properties = reader.read("invalid_yaml3.txt");
		System.out.println("testInvalidSpaces: " + properties);
	}
	
	@Test(expectedExceptions = YamlInvalidSyntaxException.class)
	public void testDashKey() {
		BaseFileReader reader = new BaseFileReader();
		reader.read("invalid_yaml4.txt");
	}
	
	@Test(expectedExceptions = YamlInvalidSyntaxException.class)
	public void testInvalidKey() {
		BaseFileReader reader = new BaseFileReader();
		reader.read("invalid_yaml5.txt");
	}
}
