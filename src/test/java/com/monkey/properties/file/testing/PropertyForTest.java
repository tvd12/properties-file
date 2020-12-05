package com.monkey.properties.file.testing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates file or method is mapping to a property in properties file 
 * or properties object, or map. Can be used in conjunction with PropertyWrapper classes    
 * 
 * @author tavandung12
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface PropertyForTest {

    /**
     * Map to name of property
     * 
     * @return string value
     */
    public String value() default "";
    
    /**
     * Map to group of property
     * 
     * @return string the prefix of group
     */
    public String prefix() default "";
    
}
