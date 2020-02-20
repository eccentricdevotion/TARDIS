package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;

import java.util.HashMap;
import java.util.regex.Pattern;

public class TARDISHandlesPattern {

    private static final HashMap<String, Pattern> patterns = new HashMap<>();

    public static Pattern getPattern(String key) {
        if (patterns.containsKey(key)) {
            return patterns.get(key);
        } else {
            // cache key
            return buildPattern(key);
        }
    }

    public static Pattern getPattern(String key, boolean custom) {
        if (patterns.containsKey(key)) {
            return patterns.get(key);
        } else {
            // cache key
            if (custom) {
                key = "custom-commands." + key + ".regex";
            } else {
                key = "core-commands." + key;
            }
            return buildPattern(key);
        }
    }

    private static Pattern buildPattern(String key) {
        Pattern pattern = null;
        String regex = TARDIS.plugin.getHandlesConfig().getString(key);
        if (regex != null) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            patterns.put(key, pattern);
        }
        return pattern;
    }
}
