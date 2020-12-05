package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.util.Date;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.struct.PropertiesBean;

import lombok.Data;

public class PropertiesBeanTest {

	@Test
	public void test() {
		new PropertiesBean(ClassA.class);

		ClassA classA = new ClassA();
		PropertiesBean mapping2 = new PropertiesBean(classA, new PropertyAnnotations());
		
		mapping2.put("charValue", "c");
		mapping2.put("byteValue", "1");
		mapping2.put("doubleValue", "2.0");
		mapping2.put("floatValue", "3.0");
		mapping2.put("longValue", "4");
		mapping2.put("shortValue", "5");
		mapping2.put("not found", "not found");
		
		assertEquals(classA.getCharValue(), 'c');
		assertEquals(classA.getByteValue(), 1);
		assertEquals(classA.getDoubleValue(), 2.0D);
		assertEquals(classA.getFloatValue(), 3.0F);
		assertEquals(classA.getLongValue(), 4);
		assertEquals(classA.getShortValue(), 5);
		
		Properties dataSourceProperties = new Properties();
		dataSourceProperties.put("datasource.username", "hello");
		dataSourceProperties.put("datasource.password", "world");
		
		PropertiesBean mapping3 = new PropertiesBean(classA, new PropertyAnnotations());
		mapping3.put("dataSourceConfig", null, dataSourceProperties);
		assert classA.dataSourceConfig.username.equals("hello");
		assert classA.dataSourceConfig.password.equals("world");
	}
	
	@Test
	public void testParseDate() {
		PropertiesBean mapping = new PropertiesBean(ClassB.class);
		mapping.put("date", "abc");
	}
	
	@Data
	public static class ClassA {
		public String value;
		public char charValue;
		public byte byteValue;
		public double doubleValue;
		public float floatValue;
		public long longValue;
		public short shortValue;
		@Property(prefix = "datasource")
		public DataSourceConfig dataSourceConfig;
	}
	
	@Data
	public static class ClassB {
		public Date date;
	}
	
	@Data
	public static class DataSourceConfig {
		private String username;
		private String password;
	}
	
}
