/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.advanced;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.enumeration.STORAGE;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAreaDisks {

    private final TARDIS plugin;

    public TARDISAreaDisks(TARDIS plugin) {
        this.plugin = plugin;
    }

    public ItemStack[] makeDisks(Player p) {

        List<ItemStack> areas = new ArrayList<ItemStack>();
        // get the areas this player has access to
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, true);
        if (rsa.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsa.getData();
            // cycle through areas
            for (HashMap<String, String> map : data) {
                String name = map.get("area_name");
                if (p.hasPermission("tardis.area." + name) || p.hasPermission("tardis.area.*")) {
                    ItemStack is = new ItemStack(Material.RECORD_3, 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName("Area Storage Disk");
                    List<String> lore = new ArrayList<String>();
                    lore.add(name);
                    lore.add(map.get("world"));
                    im.setLore(lore);
                    is.setItemMeta(im);
                    areas.add(is);
                }
            }
        }
        ItemStack[] stack = new ItemStack[54];
        // TODO set default top slots
        try {
            stack = TARDISSerializeInventory.itemStacksFromString(STORAGE.AREA.getEmpty());
        } catch (IOException ex) {
            Logger.getLogger(TARDISAreaDisks.class.getName()).log(Level.SEVERE, null, ex);
        }
        // set saved slots
        int i = 27;
        for (ItemStack st : areas) {
            stack[i] = st;
            i++;
        }
        return stack;
    }
}
