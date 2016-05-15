package com.tvd12.properties.file.writer;

import java.io.ByteArrayOutputStream;

import org.apache.commons.codec.binary.Base64;

public class Base64FileWriter extends BaseFileWriter {

    /**
     * @see com.tvd12.properties.file.writer.BaseFileWriter#encode(ByteArrayOutputStream)
     */
    @Override
    protected byte[] encode(ByteArrayOutputStream out) {
        return Base64.encodeBase64(out.toByteArray());
    }
    
}
