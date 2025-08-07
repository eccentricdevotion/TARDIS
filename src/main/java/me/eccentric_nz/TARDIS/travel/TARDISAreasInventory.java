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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Area;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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
public class TARDISAreasInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player p;
    private final Inventory inventory;

    public TARDISAreasInventory(TARDIS plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS areas", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Areas GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        List<ItemStack> areas = new ArrayList<>();
        // TARDIS areas
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, true, false);
        int i = 0;
        if (rsa.resultSet()) {
            // cycle through areas
            for (Area a : rsa.getData()) {
                String name = a.areaName();
                if (TARDISPermission.hasPermission(p, "tardis.area." + name) || TARDISPermission.hasPermission(p, "tardis.area.*")) {
                    ItemStack is = ItemStack.of(TARDISConstants.GUI_IDS.get(i), 1);
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.text(name));
                    im.lore(List.of(Component.text(a.world())));
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
        ItemStack map = ItemStack.of(Material.MAP, 1);
        ItemMeta switchto = map.getItemMeta();
        switchto.displayName(Component.text("Load TARDIS saves"));
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
}
