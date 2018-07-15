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

import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.utility.*;
import nl.rutgerkok.blocklocker.BlockLockerAPI;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.type.Piston;
import org.bukkit.block.data.type.RedstoneRail;
import org.bukkit.block.data.type.RedstoneWire;
import org.bukkit.block.data.type.Snow;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;
import org.yi.acru.bukkit.Lockette.Lockette;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static me.eccentric_nz.TARDIS.listeners.TARDISScannerListener.getNearbyEntities;

/**
 * @author eccentric_nz
 */
public class TARDISSonicListener implements Listener {

    private final TARDIS plugin;
    private final Material sonic;
    private final HashMap<UUID, Long> timeout = new HashMap<>();
    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final List<Material> diamond = new ArrayList<>();
    private final List<Material> distance = new ArrayList<>();
    private final List<Material> doors = new ArrayList<>();
    private final List<Material> redstone = new ArrayList<>();
    private final List<UUID> frozenPlayers = new ArrayList<>();
    private final List<BlockFace> faces = new ArrayList<>();
    private final List<Material> paintable = new ArrayList<>();

    public TARDISSonicListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        sonic = Material.valueOf(split[0]);
        diamond.add(Material.COBWEB);
        diamond.add(Material.IRON_BARS);
        diamond.add(Material.SNOW);
        diamond.add(Material.SNOW_BLOCK);
        diamond.addAll(TARDISMaterials.glass);
        distance.add(Material.ACACIA_BUTTON);
        distance.add(Material.ACACIA_DOOR);
        distance.add(Material.BIRCH_BUTTON);
        distance.add(Material.BIRCH_DOOR);
        distance.add(Material.DARK_OAK_BUTTON);
        distance.add(Material.DARK_OAK_DOOR);
        distance.add(Material.IRON_DOOR);
        distance.add(Material.JUNGLE_BUTTON);
        distance.add(Material.JUNGLE_DOOR);
        distance.add(Material.LEVER);
        distance.add(Material.OAK_BUTTON);
        distance.add(Material.OAK_DOOR);
        distance.add(Material.SPRUCE_BUTTON);
        distance.add(Material.SPRUCE_DOOR);
        distance.add(Material.STONE_BUTTON);
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
        faces.add(BlockFace.NORTH);
        faces.add(BlockFace.SOUTH);
        faces.add(BlockFace.EAST);
        faces.add(BlockFace.WEST);
        faces.add(BlockFace.UP);
        faces.add(BlockFace.DOWN);
        paintable.add(Material.BLACK_CARPET);
        paintable.add(Material.BLACK_STAINED_GLASS);
        paintable.add(Material.BLACK_STAINED_GLASS_PANE);
        paintable.add(Material.BLACK_TERRACOTTA);
        paintable.add(Material.BLACK_WOOL);
        paintable.add(Material.BLUE_CARPET);
        paintable.add(Material.BLUE_STAINED_GLASS);
        paintable.add(Material.BLUE_STAINED_GLASS_PANE);
        paintable.add(Material.BLUE_TERRACOTTA);
        paintable.add(Material.BLUE_WOOL);
        paintable.add(Material.BROWN_CARPET);
        paintable.add(Material.BROWN_STAINED_GLASS);
        paintable.add(Material.BROWN_STAINED_GLASS_PANE);
        paintable.add(Material.BROWN_TERRACOTTA);
        paintable.add(Material.BROWN_WOOL);
        paintable.add(Material.CYAN_CARPET);
        paintable.add(Material.CYAN_STAINED_GLASS);
        paintable.add(Material.CYAN_STAINED_GLASS_PANE);
        paintable.add(Material.CYAN_TERRACOTTA);
        paintable.add(Material.CYAN_WOOL);
        paintable.add(Material.GRAY_CARPET);
        paintable.add(Material.GRAY_STAINED_GLASS);
        paintable.add(Material.GRAY_STAINED_GLASS_PANE);
        paintable.add(Material.GRAY_TERRACOTTA);
        paintable.add(Material.GRAY_WOOL);
        paintable.add(Material.GREEN_CARPET);
        paintable.add(Material.GREEN_STAINED_GLASS);
        paintable.add(Material.GREEN_STAINED_GLASS_PANE);
        paintable.add(Material.GREEN_TERRACOTTA);
        paintable.add(Material.GREEN_WOOL);
        paintable.add(Material.LIGHT_BLUE_CARPET);
        paintable.add(Material.LIGHT_BLUE_STAINED_GLASS);
        paintable.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        paintable.add(Material.LIGHT_BLUE_TERRACOTTA);
        paintable.add(Material.LIGHT_BLUE_WOOL);
        paintable.add(Material.LIGHT_GRAY_CARPET);
        paintable.add(Material.LIGHT_GRAY_STAINED_GLASS);
        paintable.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        paintable.add(Material.LIGHT_GRAY_TERRACOTTA);
        paintable.add(Material.LIGHT_GRAY_WOOL);
        paintable.add(Material.LIME_CARPET);
        paintable.add(Material.LIME_STAINED_GLASS);
        paintable.add(Material.LIME_STAINED_GLASS_PANE);
        paintable.add(Material.LIME_TERRACOTTA);
        paintable.add(Material.LIME_WOOL);
        paintable.add(Material.MAGENTA_CARPET);
        paintable.add(Material.MAGENTA_STAINED_GLASS);
        paintable.add(Material.MAGENTA_STAINED_GLASS_PANE);
        paintable.add(Material.MAGENTA_TERRACOTTA);
        paintable.add(Material.MAGENTA_WOOL);
        paintable.add(Material.ORANGE_CARPET);
        paintable.add(Material.ORANGE_STAINED_GLASS);
        paintable.add(Material.ORANGE_STAINED_GLASS_PANE);
        paintable.add(Material.ORANGE_TERRACOTTA);
        paintable.add(Material.ORANGE_WOOL);
        paintable.add(Material.PINK_CARPET);
        paintable.add(Material.PINK_STAINED_GLASS);
        paintable.add(Material.PINK_STAINED_GLASS_PANE);
        paintable.add(Material.PINK_TERRACOTTA);
        paintable.add(Material.PINK_WOOL);
        paintable.add(Material.PURPLE_CARPET);
        paintable.add(Material.PURPLE_STAINED_GLASS);
        paintable.add(Material.PURPLE_STAINED_GLASS_PANE);
        paintable.add(Material.PURPLE_TERRACOTTA);
        paintable.add(Material.PURPLE_WOOL);
        paintable.add(Material.RED_CARPET);
        paintable.add(Material.RED_STAINED_GLASS);
        paintable.add(Material.RED_STAINED_GLASS_PANE);
        paintable.add(Material.RED_TERRACOTTA);
        paintable.add(Material.RED_WOOL);
        paintable.add(Material.WHITE_CARPET);
        paintable.add(Material.WHITE_STAINED_GLASS);
        paintable.add(Material.WHITE_STAINED_GLASS_PANE);
        paintable.add(Material.WHITE_TERRACOTTA);
        paintable.add(Material.WHITE_WOOL);
        paintable.add(Material.YELLOW_CARPET);
        paintable.add(Material.YELLOW_STAINED_GLASS);
        paintable.add(Material.YELLOW_STAINED_GLASS_PANE);
        paintable.add(Material.YELLOW_TERRACOTTA);
        paintable.add(Material.YELLOW_WOOL);
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
                    playSonicSound(player, now, 3050L, "sonic_screwdriver");
                    // rebuild Police Box if dispersed by HADS
                    if (plugin.getTrackerKeeper().getDispersed().containsKey(player.getUniqueId())) {
                        // check player's location
                        Location tmp = player.getLocation();
                        Location pl = new Location(tmp.getWorld(), tmp.getBlockX(), tmp.getBlockY(), tmp.getBlockZ());
                        Location pb = plugin.getTrackerKeeper().getDispersed().get(player.getUniqueId());
                        if (pl.equals(pb)) {
                            UUID uuid = player.getUniqueId();
                            // get TARDIS id
                            ResultSetTardisID rs = new ResultSetTardisID(plugin);
                            if (rs.fromUUID(uuid.toString())) {
                                // rebuild
                                plugin.getTrackerKeeper().getDispersed().remove(uuid);
                                plugin.getTrackerKeeper().getDispersedTARDII().remove(Integer.valueOf(rs.getTardis_id()));
                                player.performCommand("tardis rebuild");
                            }
                        }
                    }
                    if (player.hasPermission("tardis.sonic.freeze") && lore != null && lore.contains("Bio-scanner Upgrade")) {
                        long cool = System.currentTimeMillis();
                        if ((!cooldown.containsKey(player.getUniqueId()) || cooldown.get(player.getUniqueId()) < cool)) {
                            cooldown.put(player.getUniqueId(), cool + (plugin.getConfig().getInt("preferences.freeze_cooldown") * 1000L));
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                Location observerPos = player.getEyeLocation();
                                TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
                                TARDISVector3D observerStart = new TARDISVector3D(observerPos);
                                TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));

