package com.monkey.properties.file.testing;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.writer.Base64FileWriter;
import com.tvd12.properties.file.writer.BaseFileWriter;

public class PropertiesFileWriterTest {

    @Test
    public void test() throws PropertiesFileException {
        Properties properties = new Properties();
        properties.setProperty("hello", "word");
        new BaseFileWriter().write(properties, "hello.output_test");
    }
    
    @Test
    public void testBase64() throws PropertiesFileException {
        Properties properties = new Properties();
        properties.setProperty("hello", "word");
        new Base64FileWriter().write(properties, "hello_base64.test_output");
    }
    
    @Test(expectedExceptions = {PropertiesFileException.class})
    public void writeToOuputStreamInvalidCase() throws PropertiesFileException, IOException {
        Properties properties = mock(Properties.class);
        doThrow(IOException.class).when(properties).store(any(OutputStream.class), any(String.class));
        new BaseFileWriter().write(properties);
    }
    
    @Test(expectedExceptions = {PropertiesFileException.class})
    public void writeToFileInvalidCase() throws PropertiesFileException, IOException {
        Properties properties = new Properties();
        File file = mock(File.class);
        when(file.exists()).thenReturn(true);
        when(file.isDirectory()).thenReturn(true);
        new BaseFileWriter().write(properties, file);
    }
    
}
