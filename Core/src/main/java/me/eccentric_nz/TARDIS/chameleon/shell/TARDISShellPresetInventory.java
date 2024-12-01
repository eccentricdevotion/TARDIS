/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChameleon;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;

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
public class TARDISShellPresetInventory {

    private final ItemStack[] terminal;
    private final TARDIS plugin;
    private final Player player;
    private final int id;


    public TARDISShellPresetInventory(TARDIS plugin, Player player, int id) {
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

        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (!ChameleonPreset.NOT_THESE.contains(preset.getCraftMaterial()) && !preset.usesArmourStand()) {
                if (TARDISPermission.hasPermission(player, "tardis.preset." + preset.toString().toLowerCase(Locale.ROOT))) {
                    ItemStack is = new ItemStack(preset.getGuiDisplay(), 1);
                    ItemMeta im = is.getItemMeta();
                    im.setDisplayName(preset.getDisplayName());
                    is.setItemMeta(im);
                    stacks[preset.getSlot()] = is;
                }
            }
        }
        // load current preset
        ItemStack current = new ItemStack(GUIChameleonPresets.CURRENT.material(), 1);
        ItemMeta pre = current.getItemMeta();
        pre.setDisplayName("Current Chameleon preset");
        pre.setItemModel(GUIChameleonPresets.CURRENT.key());
        current.setItemMeta(pre);
        stacks[GUIChameleonPresets.CURRENT.slot()] = current;
        // saved construct
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
        if (rsc.resultSet()) {
            ItemStack saved = new ItemStack(GUIChameleonPresets.SAVED.material(), 1);
            ItemMeta con = saved.getItemMeta();
            con.setDisplayName("Saved Construct");
            con.setItemModel(GUIChameleonPresets.SAVED.key());
            saved.setItemMeta(con);
            stacks[GUIChameleonPresets.SAVED.slot()] = saved;
        }
        // Cancel / close
        ItemStack close = new ItemStack(GUIChameleonPresets.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setItemModel(GUIChameleonPresets.CLOSE.key());
        close.setItemMeta(can);
        stacks[GUIChameleonPresets.CLOSE.slot()] = close;

        return stacks;
    }

    public ItemStack[] getShells() {
        return terminal;
    }
}
