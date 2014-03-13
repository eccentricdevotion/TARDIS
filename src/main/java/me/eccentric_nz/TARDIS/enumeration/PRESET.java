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

    ANGEL(Material.FEATHER, "WEEPING", "ANGELS HAVE", false),
    APPERTURE(Material.STICK, "APPERTURE", "SCIENCE", false),
    CAKE(Material.CAKE, "CAKE AND", "EAT IT TOO", true),
    CANDY(Material.SUGAR, "", "", false),
    CHALICE(Material.QUARTZ, "", "", false),
    CREEPY(Material.STRING, "HAUNTED", "HOUSE", true),
    DESERT(Material.SAND, "", "", true),
    DUCK(Material.WOOD_BUTTON, "", "", false),
    FACTORY(Material.IRON_INGOT, "", "", true),
    FENCE(Material.IRON_FENCE, "RANDOM", "FENCE", false),
    FLOWER(Material.RED_ROSE, "", "", false),
    GAZEBO(Material.FENCE, "CHILLED OUT", "GAZEBO", false),
    GRAVESTONE(Material.ENDER_STONE, "HERE", "LIES", false),
    HELIX(Material.SMOOTH_STAIRS, "INDUSTRIAL", "DOUBLE HELIX", false),
    JAIL(Material.SMOOTH_BRICK, "$50,000", "REWARD FOR", false),
    JUNGLE(Material.VINE, "", "", true),
    LAMP(Material.GLOWSTONE_DUST, "LONELY", "LAMP POST", false),
    LIBRARY(Material.BOOK, "LIBRARY OF", "TIME LORE", false),
    LIGHTHOUSE(Material.TORCH, "TINY", "LIGHTHOUSE", true),
    MINESHAFT(Material.RAILS, "ROAD TO", "EL DORADO", false),
    NETHER(Material.NETHERRACK, "", "", true),
    NEW(Material.STEP, "POLICE", "BOX", true),
    OLD(Material.WOOL, "POLICE", "BOX", true),
    PANDORICA(Material.COAL, "", "", false),
    PARTY(Material.FIREWORK, "PARTY", "TENT", true),
    PEANUT(Material.HARD_CLAY, "JAR OF", "PEANUT BUTTER", true),
    PINE(Material.LEAVES, "PINE", "TREE", false),
    PORTAL(Material.WOOD_DOOR, "PORTAL TO", "SOMEWHERE", true),
    PUNKED(Material.PISTON_BASE, "JUST GOT", "PUNKED", false),
    RENDER(Material.FIRE, "", "", false),
    ROBOT(Material.COBBLE_WALL, "WILL BE", "DELETED", false),
    SHROOM(Material.BROWN_MUSHROOM, "TRIPPY", "SPACE SHROOM", false),
    SNOWMAN(Material.SNOW_BALL, "TAKES ONE", "TO SNOW ONE", false),
    STONE(Material.COBBLESTONE, "STONE BRICK", "COLUMN", true),
    SUBMERGED(Material.DIRT, "", "", false),
    SWAMP(Material.WATER_LILY, "SIGN ABOVE", "THE DOOR", false),
    TELEPHONE(Material.GLASS, "TELEPHONE", "BOX", true),
    THEEND(Material.BEDROCK, "DRAGON", "SLAYING", true),
    TOILET(Material.LEVER, "", "", false),
    TOPSYTURVEY(Material.BUCKET, "Topsy-turvey", "BOX O' MARVEL", false),
    TORCH(Material.FLINT_AND_STEEL, "", "", false),
    VILLAGE(Material.COBBLESTONE, "VILLAGE", "HOUSE", true),
    WELL(Material.MOSSY_COBBLESTONE, "", "", false),
    WINDMILL(Material.WOOD, "VERY SMALL", "WINDMILL", true),
    YELLOW(Material.GOLD_NUGGET, "YELLOW", "SUBMARINE", true),
    CUSTOM(Material.OBSIDIAN, "", "", false);

    Material material;
    String firstLine;
    String secondLine;
    boolean door;
    private final static Map<Material, PRESET> BY_MATERIAL = Maps.newHashMap();

    private PRESET(Material material, String firstLine, String secondLine, boolean door) {
        this.material = material;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
        this.door = door;
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

    static {
        for (PRESET preset : values()) {
            BY_MATERIAL.put(preset.getMaterial(), preset);
        }
    }
}
