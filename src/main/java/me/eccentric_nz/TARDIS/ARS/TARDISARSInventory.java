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
package me.eccentric_nz.TARDIS.ARS;

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
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural
 * Configuration software to relocate the console outside the ship (as it was
 * too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class TARDISARSInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISARSInventory(TARDIS plugin, Player player) {
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
        ItemMeta up = pad_up.getItemMeta();
        up.displayName(Component.text(plugin.getLanguage().getString("BUTTON_UP", "Up")));
        pad_up.setItemMeta(up);
        is[GUIArs.BUTTON_UP.slot()] = pad_up;
        // black wool
        ItemStack black = ItemStack.of(GUIArs.BUTTON_MAP_ON.material(), 1);
        ItemMeta wool = black.getItemMeta();
        wool.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MAP_NO", "Load the map!")));
        black.setItemMeta(wool);
        for (int j = 0; j < 37; j += 9) {
            for (int k = 0; k < 5; k++) {
                int slot = 4 + j + k;
                is[slot] = black;
            }
        }
        // direction pad left
        ItemStack pad_left = ItemStack.of(GUIArs.BUTTON_LEFT.material(), 1);
        ItemMeta left = pad_left.getItemMeta();
        left.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEFT", "Left")));
        pad_left.setItemMeta(left);
        is[GUIArs.BUTTON_LEFT.slot()] = pad_left;
        // load map
        ItemStack map = ItemStack.of(GUIArs.BUTTON_MAP.material(), 1);
        ItemMeta load = map.getItemMeta();
        load.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MAP", "Load map")));
        map.setItemMeta(load);
        is[GUIArs.BUTTON_MAP.slot()] = map;
        // direction pad right
        ItemStack pad_right = ItemStack.of(GUIArs.BUTTON_RIGHT.material(), 1);
        ItemMeta right = pad_right.getItemMeta();
        right.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RIGHT", "Right")));
        pad_right.setItemMeta(right);
        is[GUIArs.BUTTON_RIGHT.slot()] = pad_right;
        // set
        ItemStack s = ItemStack.of(GUIArs.BUTTON_RECON.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RECON")));
        s.setItemMeta(sim);
        is[GUIArs.BUTTON_RECON.slot()] = s;
        // direction pad down
        ItemStack pad_down = ItemStack.of(GUIArs.BUTTON_DOWN.material(), 1);
        ItemMeta down = pad_down.getItemMeta();
        down.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DOWN", "Down")));
        pad_down.setItemMeta(down);
        is[GUIArs.BUTTON_DOWN.slot()] = pad_down;
        // level bottom
        ItemStack level_bot = ItemStack.of(GUIArs.BUTTON_LEVEL_B.material(), 1);
        ItemMeta bot = level_bot.getItemMeta();
        bot.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_B", "Bottom level")));
        level_bot.setItemMeta(bot);
        is[GUIArs.BUTTON_LEVEL_B.slot()] = level_bot;
        // level selected
        ItemStack level_sel = ItemStack.of(GUIArs.BUTTON_LEVEL.material(), 1);
        ItemMeta main = level_sel.getItemMeta();
        main.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEVEL", "Main level")));
        level_sel.setItemMeta(main);
        is[GUIArs.BUTTON_LEVEL.slot()] = level_sel;
        // level top
        ItemStack level_top = ItemStack.of(GUIArs.BUTTON_LEVEL_T.material(), 1);
        ItemMeta top = level_top.getItemMeta();
        top.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_T", "Top level")));
        level_top.setItemMeta(top);
        is[GUIArs.BUTTON_LEVEL_T.slot()] = level_top;
        // reset
        ItemStack reset = ItemStack.of(GUIArs.BUTTON_RESET.material(), 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RESET", "Reset selected")));
        reset.setItemMeta(cobble);
        is[GUIArs.BUTTON_RESET.slot()] = reset;
        // scroll left
        ItemStack scroll_left = ItemStack.of(GUIArs.BUTTON_SCROLL_L.material(), 1);
        ItemMeta nim = scroll_left.getItemMeta();
        nim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_L", "Scroll left")));
        scroll_left.setItemMeta(nim);
        is[GUIArs.BUTTON_SCROLL_L.slot()] = scroll_left;
        // scroll right
        ItemStack scroll_right = ItemStack.of(GUIArs.BUTTON_SCROLL_R.material(), 1);
        ItemMeta pim = scroll_right.getItemMeta();
        pim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_R", "Scroll right")));
        scroll_right.setItemMeta(pim);
        is[GUIArs.BUTTON_SCROLL_R.slot()] = scroll_right;
        // jettison
        ItemStack jettison = ItemStack.of(GUIArs.BUTTON_JETT.material(), 1);
        ItemMeta tnt = jettison.getItemMeta();
        tnt.displayName(Component.text(plugin.getLanguage().getString("BUTTON_JETT")));
        jettison.setItemMeta(tnt);
        is[GUIArs.BUTTON_JETT.slot()] = jettison;

        int i = 45;
        for (TARDISARS a : TARDISARS.values()) {
            if (a.getOffset() != 0 && i < 54) {
                ItemStack room = ItemStack.of(Material.getMaterial(a.getMaterial()), 1);
                ItemMeta im = room.getItemMeta();
                im.displayName(Component.text(a.getDescriptiveName()));
                List<Component> lore = new ArrayList<>();
                lore.add(Component.text("Cost: " + plugin.getRoomsConfig().getInt("rooms." + a + ".cost")));
                String roomName = TARDISARS.ARSFor(room.getType().toString()).getConfigPath();
                if (player != null && !TARDISPermission.hasPermission(player, "tardis.room." + roomName.toLowerCase(Locale.ROOT))) {
                    lore.add(Component.text(plugin.getLanguage().getString("NO_PERM_CONSOLE", "No permission!"), NamedTextColor.RED));
                }
                im.lore(lore);
                room.setItemMeta(im);
                is[i] = room;
                i++;
            }
        }
        return is;
    }
}
