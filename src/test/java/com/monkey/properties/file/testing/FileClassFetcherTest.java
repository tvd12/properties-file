/**
 * 
 */
package com.monkey.properties.file.testing;

import java.io.File;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.tvd12.properties.file.reader.BaseFileReader;
import com.tvd12.properties.file.reader.FileClassFetcher;

/**
 * @author tavandung12
 *
 */
public class FileClassFetcherTest {

    @Test
    public void test() {
        File file = new File(getClass().getClassLoader()
                .getResource("classes.properties").getFile());
        new FileClassFetcher.Builder()
            .file(file)
            .files(file)
            .files(Lists.newArrayList(file, file))
            .reader(new BaseFileReader())
            .build()
            .fetch();
    }
    
}
