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
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;

import java.util.Map;

/**
 * @author eccentric_nz
 */
public enum PRESET {

    ANDESITE(Material.GOLD_INGOT, Material.ANDESITE, 44, "ANDESITE", "BOX", true, true),
    ANGEL(Material.FEATHER, 9, "WEEPING", "ANGELS HAVE", false, false, "Weeping Angel"),
    APPERTURE(Material.STICK, Material.IRON_TRAPDOOR, 35, "APERTURE", "SCIENCE", false, false),
    CAKE(Material.CAKE, Material.BROWN_WOOL, 29, "CAKE AND", "EAT IT TOO", true, true, "Birthday Cake"),
    CANDY(Material.SUGAR, Material.RED_TERRACOTTA, 22, "", "", false, false, "Candy Cane"),
    CHALICE(Material.QUARTZ, Material.QUARTZ_BLOCK, 13, "", "", false, false, "Chalice"),
    CHORUS(Material.CHORUS_FLOWER, 43, "", "", true, true, "Chorus Flower"),
    CREEPY(Material.STRING, Material.COBWEB, 19, "HAUNTED", "HOUSE", true, true, "Creepy"),
    DESERT(Material.SAND, Material.SANDSTONE, 14, "DESERT", "TEMPLE", true, true),
    DIORITE(Material.EMERALD, Material.DIORITE, 45, "DIORITE", "BOX", true, true),
    DUCK(Material.OAK_BUTTON, Material.YELLOW_CONCRETE, 17, "", "", false, false, "Rubber Ducky"),
    FACTORY(Material.IRON_INGOT, -1, "", "", true, true, "Factory"),
    FENCE(Material.IRON_BARS, Material.BRICKS, 33, "RANDOM", "FENCE", false, false),
    FLOWER(Material.POPPY, Material.WHITE_WOOL, 11, "", "", false, false, "Daisy Flower"),
    GAZEBO(Material.OAK_FENCE, 34, "CHILLED OUT", "GAZEBO", false, false, "Gazebo"),
    GRANITE(Material.REDSTONE, Material.GRANITE, 46, "GRANITE", "BOX", true, true),
    GRAVESTONE(Material.END_STONE, 30, "HERE", "LIES", false, false, "Gravestone"),
    HELIX(Material.STONE_BRICK_STAIRS, 41, "INDUSTRIAL", "DOUBLE HELIX", false, false, "Double Helix"),
    INVISIBLE(Material.BARRIER, -1, "", "", true, true, "Invisible"),
    JAIL(Material.STONE_BRICKS, Material.IRON_BARS, 39, "$50,000", "REWARD FOR", false, false, "Jail"),
    JUNGLE(Material.VINE, Material.MOSSY_COBBLESTONE, 1, "JUNGLE", "TEMPLE", true, true),
    LAMP(Material.GLOWSTONE_DUST, Material.GLOWSTONE, 21, "LONELY", "LAMP POST", false, false, "Lamp Post"),
    LIBRARY(Material.BOOK, Material.BOOK, 37, "LIBRARY OF", "TIME LORE", false, false, "Library"),
    LIGHTHOUSE(Material.TORCH, Material.LANTERN, 36, "TINY", "LIGHTHOUSE", true, true),
    MINESHAFT(Material.RAIL, 18, "ROAD TO", "EL DORADO", false, false, "Mineshaft"),
    NETHER(Material.NETHERRACK, Material.NETHER_BRICKS, 2, "", "", true, true, "Nether Fortress"),
    NEW(Material.STONE_SLAB, Material.BLUE_WOOL, 0, "POLICE", "BOX", true, true, "New Police Box"),
    OLD(Material.BLUE_WOOL, Material.LIGHT_BLUE_WOOL, 3, "POLICE", "BOX", true, true, "Old Police Box"),
    PANDORICA(Material.COAL, Material.BEDROCK, 40, "PANDORICA", "", false, false),
    PARTY(Material.FIREWORK_ROCKET, Material.GREEN_WOOL, 5, "PARTY", "TENT", true, true),
    PEANUT(Material.TERRACOTTA, 20, "JAR OF", "PEANUT BUTTER", true, false, "Peanut Butter Jar"),
    PINE(Material.SPRUCE_LEAVES, Material.SPRUCE_LEAVES, 26, "PINE", "TREE", false, false),
    PORTAL(Material.OAK_DOOR, Material.NETHER_QUARTZ_ORE, 28, "PORTAL TO", "SOMEWHERE", true, false, "Nether Portal"),
    PRISMARINE(Material.PRISMARINE, Material.PRISMARINE, 42, "GUARDIAN", "TEMPLE", true, true),
    PUNKED(Material.PISTON, Material.COAL_BLOCK, 27, "JUST GOT", "PUNKED", false, false, "Steam Punked"),
    RENDER(Material.FIRE, -1, "", "", false, false, "For Render Room Only"),
    ROBOT(Material.COBBLESTONE_WALL, Material.IRON_BLOCK, 24, "WILL BE", "DELETED", false, false, "Robot"),
    SHROOM(Material.BROWN_MUSHROOM, Material.BROWN_MUSHROOM_BLOCK, 32, "TRIPPY", "SPACE SHROOM", false, false, "Mushroom"),
    SNOWMAN(Material.SNOWBALL, Material.SNOW_BLOCK, 38, "TAKES ONE", "TO SNOW ONE", false, false, "Snowman"),
    STONE(Material.STONE, Material.STONE_BRICKS, 12, "STONE BRICK", "COLUMN", true, true),
    SUBMERGED(Material.DIRT, Material.DIRT, 10, "", "", false, false, "Submerged"),
    SWAMP(Material.LILY_PAD, Material.OAK_LOG, 4, "SIGN ABOVE", "THE DOOR", true, true, "Swamp"),
    TELEPHONE(Material.GLASS, Material.RED_WOOL, 8, "TELEPHONE", "BOX", true, true),
    THEEND(Material.BEDROCK, "DRAGON", "SLAYING", true, true),
    TOILET(Material.LEVER, Material.HOPPER, 23, "", "", false, false, "Water Closet"),
    TOPSYTURVEY(Material.BUCKET, Material.PINK_WOOL, 31, "Topsy-turvey", "BOX O' MARVEL", false, false, "Topsy-turvey"),
    TORCH(Material.FLINT_AND_STEEL, Material.NETHERRACK, 25, "", "", false, false, "Flaming Torch"),
    VILLAGE(Material.COBBLESTONE, Material.COBBLESTONE, 6, "VILLAGE", "HOUSE", true, true),
    WELL(Material.MOSSY_COBBLESTONE, Material.MOSSY_STONE_BRICKS, 15, "", "", false, false, "Mossy Well"),
    WINDMILL(Material.OAK_PLANKS, Material.ORANGE_WOOL, 16, "VERY SMALL", "WINDMILL", true, false, "Windmill"),
    YELLOW(Material.GOLD_NUGGET, Material.YELLOW_WOOL, 7, "YELLOW", "SUBMARINE", true, true),
    CUSTOM(Material.OBSIDIAN, Material.ENDER_CHEST, 48, "", "", false, false, "Custom"),
    JUNK(Material.BARRIER, -1, "", "", false, false, "Junk"),
    JUNK_MODE(Material.BARRIER, -1, "", "", false, false, "Junk Mode"),
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
    POLICE_BOX_BLUE(Material.BLUE_CONCRETE_POWDER, 0, "", "", false, true, "Blue Police Box"),
    POLICE_BOX_BLUE_OPEN(Material.BARRIER, -1, "", "", false, true, "Open Blue Police Box"),
    POLICE_BOX_WHITE(Material.WHITE_CONCRETE_POWDER, 1, "", "", false, false, "White Police Box"),
    POLICE_BOX_ORANGE(Material.ORANGE_CONCRETE_POWDER, 2, "", "", false, false, "Orange Police Box"),
    POLICE_BOX_MAGENTA(Material.MAGENTA_CONCRETE_POWDER, 3, "", "", false, false, "Magenta Police Box"),
    POLICE_BOX_LIGHT_BLUE(Material.LIGHT_BLUE_CONCRETE_POWDER, 4, "", "", false, false, "Light Blue Police Box"),
    POLICE_BOX_YELLOW(Material.YELLOW_CONCRETE_POWDER, 5, "", "", false, false, "Yellow Police Box"),
    POLICE_BOX_LIME(Material.LIME_CONCRETE_POWDER, 6, "", "", false, false, "Lime Police Box"),
    POLICE_BOX_PINK(Material.PINK_CONCRETE_POWDER, 7, "", "", false, false, "Pink Police Box"),
    POLICE_BOX_GRAY(Material.GRAY_CONCRETE_POWDER, 8, "", "", false, false, "Gray Police Box"),
    POLICE_BOX_LIGHT_GRAY(Material.LIGHT_GRAY_CONCRETE_POWDER, 9, "", "", false, false, "Light Gray Police Box"),
    POLICE_BOX_CYAN(Material.CYAN_CONCRETE_POWDER, 10, "", "", false, false, "Cyan Police Box"),
    POLICE_BOX_PURPLE(Material.PURPLE_CONCRETE_POWDER, 11, "", "", false, false, "Purple Police Box"),
    POLICE_BOX_BROWN(Material.BROWN_CONCRETE_POWDER, 12, "", "", false, false, "Brown Police Box"),
    POLICE_BOX_GREEN(Material.GREEN_CONCRETE_POWDER, 13, "", "", false, false, "Green Police Box"),
    POLICE_BOX_RED(Material.RED_CONCRETE_POWDER, 14, "", "", false, false, "Red Police Box"),
    POLICE_BOX_BLACK(Material.BLACK_CONCRETE_POWDER, 15, "", "", false, false, "Black Police Box");

