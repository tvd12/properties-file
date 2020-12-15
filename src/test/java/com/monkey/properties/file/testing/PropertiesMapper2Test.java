package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.io.Dates;
import com.tvd12.properties.file.mapping.PropertiesMapper;

import lombok.Data;

public class PropertiesMapper2Test {

    @Test
    public void testMapPropertiesToBean() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class.getName());
        properties.put("date", new SimpleDateFormat(Dates.getPattern()).format(new Date()));
        properties.put("x", "123");
        properties.put("valid", "true");
        
        ClassA object = new PropertiesMapper()
                .data(properties)
                .clazz(ClassA.class)
                .map();
        assertEquals(object.isValid(), true);
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
        assertEquals(object.getString(), "123");
        assertEquals(object.getString2(), "st2");
        assertEquals(object.getNotExists(), null);
        
        PropertiesMapper propertiesMapper = new PropertiesMapper()
                .data(properties)
                .clazz(ClassA.class);
        propertiesMapper.map(ClassA.class);
        ClassA objectx =  propertiesMapper.map(ClassA.class);
        assertEquals(objectx.getName(), "hello");
        assertEquals(objectx.getAge(), 24);
        assertEquals(objectx.getMoney(), 10);
        assertEquals(objectx.getClazz(), ClassA.class);
        assertNotNull(objectx.getDate());
        assertEquals(objectx.getString(), "123");
        assertEquals(objectx.getString2(), "st2");
        assertEquals(objectx.getNotExists(), null);
        
        PropertiesMapper propertiesMapperx = new PropertiesMapper()
                .data(properties)
                .bean(new ClassA());
        propertiesMapper.map(ClassA.class);
        ClassA objectxx =  propertiesMapperx.map(ClassA.class);
        assertEquals(objectxx.getName(), "hello");
        assertEquals(objectxx.getAge(), 24);
        assertEquals(objectxx.getMoney(), 10);
        assertEquals(objectxx.getClazz(), ClassA.class);
        assertNotNull(objectxx.getDate());
        assertEquals(objectxx.getString(), "123");
        assertEquals(objectxx.getString2(), "st2");
        assertEquals(objectxx.getNotExists(), null);
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
    	
    	@Property("notExist")
    	private String notExists;
    	
    	@Property("valid")
    	private boolean valid;
    	
    	@Property
    	public void setA(String a) {
    		
    	}
    }
    
    public static class ClassB {
        protected ClassB() {}
    }
}
