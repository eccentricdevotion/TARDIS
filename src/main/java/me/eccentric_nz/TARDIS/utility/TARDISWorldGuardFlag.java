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
package me.eccentric_nz.TARDIS.utility;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author eccentric_nz
 */
public class TARDISWorldGuardFlag {

    private static final Map<String, StateFlag> FLAG_LOOKUP;

    static {
        Map<String, StateFlag> flag_lookup = new HashMap<>();
        flag_lookup.put("build", DefaultFlag.BUILD);
        flag_lookup.put("chest-access", DefaultFlag.CHEST_ACCESS);
        flag_lookup.put("creeper-explosion", DefaultFlag.CREEPER_EXPLOSION);
        flag_lookup.put("enderdragon-block-damage", DefaultFlag.ENDERDRAGON_BLOCK_DAMAGE);
        flag_lookup.put("enderman-grief", DefaultFlag.ENDER_BUILD);
        flag_lookup.put("enderpearl", DefaultFlag.ENDERPEARL);
        flag_lookup.put("entity-item-frame-destroy", DefaultFlag.ENTITY_ITEM_FRAME_DESTROY);
        flag_lookup.put("entity-painting-destroy", DefaultFlag.ENTITY_PAINTING_DESTROY);
        flag_lookup.put("entry", DefaultFlag.ENTRY);
        flag_lookup.put("exit", DefaultFlag.EXIT);
        flag_lookup.put("fire-spread", DefaultFlag.FIRE_SPREAD);
        flag_lookup.put("ghast-fireball", DefaultFlag.GHAST_FIREBALL);
        flag_lookup.put("grass-growth", DefaultFlag.GRASS_SPREAD);
        flag_lookup.put("ice-form", DefaultFlag.ICE_FORM);
        flag_lookup.put("ice-melt", DefaultFlag.ICE_MELT);
        flag_lookup.put("invincible", DefaultFlag.INVINCIBILITY);
        flag_lookup.put("lava-fire", DefaultFlag.LAVA_FIRE);
        flag_lookup.put("lava-flow", DefaultFlag.LAVA_FLOW);
        flag_lookup.put("leaf-decay", DefaultFlag.LEAF_DECAY);
        flag_lookup.put("lighter", DefaultFlag.LIGHTER);
        flag_lookup.put("lightning", DefaultFlag.LIGHTNING);
        flag_lookup.put("mob-damage", DefaultFlag.MOB_DAMAGE);
        flag_lookup.put("mob-spawning", DefaultFlag.MOB_SPAWNING);
        flag_lookup.put("passthrough", DefaultFlag.PASSTHROUGH);
        flag_lookup.put("pistons", DefaultFlag.PISTONS);
        flag_lookup.put("pvp", DefaultFlag.PVP);
        flag_lookup.put("sleep", DefaultFlag.SLEEP);
        flag_lookup.put("snow-fall", DefaultFlag.SNOW_FALL);
        flag_lookup.put("snow-melt", DefaultFlag.SNOW_MELT);
        flag_lookup.put("tnt", DefaultFlag.TNT);
        flag_lookup.put("use", DefaultFlag.USE);
        flag_lookup.put("vehicle-destroy", DefaultFlag.DESTROY_VEHICLE);
        flag_lookup.put("vehicle-place", DefaultFlag.PLACE_VEHICLE);
        flag_lookup.put("water-flow", DefaultFlag.WATER_FLOW);
        FLAG_LOOKUP = Collections.unmodifiableMap(flag_lookup);
    }

    public static Map<String, StateFlag> getFLAG_LOOKUP() {
        return FLAG_LOOKUP;
    }
}
