package com.monkey.properties.file.testing;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.reader.Base64FileReader;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.MethodBuilder;
import com.tvd12.test.reflect.ReflectMethodUtil;

public class PropertiesFileReaderTest extends BaseTest {

	@Test
	public void testWithValidData() throws PropertiesFileException {
		assertNotNull(new BaseFileReader().read("classes.properties"));
		assertNotNull(new BaseFileReader().read(getClass(), "classes.properties"));
	}
	
	@Test
    public void testWithValidData1() throws PropertiesFileException {
        assertNotNull(new BaseFileReader().read("classes.properties", "classes.properties"));
        assertNotNull(new BaseFileReader().read(getClass(), "classes.properties", "classes.properties"));
        List<String> list = new ArrayList<>();
        list.add("classes.properties");
        list.add("classes.properties");
        assertNotNull(new BaseFileReader().read(getClass(), list));
    }
	
	@Test
    public void testWithValidData2() throws PropertiesFileException {
	    File file = new File(getClass()
                .getClassLoader()
                .getResource("classes.properties")
                .getFile());
        assertNotNull(new BaseFileReader().read(Lists.newArrayList(file, file)));
        assertNotNull(new BaseFileReader().read(Lists.newArrayList(file, file)));
    }
	
	@Test
    public void testWithValidData3() throws PropertiesFileException {
        assertNotNull(new BaseFileReader().loadInputStreams(
                Lists.newArrayList(getClass().getClassLoader().getResourceAsStream("classes.properties"))));
    }
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void testReadBase64Data() throws PropertiesFileException {
	    assertNotNull(new Base64FileReader().read("hello.properties"));
	}
	
	@Test(expectedExceptions = {PropertiesFileException.class})
	public void testWithInvalidData() throws PropertiesFileException {
		new BaseFileReader().read(getClass(), "classes1.properties");
	}
	
	@Test(expectedExceptions = {PropertiesFileException.class})
    public void readFromInputStreamTest() throws PropertiesFileException {
        new BaseFileReader().read(getClass(), "/xyz/classes.properties");
    }
	
	@SuppressWarnings("unchecked")
    @Test(expectedExceptions = {PropertiesFileException.class})
	public void loadInputStreamTest() throws PropertiesFileException, IOException {
	    InputStream stream = mock(InputStream.class);
	    when(stream.read(any(byte[].class))).thenThrow(IOException.class);
	    new BaseFileReader().loadInputStream(stream);
	}
	
	@Test
	public void getInputStreamByAbsolutePathTest() {
	    Method method = MethodBuilder.create()
                .clazz(BaseFileReader.class)
                .method("getInputStreamByAbsolutePath")
                .argument(File.class)
                .build();
	    ReflectMethodUtil.invokeMethod(
	            method,
	            new BaseFileReader(),
	            new File(getClass().getResource("/invalid.properties").getFile()));
	    
	    ReflectMethodUtil.invokeMethod(
                method, 
                new BaseFileReader(),
                new ExFile("abc"));
	}
    
    @Override
    public Class<?> getTestClass() {
        return BaseFileReader.class;
    }
    
    public static class ExFile extends File {
        private static final long serialVersionUID = 1L;
        public ExFile(String pathname) {
            super(pathname);
        }

        @Override
        public boolean exists() {
            return true;
        }
        
    }
	
}
