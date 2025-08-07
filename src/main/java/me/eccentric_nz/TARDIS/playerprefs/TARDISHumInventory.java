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
package me.eccentric_nz.TARDIS.playerprefs;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIInteriorSounds;
import me.eccentric_nz.TARDIS.enumeration.Hum;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that planet.
 *
 * @author eccentric_nz
 */
class TARDISHumInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    TARDISHumInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 18, Component.text("TARDIS Interior Sounds", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        List<ItemStack> options = new ArrayList<>();
        // get HUM sounds
        for (Hum hum : Hum.values()) {
            ItemStack is = ItemStack.of(Material.BOWL, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(hum.toString()));
            is.setItemMeta(im);
            options.add(is);
        }
        // add to stack
        ItemStack[] stack = new ItemStack[18];
        for (int s = 0; s < 17; s++) {
            if (s < options.size()) {
                stack[s] = options.get(s);
            } else {
                stack[s] = null;
            }
        }
        // play / save
        ItemStack play = ItemStack.of(GUIInteriorSounds.ACTION.material(), 1);
        ItemMeta save = play.getItemMeta();
        save.displayName(Component.text("Action"));
        save.lore(List.of(Component.text("PLAY")));
        play.setItemMeta(save);
        stack[GUIInteriorSounds.ACTION.slot()] = play;
        // close
        ItemStack close = ItemStack.of(GUIInteriorSounds.CLOSE.material(), 1);
        ItemMeta c_im = close.getItemMeta();
        c_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(c_im);
        stack[GUIInteriorSounds.CLOSE.slot()] = close;

        return stack;
    }
}
