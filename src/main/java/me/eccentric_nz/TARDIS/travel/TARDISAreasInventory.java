/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUIArea;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The "Hollywood" sign was among the Earth cultural items the Threshold stole and moved to the town of Wormwood on the
 * Moon. The moon was later destroyed; the sign likely was also.
 *
 * @author eccentric_nz
 */
public class TARDISAreasInventory {

    private final TARDIS plugin;
    private final ItemStack[] terminal;
    private final Player p;

    public TARDISAreasInventory(TARDIS plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
        terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Areas GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        List<ItemStack> areas = new ArrayList<>();
        // saved destinations
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, true, false);
        int i = 0;
        if (rsa.resultSet()) {
            // cycle through areas
            for (Area a : rsa.getData()) {
                String name = a.getAreaName();
                if (TARDISPermission.hasPermission(p, "tardis.area." + name) || TARDISPermission.hasPermission(p, "tardis.area.*")) {
                    ItemStack is = new ItemStack(TARDISConstants.GUI_IDS.get(i), 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(name);
                    List<String> lore = new ArrayList<>();
                    lore.add(a.getWorld());
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
        ItemStack map = new ItemStack(Material.MAP, 1);
        ItemMeta switchto = map.getItemMeta();
        switchto.setDisplayName("Load TARDIS saves");
        switchto.setCustomModelData(GUIArea.LOAD_TARDIS_SAVES.getCustomModelData());
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
