package com.tvd12.properties.file.writer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.tvd12.properties.file.constant.Constants;
import com.tvd12.properties.file.exception.PropertiesFileException;

/**
 * 
 * Support to write properties object to file
 * 
 * @author tavandung12
 *
 */
public class BaseFileWriter implements FileWriter {
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.writer.FileWriter#write(java.util.Properties)
     */
    @Override
    public ByteArrayOutputStream write(Properties properties) 
            throws PropertiesFileException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            properties.store(out, Constants.COMMENT);
        } catch (IOException e) {
            throw new PropertiesFileException(
                    "Can not write properties to output stream", e);
        }
        return out;
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.writer.FileWriter#write(java.util.Properties, java.lang.String)
     */
    @Override
    public void write(Properties properties, String filePath) 
            throws PropertiesFileException {
        write(properties, new File(filePath));
    }
    
    /* (non-Javadoc)
     * @see com.tvd12.properties.file.writer.FileWriter#write(java.util.Properties, java.io.File)
     */
    @Override
    public void write(Properties properties, File file) 
            throws PropertiesFileException {
        ByteArrayOutputStream out = write(properties);
        try {
            FileUtils.writeByteArrayToFile(file, encode(out));
        }
        catch(IOException e) {
            throw new PropertiesFileException("Can not write properties to file", e);
        }
    }

    /**
     * end code data before writing
     * 
     * @param out a ByteArrayOutputStream object
     * @return encoded byte array
     */
    protected byte[] encode(ByteArrayOutputStream out) {
        return out.toByteArray();
    }
    
}
