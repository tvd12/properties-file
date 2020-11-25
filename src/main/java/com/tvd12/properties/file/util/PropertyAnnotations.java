package com.tvd12.properties.file.util;

import com.tvd12.properties.file.annotation.Property;

public final class PropertyAnnotations {

	private PropertyAnnotations() {}
	
	public static String getPrefix(Property propertyAnno) {
		if(propertyAnno.value().isEmpty() && 
				!propertyAnno.prefix().isEmpty())
			return propertyAnno.prefix();
		return "";
	}
	
}
