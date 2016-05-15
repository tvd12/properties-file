/**
 * 
 */
package com.tvd12.properties.file.writer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;

import com.tvd12.properties.file.exception.PropertiesFileException;

/**
 * @author tavandung12
 *
 */
public interface FileWriter {

    /**
     * Write properties to ByteArrayOutputStream
     * 
     * @param properties properties object 
     * @return a ByteArrayOutputStream object
     * @throws PropertiesFileException when can't write
     */
    ByteArrayOutputStream write(Properties properties) throws PropertiesFileException;

    /**
     * Write properties object to file
     * 
     * @param properties properties object
     * @param filePath file path to write
     * @throws PropertiesFileException when can't write
     */
    void write(Properties properties, String filePath) throws PropertiesFileException;

    /**
     * Write properties object to file
     * 
     * @param properties properties object
     * @param file file to write
     * @throws PropertiesFileException when can't write
     */
    void write(Properties properties, File file) throws PropertiesFileException;

}