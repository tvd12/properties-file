package com.monkey.properties.file.testing;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.properties.file.struct.ClassWrapper;

import lombok.Data;

public class ClassStructTest {

	@Test
	public void testContainsKey() {
		ClassWrapperA wrapper = new ClassWrapperA(A.class);
		assertTrue(wrapper.containsKey("value"));
		assertFalse(wrapper.containsKey("abccc"));
		assertEquals(wrapper.keySet().size(), 1);
		assertEquals(wrapper.methodCount(), 1);
	}
	
	public static class ClassWrapperA extends ClassWrapper {

		public ClassWrapperA(Class<?> clazz) {
			super(clazz);
		}
		
		@Override
		public boolean containsKey(String key) {
			return super.containsKey(key);
		}
		
	}
	
	@Data
	public static class A {
		
		public String value;
		
	}
	
}
