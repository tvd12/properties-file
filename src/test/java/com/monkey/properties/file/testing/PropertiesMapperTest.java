package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reader.Base64FileReader;
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
    public void testMapBeanToProperties() {
        ClassA object = new ClassA();
        object.setName("hello");
        object.setAge(24);
        object.setMoney(1000);
        object.setClazz(ClassA.class);
        object.setDate(new Date());
        
//        Properties properties = new PropertiesBean(object)
//                .toProperties();
//        assertEquals(properties.getProperty("name"), "hello");
//        assertEquals(properties.get("age"), 24);
//        assertEquals(properties.get("money"), 1000L);
//        assertEquals(properties.get("clazz"), ClassA.class);
//        assertNotNull(properties.get("date"));
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
        ExampleRoom room = new PropertiesMapper()
                .file("room-config.properties")
                .clazz(ExampleRoom.class)
                .reader(new Base64FileReader())
                .map();
        assertEquals(room.getCapacity(), 1000);
        assertEquals(room.getId(), 1001);
        assertEquals(room.getMaxRoomVariablesAllowed(), 30);
        assertEquals(room.getMaxSpectators(), 50);
        assertEquals(room.getMaxUsers(), 100);
        assertEquals(room.getVariablesCount(), 0);
        assertEquals(room.isActive(), true);
        assertEquals(room.isDynamic(), true);
        assertEquals(room.isEmpty(), true);
        assertEquals(room.isFull(), false);
        assertEquals(room.isGame(), false);
        assertEquals(room.isHidden(), false);
        assertEquals(room.isPasswordProtected(), false);
        assertEquals(room.isPublic(), true);
        assertEquals(room.isUseWordsFilter(), false);
        assertEquals(room.getName(), "lobby");
        assertEquals(room.getPassword(), "123");
        assertEquals(room.getGroupdId(), "none");
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
}
