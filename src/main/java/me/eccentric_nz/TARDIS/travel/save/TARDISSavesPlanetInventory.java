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
package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUISaves;
import me.eccentric_nz.TARDIS.database.data.Planet;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDeathLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlanets;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TARDISSavesPlanetInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int id;
    private final Player player;
    private final Inventory inventory;

    public TARDISSavesPlanetInventory(TARDIS plugin, int id, Player player) {
        this.plugin = plugin;
        this.id = id;
        this.player = player;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Dimension Map", NamedTextColor.DARK_RED));
        this.inventory.setContents(getWorlds());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getWorlds() {
        ItemStack[] stack = new ItemStack[54];
        // home stack
        ItemStack his = ItemStack.of(GUISaves.HOME.material(), 1);
        ItemMeta him = his.getItemMeta();
        List<Component> hlore = new ArrayList<>();
        HashMap<String, Object> wherehl = new HashMap<>();
        wherehl.put("tardis_id", id);
        ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
        if (rsh.resultSet()) {
            him.displayName(Component.text("Home"));
            hlore.add(Component.text(rsh.getWorld().getName()));
            hlore.add(Component.text(rsh.getX()));
            hlore.add(Component.text(rsh.getY()));
            hlore.add(Component.text(rsh.getZ()));
            hlore.add(Component.text(rsh.getDirection().toString()));
            hlore.add(Component.text((rsh.isSubmarine()) ? "true" : "false"));
            if (!rsh.getPreset().isEmpty()) {
                hlore.add(Component.text(rsh.getPreset()));
            }
        } else {
            hlore.add(Component.text("Not found!"));
        }
        him.lore(hlore);
        his.setItemMeta(him);
        stack[GUISaves.HOME.slot()] = his;
        if (TARDISPermission.hasPermission(player, "tardis.save.death")) {
            // home stack
            ItemStack death = ItemStack.of(GUISaves.DEATH.material(), 1);
            ItemMeta dim = death.getItemMeta();
            dim.displayName(Component.text("Death location"));
            List<Component> dlore = new ArrayList<>();
            ResultSetDeathLocation rsd = new ResultSetDeathLocation(plugin, player.getUniqueId().toString());
            if (rsd.resultSet()) {
                dlore.add(Component.text(rsd.getWorld().getName()));
                dlore.add(Component.text(rsd.getX()));
                dlore.add(Component.text(rsd.getY()));
                dlore.add(Component.text(rsd.getZ()));
                dlore.add(Component.text(rsd.getDirection().toString()));
                dlore.add(Component.text((rsd.isSubmarine()) ? "true" : "false"));
            } else {
                dlore.add(Component.text("Not found!"));
            }
            dim.lore(dlore);
            death.setItemMeta(dim);
            stack[GUISaves.DEATH.slot()] = death;
        }
        // unique planets from saved destinations
        ResultSetPlanets rsd = new ResultSetPlanets(plugin, id);
        if (rsd.resultSet()) {
            int i = 9;
            for (Planet planet : rsd.getData()) {
                ItemStack is = ItemStack.of(planet.material(), 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(planet.name()));
                is.setItemMeta(im);
                stack[i] = is;
                i += 2;
            }
        }
        return stack;
    }
}
