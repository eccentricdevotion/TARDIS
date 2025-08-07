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

public enum ParticleItem {

    BLOCK(new NamespacedKey(TARDIS.plugin, "particle_block")),
    BLOCK_INFO(new NamespacedKey(TARDIS.plugin, "particle_block_info")),
    COLOUR(new NamespacedKey(TARDIS.plugin, "particle_colour")),
    COLOUR_INFO(new NamespacedKey(TARDIS.plugin, "particle_colour_info")),
    DENSITY(new NamespacedKey(TARDIS.plugin, "particle_density")),
    EFFECT(new NamespacedKey(TARDIS.plugin, "particle_effect")),
    EFFECT_INFO(new NamespacedKey(TARDIS.plugin, "particle_effect_info")),
    EFFECT_SELECTED(new NamespacedKey(TARDIS.plugin, "particle_effect_selected")),
    SHAPE(new NamespacedKey(TARDIS.plugin, "particle_shape")),
    SHAPE_INFO(new NamespacedKey(TARDIS.plugin, "particle_shape_info")),
    SHAPE_SELECTED(new NamespacedKey(TARDIS.plugin, "particle_shape_selected")),
    SPEED(new NamespacedKey(TARDIS.plugin, "particle_speed")),
    TEST(new NamespacedKey(TARDIS.plugin, "particle_test"));

    private final NamespacedKey key;

    ParticleItem(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
