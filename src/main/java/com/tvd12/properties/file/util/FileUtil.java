package com.tvd12.properties.file.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FileUtil {

    private FileUtil() {}

    public static String getFileExtension(String filePath) {
        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return null;
        }
        if (lastDotIndex >= filePath.length() - 1) {
            return null;
        }
        return filePath.substring(lastDotIndex + 1);
    }

    public static String getFileClasspathByProfile(String filePathString, String profile) {
        return getFilePathByProfile(filePathString, profile)
            .replace('\\', '/');
    }

    public static File getFileSytemByProfile(File file, String profile) {
        return new File(getFilePathByProfile(file.toString(), profile));
    }

    public static String getFilePathByProfile(String filePathString, String profile) {
        final Path filePath = Paths.get(filePathString);
        final String fileName = filePath.getFileName().toString();
        String fileExtension = null;
        String actualFileName = fileName;
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex >= 0) {
            fileExtension = fileName.substring(lastDotIndex + 1);
            actualFileName = lastDotIndex > 0 ? fileName.substring(0, lastDotIndex) : null;
        }
        final String newActualFileName = actualFileName == null
            ? profile
            : actualFileName + "-" + profile;
        final String newFileName = fileExtension == null
            ? newActualFileName
            : newActualFileName + "." + fileExtension;
        final Path folderPath = filePath.getParent();
        return folderPath == null
            ? newFileName
            : Paths.get(folderPath.toString(), newFileName).toString();
    }
}
