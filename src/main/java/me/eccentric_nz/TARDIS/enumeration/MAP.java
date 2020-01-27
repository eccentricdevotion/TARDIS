/*
 * Copyright (C) 2020 eccentric_nz
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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.enumeration;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum MAP {

    ADMIN("Server Admin Circuit", 1968),
    ARS("TARDIS ARS Circuit", 1973),
    BIO("Bio-scanner Circuit", 1969),
    CHAMELEON("TARDIS Chameleon Circuit", 1966),
    DIAMOND("Diamond Disruptor Circuit", 1971),
    EMERALD("Emerald Environment Circuit", 1972),
    IGNITE("Ignite Circuit", 1982),
    INPUT("TARDIS Input Circuit", 1976),
    INVISIBILITY("TARDIS Invisibility Circuit", 1981),
    LOCATOR("TARDIS Locator Circuit", 1965),
    MATERIALISATION("TARDIS Materialisation Circuit", 1964),
    MEMORY("TARDIS Memory Circuit", 1975),
    PAINTER("Painter Circuit", 1979),
    PERCEPTION("Perception Circuit", 1978),
    PICKUP("Pickup Arrows Circuit", 1984),
    RANDOM("TARDIS Randomiser Circuit", 1980),
    REDSTONE("Redstone Activator Circuit", 1970),
    RIFT("Rift Circuit", 1983),
    SCANNER("TARDIS Scanner Circuit", 1977),
    SONIC("Sonic Oscillator", 1967),
    STATTENHEIM("TARDIS Stattenheim Circuit", 1963),
    TEMPORAL("TARDIS Temporal Circuit", 1974);

    String displayName;
    int id;
    private static final HashMap<Integer, MAP> BY_ID = new HashMap<>();

    MAP(String displayName, int id) {
        this.displayName = displayName;
        this.id = id;
    }

    static {
        for (MAP map : values()) {
            BY_ID.put(map.id, map);
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    public static HashMap<Integer, MAP> getById() {
        return BY_ID;
    }
}
