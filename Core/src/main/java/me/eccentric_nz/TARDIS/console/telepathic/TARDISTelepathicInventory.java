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
package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

public class TARDISTelepathicInventory {

    private final TARDIS plugin;
    private final Player player;

    public TARDISTelepathicInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public ItemStack[] getButtons() {
        // build buttons
        ItemStack[] stack = new ItemStack[9];
        // get current telepathic status
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        boolean on = (rsp.resultSet() && rsp.isTelepathyOn());
        String onOff = on ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF" ;
        // toggling telepathic circuit on/off
        ItemStack toggle = new ItemStack(Material.REPEATER);
        ItemMeta tim = toggle.getItemMeta();
        tim.setDisplayName("Telepathic Circuit");
        tim.setLore(List.of(onOff));
        CustomModelDataComponent component = tim.getCustomModelDataComponent();
        component.setFloats(on ? SwitchVariant.TELEPATHIC_CIRCUIT_ON.getFloats() : SwitchVariant.TELEPATHIC_CIRCUIT_OFF.getFloats());
        tim.setCustomModelDataComponent(component);
        toggle.setItemMeta(tim);
        stack[0] = toggle;
        // cave finder
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.cave")) {
            ItemStack cave = new ItemStack(Material.DRIPSTONE_BLOCK);
            ItemMeta cim = cave.getItemMeta();
            cim.setDisplayName("Cave Finder");
            cim.setLore(List.of("Search for a cave", "to travel to."));
            cave.setItemMeta(cim);
            stack[2] = cave;
        }
        // structure finder
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
            ItemStack structure = new ItemStack(Material.HAY_BLOCK);
            ItemMeta sim = structure.getItemMeta();
            sim.setDisplayName("Structure Finder");
            sim.setLore(List.of("Search for a structure", "to travel to."));
            structure.setItemMeta(sim);
            stack[4] = structure;
        }
        // biome finder
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
            ItemStack biome = new ItemStack(Material.BAMBOO_MOSAIC);
            ItemMeta bim = biome.getItemMeta();
            bim.setDisplayName("Biome Finder");
            bim.setLore(List.of("Search for a biome", "to travel to."));
            biome.setItemMeta(bim);
            stack[6] = biome;
        }
        // close
        ItemStack close = new ItemStack(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        gui.setItemModel(GUIMap.BUTTON_CLOSE.key());
        close.setItemMeta(gui);
        stack[8] = close;
        return stack;
    }
}
