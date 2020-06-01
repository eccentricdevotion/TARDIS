package me.eccentric_nz.TARDIS.enumeration;

public enum WEATHER {

    CLEAR,
    RAIN,
    THUNDER;

    public static WEATHER fromString(String s) {
        String lower = s.toLowerCase();
        switch (lower) {
            case "r":
            case "rain":
            case "w":
            case "wet":
                return RAIN;
            case "t":
            case "thunder":
            case "l":
            case "lightning":
                return THUNDER;
            default:
                return CLEAR;
        }
    }
}
