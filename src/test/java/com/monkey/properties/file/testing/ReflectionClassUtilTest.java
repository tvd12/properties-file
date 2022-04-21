package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Set;

import org.testng.annotations.Test;

import com.tvd12.properties.file.util.ReflectionClassUtil;
import com.tvd12.test.base.BaseTest;

public class ReflectionClassUtilTest extends BaseTest {

    @Override
    public Class<?> getTestClass() {
        return ReflectionClassUtil.class;
    }
    
    @Test
    public void test1() {
        Set<Method> publicMethods = ReflectionClassUtil.getPublicMethods(ClassA.class);
        assertEquals(publicMethods.size(), 1);
    }
    
    @Test
    public void test2() {
        ReflectionClassUtil.flatSuperClasses(InterfaceA.class);
        ReflectionClassUtil.flatSuperClasses(ClassA.class, true);
    }
    
    @Test
    public void getNoArgsDeclaredConstructorTest() {
        assert ReflectionClassUtil
            .getNoArgsDeclaredConstructor(BX.class)
            .getParameterCount() == 0;
    }
    
    @Test
    public void getNoArgsDeclaredConstructorWithDefaultConstructorTest() {
        assert ReflectionClassUtil
            .getNoArgsDeclaredConstructor(CX.class)
            .getParameterCount() == 0;
    }
    
    @Test
    public void getNoArgsDeclaredConstructorNullTest() {
        assert ReflectionClassUtil
            .getNoArgsDeclaredConstructor(DX.class) == null;
    }
    
    @Test
    public void getMaxArgsDeclaredConstructor() {
        assert ReflectionClassUtil
            .getMaxArgsDeclaredConstructor(BX.class)
            .getParameterCount() == 2;
    }
    
    @Test(expectedExceptions = IllegalStateException.class)
    public void newInstanceByConstructorError() {
        ReflectionClassUtil.newInstance(
                ReflectionClassUtil.getMaxArgsDeclaredConstructor(BX.class)
                , 1, 2, 3);
    }
    
    public interface InterfaceA {
    }
    
    public abstract static class ClassA {
        
        public abstract void love();
        
        public void setString() {
        }
        
    }
    
    public static class BX {
        protected BX(String name) {
            this(name, "value");
        }
        
        private BX(String name, String value) {}
        
        public BX() {
            this("name");
        }
    }
    
    public static class CX {}
    
    public static class DX {
        public DX(String name) {}
    }
    
}
