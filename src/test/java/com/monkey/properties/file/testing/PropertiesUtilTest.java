package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.util.PropertiesUtil;
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
    
}
