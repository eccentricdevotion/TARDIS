/*
 * Copyright (C) 2018 eccentric_nz
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

import org.bukkit.Material;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum TARDISARS implements ARS {

    // add offsets
    ANTIGRAVITY("SANDSTONE", "ANTIGRAVITY", "Anti-gravity Well", 1),
    AQUARIUM("TUBE_CORAL_BLOCK", "AQUARIUM", "Aquarium", 1),
    ARBORETUM("OAK_LEAVES", "ARBORETUM", "Arboretum", 1),
    BAKER("END_STON", "BAKER", "4th Doctor's Secondary Console", 1),
    BEDROOM("GLOWSTONE", "BEDROOM", "Bedroom", 1),
    BIRDCAGE("YELLOW_GLAZED_TERRACOTTA", "BIRDCAGE", "Bird Cage", 1),
    EMPTY("GLASS", "EMPTY", "Empty", 1),
    FARM("DIRT", "FARM", "Mob Farm", 1),
    GRAVITY("MOSSY_COBBLESTONE", "GRAVITY", "Gravity Well", 1),
    GREENHOUSE("MELON_BLOCK", "GREENHOUSE", "Greenhouse", 1),
    HARMONY("STONE_BRICK_STAIRS", "HARMONY", "Eye of Harmony", 1),
    HUTCH("ACACIA_LOG", "HUTCH", "Rabbit Hutch", 1),
    IGLOO("PACKED_ICE", "IGLOO", "Igloo", 1),
    KITCHEN("PUMPKIN", "KITCHEN", "Kitchen", 1),
    LAZARUS("FURNACE", "LAZARUS", "Genetic Manipulator", 1),
    LIBRARY("ENCHANTING_TABLE", "LIBRARY", "Library", 1),
    MUSHROOM("GRAVEL", "MUSHROOM", "Mycellium", 1),
    PASSAGE("CLAY", "PASSAGE", "Passage", 1),
    POOL("SNOW_BLOCK", "POOL", "Pool", 1),
    RAIL("HOPPER", "RAIL", "Rail Transfer Station", 1),
    RENDERER("TERRACOTTA", "RENDERER", "Exterior Renderer", 1),
    SHELL("DEAD_BRAIN_CORAL_BLOCK", "SHELL", "Shell", 1),
    SMELTER("CHEST", "SMELTER", "Smelter", 1),
    STABLE("HAY_BLOCK", "STABLE", "Horse Stable", 1),
    STALL("NETHER_WART_BLOCK", "STALL", "Llama Stall", 1),
    TRENZALORE("BRICKS", "TRENZALORE", "Trenzalore", 1),
    VAULT("DISPENSER", "VAULT", "Storage Vault", 1),
    VILLAGE("OAK_LOG", "VILLAGE", "Village", 1),
    WOOD("OAK_PLANKS", "WOOD", "Wood Secondary Console", 1),
    WORKSHOP("RED_NETHER_BRICKS", "WORKSHOP", "Workshop", 1),
    ZERO("GRASS_BLOCK", "ZERO", "Zero Room", 0),
    JETTISON("TNT", "JETTISON", "Jettison", 0),
    SLOT("STONE", "SLOT", "Empty slot", 0);
    private final String material;
    private final String name;
    private final String descriptiveName;
    private final int offset;
    private final static HashMap<Material, ARS> EXTENDED_MATERIAL = new HashMap<>();
    private final static HashMap<String, ARS> EXTENDED_NAME = new HashMap<>();

    TARDISARS(String material, String name, String descriptiveName, int offset) {
        this.material = material;
        this.name = name;
        this.descriptiveName = descriptiveName;
        this.offset = offset;
    }

    /**
     * Gets the room seed block material.
     *
     * @return the material
     */
    @Override
    public String getMaterial() {
        return material;
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
            EXTENDED_MATERIAL.put(Material.valueOf(room.getMaterial()), room);
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
     * @param material material of the ARS room to get
     * @return ARS room if found, or null
     */
    public static ARS ARSFor(Material material) {
        if (EXTENDED_MATERIAL.containsKey(material)) {
            return EXTENDED_MATERIAL.get(material);
        } else {
            return SLOT;
        }
    }

    public static void addNewARS(ARS room) {
        if (!EXTENDED_NAME.containsKey(room.getDescriptiveName())) {
            EXTENDED_NAME.put(room.getDescriptiveName(), room);
        }
        Material material = Material.valueOf(room.getMaterial());
        if (!EXTENDED_MATERIAL.containsKey(material)) {
            EXTENDED_MATERIAL.put(material, room);
        }
    }
}
