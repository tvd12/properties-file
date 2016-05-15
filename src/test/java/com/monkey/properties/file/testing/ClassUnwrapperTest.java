package com.monkey.properties.file.testing;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.struct.ClassUnwrapper;

public class ClassUnwrapperTest {

    @Test
    public void test() {
        new ClassUnwrapper(ClassA.class);
    }
    
    @Test(expectedExceptions = {IllegalStateException.class})
    public void testNewInstanceInvalidCase() {
        new ClassUnwrapper(ClassA.class).newInstance();
    }
    
    public static class ClassA {
        
        protected ClassA() {}
        
        @Property
        public String getString() {
            return "abc";
        }
        
        @Property
        public String getString1(String abc) {
            return abc;
        }
        
        @Property
        public void setString1(String abc) {
        }
        
        @Property
        public void getString3() {
        }
    }
}
