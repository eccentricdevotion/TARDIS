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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Program;
import me.eccentric_nz.tardis.database.resultset.ResultSetProgram;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TardisHandlesProgramInventory {

    private final TardisPlugin plugin;
    private final int programId;
    private final ItemStack[] handles;

    public TardisHandlesProgramInventory(TardisPlugin plugin, int programId) {
        this.plugin = plugin;
        this.programId = programId;
        handles = getItemStack();
    }

    /**
     * Constructs an inventory for the Handles Programming GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        if (programId != 0) {
            // retrieve the program from the database
            ResultSetProgram rsp = new ResultSetProgram(plugin, programId);
            if (rsp.resultSet()) {
                Program p = rsp.getProgram();
                for (ItemStack is : p.getInventory()) {
                    stack[i] = is;
                    i++;
                }
            }
        }
        i = 36;
        for (TardisHandlesBlock b : TardisHandlesBlock.getButtons()) {
            ItemStack is = new ItemStack(Material.BOWL, 1);
            ItemMeta im = is.getItemMeta();
            assert im != null;
            im.setDisplayName(b.getDisplayName());
            im.setCustomModelData(b.getCustomModelData());
            is.setItemMeta(im);
            stack[i] = is;
            i++;
            if (i == 45) {
                i = 52;
            }
        }

        i = 45;
        for (TardisHandlesBlock b : TardisHandlesBlock.getControls()) {
            ItemStack is = new ItemStack(Material.PAPER, 1);
            ItemMeta im = is.getItemMeta();
            assert im != null;
            im.setDisplayName(b.getDisplayName());
            if (b.getLore() != null) {
                im.setLore(b.getLore());
            }
            im.setCustomModelData(b.getCustomModelData());
            is.setItemMeta(im);
            stack[i] = is;
            i++;
            if (i > 51) {
                break;
            }
        }
        return stack;
    }

    public ItemStack[] getHandles() {
        return handles;
    }
}
