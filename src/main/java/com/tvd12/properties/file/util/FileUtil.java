package com.tvd12.properties.file.util;

public final class FileUtil {

	private FileUtil() {}
	
	public static String getFileExtension(String filePath) {
		int lastDotIndex = filePath.lastIndexOf('.');
		if(lastDotIndex == -1)
			return null;
		if(lastDotIndex >= filePath.length() - 1)
			return null;
		return filePath.substring(lastDotIndex + 1);
	}
	
}
