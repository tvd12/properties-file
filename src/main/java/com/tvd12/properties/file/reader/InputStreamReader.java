package com.tvd12.properties.file.reader;

import java.io.InputStream;
import java.util.Properties;

public interface InputStreamReader {

    Properties readInputStream(InputStream inputStream);
}
