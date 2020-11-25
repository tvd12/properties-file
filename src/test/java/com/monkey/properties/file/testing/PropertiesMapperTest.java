package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.util.PropertiesUtil;

import lombok.Data;

public class PropertiesMapperTest {

    @Test
    public void testMapPropertiesToBean() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class);
        properties.put("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        
        ClassA object = new PropertiesMapper()
                .data(properties)
                .clazz(ClassA.class)
                .map();
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
    }
    
    @Test
    public void testMapPropertiesToBean2() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class);
        properties.put("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        
        ClassA object = new PropertiesMapper()
                .data(PropertiesUtil.toMap(properties))
                .bean(new ClassA())
                .map();
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
    }
    
    @Test
    public void testMapPropertiesToBeanWithMapClazz() {
        Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassA.class);
        properties.put("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        
        ClassA object = new PropertiesMapper()
                .data(PropertiesUtil.toMap(properties))
                .map(ClassA.class);
        assertEquals(object.getName(), "hello");
        assertEquals(object.getAge(), 24);
        assertEquals(object.getMoney(), 10);
        assertEquals(object.getClazz(), ClassA.class);
        assertNotNull(object.getDate());
    }
    
    @Test
    public void testMapPropertiesToBeanWithFields() {
    	Properties properties = new Properties();
        properties.setProperty("name", "hello");
        properties.put("age", 24);
        properties.put("clazz", ClassC.class);
        properties.put("date", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date()));
        
        ClassC object = new PropertiesMapper()
                .data(PropertiesUtil.toMap(properties))
                .map(ClassC.class);
        assertEquals(object.name, "hello");
        assertEquals(object.age, 24);
        assertEquals(object.money, 10);
        assertEquals(object.clazz, ClassC.class);
        assertNotNull(object.date);
    }
    
    @Test
    public void testWithNoClassCase() {
    	Properties properties = new Properties();
    	Object output = new PropertiesMapper()
    		.data(properties)
    		.map();
    	assert properties == output;
    }
    
    @Test(expectedExceptions = {IllegalStateException.class})
    public void newBeanInstanceInvalidCaseTest() {
        new PropertiesMapper()
                .data(new Properties())
                .clazz(ClassB.class)
                .map();
    }
    
    @Test(expectedExceptions = {IllegalStateException.class})
    public void getPropertiesInvalidCaseTest() {
        new PropertiesMapper()
            .file("xyz/classes.properties")
            .reader(new BaseFileReader())
            .clazz(ClassA.class)
            .context(getClass())
            .map();
    }
    
    @Test
    public void mapBase64FileToObject() {
        new PropertiesMapper()
                .file("room-config.properties")
                .clazz(ExampleRoom.class)
                .reader(new BaseFileReader())
                .map();
    }
    
    @Data
    public static class ClassA {
        private String name;
        private int age;
        private long money = 10;
        
        private Date date;
        
        private Class<?> clazz;
    }
    
    public static class ClassB {
        protected ClassB() {}
    }
    
    public static class ClassC {
        public String name;
        public int age;
        protected long money = 10;
        
        protected Date date;
        
        protected Class<?> clazz;
    }
}
