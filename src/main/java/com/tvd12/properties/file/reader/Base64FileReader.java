package com.tvd12.properties.file.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Base64.Decoder;

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
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        Decoder decoder = Base64.getDecoder();
		byte[] answer = decoder.decode(bytes);
		return answer;
    }
    
}
