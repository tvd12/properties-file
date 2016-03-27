package com.monkey.properties.file;

public class PropertiesFileException extends Exception {
	private static final long serialVersionUID = -2357054884838061118L;

	public PropertiesFileException(String msg) {
		super(msg);
	}
	
	public PropertiesFileException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
