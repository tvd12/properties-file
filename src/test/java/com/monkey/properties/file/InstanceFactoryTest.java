package com.monkey.properties.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.monkey.properties.file.model.Person;

public class InstanceFactoryTest {

	@Before
	public void init() {
		InstanceFactory.initialize("classes.properties");
	}
	
	@Test
	public void testWithValidData() {
		Person person1 = InstanceFactory.newInstance(Person.class);
		Person person2 = InstanceFactory.newInstance(Person.class);
		assertNotNull(person1);
		assertNotEquals(person1, person2);
		assertEquals(InstanceFactory.getErrors().size(), 0);
	}
	
	@Test
	public void testGetSingleton() {
		Person person1 = InstanceFactory.getSingleton(Person.class);
		Person person2 = InstanceFactory.getSingleton(Person.class);
		
		assertTrue(person1 == person2);
		assertEquals(person1, person2);
		
		System.out.println(person1 + "\n" + person2);
	}
	
	@Test
	public void testInstance() {
		InstanceFactory factory = InstanceFactory.create("classes.properties");
		
		Person person1 = factory.getInstance(Person.class);
		Person person2 = factory.getInstance(Person.class);
		assertNotNull(person1);
		assertNotEquals(person1, person2);
		assertEquals(InstanceFactory.getErrors().size(), 0);
		
		person1 = factory.getSingletonInstance(Person.class);
		person2 = factory.getSingletonInstance(Person.class);
		
		assertTrue(person1 == person2);
		assertEquals(person1, person2);
		
		System.out.println(person1 + "\n" + person2);
	}
}
