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
package me.eccentric_nz.TARDIS.geyser;

import org.geysermc.geyser.api.item.custom.CustomItemData;
import org.geysermc.geyser.api.item.custom.CustomItemOptions;

import java.util.HashMap;

public class GeyserItems {

    // TODO add items with upcoming Custom item API v2 - https://github.com/GeyserMC/Geyser/pull/5189
    public HashMap<String, CustomItemData> init() {
        HashMap<String, CustomItemData> items = new HashMap<>();
        CustomItemOptions itemOptions = CustomItemOptions.builder()
                .customModelData(101)
                .build();
        CustomItemData tardisKey = CustomItemData.builder()
                .name("tardis_key")
                .customItemOptions(itemOptions)
                .displayName("TARDIS Key")
                .build();
        items.put("minecraft:trial_key", tardisKey);
        return items;
    }
}
