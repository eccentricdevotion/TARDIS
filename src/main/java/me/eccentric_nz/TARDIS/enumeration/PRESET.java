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
package me.eccentric_nz.TARDIS.enumeration;

import com.google.common.collect.Maps;
import java.util.Map;
import org.bukkit.Material;

/**
 *
 * @author eccentric_nz
 */
public enum PRESET {

    ANDESITE(Material.GOLD_INGOT, "ANDESITE", "BOX", true, true),
    ANGEL(Material.FEATHER, "WEEPING", "ANGELS HAVE", false, false),
    APPERTURE(Material.STICK, "APPERTURE", "SCIENCE", false, false),
    CAKE(Material.CAKE, "CAKE AND", "EAT IT TOO", true, true),
    CANDY(Material.SUGAR, "", "", false, false),
    CHALICE(Material.QUARTZ, "", "", false, false),
    CREEPY(Material.STRING, "HAUNTED", "HOUSE", true, true),
    DESERT(Material.SAND, "", "", true, true),
    DIORITE(Material.EMERALD, "DIORITE", "BOX", true, true),
    DUCK(Material.WOOD_BUTTON, "", "", false, false),
    FACTORY(Material.IRON_INGOT, "", "", true, true),
    FENCE(Material.IRON_FENCE, "RANDOM", "FENCE", false, false),
    FLOWER(Material.RED_ROSE, "", "", false, false),
    GAZEBO(Material.FENCE, "CHILLED OUT", "GAZEBO", false, false),
    GRANITE(Material.REDSTONE, "GRANITE", "BOX", true, true),
    GRAVESTONE(Material.ENDER_STONE, "HERE", "LIES", false, false),
    HELIX(Material.SMOOTH_STAIRS, "INDUSTRIAL", "DOUBLE HELIX", false, false),
    INVISIBLE(Material.BARRIER, "", "", true, true),
    JAIL(Material.SMOOTH_BRICK, "$50,000", "REWARD FOR", false, false),
    JUNGLE(Material.VINE, "", "", true, true),
    LAMP(Material.GLOWSTONE_DUST, "LONELY", "LAMP POST", false, false),
    LIBRARY(Material.BOOK, "LIBRARY OF", "TIME LORE", false, false),
    LIGHTHOUSE(Material.TORCH, "TINY", "LIGHTHOUSE", true, true),
    MINESHAFT(Material.RAILS, "ROAD TO", "EL DORADO", false, false),
    NETHER(Material.NETHERRACK, "", "", true, true),
    NEW(Material.STEP, "POLICE", "BOX", true, true),
    OLD(Material.WOOL, "POLICE", "BOX", true, true),
    PANDORICA(Material.COAL, "PANDORICA", "", false, false),
    PARTY(Material.FIREWORK, "PARTY", "TENT", true, true),
    PEANUT(Material.HARD_CLAY, "JAR OF", "PEANUT BUTTER", true, false),
    PINE(Material.LEAVES, "PINE", "TREE", false, false),
    PORTAL(Material.WOOD_DOOR, "PORTAL TO", "SOMEWHERE", true, false),
    PRISMARINE(Material.PRISMARINE, "GUARDIAN", "TEMPLE", true, true),
    PUNKED(Material.PISTON_BASE, "JUST GOT", "PUNKED", false, false),
    RENDER(Material.FIRE, "", "", false, false),
    ROBOT(Material.COBBLE_WALL, "WILL BE", "DELETED", false, false),
    SHROOM(Material.BROWN_MUSHROOM, "TRIPPY", "SPACE SHROOM", false, false),
    SNOWMAN(Material.SNOW_BALL, "TAKES ONE", "TO SNOW ONE", false, false),
    STONE(Material.STONE, "STONE BRICK", "COLUMN", true, true),
    SUBMERGED(Material.DIRT, "", "", false, false),
    SWAMP(Material.WATER_LILY, "SIGN ABOVE", "THE DOOR", true, true),
    TELEPHONE(Material.GLASS, "TELEPHONE", "BOX", true, true),
    THEEND(Material.BEDROCK, "DRAGON", "SLAYING", true, true),
    TOILET(Material.LEVER, "", "", false, false),
    TOPSYTURVEY(Material.BUCKET, "Topsy-turvey", "BOX O' MARVEL", false, false),
    TORCH(Material.FLINT_AND_STEEL, "", "", false, false),
    VILLAGE(Material.COBBLESTONE, "VILLAGE", "HOUSE", true, true),
    WELL(Material.MOSSY_COBBLESTONE, "", "", false, false),
    WINDMILL(Material.WOOD, "VERY SMALL", "WINDMILL", true, false),
    YELLOW(Material.GOLD_NUGGET, "YELLOW", "SUBMARINE", true, true),
    CUSTOM(Material.OBSIDIAN, "", "", false, false),
    JUNK(Material.BARRIER, "", "", false, false),
    CONSTRUCT(Material.BARRIER, "CONSTRUCT", "", true, true);

    Material material;
    String firstLine;
    String secondLine;
    boolean door;
    boolean portal;
    private final static Map<Material, PRESET> BY_MATERIAL = Maps.newHashMap();

    private PRESET(Material material, String firstLine, String secondLine, boolean door, boolean portal) {
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

    public static PRESET getPreset(final Material mat) {
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
}