    Material craftMaterial;
    Material guiDisplay;
    int slot;
    String firstLine;
    String secondLine;
    boolean door;
    boolean portal;
    private final static Map<Material, PRESET> BY_MATERIAL = Maps.newHashMap();
    String displayName;

    PRESET(Material craftMaterial, String firstLine, String secondLine, boolean door, boolean portal) {
        this.craftMaterial = craftMaterial;
        guiDisplay = craftMaterial;
        slot = -1;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        displayName = ((this.firstLine.contains("_")) ? TARDISStringUtils.capitalise(this.firstLine) : TARDISStringUtils.titleCase(this.firstLine)) + (!this.secondLine.isEmpty() ? " " + TARDISStringUtils.titleCase(secondLine) : "");
    }

    PRESET(Material craftMaterial, int slot, String firstLine, String secondLine, boolean door, boolean portal, String displayName) {
        this.craftMaterial = craftMaterial;
        guiDisplay = craftMaterial;
        this.slot = slot;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        this.displayName = displayName;
    }

    PRESET(Material craftMaterial, Material guiDisplay, int slot, String firstLine, String secondLine, boolean door, boolean portal, String displayName) {
        this.craftMaterial = craftMaterial;
        this.guiDisplay = guiDisplay;
        this.slot = slot;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        this.displayName = displayName;
    }

    PRESET(Material craftMaterial, Material guiDisplay, int slot, String firstLine, String secondLine, boolean door, boolean portal) {
        this.craftMaterial = craftMaterial;
        this.guiDisplay = guiDisplay;
        this.slot = slot;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        displayName = ((this.firstLine.contains("_")) ? TARDISStringUtils.capitalise(this.firstLine) : TARDISStringUtils.titleCase(this.firstLine)) + (!this.secondLine.isEmpty() ? " " + TARDISStringUtils.titleCase(secondLine) : "");
    }

    public Material getCraftMaterial() {
        return craftMaterial;
    }

    public Material getGuiDisplay() {
        return guiDisplay;
    }

    public int getSlot() {
        return slot;
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

    public String getDisplayName() {
        return displayName;
    }

    static {
        for (PRESET preset : values()) {
            BY_MATERIAL.put(preset.getCraftMaterial(), preset);
        }
    }

    public boolean isColoured() {
        switch (this) {
            case POLICE_BOX_BLUE:
            case POLICE_BOX_BLUE_OPEN:
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
