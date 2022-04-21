package com.tvd12.properties.file.writer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;

/**
 * Properties file writer.
 *
 * @author tavandung12
 */
public interface FileWriter {

    /**
     * Write properties to ByteArrayOutputStream.
     *
     * @param properties properties object
     * @return a ByteArrayOutputStream object
     */
    ByteArrayOutputStream write(Properties properties);

    /**
     * Write properties object to file.
     *
     * @param properties properties object
     * @param filePath file path to write
     */
    void write(Properties properties, String filePath);

    /**
     * Write properties object to file.
     *
     * @param properties properties object
     * @param file file to write
     */
    void write(Properties properties, File file);
}