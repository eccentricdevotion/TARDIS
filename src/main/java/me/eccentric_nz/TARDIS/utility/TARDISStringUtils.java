package me.eccentric_nz.TARDIS.utility;

import java.util.Arrays;
import java.util.List;

public class TARDISStringUtils {

    private static final List<String> numbers = Arrays.asList("ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE");

    public static String capitalise(String s) {
        String[] split = s.split("_");
        StringBuilder builder = new StringBuilder();
        for (String str : split) {
            builder.append(uppercaseFirst(str)).append(" ");
        }
        return builder.toString().trim();
    }

    public static String sentenceCase(String s) {
        String replaced = s.replace("_", " ");
        return uppercaseFirst(replaced);
    }

    public static String uppercaseFirst(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String fromInt(int i) {
        if (i > 9) {
            return "A big number";
        }
        return numbers.get(i);
    }

    public static String toNumber(String s) {
        return String.format("%s", numbers.indexOf(s));
        // "-1" if not found
    }
}
