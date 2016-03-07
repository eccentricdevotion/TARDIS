/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISCompanionAddInventory {

    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] players;

    public TARDISCompanionAddInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.players = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        // get current companions
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            List<String> comps = Arrays.asList(rs.getCompanions().split(":"));
            int i = 0;
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (i < 45) {
                    UUID puid = p.getUniqueId();
                    if (puid != uuid && !comps.contains(puid.toString())) {
                        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                        SkullMeta skull = (SkullMeta) head.getItemMeta();
                        skull.setOwner(p.getName());
                        skull.setDisplayName(p.getName());
                        ArrayList<String> lore = new ArrayList<String>();
                        lore.add(p.getUniqueId().toString());
                        skull.setLore(lore);
                        head.setItemMeta(skull);
                        heads[i] = head;
                        i++;
                    }
                }
            }
        }
        // add buttons
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta ii = info.getItemMeta();
        ii.setDisplayName("Info");
        ArrayList<String> info_lore = new ArrayList<String>();
        info_lore.add("Click a player head to");
        info_lore.add("add them as a companion.");
        ii.setLore(info_lore);
        info.setItemMeta(ii);
        heads[45] = info;
        ItemStack list = new ItemStack(Material.BOOK_AND_QUILL, 1);
        ItemMeta ll = list.getItemMeta();
        ll.setDisplayName("List companions");
        list.setItemMeta(ll);
        heads[47] = list;
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);
        heads[53] = close;

        return heads;
    }

    public ItemStack[] getPlayers() {
        return players;
    }

}
