package com.tvd12.properties.file.testing;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

import static com.tvd12.properties.file.constant.Constants.ENCRYPTION_PREFIX;
import static com.tvd12.properties.file.constant.Constants.ENCRYPTION_SUFFIX;

public class AesEncrypter {

	public static final int DEFAULT_KEY_SIZE = 32;
	private static final AesEncrypter INSTANCE = new AesEncrypter();
	private static final String KEY_CHARS =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	private AesEncrypter() {}
	
	public static AesEncrypter getInstance() {
		return INSTANCE;
	}
	
	public String randomKey() {
		byte[] key = new byte[DEFAULT_KEY_SIZE];
		for (int i = 0; i < key.length; ++i) {
			key[i] = (byte)KEY_CHARS.charAt(
				ThreadLocalRandom.current().nextInt(KEY_CHARS.length())
			);
		}
		return new String(key);
	}

	public String encryptAndWrap(String message, String key) {
		return ENCRYPTION_PREFIX +
			encrypt(message, key) +
			ENCRYPTION_SUFFIX;
	}

	public String encrypt(String message, String key) {
		try {
			return Base64.getEncoder().encodeToString(
				encrypt(
					message.getBytes(StandardCharsets.UTF_8),
					key.getBytes()
				)
			);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	private byte[] encrypt(byte[] message, byte[] key) throws Exception {
		int initVectorSize = 16;
		byte[] iv = new byte[initVectorSize];
        ThreadLocalRandom.current().nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

		String keySpecAlgorithm = "AES";
		SecretKeySpec secretKeySpec = new SecretKeySpec(key, keySpecAlgorithm);

		String transformation = "AES/CBC/PKCS5Padding";
		Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(message);

        byte[] encryptedIvAndMessage = new byte[initVectorSize + encrypted.length];
        System.arraycopy(iv, 0, encryptedIvAndMessage, 0, initVectorSize);
        System.arraycopy(encrypted, 0, encryptedIvAndMessage, initVectorSize, encrypted.length);

        return encryptedIvAndMessage;
    }
}
