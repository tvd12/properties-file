package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.testng.annotations.Test;
import org.testng.collections.Sets;

import com.tvd12.properties.file.util.PropertiesUtil;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class PropertiesUtilTest extends BaseTest {

    @Override
    public Class<?> getTestClass() {
        return PropertiesUtil.class;
    }
    
    @Test
    public void getPropertiesByPrefixTest() {
    	Properties properties = new Properties();
    	properties.put("datasource", "database");
    	properties.put("datasource.username", "hello");
    	Properties expected = new Properties();
    	expected.put("", "database");
    	expected.put("username", "hello");
    	assertEquals(PropertiesUtil.getPropertiesByPrefix(properties, "datasource"), expected);
    }
    
    @Test
    public void getPropertiesByEmptyPrefixTest() {
    	Properties properties = new Properties();
    	properties.put("datasource", "database");
    	properties.put("datasource.username", "hello");
    	assertEquals(PropertiesUtil.getPropertiesByPrefix(properties, ""), properties);
    }
    
    @Test
    public void getFirstPropertyKeysWithFirstDotIndex0() {
    	// given
    	Properties properties = new Properties();
    	properties.put(".", "database");
    	properties.put("hello", "world");
    	
    	// when
    	Set<String> actual = PropertiesUtil.getFirstPropertyKeys(properties);
    	
    	// then
    	Set<String> expected = new HashSet<>(Arrays.asList(".", "hello"));
    	assertEquals(actual, expected);
    }
    
    @Test
    public void filterPropertiesByPrefixTest() {
    	Properties properties = new Properties();
    	properties.put("datasource", "database");
    	properties.put("datasource.username", "hello");
    	properties.put("cache.username", "hello");
    	properties.put("cache.pass", "pass");
    	Properties expected = new Properties();
    	expected.put("datasource", "database");
    	expected.put("datasource.username", "hello");
    	assertEquals(PropertiesUtil.filterPropertiesByKeyPrefix(properties, "datasource"), expected);
    }
    
    @Test
    public void setVariableTest() {
        // given
        Properties properties = new Properties();
        properties.put("datasource", "${hell.world}");
        properties.put("hell.world", "hello");
        
        // when
        PropertiesUtil.setVariableValues(properties);
        
        // then
        Properties expectation = new Properties();
        expectation.put("datasource", "hello");
        expectation.put("hell.world", "hello");
        Asserts.assertEquals(properties, expectation);
    }
    
    @Test
    public void setVariableInValidTest() {
        // given
        Properties properties = new Properties();
        properties.put("datasource", "database: ${username}${password}${}} url ${not found}}");
        properties.put("username", "foo");
        properties.put("password", "bar");
        
        // when
        PropertiesUtil.setVariableValues(properties);
        
        // then
        Properties expectation = new Properties();
        expectation.put("datasource", "database: foobar${}} url ${not found}}");
        expectation.put("username", "foo");
        expectation.put("password", "bar");
        Asserts.assertEquals(properties, expectation);
    }
    
    @Test
    public void getKeysFromVariableNameTest() {
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName(""));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("${}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("${"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("$ {}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("{}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("${abc"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("abc}"));
        Asserts.assertEquals(
            PropertiesUtil.getKeysFromVariableName("${a}, ${b}, ${c}"),
            Sets.newHashSet("a", "b", "c")
        );
    }
}
