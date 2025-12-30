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
package me.eccentric_nz.TARDIS.custommodels.keys;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;

import java.util.List;

public enum KeyVariant {

    BRASS_YALE(new NamespacedKey(TARDIS.plugin, "brass_yale_key"), List.of(101f)),
    BRASS_PLAIN(new NamespacedKey(TARDIS.plugin, "brass_plain_key"), List.of(102f)),
    SPADE_SHAPED(new NamespacedKey(TARDIS.plugin, "spade_shaped_key"), List.of(103f)),
    SILVER_YALE(new NamespacedKey(TARDIS.plugin, "silver_yale_key"), List.of(104f)),
    SEAL_OF_RASSILON(new NamespacedKey(TARDIS.plugin, "seal_of_rassilon_key"), List.of(105f)),
    SILVER_VARIANT(new NamespacedKey(TARDIS.plugin, "silver_variant_key"), List.of(106f)),
    SILVER_PLAIN(new NamespacedKey(TARDIS.plugin, "silver_plain_key"), List.of(107f)),
    SILVER_NEW(new NamespacedKey(TARDIS.plugin, "silver_new_key"), List.of(108f)),
    SILVER_ERA(new NamespacedKey(TARDIS.plugin, "silver_era_key"), List.of(109f)),
    SILVER_STRING(new NamespacedKey(TARDIS.plugin, "silver_string_key"), List.of(110f)),
    FILTER(new NamespacedKey(TARDIS.plugin, "perception_filter_key"), List.of(111f)),
    BRASS_STRING(new NamespacedKey(TARDIS.plugin, "brass_string_key"), List.of(112f)),
    BROMLEY_GOLD(new NamespacedKey(TARDIS.plugin, "bromley_gold_key"), List.of(113f)),
    PERCEPTION_FILTER(new NamespacedKey(TARDIS.plugin, "perception_filter_string_key"), null),
    REMOTE(new NamespacedKey(TARDIS.plugin, "remote_key"), null);

    private final NamespacedKey key;
    private final List<Float> floats;

    KeyVariant(NamespacedKey key, List<Float> floats) {
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
