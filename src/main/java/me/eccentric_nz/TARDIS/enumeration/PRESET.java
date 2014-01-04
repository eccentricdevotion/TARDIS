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

    ANGEL(Material.FEATHER),
    APPERTURE(Material.STICK),
    CAKE(Material.CAKE),
    CANDY(Material.SUGAR),
    CHALICE(Material.QUARTZ),
    CREEPY(Material.STRING),
    DESERT(Material.SAND),
    DUCK(Material.WOOD_BUTTON),
    FACTORY(Material.IRON_INGOT),
    FENCE(Material.IRON_FENCE),
    FLOWER(Material.RED_ROSE),
    GAZEBO(Material.FENCE),
    GRAVESTONE(Material.ENDER_STONE),
    HELIX(Material.SMOOTH_STAIRS),
    JAIL(Material.SMOOTH_BRICK),
    JUNGLE(Material.VINE),
    LAMP(Material.GLOWSTONE_DUST),
    LIBRARY(Material.BOOK),
    LIGHTHOUSE(Material.TORCH),
    MINESHAFT(Material.RAILS),
    NETHER(Material.NETHERRACK),
    NEW(Material.STEP),
    OLD(Material.WOOL),
    PANDORICA(Material.COAL),
    PARTY(Material.FIREWORK),
    PEANUT(Material.HARD_CLAY),
    PINE(Material.LEAVES),
    PORTAL(Material.WOOD_DOOR),
    PUNKED(Material.PISTON_BASE),
    RENDER(Material.FIRE),
    ROBOT(Material.COBBLE_WALL),
    SHROOM(Material.BROWN_MUSHROOM),
    SNOWMAN(Material.SNOW_BALL),
    STONE(Material.COBBLESTONE),
    SUBMERGED(Material.DIRT),
    SWAMP(Material.WATER_LILY),
    TELEPHONE(Material.GLASS),
    THEEND(Material.BEDROCK),
    TOILET(Material.LEVER),
    TOPSYTURVEY(Material.BUCKET),
    TORCH(Material.FLINT_AND_STEEL),
    VILLAGE(Material.COBBLESTONE),
    WELL(Material.MOSSY_COBBLESTONE),
    WINDMILL(Material.WOOD),
    YELLOW(Material.GOLD_NUGGET),
    CUSTOM(Material.OBSIDIAN);

    Material material;
    private final static Map<Material, PRESET> BY_MATERIAL = Maps.newHashMap();

    private PRESET(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
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
