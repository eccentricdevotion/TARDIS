/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIWeather;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISWeatherInventory {

    private final TARDIS plugin;
    private final ItemStack[] weather;

    public TARDISWeatherInventory(TARDIS plugin) {
        this.plugin = plugin;
        weather = getItemStack();
    }

    /**
     * Constructs an inventory for the TARDIS Weather GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[9];
        // clear
        ItemStack clear = new ItemStack(GUIWeather.CLEAR.getMaterial(), 1);
        ItemMeta sun = clear.getItemMeta();
        sun.setDisplayName(TARDISStringUtils.uppercaseFirst(plugin.getLanguage().getString("WEATHER_CLEAR")));
        sun.setCustomModelData(GUIWeather.CLEAR.getCustomModelData());
        clear.setItemMeta(sun);
        stack[GUIWeather.CLEAR.getSlot()] = clear;
        // rain
        ItemStack rain = new ItemStack(GUIWeather.RAIN.getMaterial(), 1);
        ItemMeta ing = rain.getItemMeta();
        ing.setDisplayName(TARDISStringUtils.uppercaseFirst(plugin.getLanguage().getString("WEATHER_RAIN")));
        ing.setCustomModelData(GUIWeather.RAIN.getCustomModelData());
        rain.setItemMeta(ing);
        stack[GUIWeather.RAIN.getSlot()] = rain;
        // thunder
        ItemStack thunder = new ItemStack(GUIWeather.THUNDER.getMaterial(), 1);
        ItemMeta storm = thunder.getItemMeta();
        storm.setDisplayName(plugin.getLanguage().getString("WEATHER_THUNDER"));
        storm.setCustomModelData(GUIWeather.THUNDER.getCustomModelData());
        thunder.setItemMeta(storm);
        stack[GUIWeather.THUNDER.getSlot()] = thunder;
        // excite
        ItemStack excit = new ItemStack(GUIWeather.EXCITE.getMaterial(), 1);
        ItemMeta ation = excit.getItemMeta();
        ation.setDisplayName(plugin.getLanguage().getString("WEATHER_EXCITE"));
        ation.setCustomModelData(GUIWeather.EXCITE.getCustomModelData());
        excit.setItemMeta(ation);
        stack[GUIWeather.EXCITE.getSlot()] = excit;
        // close
        ItemStack close = new ItemStack(GUIWeather.CLOSE.getMaterial(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUIWeather.CLOSE.getCustomModelData());
        close.setItemMeta(can);
        stack[GUIWeather.CLOSE.getSlot()] = close;

        return stack;
    }

    public ItemStack[] getWeatherButtons() {
        return weather;
    }
}
