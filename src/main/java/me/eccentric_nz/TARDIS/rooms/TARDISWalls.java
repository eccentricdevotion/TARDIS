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
package me.eccentric_nz.TARDIS.rooms;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.set.RegistrySet;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.key.Key;
import org.bukkit.Material;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.RecipeChoice;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A TARDIS isn't just a vehicle for travelling in space and time. As a TARDIS has no real constraints on the amount of
 * space it can use, most TARDISes contain extensive areas which can be used as living quarters or storage space.
 *
 * @author eccentric_nz
 */
public class TARDISWalls {

    public static final List<TypedKey<ItemType>> BLOCKS = new ArrayList<>();
    public static final RecipeChoice.ItemTypeChoice CHOICES;

    static {
        for (String m : TARDIS.plugin.getBlocksConfig().getStringList("tardis_blocks")) {
            try {
                TypedKey<ItemType> typedKey = TypedKey.create(RegistryKey.ITEM, Key.key(Key.MINECRAFT_NAMESPACE, m.toLowerCase(Locale.ROOT)));
                BLOCKS.add(typedKey);
            } catch (IllegalArgumentException e) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Invalid material '" + m + "' in tardis_blocks list! " + e.getMessage());
            }
        }
        RegistryKeySet<ItemType> keySet = RegistrySet.keySet(RegistryKey.ITEM, BLOCKS);
        CHOICES = RecipeChoice.itemType(keySet);
    }
}
