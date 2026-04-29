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
package me.eccentric_nz.TARDIS.ARS;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural
 * Configuration software to relocate the console outside the ship (as it was
 * too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class ARSInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ARSInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Architectural Reconfiguration", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack(player));
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack(Player player) {

        ItemStack[] is = new ItemStack[54];
        // direction pad up
        ItemStack pad_up = ItemStack.of(GUIArs.BUTTON_UP.material(), 1);
        pad_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_UP", "Up")));
        is[GUIArs.BUTTON_UP.slot()] = pad_up;
        // room relocator
        ItemStack relocator = ItemStack.of(GUIArs.BUTTON_RELOCATE.material(), 1);
        relocator.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Room Relocator"));
        is[GUIArs.BUTTON_RELOCATE.slot()] = relocator;
        // black wool
        ItemStack black = ItemStack.of(GUIArs.BUTTON_MAP_ON.material(), 1);
        black.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP_NO", "Load the map!")));
        for (int j = 0; j < 37; j += 9) {
            for (int k = 0; k < 5; k++) {
                int slot = 4 + j + k;
                is[slot] = black;
            }
        }
        // direction pad left
        ItemStack pad_left = ItemStack.of(GUIArs.BUTTON_LEFT.material(), 1);
        pad_left.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEFT", "Left")));
        is[GUIArs.BUTTON_LEFT.slot()] = pad_left;
        // load map
        ItemStack map = ItemStack.of(GUIArs.BUTTON_MAP.material(), 1);
        map.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP", "Load map")));
        is[GUIArs.BUTTON_MAP.slot()] = map;
        // direction pad right
        ItemStack pad_right = ItemStack.of(GUIArs.BUTTON_RIGHT.material(), 1);
        pad_right.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RIGHT", "Right")));
        is[GUIArs.BUTTON_RIGHT.slot()] = pad_right;
        // set
        ItemStack s = ItemStack.of(GUIArs.BUTTON_RECON.material(), 1);
        s.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RECON", "Reconfigure!")));
        is[GUIArs.BUTTON_RECON.slot()] = s;
        // direction pad down
        ItemStack pad_down = ItemStack.of(GUIArs.BUTTON_DOWN.material(), 1);
        pad_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DOWN", "Down")));
        is[GUIArs.BUTTON_DOWN.slot()] = pad_down;
        // level bottom
        ItemStack level_bot = ItemStack.of(GUIArs.BUTTON_LEVEL_B.material(), 1);
        level_bot.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_B", "Bottom level")));
        is[GUIArs.BUTTON_LEVEL_B.slot()] = level_bot;
        // level selected
        ItemStack level_sel = ItemStack.of(GUIArs.BUTTON_LEVEL.material(), 1);
        level_sel.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL", "Main level")));
        is[GUIArs.BUTTON_LEVEL.slot()] = level_sel;
        // level top
        ItemStack level_top = ItemStack.of(GUIArs.BUTTON_LEVEL_T.material(), 1);
        level_top.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_T", "Top level")));
        is[GUIArs.BUTTON_LEVEL_T.slot()] = level_top;
        // reset
        ItemStack reset = ItemStack.of(GUIArs.BUTTON_RESET.material(), 1);
        reset.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RESET", "Reset selected")));
        is[GUIArs.BUTTON_RESET.slot()] = reset;
        // scroll left
        ItemStack scroll_left = ItemStack.of(GUIArs.BUTTON_SCROLL_L.material(), 1);
        scroll_left.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_L", "Scroll left")));
        is[GUIArs.BUTTON_SCROLL_L.slot()] = scroll_left;
        // scroll right
        ItemStack scroll_right = ItemStack.of(GUIArs.BUTTON_SCROLL_R.material(), 1);
        scroll_right.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_R", "Scroll right")));
        is[GUIArs.BUTTON_SCROLL_R.slot()] = scroll_right;
        // jettison
        ItemStack jettison = ItemStack.of(GUIArs.BUTTON_JETT.material(), 1);
        jettison.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_JETT", "Jettison")));
        is[GUIArs.BUTTON_JETT.slot()] = jettison;

        int i = 45;
        for (TARDISARS a : TARDISARS.values()) {
            if (a.isInGUI() && i < 54) {
                ItemStack room = ItemStack.of(Material.getMaterial(a.getMaterial()), 1);
                room.setData(DataComponentTypes.CUSTOM_NAME, Component.text(a.getDescriptiveName()));
                ItemLore.Builder lore = ItemLore.lore();
                lore.addLine(Component.text("Cost: " + plugin.getRoomsConfig().getInt("rooms." + a + ".cost")));
                String roomName = TARDISARS.ARSFor(room.getType().toString()).getConfigPath();
                if (player != null && !TARDISPermission.hasPermission(player, "tardis.room." + roomName.toLowerCase(Locale.ROOT))) {
                    lore.addLine(Component.text(plugin.getLanguage().getString("NO_PERM_CONSOLE", "No permission!"), NamedTextColor.RED));
                }
                room.setData(DataComponentTypes.LORE, lore.build());
                is[i] = room;
                i++;
            }
        }
        return is;
    }
}
