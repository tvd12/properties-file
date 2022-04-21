package com.tvd12.properties.file.reader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.tvd12.properties.file.exception.PropertiesFileException;
import com.tvd12.properties.file.util.InputStreamUtil;

public class PropertiesInputStreamReader implements InputStreamReader {

    @Override
    public Properties readInputStream(InputStream inputStream) {
        Properties properties = new Properties();
        try {
            byte[] contentBytes = InputStreamUtil.toByteArray(inputStream);
            ByteArrayInputStream stream = new ByteArrayInputStream(contentBytes);
            try {
                properties.load(stream);
            } finally {
                stream.close();
                inputStream.close();
            }
        } catch (IOException e) {
            throw new PropertiesFileException("Can not read properties file", e);
        }
        return properties;
    }
}
