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
package me.eccentric_nz.TARDIS.transmat;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUITransmat;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmatList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TARDISTransmatInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;
    private final int id;

    public TARDISTransmatInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
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
                assert im != null;
                im.setCustomModelData(4);
                im.setDisplayName(t.getName());
                List<String> lore = new ArrayList<>();
                lore.add(String.format("X: %.2f", t.getX()));
                lore.add(String.format("Y: %.2f", t.getY()));
                lore.add(String.format("Z: %.2f", t.getZ()));
                lore.add(String.format("Yaw: %.2f", t.getYaw()));
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
            ItemStack info = new ItemStack(GUITransmat.INFO.getMaterial(), 1);
            ItemMeta meta = info.getItemMeta();
            assert meta != null;
            meta.setDisplayName(plugin.getChameleonGuis().getString("INFO"));
            meta.setCustomModelData(GUITransmat.INFO.getCustomModelData());
            meta.setLore(plugin.getChameleonGuis().getStringList("INFO_TRANSMAT"));
            info.setItemMeta(meta);
            stack[17] = info;
            // teleport
            ItemStack tele = new ItemStack(GUITransmat.TRANSMAT.getMaterial(), 1);
            ItemMeta port = tele.getItemMeta();
            assert port != null;
            port.setDisplayName(plugin.getLanguage().getString("BUTTON_TRANSMAT"));
            port.setCustomModelData(GUITransmat.TRANSMAT.getCustomModelData());
            tele.setItemMeta(port);
            stack[17] = tele;
            // delete
            ItemStack delete = new ItemStack(GUITransmat.DELETE.getMaterial(), 1);
            ItemMeta dim = delete.getItemMeta();
            assert dim != null;
            dim.setDisplayName(plugin.getLanguage().getString("BUTTON_DELETE"));
            dim.setCustomModelData(GUITransmat.DELETE.getCustomModelData());
            delete.setItemMeta(dim);
            stack[35] = delete;
        }
        // close
        ItemStack close = new ItemStack(GUITransmat.CLOSE.getMaterial(), 1);
        ItemMeta close_im = close.getItemMeta();
        assert close_im != null;
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUITransmat.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[53] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
