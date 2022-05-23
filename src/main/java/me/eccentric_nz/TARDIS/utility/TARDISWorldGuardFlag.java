/*
 * Copyright (C) 2022 eccentric_nz
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

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;

import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TARDISWorldGuardFlag {

    private static final Map<String, StateFlag> FLAG_LOOKUP;

    static {
        FLAG_LOOKUP = Map.ofEntries(
                Map.entry("build", Flags.BUILD),
                Map.entry("chest-access", Flags.CHEST_ACCESS),
                Map.entry("creeper-explosion", Flags.CREEPER_EXPLOSION),
                Map.entry("enderdragon-block-damage", Flags.ENDERDRAGON_BLOCK_DAMAGE),
                Map.entry("enderman-grief", Flags.ENDER_BUILD),
                Map.entry("enderpearl", Flags.ENDERPEARL),
                Map.entry("entity-item-frame-destroy", Flags.ENTITY_ITEM_FRAME_DESTROY),
                Map.entry("entity-painting-destroy", Flags.ENTITY_PAINTING_DESTROY),
                Map.entry("entry", Flags.ENTRY),
                Map.entry("exit", Flags.EXIT),
                Map.entry("fire-spread", Flags.FIRE_SPREAD),
                Map.entry("ghast-fireball", Flags.GHAST_FIREBALL),
                Map.entry("grass-growth", Flags.GRASS_SPREAD),
                Map.entry("ice-form", Flags.ICE_FORM),
                Map.entry("ice-melt", Flags.ICE_MELT),
                Map.entry("invincible", Flags.INVINCIBILITY),
                Map.entry("lava-fire", Flags.LAVA_FIRE),
                Map.entry("lava-flow", Flags.LAVA_FLOW),
                Map.entry("leaf-decay", Flags.LEAF_DECAY),
                Map.entry("lighter", Flags.LIGHTER),
                Map.entry("lightning", Flags.LIGHTNING),
                Map.entry("mob-damage", Flags.MOB_DAMAGE),
                Map.entry("mob-spawning", Flags.MOB_SPAWNING),
                Map.entry("passthrough", Flags.PASSTHROUGH),
                Map.entry("pistons", Flags.PISTONS),
                Map.entry("pvp", Flags.PVP),
                Map.entry("sleep", Flags.SLEEP),
                Map.entry("snow-fall", Flags.SNOW_FALL),
                Map.entry("snow-melt", Flags.SNOW_MELT),
                Map.entry("tnt", Flags.TNT),
                Map.entry("use", Flags.USE),
                Map.entry("vehicle-destroy", Flags.DESTROY_VEHICLE),
                Map.entry("vehicle-place", Flags.PLACE_VEHICLE),
                Map.entry("water-flow", Flags.WATER_FLOW)
        );
    }

    public static Map<String, StateFlag> getFLAG_LOOKUP() {
        return FLAG_LOOKUP;
    }
}
