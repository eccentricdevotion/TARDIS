/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;

/**
 *
 * @author eccentric_nz
 */
public enum TARDISARS implements ARS {

    // add offsets
    PASSAGE(82, "PASSAGE", "Passage", 1, false),
    ARBORETUM(18, "ARBORETUM", "Arboretum", 1, false),
    BEDROOM(89, "BEDROOM", "Bedroom", 1, false),
    KITCHEN(86, "KITCHEN", "Kitchen", 1, false),
    LIBRARY(116, "LIBRARY", "Library", 1, false),
    LAZARUS(61, "LAZARUS", "Genetic Manipulator", 1, false),
    POOL(80, "POOL", "Pool", 1, false),
    VAULT(23, "VAULT", "Storage Vault", 1, false),
    WORKSHOP(112, "WORKSHOP", "Workshop", 1, false),
    EMPTY(20, "EMPTY", "Empty", 1, false),
    FARM(3, "FARM", "Mob Farm", 1, false),
    WOOD(5, "WOOD", "Wood Secondary Console", 1, false),
    MUSHROOM(13, "MUSHROOM", "Mycellium", 1, false),
    VILLAGE(17, "VILLAGE", "Village", 1, false),
    GRAVITY(48, "GRAVITY", "Gravity Well", 1, false),
    ANTIGRAVITY(24, "ANTIGRAVITY", "Anti-gravity Well", 1, false),
    TRENZALORE(45, "TRENZALORE", "Trenzalore", 1, false),
    RAIL(49, "RAIL", "Rail Transfer Station", 1, false),
    RENDERER(172, "RENDERER", "Exterior Renderer", 1, false),
    GREENHOUSE(103, "GREENHOUSE", "Greenhouse", 1, false),
    HARMONY(109, "HARMONY", "Eye of Harmony", 1, false),
    BAKER(121, "BAKER", "4th Doctor's Secondary Console", 1, false),
    STABLE(170, "STABLE", "Horse Stable", 1, false),
    JETTISON(46, "JETTISON", "Jettison", 0, false),
    SLOT(1, "SLOT", "Empty slot", 0, false),
    ARS(155, "ARS", "ARS Console", 0, true),
    BUDGET(42, "BUDGET", "Default Console", 0, true),
    DELUXE(57, "DELUXE", "Supersized Deluxe Console", 0, true),
    ELEVENTH(133, "ELEVENTH", "11th Doctor's Console", 0, true),
    REDSTONE(152, "REDSTONE", "Redstone Console", 0, true),
    BIGGER(41, "BIGGER", "A Bigger Console", 0, true),
    PLANK(47, "PLANK", "Wood Console", 0, true),
    STEAMPUNK(173, "STEAMPUNK", "Steampunk Console", 0, true),
    TOM(22, "TOM", "4th Doctor's Console", 0, true),
    WAR(159, "WAR", "War Doctor's Console", 0, true),
    CUSTOM(122, "CUSTOM", "Custom Console", 0, true);
    private final int id;
    private final String name;
    private final String descriptiveName;
    private final int offset;
    private final boolean console;
    private final static HashMap<Integer, ARS> EXTENDED_ID = new HashMap<Integer, ARS>();
    private final static HashMap<String, ARS> EXTENDED_NAME = new HashMap<String, ARS>();

    private TARDISARS(int id, String name, String descriptiveName, int offset, boolean console) {
        this.id = id;
        this.name = name;
        this.descriptiveName = descriptiveName;
        this.offset = offset;
        this.console = console;
    }

    /**
     * Gets the room seed block ID.
     *
     * @return the ID
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Gets the actual room name.
     *
     * @return the actual room name
     */
    @Override
    public String getActualName() {
        return name;
    }

    /**
     * Gets the descriptive room name.
     *
     * @return the descriptive room name
     */
    @Override
    public String getDescriptiveName() {
        return descriptiveName;
    }

    /**
     * Gets the room offset.
     *
     * @return the offset
     */
    @Override
    public int getOffset() {
        return offset;
    }

    /**
     * Gets the whether the room is a console.
     *
     * @return true if it is a console, otherwise false
     */
    @Override
    public boolean isConsole() {
        return console;
    }

    static {
        for (ARS room : values()) {
            EXTENDED_NAME.put(room.getDescriptiveName(), room);
            EXTENDED_ID.put(room.getId(), room);
        }
    }

    /**
     * Attempts to get the TARDISARS with the given name.
     *
     * @param name Name of the ARS to get
     * @return ARS if found, or null
     */
    public static ARS ARSFor(String name) {
        return EXTENDED_NAME.get(name);
    }

    /**
     * Attempts to get the ARS room with the given ID
     *
     * @param id ID of the ARS room to get
     * @return ARS room if found, or null
     */
    public static ARS ARSFor(int id) {
        if (EXTENDED_ID.containsKey(id)) {
            return EXTENDED_ID.get(id);
        } else {
            return SLOT;
        }
    }

    public static void addNewARS(ARS room) {
        if (!EXTENDED_NAME.containsKey(room.getDescriptiveName())) {
            EXTENDED_NAME.put(room.getDescriptiveName(), room);
        }
        if (!EXTENDED_ID.containsKey(room.getId())) {
            EXTENDED_ID.put(room.getId(), room);
        }
    }
}
