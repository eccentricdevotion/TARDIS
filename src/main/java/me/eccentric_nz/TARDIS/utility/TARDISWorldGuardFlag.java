/*
 * Copyright (C) 2021 eccentric_nz
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author eccentric_nz
 */
public class TARDISWorldGuardFlag {

    private static final Map<String, StateFlag> FLAG_LOOKUP;

    static {
        Map<String, StateFlag> flag_lookup = new HashMap<>();
        flag_lookup.put("build", Flags.BUILD);
        flag_lookup.put("chest-access", Flags.CHEST_ACCESS);
        flag_lookup.put("creeper-explosion", Flags.CREEPER_EXPLOSION);
        flag_lookup.put("enderdragon-block-damage", Flags.ENDERDRAGON_BLOCK_DAMAGE);
        flag_lookup.put("enderman-grief", Flags.ENDER_BUILD);
        flag_lookup.put("enderpearl", Flags.ENDERPEARL);
        flag_lookup.put("entity-item-frame-destroy", Flags.ENTITY_ITEM_FRAME_DESTROY);
        flag_lookup.put("entity-painting-destroy", Flags.ENTITY_PAINTING_DESTROY);
        flag_lookup.put("entry", Flags.ENTRY);
        flag_lookup.put("exit", Flags.EXIT);
        flag_lookup.put("fire-spread", Flags.FIRE_SPREAD);
        flag_lookup.put("ghast-fireball", Flags.GHAST_FIREBALL);
        flag_lookup.put("grass-growth", Flags.GRASS_SPREAD);
        flag_lookup.put("ice-form", Flags.ICE_FORM);
        flag_lookup.put("ice-melt", Flags.ICE_MELT);
        flag_lookup.put("invincible", Flags.INVINCIBILITY);
        flag_lookup.put("lava-fire", Flags.LAVA_FIRE);
        flag_lookup.put("lava-flow", Flags.LAVA_FLOW);
        flag_lookup.put("leaf-decay", Flags.LEAF_DECAY);
        flag_lookup.put("lighter", Flags.LIGHTER);
        flag_lookup.put("lightning", Flags.LIGHTNING);
        flag_lookup.put("mob-damage", Flags.MOB_DAMAGE);
        flag_lookup.put("mob-spawning", Flags.MOB_SPAWNING);
        flag_lookup.put("passthrough", Flags.PASSTHROUGH);
        flag_lookup.put("pistons", Flags.PISTONS);
        flag_lookup.put("pvp", Flags.PVP);
        flag_lookup.put("sleep", Flags.SLEEP);
        flag_lookup.put("snow-fall", Flags.SNOW_FALL);
        flag_lookup.put("snow-melt", Flags.SNOW_MELT);
        flag_lookup.put("tnt", Flags.TNT);
        flag_lookup.put("use", Flags.USE);
        flag_lookup.put("vehicle-destroy", Flags.DESTROY_VEHICLE);
        flag_lookup.put("vehicle-place", Flags.PLACE_VEHICLE);
        flag_lookup.put("water-flow", Flags.WATER_FLOW);
        FLAG_LOOKUP = Collections.unmodifiableMap(flag_lookup);
    }

    public static Map<String, StateFlag> getFLAG_LOOKUP() {
        return FLAG_LOOKUP;
    }
}
