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
package me.eccentric_nz.TARDIS.playerprefs;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIInteriorSounds;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.enumeration.Hum;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

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
            is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(hum.toString()));
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
        play.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Action"));
        play.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text("PLAY")).build());
        stack[GUIInteriorSounds.ACTION.slot()] = play;
        // close
        stack[GUIInteriorSounds.CLOSE.slot()] = GUIItemFactory.close();

        return stack;
    }
}
