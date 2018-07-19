/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISFarmBlockListener implements Listener {

    private final TARDIS plugin;
    // seeds
    private final Material air = Material.AIR;
    private final Material bs = Material.BEETROOT_SEEDS;
    private final Material ci = Material.CARROT;
    private final Material is = Material.COCOA_BEANS;
    private final Material ms = Material.MELON_SEEDS;
    private final Material nw = Material.NETHER_WART;
    private final Material pi = Material.POTATO;
    private final Material ps = Material.PUMPKIN_SEEDS;
    private final Material sc = Material.SUGAR_CANE;
    private final Material ss = Material.WHEAT_SEEDS;
    private final Material sonic;

    public TARDISFarmBlockListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        sonic = Material.valueOf(split[0]);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlantHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("tardis.sonic.plant")) {
            return;
        }
        Block block = event.getBlock();
        if (!TARDISMaterials.crops.contains(block.getType())) {
            return;
        }
        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getType().equals(sonic) && stack.hasItemMeta()) {
            ItemMeta im = stack.getItemMeta();
            if (im.hasDisplayName() && ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver") && im.hasLore() && im.getLore().contains("Emerald Upgrade")) {
                if (block.getType().equals(Material.SUGAR_CANE) && player.getInventory().contains(sc)) {
                    processHarvest(player, sc, block);
                } else {
                    Ageable ageable = (Ageable) block.getBlockData();
                    if (ageable.getAge() == ageable.getMaximumAge()) {
                        switch (block.getType()) {
                            case BEETROOTS:
                                if (player.getInventory().contains(bs)) {
                                    processHarvest(player, bs, block);
                                }
                                break;
                            case CARROTS:
                                if (player.getInventory().contains(ci)) {
                                    processHarvest(player, ci, block);
                                }
                                break;
                            case COCOA:
                                if (player.getInventory().contains(is)) {
                                    processHarvest(player, is, block);
                                }
                                break;
                            case WHEAT:
                                if (player.getInventory().contains(ss)) {
                                    processHarvest(player, ss, block);
                                }
                                break;
                            case MELON_STEM:
                                if (player.getInventory().contains(ms)) {
                                    processHarvest(player, ms, block);
                                }
                                break;
                            case NETHER_WART:
                                if (player.getInventory().contains(nw)) {
                                    processHarvest(player, nw, block);
                                }
                                break;
                            case POTATOES:
                                if (player.getInventory().contains(pi)) {
                                    processHarvest(player, pi, block);
                                }
                                break;
                            case PUMPKIN_STEM:
                                if (player.getInventory().contains(ps)) {
                                    processHarvest(player, ps, block);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    private void processHarvest(Player p, Material m, Block b) {
        int slot = p.getInventory().first(m);
        if (slot >= 0) {
            ItemStack next = p.getInventory().getItem(slot);
            if (next.getAmount() > 1) {
                next.setAmount(next.getAmount() - 1);
                p.getInventory().setItem(slot, next);
            } else {
                p.getInventory().setItem(slot, new ItemStack(air));
            }
            Runnable tsr = new TARDISSonicReplant(plugin, b, m);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, tsr, 20);
        }
    }
}
