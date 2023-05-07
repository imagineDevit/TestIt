package io.github.imaginedevit.testIt;

public class TextUtils {

    enum U {
        ITALIC("\033[3m", "\033[0m"),
        BOLD("\u001B[1m", "\u001B[0m"),

        GREEN("\u001B[32m", "\u001B[0m"),
        BLUE("\u001B[34m", "\u001B[0m"),

        PURPLE("\u001B[35m", "\u001B[0m"),
        YELLOW("\u001B[33m", "\u001B[0m"),

        RED("\u001B[31m", "\u001B[0m"),

        BACKGROUND_CYAN("\u001B[46m", "\u001B[0m");

        String on;
        String off;

        U(String on, String off) {
            this.on = on;
            this.off = off;
        }

        public String transform(String text){
            return on + text + off;
        }
    }

    public static String bold(String text) {
        return U.BOLD.transform(text);
    }

    public static String italic(String text) {
        return U.ITALIC.transform(text);
    }

    public static String green(String text) {
        return U.GREEN.transform(text);
    }

    public static String blue(String text) {
        return U.BLUE.transform(text);
    }

    public static String yellow(String text) {
        return U.YELLOW.transform(text);
    }

    public static String purple(String text) {
        return U.PURPLE.transform(text);
    }

    public static String red(String text) {
        return U.RED.transform(text);
    }

    public static String bg(String text) {
        return U.BACKGROUND_CYAN.transform(text);
    }



}
