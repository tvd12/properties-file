package com.monkey.properties.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.monkey.properties.file.InstanceFactory;
import com.monkey.properties.file.model.Person;

public class InstanceFactoryTest {

	@Before
	public void init() {
		InstanceFactory.initialize("classes.properties");
	}
	
	@Test
	public void testWithValidData() {
		Person person = InstanceFactory.newInstance(Person.class);
		assertNotNull(person);
		assertEquals(InstanceFactory.getErrors().size(), 0);
	}
	
}
