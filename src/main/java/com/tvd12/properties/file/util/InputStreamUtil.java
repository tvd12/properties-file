package com.tvd12.properties.file.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import com.tvd12.properties.file.constant.Constants;
import com.tvd12.properties.file.exception.PropertiesFileException;

public final class InputStreamUtil {

    private InputStreamUtil() {}

    /**
     * Get a input stream from a file.
     * Find in class loader first, if not found, find in find system.
     *
     * @param classLoader the class loader
     * @param filePath    the file path
     * @return an InputStream object
     */
    public static InputStream getInputStream(
        ClassLoader classLoader,
        String filePath
    ) {
        InputStream inputStream = getResourceAsStream(classLoader, filePath);
        if (inputStream == null) {
            inputStream = getResourceAsStream(classLoader, "/" + filePath);
        }
        if (inputStream == null) {
            inputStream = getInputStreamByAbsolutePath(filePath);
        }
        if (inputStream == null) {
            throw new PropertiesFileException("Can not read file in path " + filePath + ", maybe it's not found");
        }
        return inputStream;
    }

    /**
     * Get a input stream from a file in project.
     *
     * @param classLoader the class loader
     * @param filePath    the file path in class path
     * @return an InputStream object
     */
    private static InputStream getResourceAsStream(
        ClassLoader classLoader,
        String filePath
    ) {
        ClassLoader acceptClassLoader = classLoader;
        if (acceptClassLoader == null) {
            acceptClassLoader = getDefaultClassLoader();
        }
        InputStream ip = acceptClassLoader.getResourceAsStream(filePath);
        if (ip == null) {
            ip = ClassLoader.getSystemResourceAsStream(filePath);
        }
        if (ip == null) {
            ip = getResourceAsStreamInOthers(acceptClassLoader, filePath);
        }
        return ip;
    }

    /**
     * Get the resource input stream from other jar files.
     *
     * @param classLoader the class loader
     * @param resource    the resource to get the stream
     * @return the input stream or null
     */
    private static InputStream getResourceAsStreamInOthers(
        ClassLoader classLoader,
        String resource
    ) {
        List<URL> resourcesUrls = new ArrayList<>();
        try {
            addResourceUrls(resourcesUrls, () -> classLoader.getResources(resource));
            addResourceUrls(resourcesUrls, () -> ClassLoader.getSystemResources(resource));
            addResourceUrl(resourcesUrls, classLoader.getResource(resource));
            addResourceUrl(resourcesUrls, ClassLoader.getSystemResource(resource));
        } catch (Exception e) {
            // do nothing
        }
        try {
            return resourcesUrls.isEmpty() ? null : resourcesUrls.get(0).openStream();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Add resource URLs.
     *
     * @param resourcesUrls the resource URLs output
     * @param supplier      the resource URLs suppliers
     */
    private static void addResourceUrls(
        List<URL> resourcesUrls,
        UrlsSupplier supplier
    ) {
        try {
            Enumeration<URL> urls = supplier.get();
            addResourceUrls(resourcesUrls, urls);
        } catch (Exception e) {
            // do nothing
        }
    }

    /**
     * Add resource URLs.
     *
     * @param resourcesUrls the resource URLs output
     * @param urls         the resource URLs to add
     */
    private static void addResourceUrls(
        List<URL> resourcesUrls,
        Enumeration<URL> urls
    ) {
        if (urls != null) {
            while (urls.hasMoreElements()) {
                resourcesUrls.add(urls.nextElement());
            }
        }
    }

    /**
     * Add resource URLs.
     *
     * @param resourcesUrls the resource URLs output
     * @param url           the resource URL to add
     */
    private static void addResourceUrl(
        List<URL> resourcesUrls,
        URL url
    ) {
        if (url != null) {
            resourcesUrls.add(url);
        }
    }

    /**
     * Get a input stream from a file if it exists and be readable.
     *
     * @param filePath the file path
     * @return an input stream object
     */
    private static InputStream getInputStreamByAbsolutePath(
        String filePath
    ) {
        return getInputStreamByAbsolutePath(new File(filePath));
    }

    /**
     * Get a input stream from a file if it exists and be readable.
     *
     * @param file the file
     * @return an input stream object
     */
    public static InputStream getInputStreamByAbsolutePath(File file) {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * Get default class loader.
     *
     * @return the current thread's class loader
     */
    public static ClassLoader getDefaultClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * Gets the contents of an <code>InputStream</code> as a <code>byte[]</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * </p>
     *
     * @param input the <code>InputStream</code> to read from
     * @return the requested byte array
     * @throws NullPointerException if the input is null
     * @throws IOException          if an I/O error occurs
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        int readBytes = 0;
        byte[] buffer = new byte[128];
        while (true) {
            int nbyte = input.read();
            if (nbyte == -1) {
                break;
            }
            buffer[readBytes++] = (byte) nbyte;
            if (readBytes >= buffer.length) {
                byte[] newBuffer = new byte[buffer.length + 128];
                System.arraycopy(buffer, 0, newBuffer, 0, readBytes);
                buffer = newBuffer;
            }
        }
        byte[] answer = new byte[readBytes];
        System.arraycopy(buffer, 0, answer, 0, readBytes);
        return answer;
    }

    /**
     * Guest content type from an InputStream.
     *
     * @param inputStream the InputStream
     * @return the content type
     */
    public static String guessContentType(InputStream inputStream) {
        if (!inputStream.markSupported()) {
            return Constants.FILE_EXTENSION_PROPERTIES;
        }
        int maxRead = 255;
        inputStream.mark(maxRead);
        int i = 0;
        String contenType = Constants.FILE_EXTENSION_PROPERTIES;
        try {
            while ((i++) < maxRead) {
                int b = inputStream.read();
                if (b == -1) {
                    break;
                }
                if (b == '=') {
                    break;
                } else if (b == ':') {
                    contenType = Constants.FILE_EXTENSION_YAML;
                    break;
                }
            }
            inputStream.reset();
        } catch (IOException e) {
            // do nothing
        }
        return contenType;
    }

    private interface UrlsSupplier {
        Enumeration<URL> get() throws IOException;
    }
}
