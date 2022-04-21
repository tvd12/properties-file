package com.monkey.properties.file.testing;

import java.io.File;

import org.testng.annotations.Test;

import com.tvd12.properties.file.util.FileUtil;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class FileUtilTest extends BaseTest {

    @Test
    public void test() {
        assert FileUtil.getFileExtension("filename") == null;
        assert FileUtil.getFileExtension("filename.") == null;
        assert FileUtil.getFileExtension("filename.yaml").equals("yaml");
    }

    @Test
    public void getFilePathByProfileFullPath() {
        // given
        String filePath = "hello/world.properties";
        String profile = "alpha";

        // when
        String newFilePath = FileUtil.getFilePathByProfile(filePath, profile);

        // then
        String expected = "hello/world-alpha.properties";
        Asserts.assertEquals(expected, newFilePath);
    }

    @Test
    public void getFilePathByProfileNoParent() {
        // given
        String filePath = "world.properties";
        String profile = "alpha";

        // when
        String newFilePath = FileUtil.getFilePathByProfile(filePath, profile);

        // then
        String expected = "world-alpha.properties";
        Asserts.assertEquals(expected, newFilePath);
    }

    @Test
    public void getFilePathByProfileNoExtension() {
        // given
        String filePath = "hello/world";
        String profile = "alpha";

        // when
        String newFilePath = FileUtil.getFilePathByProfile(filePath, profile);

        // then
        String expected = "hello/world-alpha";
        Asserts.assertEquals(expected, newFilePath);
    }

    @Test
    public void getFilePathByProfileStartWithDot() {
        // given
        String filePath = "hello/.properties";
        String profile = "alpha";

        // when
        String newFilePath = FileUtil.getFilePathByProfile(filePath, profile);

        // then
        String expected = "hello/alpha.properties";
        Asserts.assertEquals(expected, newFilePath);
    }

    @Test
    public void getFileClasspathByProfileTest1() {
        // given
        String filePath = "hello/world.properties";
        String profile = "alpha";

        // when
        String newFilePath = FileUtil.getFileClasspathByProfile(filePath, profile);

        // then
        String expected = "hello/world-alpha.properties";
        Asserts.assertEquals(expected, newFilePath);
    }

    @Test
    public void getFileClasspathByProfileTest2() {
        // given
        String filePath = "hello\\world.properties";
        String profile = "alpha";

        // when
        String newFilePath = FileUtil.getFileClasspathByProfile(filePath, profile);

        // then
        String expected = "hello/world-alpha.properties";
        Asserts.assertEquals(expected, newFilePath);
    }

    @Test
    public void getFileSytemByProfile1() {
        // given
        File file = new File("hello/world.properties");
        String profile = "alpha";

        // when
        File newFile = FileUtil.getFileSytemByProfile(file, profile);

        // then
        File expected = new File("hello/world-alpha.properties");
        Asserts.assertEquals(expected, newFile);
    }

    @Test
    public void getFileSytemByProfile2() {
        // given
        File file = new File("hello\\world.properties");
        String profile = "alpha";

        // when
        File newFile = FileUtil.getFileSytemByProfile(file, profile);

        // then
        File expected = new File("hello\\world-alpha.properties");
        Asserts.assertEquals(expected, newFile);
    }

    @Override
    public Class<?> getTestClass() {
        return FileUtil.class;
    }
}
