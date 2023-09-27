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
package me.eccentric_nz.TARDIS.companionGUI;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUICompanion;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisCompanions;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISCompanionAddInventory {

    private final Player player;
    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] players;

    public TARDISCompanionAddInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        uuid = this.player.getUniqueId();
        players = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        // get current companions
        List<String> comps;
        ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
        if (rs.fromUUID(uuid.toString()) && rs.getCompanions() != null && !rs.getCompanions().isEmpty()) {
            comps = Arrays.asList(rs.getCompanions().split(":"));
        } else {
            comps = new ArrayList<>();
        }
        int i = 0;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (i < 45) {
                UUID puid = p.getUniqueId();
                if (puid != uuid && !comps.contains(puid.toString()) && VanishChecker.canSee(player, p)) {
                    ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1);
                    SkullMeta skull = (SkullMeta) head.getItemMeta();
                    skull.setOwningPlayer(p);
                    skull.setDisplayName(p.getName());
                    ArrayList<String> lore = new ArrayList<>();
                    lore.add(p.getUniqueId().toString());
                    skull.setLore(lore);
                    head.setItemMeta(skull);
                    heads[i] = head;
                    i++;
                }
            }
        }
        // add buttons
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta ii = info.getItemMeta();
        ii.setDisplayName("Info");
        ArrayList<String> info_lore = new ArrayList<>();
        info_lore.add("Click a player head to");
        info_lore.add("add them as a companion.");
        ii.setLore(info_lore);
        ii.setCustomModelData(GUICompanion.INFO.getCustomModelData());
        info.setItemMeta(ii);
        heads[45] = info;
        ItemStack list = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta ll = list.getItemMeta();
        ll.setDisplayName("List companions");
        ll.setCustomModelData(GUICompanion.LIST_COMPANIONS.getCustomModelData());
        list.setItemMeta(ll);
        heads[47] = list;
        ItemStack every = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta one = every.getItemMeta();
        one.setDisplayName("Add all online players");
        one.setCustomModelData(GUICompanion.ALL_COMPANIONS.getCustomModelData());
        every.setItemMeta(one);
        heads[49] = every;
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUICompanion.BUTTON_CLOSE.getCustomModelData());
        close.setItemMeta(can);
        heads[53] = close;

        return heads;
    }

    public ItemStack[] getPlayers() {
        return players;
    }
}
