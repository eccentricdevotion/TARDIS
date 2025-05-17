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
package me.eccentric_nz.TARDIS.transmat;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUITransmat;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmatList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISTransmatInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;
    private final int id;
    private final Player player;

    public TARDISTransmatInventory(TARDIS plugin, int id, Player player) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get transmat locations
        ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
        if (rslist.resultSet()) {
            for (Transmat t : rslist.getData()) {
                if (i > 52) {
                    break;
                }
                ItemStack is = new ItemStack(Material.MAP, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(t.name());
                List<String> lore = new ArrayList<>();
                lore.add(String.format("X: %.2f", t.x()));
                lore.add(String.format("Y: %.2f", t.y()));
                lore.add(String.format("Z: %.2f", t.z()));
                lore.add(String.format("Yaw: %.2f", t.yaw()));
                im.setLore(lore);
                is.setItemMeta(im);
                stack[i] = is;
                if (i % 9 == 7) {
                    i += 2;
                } else {
                    i++;
                }
            }
            // info
            ItemStack info = new ItemStack(GUITransmat.INFO.material(), 1);
            ItemMeta meta = info.getItemMeta();
            meta.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
            meta.setLore(plugin.getChameleonGuis().getStringList("INFO_TRANSMAT"));
            info.setItemMeta(meta);
            stack[GUITransmat.INFO.slot()] = info;
            // delete
            ItemStack delete = new ItemStack(GUITransmat.DELETE.material(), 1);
            ItemMeta dim = delete.getItemMeta();
            dim.setDisplayName(plugin.getLanguage().getString("BUTTON_DELETE"));
            delete.setItemMeta(dim);
            stack[GUITransmat.DELETE.slot()] = delete;
        }
        // teleport
        ItemStack tele = new ItemStack(GUITransmat.TRANSMAT.material(), 1);
        ItemMeta port = tele.getItemMeta();
        port.setDisplayName(plugin.getLanguage().getString("BUTTON_TRANSMAT"));
        tele.setItemMeta(port);
        stack[GUITransmat.TRANSMAT.slot()] = tele;
        // rooms world
        if (plugin.getPlanetsConfig().getBoolean("planets.rooms.enabled") && plugin.getServer().getWorld("rooms") != null && TARDISPermission.hasPermission(player, "tardis.transmat.rooms")) {
            ItemStack rooms = new ItemStack(GUITransmat.ROOMS.material(), 1);
            ItemMeta world = rooms.getItemMeta();
            world.setDisplayName("Rooms World");
            rooms.setItemMeta(world);
            stack[GUITransmat.ROOMS.slot()] = rooms;
        }
        // close
        ItemStack close = new ItemStack(GUITransmat.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(close_im);
        stack[GUITransmat.CLOSE.slot()] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
