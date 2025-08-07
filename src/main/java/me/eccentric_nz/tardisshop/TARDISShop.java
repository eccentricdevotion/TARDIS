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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemBreak;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemDespawn;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemExplode;
import me.eccentric_nz.tardisshop.listener.TARDISShopItemInteract;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.RegisteredServiceProvider;

public class TARDISShop {

    private final TARDIS plugin;

    public TARDISShop(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        if (plugin.getPM().isPluginEnabled("Vault")) {
            plugin.getPM().registerEvents(new TARDISShopItemInteract(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISShopItemDespawn(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISShopItemBreak(plugin), plugin);
            plugin.getPM().registerEvents(new TARDISShopItemExplode(plugin), plugin);
            ShopSettings settings = new ShopSettings();
            settings.setItemKey(new NamespacedKey(plugin, "tardis_shop_item"));
            settings.setBlockMaterial(Material.valueOf(plugin.getShopConfig().getString("block")));
            setupEconomy(settings);
            plugin.setShopSettings(settings);
            TARDISShopCommand command = new TARDISShopCommand(plugin);
            plugin.getCommand("tardisshop").setExecutor(command);
            plugin.getCommand("tardisshop").setTabCompleter(command);
        } else {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.WARNING, "This feature requires the Vault plugin to function, disabling...");
        }
    }

    // load economy API from Vault
    private void setupEconomy(ShopSettings settings) {
        if (plugin.getPM().getPlugin("Vault") == null) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        settings.setEconomy(rsp.getProvider());
    }
}
