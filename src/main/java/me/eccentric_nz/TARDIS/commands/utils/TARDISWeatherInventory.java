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
package me.eccentric_nz.tardis.commands.utils;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.custommodeldata.GuiWeather;
import me.eccentric_nz.tardis.utility.TardisStringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class TardisWeatherInventory {

    private final TardisPlugin plugin;
    private final ItemStack[] weather;

    public TardisWeatherInventory(TardisPlugin plugin) {
        this.plugin = plugin;
        weather = getItemStack();
    }

    /**
     * Constructs an inventory for the tardis Weather GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[9];
        // clear
        ItemStack clear = new ItemStack(GuiWeather.CLEAR.getMaterial(), 1);
        ItemMeta sun = clear.getItemMeta();
        assert sun != null;
        sun.setDisplayName(TardisStringUtils.uppercaseFirst(Objects.requireNonNull(plugin.getLanguage().getString("WEATHER_CLEAR"))));
        sun.setCustomModelData(GuiWeather.CLEAR.getCustomModelData());
        clear.setItemMeta(sun);
        stack[GuiWeather.CLEAR.getSlot()] = clear;
        // rain
        ItemStack rain = new ItemStack(GuiWeather.RAIN.getMaterial(), 1);
        ItemMeta ing = rain.getItemMeta();
        assert ing != null;
        ing.setDisplayName(TardisStringUtils.uppercaseFirst(Objects.requireNonNull(plugin.getLanguage().getString("WEATHER_RAIN"))));
        ing.setCustomModelData(GuiWeather.RAIN.getCustomModelData());
        rain.setItemMeta(ing);
        stack[GuiWeather.RAIN.getSlot()] = rain;
        // thunder
        ItemStack thunder = new ItemStack(GuiWeather.THUNDER.getMaterial(), 1);
        ItemMeta storm = thunder.getItemMeta();
        assert storm != null;
        storm.setDisplayName(plugin.getLanguage().getString("WEATHER_THUNDER"));
        storm.setCustomModelData(GuiWeather.THUNDER.getCustomModelData());
        thunder.setItemMeta(storm);
        stack[GuiWeather.THUNDER.getSlot()] = thunder;
        // excite
        ItemStack excit = new ItemStack(GuiWeather.EXCITE.getMaterial(), 1);
        ItemMeta ation = excit.getItemMeta();
        assert ation != null;
        ation.setDisplayName(plugin.getLanguage().getString("WEATHER_EXCITE"));
        ation.setCustomModelData(GuiWeather.EXCITE.getCustomModelData());
        excit.setItemMeta(ation);
        stack[GuiWeather.EXCITE.getSlot()] = excit;
        // close
        ItemStack close = new ItemStack(GuiWeather.CLOSE.getMaterial(), 1);
        ItemMeta can = close.getItemMeta();
        assert can != null;
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GuiWeather.CLOSE.getCustomModelData());
        close.setItemMeta(can);
        stack[GuiWeather.CLOSE.getSlot()] = close;

        return stack;
    }

    public ItemStack[] getWeatherButtons() {
        return weather;
    }
}
