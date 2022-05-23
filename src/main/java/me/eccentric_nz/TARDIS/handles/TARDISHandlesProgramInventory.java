/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Program;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetProgram;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class TARDISHandlesProgramInventory {

    private final TARDIS plugin;
    private final int program_id;
    private final ItemStack[] handles;

    public TARDISHandlesProgramInventory(TARDIS plugin, int program_id) {
        this.plugin = plugin;
        this.program_id = program_id;
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
        if (program_id != 0) {
            // retrieve the program from the database
            ResultSetProgram rsp = new ResultSetProgram(plugin, program_id);
            if (rsp.resultSet()) {
                Program p = rsp.getProgram();
                for (ItemStack is : p.getInventory()) {
                    stack[i] = is;
                    i++;
                }
            }
        }
        i = 36;
        for (TARDISHandlesBlock b : TARDISHandlesBlock.getButtons()) {
            ItemStack is = new ItemStack(Material.BOWL, 1);
            ItemMeta im = is.getItemMeta();
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
        for (TARDISHandlesBlock b : TARDISHandlesBlock.getControls()) {
            ItemStack is = new ItemStack(Material.PAPER, 1);
            ItemMeta im = is.getItemMeta();
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
