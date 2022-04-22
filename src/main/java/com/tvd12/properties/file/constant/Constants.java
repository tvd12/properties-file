package com.tvd12.properties.file.constant;

public final class Constants {

    public static final String COMMENT = "written by com.tvd12:properties-file";
    public static final String FILE_EXTENSION_YAML = "yaml";
    public static final String FILE_EXTENSION_PROPERTIES = "properties";
    public static final String PROFILES_KEY = "include.profiles";
    public static final String PROPERTIES_KEY_DECRYPTION_KEY = "properties.decryption_key";
    public static final String ENCRYPTION_PREFIX = "ENC(";
    public static final String ENCRYPTION_SUFFIX = ")";
    public static final int MIN_ENCRYPTED_MESSAGE_LENGTH =
        ENCRYPTION_PREFIX.length() + ENCRYPTION_SUFFIX.length();

    private Constants() {}
}
