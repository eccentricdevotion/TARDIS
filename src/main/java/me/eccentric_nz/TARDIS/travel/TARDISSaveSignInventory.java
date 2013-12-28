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
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
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

    private final TARDIS plugin;
    private final ItemStack[] terminal;
    int id;

    public TARDISSaveSignInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Save Sign GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        List<ItemStack> dests = new ArrayList<ItemStack>();
        // home stack
        ItemStack his = new ItemStack(TARDISConstants.GUI_IDS.get(0), 1);
        ItemMeta him = his.getItemMeta();
        List<String> hlore = new ArrayList<String>();
        HashMap<String, Object> wherehl = new HashMap<String, Object>();
        wherehl.put("tardis_id", id);
        ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
        if (rsh.resultSet()) {
            him.setDisplayName("Home");
            hlore.add(rsh.getWorld().getName());
            hlore.add("" + rsh.getX());
            hlore.add("" + rsh.getY());
            hlore.add("" + rsh.getZ());
            hlore.add(rsh.getDirection().toString());
            if (rsh.isSubmarine()) {
                hlore.add("true");
            }
        } else {
            hlore.add("Not found!");
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
                    if (i < 45) {
                        ItemStack is = new ItemStack(TARDISConstants.GUI_IDS.get(i), 1);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(map.get("dest_name"));
                        List<String> lore = new ArrayList<String>();
                        lore.add(map.get("world"));
                        lore.add(map.get("x"));
                        lore.add(map.get("y"));
                        lore.add(map.get("z"));
                        lore.add(map.get("direction"));
                        lore.add((map.get("submarine").equals("1")) ? "true" : "false");
                        im.setLore(lore);
                        is.setItemMeta(im);
                        dests.add(is);
                        i++;
                    } else {
                        break;
                    }
                }
            }
        }

        ItemStack[] stack = new ItemStack[54];
        for (int s = 0; s < 45; s++) {
            if (s < dests.size()) {
                stack[s] = dests.get(s);
            } else {
                stack[s] = null;
            }
        }
        // add button to load TARDIS areas
        ItemStack map = new ItemStack(358, 1);
        ItemMeta switchto = map.getItemMeta();
        switchto.setDisplayName("Load TARDIS areas");
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
