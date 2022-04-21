package com.tvd12.properties.file.testing;

import java.io.Closeable;
import java.io.IOException;

public class AutoCloseTest {

    private static class InternalClose implements Closeable {
        @Override
        public void close() throws IOException {
            System.out.println("I'm closed");
        }
    }

    public static void main(String[] args) {
        try (InternalClose close = new InternalClose()) {
            System.out.println(close);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
