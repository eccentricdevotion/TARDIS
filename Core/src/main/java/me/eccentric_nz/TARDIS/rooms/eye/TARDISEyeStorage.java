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
package me.eccentric_nz.TARDIS.rooms.eye;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIArtronStorage;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISEyeStorage {

    private final TARDIS plugin;

    public TARDISEyeStorage(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack[] getGUI(int id) {
        ItemStack[] stacks = new ItemStack[9];
        // info
        ItemStack info = new ItemStack(GUIArtronStorage.INFO.material(), 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(List.of("Increase the Artron storage", "capacity by placing", "up to 5 Artron Capacitors", "in the slots to the right."));
        info.setItemMeta(info_im);
        stacks[GUIArtronStorage.INFO.slot()] = info;
        // right arrow
        ItemStack r_arrow = new ItemStack(GUIArtronStorage.ARROW_RIGHT.material(), 1);
        ItemMeta r_arrow_im = r_arrow.getItemMeta();
        r_arrow_im.setDisplayName(ChatColor.WHITE + "");
        r_arrow.setItemMeta(r_arrow_im);
        stacks[GUIArtronStorage.ARROW_RIGHT.slot()] = r_arrow;
        ResultSetArtronStorage rs = new ResultSetArtronStorage(plugin);
        if (rs.fromID(id)) {
            int count = rs.getCapacitorCount();
            int damaged = rs.getDamageCount();
            // capacitors
            for (int i = 2; i < 2 + count; i++) {
                ItemStack is = new ItemStack(Material.BUCKET, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(ChatColor.WHITE + (i > (2 + count) - damaged ? "Damaged Artron Capacitor" : "Artron Capacitor"));
                is.setItemMeta(im);
                stacks[i] = is;
            }
        }
        // left arrow
        ItemStack l_arrow = new ItemStack(GUIArtronStorage.ARROW_LEFT.material(), 1);
        ItemMeta l_arrow_im = l_arrow.getItemMeta();
        l_arrow_im.setDisplayName(ChatColor.WHITE + " ");
        l_arrow.setItemMeta(l_arrow_im);
        stacks[GUIArtronStorage.ARROW_LEFT.slot()] = l_arrow;
        // close
        ItemStack close = new ItemStack(GUIArtronStorage.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);
        stacks[GUIArtronStorage.CLOSE.slot()] = close;
        return stacks;
    }
}
