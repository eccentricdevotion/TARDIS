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
package me.eccentric_nz.TARDIS.autonomous;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIAutonomous;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISAutonomousInventory {

    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] gui;
    private final ItemStack off;

    public TARDISAutonomousInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        off = new ItemStack(Material.LIGHT_GRAY_CARPET, 1);
        ItemMeta offMeta = off.getItemMeta();
        offMeta.setDisplayName(ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
        off.setItemMeta(offMeta);
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Autonomous GUI
     *
     * @return an array of ItemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[36];
        // set unselected
        stack[12] = off;
        stack[13] = off;
        stack[14] = off;
        stack[15] = off;
        stack[30] = off;
        stack[31] = off;
        // set GUI buttons
        for (GUIAutonomous a : GUIAutonomous.values()) {
            ItemStack is = new ItemStack(a.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(a.getName().contains("Selected") ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : a.getName());
            if (a.getCustomModelData() != -1) {
                im.setCustomModelData(a.getCustomModelData());
            }
            if (a.getLore() != null) {
                im.setLore(a.getLore());
            }
            is.setItemMeta(im);
            int slot = (a.getSlot() == -1) ? findSlot(a) : a.getSlot();
            stack[slot] = is;
        }
        return stack;
    }

    private int findSlot(GUIAutonomous a) {
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
        if (rsp.resultSet()) {
            return (a.equals(GUIAutonomous.SELECTED_TYPE)) ? rsp.getAutoType().getSlot() : rsp.getAutoDefault().getSlot();
        }
        // couldn't get preference so return close slot which will be overwritten
        return 35;
    }

    public ItemStack[] getGui() {
        return gui;
    }
}
