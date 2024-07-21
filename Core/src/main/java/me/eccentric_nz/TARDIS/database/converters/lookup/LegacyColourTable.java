package me.eccentric_nz.TARDIS.database.converters.lookup;

import java.util.HashMap;

public class LegacyColourTable {

    public static final HashMap<Integer, String> LOOKUP = new HashMap<>();

    static {
        LOOKUP.put(0, "WHITE");
        LOOKUP.put(1, "ORANGE");
        LOOKUP.put(2, "MAGENTA");
        LOOKUP.put(3, "LIGHT_BLUE");
        LOOKUP.put(4, "YELLOW");
        LOOKUP.put(5, "LIME");
        LOOKUP.put(6, "PINK");
        LOOKUP.put(7, "GRAY");
        LOOKUP.put(8, "LIGHT_GRAY");
        LOOKUP.put(9, "CYAN");
        LOOKUP.put(10, "PURPLE");
        LOOKUP.put(11, "BLUE");
        LOOKUP.put(12, "BROWN");
        LOOKUP.put(13, "GREEN");
        LOOKUP.put(14, "RED");
        LOOKUP.put(15, "BLACK");
    }
}
