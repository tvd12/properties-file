package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.monkey.properties.file.testing.model.Person;
import com.monkey.properties.file.testing.model.UpdateUser;
import com.monkey.properties.file.testing.model.User;
import com.tvd12.properties.file.reader.InstanceFactory;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodInvoker;

public class InstanceFactoryTest extends BaseTest {

	public InstanceFactoryTest() {
		InstanceFactory.defaultInstance().initialize("classes.properties");
		InstanceFactory.defaultInstance().initialize("classes.properties");
	}
	
	@Test
	public void testWithValidData() {
	    InstanceFactory.defaultInstance().initialize("classes.properties");
		Person person1 = InstanceFactory.defaultInstance().getPrototype(Person.class);
		Person person2 = InstanceFactory.defaultInstance().getPrototype(Person.class);
		assertNotNull(person1);
		assertNotEquals(person1, person2);
		assertEquals(InstanceFactory.defaultInstance().getErrors().size(), 0);
		
		User user = InstanceFactory.defaultInstance().getPrototype(User.class);
		assertNotNull(user);
		
		assertNull(InstanceFactory.defaultInstance().getPrototype(Class.class));
	}
	
	@Test
	public void testGetInstanceWithProtectedConstructor() {
	    InstanceFactory.defaultInstance().getErrors().clear();
	    InstanceFactory.defaultInstance().getPrototype(UpdateUser.class);
	    assertEquals(InstanceFactory.defaultInstance().getErrors().size(), 1);
	    InstanceFactory.defaultInstance().getErrors().clear();
	}
	
	@Test
	public void testGetSingleton() {
		Person person1 = InstanceFactory.defaultInstance().getSingleton(Person.class);
		Person person2 = InstanceFactory.defaultInstance().getSingleton(Person.class);
		
		assertTrue(person1 == person2);
		assertEquals(person1, person2);
		
		System.out.println(person1 + "\n" + person2);
	}
	
	@Test
	public void testInstance() {
		InstanceFactory factory = InstanceFactory.create("classes.properties");
		
		Person person1 = factory.getPrototype(Person.class);
		Person person2 = factory.getPrototype(Person.class);
		assertNotNull(person1);
		assertNotEquals(person1, person2);
		assertEquals(InstanceFactory.defaultInstance().getErrors().size(), 0);
		
		person1 = factory.getSingleton(Person.class);
		person2 = factory.getSingleton(Person.class);
		
		assertTrue(person1 == person2);
		assertEquals(person1, person2);
		
		System.out.println(person1 + "\n" + person2);
	}
	
	@Test
	public void forNameTest() {
	    MethodInvoker.create()
	        .object(InstanceFactory.defaultInstance())
	        .method("forName")
	        .param("abc")
	        .invoke();
	    assertEquals(InstanceFactory.defaultInstance().getErrors().size(), 1);
	    InstanceFactory.defaultInstance().getErrors().clear();
	}
	
	@Test
	public void initTest() {
	    MethodInvoker.create()
	        .object(InstanceFactory.defaultInstance())
	        .method("init")
	        .param(getClass())
	        .param(new String[] {"abc"})
	        .invoke();
	    assertEquals(InstanceFactory.defaultInstance().getErrors().size(), 1);
        InstanceFactory.defaultInstance().getErrors().clear();
        InstanceFactory.defaultInstance().initialize("classes.properties");
	}
}
