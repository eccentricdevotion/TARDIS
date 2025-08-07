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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

public class TARDISTelepathicInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;

    public TARDISTelepathicInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Telepathic Circuit", NamedTextColor.DARK_RED));
        this.inventory.setContents(getButtons());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public ItemStack[] getButtons() {
        // build buttons
        ItemStack[] stack = new ItemStack[9];
        // get current telepathic status
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
        boolean on = (rsp.resultSet() && rsp.isTelepathyOn());
        Component onOff = on ? Component.text("ON", NamedTextColor.GREEN) : Component.text("OFF", NamedTextColor.RED);
        // toggling telepathic circuit on/off
        ItemStack toggle = ItemStack.of(Material.REPEATER);
        ItemMeta tim = toggle.getItemMeta();
        tim.displayName(Component.text("Telepathic Circuit"));
        tim.lore(List.of(onOff));
        CustomModelDataComponent component = tim.getCustomModelDataComponent();
        component.setFloats(on ? SwitchVariant.TELEPATHIC_CIRCUIT_ON.getFloats() : SwitchVariant.TELEPATHIC_CIRCUIT_OFF.getFloats());
        tim.setCustomModelDataComponent(component);
        toggle.setItemMeta(tim);
        stack[0] = toggle;
        // cave finder
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.cave")) {
            ItemStack cave = ItemStack.of(Material.DRIPSTONE_BLOCK);
            ItemMeta cim = cave.getItemMeta();
            cim.displayName(Component.text("Cave Finder"));
            cim.lore(List.of(
                    Component.text("Search for a cave"),
                    Component.text("to travel to.")
            ));
            cave.setItemMeta(cim);
            stack[2] = cave;
        }
        // structure finder
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.village")) {
            ItemStack structure = ItemStack.of(Material.HAY_BLOCK);
            ItemMeta sim = structure.getItemMeta();
            sim.displayName(Component.text("Structure Finder"));
            sim.lore(List.of(
                    Component.text("Search for a structure"),
                    Component.text("to travel to.")
            ));
            structure.setItemMeta(sim);
            stack[4] = structure;
        }
        // biome finder
        if (TARDISPermission.hasPermission(player, "tardis.timetravel.biome")) {
            ItemStack biome = ItemStack.of(Material.BAMBOO_MOSAIC);
            ItemMeta bim = biome.getItemMeta();
            bim.displayName(Component.text("Biome Finder"));
            bim.lore(List.of(
                    Component.text("Search for a biome"),
                    Component.text("to travel to.")
            ));
            biome.setItemMeta(bim);
            stack[6] = biome;
        }
        // close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        gui.setItemModel(GUIMap.BUTTON_CLOSE.key());
        close.setItemMeta(gui);
        stack[8] = close;
        return stack;
    }
}
