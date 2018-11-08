package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.annotation.PropertyWrapper;
import com.tvd12.properties.file.struct.PropertiesBean;
import com.tvd12.test.base.BaseTest;

import lombok.Data;

public class PropertiesClassTest extends BaseTest {
    
    @Test
    public void notWrapperTest() {
        PropertiesBean map = new PropertiesBean(ClassA.class);
        assertEquals(map.getWriteMethod("n").getName(), "setName");
        map.put("length", 1);
        map.put("value", "zzz");
        map.put("value", "yyy");
    }
    
    @Test
    public void wrapperTest() {
//        ClassWrapper wrapper = new ClassWrapper(ClassB.class);
//        ClassUnwrapper unwrapper = new ClassUnwrapper(ClassB.class);
//        PropertiesBean map = new PropertiesBean();
//        map.setWrapper(wrapper);
//        map.setUnwrapper(unwrapper);
//        map.setBean(wrapper.newInstance());
//        assertTrue(map.containsKey("n"));
//        assertTrue(map.containsKey("msg"));
//        assertEquals(map.getReadMethod("n").getName(), "getName");
//        assertEquals(map.getWriteMethod("n").getName(), "setName");
//        assertEquals(map.put("length", 1), 0);
//        assertEquals(map.put("value", "zzz"), null);
//        assertEquals(map.put("value", "yyy"), "zzz");
//        assertEquals(map.get("visible"), true);
//        assertEquals(map.keySet().size(), 8);
    }
    
    @Test
    public void wrapperTest2() {
        PropertiesBean map = new PropertiesBean(ClassC.class);
        assertEquals(map.getWriteMethod("name").getName(), "setName");
        map.put("length", 1);
        map.put("value", "zzz");
        map.put("value", "yyy");
        map.put("clazz", ClassA.class);
        map.put("classD", new ClassD(1));
    }
    
    @Test
    public void transformInvalidCaseTest() {
        PropertiesBean map = new PropertiesBean(ClassC.class);
        map.put("classD", "classD");
    }
    
    @Test
    public void transformClazzInvalidCaseTest() {
        PropertiesBean map = new PropertiesBean(ClassE.class);
        map.put("clazz", "classD");
    }
    
    @Test
    public void transformClazzValidCaseTest() {
        PropertiesBean map = new PropertiesBean(ClassE.class);
        map.put("clazz", ClassA.class.toString());
    }
    
    public static void main(String[] args) {
        PropertiesBean map = new PropertiesBean(ClassE.class);
        map.put("clazz", ClassA.class.toString());
    }
    
    @Data
    public static class ClassA {
        @Property("n")
        private String name;
        
        @Property
        private String value;
        
        private int length;
        
        @Property("")
        public boolean isVisible() {
            return true;
        }
        
        @Property("hsv")
        public boolean hasValue() {
            return true;
        }
        
        @Property("msg")
        public String getMessage() {
            return "hello";
        }
        
        @Property("msg")
        public void setMessage(String msg) {
            
        }
    }
    
    @Data
    @PropertyWrapper
    public static class ClassB {
        @Property("n")
        private String name;
        
        @Property
        private String value;
        
        private int length;
        
        @Property("")
        public boolean isVisible() {
            return true;
        }
        
        @Property("hsv")
        public boolean hasValue() {
            return true;
        }
        
        @Property("msg")
        public String getMessage() {
            return "hello";
        }
        
        @Property("msg")
        public void setMessage(String msg) {
            
        }
    }
    
    @Data
    public static class ClassC {
        private String name;
        
        private String value;
        
        private int length;
        
        private Class<?> clazz;
        
        private ClassD classD;
        
        public boolean isVisible() {
            return true;
        }
        
        public boolean hasValue1() {
            return true;
        }
        
        public String getMessage() {
            return "hello";
        }
        
        public void setMessage(String msg) {
            
        }
    }
    
    public static class ClassD {
        public ClassD(String value) {
            throw new IllegalArgumentException();
        }
        
        public ClassD(int a) {
        }
    }
    
    @Data
    public static class ClassE {
        private Class<?> clazz;
    } 
    
}
