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
package me.eccentric_nz.tardis.companiongui;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.custommodeldata.GuiCompanion;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisCompanions;
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
public class TardisCompanionAddInventory {

    private final Player player;
    private final TardisPlugin plugin;
    private final UUID uuid;
    private final ItemStack[] players;

    public TardisCompanionAddInventory(TardisPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        uuid = this.player.getUniqueId();
        players = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] heads = new ItemStack[54];
        // get current companions
        ResultSetTardisCompanions rs = new ResultSetTardisCompanions(plugin);
        if (rs.fromUuid(uuid.toString())) {
            List<String> comps;
            if (rs.getCompanions() != null && !rs.getCompanions().isEmpty()) {
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
                        assert skull != null;
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
        }
        // add buttons
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta ii = info.getItemMeta();
        assert ii != null;
        ii.setDisplayName("Info");
        ArrayList<String> info_lore = new ArrayList<>();
        info_lore.add("Click a player head to");
        info_lore.add("add them as a companion.");
        ii.setLore(info_lore);
        info.setItemMeta(ii);
        heads[45] = info;
        ItemStack list = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta ll = list.getItemMeta();
        assert ll != null;
        ll.setDisplayName("List companions");
        list.setItemMeta(ll);
        heads[47] = list;
        ItemStack every = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta one = every.getItemMeta();
        assert one != null;
        one.setDisplayName("Add all online players");
        every.setItemMeta(one);
        heads[49] = every;
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        assert can != null;
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GuiCompanion.BUTTON_CLOSE.getCustomModelData());
        close.setItemMeta(can);
        heads[53] = close;

        return heads;
    }

    public ItemStack[] getPlayers() {
        return players;
    }
}
