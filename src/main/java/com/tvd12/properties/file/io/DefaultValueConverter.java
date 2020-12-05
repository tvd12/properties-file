package com.tvd12.properties.file.io;

public final class DefaultValueConverter extends SimpleValueConverter {
	
	private static final DefaultValueConverter INSTANCE = 
			new DefaultValueConverter();
	
	private DefaultValueConverter() {}
	
	public static DefaultValueConverter getInstance() {
		return INSTANCE;
	}
	
}
