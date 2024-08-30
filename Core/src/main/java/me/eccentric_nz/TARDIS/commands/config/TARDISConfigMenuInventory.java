/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIConfiguration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that planet.
 *
 * @author eccentric_nz
 */
public class TARDISConfigMenuInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;

    public TARDISConfigMenuInventory(TARDIS plugin) {
        this.plugin = plugin;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Admin Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        List<ItemStack> options = new ArrayList<>();
        Set<String> config = new TreeSet<>(plugin.getConfig().getKeys(true));
        config.forEach((c) -> {
            String value = plugin.getConfig().getString(c);
            if ((value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false"))
                    && !c.startsWith("abandon")
                    && !c.startsWith("arch")
                    && !c.startsWith("autonomous_areas")
                    && !c.startsWith("blueprints")
                    && !c.startsWith("circuits")
                    && !c.startsWith("conversions")
                    && !c.startsWith("creation")
                    && !c.startsWith("debug")
                    && !c.startsWith("desktop")
                    && !c.startsWith("mapping")
                    && !c.startsWith("junk")
                    && !c.startsWith("modules")
                    && !c.startsWith("siege")
                    && !c.startsWith("sonic")
                    && !c.startsWith("storage")
                    && !c.startsWith("travel")
                    && !c.startsWith("worlds")
            ) {
                ItemStack is = new ItemStack(Material.REPEATER, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(c);
                int cmd = GUIConfiguration.valueOf(c.split("\\.")[0].toUpperCase(Locale.ROOT)).getCustomModelData();
                if (value.equals("false")) {
                    cmd += 100; // xx -> 1xx
                }
                im.setCustomModelData(cmd);
                im.setLore(List.of(value));
                is.setItemMeta(im);
                options.add(is);
            }
        });
        ItemStack[] stack = new ItemStack[54];
        for (int s = 0; s < 52; s++) {
            if (s < options.size()) {
                stack[s] = options.get(s);
            } else {
                stack[s] = null;
            }
        }
        // next page
        ItemStack next = new ItemStack(Material.BOWL, 1);
        ItemMeta page = next.getItemMeta();
        page.setDisplayName("Next page");
        page.setCustomModelData(GUIConfiguration.NEXT.getCustomModelData());
        next.setItemMeta(page);
        stack[52] = next;
        // player prefs
        ItemStack play = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta prefs = play.getItemMeta();
        prefs.setDisplayName("Player Preferences");
        prefs.setCustomModelData(GUIConfiguration.PREFS.getCustomModelData());
        play.setItemMeta(prefs);
        stack[53] = play;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
