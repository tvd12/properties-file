package com.monkey.properties.file.testing;

import org.testng.annotations.Test;

import com.tvd12.properties.file.util.Logger;

public class LoggerTest {

    @Test
    public void test() {
        Logger.print("Hello World", new IllegalArgumentException("just test"));
        Logger.setPrinter((message, e) -> System.out.println("test:: " + message + " -> " + e));
    }
}
