package com.tvd12.properties.file.struct;

import java.lang.reflect.Method;

/**
 * Support for filter method.
 *
 * @author tavandung12
 */
public interface MethodFilter {

    /**
     * filter method.
     *
     * @param method method to validate
     * @return true or false
     */
    boolean filter(Method method);
}
