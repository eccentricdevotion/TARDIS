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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.utils.weather;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIWeather;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class WeatherInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public WeatherInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Weather Menu", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the TARDIS Weather GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[9];
        // clear
        ItemStack clear = ItemStack.of(GUIWeather.CLEAR.material(), 1);
        ItemMeta sun = clear.getItemMeta();
        sun.displayName(Component.text(TARDISStringUtils.uppercaseFirst(plugin.getLanguage().getString("WEATHER_CLEAR"))));
        clear.setItemMeta(sun);
        stack[GUIWeather.CLEAR.slot()] = clear;
        // rain
        ItemStack rain = ItemStack.of(GUIWeather.RAIN.material(), 1);
        ItemMeta ing = rain.getItemMeta();
        ing.displayName(Component.text(TARDISStringUtils.uppercaseFirst(plugin.getLanguage().getString("WEATHER_RAIN"))));
        rain.setItemMeta(ing);
        stack[GUIWeather.RAIN.slot()] = rain;
        // thunder
        ItemStack thunder = ItemStack.of(GUIWeather.THUNDER.material(), 1);
        ItemMeta storm = thunder.getItemMeta();
        storm.displayName(Component.text(plugin.getLanguage().getString("WEATHER_THUNDER")));
        thunder.setItemMeta(storm);
        stack[GUIWeather.THUNDER.slot()] = thunder;
        // excite
        ItemStack excit = ItemStack.of(GUIWeather.EXCITE.material(), 1);
        ItemMeta ation = excit.getItemMeta();
        ation.displayName(Component.text(plugin.getLanguage().getString("WEATHER_EXCITE")));
        excit.setItemMeta(ation);
        stack[GUIWeather.EXCITE.slot()] = excit;
        // close
        ItemStack close = ItemStack.of(GUIWeather.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        stack[GUIWeather.CLOSE.slot()] = close;

        return stack;
    }
}
