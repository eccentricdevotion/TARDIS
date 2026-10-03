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
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicItem;
import me.eccentric_nz.TARDIS.doors.Door;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
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

public class DoorBuilder {

    private final TARDIS plugin;

    public DoorBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void place(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        // fill chests with every TARDIS time rotor state
        int chests = ((Door.byMaterial.size() * 8) / 27) + 1;
        Location location = player.getLocation().add(0, 2, 0);
        // place some chests
        for (int i = 0; i < chests; i++) {
            location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
        }
        int count = 0;
        int chestNum = 0;
        Chest chest = (Chest) location.getBlock().getState();
        for (Map.Entry<Material, Door> entry : Door.byMaterial.entrySet()) {
            String name = entry.getValue().getName();
            plugin.getLogger().log(Level.INFO, name);
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            Material material = entry.getKey();
            ItemStack closed = new ItemStack(material, 1);
            closed.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(plugin, name + "_closed"));
            chest.getBlockInventory().addItem(closed);
            ItemStack open = new ItemStack(material, 1);
            open.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(plugin, name + "_open"));
            chest.getBlockInventory().addItem(open);
            count += 2;
            if (entry.getValue().hasExtra()) {
                ItemStack extra = new ItemStack(material, 1);
                extra.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(plugin, name + "_extra"));
                chest.getBlockInventory().addItem(extra);
                count++;
            }
            int[] unique = Arrays.stream(entry.getValue().getFrames()).distinct().toArray();
            for (int j : unique) {
                ItemStack is = ItemStack.of(material, 1);
                String t = name + "_" + j;
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
        // sonic generator and dock
        ItemStack generator = new ItemStack(Material.FLOWER_POT, 1);
        generator.setData(DataComponentTypes.ITEM_MODEL, SonicItem.SONIC_GENERATOR.getKey());
        chest.getBlockInventory().addItem(generator);
        ItemStack on = new ItemStack(Material.FLOWER_POT, 1);
        on.setData(DataComponentTypes.ITEM_MODEL, SonicItem.SONIC_DOCK_ON.getKey());
        chest.getBlockInventory().addItem(on);
        ItemStack off = new ItemStack(Material.FLOWER_POT, 1);
        off.setData(DataComponentTypes.ITEM_MODEL, SonicItem.SONIC_DOCK_OFF.getKey());
        chest.getBlockInventory().addItem(off);
        // monitor + frame
        ItemStack monitor = new ItemStack(Material.MAP, 1);
        monitor.setData(DataComponentTypes.ITEM_MODEL, RecipeItem.TARDIS_MONITOR.getModel());
        chest.getBlockInventory().addItem(monitor);
        ItemStack frame = new ItemStack(Material.GLASS, 1);
        frame.setData(DataComponentTypes.ITEM_MODEL, ModelledControl.MONITOR_FRAME_LEFT.getKey());
        chest.getBlockInventory().addItem(frame);
        ItemStack middle = new ItemStack(Material.GLASS, 1);
        middle.setData(DataComponentTypes.ITEM_MODEL, ModelledControl.MONITOR_FRAME_MIDDLE.getKey());
        chest.getBlockInventory().addItem(middle);
        ItemStack right = new ItemStack(Material.GLASS, 1);
        right.setData(DataComponentTypes.ITEM_MODEL, ModelledControl.MONITOR_FRAME_RIGHT.getKey());
        chest.getBlockInventory().addItem(right);
    }
}
