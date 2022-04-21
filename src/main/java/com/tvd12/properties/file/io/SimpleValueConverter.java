package com.tvd12.properties.file.io;

import static com.tvd12.properties.file.io.StringConveter.stringToChar;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveBoolArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveBoolArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveByteArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveByteArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveCharArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveCharArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveDoubleArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveDoubleArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveFloatArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveFloatArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveIntArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveIntArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveLongArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveLongArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveShortArray;
import static com.tvd12.properties.file.io.StringConveter.stringToPrimitiveShortArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToStringArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperBoolArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperBoolArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperByteArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperByteArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperCharArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperCharArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperDoubleArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperDoubleArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperFloatArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperFloatArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperIntArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperIntArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperLongArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperLongArrays;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperShortArray;
import static com.tvd12.properties.file.io.StringConveter.stringToWrapperShortArrays;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tvd12.properties.file.util.Logger;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleValueConverter implements ValueConverter {

    protected final Map<Class, Transformer> transformers;
    
    public SimpleValueConverter() {
        this.transformers = defaultTransformers();
    }
    
    @Override
    public <T> T convert(Object value, Class<T> outType) {
        if(value == null)
            return null;
        Transformer transformer = transformers.get(outType);
        if(transformer != null)
            return (T) transformer.transform(value);
        if(outType.isEnum())
            return (T) Enum.valueOf((Class<Enum>)outType, value.toString());
        return (T)value;
    }
    
    //tank
    private Map<Class, Transformer> 
        defaultTransformers() {
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
        answer.put(boolean.class, value -> {
            return Boolean.valueOf(value.toString());
        });
        answer.put(byte.class, value -> {
            return Byte.valueOf(value.toString());
        });
        answer.put(char.class, value -> {
            return stringToChar(value.toString());
        });
        answer.put(double.class, value -> {
            return Double.valueOf(value.toString());
        });
        answer.put(float.class, value -> {
            return Float.valueOf(value.toString());
        });
        answer.put(int.class, value -> {
            return Integer.valueOf(value.toString());
        });
        answer.put(long.class, value -> {
            return Long.valueOf(value.toString());
        });
        answer.put(short.class, value -> {
            return Short.valueOf(value.toString());
        });
    }

    protected void addWrapperTransformers(Map<Class, Transformer> answer) {
        answer.put(Boolean.class, value -> {
            return Boolean.valueOf(value.toString());
        });
        answer.put(Byte.class, value -> {
            return Byte.valueOf(value.toString());
        });
        answer.put(Character.class, value -> {
            return stringToChar(value.toString());
        });
        answer.put(Double.class, value -> {
            return Double.valueOf(value.toString());
        });
        answer.put(Float.class, value -> {
            return Float.valueOf(value.toString());
        });
        answer.put(Integer.class, value -> {
            return Integer.valueOf(value.toString());
        });
        answer.put(Long.class, value -> {
            return Long.valueOf(value.toString());
        });
        answer.put(Short.class, value -> {
            return Short.valueOf(value.toString());
        });
        
        answer.put(String.class, Object::toString);
    }

    protected void addPrimitiveArrayTransformers(Map<Class, Transformer> answer) {
        answer.put(boolean[].class, value -> {
            return stringToPrimitiveBoolArray((String)value);
        });
        answer.put(byte[].class, value -> {
            return stringToPrimitiveByteArray((String)value);
        });
        answer.put(char[].class, value -> {
            return stringToPrimitiveCharArray((String)value);
        });

        answer.put(double[].class, value -> {
            return stringToPrimitiveDoubleArray((String)value);
        });
        answer.put(float[].class, value -> {
            return stringToPrimitiveFloatArray((String)value);
        });
        answer.put(int[].class, value -> {
            return stringToPrimitiveIntArray((String)value);
        });
        answer.put(long[].class, value -> {
            return stringToPrimitiveLongArray((String)value);
        });
        answer.put(short[].class, value -> {
            return stringToPrimitiveShortArray((String)value);
        });
    }
    protected void addWrapperArrayTransformers(Map<Class, Transformer> answer) {
        answer.put(Boolean[].class, value -> {
            return stringToWrapperBoolArray((String)value);
        });
        answer.put(Byte[].class, value -> {
            return stringToWrapperByteArray((String)value);
        });
        answer.put(Character[].class, value -> {
            return stringToWrapperCharArray((String)value);
        });
        answer.put(Double[].class, value -> {
            return stringToWrapperDoubleArray((String)value);
        });
        answer.put(Float[].class, value -> {
            return stringToWrapperFloatArray((String)value);
        });
        answer.put(Integer[].class, value -> {
            return stringToWrapperIntArray((String)value);
        });
        answer.put(Long[].class, value -> {
            return stringToWrapperLongArray((String)value);
        });
        answer.put(Short[].class, value -> {
            return stringToWrapperShortArray((String)value);
        });
        
        answer.put(String[].class, value -> {
            return ((String)value).split(",");
        });
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
        answer.put(boolean[][].class, value -> {
            return stringToPrimitiveBoolArrays((String)value);
        });
        answer.put(byte[][].class, value -> {
            return stringToPrimitiveByteArrays((String)value);
        });
        answer.put(char[][].class, value -> {
            return stringToPrimitiveCharArrays((String)value);
        });
        answer.put(double[][].class, value -> {
            return stringToPrimitiveDoubleArrays((String)value);
        });
        answer.put(float[][].class, value -> {
            return stringToPrimitiveFloatArrays((String)value);
        });
        answer.put(int[][].class, value -> {
            return stringToPrimitiveIntArrays((String)value);
        });
        answer.put(long[][].class, value -> {
            return stringToPrimitiveLongArrays((String)value);
        });
        answer.put(short[][].class, value -> {
            return stringToPrimitiveShortArrays((String)value);
        });
    }

    protected void addTwoDimensionsWrapperArrayTransformers(Map<Class, Transformer> answer) {
        answer.put(Boolean[][].class, value -> {
            return stringToWrapperBoolArrays((String)value);
        });
        answer.put(Byte[][].class, value -> {
            return stringToWrapperByteArrays((String)value);
        });
        answer.put(Character[][].class, value -> {
            return stringToWrapperCharArrays((String)value);
        });
        answer.put(Double[][].class, value -> {
            return stringToWrapperDoubleArrays((String)value);
        });
        answer.put(Float[][].class, value -> {
            return stringToWrapperFloatArrays((String)value);
        });
        answer.put(Integer[][].class, value -> {
            return stringToWrapperIntArrays((String)value);
        });
        answer.put(Long[][].class, value -> {
            return stringToWrapperLongArrays((String)value);
        });
        answer.put(Short[][].class, value -> {
            return stringToWrapperShortArrays((String)value);
        });
        answer.put(String[][].class, value -> {
            return stringToStringArrays((String)value);
        });
    }
}
