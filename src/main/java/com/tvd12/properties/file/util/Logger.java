package com.tvd12.properties.file.util;

public final class Logger {

    private static Printer printer = (message, e) -> System.out.println(message + "\n" + e);

    private Logger() {}

    public static void print(String message, Throwable e) {
        printer.print(message, e);
    }

    public static void setPrinter(Printer printer) {
        Logger.printer = printer;
    }

    public interface Printer {
        void print(String message, Throwable e);
    }
}
