/*
 * Copyright (C) 2026 eccentric_nz
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
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
public class ShellPresetInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final Inventory inventory;

    public ShellPresetInventory(TARDIS plugin, Player player, int id) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Shell Loader", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
                    ItemStack is = ItemStack.of(preset.getGuiDisplay(), 1);
                    ItemMeta im = is.getItemMeta();
                    im.displayName(Component.text(preset.getDisplayName()));
                    is.setItemMeta(im);
                    stacks[preset.getSlot()] = is;
                }
            }
        }
        // load current preset
        ItemStack current = ItemStack.of(GUIChameleonPresets.CURRENT.material(), 1);
        ItemMeta pre = current.getItemMeta();
        pre.displayName(Component.text("Current Chameleon preset"));
        current.setItemMeta(pre);
        stacks[GUIChameleonPresets.CURRENT.slot()] = current;
        // saved construct
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
        if (rsc.resultSet()) {
            ItemStack saved = ItemStack.of(GUIChameleonPresets.SAVED.material(), 1);
            ItemMeta con = saved.getItemMeta();
            con.displayName(Component.text("Saved Construct"));
            saved.setItemMeta(con);
            stacks[GUIChameleonPresets.SAVED.slot()] = saved;
        }
        // Cancel / close
        ItemStack close = ItemStack.of(GUIChameleonPresets.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        stacks[GUIChameleonPresets.CLOSE.slot()] = close;

        return stacks;
    }
}
