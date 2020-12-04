package com.tvd12.properties.file.yaml;

public class YamlInvalidSyntaxException extends RuntimeException {
	private static final long serialVersionUID = -797301090393627844L;
	
	public YamlInvalidSyntaxException(String msg) {
		super(msg);
	}
}
