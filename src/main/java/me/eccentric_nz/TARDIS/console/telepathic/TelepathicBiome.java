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
package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.custommodels.GUIWallFloor;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.block.Biome;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TelepathicBiome implements InventoryHolder {

    private final TARDIS plugin;
    private final int id;
    private final Inventory inventory;

    public TelepathicBiome(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Telepathic Biome Finder", NamedTextColor.DARK_RED));
        this.inventory.setContents(getButtons());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getButtons() {
        ItemStack[] stack = new ItemStack[54];
        // only show biomes for the environment the TARDIS is currently in
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            Environment environment = rsc.getCurrent().location().getWorld().getEnvironment();
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
                Material material = EnvironmentBiomes.BIOME_BLOCKS.get(biome.getKey().getKey());
                if (material != null) {
                    ItemStack is = ItemStack.of(material, 1);
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.text(TARDISStringUtils.capitalise(biome.getKey().getKey())));
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
                ItemStack scroll_up = ItemStack.of(GUIWallFloor.BUTTON_SCROLL_U.material(), 1);
                ItemMeta uim = scroll_up.getItemMeta();
                uim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_U", "Scroll up")));
                uim.setItemModel(GUIWallFloor.BUTTON_SCROLL_U.key());
                scroll_up.setItemMeta(uim);
                stack[GUIWallFloor.BUTTON_SCROLL_U.slot()] = scroll_up;
                // scroll down
                ItemStack scroll_down = ItemStack.of(GUIWallFloor.BUTTON_SCROLL_D.material(), 1);
                ItemMeta dim = scroll_down.getItemMeta();
                dim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_D", "Scroll down")));
                dim.setItemModel(GUIWallFloor.BUTTON_SCROLL_D.key());
                scroll_down.setItemMeta(dim);
                stack[GUIWallFloor.BUTTON_SCROLL_D.slot()] = scroll_down;
            }
        }
        // close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        gui.setItemModel(GUIMap.BUTTON_CLOSE.key());
        close.setItemMeta(gui);
        stack[53] = close;
        return stack;
    }
}
