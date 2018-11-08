package com.tvd12.properties.file.writer;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Base64.Encoder;

public class Base64FileWriter extends BaseFileWriter {

    /**
     * @see com.tvd12.properties.file.writer.BaseFileWriter#encode(ByteArrayOutputStream)
     */
    @Override
    protected byte[] encode(ByteArrayOutputStream out) {
    		Encoder encoder = Base64.getEncoder();
    		byte[] outBytes = out.toByteArray();
    		byte[] answer = encoder.encode(outBytes);
    		return answer;
    }
    
}
