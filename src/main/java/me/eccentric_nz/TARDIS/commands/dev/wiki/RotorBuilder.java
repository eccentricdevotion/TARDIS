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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

public class RotorBuilder {

    private final TARDIS plugin;

    public RotorBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void place(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        // fill chests with every TARDIS time rotor state
        int chests = ((Rotor.byMaterial.size() * 13) / 27); // probably excessive
        Location location = player.getLocation().add(0, 2, 0);
        // place some chests
        for (int i = 0; i < chests; i++) {
            location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
        }
        int count = 0;
        int chestNum = 0;
        Chest chest = (Chest) location.getBlock().getState();
        for (Map.Entry<Material, Rotor> entry : Rotor.byMaterial.entrySet()) {
            String name = entry.getValue().name();
            plugin.getLogger().log(Level.INFO, name);
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            Material material = entry.getKey();
            ItemStack off = new ItemStack(Material.LIGHT_GRAY_DYE, 1);
            off.setData(DataComponentTypes.ITEM_MODEL, entry.getValue().offModel());
            chest.getBlockInventory().addItem(off);
            count++;
            int[] unique = Arrays.stream(entry.getValue().frames()).distinct().toArray();
            for (int i = 0; i < unique.length; i++) {
                ItemStack is = ItemStack.of(material, 1);
                String t = "time_rotor_" + name + "_" + unique[i];
                if (name.equals("engine_rotor")) {
                    t = "time_rotor_engine_" + unique[i];
                }
                if (name.equals("engine")) {
                    t = "time_engine_" + unique[i];
                }
                is.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(plugin, t));
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(t));
                chest.getBlockInventory().addItem(is);
                count++;
                if (count == 27) {
                    // get next chest
                    chestNum++;
                    count = 0;
                    chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
                }
            }
        }
    }
}
