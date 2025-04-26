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
package me.eccentric_nz.TARDIS.howto;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the TARDIS revealed that she had archived 30 versions. Once a control room was reconfigured, the TARDIS archived the
 * old design "for neatness". The TARDIS effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
public class TARDISSeedsInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;
    private final Player player;

    public TARDISSeedsInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[45];
        int i = 0;
        // get consoles
        for (Schematic a : Consoles.getBY_NAMES().values()) {
            if (TARDISPermission.hasPermission(player, "tardis." + a.getPermission()) && !a.getSeedMaterial().equals(Material.COBBLESTONE)) {
                Material m = Material.getMaterial(a.getSeed());
                ItemStack is = new ItemStack(m, 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(a.getDescription());
                List<String> lore = new ArrayList<>();
                lore.add("Click to see recipe...");
                im.setLore(lore);
                NamespacedKey key = new NamespacedKey(plugin, "seed_" + a.getPermission());
                is.setItemMeta(im);
                stack[i] = is;
                i++;
            }
        }
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(close_im);
        stack[44] = close;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
