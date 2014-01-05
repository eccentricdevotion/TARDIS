/*
 * Copyright (C) 2013 eccentric_nz
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

import com.google.common.collect.Maps;
import java.util.Map;

/**
 *
 * @author eccentric_nz
 */
public enum MAP {

    ADMIN((byte) 1968, "Server Admin Circuit"),
    ARS((byte) 1973, "TARDIS ARS Circuit"),
    BIO((byte) 1969, "Bio-scanner Circuit"),
    CHAMELEON((byte) 1966, "TARDIS Chameleon Circuit"),
    DIAMOND((byte) 1971, "Diamond Disruptor Circuit"),
    EMERALD((byte) 1972, "Emerald Environment Circuit"),
    INPUT((byte) 1976, "TARDIS Input Circuit"),
    LOCATOR((byte) 1965, "TARDIS Locator Circuit"),
    MATERIALISATION((byte) 1964, "TARDIS Materialisation Circuit"),
    MEMORY((byte) 1975, "TARDIS Memory Circuit"),
    PERCEPTION((byte) 1978, "Perception Circuit"),
    REDSTONE((byte) 1970, "Redstone Activator Circuit"),
    SCANNER((byte) 1977, "TARDIS Scanner Circuit"),
    SONIC((byte) 1967, "Sonic Oscillator"),
    STATTENHEIM((byte) 1963, "TARDIS Stattenheim Circuit"),
    TEMPORAL((byte) 1974, "TARDIS Temporal Circuit"),;

    byte data;
    String displayName;
    private final static Map<Byte, MAP> BY_BYTE = Maps.newHashMap();

    private MAP(byte data, String displayName) {
        this.data = data;
        this.displayName = displayName;
    }

    public byte getData() {
        return data;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MAP getMap(final Byte data) {
        return BY_BYTE.get(data);
    }

    static {
        for (MAP map : values()) {
            BY_BYTE.put(map.getData(), map);
        }
    }
}
