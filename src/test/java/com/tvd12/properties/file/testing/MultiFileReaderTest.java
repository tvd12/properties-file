package com.tvd12.properties.file.testing;

import java.io.File;
import java.util.Arrays;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.reader.FileReader;
import com.tvd12.properties.file.reader.MultiFileReader;
import com.tvd12.test.assertion.Asserts;

public class MultiFileReaderTest {

    @Test
    public void readClassPathFiles() {
        FileReader reader = new MultiFileReader(Arrays.asList("alpha", ""));
        Properties properties = reader.read("application.yaml");
        System.out.println(properties);
        Asserts.assertNull(properties.get("include.profiles"));
        assert properties.get("cors.allow_origin").equals("*");
        assert properties.get("cors.enable").equals("true");
        assert properties.get("server.port").equals("3005");
        assert properties.get("server.host").equals("1.1.1.1");
        assert properties.get("server.admin.username").equals("admin");
        assert properties.get("server.admin.password").equals("123456");
        assert properties.get("hello").equals("world");
        assert properties.get("foo").equals("bar");
    }

    @Test
    public void readEmptyProfiles() {
        FileReader reader = new MultiFileReader("alpha,");
        Properties properties = reader.read("application1.properties");
        Asserts.assertNull(properties.get("include.profiles"));
    }

    @Test
    public void readNoProfiles() {
        FileReader reader = new MultiFileReader();
        Properties properties = reader.read("application1.properties");
        Asserts.assertNull(properties.get("include.profiles"));
    }

    @Test
    public void readSystemFiles() {
        FileReader reader = new MultiFileReader(Arrays.asList("alpha", ""));
        Properties properties = reader.read(new File("test/config/application.yaml"));
        System.out.println(properties);
        Asserts.assertNull(properties.get("include.profiles"));
        assert properties.get("cors.allow_origin").equals("*");
        assert properties.get("cors.enable").equals("true");
        Asserts.assertEquals("3006", properties.get("server.port"));
        assert properties.get("server.host").equals("2.2.2.2");
        assert properties.get("server.admin.username").equals("admin");
        assert properties.get("server.admin.password").equals("123456");
        assert properties.get("hello").equals("world");
        assert properties.get("foo").equals("bar");
    }
}
