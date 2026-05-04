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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.database.data.Program;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPrograms;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author eccentric_nz
 */
class HandlesSavedInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final String uuid;
    private final Inventory inventory;

    HandlesSavedInventory(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Saved Programs", NamedTextColor.DARK_RED));
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

        // retrieve the programs from the database
        int i = 0;
        ResultSetPrograms rs = new ResultSetPrograms(plugin, uuid);
        if (rs.resultSet()) {
            for (Program p : rs.getPrograms()) {
                ItemStack is = ItemStack.of(Material.MUSIC_DISC_WARD, 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Handles Program Disk"));
                String checked = (p.isCheckedOut()) ? "Checked OUT" : "Checked IN";
                if (!p.getParsed().isEmpty()) {
                    is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                            Component.text(p.getName()),
                            Component.text(p.getProgram_id()),
                            Component.text(checked),
                            Component.text("Running", NamedTextColor.AQUA)
                    )));
                } else {
                    is.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                            Component.text(p.getName()),
                            Component.text(p.getProgram_id()),
                            Component.text(checked)
                    )));
                }
                is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                        .addHiddenComponents(TARDISConstants.HIDE)
                        .build());
                stack[i] = is;
                i++;
                if (i > 44) {
                    break;
                }
            }
        }
        // back
        ItemStack back = ItemStack.of(Material.ARROW, 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back to editor"));
        stack[45] = back;
        // load button
        ItemStack load = ItemStack.of(Material.BOWL, 1);
        load.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Load selected program in editor"));
        stack[47] = load;
        // deactivate
        ItemStack deactivate = ItemStack.of(Material.BUCKET, 1);
        deactivate.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Deactivate selected program"));
        stack[48] = deactivate;
        // delete
        ItemStack delete = ItemStack.of(Material.BUCKET, 1);
        delete.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Delete selected program"));
        stack[49] = delete;
        // check out
        ItemStack checked = ItemStack.of(Material.BOWL, 1);
        checked.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Check out selected program"));
        stack[51] = checked;
        // close
        stack[53] = GUIItemFactory.close();
        return stack;
    }
}
