package com.monkey.properties.file;

import org.junit.Before;
import org.junit.Test;

import com.monkey.properties.file.model.Constants;
import com.monkey.properties.file.model.UserActionHandler;
import com.tvd12.properties.file.ClassFetcher;
import com.tvd12.properties.file.PropertiesFileException;

import static org.junit.Assert.*;

public class ClassFetcherTest {

	private ClassFetcher fetcher;
	
	@Before
	public void init() throws PropertiesFileException {
		fetcher = ClassFetcher.newInstance(getClass(), "user_action_handlers.properties");
	}
	
	@Test
	public void testGetClassWithValidData() 
			throws PropertiesFileException, InstantiationException, IllegalAccessException {
		Class<UserActionHandler> handler1 = 
				fetcher.getClass(Constants.BETTING_ACTION);
		assertNotNull(handler1);
		
		handler1 = fetcher.getClass("" + Constants.BETTING_ACTION);
		assertNotNull(handler1);
		handler1.newInstance().handle(1000);
	}
}
