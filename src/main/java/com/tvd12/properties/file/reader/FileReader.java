package com.tvd12.properties.file.reader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import com.tvd12.properties.file.util.FileUtil;
import com.tvd12.properties.file.util.InputStreamUtil;

public interface FileReader {

    /**
     * Read properties file in a path.
     *
     * @param filePath properties file path
     * @param context  which class to get resource as stream
     * @return properties object
     */
    default Properties read(Class<?> context, String filePath) {
        return read(context.getClassLoader(), filePath);
    }

    /**
     * Read properties file in a path.
     *
     * @param filePath    properties file path
     * @param classLoader the class loader
     * @return properties object
     */
    default Properties read(ClassLoader classLoader, String filePath) {
        try (InputStream inputStream = InputStreamUtil.getInputStream(classLoader, filePath)) {
            return loadInputStreamOrThrows(
                inputStream,
                FileUtil.getFileExtension(filePath)
            );
        } catch (IOException e) {
            return new Properties();
        }
    }

    /**
     * Read properties files in multiple paths.
     *
     * @param filePaths list of properties files
     * @param context   which class to get resource as stream
     * @return list of properties object
     */
    default List<Properties> read(Class<?> context, String... filePaths) {
        return read(context.getClassLoader(), filePaths);
    }

    /**
     * Read properties files in multiple paths.
     *
     * @param filePaths   list of properties files
     * @param classLoader the class loader
     * @return list of properties object
     */
    default List<Properties> read(ClassLoader classLoader, String... filePaths) {
        List<Properties> result = new ArrayList<>();
        for (String file : filePaths) {
            result.add(read(classLoader, file));
        }
        return result;
    }

    /**
     * Read properties files in multiple paths.
     *
     * @param filePaths list of properties files
     * @param context   which class to get resource as stream
     * @return list of properties object
     */
    default List<Properties> read(Class<?> context, Collection<String> filePaths) {
        return read(context.getClassLoader(), filePaths);
    }

    /**
     * Read properties files in multiple paths.
     *
     * @param filePaths   list of properties files
     * @param classLoader the class loader
     * @return list of properties object
     */
    default List<Properties> read(ClassLoader classLoader, Collection<String> filePaths) {
        return read(classLoader, filePaths.toArray(new String[0]));
    }

    /**
     * Read properties file in a path.
     *
     * @param filePath properties file path
     * @return properties object
     */
    default Properties read(String filePath) {
        return read(InputStreamUtil.getDefaultClassLoader(), filePath);
    }

    /**
     * Read properties file.
     *
     * @param file properties file
     * @return properties object
     */
    default Properties read(File file) {
        try (InputStream inputStream = InputStreamUtil.getInputStreamByAbsolutePath(file)) {
            if (inputStream != null) {
                return loadInputStreamOrThrows(
                    inputStream,
                    FileUtil.getFileExtension(file.getPath())
                );
            }
        } catch (IOException e) {
            // do nothing
        }
        return new Properties();
    }

    /**
     * Read properties files in multiple paths.
     *
     * @param filePaths list of properties files
     * @return array of properties object
     */
    default List<Properties> read(String... filePaths) {
        return read(InputStreamUtil.getDefaultClassLoader(), filePaths);
    }

    /**
     * Read properties files.
     *
     * @param files array of properties files
     * @return list of properties object
     */
    default List<Properties> read(File... files) {
        List<Properties> result = new ArrayList<>();
        for (File file : files) {
            result.add(read(file));
        }
        return result;
    }

    /**
     * Read properties files.
     *
     * @param files array of properties files
     * @return list of properties object
     */
    default List<Properties> read(Collection<File> files) {
        return read(files.toArray(new File[0]));
    }

    /**
     * Load an input stream and read properties.
     *
     * @param inputStream the input stream
     * @return properties the properties
     */
    default Properties loadInputStream(InputStream inputStream) {
        return loadInputStream(inputStream, null);
    }

    /**
     * Load an input stream and read properties.
     *
     * @param inputStream the input stream
     * @param contentType the content type
     * @return properties the properties
     */
    Properties loadInputStream(InputStream inputStream, String contentType);

    /**
     * Load an input stream and read properties.
     *
     * @param inputStream the input stream
     * @param contentType the content type
     * @return properties the properties
     */
    default Properties loadInputStreamOrThrows(
        InputStream inputStream, String contentType) throws IOException {
        return loadInputStream(inputStream, contentType);
    }

    /**
     * Read properties files.
     *
     * @param inputStreams array of properties files
     * @return list of properties object
     */
    default List<Properties> loadInputStreams(InputStream... inputStreams) {
        List<Properties> result = new ArrayList<>();
        for (InputStream inputStream : inputStreams) {
            result.add(loadInputStream(inputStream));
        }
        return result;
    }

    /**
     * Read properties files.
     *
     * @param inputStreams array of properties files
     * @return list of properties object
     */
    default List<Properties> loadInputStreams(Collection<InputStream> inputStreams) {
        return loadInputStreams(inputStreams.toArray(new InputStream[0]));
    }
}
