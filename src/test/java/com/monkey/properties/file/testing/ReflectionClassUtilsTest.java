package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Set;

import org.testng.annotations.Test;

import com.tvd12.properties.file.reflect.ReflectionClassUtils;
import com.tvd12.test.base.BaseTest;

public class ReflectionClassUtilsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return ReflectionClassUtils.class;
	}
	
	@Test
	public void test1() {
		Set<Method> publicMethods = ReflectionClassUtils.getPublicMethods(ClassA.class);
		assertEquals(publicMethods.size(), 1);
	}
	
	@Test
	public void test2() {
		ReflectionClassUtils.flatSuperClasses(InterfaceA.class);
		ReflectionClassUtils.flatSuperClasses(ClassA.class, true);
	}
	
	public static interface InterfaceA {
	}
	
	public abstract class ClassA {
		
		public abstract void love();
		
		public void setString() {
		}
		
	}
	
}
