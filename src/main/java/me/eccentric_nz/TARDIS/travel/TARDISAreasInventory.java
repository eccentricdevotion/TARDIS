/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The "Hollywood" sign was among the Earth cultural items the Threshold stole
 * and moved to the town of Wormwood on the Moon. The moon was later destroyed;
 * the sign likely was also.
 *
 * @author eccentric_nz
 */
public class TARDISAreasInventory {

    private TARDIS plugin;
    private ItemStack[] terminal;
    private List<Integer> ids = new ArrayList<Integer>();
    Player p;

    public TARDISAreasInventory(TARDIS plugin, Player p) {
        ids.addAll(Arrays.asList(new Integer[]{1, 2, 5, 7, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 29, 35, 41, 42, 45, 46, 47, 48, 49, 52, 56, 57, 58, 61, 73, 79, 80, 81, 82, 84, 86, 87, 88, 89, 98, 99, 100, 103, 110, 112, 118, 121, 123, 129, 133, 153, 155}));
        this.plugin = plugin;
        this.p = p;
        this.terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Areas GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        List<ItemStack> areas = new ArrayList<ItemStack>();
        // saved destinations
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, true);
        int i = 0;
        if (rsa.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsa.getData();
            // cycle through areas
            for (HashMap<String, String> map : data) {
                String name = map.get("area_name");
                if (p.hasPermission("tardis.area." + name)) {
                    ItemStack is = new ItemStack(ids.get(i), 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(name);
                    List<String> lore = new ArrayList<String>();
                    lore.add(map.get("world"));
                    im.setLore(lore);
                    is.setItemMeta(im);
                    areas.add(is);
                    i++;
                }
            }
        }

        ItemStack[] stack = new ItemStack[54];
        for (int s = 0; s < 45; s++) {
            if (s < areas.size()) {
                stack[s] = areas.get(s);
            } else {
                stack[s] = null;
            }
        }
        // add button to load TARDIS areas
        ItemStack map = new ItemStack(358, 1);
        ItemMeta switchto = map.getItemMeta();
        switchto.setDisplayName("Load TARDIS saves");
        map.setItemMeta(switchto);
        for (int m = 45; m < 54; m++) {
            if (m == 49) {
                stack[m] = map;
            } else {
                stack[m] = null;
            }
        }
        return stack;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
