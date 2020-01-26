/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.enumeration.WORLD_MANAGER;
import me.eccentric_nz.TARDIS.sonic.actions.*;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.eccentric_nz.TARDIS.listeners.TARDISScannerListener.getNearbyEntities;

/**
 * @author eccentric_nz
 */
public class TARDISSonicListener implements Listener {

    private final TARDIS plugin;
    private final Material sonic;
    private final List<Material> diamond = new ArrayList<>();
    private final List<Material> doors = new ArrayList<>();
    private final List<Material> redstone = new ArrayList<>();

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
        redstone.add(Material.PISTON);
        redstone.add(Material.STICKY_PISTON);
        redstone.add(Material.POWERED_RAIL);
        redstone.add(Material.REDSTONE_LAMP);
        redstone.add(Material.REDSTONE_WIRE);
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
                    TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                    // rebuild Police Box if dispersed by HADS
                    if (plugin.getTrackerKeeper().getDispersed().containsKey(player.getUniqueId())) {
                        TARDISSonicDispersed.assemble(plugin, player);
                    } else if (player.hasPermission("tardis.sonic.freeze") && lore != null && lore.contains("Bio-scanner Upgrade")) {
                        // freeze target player
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            Player target = TARDISSonicFreeze.getTargetPlayer(player);
                            if (target != null) {
                                TARDISSonicFreeze.immobilise(plugin, player, target);
                            }
                        }, 20L);
                    }
                    if (player.hasPermission("tardis.sonic.standard")) {
                        TARDISSonic.standardSonic(plugin, player);
                        return;
                    }
                }
                if (action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking() && player.hasPermission("tardis.sonic.standard")) {
                    Inventory ppm = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Player Prefs Menu");
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
                        scan(block.getLocation(), player);
                    }
                    if (redstone.contains(block.getType()) && player.hasPermission("tardis.sonic.redstone") && lore != null && lore.contains("Redstone Upgrade")) {
                        // toggle powered state
                        TARDISSonicRedstone.togglePoweredState(plugin, player, block);
                    }
                }
                if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                    Block block = event.getClickedBlock();
                    if (!player.isSneaking()) {
                        if ((block.getType().isBurnable() || block.getType().equals(Material.NETHERRACK)) && player.hasPermission("tardis.sonic.ignite") && lore != null && lore.contains("Ignite Upgrade")) {
                            TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_short");
                            // ignite block
                            ignite(block, player);
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

    private void scan(Location scan_loc, Player player) {
        // record nearby entities
        HashMap<EntityType, Integer> scannedentities = new HashMap<>();
        List<String> playernames = new ArrayList<>();
        getNearbyEntities(scan_loc, 16).forEach((k) -> {
            EntityType et = k.getType();
            if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                Integer entity_count = scannedentities.getOrDefault(et, 0);
                boolean visible = true;
                if (et.equals(EntityType.PLAYER)) {
                    Player entPlayer = (Player) k;
                    if (player.canSee(entPlayer)) {
                        playernames.add(entPlayer.getName());
                    } else {
                        visible = false;
                    }
                }
                if (visible) {
                    scannedentities.put(et, entity_count + 1);
                }
            }
        });
        long time = scan_loc.getWorld().getTime();
        String daynight = TARDISStaticUtils.getTime(time);
        String worldname;
        if (plugin.getWorldManager().equals(WORLD_MANAGER.MULTIVERSE)) {
            worldname = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            worldname = scan_loc.getWorld().getName();
        }
        String wn = worldname;
        // message the player
        TARDISMessage.send(player, "SONIC_SCAN");
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            TARDISMessage.send(player, "SCAN_WORLD", wn);
            TARDISMessage.send(player, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
        }, 20L);
        // get biome
        Biome biome = scan_loc.getBlock().getBiome();
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "BIOME_TYPE", biome.toString()), 40L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TIME", daynight + " / " + time), 60L);
        // get weather
        String weather;
        switch (biome) {
            case BADLANDS:
            case BADLANDS_PLATEAU:
            case DESERT:
            case DESERT_HILLS:
            case DESERT_LAKES:
            case ERODED_BADLANDS:
            case MODIFIED_BADLANDS_PLATEAU:
            case MODIFIED_WOODED_BADLANDS_PLATEAU:
            case SAVANNA:
            case SAVANNA_PLATEAU:
            case SHATTERED_SAVANNA:
            case SHATTERED_SAVANNA_PLATEAU:
            case WOODED_BADLANDS_PLATEAU:
                weather = plugin.getLanguage().getString("WEATHER_DRY");
                break;
            case FROZEN_OCEAN:
            case FROZEN_RIVER:
            case ICE_SPIKES:
            case SNOWY_BEACH:
            case SNOWY_MOUNTAINS:
            case SNOWY_TAIGA:
            case SNOWY_TAIGA_HILLS:
            case SNOWY_TAIGA_MOUNTAINS:
            case SNOWY_TUNDRA:
                weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
                break;
            default:
                weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
                break;
        }
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_WEATHER", weather), 80L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity())), 100L);
        bsched.scheduleSyncDelayedTask(plugin, () -> TARDISMessage.send(player, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature())), 120L);
        bsched.scheduleSyncDelayedTask(plugin, () -> {
            TARDISMessage.send(player, "SCAN_ENTS");
            if (scannedentities.size() > 0) {
                scannedentities.forEach((key, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (key.equals(EntityType.PLAYER) && playernames.size() > 0) {
                        playernames.forEach((p) -> buf.append(", ").append(p));
                        message = " (" + buf.toString().substring(2) + ")";
                    }
                    player.sendMessage("    " + key + ": " + value + message);
                });
                scannedentities.clear();
            } else {
                TARDISMessage.send(player, "SCAN_NONE");
            }
        }, 140L);
    }

    private void ignite(Block b, Player p) {
        if (!TARDISSonicRespect.checkBlockRespect(plugin, p, b)) {
            Block above = b.getRelative(BlockFace.UP);
            if (b.getType().equals(Material.TNT)) {
                b.setBlockData(TARDISConstants.AIR);
                b.getWorld().spawnEntity(b.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.PRIMED_TNT);
                plugin.getPM().callEvent(new BlockIgniteEvent(b, IgniteCause.FLINT_AND_STEEL, p));
                return;
            }
            if (above.getType().equals(Material.AIR)) {
                above.setBlockData(TARDISConstants.FIRE);
                // call a block ignite event
                plugin.getPM().callEvent(new BlockIgniteEvent(b, IgniteCause.FLINT_AND_STEEL, p));
            }
        }
    }
}
