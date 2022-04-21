package com.tvd12.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.properties.file.util.ReflectionGenericUtil;
import com.tvd12.test.base.BaseTest;

@SuppressWarnings("rawtypes")
public class ReflectionGenericUtilTest extends BaseTest {

    @Test
    public void getGenericClassArgumentsWithNotParameterizedType() throws Exception {
        // given
        int size = 2;
        Field field = ClassA.class.getDeclaredField("name");

        // when
        Class[] actual = ReflectionGenericUtil
            .getGenericClassArguments(field.getGenericType(), size);

        // then
        Class[] expected = new Class[size];
        assertEquals(actual, expected);
    }

    @Test
    public void getGenericClassArgumentsWithDiffSize() throws Exception {
        // given
        int size = 1;
        Field field = ClassA.class.getDeclaredField("values1");

        // when
        Class[] actual = ReflectionGenericUtil
            .getGenericClassArguments(field.getGenericType(), size);

        // then
        Class[] expected = new Class[size];
        assertEquals(actual, expected);
    }

    @Test
    public void getGenericClassArgumentsSuccess() throws Exception {
        // given
        int size = 2;
        Field field = ClassA.class.getDeclaredField("values1");

        // when
        Class[] actual = ReflectionGenericUtil
            .getGenericClassArguments(field.getGenericType(), size);

        // then
        Class[] expected = new Class[]{String.class, Integer.class};
        assertEquals(actual, expected);
    }

    @Test
    public void getGenericClassArgumentsWithNoGenericType() throws Exception {
        // given
        int size = 2;
        Field field = ClassA.class.getDeclaredField("values3");

        // when
        Class[] actual = ReflectionGenericUtil
            .getGenericClassArguments(field.getGenericType(), size);

        // then
        Class[] expected = new Class[]{null, String.class};
        assertEquals(actual, expected);
    }

    @Override
    public Class<?> getTestClass() {
        return ReflectionGenericUtil.class;
    }

    public static class ClassA {
        public String name;
        public Map<String, Integer> values1;
        public Map values2;
        public Map<?, String> values3;
    }
}
