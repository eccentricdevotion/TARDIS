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
package me.eccentric_nz.TARDIS.ARS;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.util.Java15Compat;

/**
 *
 * @author eccentric_nz
 */
public enum TARDISARS {

    // add offsets
    PASSAGE(82, "Passage", 1),
    ARBORETUM(18, "Arboretum", 1),
    BEDROOM(89, "Bedroom", 1),
    KITCHEN(86, "Kitchen", 1),
    LIBRARY(116, "Library", 1),
    POOL(80, "Pool", 1),
    VAULT(23, "Storage Vault", 1),
    WORKSHOP(112, "Workshop", 1),
    EMPTY(20, "Empty", 1),
    FARM(3, "Mob Farm", 1),
    WOOD(5, "Wood Secondary Console", 1),
    MUSHROOM(13, "Mycellium", 1),
    VILLAGE(17, "Village", 1),
    GRAVITY(48, "Gravity Well", 1),
    ANTIGRAVITY(24, "Anti-gravity Well", 1),
    TRENZALORE(45, "Trenzalore", 1),
    RAIL(49, "Rail Transfer Station", 1),
    GREENHOUSE(103, "Greenhouse", 1),
    HARMONY(109, "Eye of Harmony", 1),
    BAKER(121, "4th Doctor's Secondary Console", 1),
    STABLE(170, "Horse Stable", 1),
    JETTISON(46, "Jettison", 0),
    SLOT(1, "Empty slot", 0),
    ARS(159, "ARS Console", 0),
    BUDGET(42, "Default Console", 0),
    DELUXE(57, "Supersized Deluxe Console", 0),
    ELEVENTH(133, "11th Doctor's Console", 0),
    REDSTONE(152, "Redstone Console", 0),
    BIGGER(41, "A Bigger Console", 0),
    PLANK(22, "Wood Console", 0),
    STEAMPUNK(173, "Steampunk Console", 0),
    TOM(155, "4th Doctor's Console", 0);
    private final int id;
    private final String name;
    private final int offset;
    private static TARDISARS[] byId = new TARDISARS[22];
    private final static Map<String, TARDISARS> BY_NAME = Maps.newHashMap();

    private TARDISARS(int id, String name, int offset) {
        this.id = id;
        this.name = name;
        this.offset = offset;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOffset() {
        return offset;
    }

    /**
     * Attempts to get the ARS room with the given ID
     *
     * @param id ID of the ARS room to get
     * @return ARS room if found, or null
     */
    public static TARDISARS getARS(final int id) {
        if (byId.length > id && id >= 0) {
            return byId[id];
        } else {
            return SLOT;
        }
    }

    /**
     * Attempts to get the TARDISARS with the given name.
     *
     * @param name Name of the ARS to get
     * @return ARS if found, or null
     */
    public static TARDISARS getARS(final String name) {
        return BY_NAME.get(name);
    }

    static {
        for (TARDISARS room : values()) {
            if (byId.length > room.id) {
                byId[room.id] = room;
            } else {
                byId = Java15Compat.Arrays_copyOfRange(byId, 0, room.id + 2);
                byId[room.id] = room;
            }
            BY_NAME.put(room.getName(), room);
        }
    }
}
