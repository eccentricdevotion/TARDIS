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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;

import java.util.Map;

public class ChestBuilder {

    private final TARDIS plugin;

    public ChestBuilder(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean place(CommandSender sender) {
        if (!(sender instanceof Player player)) {
            return true;
        }
        // fill chests with every TARDIS item
        Location location = player.getLocation().add(0, 2, 0);
        int shapedChests = (plugin.getFigura().getShapedRecipes().size() / 27) + 1;
        int shlessChests = (plugin.getIncomposita().getShapelessRecipes().size() / 27) + 1;
        // place some chests
        for (int i = 0; i < shapedChests; i++) {
            location.getBlock().getRelative(BlockFace.EAST, i).setType(Material.CHEST);
        }
        for (int i = 1; i <= shlessChests; i++) {
            location.getBlock().getRelative(BlockFace.WEST, i).setType(Material.CHEST);
        }
        int count = 0;
        int chestNum = 0;
        Chest chest = (Chest) location.getBlock().getState();
        ItemStack is;
        for (ShapedRecipe s : plugin.getFigura().getShapedRecipes().values()) {
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.EAST, chestNum).getState();
            }
            is = s.getResult();
            is.setAmount(1);
            if (is.getItemMeta() instanceof Damageable damageable) {
                damageable.setDamage(0);
                damageable.setUnbreakable(true);
                damageable.addAttributeModifier(
                        Attribute.LUCK,
                        new AttributeModifier(
                                s.getKey(),
                                0.0d,
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.ANY
                        )
                );
                damageable.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
                damageable.setAttributeModifiers(Multimaps.forMap(Map.of()));
                is.setItemMeta(damageable);
            }
            chest.getBlockInventory().addItem(is);
            count++;
        }
        count = 0;
        chestNum = 0;
        chest = (Chest) location.getBlock().getRelative(BlockFace.WEST).getState();
        for (ShapelessRecipe s : plugin.getIncomposita().getShapelessRecipes().values()) {
            if (count == 27) {
                // get next chest
                chestNum++;
                count = 0;
                chest = (Chest) location.getBlock().getRelative(BlockFace.WEST, chestNum).getState();
            }
            is = s.getResult();
            is.setAmount(1);
            if (is.getItemMeta() instanceof Damageable damageable) {
                damageable.setDamage(0);
                damageable.setUnbreakable(true);
                damageable.addItemFlags(ItemFlag.values());
                damageable.setAttributeModifiers(Multimaps.forMap(Map.of()));
                is.setItemMeta(damageable);
            }
            chest.getBlockInventory().addItem(is);
            count++;
        }
        return true;
    }
}
