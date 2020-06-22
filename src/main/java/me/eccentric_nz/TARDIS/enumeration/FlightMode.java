package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

public enum FlightMode {

    NORMAL(1),
    REGULATOR(2),
    MANUAL(3);

    private static final HashMap<Integer, FlightMode> byMode = new HashMap<>();

    static {
        for (FlightMode fm : values()) {
            byMode.put(fm.mode, fm);
        }
    }

    private final int mode;

    FlightMode(int mode) {
        this.mode = mode;
    }

    public static HashMap<Integer, FlightMode> getByMode() {
        return byMode;
    }

    public int getMode() {
        return mode;
    }
}
