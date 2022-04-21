package com.tvd12.properties.file.exception;

/**
 * An api exception
 * 
 * @author tavandung12
 *
 */
public class PropertiesFileException extends IllegalStateException {
    private static final long serialVersionUID = -2357054884838061118L;

    /**
     * constructor
     * 
     * @param msg message
     */
    public PropertiesFileException(String msg) {
        super(msg);
    }
    
    /**
     * constructor
     * 
     * @param msg message
     * @param cause exception
     */
    public PropertiesFileException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
}
