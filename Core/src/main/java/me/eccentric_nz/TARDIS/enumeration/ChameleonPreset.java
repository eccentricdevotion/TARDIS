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
package me.eccentric_nz.TARDIS.enumeration;

import com.google.common.collect.Maps;
import me.eccentric_nz.TARDIS.custommodeldata.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodeldata.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public enum ChameleonPreset {

    ADAPTIVE(Material.BLUE_WOOL, Material.BLUE_WOOL, 0, "ADAPTIVE", "BOX", true, true, "Adaptive Box"),
    ANDESITE(Material.GOLD_INGOT, Material.ANDESITE, 42, "ANDESITE", "BOX", true, true),
    ANGEL(Material.FEATHER, 8, "WEEPING", "ANGELS HAVE", false, false, "Weeping Angel"),
    APPERTURE(Material.STICK, Material.IRON_TRAPDOOR, 34, "APERTURE", "SCIENCE", false, false),
    CAKE(Material.CAKE, Material.BROWN_WOOL, 28, "CAKE AND", "EAT IT TOO", true, true, "Birthday Cake"),
    CANDY(Material.SUGAR, Material.RED_TERRACOTTA, 21, "", "", false, false, "Candy Cane"),
    CHALICE(Material.QUARTZ, Material.QUARTZ_BLOCK, 12, "", "", false, false, "Chalice"),
    CHORUS(Material.CHORUS_FLOWER, 41, "", "", true, true, "Chorus Flower"),
    CREEPY(Material.STRING, Material.COBWEB, 18, "HAUNTED", "HOUSE", true, true, "Creepy"),
    DESERT(Material.SAND, Material.SANDSTONE, 13, "DESERT", "TEMPLE", true, true),
    DIORITE(Material.EMERALD, Material.DIORITE, 43, "DIORITE", "BOX", true, true),
    DUCK(Material.OAK_BUTTON, Material.YELLOW_CONCRETE, 16, "", "", false, false, "Rubber Ducky"),
    FACTORY(Material.IRON_INGOT, -1, "", "", true, true, "Factory"),
    FENCE(Material.IRON_BARS, Material.BRICKS, 32, "RANDOM", "FENCE", false, false),
    FLOWER(Material.POPPY, Material.WHITE_WOOL, 10, "", "", false, false, "Daisy Flower"),
    GAZEBO(Material.OAK_FENCE, 33, "CHILLED OUT", "GAZEBO", false, false, "Gazebo"),
    GRANITE(Material.REDSTONE, Material.GRANITE, 44, "GRANITE", "BOX", true, true),
    GRAVESTONE(Material.END_STONE, 29, "HERE", "LIES", false, false, "Gravestone"),
    HELIX(Material.STONE_BRICK_STAIRS, 39, "INDUSTRIAL", "DOUBLE HELIX", false, false, "Double Helix"),
    INVISIBLE(Material.BARRIER, -1, "", "", true, true, "Invisible"),
    JAIL(Material.STONE_BRICKS, Material.IRON_BARS, 38, "$50,000", "REWARD FOR", false, false, "Jail"),
    JUNGLE(Material.VINE, Material.MOSSY_COBBLESTONE, 1, "JUNGLE", "TEMPLE", true, true),
    LAMP(Material.GLOWSTONE_DUST, Material.GLOWSTONE, 20, "LONELY", "LAMP POST", false, false, "Lamp Post"),
    LIBRARY(Material.BOOK, Material.BOOK, 36, "LIBRARY OF", "TIME LORE", false, false, "Library"),
    LIGHTHOUSE(Material.TORCH, Material.LANTERN, 35, "TINY", "LIGHTHOUSE", true, true),
    MINESHAFT(Material.RAIL, 17, "ROAD TO", "EL DORADO", false, false, "Mineshaft"),
    NETHER(Material.NETHERRACK, Material.NETHER_BRICKS, 2, "", "", true, true, "Nether Fortress"),
    PARTY(Material.FIREWORK_ROCKET, Material.GREEN_WOOL, 4, "PARTY", "TENT", true, true),
    PEANUT(Material.TERRACOTTA, 19, "JAR OF", "PEANUT BUTTER", true, false, "Peanut Butter Jar"),
    PINE(Material.SPRUCE_LEAVES, Material.SPRUCE_LEAVES, 25, "PINE", "TREE", false, false),
    PORTAL(Material.OAK_DOOR, Material.NETHER_QUARTZ_ORE, 27, "PORTAL TO", "SOMEWHERE", true, false, "Nether Portal"),
    PRISMARINE(Material.PRISMARINE, Material.PRISMARINE, 40, "GUARDIAN", "TEMPLE", true, true),
    PUNKED(Material.PISTON, Material.COAL_BLOCK, 26, "JUST GOT", "PUNKED", false, false, "Steam Punked"),
    RENDER(Material.FIRE, -1, "", "", false, false, "For Render Room Only"),
    ROBOT(Material.COBBLESTONE_WALL, Material.IRON_BLOCK, 23, "WILL BE", "DELETED", false, false, "Robot"),
    SHROOM(Material.BROWN_MUSHROOM, Material.BROWN_MUSHROOM_BLOCK, 31, "TRIPPY", "SPACE SHROOM", false, false, "Mushroom"),
    SNOWMAN(Material.SNOWBALL, Material.SNOW_BLOCK, 37, "TAKES ONE", "TO SNOW ONE", false, false, "Snowman"),
    STONE(Material.STONE, Material.STONE_BRICKS, 11, "STONE BRICK", "COLUMN", true, true),
    SUBMERGED(Material.DIRT, Material.DIRT, 9, "", "", false, false, "Submerged"),
    SWAMP(Material.LILY_PAD, Material.OAK_LOG, 3, "SIGN ABOVE", "THE DOOR", true, true, "Swamp"),
    TELEPHONE(Material.GLASS, Material.RED_WOOL, 7, "TELEPHONE", "BOX", true, true),
    THEEND(Material.BEDROCK, "DRAGON", "SLAYING", true, true),
    TOILET(Material.LEVER, Material.HOPPER, 22, "", "", false, false, "Water Closet"),
    TOPSYTURVEY(Material.BUCKET, Material.PINK_WOOL, 30, "Topsy-turvey", "BOX O' MARVEL", false, false, "Topsy-turvey"),
    TORCH(Material.FLINT_AND_STEEL, Material.NETHERRACK, 24, "", "", false, false, "Flaming Torch"),
    VILLAGE(Material.COBBLESTONE, Material.COBBLESTONE, 5, "VILLAGE", "HOUSE", true, true),
    WELL(Material.MOSSY_COBBLESTONE, Material.MOSSY_STONE_BRICKS, 14, "", "", false, false, "Mossy Well"),
    WINDMILL(Material.OAK_PLANKS, Material.ORANGE_WOOL, 15, "VERY SMALL", "WINDMILL", true, false, "Windmill"),
    YELLOW(Material.GOLD_NUGGET, Material.YELLOW_WOOL, 6, "YELLOW", "SUBMARINE", true, true),
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
    CAVE(Material.BARRIER, "CAVE", "", true, true),
    // coloured police boxes
    POLICE_BOX_BLUE(Material.BLUE_CONCRETE_POWDER, 0, "", "", false, true, "Blue Police Box"),
    POLICE_BOX_WHITE(Material.WHITE_CONCRETE_POWDER, 1, "", "", false, true, "White Police Box"),
    POLICE_BOX_ORANGE(Material.ORANGE_CONCRETE_POWDER, 2, "", "", false, true, "Orange Police Box"),
    POLICE_BOX_MAGENTA(Material.MAGENTA_CONCRETE_POWDER, 3, "", "", false, true, "Magenta Police Box"),
    POLICE_BOX_LIGHT_BLUE(Material.LIGHT_BLUE_CONCRETE_POWDER, 4, "", "", false, true, "Light Blue Police Box"),
    POLICE_BOX_YELLOW(Material.YELLOW_CONCRETE_POWDER, 5, "", "", false, true, "Yellow Police Box"),
    POLICE_BOX_LIME(Material.LIME_CONCRETE_POWDER, 6, "", "", false, true, "Lime Police Box"),
    POLICE_BOX_PINK(Material.PINK_CONCRETE_POWDER, 7, "", "", false, true, "Pink Police Box"),
    POLICE_BOX_GRAY(Material.GRAY_CONCRETE_POWDER, 8, "", "", false, true, "Gray Police Box"),
    POLICE_BOX_LIGHT_GRAY(Material.LIGHT_GRAY_CONCRETE_POWDER, 9, "", "", false, true, "Light Gray Police Box"),
    POLICE_BOX_CYAN(Material.CYAN_CONCRETE_POWDER, 10, "", "", false, true, "Cyan Police Box"),
    POLICE_BOX_PURPLE(Material.PURPLE_CONCRETE_POWDER, 11, "", "", false, true, "Purple Police Box"),
    POLICE_BOX_BROWN(Material.BROWN_CONCRETE_POWDER, 12, "", "", false, true, "Brown Police Box"),
    POLICE_BOX_GREEN(Material.GREEN_CONCRETE_POWDER, 13, "", "", false, true, "Green Police Box"),
    POLICE_BOX_RED(Material.RED_CONCRETE_POWDER, 14, "", "", false, true, "Red Police Box"),
    POLICE_BOX_BLACK(Material.BLACK_CONCRETE_POWDER, 15, "", "", false, true, "Black Police Box"),
    POLICE_BOX_TENNANT(Material.CYAN_CONCRETE, 16, "", "", false, true, "Tennant Era Police Box"),
    WEEPING_ANGEL(Material.GRAY_CONCRETE, 17, "", "", false, false, "Weeping Angel"),
    PANDORICA(Material.LEATHER_HORSE_ARMOR, 18, "", "", false, true, "Pandorica"),
    COLOURED(Material.LEATHER_HORSE_ARMOR, 19, "", "", false, true, "Pick a colour"),

    ITEM(Material.BARRIER, -1, "", "", false, true, "Item");

    public final static List<Material> NOT_THESE = Arrays.asList(Material.BARRIER, Material.BEDROCK, Material.IRON_INGOT, Material.FIRE);
    private final static Map<Material, ChameleonPreset> BY_MATERIAL = Maps.newHashMap();
    private final static Map<Integer, ChameleonPreset> BY_SLOT = Maps.newHashMap();
    private final static Map<Integer, ChameleonPreset> IF_BY_SLOT = Maps.newHashMap();

    static {
        for (ChameleonPreset preset : values()) {
            if (!NOT_THESE.contains(preset.getCraftMaterial())) {
                BY_MATERIAL.put(preset.getCraftMaterial(), preset);
                if (preset.usesArmourStand()) {
                    IF_BY_SLOT.put(preset.getSlot(), preset);
                } else {
                    BY_SLOT.put(preset.getSlot(), preset);
                }
            }
        }
    }

    final Material craftMaterial;
    final Material guiDisplay;
    final int slot;
    final String firstLine;
    final String secondLine;
    final boolean door;
    final boolean portal;
    final String displayName;

    ChameleonPreset(Material craftMaterial, String firstLine, String secondLine, boolean door, boolean portal) {
        this.craftMaterial = craftMaterial;
        guiDisplay = craftMaterial;
        slot = -1;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        displayName = ((this.firstLine.contains("_")) ? TARDISStringUtils.capitalise(this.firstLine) : TARDISStringUtils.titleCase(this.firstLine)) + (!this.secondLine.isEmpty() ? " " + TARDISStringUtils.titleCase(secondLine) : "");
    }

    ChameleonPreset(Material craftMaterial, int slot, String firstLine, String secondLine, boolean door, boolean portal, String displayName) {
        this.craftMaterial = craftMaterial;
        guiDisplay = craftMaterial;
        this.slot = slot;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        this.displayName = displayName;
    }

    ChameleonPreset(Material craftMaterial, Material guiDisplay, int slot, String firstLine, String secondLine, boolean door, boolean portal, String displayName) {
        this.craftMaterial = craftMaterial;
        this.guiDisplay = guiDisplay;
        this.slot = slot;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        this.displayName = displayName;
    }

    ChameleonPreset(Material craftMaterial, Material guiDisplay, int slot, String firstLine, String secondLine, boolean door, boolean portal) {
        this.craftMaterial = craftMaterial;
        this.guiDisplay = guiDisplay;
        this.slot = slot;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
        this.portal = portal;
        displayName = ((this.firstLine.contains("_")) ? TARDISStringUtils.capitalise(this.firstLine) : TARDISStringUtils.titleCase(this.firstLine)) + (!this.secondLine.isEmpty() ? " " + TARDISStringUtils.titleCase(secondLine) : "");
    }

    public static ChameleonPreset getPreset(Material mat) {
        return BY_MATERIAL.get(mat);
    }

    public static ChameleonPreset getItemFramePresetBySlot(int slot) {
        return IF_BY_SLOT.get(slot);
    }

    public static ChameleonPreset getPresetBySlot(int slot) {
        return BY_SLOT.get(slot);
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

    public boolean hasDoor() {
        return door;
    }

    public boolean hasPortal() {
        return portal;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean usesArmourStand() {
        return switch (this) {
            case POLICE_BOX_BLUE, POLICE_BOX_WHITE, POLICE_BOX_ORANGE, POLICE_BOX_MAGENTA, POLICE_BOX_LIGHT_BLUE,
                POLICE_BOX_YELLOW, POLICE_BOX_LIME, POLICE_BOX_PINK, POLICE_BOX_GRAY, POLICE_BOX_LIGHT_GRAY,
                POLICE_BOX_CYAN, POLICE_BOX_PURPLE, POLICE_BOX_BROWN, POLICE_BOX_GREEN, POLICE_BOX_RED,
                POLICE_BOX_BLACK, POLICE_BOX_TENNANT, WEEPING_ANGEL, COLOURED, PANDORICA, ITEM -> true;
            default -> false;
        };
    }

    public boolean isBlockPreset() {
        return switch (this) {
            case ADAPTIVE, ANDESITE, ANGEL, APPERTURE, CAKE, CANDY, CHALICE, CHORUS, CREEPY, DESERT, DIORITE,
                DUCK, FACTORY, FENCE, FLOWER, GAZEBO, GRANITE, GRAVESTONE, HELIX, JAIL, JUNGLE, LAMP, LIBRARY,
                LIGHTHOUSE, MINESHAFT, NETHER, PARTY, PEANUT, PINE, PORTAL, PRISMARINE, PUNKED,
                ROBOT, SHROOM, SNOWMAN, STONE, SUBMERGED, SWAMP, TELEPHONE, TOILET, TOPSYTURVEY, TORCH,
                VILLAGE, WELL, WINDMILL, YELLOW, CUSTOM -> true;
            default -> false;
        };
    }

    public NamespacedKey getClosed() {
        switch (this) {
            case POLICE_BOX_BLUE -> { return ChameleonVariant.BLUE.getKey(); }
            case POLICE_BOX_WHITE -> { return ChameleonVariant.WHITE.getKey(); }
            case POLICE_BOX_ORANGE -> { return ChameleonVariant.ORANGE.getKey(); }
            case POLICE_BOX_MAGENTA -> { return ChameleonVariant.MAGENTA.getKey(); }
            case POLICE_BOX_LIGHT_BLUE -> { return ChameleonVariant.LIGHT_BLUE.getKey(); }
            case POLICE_BOX_YELLOW -> { return ChameleonVariant.YELLOW.getKey(); }
            case POLICE_BOX_LIME -> { return ChameleonVariant.LIME.getKey(); }
            case POLICE_BOX_PINK -> { return ChameleonVariant.PINK.getKey(); }
            case POLICE_BOX_GRAY -> { return ChameleonVariant.GRAY.getKey(); }
            case POLICE_BOX_LIGHT_GRAY -> { return ChameleonVariant.LIGHT_GRAY.getKey(); }
            case POLICE_BOX_CYAN -> { return ChameleonVariant.CYAN.getKey(); }
            case POLICE_BOX_PURPLE -> { return ChameleonVariant.PURPLE.getKey(); }
            case POLICE_BOX_BROWN -> { return ChameleonVariant.BROWN.getKey(); }
            case POLICE_BOX_GREEN -> { return ChameleonVariant.GREEN.getKey(); }
            case POLICE_BOX_RED -> { return ChameleonVariant.RED.getKey(); }
            case POLICE_BOX_BLACK -> { return ChameleonVariant.BLACK.getKey(); }
            case POLICE_BOX_TENNANT -> { return ChameleonVariant.TENNANT.getKey(); }
            case WEEPING_ANGEL -> { return ChameleonVariant.WEEPING_ANGEL.getKey(); }
            case COLOURED -> { return ColouredVariant.TARDIS_TINTED.getKey(); }
            case PANDORICA -> { return ChameleonVariant.PANDORICA.getKey(); }
        }
        return null;
    }

    public NamespacedKey getOpen() {
        switch (this) {
            case POLICE_BOX_BLUE -> { return ChameleonVariant.BLUE_OPEN.getKey(); }
            case POLICE_BOX_WHITE -> { return ChameleonVariant.WHITE_OPEN.getKey(); }
            case POLICE_BOX_ORANGE -> { return ChameleonVariant.ORANGE_OPEN.getKey(); }
            case POLICE_BOX_MAGENTA -> { return ChameleonVariant.MAGENTA_OPEN.getKey(); }
            case POLICE_BOX_LIGHT_BLUE -> { return ChameleonVariant.LIGHT_BLUE_OPEN.getKey(); }
            case POLICE_BOX_YELLOW -> { return ChameleonVariant.YELLOW_OPEN.getKey(); }
            case POLICE_BOX_LIME -> { return ChameleonVariant.LIME_OPEN.getKey(); }
            case POLICE_BOX_PINK -> { return ChameleonVariant.PINK_OPEN.getKey(); }
            case POLICE_BOX_GRAY -> { return ChameleonVariant.GRAY_OPEN.getKey(); }
            case POLICE_BOX_LIGHT_GRAY -> { return ChameleonVariant.LIGHT_GRAY_OPEN.getKey(); }
            case POLICE_BOX_CYAN -> { return ChameleonVariant.CYAN_OPEN.getKey(); }
            case POLICE_BOX_PURPLE -> { return ChameleonVariant.PURPLE_OPEN.getKey(); }
            case POLICE_BOX_BROWN -> { return ChameleonVariant.BROWN_OPEN.getKey(); }
            case POLICE_BOX_GREEN -> { return ChameleonVariant.GREEN_OPEN.getKey(); }
            case POLICE_BOX_RED -> { return ChameleonVariant.RED_OPEN.getKey(); }
            case POLICE_BOX_BLACK -> { return ChameleonVariant.BLACK_OPEN.getKey(); }
            case POLICE_BOX_TENNANT -> { return ChameleonVariant.TENNANT_OPEN.getKey(); }
            case WEEPING_ANGEL -> { return ChameleonVariant.WEEPING_ANGEL_OPEN.getKey(); }
            case COLOURED -> { return ColouredVariant.TARDIS_TINTED_OPEN.getKey(); }
            case PANDORICA -> { return ChameleonVariant.PANDORICA_OPEN.getKey(); }
        }
        return null;
    }

    public NamespacedKey getStained() {
        switch (this) {
            case POLICE_BOX_BLUE -> { return ChameleonVariant.BLUE_STAINED.getKey(); }
            case POLICE_BOX_WHITE -> { return ChameleonVariant.WHITE_STAINED.getKey(); }
            case POLICE_BOX_ORANGE -> { return ChameleonVariant.ORANGE_STAINED.getKey(); }
            case POLICE_BOX_MAGENTA -> { return ChameleonVariant.MAGENTA_STAINED.getKey(); }
            case POLICE_BOX_LIGHT_BLUE -> { return ChameleonVariant.LIGHT_BLUE_STAINED.getKey(); }
            case POLICE_BOX_YELLOW -> { return ChameleonVariant.YELLOW_STAINED.getKey(); }
            case POLICE_BOX_LIME -> { return ChameleonVariant.LIME_STAINED.getKey(); }
            case POLICE_BOX_PINK -> { return ChameleonVariant.PINK_STAINED.getKey(); }
            case POLICE_BOX_GRAY -> { return ChameleonVariant.GRAY_STAINED.getKey(); }
            case POLICE_BOX_LIGHT_GRAY -> { return ChameleonVariant.LIGHT_GRAY_STAINED.getKey(); }
            case POLICE_BOX_CYAN -> { return ChameleonVariant.CYAN_STAINED.getKey(); }
            case POLICE_BOX_PURPLE -> { return ChameleonVariant.PURPLE_STAINED.getKey(); }
            case POLICE_BOX_BROWN -> { return ChameleonVariant.BROWN_STAINED.getKey(); }
            case POLICE_BOX_GREEN -> { return ChameleonVariant.GREEN_STAINED.getKey(); }
            case POLICE_BOX_RED -> { return ChameleonVariant.RED_STAINED.getKey(); }
            case POLICE_BOX_BLACK -> { return ChameleonVariant.BLACK_STAINED.getKey(); }
            case POLICE_BOX_TENNANT -> { return ChameleonVariant.TENNANT_STAINED.getKey(); }
            case WEEPING_ANGEL -> { return ChameleonVariant.WEEPING_ANGEL_STAINED.getKey(); }
            case COLOURED -> { return ColouredVariant.TARDIS_STAINED_TINTED.getKey(); }
            case PANDORICA -> { return ChameleonVariant.PANDORICA_STAINED.getKey(); }
        }
        return null;
    }

    public NamespacedKey getGlass() {
        switch (this) {
            case POLICE_BOX_BLUE, POLICE_BOX_WHITE, POLICE_BOX_ORANGE, POLICE_BOX_MAGENTA, POLICE_BOX_LIGHT_BLUE,
                 POLICE_BOX_YELLOW, POLICE_BOX_LIME, POLICE_BOX_PINK, POLICE_BOX_GRAY, POLICE_BOX_LIGHT_GRAY,
                 POLICE_BOX_CYAN, POLICE_BOX_PURPLE, POLICE_BOX_BROWN, POLICE_BOX_GREEN, POLICE_BOX_RED, POLICE_BOX_BLACK,
                 POLICE_BOX_TENNANT, COLOURED -> { return ChameleonVariant.GLASS.getKey(); }
            case WEEPING_ANGEL -> { return ChameleonVariant.WEEPING_ANGEL_GLASS.getKey(); }
            case PANDORICA -> { return ChameleonVariant.PANDORICA_GLASS.getKey(); }
        }
        return null;
    }
}
