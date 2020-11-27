package com.tvd12.properties.file.struct;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.tvd12.properties.file.annotation.Property;
import com.tvd12.properties.file.bean.Transformer;
import com.tvd12.properties.file.constant.Constants;
import com.tvd12.properties.file.mapping.MappingLevel;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reflect.ReflectionClassUtils;
import com.tvd12.properties.file.util.Logger;
import com.tvd12.properties.file.util.PropertyAnnotations;

/**
 * 
 * Holds structure of java bean class, map object to properties object
 * and also  
 * 
 * @author tavandung12
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class PropertiesBean {

	private Object bean;
    private ClassWrapper wrapper;
    private ClassLoader classLoader;
    
	private static final Map<Class, Transformer> TRANSFORMERS =
            Collections.unmodifiableMap(createTypeTransformers());
	
    public PropertiesBean(Class<?> clazz) {
        this(ReflectionClassUtils.newInstance(clazz));
    }
    
    public PropertiesBean(Object bean) {
        this(bean, MappingLevel.ALL, null);
    }
    
    public PropertiesBean(
    		Object bean, 
    		MappingLevel mappingLevel, ClassLoader classLoader) {
    	this.bean = bean;
    	this.classLoader = classLoader;
    	this.wrapper = new ClassWrapper(bean.getClass(), mappingLevel);
    }
    
    public <T> T getObject() {
        return (T)bean;
    }

    protected MethodStruct getWriteMethodStruct(Object key) {
    	return wrapper.getMethodStruct(key.toString());
    }

    public void put(Object key, Object value) {
    	put(key, value, null);
    }
    
    public void put(Object key, Object value, Properties properties) {
    	MethodStruct methodStruct = getWriteMethodStruct(key);
        if(methodStruct == null)
            return;
        if(value instanceof String)
            value = ((String) value).trim();
        try {
    		Object argument = transform(methodStruct, value, properties);
    		if(methodStruct.getMethod() != null) {
    			methodStruct.getMethod().invoke(bean, argument);
    		}
    		else {
    			Field field = methodStruct.getField();
    			if(!Modifier.isPublic(field.getModifiers()))
    				field.setAccessible(true);
    			field.set(bean, argument);
    		}
        }
        catch (Exception e) {
    		printError("put value: " + value + " with key: " + key + " error", e);
			return;
		}
    }
    
    public void putAll(Properties properties) {
        for(Object key : properties.keySet()) {
    		Object value = properties.get(key);
    		put(key, value, properties);
        }
    }
    
    protected Class getWriteArgumentType(MethodStruct methodStruct) {
    	if(methodStruct.getField() != null)
    		return methodStruct.getField().getType();
		return methodStruct.getMethod().getParameterTypes()[0];
    }
    
    protected Object transform(
    		MethodStruct methodStruct, Object value, Properties properties) {
    	String prefix = "";
    	Property propertyAnno = methodStruct.getAnnotation(Property.class);
    	if(propertyAnno != null)
    		prefix = PropertyAnnotations.getPrefix(propertyAnno);
        Class argumentType = getWriteArgumentType(methodStruct);
        if(properties == null || prefix.isEmpty())
    		return transform(argumentType, value);
        return new PropertiesMapper()
        		.data(properties)
        		.classLoader(classLoader)
        		.propertyPrefix(prefix)
        		.map(argumentType);
    }
    
	protected Object transform(Class newType, Object value) {
		Transformer transformer = getTypeTransformer(newType);
		if (transformer == null)
			return value;
		Object answer = transformer.transform(value);
		return answer;
	}
    
    
    protected Transformer getTypeTransformer(Class aType) {
        Transformer transformer = TRANSFORMERS.get(aType);
        if(transformer == null)
            return null;
        return transformer;
    }
    
    protected void printError(String message, Throwable throwable) {
    	Logger.print(message, throwable);
    }
    
    private static Map<Class, Transformer> createTypeTransformers() {
        Map<Class, Transformer> transformers = new HashMap<>();
        transformers.put(Boolean.TYPE, new Transformer() {
			public Object transform(Object input) {
				return Boolean.valueOf(input.toString());
			}
		});
		transformers.put(Character.TYPE, new Transformer() {
			public Object transform(Object input) {
				return new Character(input.toString().charAt(0));
			}
		});
		transformers.put(Byte.TYPE, new Transformer() {
			public Object transform(Object input) {
				return Byte.valueOf(input.toString());
			}
		});
		transformers.put(Double.TYPE, new Transformer() {
			public Object transform(Object input) {
				return Double.valueOf(input.toString());
			}
		});
		transformers.put(Float.TYPE, new Transformer() {
			public Object transform(Object input) {
				return Float.valueOf(input.toString());
			}
		});
		transformers.put(Integer.TYPE, new Transformer() {
			public Object transform(Object input) {
				return Integer.valueOf(input.toString());
			}
		});
		transformers.put(Long.TYPE, new Transformer() {
			public Object transform(Object input) {
				return Long.valueOf(input.toString());
			}
		});
		transformers.put(Short.TYPE, new Transformer() {
			public Object transform(Object input) {
				return Short.valueOf(input.toString());
			}
		});
		transformers.put(String.class, new Transformer() {
			public Object transform(Object input) {
				return input.toString();
			}
		});
		transformers.put(Date.class, new Transformer() {
            @Override
            public Object transform(Object value) {
        		String str = value.toString();
        		for(String pattern : Constants.DATE_FORMATS) {
        			SimpleDateFormat format = new SimpleDateFormat(pattern);
        			try {
        				return format.parse(str);
        			}
        			catch(Exception e) {
        				//ignore
        			}
        		}
        		throw new IllegalArgumentException("has no pattern to format date string: " + str);
            }
        });
        
        transformers.put(Class.class, new Transformer() {
            @Override
            public Object transform(Object value) {
                try {
                    String string = value.toString();
                    if(string.startsWith("class ")) 
                        string = string.substring("class ".length()).trim();
                    return Class.forName(string);
                } catch (ClassNotFoundException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
        
		return transformers;
    }
    
}
