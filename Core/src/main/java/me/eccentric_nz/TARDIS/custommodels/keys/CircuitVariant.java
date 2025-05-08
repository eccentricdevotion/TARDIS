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

import java.util.List;

public enum CircuitVariant {

    ADMIN(new NamespacedKey(TARDIS.plugin, "admin"), List.of(113.0f)),
    ARS(new NamespacedKey(TARDIS.plugin, "ars"), List.of(115.0f)),
    BIO(new NamespacedKey(TARDIS.plugin, "bio"), List.of(101.0f)),
    BRUSH(new NamespacedKey(TARDIS.plugin, "brush"), List.of(102.0f)),
    CHAMELEON(new NamespacedKey(TARDIS.plugin, "chameleon"), List.of(116.0f)),
    CONVERSION(new NamespacedKey(TARDIS.plugin, "conversion"), List.of(103.0f)),
    DIAMOND(new NamespacedKey(TARDIS.plugin, "circuit_diamond"), List.of(104.0f)),
    EMERALD(new NamespacedKey(TARDIS.plugin, "circuit_emerald"), List.of(105.0f)),
    GALLIFREY(new NamespacedKey(TARDIS.plugin, "gallifrey"), List.of(130.0f)),
    IGNITE(new NamespacedKey(TARDIS.plugin, "ignite"), List.of(106.0f)),
    INPUT(new NamespacedKey(TARDIS.plugin, "input"), List.of(117.0f)),
    INVISIBILITY(new NamespacedKey(TARDIS.plugin, "invisibility"), List.of(118.0f)),
    KNOCKBACK(new NamespacedKey(TARDIS.plugin, "knockback"), List.of(107.0f)),
    LOCATOR(new NamespacedKey(TARDIS.plugin, "locator"), List.of(119.0f)),
    MATERIALISATION(new NamespacedKey(TARDIS.plugin, "materialisation"), List.of(120.0f)),
    MEMORY(new NamespacedKey(TARDIS.plugin, "memory"), List.of(121.0f)),
    PAINTER(new NamespacedKey(TARDIS.plugin, "painter"), List.of(108.0f)),
    PERCEPTION(new NamespacedKey(TARDIS.plugin, "perception"), List.of(109.0f)),
    PICKUP(new NamespacedKey(TARDIS.plugin, "pickup"), List.of(110.0f)),
    RANDOM(new NamespacedKey(TARDIS.plugin, "random"), List.of(122.0f)),
    REDSTONE(new NamespacedKey(TARDIS.plugin, "circuit_redstone"), List.of(111.0f)),
    RIFT(new NamespacedKey(TARDIS.plugin, "rift"), List.of(112.0f)),
    SCANNER(new NamespacedKey(TARDIS.plugin, "scanner"), List.of(123.0f)),
    SONIC(new NamespacedKey(TARDIS.plugin, "sonic"), List.of(114.0f)),
    STATTENHEIM(new NamespacedKey(TARDIS.plugin, "stattenheim"), List.of(124.0f)),
    TELEPATHIC(new NamespacedKey(TARDIS.plugin, "telepathic"), List.of(125.0f)),
    TEMPORAL(new NamespacedKey(TARDIS.plugin, "temporal"), List.of(126.0f)),
    TELEPATHIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "telepathic_damaged"), List.of(225.0f)),
    STATTENHEIM_DAMAGED(new NamespacedKey(TARDIS.plugin, "stattenheim_damaged"), List.of(224.0f)),
    MATERIALISATION_DAMAGED(new NamespacedKey(TARDIS.plugin, "materialisation_damaged"), List.of(220.0f)),
    LOCATOR_DAMAGED(new NamespacedKey(TARDIS.plugin, "locator_damaged"), List.of(219.0f)),
    CHAMELEON_DAMAGED(new NamespacedKey(TARDIS.plugin, "chameleon_damaged"), List.of(216.0f)),
    SONIC_DAMAGED(new NamespacedKey(TARDIS.plugin, "sonic_damaged"), List.of(214.0f)),
    ARS_DAMAGED(new NamespacedKey(TARDIS.plugin, "ars_damaged"), List.of(215.0f)),
    TEMPORAL_DAMAGED(new NamespacedKey(TARDIS.plugin, "temporal_damaged"), List.of(226.0f)),
    MEMORY_DAMAGED(new NamespacedKey(TARDIS.plugin, "memory_damaged"), List.of(221.0f)),
    INPUT_DAMAGED(new NamespacedKey(TARDIS.plugin, "input_damaged"), List.of(217.0f)),
    SCANNER_DAMAGED(new NamespacedKey(TARDIS.plugin, "scanner_damaged"), List.of(223.0f)),
    PERCEPTION_DAMAGED(new NamespacedKey(TARDIS.plugin, "perception_damaged"), List.of(209.0f)),
    RANDOM_DAMAGED(new NamespacedKey(TARDIS.plugin, "random_damaged"), List.of(222.0f)),
    INVISIBILITY_DAMAGED(new NamespacedKey(TARDIS.plugin, "invisibility_damaged"), List.of(218.0f)),
    RIFT_DAMAGED(new NamespacedKey(TARDIS.plugin, "rift_damaged"), List.of(212.0f));

    private final NamespacedKey key;
    private final List<Float> floats;

    CircuitVariant(NamespacedKey key, List<Float> floats) {
        this.key = key;
        this.floats = floats;
    }

    public NamespacedKey getKey() {
        return key;
    }

    public List<Float> getFloats() {
        return floats;
    }
}
