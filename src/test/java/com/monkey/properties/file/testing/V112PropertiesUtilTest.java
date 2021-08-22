package com.monkey.properties.file.testing;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.util.PropertiesUtil;
import com.tvd12.test.assertion.Asserts;

public class V112PropertiesUtilTest {

	@Test
	public void getKeyFromVariableNameSuccess() {
		// given
		String variableName = "${foo.bar}";
		
		// when
		String actual = PropertiesUtil.getKeyFromVariableName(variableName);
		
		// then
		String expectation = "foo.bar";
		
		Asserts.assertEquals(expectation, actual);
	}
	
	@Test
	public void getKeyFromVariableNameNullDueToMissingFirst() {
		// given
		String variableName = "foo.bar}";
		
		// when
		String actual = PropertiesUtil.getKeyFromVariableName(variableName);
		
		// then
		Asserts.assertNull(actual);
	}
	
	@Test
	public void getKeyFromVariableNameNullDueToMissingLast() {
		// given
		String variableName = "${foo.bar";
		
		// when
		String actual = PropertiesUtil.getKeyFromVariableName(variableName);
		
		// then
		Asserts.assertNull(actual);
	}
	
	@Test
	public void getKeyFromVariableNameNullDueToEmpty() {
		// given
		String variableName = "${}";
		
		// when
		String actual = PropertiesUtil.getKeyFromVariableName(variableName);
		
		// then
		Asserts.assertNull(actual);
	}
	
	@Test
	public void setVariableValuesSuccess() {
		// given
		Map<Object, Object> properties = new HashMap<>();
		properties.put("hello", "world");
		properties.put("hi", "${hello}");
		properties.put("welcome", "hello}");
		properties.put("value", 1);
		properties.put("null", null);
		properties.put("abc", "${xyz}");
		
		// when
		PropertiesUtil.setVariableValues(properties);
		
		// then
		Map<Object, Object> expectation = new HashMap<>();
		expectation.put("hello", "world");
		expectation.put("hi", "world");
		expectation.put("welcome", "hello}");
		expectation.put("value", 1);
		expectation.put("null", null);
		expectation.put("abc", "${xyz}");
		Asserts.assertEquals(expectation, properties);
	}
	
	@Test
	public void readVariableFile() {
		// given
		Map<Object, Object> properties = new BaseFileReader()
				.read("v112_application.yaml");
		
		// when
		PropertiesUtil.setVariableValues(properties);
		
		// then
		Map<Object, Object> expectation = new HashMap<>();
		expectation.put("app.hello", "world");
		expectation.put("app.hi", "world");
		Asserts.assertEquals(expectation, properties, false);
	}
}
