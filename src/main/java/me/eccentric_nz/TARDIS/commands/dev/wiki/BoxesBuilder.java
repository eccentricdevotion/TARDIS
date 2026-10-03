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
import me.eccentric_nz.TARDIS.builders.exterior.BuilderUtility;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BoxesBuilder {

    public void place(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return;
        }
        // fill chests with every TARDIS custom model exterior
        List<ChameleonPreset> presets = new ArrayList<>();
        int p = 0;
        Location location = player.getLocation().add(0, 2, 0);
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (preset.usesArmourStand() && preset != ChameleonPreset.ITEM) {
                presets.add(preset);
                p++;
            }
        }
        int chests = ((p * 4) / 27) + 1;
        // place some chests
        for (int i = 0; i < chests; i++) {
            location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
        }
        int count = 0;
        int chestNum = 0;
        Chest chest = (Chest) location.getBlock().getState();
        for (ChameleonPreset preset : presets) {
            if (count > 23) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            Material material = BuilderUtility.getMaterialForArmourStand(preset, -1, true);
            ItemStack closed = ItemStack.of(material, 1);
            closed.setData(DataComponentTypes.ITEM_MODEL, preset.getClosed());
            ItemStack open = ItemStack.of(material, 1);
            open.setData(DataComponentTypes.ITEM_MODEL, preset.getOpen());
            ItemStack stained = ItemStack.of(material, 1);
            stained.setData(DataComponentTypes.ITEM_MODEL, preset.getStained());
            ItemStack glass = ItemStack.of(material, 1);
            glass.setData(DataComponentTypes.ITEM_MODEL, preset.getGlass());
            chest.getBlockInventory().addItem(closed);
            chest.getBlockInventory().addItem(open);
            chest.getBlockInventory().addItem(stained);
            chest.getBlockInventory().addItem(glass);
            count += 4;
        }
        // plus type40
        ItemStack closed = ItemStack.of(Material.CLAY_BALL, 1);
        closed.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.TYPE_40_CLOSED.getKey());
        ItemStack open = ItemStack.of(Material.CLAY_BALL, 1);
        open.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.TYPE_40_OPEN.getKey());
        ItemStack stained = ItemStack.of(Material.CLAY_BALL, 1);
        stained.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.TYPE_40_STAINED.getKey());
        ItemStack glass = ItemStack.of(Material.CLAY_BALL, 1);
        glass.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.TYPE_40_GLASS.getKey());
        chest.getBlockInventory().addItem(closed);
        chest.getBlockInventory().addItem(open);
        chest.getBlockInventory().addItem(stained);
        chest.getBlockInventory().addItem(glass);
        // and bad wolf
        ItemStack badclosed = ItemStack.of(Material.WOLF_SPAWN_EGG, 1);
        badclosed.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BAD_WOLF_CLOSED.getKey());
        ItemStack badopen = ItemStack.of(Material.WOLF_SPAWN_EGG, 1);
        badopen.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BAD_WOLF_OPEN.getKey());
        ItemStack badstained = ItemStack.of(Material.WOLF_SPAWN_EGG, 1);
        badstained.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BAD_WOLF_STAINED.getKey());
        ItemStack badglass = ItemStack.of(Material.WOLF_SPAWN_EGG, 1);
        badglass.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BAD_WOLF_GLASS.getKey());
        chest.getBlockInventory().addItem(badclosed);
        chest.getBlockInventory().addItem(badopen);
        chest.getBlockInventory().addItem(badstained);
        chest.getBlockInventory().addItem(badglass);     }
}
