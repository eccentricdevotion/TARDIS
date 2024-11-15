/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodeldata.keys.*;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import org.bukkit.NamespacedKey;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public enum TARDISARS implements ARS {

    ALLAY("LIGHT_BLUE_CONCRETE", "Allay House", 1, LightBlueConcrete.ALLAY.getKey()),
    ANTIGRAVITY("SANDSTONE", "Anti-gravity Well", 1, Sandstone.ANTIGRAVITY.getKey()),
    APIARY("BEE_NEST", "Apiary", 1, BeeNest.APIARY.getKey()),
    AQUARIUM("TUBE_CORAL_BLOCK", "Aquarium", 1, TubeCoralBlock.AQUARIUM.getKey()),
    ARBORETUM("OAK_LEAVES", "Arboretum", 1, OakLeaves.ARBORETUM.getKey()),
    BAKER("END_STONE", "4th Doctor's Secondary Console", 1, EndStone.BAKER.getKey()),
    BAMBOO("BAMBOO", "Bamboo", 1, Bamboo.BAMBOO.getKey()),
    BEDROOM("GLOWSTONE", "Bedroom", 1, Glowstone.BEDROOM.getKey()),
    BIRDCAGE("YELLOW_GLAZED_TERRACOTTA", "Bird Cage", 1, YellowGlazedTerracotta.BIRDCAGE.getKey()),
    CHEMISTRY("BLAST_FURNACE", "Chemistry Lab", 1, BlastFurnace.CHEMISTRY.getKey()),
    EMPTY("GLASS", "Empty", 1, Glass.EMPTY.getKey()),
    EYE("SHROOMLIGHT", "Eye of Harmony", 1, Shroomlight.EYE.getKey()),
    FARM("DIRT", "Mob Farm", 1, Dirt.FARM.getKey()),
    GARDEN("CHERRY_LEAVES", "Flower Garden", 1, CherryLeaves.GARDEN.getKey()),
    GEODE("AMETHYST_BLOCK", "Geode", 1, AmethystBlock.GEODE.getKey()),
    GRAVITY("MOSSY_COBBLESTONE", "Gravity Well", 1, MossyCobblestone.GRAVITY.getKey()),
    GREENHOUSE("MELON", "Greenhouse", 1, Melon.GREENHOUSE.getKey()),
    HARMONY("STONE_BRICK_STAIRS", "Condenser", 1, StoneBrickStairs.HARMONY.getKey()),
    HUTCH("ACACIA_LOG", "Rabbit Hutch", 1, AcaciaLog.HUTCH.getKey()),
    IGLOO("PACKED_ICE", "Igloo", 1, PackedIce.IGLOO.getKey()),
    IISTUBIL("WHITE_GLAZED_TERRACOTTA", "Camel Stable", 1, WhiteGlazedTerracotta.IISTUBIL.getKey()),
    KITCHEN("PUMPKIN", "Kitchen", 1, Pumpkin.KITCHEN.getKey()),
    LAVA("MAGMA_BLOCK", "Lava", 1, MagmaBlock.LAVA.getKey()),
    LAZARUS("FURNACE", "Genetic Manipulator", 1, Furnace.LAZARUS.getKey()),
    LIBRARY("ENCHANTING_TABLE", "Library", 1, EnchantingTable.LIBRARY.getKey()),
    MANGROVE("MUDDY_MANGROVE_ROOTS", "Mangrove", 1, MuddyMangroveRoots.MANGROVE.getKey()),
    MAZE("LODESTONE", "Maze", 1, Lodestone.MAZE.getKey()),
    MUSHROOM("GRAVEL", "Mycellium", 1, Gravel.MUSHROOM.getKey()),
    NETHER("BLACKSTONE", "Nether", 1, Blackstone.NETHER.getKey()),
    OBSERVATORY("POLISHED_BLACKSTONE_BRICKS", "Astronomical Observatory", 1, PolishedBlackstoneBricks.OBSERVATORY.getKey()),
    PASSAGE("CLAY", "Passage", 1, Clay.PASSAGE.getKey()),
    PEN("MOSS_BLOCK", "Sniffer Pen", 1, MossBlock.PEN.getKey()),
    POOL("SNOW_BLOCK", "Pool", 1, SnowBlock.POOL.getKey()),
    RAIL("HOPPER", "Rail Transfer Station", 1, Hopper.RAIL.getKey()),
    RENDERER("TERRACOTTA", "Exterior Renderer", 1, Terracotta.RENDERER.getKey()),
    SHELL("DEAD_BRAIN_CORAL_BLOCK", "Shell", 1, DeadBrainCoralBlock.SHELL.getKey()),
    SMELTER("CHEST", "Smelter", 1, Chest.SMELTER.getKey()),
    STABLE("HAY_BLOCK", "Horse Stable", 1, HayBlock.STABLE.getKey()),
    STALL("BROWN_GLAZED_TERRACOTTA", "Llama Stall", 1, BrownGlazedTerracotta.STALL.getKey()),
    SURGERY("RED_CONCRETE", "Hospital Surgery", 1, RedConcrete.SURGERY.getKey()),
    TRENZALORE("BRICKS", "Trenzalore", 1, Bricks.TRENZALORE.getKey()),
    VAULT("DISPENSER", "Storage Vault", 1, Dispenser.VAULT.getKey()),
    VILLAGE("OAK_LOG", "Village", 1, OakLog.VILLAGE.getKey()),
    WOOD("OAK_PLANKS", "Wood Secondary Console", 1, OakPlanks.WOOD.getKey()),
    WORKSHOP("RED_NETHER_BRICKS", "Workshop", 1, RedNetherBricks.WORKSHOP.getKey()),
    ZERO("GRASS_BLOCK", "Zero Room", 0, null),
    JETTISON("TNT", "Jettison", 0, Tnt.JETTISON.getKey()),
    SLOT("STONE", "Empty slot", 0, Stone.SLOT.getKey()),
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
        if (Consoles.getBY_MATERIALS().containsKey(mat)) {
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
