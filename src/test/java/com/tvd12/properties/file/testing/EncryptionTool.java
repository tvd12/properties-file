package com.tvd12.properties.file.testing;

import com.tvd12.properties.file.encryption.AesDecrypter;

public class EncryptionTool {

    public static void main(String[] args) {
        String key = generateKey();
        System.out.println("key: " + key);

        String message = "<your message>";
        System.out.println("clear message: " + message);

        String encryptedMessage = encrypt(message, key);
        System.out.println("encrypted message: " + encryptedMessage);

        String decryptedMessage = decrypt(encryptedMessage, key);
        System.out.println("decrypted message: " + decryptedMessage);
    }

    private static String generateKey() {
        return AesEncrypter.getInstance().randomKey();
    }

    private static String encrypt(String message, String key) {
        return AesEncrypter.getInstance().encryptAndWrap(message, key);
    }

    private static String decrypt(String encryptedMessage, String key) {
        return AesDecrypter.getInstance().decryptOrIgnore(
            encryptedMessage,
            key
        );
    }
}
