package com.monkey.properties.file.testing;

import org.testng.annotations.Test;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.mapping.MappingLevel;
import com.tvd12.properties.file.struct.ClassUnwrapper;

import lombok.Data;

public class ClassUnwrapperTest {

    @Test
    public void test() {
        new ClassUnwrapper(ClassA.class, MappingLevel.ALL);
        new ClassUnwrapper(ClassB.class, MappingLevel.ALL);
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
    
    @Data
    public class ClassB {
		@Property
		public String value;
		
		@Property
		public boolean hasValue() {
			return true;
		}
		
		@Property
		public boolean isNow() {
			return true;
		}
    }
}
