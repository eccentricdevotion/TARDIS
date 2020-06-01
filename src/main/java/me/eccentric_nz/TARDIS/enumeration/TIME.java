package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

public enum TIME {

    MORNING(0),
    NOON(6000),
    NIGHT(12000),
    MIDNIGHT(18000),
    AM_6(0),
    AM_7(1000),
    AM_8(2000),
    AM_9(3000),
    AM_10(4000),
    AM_11(5000),
    PM_12(6000),
    PM_1(7000),
    PM_2(8000),
    PM_3(9000),
    PM_4(10000),
    PM_5(11000),
    PM_6(12000),
    PM_7(13000),
    PM_8(14000),
    PM_9(15000),
    PM_10(16000),
    PM_11(17000),
    AM_12(18000),
    AM_1(19000),
    AM_2(20000),
    AM_3(21000),
    AM_4(22000),
    AM_5(23000);

    private final long ticks;
    private final String name;
    private static final HashMap<String, TIME> BY_NAME = new HashMap<>();

    TIME(long ticks) {
        this.ticks = ticks;
        name = getName();
    }

    public long getTicks() {
        return ticks;
    }

    private String getName() {
        String[] split = toString().split("_");
        return (split.length == 2) ? split[1] + split[0] : split[0];
    }

    public static HashMap<String, TIME> getByName() {
        return BY_NAME;
    }

    static {
        for (TIME time : values()) {
            BY_NAME.put(time.name, time);
        }
    }
}
