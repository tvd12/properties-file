package com.tvd12.properties.file.testing;

import static com.tvd12.properties.file.constant.Constants.PROPERTIES_KEY_DECRYPTION_KEY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.tvd12.test.util.RandomUtil;
import org.testng.annotations.Test;
import org.testng.collections.Sets;

import com.tvd12.properties.file.util.PropertiesUtil;
import com.tvd12.test.assertion.Asserts;
import com.tvd12.test.base.BaseTest;

public class PropertiesUtilTest extends BaseTest {

    @Override
    public Class<?> getTestClass() {
        return PropertiesUtil.class;
    }

    @Test
    public void getPropertiesByPrefixTest() {
        Properties properties = new Properties();
        properties.put("datasource", "database");
        properties.put("datasource.username", "hello");
        Properties expected = new Properties();
        expected.put("", "database");
        expected.put("username", "hello");
        assertEquals(PropertiesUtil.getPropertiesByPrefix(properties, "datasource"), expected);
    }

    @Test
    public void getPropertiesByEmptyPrefixTest() {
        Properties properties = new Properties();
        properties.put("datasource", "database");
        properties.put("datasource.username", "hello");
        assertEquals(PropertiesUtil.getPropertiesByPrefix(properties, ""), properties);
    }

    @Test
    public void getFirstPropertyKeysWithFirstDotIndex0() {
        // given
        Properties properties = new Properties();
        properties.put("", "foo");
        properties.put(".", "database");
        properties.put("hello", "world");
        properties.put("hello.1", "world");
        properties.put("hello1.2", "world");

        // when
        Set<String> actual = PropertiesUtil.getFirstPropertyKeys(properties);

        // then
        Set<String> expected = new HashSet<>(Arrays.asList("", ".", "hello", "hello1"));
        assertEquals(actual, expected);
    }

    @Test
    public void getFirstPropertyKeyListWithFirstDotIndex0() {
        // given
        Properties properties = new Properties();
        properties.put("", "foo");
        properties.put(".", "database");
        properties.put("hello", "world");
        properties.put("hello.1", "world");
        properties.put("hello1.2", "world");

        // when
        List<String> actual = PropertiesUtil.getFirstPropertyKeyList(properties);

        // then
        assertEquals(actual.size(), 4);
        assertTrue(actual.containsAll(Arrays.asList("", ".", "hello", "hello1")));
    }

    @Test
    public void filterPropertiesByPrefixTest() {
        Properties properties = new Properties();
        properties.put("datasource", "database");
        properties.put("datasource.username", "hello");
        properties.put("cache.username", "hello");
        properties.put("cache.pass", "pass");
        Properties expected = new Properties();
        expected.put("datasource", "database");
        expected.put("datasource.username", "hello");
        assertEquals(PropertiesUtil.filterPropertiesByKeyPrefix(properties, "datasource"), expected);
    }

    @Test
    public void setVariableTest() {
        // given
        Properties properties = new Properties();
        properties.put("datasource", "${hell.world}");
        properties.put("hell.world", "hello");

        // when
        PropertiesUtil.setVariableValues(properties);

        // then
        Properties expectation = new Properties();
        expectation.put("datasource", "hello");
        expectation.put("hell.world", "hello");
        Asserts.assertEquals(properties, expectation);
    }

    @Test
    public void setVariableInValidTest() {
        // given
        Properties properties = new Properties();
        properties.put("datasource", "database: ${username}${password}${}} url ${not found}}");
        properties.put("username", "foo");
        properties.put("password", "bar");

        // when
        PropertiesUtil.setVariableValues(properties);

        // then
        Properties expectation = new Properties();
        expectation.put("datasource", "database: foobar${}} url ${not found}}");
        expectation.put("username", "foo");
        expectation.put("password", "bar");
        Asserts.assertEquals(properties, expectation);
    }

    @Test
    public void getKeysFromVariableNameTest() {
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName(""));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("${}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("${"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("$ {}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("{}"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("${abc"));
        Asserts.assertEmpty(PropertiesUtil.getKeysFromVariableName("abc}"));
        Asserts.assertEquals(
            PropertiesUtil.getKeysFromVariableName("${a}, ${b}, ${c}"),
            Sets.newHashSet("a", "b", "c")
        );
    }

    @Test
    public void getValueFromSystemProperty() {
        // given
        String key = RandomUtil.randomShortAlphabetString();
        String value = RandomUtil.randomShortAlphabetString();
        System.setProperty(key, value);

        Properties properties = new Properties();

        // when
        Object actual = PropertiesUtil.getValue(properties, key);

        // then
        Asserts.assertEquals(actual, value);
    }

    @Test
    public void getValueFromSystemEnv() {
        // given
        Properties properties = new Properties();

        // when
        Object actual = PropertiesUtil.getValue(properties, "PATH");

        // then
        System.out.println(actual);
    }

    @Test
    public void getSystemVariableValueFromSystemProperty() {
        // given
        String key = RandomUtil.randomShortAlphabetString();
        String value = RandomUtil.randomShortAlphabetString();
        System.setProperty(key, value);

        // when
        Object actual = PropertiesUtil.getSystemVariableValue(key);

        // then
        Asserts.assertEquals(actual, value);
    }

    @Test
    public void getSystemVariableValueSystemEnv() {
        // given
        // when
        Object actual = PropertiesUtil.getSystemVariableValue("PATH");

        // then
        System.out.println(actual);
    }

    @Test
    public void decryptedAndSetVariableValues() {
        // given
        Properties properties = new Properties();

        String key1 = RandomUtil.randomShortAlphabetString();
        int value1 = RandomUtil.randomInt();
        properties.put(key1, value1);

        String key2 = RandomUtil.randomShortAlphabetString();
        String value2 = RandomUtil.randomShortAlphabetString();

        String encryptionKey = AesEncrypter.getInstance()
            .randomKey();
        System.setProperty(PROPERTIES_KEY_DECRYPTION_KEY, encryptionKey);

        String encryptedValue2 = AesEncrypter.getInstance()
            .encryptAndWrap(value2, encryptionKey);
        properties.put(key2, encryptedValue2);

        // when
        PropertiesUtil.setVariableValues(properties);

        // then
        Properties expected = new Properties();
        expected.put(key1, value1);
        expected.put(key2, value2);

        Asserts.assertEquals(properties, expected);
    }
}