                                Player hit = null;
                                // Get nearby entities
                                for (Player target : player.getWorld().getPlayers()) {
                                    // Bounding box of the given player
                                    TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
                                    TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
                                    TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
                                    if (target != player && hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                                        if (hit == null || hit.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                                            hit = target;
                                        }
                                    }
                                }
                                // freeze the closest player
                                if (hit != null) {
                                    TARDISMessage.send(hit, "FREEZE", player.getName());
                                    UUID uuid = hit.getUniqueId();
                                    frozenPlayers.add(uuid);
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> frozenPlayers.remove(uuid), 100L);
                                } else if (player.hasPermission("tardis.sonic.standard")) {
                                    standardSonic(player);
                                }
                            }, 20L);
                        } else {
                            TARDISMessage.send(player, "FREEZE_NO");
                        }
                        return;
                    }
                    if (player.hasPermission("tardis.sonic.standard")) {
                        standardSonic(player);
                        return;
                    }
                }
                if (action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking() && player.hasPermission("tardis.sonic.standard")) {
                    Inventory ppm = plugin.getServer().createInventory(player, 27, "§4Player Prefs Menu");
                    ppm.setContents(new TARDISPrefsMenuInventory(plugin, player.getUniqueId()).getMenu());
                    player.openInventory(ppm);
                    return;
                }
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    Block b = event.getClickedBlock();
                    if (doors.contains(b.getType()) && player.hasPermission("tardis.admin") && lore != null && lore.contains("Admin Upgrade")) {
                        playSonicSound(player, now, 600L, "sonic_short");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            HashMap<String, Object> wheredoor = new HashMap<>();
                            Location loc = b.getLocation();
                            String bw = loc.getWorld().getName();
                            int bx = loc.getBlockX();
                            int by = loc.getBlockY();
                            int bz = loc.getBlockZ();
                            if (Tag.DOORS.isTagged(b.getType())) {
                                Bisected bisected = (Bisected) b;
                                if (bisected.getHalf().equals(Half.TOP)) {
                                    by = (by - 1);
                                }
                            }
                            String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                            wheredoor.put("door_location", doorloc);
                            wheredoor.put("door_type", 0);
                            ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
                            if (rsd.resultSet()) {
                                int id = rsd.getTardis_id();
                                // get the TARDIS owner's name
                                HashMap<String, Object> wheren = new HashMap<>();
                                wheren.put("tardis_id", id);
                                ResultSetTardis rsn = new ResultSetTardis(plugin, wheren, "", false, 0);
                                if (rsn.resultSet()) {
                                    Tardis tardis = rsn.getTardis();
                                    String name = plugin.getServer().getOfflinePlayer(tardis.getUuid()).getName();
                                    TARDISMessage.send(player, "TARDIS_WHOSE", name);
                                    int percent = Math.round((tardis.getArtron_level() * 100F) / plugin.getArtronConfig().getInt("full_charge"));
                                    TARDISMessage.send(player, "ENERGY_LEVEL", String.format("%d", percent));
                                    HashMap<String, Object> whereb = new HashMap<>();
                                    whereb.put("tardis_id", id);
                                    ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, whereb);
                                    if (rsb.resultSet()) {
                                        TARDISMessage.send(player, "SCAN_LAST", rsb.getWorld().getName() + " " + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ());
                                    }
                                }
                                HashMap<String, Object> whereid = new HashMap<>();
                                whereid.put("tardis_id", id);
                                ResultSetTravellers rst = new ResultSetTravellers(plugin, whereid, true);
                                if (rst.resultSet()) {
                                    List<UUID> data = rst.getData();
                                    TARDISMessage.send(player, "SONIC_INSIDE");
                                    data.forEach((s) -> {
                                        Player p = plugin.getServer().getPlayer(s);
                                        if (p != null) {
                                            player.sendMessage(p.getDisplayName());
                                        } else {
                                            player.sendMessage(plugin.getServer().getOfflinePlayer(s).getName() + " (Offline)");
                                        }
                                    });
                                } else {
                                    TARDISMessage.send(player, "SONIC_OCCUPY");
                                }
                            }
                        }, 60L);
                    }
                    if (b.getType().equals(Material.WALL_SIGN) && player.hasPermission("tardis.atmospheric")) {
                        // check the text on the sign
                        Sign sign = (Sign) b.getState();
                        String line0 = ChatColor.stripColor(sign.getLine(0));
                        String line1 = ChatColor.stripColor(sign.getLine(1));
                        String line2 = ChatColor.stripColor(sign.getLine(2));
                        if (isPresetSign(line0, line1, line2)) {
                            // get TARDIS id
                            ResultSetTardisID rs = new ResultSetTardisID(plugin);
                            if (rs.fromUUID(player.getUniqueId().toString())) {
                                int tid = rs.getTardis_id();
                                Block blockbehind = null;
                                Directional directional = (Directional) b.getBlockData();
                                if (directional.getFacing().equals(BlockFace.WEST)) {
                                    blockbehind = b.getRelative(BlockFace.EAST, 2);
                                }
                                if (directional.getFacing().equals(BlockFace.EAST)) {
                                    blockbehind = b.getRelative(BlockFace.WEST, 2);
                                }
                                if (directional.getFacing().equals(BlockFace.SOUTH)) {
                                    blockbehind = b.getRelative(BlockFace.NORTH, 2);
                                }
                                if (directional.getFacing().equals(BlockFace.NORTH)) {
                                    blockbehind = b.getRelative(BlockFace.SOUTH, 2);
                                }
                                if (blockbehind != null) {
                                    Block blockDown = blockbehind.getRelative(BlockFace.DOWN, 2);
                                    Location bd_loc = blockDown.getLocation();
                                    HashMap<String, Object> wherecl = new HashMap<>();
                                    wherecl.put("tardis_id", tid);
                                    wherecl.put("world", bd_loc.getWorld().getName());
                                    wherecl.put("x", bd_loc.getBlockX());
                                    wherecl.put("y", bd_loc.getBlockY());
                                    wherecl.put("z", bd_loc.getBlockZ());
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (rsc.resultSet()) {
                                        new TARDISAtmosphericExcitation(plugin).excite(tid, player);
                                        plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
                                        return;
                                    }
                                }
                            }
                        }
                    }
                    if (!redstone.contains(b.getType()) && player.hasPermission("tardis.sonic.emerald") && lore != null && lore.contains("Emerald Upgrade") && !plugin.getGeneralKeeper().getInteractables().contains(b.getType())) {
                        playSonicSound(player, now, 3050L, "sonic_screwdriver");
                        // scan environment
                        scan(b.getLocation(), player);
                    }
                    if (redstone.contains(b.getType()) && player.hasPermission("tardis.sonic.redstone") && lore != null && lore.contains("Redstone Upgrade")) {
                        // not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
                        if (checkBlockRespect(player, b)) {
                            return;
                        }
                        playSonicSound(player, now, 600L, "sonic_short");
                        Material blockType = b.getType();
                        BlockState bs = b.getState();
                        // do redstone activation
                        switch (blockType) {
                            case DETECTOR_RAIL:
                            case POWERED_RAIL:
                                RedstoneRail rail = (RedstoneRail) b.getBlockData();
                                if (plugin.getGeneralKeeper().getSonicRails().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicRails().remove(b.getLocation().toString());
                                    rail.setPowered(false);
                                } else {
                                    plugin.getGeneralKeeper().getSonicRails().add(b.getLocation().toString());
                                    rail.setPowered(true);
                                }
                                b.setBlockData(rail, true);
                                break;
                            case IRON_DOOR:
                                // get bottom door block
                                Block tmp = b;
                                Bisected bisected = (Bisected) b.getBlockData();
                                if (bisected.getHalf().equals(Half.TOP)) {
                                    tmp = b.getRelative(BlockFace.DOWN);
                                }
                                // not TARDIS doors!
                                String doorloc = tmp.getLocation().getWorld().getName() + ":" + tmp.getLocation().getBlockX() + ":" + tmp.getLocation().getBlockY() + ":" + tmp.getLocation().getBlockZ();
                                HashMap<String, Object> wheredoor = new HashMap<>();
                                wheredoor.put("door_location", doorloc);
                                ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
                                if (rsd.resultSet()) {
                                    return;
                                }
                                if (!plugin.getTrackerKeeper().getSonicDoors().contains(player.getUniqueId())) {
                                    plugin.getTrackerKeeper().getSonicDoors().add(player.getUniqueId());
                                    Block door_bottom = tmp;
                                    Openable openable = (Openable) door_bottom.getBlockData();
                                    openable.setOpen(true);
                                    door_bottom.setBlockData(openable, true);
                                    // return the door to its previous state after 3 seconds
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        openable.setOpen(false);
                                        door_bottom.setBlockData(openable, true);
                                        plugin.getTrackerKeeper().getSonicDoors().remove(player.getUniqueId());
                                    }, 60L);
                                }
                                break;
                            case PISTON:
                            case STICKY_PISTON:
                                Piston piston = (Piston) b.getBlockData();
                                // find the direction the piston is facing
                                if (plugin.getGeneralKeeper().getSonicPistons().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicPistons().remove(b.getLocation().toString());
                                    for (BlockFace f : faces) {
                                        if (b.getRelative(f).getType().equals(Material.AIR)) {
                                            b.getRelative(f).setType(Material.GLASS, true);
                                            b.getRelative(f).setType(Material.AIR, true);
                                            break;
                                        }
                                    }
                                } else if (setExtension(b)) {
                                    plugin.getGeneralKeeper().getSonicPistons().add(b.getLocation().toString());
                                    piston.setExtended(true);
                                    b.setBlockData(piston, true);
                                    player.playSound(b.getLocation(), Sound.BLOCK_PISTON_EXTEND, 1.0f, 1.0f);
                                }
                                break;
                            case REDSTONE_LAMP:
                                if (blockType.equals(Material.REDSTONE_LAMP)) {
                                    plugin.getGeneralKeeper().getSonicLamps().add(b.getLocation().toString());
                                    for (BlockFace f : faces) {
                                        if (b.getRelative(f).getType().equals(Material.AIR)) {
                                            b.getRelative(f).setType(Material.REDSTONE_BLOCK, true);
                                            b.setType(Material.REDSTONE_LAMP);
                                            b.getRelative(f).setType(Material.AIR, true);
                                            break;
                                        }
                                    }
                                } else if (plugin.getGeneralKeeper().getSonicLamps().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicLamps().remove(b.getLocation().toString());
                                    b.setType(Material.REDSTONE_LAMP);
                                }
                                break;
                            case REDSTONE_WIRE:
                                RedstoneWire wire = (RedstoneWire) b.getBlockData();
                                if (plugin.getGeneralKeeper().getSonicWires().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicWires().remove(b.getLocation().toString());
                                    wire.setPower(0);
                                    faces.forEach((f) -> {
                                        if (b.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
                                            // TODO check: should be b.getRelative(f).setPower(0)?
                                            wire.setPower(0);
                                        }
                                    });
                                    b.setBlockData(wire, true);
                                } else {
                                    plugin.getGeneralKeeper().getSonicWires().add(b.getLocation().toString());
                                    wire.setPower(15);
                                    faces.forEach((f) -> {
                                        if (b.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
                                            // TODO check: should be b.getRelative(f).setPower(13)?
                                            wire.setPower(13);
                                        }
                                    });
                                    b.setBlockData(wire, true);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
                if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                    Block b = event.getClickedBlock();
                    if (!player.isSneaking()) {
                        if ((b.getType().isBurnable() || b.getType().equals(Material.NETHERRACK)) && player.hasPermission("tardis.sonic.ignite") && lore != null && lore.contains("Ignite Upgrade")) {
                            playSonicSound(player, now, 3050L, "sonic_short");
                            // ignite block
                            ignite(b, player);
                        }
                        if (diamond.contains(b.getType()) && player.hasPermission("tardis.sonic.diamond") && lore != null && lore.contains("Diamond Upgrade")) {
                            // not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
                            if (checkBlockRespect(player, b)) {
                                TARDISMessage.send(player, "SONIC_PROTECT");
                                return;
                            }
                            playSonicSound(player, now, 600L, "sonic_short");
                            // drop appropriate material
                            Material mat = b.getType();
                            if (player.hasPermission("tardis.sonic.silktouch")) {
                                Location l = b.getLocation();
                                if (mat.equals(Material.SNOW)) {
                                    Snow snow = (Snow) b;
                                    b.getLocation().getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.SNOWBALL, 1 + snow.getLayers()));
                                } else {
                                    l.getWorld().dropItemNaturally(l, new ItemStack(b.getType(), 1));
                                }
                                l.getWorld().playSound(l, Sound.ENTITY_SHEEP_SHEAR, 1.0F, 1.5F);
                                // set the block to AIR
                                b.setType(Material.AIR);
                            } else if (mat.equals(Material.SNOW) || mat.equals(Material.SNOW_BLOCK)) {
                                // how many?
                                int balls;
                                if (mat.equals(Material.SNOW_BLOCK)) {
                                    balls = 4;
                                } else {
                                    Snow snow = (Snow) b;
                                    balls = 1 + snow.getLayers();
                                }
                                b.setType(Material.AIR);
                                b.getLocation().getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.SNOWBALL, balls));
                            } else {
                                b.breakNaturally();
                                b.getLocation().getWorld().playSound(b.getLocation(), Sound.ENTITY_SHEEP_SHEAR, 1.0F, 1.5F);
                            }
                        }
                    } else if (paintable.contains(b.getType()) && player.hasPermission("tardis.sonic.paint") && lore != null && lore.contains("Painter Upgrade")) {
                        // must be in TARDIS world
                        if (!plugin.getUtils().inTARDISWorld(player)) {
                            TARDISMessage.send(player, "UPDATE_IN_WORLD");
                            return;
                        }
                        // not protected blocks - WorldGuard / GriefPrevention / Lockette / Towny
                        if (checkBlockRespect(player, b)) {
                            TARDISMessage.send(player, "SONIC_PROTECT");
                            return;
                        }
                        playSonicSound(player, now, 600L, "sonic_short");
                        // check for dye in slot
                        PlayerInventory inv = player.getInventory();
                        ItemStack dye = inv.getItem(8);
                        if (dye == null || !TARDISMaterials.dyes.contains(dye.getType())) {
                            TARDISMessage.send(player, "SONIC_DYE");
                            return;
                        }
                        // don't do anything if it is the same colour
                        switch (b.getType()) {
                            case BLACK_CARPET:
                            case BLACK_STAINED_GLASS:
                            case BLACK_STAINED_GLASS_PANE:
                            case BLACK_TERRACOTTA:
                            case BLACK_WOOL:
                                if (!dye.getType().equals(Material.INK_SAC)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case BLUE_CARPET:
                            case BLUE_STAINED_GLASS:
                            case BLUE_STAINED_GLASS_PANE:
                            case BLUE_TERRACOTTA:
                            case BLUE_WOOL:
                                if (!dye.getType().equals(Material.LAPIS_LAZULI)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case BROWN_CARPET:
                            case BROWN_STAINED_GLASS:
                            case BROWN_STAINED_GLASS_PANE:
                            case BROWN_TERRACOTTA:
                            case BROWN_WOOL:
                                if (!dye.getType().equals(Material.COCOA_BEANS)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case CYAN_CARPET:
                            case CYAN_STAINED_GLASS:
                            case CYAN_STAINED_GLASS_PANE:
                            case CYAN_TERRACOTTA:
                            case CYAN_WOOL:
                                if (!dye.getType().equals(Material.CYAN_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case GRAY_CARPET:
                            case GRAY_STAINED_GLASS:
                            case GRAY_STAINED_GLASS_PANE:
                            case GRAY_TERRACOTTA:
                            case GRAY_WOOL:
                                if (!dye.getType().equals(Material.GRAY_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case GREEN_CARPET:
                            case GREEN_STAINED_GLASS:
                            case GREEN_STAINED_GLASS_PANE:
                            case GREEN_TERRACOTTA:
                            case GREEN_WOOL:
                                if (!dye.getType().equals(Material.CACTUS_GREEN)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case LIGHT_BLUE_CARPET:
                            case LIGHT_BLUE_STAINED_GLASS:
                            case LIGHT_BLUE_STAINED_GLASS_PANE:
                            case LIGHT_BLUE_TERRACOTTA:
                            case LIGHT_BLUE_WOOL:
                                if (!dye.getType().equals(Material.LIGHT_BLUE_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case LIGHT_GRAY_CARPET:
                            case LIGHT_GRAY_STAINED_GLASS:
                            case LIGHT_GRAY_STAINED_GLASS_PANE:
                            case LIGHT_GRAY_TERRACOTTA:
                            case LIGHT_GRAY_WOOL:
                                if (!dye.getType().equals(Material.LIGHT_GRAY_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case LIME_CARPET:
                            case LIME_STAINED_GLASS:
                            case LIME_STAINED_GLASS_PANE:
                            case LIME_TERRACOTTA:
                            case LIME_WOOL:
                                if (!dye.getType().equals(Material.LIME_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case MAGENTA_CARPET:
                            case MAGENTA_STAINED_GLASS:
                            case MAGENTA_STAINED_GLASS_PANE:
                            case MAGENTA_TERRACOTTA:
                            case MAGENTA_WOOL:
                                if (!dye.getType().equals(Material.MAGENTA_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case ORANGE_CARPET:
                            case ORANGE_STAINED_GLASS:
                            case ORANGE_STAINED_GLASS_PANE:
                            case ORANGE_TERRACOTTA:
                            case ORANGE_WOOL:
                                if (!dye.getType().equals(Material.ORANGE_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case PINK_CARPET:
                            case PINK_STAINED_GLASS:
                            case PINK_STAINED_GLASS_PANE:
                            case PINK_TERRACOTTA:
                            case PINK_WOOL:
                                if (!dye.getType().equals(Material.PINK_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case PURPLE_CARPET:
                            case PURPLE_STAINED_GLASS:
                            case PURPLE_STAINED_GLASS_PANE:
                            case PURPLE_TERRACOTTA:
                            case PURPLE_WOOL:
                                if (!dye.getType().equals(Material.PURPLE_DYE)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case RED_CARPET:
                            case RED_STAINED_GLASS:
                            case RED_STAINED_GLASS_PANE:
                            case RED_TERRACOTTA:
                            case RED_WOOL:
                                if (!dye.getType().equals(Material.ROSE_RED)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case WHITE_CARPET:
                            case WHITE_STAINED_GLASS:
                            case WHITE_STAINED_GLASS_PANE:
                            case WHITE_TERRACOTTA:
                            case WHITE_WOOL:
                                if (!dye.getType().equals(Material.BONE_MEAL)) {
                                    changeColour(b, dye, inv, player);
                                }
                                break;
                            case YELLOW_CARPET:
                            case YELLOW_STAINED_GLASS:
                            case YELLOW_STAINED_GLASS_PANE:
                            case YELLOW_TERRACOTTA:
                            case YELLOW_WOOL:
                                if (!dye.getType().equals(Material.DANDELION_YELLOW)) {
                                    changeColour(b, dye, inv, player);
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

    private void changeColour(Block block, ItemStack dye, Inventory inv, Player player) {
        // remove one dye
        int a = dye.getAmount();
        int a2 = a - 1;
        if (a2 > 0) {
            inv.getItem(8).setAmount(a2);
        } else {
            inv.setItem(8, null);
        }
        player.updateInventory();
        // determine colour
        String[] split = block.getType().toString().split("_");
        switch (dye.getType()) {
            case BONE_MEAL:
                split[0] = "WHITE";
                break;
            case CACTUS_GREEN:
                split[0] = "GREEN";
                break;
            case COCOA_BEANS:
                split[0] = "BROWN";
                break;
            case CYAN_DYE:
                split[0] = "CYAN";
                break;
            case DANDELION_YELLOW:
                split[0] = "YELLOW";
                break;
            case GRAY_DYE:
                split[0] = "GRAY";
                break;
            case INK_SAC:
                split[0] = "BLACK";
                break;
            case LAPIS_LAZULI:
                split[0] = "BLUE";
                break;
            case LIGHT_BLUE_DYE:
                split[0] = "LIGHT_BLUE";
                break;
            case LIGHT_GRAY_DYE:
                split[0] = "LIGHT_GRAY";
                break;
            case LIME_DYE:
                split[0] = "LIME";
                break;
            case MAGENTA_DYE:
                split[0] = "MAGENTA";
                break;
            case ORANGE_DYE:
                split[0] = "ORANGE";
                break;
            case PINK_DYE:
                split[0] = "PINK";
                break;
            case PURPLE_DYE:
                split[0] = "PURPLE";
                break;
            case ROSE_RED:
                split[0] = "RED";
                break;
            default:
                break;
        }
        String joined = String.join("_", split);
        Material material = Material.valueOf(joined);
        block.setType(material, true);
    }

    public void playSonicSound(Player player, long now, long cooldown, String sound) {
        if ((!timeout.containsKey(player.getUniqueId()) || timeout.get(player.getUniqueId()) < now)) {
            ItemMeta im = player.getInventory().getItemInMainHand().getItemMeta();
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.values());
            player.getInventory().getItemInMainHand().setItemMeta(im);
            timeout.put(player.getUniqueId(), now + cooldown);
            TARDISSounds.playTARDISSound(player.getLocation(), sound);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                ItemStack is = player.getInventory().getItemInMainHand();
                if (is.hasItemMeta()) {
                    ItemMeta im1 = is.getItemMeta();
                    if (im1.hasDisplayName() && ChatColor.stripColor(im1.getDisplayName()).equals("Sonic Screwdriver")) {
                        player.getInventory().getItemInMainHand().getEnchantments().keySet().forEach((e) -> player.getInventory().getItemInMainHand().removeEnchantment(e));
                    } else {
                        // find the screwdriver in the player's inventory
                        removeSonicEnchant(player.getInventory());
                    }
                } else {
                    // find the screwdriver in the player's inventory
                    removeSonicEnchant(player.getInventory());
                }
            }, (cooldown / 50L));
        }
    }

    private void removeSonicEnchant(PlayerInventory inv) {
        int first = inv.first(sonic);
        if (first < 0) {
            return;
        }
        ItemStack stack = inv.getItem(first);
        if (stack.containsEnchantment(Enchantment.DURABILITY)) {
            stack.getEnchantments().keySet().forEach(stack::removeEnchantment);
        }
    }

    private boolean hasIntersection(TARDISVector3D p1, TARDISVector3D p2, TARDISVector3D min, TARDISVector3D max) {
        double epsilon = 0.0001f;
        TARDISVector3D d = p2.subtract(p1).multiply(0.5);
        TARDISVector3D e = max.subtract(min).multiply(0.5);
        TARDISVector3D c = p1.add(d).subtract(min.add(max).multiply(0.5));
        TARDISVector3D ad = d.abs();
        if (Math.abs(c.x) > e.x + ad.x) {
            return false;
        }
        if (Math.abs(c.y) > e.y + ad.y) {
            return false;
        }
        if (Math.abs(c.z) > e.z + ad.z) {
            return false;
        }
        if (Math.abs(d.y * c.z - d.z * c.y) > e.y * ad.z + e.z * ad.y + epsilon) {
            return false;
        }
        if (Math.abs(d.z * c.x - d.x * c.z) > e.z * ad.x + e.x * ad.z + epsilon) {
            return false;
        }
        return Math.abs(d.x * c.y - d.y * c.x) <= e.x * ad.y + e.y * ad.x + epsilon;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFrozenMove(PlayerMoveEvent event) {
        if (frozenPlayers.contains(event.getPlayer().getUniqueId())) {
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
        if (plugin.isMVOnServer()) {
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

    private void standardSonic(Player player) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation().getBlock();
        Material blockType = targetBlock.getType();
        if (distance.contains(blockType)) {
            BlockState bs = targetBlock.getState();
            switch (blockType) {
                case ACACIA_DOOR:
                case BIRCH_DOOR:
                case DARK_OAK_DOOR:
                case IRON_DOOR:
                case JUNGLE_DOOR:
                case OAK_DOOR:
                case SPRUCE_DOOR:
                    Block lowerdoor;
                    Bisected bisected = (Bisected) targetBlock.getBlockData();
                    if (bisected.getHalf().equals(Half.TOP)) {
                        lowerdoor = targetBlock.getRelative(BlockFace.DOWN);
                    } else {
                        lowerdoor = targetBlock;
                    }
                    // not protected doors - WorldGuard / GriefPrevention / Lockette / Towny
                    boolean allow = !checkBlockRespect(player, lowerdoor);
                    // is it a TARDIS door?
                    HashMap<String, Object> where = new HashMap<>();
                    String doorloc = lowerdoor.getLocation().getWorld().getName() + ":" + lowerdoor.getLocation().getBlockX() + ":" + lowerdoor.getLocation().getBlockY() + ":" + lowerdoor.getLocation().getBlockZ();
                    where.put("door_location", doorloc);
                    ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
                    if (rs.resultSet()) {
                        return;
                    }
                    if (allow) {
                        if (!plugin.getTrackerKeeper().getSonicDoors().contains(player.getUniqueId())) {
                            Openable openable = (Openable) lowerdoor.getBlockData();
                            boolean open = !openable.isOpen();
                            openable.setOpen(open);
                            lowerdoor.setBlockData(openable, true);
                            if (blockType.equals(Material.IRON_DOOR)) {
                                plugin.getTrackerKeeper().getSonicDoors().add(player.getUniqueId());
                                // return the door to its previous state after 3 seconds
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                    openable.setOpen(!open);
                                    lowerdoor.setBlockData(openable, true);
                                    plugin.getTrackerKeeper().getSonicDoors().remove(player.getUniqueId());
                                }, 60L);
                            }
                        }
                    }
                    break;
                case LEVER:
                    Powerable lever = (Powerable) targetBlock.getBlockData();
                    lever.setPowered(!lever.isPowered());
                    targetBlock.setBlockData(lever, true);
                    break;
                case ACACIA_BUTTON:
                case BIRCH_BUTTON:
                case DARK_OAK_BUTTON:
                case JUNGLE_BUTTON:
                case OAK_BUTTON:
                case SPRUCE_BUTTON:
                case STONE_BUTTON:
                    Powerable button = (Powerable) targetBlock.getBlockData();
                    button.setPowered(true);
                    targetBlock.setBlockData(button, true);
                    long delay = (blockType.equals(Material.STONE_BUTTON)) ? 20L : 30L;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        button.setPowered(false);
                        targetBlock.setBlockData(button);
                    }, delay);
                    break;
                default:
                    break;
            }
        }
    }

    public boolean setExtension(Block b) {
        BlockFace face = ((Piston) b.getBlockData()).getFacing();
        Block l = b.getRelative(face);
        Material mat = l.getType();
        // check if there is a block there
        if (!mat.equals(Material.PISTON_HEAD)) {
            if (mat.equals(Material.AIR)) {
                extend(b, l);
                return true;
            } else {
                // check the block further on for AIR
                Block two = b.getRelative(face, 2);
                if (two.getType().equals(Material.AIR)) {
                    two.setType(mat);
                    extend(b, l);
                    return true;
                }
            }
        }
        return false;
    }

    private void extend(Block b, Block l) {
        l.setType(Material.PISTON_HEAD);
        Piston piston = (Piston) b.getBlockData();
        piston.setExtended(true);
        b.setBlockData(piston, true);
    }

    private void ignite(Block b, Player p) {
        if (!checkBlockRespect(p, b)) {
            Block above = b.getRelative(BlockFace.UP);
            if (b.getType().equals(Material.TNT)) {
                b.setType(Material.AIR);
                b.getWorld().spawnEntity(b.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.PRIMED_TNT);
                plugin.getPM().callEvent(new BlockIgniteEvent(b, IgniteCause.FLINT_AND_STEEL, p));
                return;
            }
            if (above.getType().equals(Material.AIR)) {
                above.setType(Material.FIRE);
                // call a block ignite event
                plugin.getPM().callEvent(new BlockIgniteEvent(b, IgniteCause.FLINT_AND_STEEL, p));
            }
        }
    }

    private boolean checkBlockRespect(Player p, Block b) {
        boolean gpr = false;
        boolean wgu = false;
        boolean lke = false;
        boolean pro = false;
        boolean bll = false;
        boolean tny = false;
        // GriefPrevention
        if (plugin.getPM().isPluginEnabled("GriefPrevention")) {
            gpr = new TARDISGriefPreventionChecker(plugin, true).isInClaim(p, b.getLocation());
        }
        // WorldGuard
        if (plugin.isWorldGuardOnServer()) {
            wgu = !plugin.getWorldGuardUtils().canBuild(p, b.getLocation());
        }
        // Lockette
        if (plugin.getPM().isPluginEnabled("Lockette")) {
            lke = Lockette.isProtected(b);
        }
        if (plugin.getPM().isPluginEnabled("LockettePro")) {
            pro = LocketteProAPI.isProtected(b);
        }
        if (plugin.getPM().isPluginEnabled("BlockLocker")) {
            pro = BlockLockerAPI.isProtected(b);
        }
        // Towny
        if (plugin.getPM().isPluginEnabled("Towny")) {
            tny = new TARDISTownyChecker(plugin, true).checkTowny(p, b.getLocation());
        }
        return (gpr || wgu || lke || pro || tny);
    }

    private boolean isPresetSign(String l0, String l1, String l2) {
        if (l0.equalsIgnoreCase("WEEPING") || l0.equalsIgnoreCase("$50,000")) {
            return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l0) && l1.equals(plugin.getGeneralKeeper().getSign_lookup().get(l0)));
        } else {
            return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l1) && l2.equals(plugin.getGeneralKeeper().getSign_lookup().get(l1)));
        }
    }
}
