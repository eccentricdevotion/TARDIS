/*
 * Copyright (C) 2016 eccentric_nz
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
    ANTIGRAVITY(24, "ANTIGRAVITY", "Anti-gravity Well", 1),
    ARBORETUM(18, "ARBORETUM", "Arboretum", 1),
    BAKER(121, "BAKER", "4th Doctor's Secondary Console", 1),
    BEDROOM(89, "BEDROOM", "Bedroom", 1),
    EMPTY(20, "EMPTY", "Empty", 1),
    FARM(3, "FARM", "Mob Farm", 1),
    GRAVITY(48, "GRAVITY", "Gravity Well", 1),
    GREENHOUSE(103, "GREENHOUSE", "Greenhouse", 1),
    HARMONY(109, "HARMONY", "Eye of Harmony", 1),
    HUTCH(162, "HUTCH", "Rabbit Hutch", 1),
    IGLOO(174, "IGLOO", "Igloo", 1),
    KITCHEN(86, "KITCHEN", "Kitchen", 1),
    LAZARUS(61, "LAZARUS", "Genetic Manipulator", 1),
    LIBRARY(116, "LIBRARY", "Library", 1),
    MUSHROOM(13, "MUSHROOM", "Mycellium", 1),
    PASSAGE(82, "PASSAGE", "Passage", 1),
    POOL(80, "POOL", "Pool", 1),
    RAIL(154, "RAIL", "Rail Transfer Station", 1),
    RENDERER(172, "RENDERER", "Exterior Renderer", 1),
    STABLE(170, "STABLE", "Horse Stable", 1),
    TRENZALORE(45, "TRENZALORE", "Trenzalore", 1),
    VAULT(23, "VAULT", "Storage Vault", 1),
    VILLAGE(17, "VILLAGE", "Village", 1),
    WOOD(5, "WOOD", "Wood Secondary Console", 1),
    WORKSHOP(112, "WORKSHOP", "Workshop", 1),
    ZERO(2, "ZERO", "Zero Room", 0),
    JETTISON(46, "JETTISON", "Jettison", 0),
    SLOT(1, "SLOT", "Empty slot", 0);
    private final int id;
    private final String name;
    private final String descriptiveName;
    private final int offset;
    private final static HashMap<Integer, ARS> EXTENDED_ID = new HashMap<Integer, ARS>();
    private final static HashMap<String, ARS> EXTENDED_NAME = new HashMap<String, ARS>();

    private TARDISARS(int id, String name, String descriptiveName, int offset) {
        this.id = id;
        this.name = name;
        this.descriptiveName = descriptiveName;
        this.offset = offset;
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
