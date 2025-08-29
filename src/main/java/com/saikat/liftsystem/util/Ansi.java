package com.saikat.liftsystem.util;

public final class Ansi {
    private Ansi() {}
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";

    public static String up(String s){ return GREEN + s + RESET; }
    public static String down(String s){ return RED + s + RESET; }
    public static String idle(String s){ return YELLOW + s + RESET; }
    public static String info(String s){ return CYAN + s + RESET; }
}
