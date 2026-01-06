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
package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Program;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetProgram;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author eccentric_nz
 */
public class HandlesProgramInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int program_id;
    private final Inventory inventory;

    public HandlesProgramInventory(TARDIS plugin, int program_id) {
        this.plugin = plugin;
        this.program_id = program_id;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Handles Program", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
        for (HandlesBlock b : HandlesBlock.getButtons()) {
            ItemStack is = ItemStack.of(Material.BOWL, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(b.getDisplayName()));
            is.setItemMeta(im);
            stack[i] = is;
            i++;
            if (i == 45) {
                i = 52;
            }
        }

        i = 45;
        for (HandlesBlock b : HandlesBlock.getControls()) {
            ItemStack is = ItemStack.of(Material.PAPER, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(b.getDisplayName()));
            if (b.getLore() != null) {
                im.lore(b.getLore());
            }
            is.setItemMeta(im);
            stack[i] = is;
            i++;
            if (i > 51) {
                break;
            }
        }
        return stack;
    }
}
