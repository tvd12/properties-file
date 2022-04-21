package com.tvd12.properties.file.io;

import static com.tvd12.properties.file.io.StringConverter.stringToChar;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveBoolArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveBoolArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveByteArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveByteArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveCharArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveCharArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveDoubleArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveDoubleArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveFloatArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveFloatArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveIntArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveIntArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveLongArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveLongArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveShortArray;
import static com.tvd12.properties.file.io.StringConverter.stringToPrimitiveShortArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToStringArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperBoolArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperBoolArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperByteArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperByteArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperCharArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperCharArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperDoubleArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperDoubleArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperFloatArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperFloatArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperIntArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperIntArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperLongArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperLongArrays;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperShortArray;
import static com.tvd12.properties.file.io.StringConverter.stringToWrapperShortArrays;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.properties.file.util.Logger;

@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleValueConverter implements ValueConverter {

    protected final Map<Class, Transformer> transformers;

    public SimpleValueConverter() {
        this.transformers = defaultTransformers();
    }

    @Override
    public <T> T convert(Object value, Class<T> outType) {
        if (value == null) {
            return null;
        }
        Transformer transformer = transformers.get(outType);
        if (transformer != null) {
            return (T) transformer.transform(value);
        }
        if (outType.isEnum()) {
            return (T) Enum.valueOf((Class<Enum>) outType, value.toString());
        }
        return (T) value;
    }

    //tank
    private Map<Class, Transformer> defaultTransformers() {
        Map<Class, Transformer> answer = new HashMap<>();
        addOtherTransformers(answer);
        addWrapperTransformers(answer);
        addPrimitiveTransformers(answer);
        addWrapperArrayTransformers(answer);
        addPrimitiveArrayTransformers(answer);
        addTwoDimensionsWrapperArrayTransformers(answer);
        addTwoDimensionsPrimitiveArrayTransformers(answer);
        return answer;
    }

    protected void addPrimitiveTransformers(Map<Class, Transformer> answer) {
        answer.put(boolean.class, value -> Boolean.valueOf(value.toString()));
        answer.put(byte.class, value -> Byte.valueOf(value.toString()));
        answer.put(char.class, value -> stringToChar(value.toString()));
        answer.put(double.class, value -> Double.valueOf(value.toString()));
        answer.put(float.class, value -> Float.valueOf(value.toString()));
        answer.put(int.class, value -> Integer.valueOf(value.toString()));
        answer.put(long.class, value -> Long.valueOf(value.toString()));
        answer.put(short.class, value -> Short.valueOf(value.toString()));
    }

    protected void addWrapperTransformers(Map<Class, Transformer> answer) {
        answer.put(Boolean.class, value -> Boolean.valueOf(value.toString()));
        answer.put(Byte.class, value -> Byte.valueOf(value.toString()));
        answer.put(Character.class, value -> stringToChar(value.toString()));
        answer.put(Double.class, value -> Double.valueOf(value.toString()));
        answer.put(Float.class, value -> Float.valueOf(value.toString()));
        answer.put(Integer.class, value -> Integer.valueOf(value.toString()));
        answer.put(Long.class, value -> Long.valueOf(value.toString()));
        answer.put(Short.class, value -> Short.valueOf(value.toString()));

        answer.put(String.class, Object::toString);
    }

    protected void addPrimitiveArrayTransformers(Map<Class, Transformer> answer) {
        answer.put(boolean[].class, value -> stringToPrimitiveBoolArray((String) value));
        answer.put(byte[].class, value -> stringToPrimitiveByteArray((String) value));
        answer.put(char[].class, value -> stringToPrimitiveCharArray((String) value));

        answer.put(double[].class, value -> stringToPrimitiveDoubleArray((String) value));
        answer.put(float[].class, value -> stringToPrimitiveFloatArray((String) value));
        answer.put(int[].class, value -> stringToPrimitiveIntArray((String) value));
        answer.put(long[].class, value -> stringToPrimitiveLongArray((String) value));
        answer.put(short[].class, value -> stringToPrimitiveShortArray((String) value));
    }

    protected void addWrapperArrayTransformers(Map<Class, Transformer> answer) {
        answer.put(Boolean[].class, value -> stringToWrapperBoolArray((String) value));
        answer.put(Byte[].class, value -> stringToWrapperByteArray((String) value));
        answer.put(Character[].class, value -> stringToWrapperCharArray((String) value));
        answer.put(Double[].class, value -> stringToWrapperDoubleArray((String) value));
        answer.put(Float[].class, value -> stringToWrapperFloatArray((String) value));
        answer.put(Integer[].class, value -> stringToWrapperIntArray((String) value));
        answer.put(Long[].class, value -> stringToWrapperLongArray((String) value));
        answer.put(Short[].class, value -> stringToWrapperShortArray((String) value));

        answer.put(String[].class, value -> ((String) value).split(","));
    }

    protected void addOtherTransformers(Map<Class, Transformer> answer) {
        answer.put(Date.class, value -> {
            try {
                return Dates.parse(value.toString());
            } catch (Exception e) {
                Logger.print("value: " + value + " is invalid", e);
            }
            return null;
        });

        answer.put(LocalDate.class, value -> {
            try {
                return Dates.parseDate(value.toString(), "yyyy-MM-dd");
            } catch (Exception e) {
                Logger.print("value: " + value + " is invalid", e);
            }
            return null;
        });

        answer.put(LocalDateTime.class, value -> {
            try {
                return Dates.parseDateTime(value.toString());
            } catch (Exception e) {
                Logger.print("value: " + value + " is invalid", e);
            }
            return null;
        });

        //other
        answer.put(Class.class, value -> {
            try {
                return Class.forName(value.toString());
            } catch (Exception e) {
                Logger.print("value: " + value + " is invalid", e);
            }
            return null;
        });
    }

    protected void addTwoDimensionsPrimitiveArrayTransformers(Map<Class, Transformer> answer) {
        answer.put(boolean[][].class, value -> stringToPrimitiveBoolArrays((String) value));
        answer.put(byte[][].class, value -> stringToPrimitiveByteArrays((String) value));
        answer.put(char[][].class, value -> stringToPrimitiveCharArrays((String) value));
        answer.put(double[][].class, value -> stringToPrimitiveDoubleArrays((String) value));
        answer.put(float[][].class, value -> stringToPrimitiveFloatArrays((String) value));
        answer.put(int[][].class, value -> stringToPrimitiveIntArrays((String) value));
        answer.put(long[][].class, value -> stringToPrimitiveLongArrays((String) value));
        answer.put(short[][].class, value -> stringToPrimitiveShortArrays((String) value));
    }

    protected void addTwoDimensionsWrapperArrayTransformers(Map<Class, Transformer> answer) {
        answer.put(Boolean[][].class, value -> stringToWrapperBoolArrays((String) value));
        answer.put(Byte[][].class, value -> stringToWrapperByteArrays((String) value));
        answer.put(Character[][].class, value -> stringToWrapperCharArrays((String) value));
        answer.put(Double[][].class, value -> stringToWrapperDoubleArrays((String) value));
        answer.put(Float[][].class, value -> stringToWrapperFloatArrays((String) value));
        answer.put(Integer[][].class, value -> stringToWrapperIntArrays((String) value));
        answer.put(Long[][].class, value -> stringToWrapperLongArrays((String) value));
        answer.put(Short[][].class, value -> stringToWrapperShortArrays((String) value));
        answer.put(String[][].class, value -> stringToStringArrays((String) value));
    }
}
