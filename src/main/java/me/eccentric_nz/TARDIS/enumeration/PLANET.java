package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

public enum PLANET {

    GALLIFREY("Gallifrey"),
    SILURIA("Siluria"),
    SKARO("Skaro");

    private final String name;
    private static final HashMap<String, PLANET> PLANET_MAP = new HashMap<>();

    PLANET(String name) {
        this.name = name;
    }

    static {
        for (PLANET planet : values()) {
            PLANET_MAP.put(planet.name, planet);
        }
    }

    public String getName() {
        return name;
    }

    public static HashMap<String, PLANET> getPlanetMap() {
        return PLANET_MAP;
    }
}
