package com.monkey.properties.file.testing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

import org.testng.annotations.Test;

import com.monkey.properties.file.testing.model.Constants;
import com.monkey.properties.file.testing.model.UserActionHandler;
import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.reader.ClassFetcher;
import com.tvd12.test.reflect.MethodBuilder;
import com.tvd12.test.reflect.MethodInvoker;

public class ClassFetcherTest {

	private ClassFetcher fetcher;
	
	public ClassFetcherTest() throws PropertiesFileException {
	    fetcher = new ClassFetcher.Builder()
	            .context(getClass())
	            .file("user_action_handlers.properties")
	            .files("user_action_handlers2.properties")
	            .files(Lists.newArrayList("user_action_handlers2.properties"))
	            .reader(new BaseFileReader())
	            .build();
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void loadInvalidCaseTest() {
	    new ClassFetcher.Builder()
	        .context(getClass())
	        .file("user_action_handlers.properties")
	        .reader(new BaseFileReader() {
	            @Override
	            public List<Properties> read(Class<?> entry, String... propertiesFiles)
	                    throws PropertiesFileException {
	                throw new PropertiesFileException("");
	            }
	        })
	        .build();
	}
	
	@Test
	public void testGetClassWithValidData() 
			throws PropertiesFileException, InstantiationException, IllegalAccessException {
		Class<UserActionHandler> handler1 = 
				fetcher.fetch(Constants.BETTING_ACTION);
		assertNotNull(handler1);
		
		handler1 = fetcher.fetch("" + Constants.BETTING_ACTION);
		assertNotNull(handler1);
		handler1.newInstance().handle(1000);
		assertEquals(2, fetcher.fetch().size());
		assertNull(fetcher.fetch("abcd"));
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
    public void testGetClassWithInvalidData() throws PropertiesFileException {
	    MethodInvoker.create()
	        .object(fetcher)
	        .method("fetch")
	        .param(new Properties())
	        .param(new Object())
	        .invoke();
	}
	
	@Test(expectedExceptions = {IllegalStateException.class})
    public void testGetClassWithInvalidData2() throws PropertiesFileException {
        Properties pro = new Properties();
        pro.setProperty("a", "a.b.c");
        Method method = MethodBuilder.create()
                .clazz(ClassFetcher.class)
                .method("fetch")
                .argument(Properties.class)
                .argument(Object.class)
                .build();
        MethodInvoker.create()
            .object(fetcher)
            .method(method)
            .param(pro)
            .param("a")
            .invoke();
    }
}
