package com.tvd12.properties.file.io;

public interface ValueConverter {

	<T> T convert(Object value, Class<T> outType);
	
}
