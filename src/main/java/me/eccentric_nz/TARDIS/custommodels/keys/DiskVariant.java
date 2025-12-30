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

public enum DiskVariant {

    AREA_DISK(new NamespacedKey(TARDIS.plugin, "area_disk")),
    BIOME_DISK(new NamespacedKey(TARDIS.plugin, "biome_disk")),
    SAVE_DISK(new NamespacedKey(TARDIS.plugin, "save_disk")),
    CONTROL_DISK(new NamespacedKey(TARDIS.plugin, "control_disk")),
    PRESET_DISK(new NamespacedKey(TARDIS.plugin, "preset_disk")),
    BLUEPRINT_DISK(new NamespacedKey(TARDIS.plugin, "blueprint_disk")),
    BLANK_DISK(new NamespacedKey(TARDIS.plugin, "blank_disk")),
    PLAYER_DISK(new NamespacedKey(TARDIS.plugin, "player_disk")),
    HANDLES_DISK(new NamespacedKey(TARDIS.plugin, "handles_disk"));

    private final NamespacedKey key;

    DiskVariant(NamespacedKey key) {
        this.key = key;
    }

    public NamespacedKey getKey() {
        return key;
    }
}
