/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import com.destroystokyo.paper.MaterialTags;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISMaterials {

    public static final Set<Material> doors = Sets.union(Sets.union(Tag.DOORS.getValues(), Tag.TRAPDOORS.getValues()), Tag.BUTTONS.getValues());

    public static final List<Material> plants = List.of(Material.CACTUS, Material.DEAD_BUSH, Material.FERN, Material.SHORT_GRASS, Material.LARGE_FERN, Material.TALL_GRASS, Material.HANGING_ROOTS, Material.WARPED_ROOTS, Material.NETHER_SPROUTS, Material.CRIMSON_ROOTS);

    public static final List<Material> not_glass = new ArrayList<>() {
        {
            add(Material.AIR);
            add(Material.GLASS);
            add(Material.REDSTONE_LAMP);
            add(Material.REDSTONE_TORCH);
            add(Material.SOUL_TORCH);
            add(Material.SOUL_WALL_TORCH);
            add(Material.TORCH);
            add(Material.VINE);
            add(Material.WALL_TORCH);
            addAll(Tag.DOORS.getValues());
            addAll(Tag.SIGNS.getValues());
            addAll(Tag.TRAPDOORS.getValues());
            addAll(Tag.BUTTONS.getValues());
            addAll(Tag.WALL_SIGNS.getValues());
        }
    };

    public static final List<Material> precious = new ArrayList<>() {
        {
            add(Material.BARRIER);
            add(Material.BEDROCK);
            add(Material.DIAMOND_BLOCK);
            add(Material.EMERALD_BLOCK);
            add(Material.GOLD_BLOCK);
            add(Material.IRON_BLOCK);
            add(Material.LAPIS_BLOCK);
            add(Material.LIGHT);
            add(Material.NETHERITE_BLOCK);
            add(Material.NETHER_QUARTZ_ORE);
            add(Material.OBSIDIAN);
            add(Material.RAW_GOLD_BLOCK);
            add(Material.RAW_IRON_BLOCK);
            add(Material.REDSTONE_BLOCK);
            addAll(MaterialTags.ORES.getValues());
        }
    };

    public static final List<Material> crops = List.of(Material.SUGAR_CANE, Material.WHEAT, Material.CARROTS, Material.BEETROOTS, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.NETHER_WART, Material.POTATOES, Material.COCOA, Material.CACTUS, Material.SWEET_BERRY_BUSH, Material.PITCHER_CROP, Material.TORCHFLOWER);

    public static final List<Material> fish_buckets = List.of(Material.TROPICAL_FISH_BUCKET, Material.COD_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET);

    public static final List<Material> submarine_blocks = List.of(Material.BLUE_ICE, Material.FROSTED_ICE, Material.ICE, Material.KELP_PLANT, Material.PACKED_ICE, Material.SEA_PICKLE, Material.SEAGRASS, Material.TALL_SEAGRASS, Material.WATER);

    public static final HashMap<Material, EntityType> fishMap = new HashMap<>() {

        @Serial
        private static final long serialVersionUID = 3109256773218160485L;

        {
            put(Material.TROPICAL_FISH_BUCKET, EntityType.TROPICAL_FISH);
            put(Material.COD_BUCKET, EntityType.COD);
            put(Material.PUFFERFISH_BUCKET, EntityType.PUFFERFISH);
            put(Material.SALMON_BUCKET, EntityType.SALMON);
        }
    };
}
