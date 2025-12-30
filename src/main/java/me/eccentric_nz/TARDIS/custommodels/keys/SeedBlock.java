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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

public enum SeedBlock {

    ANCIENT(new NamespacedKey(TARDIS.plugin, "seed_ancient")),
    ARS(new NamespacedKey(TARDIS.plugin, "seed_ars")),
    BIGGER(new NamespacedKey(TARDIS.plugin, "seed_bigger")),
    BONE(new NamespacedKey(TARDIS.plugin, "seed_bone")),
    BUDGET(new NamespacedKey(TARDIS.plugin, "seed_budget")),
    CAVE(new NamespacedKey(TARDIS.plugin, "seed_cave")),
    COPPER(new NamespacedKey(TARDIS.plugin, "seed_copper")),
    CORAL(new NamespacedKey(TARDIS.plugin, "seed_coral")),
    CURSED(new NamespacedKey(TARDIS.plugin, "seed_cursed")),
    DELTA(new NamespacedKey(TARDIS.plugin, "seed_delta")),
    DELUXE(new NamespacedKey(TARDIS.plugin, "seed_deluxe")),
    DIVISION(new NamespacedKey(TARDIS.plugin, "seed_division")),
    EIGHTH(new NamespacedKey(TARDIS.plugin, "seed_eighth")),
    ELEVENTH(new NamespacedKey(TARDIS.plugin, "seed_eleventh")),
    ENDER(new NamespacedKey(TARDIS.plugin, "seed_ender")),
    FACTORY(new NamespacedKey(TARDIS.plugin, "seed_factory")),
    FIFTEENTH(new NamespacedKey(TARDIS.plugin, "seed_fifteenth")),
    FUGITIVE(new NamespacedKey(TARDIS.plugin, "seed_fugitive")),
    HOSPITAL(new NamespacedKey(TARDIS.plugin, "seed_hospital")),
    MASTER(new NamespacedKey(TARDIS.plugin, "seed_master")),
    MECHANICAL(new NamespacedKey(TARDIS.plugin, "seed_mechanical")),
    ORIGINAL(new NamespacedKey(TARDIS.plugin, "seed_original")),
    PLANK(new NamespacedKey(TARDIS.plugin, "seed_plank")),
    PYRAMID(new NamespacedKey(TARDIS.plugin, "seed_pyramid")),
    REDSTONE(new NamespacedKey(TARDIS.plugin, "seed_redstone")),
    ROTOR(new NamespacedKey(TARDIS.plugin, "seed_rotor")),
    RUSTIC(new NamespacedKey(TARDIS.plugin, "seed_rustic")),
    SIDRAT(new NamespacedKey(TARDIS.plugin, "seed_sidrat")),
    STEAMPUNK(new NamespacedKey(TARDIS.plugin, "seed_steampunk")),
    THIRTEENTH(new NamespacedKey(TARDIS.plugin, "seed_thirteenth")),
    TOM(new NamespacedKey(TARDIS.plugin, "seed_tom")),
    TWELFTH(new NamespacedKey(TARDIS.plugin, "seed_twelfth")),
    WAR(new NamespacedKey(TARDIS.plugin, "seed_war")),
    WEATHERED(new NamespacedKey(TARDIS.plugin, "seed_weathered")),
    LEGACY_BIGGER(new NamespacedKey(TARDIS.plugin, "seed_legacy_bigger")),
    LEGACY_DELUXE(new NamespacedKey(TARDIS.plugin, "seed_legacy_deluxe")),
    LEGACY_ELEVENTH(new NamespacedKey(TARDIS.plugin, "seed_legacy_eleventh")),
    LEGACY_REDSTONE(new NamespacedKey(TARDIS.plugin, "seed_legacy_redstone")),
    CUSTOM(new NamespacedKey(TARDIS.plugin, "seed_custom")),
    GROW(new NamespacedKey(TARDIS.plugin, "seed_grow")),
    SMALL(new NamespacedKey(TARDIS.plugin, "seed_small")),
    MEDIUM(new NamespacedKey(TARDIS.plugin, "seed_medium")),
    TALL(new NamespacedKey(TARDIS.plugin, "seed_tall"));

    private final NamespacedKey key;

    SeedBlock(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
