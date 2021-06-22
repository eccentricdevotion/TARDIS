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
package me.eccentric_nz.tardis.sonic;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.commands.preferences.TardisPrefsMenuInventory;
import me.eccentric_nz.tardis.sonic.actions.*;
import me.eccentric_nz.tardis.utility.TardisMaterials;
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
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisSonicListener implements Listener {

    private final TardisPlugin plugin;
    private final Material sonic;
    private final List<Material> diamond = new ArrayList<>();
    private final List<Material> doors = new ArrayList<>();
    private final List<Material> redstone = new ArrayList<>();
    private final List<Material> ignite = new ArrayList<>();

    public TardisSonicListener(TardisPlugin plugin) {
        this.plugin = plugin;
        String[] split = Objects.requireNonNull(plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result")).split(":");
        sonic = Material.valueOf(split[0]);
        diamond.add(Material.COBWEB);
        diamond.add(Material.IRON_BARS);
        diamond.add(Material.SNOW);
        diamond.add(Material.SNOW_BLOCK);
        diamond.addAll(TardisMaterials.glass);
        doors.add(Material.ACACIA_DOOR);
        doors.add(Material.ACACIA_TRAPDOOR);
        doors.add(Material.BIRCH_DOOR);
        doors.add(Material.BIRCH_TRAPDOOR);
        doors.add(Material.CRIMSON_DOOR);
        doors.add(Material.CRIMSON_TRAPDOOR);
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
        doors.add(Material.WARPED_DOOR);
        doors.add(Material.WARPED_TRAPDOOR);
        redstone.add(Material.DETECTOR_RAIL);
        redstone.add(Material.IRON_DOOR);
        redstone.add(Material.IRON_TRAPDOOR);
        redstone.add(Material.MUSHROOM_STEM);
        redstone.add(Material.PISTON);
        redstone.add(Material.STICKY_PISTON);
        redstone.add(Material.POWERED_RAIL);
        redstone.add(Material.REDSTONE_LAMP);
        redstone.add(Material.REDSTONE_WIRE);
        redstone.add(Material.IRON_TRAPDOOR);
        ignite.add(Material.CAMPFIRE);
        ignite.add(Material.NETHERRACK);
        ignite.add(Material.OBSIDIAN);
        ignite.add(Material.SOUL_CAMPFIRE);
        ignite.add(Material.SOUL_SAND);
        ignite.add(Material.SOUL_SOIL);
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
            assert im != null;
            if (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver")) {
                List<String> lore = im.getLore();
                Action action = event.getAction();
                if (action.equals(Action.RIGHT_CLICK_AIR) && !player.isSneaking()) {
                    // rebuild Police Box if dispersed by HADS
                    if (plugin.getTrackerKeeper().getDispersed().containsKey(player.getUniqueId())) {
                        TardisSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        TardisSonicDispersed.assemble(plugin, player);
                    } else if (lore != null && (lore.contains("Bio-scanner Upgrade") || lore.contains("Knockback Upgrade"))) {
                        TardisSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        if (TardisPermission.hasPermission(player, "tardis.sonic.freeze")) {
                            // freeze target player
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                Player target = TardisSonicFreeze.getTargetPlayer(player);
                                if (target != null) {
                                    TardisSonicFreeze.immobilise(plugin, player, target);
                                }
                            }, 20L);
                        }
                        if (TardisPermission.hasPermission(player, "tardis.sonic.knockback")) {
                            // knockback any monsters in line of sight
                            Entity target = TardisSonicKnockback.getTargetEntity(player);
                            if (target != null) {
                                TardisSonicKnockback.knockback(player, target);
                            }
                        }
                    }
                    if (TardisPermission.hasPermission(player, "tardis.sonic.standard")) {
                        TardisSonic.standardSonic(plugin, player, now);
                        return;
                    }
                }
                if (action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking() && TardisPermission.hasPermission(player, "tardis.sonic.standard")) {
                    Inventory ppm = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Player Prefs Menu");
                    ppm.setContents(new TardisPrefsMenuInventory(plugin, player.getUniqueId()).getMenu());
                    player.openInventory(ppm);
                    return;
                }
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block block = event.getClickedBlock();
                    assert block != null;
                    if (doors.contains(block.getType()) && player.hasPermission("tardis.admin") && lore != null && lore.contains("Admin Upgrade")) {
                        // display tardis info
                        TardisSonicAdmin.displayInfo(plugin, player, block);
                    }
                    if (Tag.WALL_SIGNS.isTagged(block.getType()) && TardisPermission.hasPermission(player, "tardis.atmospheric")) {
                        // make it snow
                        TardisSonicAtmospheric.makeItSnow(plugin, player, block);
                        return;
                    }
                    if (TardisPermission.hasPermission(player, "tardis.sonic.arrow") && lore != null && lore.contains("Pickup Arrows Upgrade")) {
                        if (!block.getType().isInteractable()) {
                            TardisSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
                        }
                        // scan area around block for an arrow
                        List<Entity> nearbyEntites = block.getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2).stream().toList();
                        for (Entity e : nearbyEntites) {
                            if (e instanceof Arrow arrow) {
                                // pick up arrow
                                arrow.setPickupStatus(AbstractArrow.PickupStatus.ALLOWED);
                                return;
                            }
                        }
                    }
                    if (redstone.contains(block.getType()) && TardisPermission.hasPermission(player, "tardis.sonic.redstone") && lore != null && lore.contains("Redstone Upgrade")) {
                        // toggle powered state
                        TardisSonicRedstone.togglePoweredState(plugin, player, block);
                        return;
                    }
                    if (!redstone.contains(block.getType()) && TardisPermission.hasPermission(player, "tardis.sonic.emerald") && lore != null && lore.contains("Emerald Upgrade") && !block.getType().isInteractable()) {
                        TardisSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        // scan environment
                        TardisSonicScanner.scan(plugin, block.getLocation(), player);
                    }
                }
                if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                    Block block = event.getClickedBlock();
                    if (!player.isSneaking()) {
                        assert block != null;
                        if ((block.getType().isBurnable() || ignite.contains(block.getType())) && TardisPermission.hasPermission(player, "tardis.sonic.ignite") && lore != null && lore.contains("Ignite Upgrade")) {
                            TardisSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_short");
                            // ignite block
                            TardisSonicIgnite.ignite(plugin, block, player);
                        }
                        if (diamond.contains(block.getType()) && TardisPermission.hasPermission(player, "tardis.sonic.diamond") && lore != null && lore.contains("Diamond Upgrade")) {
                            // break block
                            TardisSonicDisruptor.breakBlock(plugin, player, block);
                        }
                    } else {
                        assert block != null;
                        if (TardisSonicPainter.getPaintable().contains(block.getType()) && TardisPermission.hasPermission(player, "tardis.sonic.paint") && lore != null && lore.contains("Painter Upgrade")) {
                            // paint the block
                            TardisSonicPainter.paint(plugin, player, block);
                        }
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
