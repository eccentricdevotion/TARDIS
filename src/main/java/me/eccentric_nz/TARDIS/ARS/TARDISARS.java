/*
 * Copyright (C) 2026 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodels.keys.RoomVariant;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum TARDISARS implements ARS {

    ALLAY("LIGHT_BLUE_CONCRETE", "Allay House", 1, RoomVariant.ALLAY.getKey()),
    ANTIGRAVITY("SANDSTONE", "Anti-gravity Well", 1, RoomVariant.ANTIGRAVITY.getKey()),
    APIARY("BEE_NEST", "Apiary", 1, RoomVariant.APIARY.getKey()),
    AQUARIUM("TUBE_CORAL_BLOCK", "Aquarium", 1, RoomVariant.AQUARIUM.getKey()),
    ARBORETUM("OAK_LEAVES", "Arboretum", 1, RoomVariant.ARBORETUM.getKey()),
    BAKER("END_STONE", "4th Doctor's Secondary Console", 1, RoomVariant.BAKER.getKey()),
    BAMBOO("BAMBOO", "Bamboo", 1, RoomVariant.BAMBOO.getKey()),
    BEDROOM("GLOWSTONE", "Bedroom", 1, RoomVariant.BEDROOM.getKey()),
    BIRDCAGE("YELLOW_GLAZED_TERRACOTTA", "Bird Cage", 1, RoomVariant.BIRDCAGE.getKey()),
    CHEMISTRY("BLAST_FURNACE", "Chemistry Lab", 1, RoomVariant.CHEMISTRY.getKey()),
    EMPTY("GLASS", "Empty", 1, RoomVariant.EMPTY.getKey()),
    EYE("SHROOMLIGHT", "Eye of Harmony", 1, RoomVariant.EYE.getKey()),
    FARM("DIRT", "Mob Farm", 1, RoomVariant.FARM.getKey()),
    GARDEN("CHERRY_LEAVES", "Flower Garden", 1, RoomVariant.GARDEN.getKey()),
    GEODE("AMETHYST_BLOCK", "Geode", 1, RoomVariant.GEODE.getKey()),
    GRAVITY("MOSSY_COBBLESTONE", "Gravity Well", 1, RoomVariant.GRAVITY.getKey()),
    GREENHOUSE("MELON", "Greenhouse", 1, RoomVariant.GREENHOUSE.getKey()),
    HAPPY("STRIPPED_OAK_LOG", "Happy Ghast Dock", 1, RoomVariant.HAPPY.getKey()),
    HARMONY("STONE_BRICK_STAIRS", "Condenser", 1, RoomVariant.HARMONY.getKey()),
    HUTCH("ACACIA_LOG", "Rabbit Hutch", 1, RoomVariant.HUTCH.getKey()),
    IGLOO("PACKED_ICE", "Igloo", 1, RoomVariant.IGLOO.getKey()),
    IISTUBIL("WHITE_GLAZED_TERRACOTTA", "Camel Stable", 1, RoomVariant.IISTUBIL.getKey()),
    KITCHEN("PUMPKIN", "Kitchen", 1, RoomVariant.KITCHEN.getKey()),
    LAVA("MAGMA_BLOCK", "Lava", 1, RoomVariant.LAVA.getKey()),
    LAZARUS("FURNACE", "Genetic Manipulator", 1, RoomVariant.LAZARUS.getKey()),
    LIBRARY("ENCHANTING_TABLE", "Library", 1, RoomVariant.LIBRARY.getKey()),
    MANGROVE("MUDDY_MANGROVE_ROOTS", "Mangrove", 1, RoomVariant.MANGROVE.getKey()),
    MAZE("LODESTONE", "Maze", 1, RoomVariant.MAZE.getKey()),
    MUSHROOM("GRAVEL", "Mycellium", 1, RoomVariant.MUSHROOM.getKey()),
    NAUTILUS("DEAD_HORN_CORAL_BLOCK", "Nautilus Tank", 1, RoomVariant.NAUTILUS.getKey()),
    NETHER("BLACKSTONE", "Nether", 1, RoomVariant.NETHER.getKey()),
    OBSERVATORY("POLISHED_BLACKSTONE_BRICKS", "Astronomical Observatory", 1, RoomVariant.OBSERVATORY.getKey()),
    PASSAGE("CLAY", "Passage", 1, RoomVariant.PASSAGE.getKey()),
    PEN("MOSS_BLOCK", "Sniffer Pen", 1, RoomVariant.PEN.getKey()),
    POOL("SNOW_BLOCK", "Pool", 1, RoomVariant.POOL.getKey()),
    RAIL("HOPPER", "Rail Transfer Station", 1, RoomVariant.RAIL.getKey()),
    RENDERER("TERRACOTTA", "Exterior Renderer", 1, RoomVariant.RENDERER.getKey()),
    SHELL("DEAD_BRAIN_CORAL_BLOCK", "Shell", 1, RoomVariant.SHELL.getKey()),
    SMELTER("CHEST", "Smelter", 1, RoomVariant.SMELTER.getKey()),
    STABLE("HAY_BLOCK", "Horse Stable", 1, RoomVariant.STABLE.getKey()),
    STALL("BROWN_GLAZED_TERRACOTTA", "Llama Stall", 1, RoomVariant.STALL.getKey()),
    SURGERY("RED_CONCRETE", "Hospital Surgery", 1, RoomVariant.SURGERY.getKey()),
    TRENZALORE("BRICKS", "Trenzalore", 1, RoomVariant.TRENZALORE.getKey()),
    VAULT("DISPENSER", "Storage Vault", 1, RoomVariant.VAULT.getKey()),
    VILLAGE("OAK_LOG", "Village", 1, RoomVariant.VILLAGE.getKey()),
    WOOD("OAK_PLANKS", "Wood Secondary Console", 1, RoomVariant.WOOD.getKey()),
    WORKSHOP("RED_NETHER_BRICKS", "Workshop", 1, RoomVariant.WORKSHOP.getKey()),
    ZERO("GRASS_BLOCK", "Zero Room", 0, null),
    JETTISON("TNT", "Jettison", 0, RoomVariant.JETTISON.getKey()),
    SLOT("STONE", "Empty slot", 0, RoomVariant.SLOT.getKey()),
    CUSTOM("", "Custom room", 0, null),
    CONSOLE("", "Console", 0, null);

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
    private final NamespacedKey key;

    TARDISARS(String material, String descriptiveName, int offset, NamespacedKey key) {
        this.material = material;
        this.descriptiveName = descriptiveName;
        configPath = toString();
        this.offset = offset;
        this.key = key;
    }

    /**
     * Attempts to get the TARDISARS for the given material.
     *
     * @param mat the Material of the ARS to get
     * @return ARS if found, or null
     */
    public static ARS ARSFor(String mat) {
        if (Desktops.getBY_MATERIALS().containsKey(mat)) {
            return CONSOLE;
        } else {
            return EXTENDED_MATERIAL.getOrDefault(mat, CUSTOM);
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

    /**
     * Gets the item model namespaced key
     *
     * @return the namespaced key
     */
    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
