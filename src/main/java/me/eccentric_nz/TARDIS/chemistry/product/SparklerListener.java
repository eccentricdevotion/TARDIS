/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.chemistry.product;

import me.eccentric_nz.tardis.TardisPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class SparklerListener implements Listener {

    private final TardisPlugin plugin;
    private final HashMap<String, BlockData> colours = new HashMap<>();

    public SparklerListener(TardisPlugin plugin) {
        this.plugin = plugin;
        colours.put("Orange Sparkler", Material.ORANGE_WOOL.createBlockData());
        colours.put("Blue Sparkler", Material.BLUE_WOOL.createBlockData());
        colours.put("Green Sparkler", Material.GREEN_WOOL.createBlockData());
        colours.put("Purple Sparkler", Material.PURPLE_WOOL.createBlockData());
        colours.put("Red Sparkler", Material.RED_WOOL.createBlockData());
    }

    @EventHandler
    public void onSparklerUse(PlayerInteractEvent event) {
        if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Player player = event.getPlayer();
            ItemStack is = event.getItem();
            if (is != null && SparklerMaterial.isCorrectMaterial(is.getType()) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                assert im != null;
                if (im.hasDisplayName() && im.getDisplayName().endsWith("Sparkler") && im.hasCustomModelData() && !im.hasEnchant(Enchantment.LOYALTY)) {
                    player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1.0f, 1.0f);
                    // switch custom data models e.g. 10000035 -> 12000035
                    int cmd = im.getCustomModelData() + 2000000;
                    im.setCustomModelData(cmd);
                    // set custom data on sparkler
                    im.addEnchant(Enchantment.LOYALTY, 1, true);
                    is.setItemMeta(im);
                    // start sparkler runnable
                    BlockData colour = colours.get(ChatColor.stripColor(im.getDisplayName()));
                    SparklerRunnable runnable = new SparklerRunnable(player, colour, System.currentTimeMillis());
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 1L, 2L);
                    runnable.setTaskId(taskId);
                }
            }
        }
    }
}
