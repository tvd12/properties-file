package com.monkey.properties.file.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.testng.annotations.Test;

import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.writer.BaseFileWriter;

public class BaseFileWriterTest {

    @Test
    public void test1() {
        String filePath1 = "BaseFileWriterTestFile1.txt";
        File file1 = new File(filePath1);
        if (file1.exists()) {
            file1.delete();
        }
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "2");
        BaseFileWriter fileWriter = new BaseFileWriter();
        fileWriter.write(properties, file1);
    }

    @Test(expectedExceptions = {PropertiesFileException.class})
    public void test2() {
        String filePath1 = "BaseFileWriterTestFile2.txt";
        File file1 = new File(filePath1) {
            private static final long serialVersionUID = -1227507502469046517L;

            @Override
            public boolean createNewFile() throws IOException {
                throw new IOException();
            }
        };
        if (file1.exists()) {
            file1.delete();
        }
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "2");
        BaseFileWriter fileWriter = new BaseFileWriter();
        fileWriter.write(properties, file1);
    }

    @Test(expectedExceptions = {PropertiesFileException.class})
    public void test3() {
        String filePath1 = "BaseFileWriterTestFile3.txt";
        final File file1 = new File(filePath1);
        if (file1.exists()) {
            file1.delete();
        }
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "2");
        BaseFileWriter fileWriter = new BaseFileWriter() {
            @Override
            protected FileOutputStream newFileOutputStream(File file) throws FileNotFoundException {
                return new FileOutputStream(file) {
                    @Override
                    public void write(byte[] b) throws IOException {
                        throw new IOException();
                    }
                };
            }
        };
        fileWriter.write(properties, file1);
    }

    @Test(expectedExceptions = {PropertiesFileException.class})
    public void test4() {
        String filePath1 = "BaseFileWriterTestFile3.txt";
        final File file1 = new File(filePath1);
        if (file1.exists()) {
            file1.delete();
        }
        Properties properties = new Properties();
        properties.setProperty("a", "1");
        properties.setProperty("b", "2");
        BaseFileWriter fileWriter = new BaseFileWriter() {
            @Override
            protected FileOutputStream newFileOutputStream(File file) throws FileNotFoundException {
                return new FileOutputStream(file) {
                    @Override
                    public void close() throws IOException {
                        throw new IOException();
                    }
                };
            }
        };
        fileWriter.write(properties, file1);
    }
}
