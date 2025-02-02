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
package me.eccentric_nz.tardisshop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

/**
 *
 * @author eccentric_nz
 */
public class ShopSettings {
    
    private final HashMap<UUID, TARDISShopItem> settingItem = new HashMap<>();
    private final Set<UUID> removingItem = new HashSet<>();
    private NamespacedKey itemKey;
    private Material blockMaterial;
    private Economy economy;

    public HashMap<UUID, TARDISShopItem> getSettingItem() {
        return settingItem;
    }

    public Set<UUID> getRemovingItem() {
        return removingItem;
    }

    public NamespacedKey getItemKey() {
        return itemKey;
    }

    public void setItemKey(NamespacedKey itemKey) {
        this.itemKey = itemKey;
    }

    public Material getBlockMaterial() {
        return blockMaterial;
    }

    public void setBlockMaterial(Material blockMaterial) {
        this.blockMaterial = blockMaterial;
    }

    public Economy getEconomy() {
        return economy;
    }

    public void setEconomy(Economy economy) {
        this.economy = economy;
    }
}
