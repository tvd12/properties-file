/**
 * 
 */
package com.monkey.properties.file.testing;

import java.io.InputStream;

import org.junit.Test;

import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.reader.InputStreamClassFetcher;

/**
 * @author tavandung12
 *
 */
public class InputStreamClassFetcherTest {

    @Test
    public void test() {
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("classes.properties");
        new InputStreamClassFetcher.Builder()
            .inputStream(inputStream)
            .inputStreams(inputStream)
            .inputStreams(Lists.newArrayList(inputStream, inputStream))
            .reader(new BaseFileReader())
            .build()
            .fetch();
    }
    
}
