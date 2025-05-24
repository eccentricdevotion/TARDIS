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
package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.custommodels.GUIWallFloor;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISTelepathicBiome {

    private final TARDIS plugin;
    private final int id;

    public TARDISTelepathicBiome(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public ItemStack[] getButtons() {
        ItemStack[] stack = new ItemStack[54];
        // only show biomes for the environment the TARDIS is currently in
        Current current = TARDISCache.CURRENT.get(id);
        if (current != null) {
            Environment environment = current.location().getWorld().getEnvironment();
            // biome finder
            List<Biome> biomes;
            switch (environment) {
                case NETHER -> biomes = EnvironmentBiomes.NETHER;
                case THE_END -> biomes = EnvironmentBiomes.END;
                default -> biomes = EnvironmentBiomes.OVERWORLD;
            }
            int i = 0;
            for (Biome biome : biomes) {
                if (i > 52) {
                    break;
                }
                Material material = EnvironmentBiomes.BIOME_BLOCKS.get(plugin.getFromRegistry().getKeysKey(biome));
                if (material != null) {
                    ItemStack is = new ItemStack(material, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(TARDISStringUtils.capitalise(biome.toString()));
                    is.setItemMeta(im);
                    stack[i] = is;
                    if (i % 9 == 7) {
                        i += 2;
                    } else {
                        i++;
                    }
                }
            }
            if (environment == Environment.NORMAL) {
                // scroll up
                ItemStack scroll_up = new ItemStack(GUIWallFloor.BUTTON_SCROLL_U.material(), 1);
                ItemMeta uim = scroll_up.getItemMeta();
                uim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_U"));
                uim.setItemModel(GUIWallFloor.BUTTON_SCROLL_U.key());
                scroll_up.setItemMeta(uim);
                stack[GUIWallFloor.BUTTON_SCROLL_U.slot()] = scroll_up;
                // scroll down
                ItemStack scroll_down = new ItemStack(GUIWallFloor.BUTTON_SCROLL_D.material(), 1);
                ItemMeta dim = scroll_down.getItemMeta();
                dim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_D"));
                dim.setItemModel(GUIWallFloor.BUTTON_SCROLL_D.key());
                scroll_down.setItemMeta(dim);
                stack[GUIWallFloor.BUTTON_SCROLL_D.slot()] = scroll_down;
            }
        }
        // close
        ItemStack close = new ItemStack(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        gui.setItemModel(GUIMap.BUTTON_CLOSE.key());
        close.setItemMeta(gui);
        stack[53] = close;
        return stack;
    }
}
