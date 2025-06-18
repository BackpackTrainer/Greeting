package com.example.demo.bdd.util;

public class EmojiStripper {

    public static String stripEmojiPrefix(String text) {
        // This assumes your emoji prefix is always followed by a space

        if (text == null || text.isBlank()) return "";
        int firstSpace = text.indexOf(" ");
        if (firstSpace >= 0) {
            return text.substring(firstSpace + 1).trim();
        } else {
            return text.trim();
        }
    }
}
