package com.tvd12.properties.file.reader;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

/**
 * Support for reading base64 encoded file
 * 
 * @author tavandung12
 *
 */
public class Base64FileReader extends BaseFileReader {

    
    /* 
     * @see com.tvd12.properties.file.reader.BaseFileReader#decode(java.io.InputStream)
     */
    @Override
    protected byte[] decode(InputStream inputStream) throws IOException {
        String str = IOUtils.toString(inputStream);
        return Base64.decodeBase64(str);
    }
    
}
