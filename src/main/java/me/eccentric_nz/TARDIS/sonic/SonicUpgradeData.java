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

import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.List;

public class SonicUpgradeData {

    public static final HashMap<String, String> upgrades = new HashMap<>();
    public static final HashMap<NamespacedKey, String> customModelData = new HashMap<>();
    public static final HashMap<String, String> displayNames = new HashMap<>();
    public static final List<String> sonicKeys = List.of(
            "Bio-scanner Upgrade",
            "Diamond Upgrade",
            "Emerald Upgrade",
            "Redstone Upgrade",
            "Painter Upgrade",
            "Ignite Upgrade",
            "Pickup Arrows Upgrade",
            "Knockback Upgrade",
            "Brush Upgrade",
            "Conversion Upgrade");

    static {
        upgrades.put("Admin Upgrade", "admin");
        upgrades.put("Bio-scanner Upgrade", "bio");
        upgrades.put("Redstone Upgrade", "redstone");
        upgrades.put("Diamond Upgrade", "diamond");
        upgrades.put("Emerald Upgrade", "emerald");
        upgrades.put("Painter Upgrade", "painter");
        upgrades.put("Ignite Upgrade", "ignite");
        upgrades.put("Pickup Arrows Upgrade", "arrow");
        upgrades.put("Knockback Upgrade", "knockback");
        upgrades.put("Brush Upgrade", "brush");
        upgrades.put("Conversion Upgrade", "conversion");
        customModelData.put(CircuitVariant.ADMIN.getKey(), "Admin Upgrade");
        customModelData.put(CircuitVariant.BIO.getKey(), "Bio-scanner Upgrade");
        customModelData.put(CircuitVariant.REDSTONE.getKey(), "Redstone Upgrade");
        customModelData.put(CircuitVariant.DIAMOND.getKey(), "Diamond Upgrade");
        customModelData.put(CircuitVariant.EMERALD.getKey(), "Emerald Upgrade");
        customModelData.put(CircuitVariant.PAINTER.getKey(), "Painter Upgrade");
        customModelData.put(CircuitVariant.IGNITE.getKey(), "Ignite Upgrade");
        customModelData.put(CircuitVariant.PICKUP.getKey(), "Pickup Arrows Upgrade");
        customModelData.put(CircuitVariant.KNOCKBACK.getKey(), "Knockback Upgrade");
        customModelData.put(CircuitVariant.BRUSH.getKey(), "Brush Upgrade");
        customModelData.put(CircuitVariant.CONVERSION.getKey(), "Conversion Upgrade");
        displayNames.put("Bio-scanner Circuit", "Bio-scanner Upgrade");
        displayNames.put("Brush Circuit", "Brush Upgrade");
        displayNames.put("Conversion Circuit", "Conversion Upgrade");
        displayNames.put("Diamond Disruptor Circuit", "Diamond Upgrade");
        displayNames.put("Emerald Environment Circuit", "Emerald Upgrade");
        displayNames.put("Ignite Circuit", "Ignite Upgrade");
        displayNames.put("Knockback Circuit", "Knockback Upgrade");
        displayNames.put("Painter Circuit", "Painter Upgrade");
        displayNames.put("Pickup Arrows Circuit", "Pickup Arrows Upgrade");
        displayNames.put("Redstone Activator Circuit", "Redstone Upgrade");
        displayNames.put("Server Admin Circuit", "Admin Upgrade");
    }
}
