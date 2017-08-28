package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.mapping.PropertiesMapper;

import lombok.Data;

public class PropertiesMapper2Test {

    @Test
    public void testMapPropertiesToBean() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class);
        properties.put("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        properties.put("x", "123");
        
        ClassA object = new PropertiesMapper()
                .data(properties)
                .clazz(ClassA.class)
                .map();
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
        assertEquals(object.getString(), "123");
        assertEquals(object.getString2(), "st2");
    }
    
    @Data
    public static class ClassA {
    	@Property("name")
        private String name;
    	
    	@Property("age")
        private int age;
    	
    	@Property("money")
        private long money = 10;
        
    	@Property("date")
        private Date date;
        
    	@Property("clazz")
        private Class<?> clazz;
    	
    	@Property("x")
    	private String string;
    	
    	@Property("y")
    	private String string2 = "st2";
    }
    
    public static class ClassB {
        protected ClassB() {}
    }
}
