/*
 * Copyright (C) 2021 eccentric_nz
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

import me.eccentric_nz.TARDIS.enumeration.Consoles;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum TARDISARS implements ARS {

    ANTIGRAVITY("SANDSTONE", "Anti-gravity Well", 1),
    APIARY("BEE_NEST", "Apiary", 1),
    AQUARIUM("TUBE_CORAL_BLOCK", "Aquarium", 1),
    ARBORETUM("OAK_LEAVES", "Arboretum", 1),
    BAKER("END_STONE", "4th Doctor's Secondary Console", 1),
    BAMBOO("BAMBOO", "Bamboo", 1),
    BEDROOM("GLOWSTONE", "Bedroom", 1),
    BIRDCAGE("YELLOW_GLAZED_TERRACOTTA", "Bird Cage", 1),
    CHEMISTRY("BLAST_FURNACE", "Chemistry Lab", 1),
    EMPTY("GLASS", "Empty", 1),
    FARM("DIRT", "Mob Farm", 1),
    GEODE("AMETHYST_BLOCK", "Geode", 1),
    GRAVITY("MOSSY_COBBLESTONE", "Gravity Well", 1),
    GREENHOUSE("MELON", "Greenhouse", 1),
    HARMONY("STONE_BRICK_STAIRS", "Eye of Harmony", 1),
    HUTCH("ACACIA_LOG", "Rabbit Hutch", 1),
    IGLOO("PACKED_ICE", "Igloo", 1),
    KITCHEN("PUMPKIN", "Kitchen", 1),
    LAZARUS("FURNACE", "Genetic Manipulator", 1),
    LIBRARY("ENCHANTING_TABLE", "Library", 1),
    MAZE("LODESTONE", "Maze", 1),
    MUSHROOM("GRAVEL", "Mycellium", 1),
    NETHER("BLACKSTONE", "Nether", 1),
    PASSAGE("CLAY", "Passage", 1),
    POOL("SNOW_BLOCK", "Pool", 1),
    RAIL("HOPPER", "Rail Transfer Station", 1),
    RENDERER("TERRACOTTA", "Exterior Renderer", 1),
    SHELL("DEAD_BRAIN_CORAL_BLOCK", "Shell", 1),
    SMELTER("CHEST", "Smelter", 1),
    STABLE("HAY_BLOCK", "Horse Stable", 1),
    STALL("BROWN_GLAZED_TERRACOTTA", "Llama Stall", 1),
    TRENZALORE("BRICKS", "Trenzalore", 1),
    VAULT("DISPENSER", "Storage Vault", 1),
    VILLAGE("OAK_LOG", "Village", 1),
    WOOD("OAK_PLANKS", "Wood Secondary Console", 1),
    WORKSHOP("RED_NETHER_BRICKS", "Workshop", 1),
    ZERO("GRASS_BLOCK", "Zero Room", 0),
    JETTISON("TNT", "Jettison", 0),
    SLOT("STONE", "Empty slot", 0),
    CONSOLE("", "Console", 0);

    private final static HashMap<String, ARS> EXTENDED_MATERIAL = new HashMap<>();

    static {
        for (ARS room : values()) {
            EXTENDED_MATERIAL.put(room.getMaterial(), room);
        }
    }

    private final String material;
    private final String descriptiveName;
    private final String configPath;
    private final int offset;

    TARDISARS(String material, String descriptiveName, int offset) {
        this.material = material;
        this.descriptiveName = descriptiveName;
        configPath = toString();
        this.offset = offset;
    }

    /**
     * Attempts to get the TARDISARS for the given material.
     *
     * @param mat the Material of the ARS to get
     * @return ARS if found, or null
     */
    public static ARS ARSFor(String mat) {
        if (Consoles.getBY_MATERIALS().containsKey(mat)) {
            return CONSOLE;
        } else {
            return EXTENDED_MATERIAL.getOrDefault(mat, SLOT);
        }
    }

    public static void addNewARS(ARS room) {
        if (!EXTENDED_MATERIAL.containsKey(room.getMaterial())) {
            EXTENDED_MATERIAL.put(room.getMaterial(), room);
        }
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
     * Gets the descriptive room name.
     *
     * @return the descriptive room name
     */
    @Override
    public String getDescriptiveName() {
        return descriptiveName;
    }

    /**
     * Gets the config path for the room.
     *
     * @return the config path
     */
    @Override
    public String getConfigPath() {
        return configPath;
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
}
