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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.sonic.actions.SonicReplant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class SonicFarmBlockListener implements Listener {

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
    private final Material sb = Material.SWEET_BERRIES;
    private final Material ca = Material.CACTUS;
    private final Material pp = Material.PITCHER_POD;
    private final Material tf = Material.TORCHFLOWER;

    public SonicFarmBlockListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlantHarvest(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!TARDISPermission.hasPermission(player, "tardis.sonic.plant")) {
            return;
        }
        Block block = event.getBlock();
        Material material = block.getType();
        if (!TARDISMaterials.crops.contains(material)) {
            return;
        }
        PlayerInventory inv = player.getInventory();
        ItemStack stack = inv.getItemInMainHand();
        if (stack.getType().equals(Material.BLAZE_ROD) && stack.hasItemMeta()) {
            ItemMeta im = stack.getItemMeta();
            if (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Sonic Screwdriver") && im.hasLore() && im.lore().contains(Component.text("Emerald Upgrade"))) {
                if ((material.equals(sc)) && inv.contains(sc)) {
                    // SUGAR_CANE
                    processHarvest(player, sc, block);
                } else if (material.equals(ca) && inv.contains(ca)) {
                    // CACTUS
                    processHarvest(player, ca, block);
                } else if (material.equals(tf) && inv.contains(tf)) {
                    // TORCHFLOWER
                    processHarvest(player, tf, block);
                } else {
                    Ageable ageable = (Ageable) block.getBlockData();
                    if (ageable.getAge() == ageable.getMaximumAge()) {
                        switch (material) {
                            case BEETROOTS -> {
                                if (inv.contains(bs)) {
                                    processHarvest(player, bs, block);
                                }
                            }
                            case CARROTS -> {
                                if (inv.contains(ci)) {
                                    processHarvest(player, ci, block);
                                }
                            }
                            case COCOA -> {
                                if (inv.contains(is)) {
                                    processHarvest(player, is, block);
                                }
                            }
                            case WHEAT -> {
                                if (inv.contains(ss)) {
                                    processHarvest(player, ss, block);
                                }
                            }
                            case MELON_STEM -> {
                                if (inv.contains(ms)) {
                                    processHarvest(player, ms, block);
                                }
                            }
                            case NETHER_WART -> {
                                if (inv.contains(nw)) {
                                    processHarvest(player, nw, block);
                                }
                            }
                            case POTATOES -> {
                                if (inv.contains(pi)) {
                                    processHarvest(player, pi, block);
                                }
                            }
                            case PUMPKIN_STEM -> {
                                if (inv.contains(ps)) {
                                    processHarvest(player, ps, block);
                                }
                            }
                            case SWEET_BERRY_BUSH -> {
                                if (inv.contains(sb)) {
                                    processHarvest(player, sb, block);
                                }
                            }
                            case PITCHER_CROP -> {
                                if (inv.contains(pp)) {
                                    processHarvest(player, pp, block);
                                }
                            }
                            default -> {
                            }
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
                p.getInventory().setItem(slot, ItemStack.of(air));
            }
            Runnable tsr = new SonicReplant(plugin, b, m);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, tsr, 20);
        }
    }
}
