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
package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUICompanion;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionInventory {

    private final TARDIS plugin;
    private final ItemStack[] skulls;
    private final String[] companionData;

    public TARDISCompanionInventory(TARDIS plugin, String[] companionData) {
        this.plugin = plugin;
        this.companionData = companionData;
        skulls = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        int i = 0;
        for (String c : companionData) {
            if (!c.isEmpty() && i < 45) {
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(UUID.fromString(c));
                ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta skull = (SkullMeta) head.getItemMeta();
                skull.setOwningPlayer(op);
                skull.setDisplayName(op.getName());
                ArrayList<String> lore = new ArrayList<>();
                lore.add(c);
                skull.setLore(lore);
                head.setItemMeta(skull);
                heads[i] = head;
                i++;
            }
        }
        // add buttons
        ItemStack info = new ItemStack(GUICompanion.INFO.material(), 1);
        ItemMeta ii = info.getItemMeta();
        ii.setDisplayName("Info");
        ArrayList<String> info_lore = new ArrayList<>();
        info_lore.add("To REMOVE a companion");
        info_lore.add("select a player head");
        info_lore.add("then click the Remove");
        info_lore.add("button (bucket).");
        info_lore.add("To ADD a companion");
        info_lore.add("click the Add button");
        info_lore.add("(nether star).");
        ii.setLore(info_lore);
        info.setItemMeta(ii);
        heads[GUICompanion.INFO.slot()] = info;
        ItemStack add = new ItemStack(GUICompanion.ADD_COMPANION.material(), 1);
        ItemMeta aa = add.getItemMeta();
        aa.setDisplayName("Add");
        add.setItemMeta(aa);
        heads[GUICompanion.ADD_COMPANION.slot()] = add;
        ItemStack del = new ItemStack(GUICompanion.DELETE_COMPANION.material(), 1);
        ItemMeta dd = del.getItemMeta();
        dd.setDisplayName("Remove");
        del.setItemMeta(dd);
        heads[GUICompanion.DELETE_COMPANION.slot()] = del;
        // Cancel / close
        ItemStack close = new ItemStack(GUICompanion.BUTTON_CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);
        heads[GUICompanion.BUTTON_CLOSE.slot()] = close;

        return heads;
    }

    public ItemStack[] getSkulls() {
        return skulls;
    }
}
