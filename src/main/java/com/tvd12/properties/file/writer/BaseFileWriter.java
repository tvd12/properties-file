package com.tvd12.properties.file.writer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

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
        try {
        		if(!file.exists())
        			file.createNewFile();
        		ByteArrayOutputStream out = write(properties);
        		byte[] bytes = encode(out);
        		writeBytes0(file, bytes);
        }
        catch(IOException e) {
            throw new PropertiesFileException("Can not write properties to file", e);
        }
    }
    
    /**
     * write byte array to file
     * 
     * @param file the file
     * @param bytes the byte array
     * @throws IOException exception
     */
    protected void writeBytes0(File file, byte[] bytes) throws IOException {
    		FileOutputStream fos = newFileOutputStream(file);
    		try {
    			fos.write(bytes);
    		}
    		finally {
    			fos.close();
		}
    }
    
    /**
     * new file output stream
     * 
     * @param file the file
     * @return a file input stream
     * @throws FileNotFoundException exception
     */
    protected FileOutputStream newFileOutputStream(File file) throws FileNotFoundException {
    		return new FileOutputStream(file);
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
