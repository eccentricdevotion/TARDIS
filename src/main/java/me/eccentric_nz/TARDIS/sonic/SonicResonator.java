/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public enum SonicResonator {

    // pitch values between low 0.5 - high 2.0
    RESIN(Material.RESIN_CLUMP, 0.50f, Set.of(EntityType.SNIFFER)),
    GOLD(Material.GOLD_NUGGET, 0.71f, Set.of(EntityType.PANDA, EntityType.POLAR_BEAR)),
    ICE(Material.ICE, 0.80f, Set.of(EntityType.COW, EntityType.MOOSHROOM)),
    // TODO ICE(Material.ICE_CRYSTAL, 0.80f, Set.of(EntityType.COW, EntityType.MOOSHROOM)),
    GLOWSTONE(Material.GLOWSTONE, 0.85f, Set.of(EntityType.HORSE)),
    COPPER(Material.COPPER_NUGGET, 0.91f, Set.of(EntityType.CAMEL, EntityType.LLAMA)),
    REDSTONE(Material.REDSTONE, 0.94f, Set.of(EntityType.STRIDER)),
    PRISMARINE(Material.PRISMARINE_SHARD, 1.00f, Set.of(EntityType.SHEEP)),
    LAPIS(Material.LAPIS_LAZULI, 1.08f, Set.of(EntityType.PIG)),
    IRON(Material.IRON_NUGGET, 1.11f, Set.of(EntityType.CAT, EntityType.WOLF)),
    QUARTZ(Material.QUARTZ, 1.14f, Set.of(EntityType.RABBIT)),
    AMETHYST(Material.AMETHYST_SHARD, 1.15f, Set.of(EntityType.CHICKEN, EntityType.PARROT)),
    EMERALD(Material.EMERALD, 1.37f, Set.of(EntityType.AXOLOTL, EntityType.FROG)),
    ECHO_SHARD(Material.ECHO_SHARD, 1.60f, Set.of(EntityType.HAPPY_GHAST)),
    DIAMOND(Material.DIAMOND, 2.00f, Set.of(EntityType.ALLAY, EntityType.BEE));

    SonicResonator(Material material, float pitch, Set<EntityType> mobs) {
        this.material = material;
        this.pitch = pitch;
        this.mobs = mobs;
    }

    private final Material material;
    private final float pitch;
    private final Set<EntityType> mobs;
    public static final Set<Material> resonators = new HashSet<>();
    public static final HashMap<Material, SonicResonator> lookup = new HashMap<>();

    static {
        for (SonicResonator r : values()) {
            resonators.add(r.material);
            lookup.put(r.material, r);
        }
    }

    public Material getMaterial() {
        return material;
    }

    public float getPitch() {
        return pitch;
    }

    public Set<EntityType> getMobs() {
        return mobs;
    }
}
