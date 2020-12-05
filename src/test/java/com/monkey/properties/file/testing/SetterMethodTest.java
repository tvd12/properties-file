package com.monkey.properties.file.testing;

import java.lang.reflect.Method;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.PropertyAnnotations;
import com.tvd12.properties.file.struct.SetterMethod;
import com.tvd12.test.reflect.MethodBuilder;

import lombok.Data;

public class SetterMethodTest {

    @Test
    public void test() {
        Method method = MethodBuilder.create()
                .clazz(ClassA.class)
                .method("setName")
                .argument(String.class)
                .build();
        SetterMethod setterMethod = new SetterMethod(new PropertyAnnotations());
        setterMethod.initWithMethod(method);
        setterMethod.invoke(new ClassA(), "123");
    }
    
    @Test(expectedExceptions = {IllegalStateException.class})
    public void testInvalidCase() {
        Method method = MethodBuilder.create()
                .clazz(ClassA.class)
                .method("setValues")
                .argument(String.class)
                .argument(String.class)
                .build();
        SetterMethod setterMethod = new SetterMethod(new PropertyAnnotations());
        setterMethod.initWithMethod(method);
        setterMethod.invoke(new ClassA(), "123");
    }
    
    @Data
    public static class ClassA {
        private String name;
        
        public void setValues(String value, String name) {
            
        }
    }
    
}
