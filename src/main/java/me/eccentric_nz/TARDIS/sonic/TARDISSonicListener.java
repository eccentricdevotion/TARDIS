/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.sonic.actions.*;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISSonicListener implements Listener {

    private final TARDIS plugin;
    private final Material sonic;
    private final List<Material> diamond = new ArrayList<>();
    private final List<Material> doors = new ArrayList<>();
    private final List<Material> redstone = new ArrayList<>();
    private final List<Material> ignite = new ArrayList<>();

    public TARDISSonicListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        sonic = Material.valueOf(split[0]);
        diamond.add(Material.COBWEB);
        diamond.add(Material.IRON_BARS);
        diamond.add(Material.SNOW);
        diamond.add(Material.SNOW_BLOCK);
        diamond.addAll(TARDISMaterials.glass);
        doors.add(Material.ACACIA_DOOR);
        doors.add(Material.ACACIA_TRAPDOOR);
        doors.add(Material.BIRCH_DOOR);
        doors.add(Material.BIRCH_TRAPDOOR);
        doors.add(Material.DARK_OAK_DOOR);
        doors.add(Material.DARK_OAK_TRAPDOOR);
        doors.add(Material.IRON_DOOR);
        doors.add(Material.IRON_TRAPDOOR);
        doors.add(Material.JUNGLE_DOOR);
        doors.add(Material.JUNGLE_TRAPDOOR);
        doors.add(Material.OAK_DOOR);
        doors.add(Material.OAK_TRAPDOOR);
        doors.add(Material.SPRUCE_DOOR);
        doors.add(Material.SPRUCE_TRAPDOOR);
        redstone.add(Material.DETECTOR_RAIL);
        redstone.add(Material.IRON_DOOR);
        redstone.add(Material.IRON_TRAPDOOR);
        redstone.add(Material.MUSHROOM_STEM);
        redstone.add(Material.PISTON);
        redstone.add(Material.STICKY_PISTON);
        redstone.add(Material.POWERED_RAIL);
        redstone.add(Material.REDSTONE_LAMP);
        redstone.add(Material.REDSTONE_WIRE);
        ignite.add(Material.CAMPFIRE);
        ignite.add(Material.NETHERRACK);
        ignite.add(Material.OBSIDIAN);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(sonic) && is.hasItemMeta()) {
            ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
            if (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver")) {
                List<String> lore = im.getLore();
                Action action = event.getAction();
                if (action.equals(Action.RIGHT_CLICK_AIR) && !player.isSneaking()) {
                    // rebuild Police Box if dispersed by HADS
                    if (plugin.getTrackerKeeper().getDispersed().containsKey(player.getUniqueId())) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        TARDISSonicDispersed.assemble(plugin, player);
                    } else if (lore != null && (lore.contains("Bio-scanner Upgrade") || lore.contains("Knockback Upgrade"))) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        if (player.hasPermission("tardis.sonic.freeze")) {
                            // freeze target player
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                Player target = TARDISSonicFreeze.getTargetPlayer(player);
                                if (target != null) {
                                    TARDISSonicFreeze.immobilise(plugin, player, target);
                                }
                            }, 20L);
                        }
                        if (player.hasPermission("tardis.sonic.knockback")) {
                            // knockback any monsters in line of sight
                            Entity target = TARDISSonicKnockback.getTargetEntity(player);
                            if (target != null) {
                                TARDISSonicKnockback.knockback(player, target);
                            }
                        }
                    }
                    if (player.hasPermission("tardis.sonic.standard")) {
                        TARDISSonic.standardSonic(plugin, player, now);
                        return;
                    }
                }
                if (action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking() && player.hasPermission("tardis.sonic.standard")) {
                    Inventory ppm = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Player Prefs Menu");
                    ppm.setContents(new TARDISPrefsMenuInventory(plugin, player.getUniqueId()).getMenu());
                    player.openInventory(ppm);
                    return;
                }
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block block = event.getClickedBlock();
                    if (doors.contains(block.getType()) && player.hasPermission("tardis.admin") && lore != null && lore.contains("Admin Upgrade")) {
                        // display TARDIS info
                        TARDISSonicAdmin.displayInfo(plugin, player, block);
                    }
                    if (Tag.WALL_SIGNS.isTagged(block.getType()) && player.hasPermission("tardis.atmospheric")) {
                        // make it snow
                        TARDISSonicAtmospheric.makeItSnow(plugin, player, block);
                    }
                    if (player.hasPermission("tardis.sonic.arrow") && lore != null && lore.contains("Pickup Arrows Upgrade")) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
                        // scan area around block for an arrow
                        List<Entity> nearbyEntites = new ArrayList(block.getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2));
                        for (Entity e : nearbyEntites) {
                            if (e instanceof Arrow) {
                                // pick up arrow
                                Arrow arrow = (Arrow) e;
                                arrow.setPickupStatus(AbstractArrow.PickupStatus.ALLOWED);
                            }
                        }
                    }
                    if (!redstone.contains(block.getType()) && player.hasPermission("tardis.sonic.emerald") && lore != null && lore.contains("Emerald Upgrade") && !block.getType().isInteractable()) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        // scan environment
                        TARDISSonicScanner.scan(plugin, block.getLocation(), player);
                    }
                    if (redstone.contains(block.getType()) && player.hasPermission("tardis.sonic.redstone") && lore != null && lore.contains("Redstone Upgrade")) {
                        // toggle powered state
                        TARDISSonicRedstone.togglePoweredState(plugin, player, block);
                    }
                }
                if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                    Block block = event.getClickedBlock();
                    if (!player.isSneaking()) {
                        if ((block.getType().isBurnable() || ignite.contains(block.getType())) && player.hasPermission("tardis.sonic.ignite") && lore != null && lore.contains("Ignite Upgrade")) {
                            TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_short");
                            // ignite block
                            TARDISSonicIgnite.ignite(plugin, block, player);
                        }
                        if (diamond.contains(block.getType()) && player.hasPermission("tardis.sonic.diamond") && lore != null && lore.contains("Diamond Upgrade")) {
                            // break block
                            TARDISSonicDisruptor.breakBlock(plugin, player, block);
                        }
                    } else if (TARDISSonicPainter.getPaintable().contains(block.getType()) && player.hasPermission("tardis.sonic.paint") && lore != null && lore.contains("Painter Upgrade")) {
                        // paint the block
                        TARDISSonicPainter.paint(plugin, player, block);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFrozenMove(PlayerMoveEvent event) {
        if (plugin.getTrackerKeeper().getFrozenPlayers().contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
