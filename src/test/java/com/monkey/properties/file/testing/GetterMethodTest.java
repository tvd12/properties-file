package com.monkey.properties.file.testing;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.struct.GetterMethod;
import com.tvd12.test.reflect.MethodBuilder;

import static org.testng.Assert.*;

import lombok.Data;

public class GetterMethodTest {

    @Test
    public void test() {
        Method method = MethodBuilder.create()
                .clazz(ClassA.class)
                .method("getName")
                .build();
        GetterMethod getterMethod = new GetterMethod(new PropertyAnnotations());
        getterMethod.initWithMethod(method);
        assertEquals(getterMethod.getType(), String.class);
        assertEquals(getterMethod.getField(), null);
        assertEquals(getterMethod.getMethodName(), "getName");
    }
    
    @Test
    public void getMethodByFieldInvalidCaseTest() throws NoSuchFieldException, SecurityException {
        Field field = ClassB.class.getDeclaredField("name");
        GetterMethod getterMethod = new GetterMethod(new PropertyAnnotations());
        getterMethod.initWithField(ClassB.class, field);
    }
    
    @Test
    public void invokeTest() {
        Method method = MethodBuilder.create()
                .clazz(ClassA.class)
                .method("getName")
                .build();
        GetterMethod getterMethod = new GetterMethod(new PropertyAnnotations());
        getterMethod.initWithMethod(method);
        assertNull(getterMethod.invoke(new ClassA()));
    }
    
    @Test(expectedExceptions = {IllegalStateException.class})
    public void invokeInvalidCaseTest() {
        Method method = MethodBuilder.create()
                .clazz(ClassA.class)
                .method("setName")
                .argument(String.class)
                .build();
        GetterMethod getterMethod = new GetterMethod(new PropertyAnnotations());
        getterMethod.initWithMethod(method);
        assertNull(getterMethod.invoke(new ClassA()));
    }
    
    @Data
    public static class ClassA {
        private String name;
        private String value;
        
        public void setValues(String a, String b) {
            
        }
    }
    
    public static class ClassB {
        protected String name;
    }
    
}
