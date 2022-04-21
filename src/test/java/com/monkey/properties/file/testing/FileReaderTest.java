package com.monkey.properties.file.testing;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.reader.FileReader;
import com.tvd12.test.assertion.Asserts;

import lombok.AllArgsConstructor;

public class FileReaderTest {

    private static final Properties PROPERTIES = new Properties();

    @Test
    public void readByClassLoaderAndFileCloseFailed() throws Exception {
        // given
        InternalFileReader sut = new InternalFileReader(false);

        ClassLoader classLoader = mock(ClassLoader.class);
        String file = "config.properties";

        InputStream inputStream = mock(InputStream.class);
        doThrow(new IOException("just test")).when(inputStream).close();
        when(classLoader.getResourceAsStream(file)).thenReturn(inputStream);

        Properties actual = sut.read(classLoader, file);

        // then
        Asserts.assertEmpty(actual);
        verify(classLoader, times(1)).getResourceAsStream(file);
    }

    @Test
    public void readFailed() throws Exception {
        // given
        InternalFileReader sut = new InternalFileReader(true);

        File file = new File("src/test/resources/application.yaml");

        Properties actual = sut.read(file);

        // then
        Asserts.assertEmpty(actual);
    }

    @AllArgsConstructor
    private static class InternalFileReader implements FileReader {

        private final boolean exception;

        @Override
        public Properties loadInputStream(
            InputStream inputStream, String contentType) {
            return PROPERTIES;
        }

        @Override
        public Properties loadInputStreamOrThrows(
            InputStream inputStream, String contentType) throws IOException {
            if (exception)
                throw new IOException("just test");
            return PROPERTIES;
        }
    }
}
