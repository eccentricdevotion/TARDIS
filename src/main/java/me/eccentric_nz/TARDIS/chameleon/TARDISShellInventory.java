/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChameleon;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

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

    private final ItemStack[] terminal;
    private final TARDIS plugin;
    private final Player player;
    private final int id;


    public TARDISShellInventory(TARDIS plugin, Player player, int id) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Shell Loader GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];

        for (PRESET preset : PRESET.values()) {
            if (!PRESET.NOT_THESE.contains(preset.getCraftMaterial()) && !preset.usesItemFrame()) {
                if (TARDISPermission.hasPermission(player, "tardis.preset." + preset.toString().toLowerCase())) {
                    ItemStack is = new ItemStack(preset.getGuiDisplay(), 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(preset.getDisplayName());
                    is.setItemMeta(im);
                    stacks[preset.getSlot()] = is;
                }
            }
        }
        // load current preset
        ItemStack current = new ItemStack(Material.BOWL, 1);
        ItemMeta pre = current.getItemMeta();
        pre.setDisplayName("Current Chameleon preset");
        pre.setCustomModelData(GUIChameleonPresets.CURRENT.getCustomModelData());
        current.setItemMeta(pre);
        stacks[50] = current;
        // saved construct
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
        if (rsc.resultSet()) {
            ItemStack saved = new ItemStack(Material.BOWL, 1);
            ItemMeta con = saved.getItemMeta();
            con.setDisplayName("Saved Construct");
            con.setCustomModelData(GUIChameleonPresets.SAVED.getCustomModelData());
            saved.setItemMeta(con);
            stacks[51] = saved;
        }
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUIChameleonPresets.CLOSE.getCustomModelData());
        close.setItemMeta(can);
        stacks[53] = close;

        return stacks;
    }

    public ItemStack[] getShells() {
        return terminal;
    }
}
