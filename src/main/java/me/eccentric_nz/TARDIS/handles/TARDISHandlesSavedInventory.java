/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.custommodels.GUISavedPrograms;
import me.eccentric_nz.TARDIS.custommodels.keys.DiskVariant;
import me.eccentric_nz.TARDIS.database.data.Program;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPrograms;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISHandlesSavedInventory {

    private final TARDIS plugin;
    private final String uuid;
    private final ItemStack[] programs;

    TARDISHandlesSavedInventory(TARDIS plugin, String uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        programs = getItemStack();
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
                ItemStack is = new ItemStack(Material.MUSIC_DISC_WARD, 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text("Handles Program Disk"));
                String checked = (p.isCheckedOut()) ? "Checked OUT" : "Checked IN";
                if (!p.getParsed().isEmpty()) {
                    im.lore(List.of(
                            Component.text(p.getName()),
                            Component.text(p.getProgram_id()),
                            Component.text(checked),
                            Component.text().color(NamedTextColor.AQUA).append(Component.text("Running")).build()
                    ));
                } else {
                    im.lore(List.of(
                            Component.text(p.getName()),
                            Component.text(p.getProgram_id()),
                            Component.text(checked)
                    ));
                }
                im.addItemFlags(ItemFlag.values());
                im.setItemModel(DiskVariant.HANDLES_DISK.getKey());
                is.setItemMeta(im);
                stack[i] = is;
                i++;
                if (i > 44) {
                    break;
                }
            }
        }
        // back
        ItemStack back = new ItemStack(Material.ARROW, 1);
        ItemMeta bk = back.getItemMeta();
        bk.displayName(Component.text("Back to editor"));
        bk.setItemModel(GUISavedPrograms.BACK_TO_EDITOR.getModel());
        back.setItemMeta(bk);
        stack[45] = back;
        // load button
        ItemStack load = new ItemStack(Material.BOWL, 1);
        ItemMeta ld = load.getItemMeta();
        ld.displayName(Component.text("Load selected program in editor"));
        ld.setItemModel(GUISavedPrograms.LOAD_SELECTED_PROGRAM_IN_EDITOR.getModel());
        load.setItemMeta(ld);
        stack[47] = load;
        // deactivate
        ItemStack deactivate = new ItemStack(Material.BUCKET, 1);
        ItemMeta dem = deactivate.getItemMeta();
        dem.displayName(Component.text("Deactivate selected program"));
        dem.setItemModel(GUISavedPrograms.DEACTIVATE_SELECTED_PROGRAM.getModel());
        deactivate.setItemMeta(dem);
        stack[48] = deactivate;
        // delete
        ItemStack delete = new ItemStack(Material.BUCKET, 1);
        ItemMeta dm = delete.getItemMeta();
        dm.displayName(Component.text("Delete selected program"));
        dm.setItemModel(GUISavedPrograms.DELETE_SELECTED_PROGRAM.getModel());
        delete.setItemMeta(dm);
        stack[49] = delete;
        // check out
        ItemStack checked = new ItemStack(Material.BOWL, 1);
        ItemMeta km = checked.getItemMeta();
        km.displayName(Component.text("Check out selected program"));
        km.setItemModel(GUISavedPrograms.CHECK_OUT_SELECTED_PROGRAM.getModel());
        checked.setItemMeta(km);
        stack[51] = checked;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta cm = close.getItemMeta();
        cm.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE")));
        cm.setItemModel(GUISavedPrograms.CLOSE.getModel());
        close.setItemMeta(cm);
        stack[53] = close;
        return stack;
    }

    public ItemStack[] getPrograms() {
        return programs;
    }
}
