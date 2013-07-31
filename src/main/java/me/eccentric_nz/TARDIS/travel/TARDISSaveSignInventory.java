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
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The "Hollywood" sign was among the Earth cultural items the Threshold stole
 * and moved to the town of Wormwood on the Moon. The moon was later destroyed;
 * the sign likely was also.
 *
 * @author eccentric_nz
 */
public class TARDISSaveSignInventory {

    private TARDIS plugin;
    private ItemStack[] terminal;
    private List<Integer> ids = new ArrayList<Integer>();
    int id;
    String home;

    public TARDISSaveSignInventory(TARDIS plugin, int id, String home) {
        ids.addAll(Arrays.asList(new Integer[]{1, 2, 5, 7, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 29, 35, 41, 42, 45, 46, 47, 48, 49, 52, 56, 57, 58, 61, 73, 79, 80, 81, 82, 84, 86, 87, 88, 89, 98, 99, 100, 103, 110, 112, 118, 121, 123, 129, 133, 153, 155}));
        this.plugin = plugin;
        this.id = id;
        this.home = home;
        this.terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Save Sign GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        List<ItemStack> dests = new ArrayList<ItemStack>();
        // home stack
        String[] hh = home.split(":");
        ItemStack his = new ItemStack(ids.get(0), 1);
        ItemMeta him = his.getItemMeta();
        him.setDisplayName("Home");
        List<String> hlore = new ArrayList<String>();
        hlore.add(hh[0]);
        hlore.add(hh[1]);
        hlore.add(hh[2]);
        hlore.add(hh[3]);
        if (hh.length > 4) {
            hlore.add(hh[4]);
        }
        him.setLore(hlore);
        his.setItemMeta(him);
        dests.add(his);
        // saved destinations
        HashMap<String, Object> did = new HashMap<String, Object>();
        did.put("tardis_id", id);
        ResultSetDestinations rsd = new ResultSetDestinations(plugin, did, true);
        int i = 1;
        if (rsd.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsd.getData();
            // cycle through saves
            for (HashMap<String, String> map : data) {
                if (map.get("type").equals("0")) {
                    ItemStack is = new ItemStack(ids.get(i), 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(map.get("dest_name"));
                    List<String> lore = new ArrayList<String>();
                    lore.add(map.get("world"));
                    lore.add(map.get("x"));
                    lore.add(map.get("y"));
                    lore.add(map.get("z"));
                    lore.add(map.get("direction"));
                    im.setLore(lore);
                    is.setItemMeta(im);
                    dests.add(is);
                    i++;
                }
            }
        }

        ItemStack[] stack = new ItemStack[54];
        for (int s = 0; s < 54; s++) {
            if (s < dests.size()) {
                stack[s] = dests.get(s);
            } else {
                stack[s] = null;
            }
        }
        return stack;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
