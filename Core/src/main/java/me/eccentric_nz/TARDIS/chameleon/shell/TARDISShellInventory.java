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
package me.eccentric_nz.TARDIS.chameleon.shell;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetShells;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A TARDIS with a functioning chameleon circuit can appear as almost anything desired. The owner can program the
 * circuit to make it assume a specific shape. If no appearance is specified, the TARDIS automatically choses its own
 * shape. When a TARDIS materialises in a new location, within the first nanosecond of landing, its chameleon circuit
 * analyses the surrounding area, calculates a twelve-dimensional data map of all objects within a thousand-mile radius
 * and then determines which outer shell will best blend in with the environment. According to the Eleventh Doctor, the
 * TARDIS would perform these functions, but then disguise itself as a 1960s era police box anyway.
 *
 * @author eccentric_nz
 */
public class TARDISShellInventory {

    private final ItemStack[] shells;
    private final TARDIS plugin;
    private final int id;


    public TARDISShellInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        shells = getItemStack();
    }

    /**
     * Constructs an inventory for the Shell Loader GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];

        // saved construct
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ResultSetShells rss = new ResultSetShells(plugin, wherec);
        if (rss.resultSet()) {
            int i = 0;
            ArrayList<HashMap<String, String>> data = rss.getData();
            for (HashMap<String, String> map : data) {
                // get the first non-air block of the shell
                Material material = null;
                String blueprint = map.get("blueprintData");
                JsonArray json = JsonParser.parseString(blueprint).getAsJsonArray();
                outer:
                for (int k = 0; k < 10; k++) {
                    JsonArray inner = json.get(k).getAsJsonArray();
                    for (int j = 0; j < 4; j++) {
                        String block = inner.get(j).getAsString();
                        if (!block.equals("minecraft:air")) {
                            BlockData blockData = plugin.getServer().createBlockData(block);
                            material = blockData.getMaterial();
                            break outer;
                        }
                    }
                }
                if (material == null) {
                    material = GUIChameleonPresets.SAVED.material();
                }
                ItemStack saved = new ItemStack(material, 1);
                ItemMeta con = saved.getItemMeta();
                con.setDisplayName("Saved Construct");
                List<String> lore = new ArrayList<>();
                lore.add(map.get("line1"));
                lore.add(map.get("line2"));
                lore.add(map.get("line3"));
                lore.add(map.get("line4"));
                if (map.get("active").equals("1")) {
                    lore.add(ChatColor.AQUA + "Active shell");
                }
                con.setLore(lore);
                con.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, TARDISNumberParsers.parseInt(map.get("chameleon_id")));
                saved.setItemMeta(con);
                stacks[i] = saved;
                i++;
                // only first 5 rows
                if (i > 44) {
                    break;
                }
            }
        }
        // use selected shell
        ItemStack use = new ItemStack(GUIChameleonPresets.USE_SELECTED.material(), 1);
        ItemMeta uim = use.getItemMeta();
        uim.setDisplayName("Use selected shell");
        uim.setLore(List.of("Will apply shell to", "the Chameleon Circuit", "and rebuild the exterior."));
        use.setItemMeta(uim);
        stacks[GUIChameleonPresets.USE_SELECTED.slot()] = use;
        // delete selected shell
        ItemStack delete = new ItemStack(GUIChameleonPresets.DELETE_SELECTED.material(), 1);
        ItemMeta dim = delete.getItemMeta();
        dim.setDisplayName("Delete selected shell");
        delete.setItemMeta(dim);
        stacks[GUIChameleonPresets.DELETE_SELECTED.slot()] = delete;
        // update selected shell
        ItemStack update = new ItemStack(GUIChameleonPresets.UPDATE_SELECTED.material(), 1);
        ItemMeta upim = update.getItemMeta();
        upim.setDisplayName("Update selected shell");
        update.setItemMeta(upim);
        stacks[GUIChameleonPresets.UPDATE_SELECTED.slot()] = update;
        // clear shell on platform
        ItemStack newShell = new ItemStack(GUIChameleonPresets.NEW.material(), 1);
        ItemMeta ns = newShell.getItemMeta();
        ns.setDisplayName("New Chameleon shell");
        ns.setLore(List.of("Will clear the shell platform", "ready for building."));
        newShell.setItemMeta(ns);
        stacks[GUIChameleonPresets.NEW.slot()] = newShell;
        // Save current shell on platform
        ItemStack save = new ItemStack(GUIChameleonPresets.SAVE.material(), 1);
        ItemMeta pre = save.getItemMeta();
        pre.setDisplayName("Save Chameleon shell");
        ns.setLore(List.of("Will save shell and", "rebuild the exterior."));
        save.setItemMeta(pre);
        stacks[GUIChameleonPresets.SAVE.slot()] = save;
        // Cancel / close
        ItemStack close = new ItemStack(GUIChameleonPresets.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);
        stacks[GUIChameleonPresets.CLOSE.slot()] = close;

        return stacks;
    }

    public ItemStack[] getPlayerShells() {
        return shells;
    }
}
