package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.util.Date;

import org.testng.annotations.Test;

import com.tvd12.properties.file.struct.PropertiesBean;

import lombok.Data;

public class PropertiesBeanTest {

	@Test
	public void test() {
		PropertiesBean mapping1 = new PropertiesBean();
		mapping1.init(ClassA.class);
		mapping1.init(ClassA.class);

		ClassA classA = new ClassA();
		PropertiesBean mapping2 = new PropertiesBean();
		mapping2.init(classA);
		mapping2.init(new ClassA());
		
		mapping2.put("charValue", "c");
		mapping2.put("byteValue", "1");
		mapping2.put("doubleValue", "2.0");
		mapping2.put("floatValue", "3.0");
		mapping2.put("longValue", "4");
		mapping2.put("shortValue", "5");
		
		assertEquals(classA.getCharValue(), 'c');
		assertEquals(classA.getByteValue(), 1);
		assertEquals(classA.getDoubleValue(), 2.0D);
		assertEquals(classA.getFloatValue(), 3.0F);
		assertEquals(classA.getLongValue(), 4);
		assertEquals(classA.getShortValue(), 5);
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
	}
	
	@Data
	public static class ClassB {
		public Date date;
	}
	
}
