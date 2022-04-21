package com.tvd12.properties.file.encryption;

import com.tvd12.properties.file.util.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AesDecrypter {

    private static final AesDecrypter INSTANCE = new AesDecrypter();
    private static final String ENCRYPTION_PREFIX = "ENC(";
    private static final String ENCRYPTION_SUFFIX = ")";
    private static final int MIN_ENCRYPTED_MESSAGE_LENGTH =
        ENCRYPTION_PREFIX.length() + ENCRYPTION_SUFFIX.length();

    private AesDecrypter() {}

    public static AesDecrypter getInstance() {
        return INSTANCE;
    }

    public String decryptOrIgnore(String message, String key) {
        if (message == null) {
            return null;
        }
        if (message.startsWith(ENCRYPTION_PREFIX)
            && message.endsWith(ENCRYPTION_SUFFIX)
            && message.length() > MIN_ENCRYPTED_MESSAGE_LENGTH
        ) {
            String encryptedMessage = message.substring(
                ENCRYPTION_PREFIX.length(),
                message.length() - ENCRYPTION_SUFFIX.length()
            );
            return decrypt(encryptedMessage, key.getBytes());
        }
        return message;
    }

    private String decrypt(String message, byte[] key) {
        try {
            byte[] bytes = Base64.getDecoder().decode(
                message.getBytes(StandardCharsets.UTF_8)
            );
            return new String(decrypt(bytes, key), StandardCharsets.UTF_8);
        } catch (Exception e) {
            Logger.print("decrypt message: " + message + " failed", e);
            return message;
        }
    }

    private byte[] decrypt(byte[] message, byte[] key) throws Exception {
        int initVectorSize = 16;
        byte[] iv = new byte[initVectorSize];
        System.arraycopy(message, 0, iv, 0, iv.length);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        int encryptedSize = message.length - iv.length;
        byte[] encryptedBytes = new byte[encryptedSize];
        System.arraycopy(message, initVectorSize, encryptedBytes, 0, encryptedSize);

        String keySpecAlgorithm = "AES";
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, keySpecAlgorithm);

        String transformation = "AES/CBC/PKCS5Padding";
        Cipher cipherDecrypt = Cipher.getInstance(transformation);
        cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        return cipherDecrypt.doFinal(encryptedBytes);
    }
}
