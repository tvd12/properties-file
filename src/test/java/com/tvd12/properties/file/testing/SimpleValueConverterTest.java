package com.tvd12.properties.file.testing;

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import org.testng.annotations.Test;

import com.tvd12.properties.file.io.SimpleValueConverter;
import com.tvd12.properties.file.io.ValueConverter;

public class SimpleValueConverterTest {

    @Test
    public void test() {
        ValueConverter transformer = new SimpleValueConverter();
        assert transformer.convert(null, Object.class) == null;
        assert transformer.convert("abc", Date.class) == null;
        assert transformer.convert("2017-10-10T10:10:10:10", Date.class) != null;
        assert transformer.convert("abc", LocalDate.class) == null;
        assert transformer.convert("2017-10-10", LocalDate.class) != null;
        assert transformer.convert("abc", LocalDateTime.class) == null;
        assert transformer.convert("2017-10-10T10:10:10:100", LocalDateTime.class) != null;
        assert transformer.convert("", Class.class) == null;
        assert transformer.convert(getClass().getName(), Class.class) != null;

        assertEquals((Object) transformer.convert("true,false,true;true,false,true", boolean[][].class), new boolean[][]{{true, false, true}, {true, false, true}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", byte[][].class), new byte[][]{{1, 2, 3}, {1, 2, 3}});
        assertEquals((Object) transformer.convert("123;123", char[][].class), new char[][]{{'1', '2', '3'}, {'1', '2', '3'}});
        assertEquals((Object) transformer.convert("1.1,2.2,3.3;1.1,2.2,3.3", double[][].class), new double[][]{{1.1D, 2.2D, 3.3D}, {1.1D, 2.2D, 3.3D}});
        assertEquals((Object) transformer.convert("1.1,2.2,3.3;1.1,2.2,3.3", float[][].class), new float[][]{{1.1F, 2.2F, 3.3F}, {1.1F, 2.2F, 3.3F}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", int[][].class), new int[][]{{1, 2, 3}, {1, 2, 3}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", long[][].class), new long[][]{{1, 2, 3}, {1, 2, 3}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", short[][].class), new short[][]{{1, 2, 3}, {1, 2, 3}});

        assertEquals(transformer.convert("true,false,true", boolean[].class), new boolean[]{true, false, true});
        assertEquals(transformer.convert("1,2,3", byte[].class), new byte[]{1, 2, 3});
        assertEquals(transformer.convert("123", char[].class), new char[]{'1', '2', '3'});
        assertEquals(transformer.convert("1.1,2.2,3.3", double[].class), new double[]{1.1D, 2.2D, 3.3D});
        assertEquals(transformer.convert("1.1,2.2,3.3", float[].class), new float[]{1.1F, 2.2F, 3.3F});
        assertEquals(transformer.convert("1,2,3", int[].class), new int[]{1, 2, 3});
        assertEquals(transformer.convert("1,2,3", long[].class), new long[]{1, 2, 3});
        assertEquals(transformer.convert("1,2,3", short[].class), new short[]{1, 2, 3});

        assert transformer.convert("true", boolean.class);
        assert !transformer.convert("false", boolean.class);
        assert transformer.convert("TRUE", boolean.class);
        assert !transformer.convert("FALSE", boolean.class);
        assert transformer.convert("1", byte.class) == (byte) 1;
        assert transformer.convert("a", char.class) == 'a';
        assert transformer.convert("1.0", double.class) == 1.0D;
        assert transformer.convert("1.0", float.class) == 1.0F;
        assert transformer.convert("1", int.class) == 1;
        assert transformer.convert("1", long.class) == 1L;
        assert transformer.convert("1", short.class) == (short) 1;

        assertEquals((Object) transformer.convert("true,false,true;true,false,true", Boolean[][].class), new Boolean[][]{{true, false, true}, {true, false, true}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", Byte[][].class), new Byte[][]{{1, 2, 3}, {1, 2, 3}});
        assertEquals((Object) transformer.convert("123;123", Character[][].class), new Character[][]{{'1', '2', '3'}, {'1', '2', '3'}});
        assertEquals((Object) transformer.convert("1.1,2.2,3.3;1.1,2.2,3.3", Double[][].class), new Double[][]{{1.1D, 2.2D, 3.3D}, {1.1D, 2.2D, 3.3D}});
        assertEquals((Object) transformer.convert("1.1,2.2,3.3;1.1,2.2,3.3", Float[][].class), new Float[][]{{1.1F, 2.2F, 3.3F}, {1.1F, 2.2F, 3.3F}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", Integer[][].class), new Integer[][]{{1, 2, 3}, {1, 2, 3}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", Long[][].class), new Long[][]{{1L, 2L, 3L}, {1L, 2L, 3L}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", Short[][].class), new Short[][]{{1, 2, 3}, {1, 2, 3}});
        assertEquals((Object) transformer.convert("1,2,3;1,2,3", String[][].class), new String[][]{{"1", "2", "3"}, {"1", "2", "3"}});

        assertEquals(transformer.convert("true,false,true", Boolean[].class), new Boolean[]{true, false, true});
        assertEquals(transformer.convert("1,2,3", Byte[].class), new Byte[]{1, 2, 3});
        assertEquals(transformer.convert("123", Character[].class), new Character[]{'1', '2', '3'});
        assertEquals(transformer.convert("1.1,2.2,3.3", Double[].class), new Double[]{1.1D, 2.2D, 3.3D});
        assertEquals(transformer.convert("1.1,2.2,3.3", Float[].class), new Float[]{1.1F, 2.2F, 3.3F});
        assertEquals(transformer.convert("1,2,3", Integer[].class), new Integer[]{1, 2, 3});
        assertEquals(transformer.convert("1,2,3", Long[].class), new Long[]{1L, 2L, 3L});
        assertEquals(transformer.convert("1,2,3", Short[].class), new Short[]{1, 2, 3});
        assertEquals(transformer.convert("1,2,3", String[].class), new String[]{"1", "2", "3"});

        assert transformer.convert("true", Boolean.class);
        assert !transformer.convert("false", Boolean.class);
        assert transformer.convert("TRUE", Boolean.class);
        assert !transformer.convert("FALSE", Boolean.class);
        assert transformer.convert(Boolean.TRUE, Boolean.class);

        assert transformer.convert("1", Byte.class) == (byte) 1;
        assert transformer.convert("a", Character.class) == 'a';
        assert transformer.convert("1.0", Double.class) == 1.0D;
        assert transformer.convert("1.0", Float.class) == 1.0F;
        assert transformer.convert("1", Integer.class) == 1;
        assert transformer.convert("1", Long.class) == 1L;
        assert transformer.convert("1", Short.class) == (short) 1;
        assert transformer.convert("HELLO", ExEnum.class) == ExEnum.HELLO;

        try {
            transformer.convert("no thing", ExEnum.class);
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    public enum ExEnum {
        HELLO,
        WORLD
    }
}
