package com.tvd12.properties.file.testing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.testng.annotations.Test;

import com.tvd12.properties.file.util.InputStreamUtil;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.reflect.MethodInvoker;

import static org.mockito.Mockito.*;

public class InputStreamUtilTest {

    @Test
    public void getResourceAsStreamInOthersTest() {
        // given
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String resource = "application.yaml";

        // when
        InputStream actual = MethodInvoker.create()
            .staticClass(InputStreamUtil.class)
            .method("getResourceAsStreamInOthers")
            .param(ClassLoader.class, classLoader)
            .param(String.class, resource)
            .invoke(InputStream.class);
        // then
        Asserts.assertNotNull(actual);
    }

    @Test
    public void getResourceAsStreamInOthersIOExceptionTest() throws Exception {
        // given
        ClassLoader classLoader = mock(ClassLoader.class);
        String resource = "application.yaml";

        when(classLoader.getResources(resource)).thenThrow(new IOException("just test"));

        // when
        InputStream actual = MethodInvoker.create()
            .staticClass(InputStreamUtil.class)
            .method("getResourceAsStreamInOthers")
            .param(ClassLoader.class, classLoader)
            .param(String.class, resource)
            .invoke(InputStream.class);
        // then
        Asserts.assertNotNull(actual);
        verify(classLoader, times(1)).getResources(resource);
        verify(classLoader, times(1)).getResource(resource);
    }

    @Test
    public void getResourceAsStreamInOthersExceptionTest() throws Exception {
        // given
        ClassLoader classLoader = mock(ClassLoader.class);
        String resource = "application.yaml";

        when(classLoader.getResource(resource)).thenThrow(new IllegalStateException("just test"));

        // when
        InputStream actual = MethodInvoker.create()
            .staticClass(InputStreamUtil.class)
            .method("getResourceAsStreamInOthers")
            .param(ClassLoader.class, classLoader)
            .param(String.class, resource)
            .invoke(InputStream.class);
        // then
        Asserts.assertNotNull(actual);
        verify(classLoader, times(1)).getResources(resource);
        verify(classLoader, times(1)).getResource(resource);
    }

    @Test
    public void getResourceAsStreamInOthersOpenExceptionTest() throws Exception {
        // given
        ClassLoader classLoader = mock(ClassLoader.class);
        String resource = "https://localhost:001";

        when(classLoader.getResource(resource)).thenReturn(new URL(resource));

        // when
        InputStream actual = MethodInvoker.create()
            .staticClass(InputStreamUtil.class)
            .method("getResourceAsStreamInOthers")
            .param(ClassLoader.class, classLoader)
            .param(String.class, resource)
            .invoke(InputStream.class);
        // then
        Asserts.assertNull(actual);
        verify(classLoader, times(1)).getResources(resource);
        verify(classLoader, times(1)).getResource(resource);
    }
}
