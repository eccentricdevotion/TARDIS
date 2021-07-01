/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.handles;

import me.eccentric_nz.tardis.TardisPlugin;

import java.util.HashMap;
import java.util.regex.Pattern;

public class TardisHandlesPattern {

    private static final HashMap<String, Pattern> PATTERNS = new HashMap<>();

    public static Pattern getPattern(String key) {
        if (PATTERNS.containsKey(key)) {
            return PATTERNS.get(key);
        } else {
            // cache key
            return buildPattern(key);
        }
    }

    public static Pattern getPattern(String key, boolean custom) {
        if (PATTERNS.containsKey(key)) {
            return PATTERNS.get(key);
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
        String regex = TardisPlugin.plugin.getHandlesConfig().getString(key);
        if (regex != null) {
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            PATTERNS.put(key, pattern);
        }
        return pattern;
    }
}
