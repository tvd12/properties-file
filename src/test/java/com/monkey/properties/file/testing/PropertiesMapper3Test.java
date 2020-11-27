package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.mapping.PropertiesMapper;

import lombok.Getter;
import lombok.Setter;

public class PropertiesMapper3Test {

	@Test
	public void test1() {
		Properties properties = new Properties();
		properties.setProperty("valueA", "a");
		properties.setProperty("valueB", "b");
		properties.setProperty("hello", "galaxy");
		properties.setProperty("welcome", "Dzung");
		ClassB obj = new PropertiesMapper()
			.clazz(ClassB.class)
			.context(getClass())
			.data(properties)
			.map();
		assertEquals(obj.getValueA(), "a");
		assertEquals(obj.getValueB(), "b");
	}
	
	@Test
	public void test2() {
		Properties properties = new Properties();
		properties.setProperty("valueC", "c");
		properties.setProperty("valueD", "d");
		properties.setProperty("hello", "galaxy");
		ClassD obj = new PropertiesMapper()
			.clazz(ClassD.class)
			.context(getClass())
			.data(properties)
			.map();
		assertEquals(obj.getValueC(), "c");
		assertEquals(obj.getValueD(), "d");
	}
	
	@Setter
	@Getter
	public static class ClassA {
		@Property
		private String valueA;
		
		@Property
		public void setHello(String world) {
			System.out.println("hello world: " + world);
		}
	}
	
	@Setter
	@Getter
	public static class ClassB extends ClassA implements InterfaceE  {
		@Property
		private String valueB;
		
		@Override
		public void setWelcome(String who) {
			System.out.println("welcome: " + who);
		}
	}
	
	@Setter
	@Getter
	public static class ClassC {
		private String valueC;
		
		public void setHello(String world) {
			System.out.println("hello world: " + world);
		}
	}
	
	@Setter
	@Getter
	public static class ClassD extends ClassC{
		private String valueD;
	}
	
	public static interface InterfaceE {
		@Property
		void setWelcome(String who);
	}
	
}
