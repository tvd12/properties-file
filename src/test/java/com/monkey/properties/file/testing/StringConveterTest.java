package com.monkey.properties.file.testing;

import org.testng.annotations.Test;

import com.tvd12.properties.file.io.StringConverter;
import com.tvd12.test.base.BaseTest;

public class StringConveterTest extends BaseTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void stringToCharFailed() {
        StringConverter.stringToChar("");
    }
    
    @Override
    public Class<?> getTestClass() {
        return StringConverter.class;
    }
    
}
