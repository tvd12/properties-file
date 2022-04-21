package com.tvd12.properties.file.writer;

import com.tvd12.properties.file.constant.Constants;
import com.tvd12.properties.file.exception.PropertiesFileException;

import java.io.*;
import java.util.Properties;

/**
 * Support to write properties object to file.
 *
 * @author tavandung12
 */
public class BaseFileWriter implements FileWriter {

    /* (non-Javadoc)
     * @see com.tvd12.properties.file.writer.FileWriter#write(java.util.Properties)
     */
    @Override
    public ByteArrayOutputStream write(Properties properties) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            properties.store(out, Constants.COMMENT);
        } catch (IOException e) {
            throw new PropertiesFileException(
                "Can not write properties to output stream",
                e
            );
        }
        return out;
    }

    /* (non-Javadoc)
     * @see com.tvd12.properties.file.writer.FileWriter#write(java.util.Properties, java.lang.String)
     */
    @Override
    public void write(Properties properties, String filePath) {
        write(properties, new File(filePath));
    }

    /* (non-Javadoc)
     * @see com.tvd12.properties.file.writer.FileWriter#write(java.util.Properties, java.io.File)
     */
    @Override
    public void write(Properties properties, File file) {
        try (ByteArrayOutputStream out = write(properties)) {
            if (!file.exists()) {
                file.createNewFile();
            }
            byte[] bytes = encode(out);
            writeBytes0(file, bytes);
        } catch (IOException e) {
            throw new PropertiesFileException("Can not write properties to file", e);
        }
    }

    /**
     * write byte array to file.
     *
     * @param file  the file
     * @param bytes the byte array
     * @throws IOException exception
     */
    protected void writeBytes0(File file, byte[] bytes) throws IOException {
        try (FileOutputStream fos = newFileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    /**
     * new file output stream.
     *
     * @param file the file
     * @return a file input stream
     * @throws FileNotFoundException exception
     */
    protected FileOutputStream newFileOutputStream(File file) throws FileNotFoundException {
        return new FileOutputStream(file);
    }

    /**
     * end code data before writing.
     *
     * @param out a ByteArrayOutputStream object
     * @return encoded byte array
     */
    protected byte[] encode(ByteArrayOutputStream out) {
        return out.toByteArray();
    }
}
