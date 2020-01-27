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

import com.google.common.collect.Maps;
import org.bukkit.Material;

import java.util.Map;

/**
 * @author eccentric_nz
 */
public enum PRESET {

    ANDESITE(Material.GOLD_INGOT, "ANDESITE", "BOX", true, true),
    ANGEL(Material.FEATHER, "WEEPING", "ANGELS HAVE", false, false),
    APPERTURE(Material.STICK, "APERTURE", "SCIENCE", false, false),
    CAKE(Material.CAKE, "CAKE AND", "EAT IT TOO", true, true),
    CANDY(Material.SUGAR, "", "", false, false),
    CHALICE(Material.QUARTZ, "", "", false, false),
    CHORUS(Material.CHORUS_FLOWER, "", "", true, true),
    CREEPY(Material.STRING, "HAUNTED", "HOUSE", true, true),
    DESERT(Material.SAND, "DESERT", "TEMPLE", true, true),
    DIORITE(Material.EMERALD, "DIORITE", "BOX", true, true),
    DUCK(Material.OAK_BUTTON, "", "", false, false),
    FACTORY(Material.IRON_INGOT, "", "", true, true),
    FENCE(Material.IRON_BARS, "RANDOM", "FENCE", false, false),
    FLOWER(Material.POPPY, "", "", false, false),
    GAZEBO(Material.OAK_FENCE, "CHILLED OUT", "GAZEBO", false, false),
    GRANITE(Material.REDSTONE, "GRANITE", "BOX", true, true),
    GRAVESTONE(Material.END_STONE, "HERE", "LIES", false, false),
    HELIX(Material.STONE_BRICK_STAIRS, "INDUSTRIAL", "DOUBLE HELIX", false, false),
    INVISIBLE(Material.BARRIER, "", "", true, true),
    JAIL(Material.STONE_BRICKS, "$50,000", "REWARD FOR", false, false),
    JUNGLE(Material.VINE, "JUNGLE", "TEMPLE", true, true),
    LAMP(Material.GLOWSTONE_DUST, "LONELY", "LAMP POST", false, false),
    LIBRARY(Material.BOOK, "LIBRARY OF", "TIME LORE", false, false),
    LIGHTHOUSE(Material.TORCH, "TINY", "LIGHTHOUSE", true, true),
    MINESHAFT(Material.RAIL, "ROAD TO", "EL DORADO", false, false),
    NETHER(Material.NETHERRACK, "", "", true, true),
    NEW(Material.STONE_SLAB, "POLICE", "BOX", true, true),
    OLD(Material.BLUE_WOOL, "POLICE", "BOX", true, true),
    PANDORICA(Material.COAL, "PANDORICA", "", false, false),
    PARTY(Material.FIREWORK_ROCKET, "PARTY", "TENT", true, true),
    PEANUT(Material.TERRACOTTA, "JAR OF", "PEANUT BUTTER", true, false),
    PINE(Material.SPRUCE_LEAVES, "PINE", "TREE", false, false),
    PORTAL(Material.OAK_DOOR, "PORTAL TO", "SOMEWHERE", true, false),
    PRISMARINE(Material.PRISMARINE, "GUARDIAN", "TEMPLE", true, true),
    PUNKED(Material.PISTON, "JUST GOT", "PUNKED", false, false),
    RENDER(Material.FIRE, "", "", false, false),
    ROBOT(Material.COBBLESTONE_WALL, "WILL BE", "DELETED", false, false),
    SHROOM(Material.BROWN_MUSHROOM, "TRIPPY", "SPACE SHROOM", false, false),
    SNOWMAN(Material.SNOWBALL, "TAKES ONE", "TO SNOW ONE", false, false),
    STONE(Material.STONE, "STONE BRICK", "COLUMN", true, true),
    SUBMERGED(Material.DIRT, "", "", false, false),
    SWAMP(Material.LILY_PAD, "SIGN ABOVE", "THE DOOR", true, true),
    TELEPHONE(Material.GLASS, "TELEPHONE", "BOX", true, true),
    THEEND(Material.BEDROCK, "DRAGON", "SLAYING", true, true),
    TOILET(Material.LEVER, "", "", false, false),
    TOPSYTURVEY(Material.BUCKET, "Topsy-turvey", "BOX O' MARVEL", false, false),
    TORCH(Material.FLINT_AND_STEEL, "", "", false, false),
    VILLAGE(Material.COBBLESTONE, "VILLAGE", "HOUSE", true, true),
    WELL(Material.MOSSY_COBBLESTONE, "", "", false, false),
    WINDMILL(Material.OAK_PLANKS, "VERY SMALL", "WINDMILL", true, false),
    YELLOW(Material.GOLD_NUGGET, "YELLOW", "SUBMARINE", true, true),
    CUSTOM(Material.OBSIDIAN, "", "", false, false),
    JUNK(Material.BARRIER, "", "", false, false),
    JUNK_MODE(Material.BARRIER, "", "", false, false),
    CONSTRUCT(Material.BARRIER, "CONSTRUCT", "", true, true),
    // biome adaptive
    EXTREME_HILLS(Material.BARRIER, "EXTREME_HILLS", "", true, true),
    FOREST(Material.BARRIER, "FOREST", "", true, true),
    ICE_FLATS(Material.BARRIER, "ICE_FLATS", "", true, true),
    ICE_SPIKES(Material.BARRIER, "ICE_SPIKES", "", true, true),
    MESA(Material.BARRIER, "MESA", "", true, true),
    PLAINS(Material.BARRIER, "PLAINS", "", true, true),
    ROOFED_FOREST(Material.BARRIER, "ROOFED_FOREST", "", true, true),
    SAVANNA(Material.BARRIER, "SAVANNA", "", true, true),
    TAIGA(Material.BARRIER, "TAIGA", "", true, true),
    COLD_TAIGA(Material.BARRIER, "COLD_TAIGA", "", true, true),
    BOAT(Material.BARRIER, "BOAT", "", false, false),
    // coloured police boxes
    POLICE_BOX_BLUE(Material.BLUE_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_WHITE(Material.WHITE_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_ORANGE(Material.ORANGE_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_MAGENTA(Material.MAGENTA_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_LIGHT_BLUE(Material.LIGHT_BLUE_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_YELLOW(Material.YELLOW_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_LIME(Material.LIME_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_PINK(Material.PINK_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_GRAY(Material.GRAY_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_LIGHT_GRAY(Material.LIGHT_GRAY_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_CYAN(Material.CYAN_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_PURPLE(Material.PURPLE_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_BROWN(Material.BROWN_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_GREEN(Material.GREEN_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_RED(Material.RED_CONCRETE_POWDER, "", "", false, false),
    POLICE_BOX_BLACK(Material.BLACK_CONCRETE_POWDER, "", "", false, false);

    Material material;
    String firstLine;
    String secondLine;
    boolean door;
    boolean portal;
    private final static Map<Material, PRESET> BY_MATERIAL = Maps.newHashMap();

    PRESET(Material material, String firstLine, String secondLine, boolean door, boolean portal) {
        this.material = material;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
    }

    public Material getMaterial() {
        return material;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }

    public static PRESET getPreset(Material mat) {
        return BY_MATERIAL.get(mat);
    }

    public boolean hasDoor() {
        return door;
    }

    public boolean hasPortal() {
        return portal;
    }

    static {
        for (PRESET preset : values()) {
            BY_MATERIAL.put(preset.getMaterial(), preset);
        }
    }

    public boolean isColoured() {
        switch (this) {
            case POLICE_BOX_BLUE:
            case POLICE_BOX_WHITE:
            case POLICE_BOX_ORANGE:
            case POLICE_BOX_MAGENTA:
            case POLICE_BOX_LIGHT_BLUE:
            case POLICE_BOX_YELLOW:
            case POLICE_BOX_LIME:
            case POLICE_BOX_PINK:
            case POLICE_BOX_GRAY:
            case POLICE_BOX_LIGHT_GRAY:
            case POLICE_BOX_CYAN:
            case POLICE_BOX_PURPLE:
            case POLICE_BOX_BROWN:
            case POLICE_BOX_GREEN:
            case POLICE_BOX_RED:
            case POLICE_BOX_BLACK:
                return true;
            default:
                return false;
        }
    }
}
