/*
 * Copyright (C) 2013 eccentric_nz
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

    ANGEL(Material.FEATHER, "WEEPING", "ANGELS HAVE"),
    APPERTURE(Material.STICK, "APPERTURE", "SCIENCE"),
    CAKE(Material.CAKE, "CAKE AND", "EAT IT TOO"),
    CANDY(Material.SUGAR, "", ""),
    CHALICE(Material.QUARTZ, "", ""),
    CREEPY(Material.STRING, "HAUNTED", "HOUSE"),
    DESERT(Material.SAND, "", ""),
    DUCK(Material.WOOD_BUTTON, "", ""),
    FACTORY(Material.IRON_INGOT, "", ""),
    FENCE(Material.IRON_FENCE, "RANDOM", "FENCE"),
    FLOWER(Material.RED_ROSE, "", ""),
    GAZEBO(Material.FENCE, "CHILLED OUT", "GAZEBO"),
    GRAVESTONE(Material.ENDER_STONE, "HERE", "LIES"),
    HELIX(Material.SMOOTH_STAIRS, "INDUSTRIAL", "DOUBLE HELIX"),
    JAIL(Material.SMOOTH_BRICK, "$50,000", "REWARD FOR"),
    JUNGLE(Material.VINE, "", ""),
    LAMP(Material.GLOWSTONE_DUST, "LONELY", "LAMP POST"),
    LIBRARY(Material.BOOK, "LIBRARY OF", "TIME LORE"),
    LIGHTHOUSE(Material.TORCH, "TINY", "LIGHTHOUSE"),
    MINESHAFT(Material.RAILS, "ROAD TO", "EL DORADO"),
    NETHER(Material.NETHERRACK, "", ""),
    NEW(Material.STEP, "POLICE", "BOX"),
    OLD(Material.WOOL, "POLICE", "BOX"),
    PANDORICA(Material.COAL, "", ""),
    PARTY(Material.FIREWORK, "PARTY", "TENT"),
    PEANUT(Material.HARD_CLAY, "JAR OF", "PEANUT BUTTER"),
    PINE(Material.LEAVES, "PINE", "TREE"),
    PORTAL(Material.WOOD_DOOR, "PORTAL TO", "SOMEWHERE"),
    PUNKED(Material.PISTON_BASE, "JUST GOT", "PUNKED"),
    RENDER(Material.FIRE, "", ""),
    ROBOT(Material.COBBLE_WALL, "WILL BE", "DELETED"),
    SHROOM(Material.BROWN_MUSHROOM, "TRIPPY", "SPACE SHROOM"),
    SNOWMAN(Material.SNOW_BALL, "TAKES ONE", "TO SNOW ONE"),
    STONE(Material.COBBLESTONE, "STONE BRICK", "COLUMN"),
    SUBMERGED(Material.DIRT, "", ""),
    SWAMP(Material.WATER_LILY, "SIGN ABOVE", "THE DOOR"),
    TELEPHONE(Material.GLASS, "TELEPHONE", "BOX"),
    THEEND(Material.BEDROCK, "DRAGON", "SLAYING"),
    TOILET(Material.LEVER, "", ""),
    TOPSYTURVEY(Material.BUCKET, "Topsy-turvey", "BOX O' MARVEL"),
    TORCH(Material.FLINT_AND_STEEL, "", ""),
    VILLAGE(Material.COBBLESTONE, "VILLAGE", "HOUSE"),
    WELL(Material.MOSSY_COBBLESTONE, "", ""),
    WINDMILL(Material.WOOD, "VERY SMALL", "WINDMILL"),
    YELLOW(Material.GOLD_NUGGET, "YELLOW", "SUBMARINE"),
    CUSTOM(Material.OBSIDIAN, "", "");

    Material material;
    String firstLine;
    String secondLine;
    private final static Map<Material, PRESET> BY_MATERIAL = Maps.newHashMap();

    private PRESET(Material material, String firstLine, String secondLine) {
        this.material = material;
        this.firstLine = firstLine;
        this.secondLine = secondLine;
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

    static {
        for (PRESET preset : values()) {
            BY_MATERIAL.put(preset.getMaterial(), preset);
        }
    }
}
