package com.tvd12.properties.file.testing;

import com.tvd12.properties.file.encryption.AesDecrypter;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

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
}
