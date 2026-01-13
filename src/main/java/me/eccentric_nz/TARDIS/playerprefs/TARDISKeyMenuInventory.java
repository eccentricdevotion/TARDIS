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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIKeyPreferences;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISKeyMenuInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISKeyMenuInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("TARDIS Key Prefs Menu", NamedTextColor.DARK_RED));
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
        ItemStack[] itemStacks = new ItemStack[27];
        Material material;
        try {
            material = Material.valueOf(plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            material = Material.GOLD_NUGGET;
        }
        for (GUIKeyPreferences key : GUIKeyPreferences.values()) {
            ItemStack is;
            ItemMeta im;
            if (key == GUIKeyPreferences.CLOSE || key == GUIKeyPreferences.INSTRUCTIONS || key == GUIKeyPreferences.NAME || key == GUIKeyPreferences.DISPLAY_NAME_COLOUR) {
                is = ItemStack.of(key.getMaterial(), 1);
                im = is.getItemMeta();
                im.displayName(Component.text(key.getName()));
            } else {
                is = ItemStack.of(material);
                im = is.getItemMeta();
                im.displayName(ComponentUtils.toWhite("TARDIS Key"));
            }
            if (!key.getLore().isEmpty()) {
                if (key.getLore().contains("~")) {
                    String[] split = key.getLore().split("~");
                    List<Component> components = new ArrayList<>();
                    for (String s : split) {
                        components.add(Component.text(s));
                    }
                    im.lore(components);
                } else {
                    im.lore(List.of(Component.text(key.getLore())));
                }
            }
            if (key.getSlot() < 17) {
                try {
                    KeyVariant variant = KeyVariant.valueOf(key.toString());
                    CustomModelDataComponent component = im.getCustomModelDataComponent();
                    component.setFloats(variant.getFloats());
                    im.setCustomModelDataComponent(component);
                } catch (IllegalArgumentException ignored) {
                }
            }
            is.setItemMeta(im);
            itemStacks[key.getSlot()] = is;
        }
        return itemStacks;
    }
}
