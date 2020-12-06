package com.monkey.properties.file.testing;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.tvd12.properties.file.constant.Constants;
import com.tvd12.properties.file.util.InputStreamUtil;
import static org.mockito.Mockito.*;

public class InputStreamTest {
	
	@Test
	public void test() {
		InputStream inputStream = getClass().getResourceAsStream("/classes.properties");
		System.out.println(inputStream);
	}
	
	@Test
	public void guessContentTypeNotFoundTest() {
		byte[] bytes = new byte[0];
		InputStream inputStream = new ByteArrayInputStream(bytes);
		assert InputStreamUtil.guessContentType(inputStream).equals(Constants.FILE_EXTENSION_PROPERTIES);
	}
	
	@Test
	public void guessContentTypeNotFoundMaxReadTest() {
		byte[] bytes = new byte[1024];
		InputStream inputStream = new ByteArrayInputStream(bytes);
		assert InputStreamUtil.guessContentType(inputStream).equals(Constants.FILE_EXTENSION_PROPERTIES);
	}
	
	@Test
	public void guessContentTypeNotFoundExceptionTest() throws Exception {
		InputStream inputStream = Mockito.spy(InputStream.class);
		when(inputStream.markSupported()).thenReturn(true);
		when(inputStream.read()).thenThrow(new IOException("just test"));
		assert InputStreamUtil.guessContentType(inputStream).equals(Constants.FILE_EXTENSION_PROPERTIES);
	}
}
