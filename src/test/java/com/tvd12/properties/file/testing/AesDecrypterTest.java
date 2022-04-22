package com.tvd12.properties.file.testing;

import com.tvd12.properties.file.encryption.AesDecrypter;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import static com.tvd12.properties.file.constant.Constants.ENCRYPTION_PREFIX;
import static com.tvd12.properties.file.constant.Constants.ENCRYPTION_SUFFIX;

public class AesDecrypterTest {

    @Test
    public void decryptOkTest() {
        // given
        String key = "KSYzjcc8nqrBk8jXtW4QaMpr2suBU9vY";
        String encryptedText = "ENC(mWUgQdrI9HiTI3oGJdmfYCBkV7b58ACDe2yy9AC4gAM=)";

        // when
        String decryptedText = AesDecrypter.getInstance()
            .decryptOrIgnore(encryptedText, key);

        // then
        Asserts.assertEquals(decryptedText, "hello");
    }

    @Test
    public void decryptOkWithToolTest() {
        // given
        String key = AesEncrypter.getInstance().randomKey();
        String clearText = "hello";
        String encryptedText = AesEncrypter.getInstance()
            .encryptAndWrap(clearText, key);

        // when
        String decryptedText = AesDecrypter.getInstance()
            .decryptOrIgnore(encryptedText, key);

        // then
        Asserts.assertEquals(decryptedText, clearText);
    }

    @Test
    public void decryptNull() {
        // given
        String key = AesEncrypter.getInstance().randomKey();

        // when
        String decrypted = AesDecrypter.getInstance().decryptOrIgnore(
            null,
            key
        );

        // then
        Asserts.assertNull(decrypted);
    }

    @Test
    public void decryptNonEncrypted() {
        // given
        String key = AesEncrypter.getInstance().randomKey();

        // when
        // then
        Asserts.assertEquals(
            AesDecrypter.getInstance().decryptOrIgnore(
                ENCRYPTION_PREFIX,
                key
            ),
            ENCRYPTION_PREFIX
        );
        Asserts.assertEquals(
            AesDecrypter.getInstance().decryptOrIgnore(
                ENCRYPTION_PREFIX + "a",
                key
            ),
            ENCRYPTION_PREFIX + "a"
        );
        Asserts.assertEquals(
            AesDecrypter.getInstance().decryptOrIgnore(
                ENCRYPTION_PREFIX + ENCRYPTION_SUFFIX,
                key
            ),
            ENCRYPTION_PREFIX + ENCRYPTION_SUFFIX
        );
        Asserts.assertEquals(
            AesDecrypter.getInstance().decryptOrIgnore(
                "message",
                key
            ),
            "message"
        );
    }

    @Test
    public void decryptInvalidKey() {
        // given
        String key = "123";
        String encryptedText = "ENC(mWUgQdrI9HiTI3oGJdmfYCBkV7b58ACDe2yy9AC4gAM=)";

        // when
        String decrypted = AesDecrypter.getInstance().decryptOrIgnore(
            encryptedText,
            key
        );

        // then
        Asserts.assertEquals(decrypted, encryptedText);
    }
}
