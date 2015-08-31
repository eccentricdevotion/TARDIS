/*
 * Copyright (C) 2014 eccentric_nz
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

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.database.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import static me.eccentric_nz.TARDIS.listeners.TARDISScannerListener.getNearbyEntities;
import me.eccentric_nz.TARDIS.utility.TARDISGriefPreventionChecker;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Button;
import org.bukkit.material.DetectorRail;
import org.bukkit.material.Door;
import org.bukkit.material.Lever;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.material.PistonExtensionMaterial;
import org.bukkit.material.PoweredRail;
import org.bukkit.scheduler.BukkitScheduler;
import org.yi.acru.bukkit.Lockette.Lockette;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSonicListener implements Listener {

    private final TARDIS plugin;
    private final Material sonic;
    private final HashMap<UUID, Long> timeout = new HashMap<UUID, Long>();
    private final HashMap<UUID, Long> cooldown = new HashMap<UUID, Long>();
    private final List<Material> diamond = new ArrayList<Material>();
    private final List<Material> distance = new ArrayList<Material>();
    private final List<Material> doors = new ArrayList<Material>();
    private final List<Material> interactables = new ArrayList<Material>();
    private final List<Material> redstone = new ArrayList<Material>();
    private final List<UUID> frozenPlayers = new ArrayList<UUID>();
    private final List<BlockFace> faces = new ArrayList<BlockFace>();
    private final List<Material> paintable = new ArrayList<Material>();

    public TARDISSonicListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        this.sonic = Material.valueOf(split[0]);
        diamond.add(Material.GLASS);
        diamond.add(Material.IRON_FENCE);
        diamond.add(Material.SNOW);
        diamond.add(Material.SNOW_BLOCK);
        diamond.add(Material.STAINED_GLASS);
        diamond.add(Material.STAINED_GLASS_PANE);
        diamond.add(Material.THIN_GLASS);
        diamond.add(Material.WEB);
        distance.add(Material.IRON_DOOR_BLOCK);
        distance.add(Material.LEVER);
        distance.add(Material.STONE_BUTTON);
        distance.add(Material.WOODEN_DOOR);
        distance.add(Material.WOOD_BUTTON);
        doors.add(Material.ACACIA_DOOR);
        doors.add(Material.BIRCH_DOOR);
        doors.add(Material.DARK_OAK_DOOR);
        doors.add(Material.IRON_DOOR_BLOCK);
        doors.add(Material.JUNGLE_DOOR);
        doors.add(Material.SPRUCE_DOOR);
        doors.add(Material.TRAP_DOOR);
        doors.add(Material.WOODEN_DOOR);
        interactables.add(Material.ACACIA_DOOR);
        interactables.add(Material.ACACIA_FENCE_GATE);
        interactables.add(Material.ANVIL);
        interactables.add(Material.BEACON);
        interactables.add(Material.BED_BLOCK);
        interactables.add(Material.BIRCH_DOOR);
        interactables.add(Material.BIRCH_FENCE_GATE);
        interactables.add(Material.BURNING_FURNACE);
        interactables.add(Material.CHEST);
        interactables.add(Material.DARK_OAK_DOOR);
        interactables.add(Material.DARK_OAK_FENCE_GATE);
        interactables.add(Material.DIODE_BLOCK_OFF);
        interactables.add(Material.DIODE_BLOCK_ON);
        interactables.add(Material.DISPENSER);
        interactables.add(Material.DROPPER);
        interactables.add(Material.ENDER_CHEST);
        interactables.add(Material.FENCE_GATE);
        interactables.add(Material.FURNACE);
        interactables.add(Material.GOLD_PLATE);
        interactables.add(Material.HOPPER);
        interactables.add(Material.IRON_DOOR_BLOCK);
        interactables.add(Material.IRON_PLATE);
        interactables.add(Material.IRON_TRAPDOOR);
        interactables.add(Material.JUKEBOX);
        interactables.add(Material.JUNGLE_DOOR);
        interactables.add(Material.JUNGLE_FENCE_GATE);
        interactables.add(Material.LEVER);
        interactables.add(Material.NOTE_BLOCK);
        interactables.add(Material.REDSTONE_COMPARATOR_OFF);
        interactables.add(Material.REDSTONE_COMPARATOR_ON);
        interactables.add(Material.SPRUCE_DOOR);
        interactables.add(Material.SPRUCE_FENCE_GATE);
        interactables.add(Material.STONE_BUTTON);
        interactables.add(Material.STONE_PLATE);
        interactables.add(Material.TRAPPED_CHEST);
        interactables.add(Material.TRAP_DOOR);
        interactables.add(Material.WOODEN_DOOR);
        interactables.add(Material.WOOD_BUTTON);
        interactables.add(Material.WOOD_PLATE);
        interactables.add(Material.WORKBENCH);
        redstone.add(Material.DETECTOR_RAIL);
        redstone.add(Material.IRON_DOOR_BLOCK);
        redstone.add(Material.PISTON_BASE);
        redstone.add(Material.PISTON_STICKY_BASE);
        redstone.add(Material.POWERED_RAIL);
        redstone.add(Material.REDSTONE_LAMP_OFF);
        redstone.add(Material.REDSTONE_LAMP_ON);
        redstone.add(Material.REDSTONE_WIRE);
        faces.add(BlockFace.NORTH);
        faces.add(BlockFace.SOUTH);
        faces.add(BlockFace.EAST);
        faces.add(BlockFace.WEST);
        faces.add(BlockFace.UP);
        faces.add(BlockFace.DOWN);
        this.paintable.add(Material.CARPET);
        this.paintable.add(Material.STAINED_CLAY);
        this.paintable.add(Material.STAINED_GLASS);
        this.paintable.add(Material.STAINED_GLASS_PANE);
        this.paintable.add(Material.WOOL);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        final ItemStack is = player.getItemInHand();
        if (is.getType().equals(sonic) && is.hasItemMeta()) {
            ItemMeta im = player.getItemInHand().getItemMeta();
            if (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver")) {
                List<String> lore = im.getLore();
                Action action = event.getAction();
                if (action.equals(Action.RIGHT_CLICK_AIR) && !player.isSneaking()) {
                    playSonicSound(player, now, 3050L, "sonic_screwdriver");
                    if (player.hasPermission("tardis.sonic.freeze") && lore != null && lore.contains("Bio-scanner Upgrade")) {
                        long cool = System.currentTimeMillis();
                        if ((!cooldown.containsKey(player.getUniqueId()) || cooldown.get(player.getUniqueId()) < cool)) {
                            cooldown.put(player.getUniqueId(), cool + (plugin.getConfig().getInt("preferences.freeze_cooldown") * 1000L));
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
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
                                        final UUID uuid = hit.getUniqueId();
                                        frozenPlayers.add(uuid);
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                            @Override
                                            public void run() {
                                                frozenPlayers.remove(uuid);
                                            }
                                        }, 100L);
                                    } else {
                                        if (player.hasPermission("tardis.sonic.standard")) {
                                            standardSonic(player);
                                        }
                                    }
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
                    final Block b = event.getClickedBlock();
                    if (doors.contains(b.getType()) && player.hasPermission("tardis.admin") && lore != null && lore.contains("Admin Upgrade")) {
                        playSonicSound(player, now, 600L, "sonic_short");
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                HashMap<String, Object> wheredoor = new HashMap<String, Object>();
                                Location loc = b.getLocation();
                                String bw = loc.getWorld().getName();
                                int bx = loc.getBlockX();
                                int by = loc.getBlockY();
                                int bz = loc.getBlockZ();
                                if (b.getData() >= 8 && !b.getType().equals(Material.TRAP_DOOR)) {
                                    by = (by - 1);
                                }
                                String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                                wheredoor.put("door_location", doorloc);
                                wheredoor.put("door_type", 0);
                                ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
                                if (rsd.resultSet()) {
                                    int id = rsd.getTardis_id();
                                    // get the TARDIS owner's name
                                    HashMap<String, Object> wheren = new HashMap<String, Object>();
                                    wheren.put("tardis_id", id);
                                    ResultSetTardis rsn = new ResultSetTardis(plugin, wheren, "", false);
                                    if (rsn.resultSet()) {
                                        String name = plugin.getServer().getOfflinePlayer(rsn.getUuid()).getName();
                                        TARDISMessage.send(player, "TARDIS_WHOSE", name);
                                        int percent = Math.round((rsn.getArtron_level() * 100F) / plugin.getArtronConfig().getInt("full_charge"));
                                        TARDISMessage.send(player, "ENERGY_LEVEL", String.format("%d", percent));
                                        HashMap<String, Object> whereb = new HashMap<String, Object>();
                                        whereb.put("tardis_id", id);
                                        ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, whereb);
                                        if (rsb.resultSet()) {
                                            TARDISMessage.send(player, "SCAN_LAST", rsb.getWorld().getName() + " " + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ());
                                        }
                                    }
                                    HashMap<String, Object> whereid = new HashMap<String, Object>();
                                    whereid.put("tardis_id", id);
                                    ResultSetTravellers rst = new ResultSetTravellers(plugin, whereid, true);
                                    if (rst.resultSet()) {
                                        List<UUID> data = rst.getData();
                                        TARDISMessage.send(player, "SONIC_INSIDE");
                                        for (UUID s : data) {
                                            player.sendMessage(plugin.getServer().getPlayer(s).getDisplayName());
                                        }
                                    } else {
                                        TARDISMessage.send(player, "SONIC_OCCUPY");
                                    }
                                }
                            }
                        }, 60L);
                    }
                    if (!redstone.contains(b.getType()) && player.hasPermission("tardis.sonic.emerald") && lore != null && lore.contains("Emerald Upgrade") && !interactables.contains(b.getType())) {
                        playSonicSound(player, now, 3050L, "sonic_screwdriver");
                        // scan environment
                        this.scan(b.getLocation(), player);
                    }
                    if (redstone.contains(b.getType()) && player.hasPermission("tardis.sonic.redstone") && lore != null && lore.contains("Redstone Upgrade")) {
                        playSonicSound(player, now, 600L, "sonic_short");
                        Material blockType = b.getType();
                        BlockState bs = b.getState();
                        // do redstone activation
                        switch (blockType) {
                            case DETECTOR_RAIL:
                                DetectorRail drail = (DetectorRail) bs.getData();
                                if (plugin.getGeneralKeeper().getSonicRails().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicRails().remove(b.getLocation().toString());
                                    drail.setPressed(false);
                                    b.setData((byte) (drail.getData() - 8));
                                } else {
                                    plugin.getGeneralKeeper().getSonicRails().add(b.getLocation().toString());
                                    drail.setPressed(true);
                                    b.setData((byte) (drail.getData() + 8));
                                }
                                break;
                            case IRON_DOOR_BLOCK:
                                // get bottom door block
                                Block tmp = b;
                                if (b.getData() >= 8) {
                                    tmp = b.getRelative(BlockFace.DOWN);
                                }
                                // not TARDIS doors!
                                String doorloc = tmp.getLocation().getWorld().getName() + ":" + tmp.getLocation().getBlockX() + ":" + tmp.getLocation().getBlockY() + ":" + tmp.getLocation().getBlockZ();
                                HashMap<String, Object> wheredoor = new HashMap<String, Object>();
                                wheredoor.put("door_location", doorloc);
                                ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
                                if (rsd.resultSet()) {
                                    return;
                                }
                                // not protected doors - WorldGuard / GriefPrevention / Lockette / LWC
                                if (checkBlockRespect(player, tmp)) {
                                    return;
                                }
                                if (!plugin.getTrackerKeeper().getSonicDoors().contains(player.getUniqueId())) {
                                    plugin.getTrackerKeeper().getSonicDoors().add(player.getUniqueId());
                                    final Block door_bottom = tmp;
                                    final byte door_data = door_bottom.getData();
                                    switch (door_data) {
                                        case (byte) 0:
                                            door_bottom.setData((byte) 4, false);
                                            break;
                                        case (byte) 1:
                                            door_bottom.setData((byte) 5, false);
                                            break;
                                        case (byte) 2:
                                            door_bottom.setData((byte) 6, false);
                                            break;
                                        case (byte) 3:
                                            door_bottom.setData((byte) 7, false);
                                            break;
                                        case (byte) 4:
                                            door_bottom.setData((byte) 0, false);
                                            break;
                                        case (byte) 5:
                                            door_bottom.setData((byte) 1, false);
                                            break;
                                        case (byte) 6:
                                            door_bottom.setData((byte) 2, false);
                                            break;
                                        default:
                                            door_bottom.setData((byte) 3, false);
                                            break;
                                    }
                                    // return the door to its previous state after 3 seconds
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            if (door_bottom.getData() != door_data) {
                                                door_bottom.setData(door_data, false);
                                            }
                                            plugin.getTrackerKeeper().getSonicDoors().remove(player.getUniqueId());
                                        }
                                    }, 60L);
                                }
                                break;
                            case POWERED_RAIL:
                                PoweredRail rail = (PoweredRail) bs.getData();
                                if (plugin.getGeneralKeeper().getSonicRails().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicRails().remove(b.getLocation().toString());
                                    rail.setPowered(false);
                                    b.setData((byte) (rail.getData() - 8));
                                } else {
                                    plugin.getGeneralKeeper().getSonicRails().add(b.getLocation().toString());
                                    rail.setPowered(true);
                                    b.setData((byte) (rail.getData() + 8));
                                }
                                break;
                            case PISTON_BASE:
                            case PISTON_STICKY_BASE:
                                PistonBaseMaterial piston = (PistonBaseMaterial) bs.getData();
                                // find the direction the piston is facing
                                if (plugin.getGeneralKeeper().getSonicPistons().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicPistons().remove(b.getLocation().toString());
                                    for (BlockFace f : faces) {
                                        if (b.getRelative(f).getType().equals(Material.AIR)) {
                                            b.getRelative(f).setTypeIdAndData(20, (byte) 0, true);
                                            b.getRelative(f).setTypeIdAndData(0, (byte) 0, true);
                                            break;
                                        }
                                    }
                                } else {
                                    if (setExtension(b)) {
                                        plugin.getGeneralKeeper().getSonicPistons().add(b.getLocation().toString());
                                        piston.setPowered(true);
                                        player.playSound(b.getLocation(), Sound.PISTON_EXTEND, 1.0f, 1.0f);
                                    }
                                }
                                b.setData(piston.getData());
                                bs.update(true);
                                break;
                            case REDSTONE_LAMP_OFF:
                            case REDSTONE_LAMP_ON:
                                if (blockType.equals(Material.REDSTONE_LAMP_OFF)) {
                                    plugin.getGeneralKeeper().getSonicLamps().add(b.getLocation().toString());
                                    for (BlockFace f : faces) {
                                        if (b.getRelative(f).getType().equals(Material.AIR)) {
                                            b.getRelative(f).setTypeIdAndData(152, (byte) 0, true);
                                            b.setType(Material.REDSTONE_LAMP_ON);
                                            b.getRelative(f).setTypeIdAndData(0, (byte) 0, true);
                                            break;
                                        }
                                    }
                                } else if (plugin.getGeneralKeeper().getSonicLamps().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicLamps().remove(b.getLocation().toString());
                                    b.setType(Material.REDSTONE_LAMP_OFF);
                                }
                                break;
                            case REDSTONE_WIRE:
                                if (plugin.getGeneralKeeper().getSonicWires().contains(b.getLocation().toString())) {
                                    plugin.getGeneralKeeper().getSonicWires().remove(b.getLocation().toString());
                                    b.setData((byte) 0);
                                    for (BlockFace f : faces) {
                                        if (b.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
                                            b.setData((byte) 0);
                                        }
                                    }
                                } else {
                                    plugin.getGeneralKeeper().getSonicWires().add(b.getLocation().toString());
                                    b.setData((byte) 15);
                                    for (BlockFace f : faces) {
                                        if (b.getRelative(f).getType().equals(Material.REDSTONE_WIRE)) {
                                            b.setData((byte) 13);
                                        }
                                    }
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
                            this.ignite(b, player);
                        }
                        if (diamond.contains(b.getType()) && player.hasPermission("tardis.sonic.diamond") && lore != null && lore.contains("Diamond Upgrade")) {
                            // check the block is not protected by WorldGuard
                            if (plugin.isWorldGuardOnServer()) {
                                if (!plugin.getWorldGuardUtils().canBreakBlock(player, b)) {
                                    TARDISMessage.send(player, "SONIC_PROTECT");
                                    return;
                                }
                            }
                            playSonicSound(player, now, 600L, "sonic_short");
                            // drop appropriate material
                            Material mat = b.getType();
                            if (player.hasPermission("tardis.sonic.silktouch")) {
                                Location l = b.getLocation();
                                switch (mat) {
                                    case GLASS:
                                        l.getWorld().dropItemNaturally(l, new ItemStack(Material.GLASS, 1));
                                        break;
                                    case IRON_FENCE:
                                        l.getWorld().dropItemNaturally(l, new ItemStack(Material.IRON_FENCE, 1));
                                        break;
                                    case SNOW:
                                        b.getLocation().getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.SNOW_BALL, 1 + b.getData()));
                                        break;
                                    case SNOW_BLOCK:
                                        l.getWorld().dropItemNaturally(l, new ItemStack(Material.SNOW_BLOCK, 1));
                                        break;
                                    case STAINED_GLASS:
                                        l.getWorld().dropItemNaturally(l, new ItemStack(Material.STAINED_GLASS, 1, b.getData()));
                                        break;
                                    case STAINED_GLASS_PANE:
                                        l.getWorld().dropItemNaturally(l, new ItemStack(Material.STAINED_GLASS_PANE, 1, b.getData()));
                                        break;
                                    case THIN_GLASS:
                                        l.getWorld().dropItemNaturally(l, new ItemStack(Material.THIN_GLASS, 1));
                                        break;
                                    case WEB:
                                        l.getWorld().dropItemNaturally(l, new ItemStack(Material.WEB, 1));
                                        break;
                                    default:
                                        break;
                                }
                                l.getWorld().playSound(l, Sound.SHEEP_SHEAR, 1.0F, 1.5F);
                                // set the block to AIR
                                b.setType(Material.AIR);
                            } else {
                                if (mat.equals(Material.SNOW) || mat.equals(Material.SNOW_BLOCK)) {
                                    // how many?
                                    int balls;
                                    if (mat.equals(Material.SNOW_BLOCK)) {
                                        balls = 4;
                                    } else {
                                        balls = 1 + b.getData();
                                    }
                                    b.setType(Material.AIR);
                                    b.getLocation().getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.SNOW_BALL, balls));
                                } else {
                                    b.breakNaturally();
                                    b.getLocation().getWorld().playSound(b.getLocation(), Sound.SHEEP_SHEAR, 1.0F, 1.5F);
                                }
                            }
                        }
                    } else {
                        if (paintable.contains(b.getType()) && player.hasPermission("tardis.sonic.paint") && lore != null && lore.contains("Painter Upgrade")) {
                            // must be in TARDIS world
                            if (!plugin.getUtils().inTARDISWorld(player)) {
                                TARDISMessage.send(player, "UPDATE_IN_WORLD");
                                return;
                            }
                            // check the block is not protected by WorldGuard
                            if (plugin.isWorldGuardOnServer()) {
                                if (!plugin.getWorldGuardUtils().canBreakBlock(player, b)) {
                                    TARDISMessage.send(player, "SONIC_PROTECT");
                                    return;
                                }
                            }
                            playSonicSound(player, now, 600L, "sonic_short");
                            // check for dye in slot
                            PlayerInventory inv = player.getInventory();
                            ItemStack dye = inv.getItem(8);
                            if (dye == null || !dye.getType().equals(Material.INK_SACK)) {
                                TARDISMessage.send(player, "SONIC_DYE");
                                return;
                            }
                            byte dye_data = dye.getData().getData();
                            byte block_data = b.getData();
                            byte new_data = (byte) (15 - dye_data);
                            // don't do anything if it is the same colour
                            if (new_data == block_data) {
                                return;
                            }
                            // remove one dye
                            int a = dye.getAmount();
                            int a2 = a - 1;
                            if (a2 > 0) {
                                inv.getItem(8).setAmount(a2);
                            } else {
                                inv.setItem(8, null);
                            }
                            player.updateInventory();
                            b.setData(new_data, true);
                        }
                    }
                }
            }
        }
    }

    public void playSonicSound(final Player player, long now, long cooldown, String sound) {
        if ((!timeout.containsKey(player.getUniqueId()) || timeout.get(player.getUniqueId()) < now)) {
            ItemMeta im = player.getItemInHand().getItemMeta();
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            if (!plugin.getPM().isPluginEnabled("Multiverse-Inventories")) {
                im.addItemFlags(ItemFlag.values());
            }
            player.getItemInHand().setItemMeta(im);
            timeout.put(player.getUniqueId(), now + cooldown);
            TARDISSounds.playTARDISSound(player.getLocation(), player, sound);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    ItemStack is = player.getItemInHand();
                    if (is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName() && ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver")) {
                            for (Enchantment e : player.getItemInHand().getEnchantments().keySet()) {
                                player.getItemInHand().removeEnchantment(e);
                            }
                        } else {
                            // find the screwdriver in the player's inventory
                            removeSonicEnchant(player.getInventory());
                        }
                    } else {
                        // find the screwdriver in the player's inventory
                        removeSonicEnchant(player.getInventory());
                    }
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
            for (Enchantment e : stack.getEnchantments().keySet()) {
                stack.removeEnchantment(e);
            }
        }
    }

    private boolean hasIntersection(TARDISVector3D p1, TARDISVector3D p2, TARDISVector3D min, TARDISVector3D max) {
        final double epsilon = 0.0001f;
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

    public void scan(final Location scan_loc, final Player player) {
        // record nearby entities
        final HashMap<EntityType, Integer> scannedentities = new HashMap<EntityType, Integer>();
        final List<String> playernames = new ArrayList<String>();
        for (Entity k : getNearbyEntities(scan_loc, 16)) {
            EntityType et = k.getType();
            if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                Integer entity_count = (scannedentities.containsKey(et)) ? scannedentities.get(et) : 0;
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
        }
        final long time = scan_loc.getWorld().getTime();
        final String daynight = TARDISStaticUtils.getTime(time);
        String worldname;
        if (plugin.isMVOnServer()) {
            worldname = plugin.getMVHelper().getAlias(scan_loc.getWorld());
        } else {
            worldname = scan_loc.getWorld().getName();
        }
        final String wn = worldname;
        // message the player
        TARDISMessage.send(player, "SONIC_SCAN");
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, true, "SCAN_WORLD", wn);
                TARDISMessage.send(player, true, "SONIC_COORDS", scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
            }
        }, 20L);
        // get biome
        final Biome biome = scan_loc.getBlock().getBiome();
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, true, "BIOME_TYPE", biome.toString());
            }
        }, 40L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, true, "SCAN_TIME", daynight + " / " + time);
            }
        }, 60L);
        // get weather
        final String weather;
        if (biome.equals(Biome.DESERT) || biome.equals(Biome.DESERT_HILLS) || biome.equals(Biome.SAVANNA) || biome.equals(Biome.SAVANNA_MOUNTAINS) || biome.equals(Biome.SAVANNA_PLATEAU) || biome.equals(Biome.SAVANNA_PLATEAU_MOUNTAINS) || biome.equals(Biome.MESA) || biome.equals(Biome.MESA_BRYCE) || biome.equals(Biome.MESA_PLATEAU) || biome.equals(Biome.MESA_PLATEAU_MOUNTAINS)) {
            weather = plugin.getLanguage().getString("WEATHER_DRY");
        } else if (biome.equals(Biome.ICE_PLAINS) || biome.equals(Biome.ICE_PLAINS_SPIKES) || biome.equals(Biome.FROZEN_OCEAN) || biome.equals(Biome.FROZEN_RIVER) || biome.equals(Biome.COLD_BEACH) || biome.equals(Biome.COLD_TAIGA) || biome.equals(Biome.COLD_TAIGA_HILLS) || biome.equals(Biome.COLD_TAIGA_MOUNTAINS)) {
            weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_SNOW") : plugin.getLanguage().getString("WEATHER_COLD");
        } else {
            weather = (scan_loc.getWorld().hasStorm()) ? plugin.getLanguage().getString("WEATHER_RAIN") : plugin.getLanguage().getString("WEATHER_CLEAR");
        }
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, true, "SCAN_WEATHER", weather);
            }
        }, 80L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, true, "SCAN_HUMIDITY", String.format("%.2f", scan_loc.getBlock().getHumidity()));
            }
        }, 100L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, true, "SCAN_TEMP", String.format("%.2f", scan_loc.getBlock().getTemperature()));
            }
        }, 120L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, true, "SCAN_ENTS");
                if (scannedentities.size() > 0) {
                    for (Map.Entry<EntityType, Integer> entry : scannedentities.entrySet()) {
                        String message = "";
                        StringBuilder buf = new StringBuilder();
                        if (entry.getKey().equals(EntityType.PLAYER) && playernames.size() > 0) {
                            for (String p : playernames) {
                                buf.append(", ").append(p);
                            }
                            message = " (" + buf.toString().substring(2) + ")";
                        }
                        player.sendMessage("    " + entry.getKey() + ": " + entry.getValue() + message);
                    }
                    scannedentities.clear();
                } else {
                    TARDISMessage.send(player, true, "SCAN_NONE");
                }
            }
        }, 140L);
    }

    @SuppressWarnings("deprecation")
    private void standardSonic(final Player player) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation().getBlock();
        Material blockType = targetBlock.getType();
        if (distance.contains(blockType)) {
            final BlockState bs = targetBlock.getState();
            switch (blockType) {
                case IRON_DOOR_BLOCK:
                case WOODEN_DOOR:
                    final Block lowerdoor;
                    if (targetBlock.getData() >= 8) {
                        lowerdoor = targetBlock.getRelative(BlockFace.DOWN);
                    } else {
                        lowerdoor = targetBlock;
                    }
                    // not protected doors - WorldGuard / GriefPrevention / Lockette / LWC
                    boolean allow = !checkBlockRespect(player, lowerdoor);
                    // is it a TARDIS door?
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    String doorloc = lowerdoor.getLocation().getWorld().getName() + ":" + lowerdoor.getLocation().getBlockX() + ":" + lowerdoor.getLocation().getBlockY() + ":" + lowerdoor.getLocation().getBlockZ();
                    where.put("door_location", doorloc);
                    ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
                    if (rs.resultSet()) {
                        return;
                    }
                    if (allow) {
                        if (!plugin.getTrackerKeeper().getSonicDoors().contains(player.getUniqueId())) {
                            final BlockState bsl = lowerdoor.getState();
                            final byte door_data = lowerdoor.getData();
                            Door door = (Door) bsl.getData();
                            door.setOpen(!door.isOpen());
                            bsl.setData(door);
                            bsl.update(true);
                            if (blockType.equals(Material.IRON_DOOR_BLOCK)) {
                                plugin.getTrackerKeeper().getSonicDoors().add(player.getUniqueId());
                                // return the door to its previous state after 3 seconds
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (lowerdoor.getData() != door_data) {
                                            lowerdoor.setData(door_data, false);
                                        }
                                        plugin.getTrackerKeeper().getSonicDoors().remove(player.getUniqueId());
                                    }
                                }, 60L);
                            }
                        }
                    }
                    break;
                case LEVER:
                    Lever lever = (Lever) bs.getData();
                    lever.setPowered(!lever.isPowered());
                    bs.setData(lever);
                    bs.update(true);
                    break;
                case STONE_BUTTON:
                case WOOD_BUTTON:
                    final Button button = (Button) bs.getData();
                    button.setPowered(true);
                    bs.setData(button);
                    bs.update(true);
                    long delay = (blockType.equals(Material.STONE_BUTTON)) ? 20L : 30L;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            button.setPowered(false);
                            bs.setData(button);
                            bs.update(true);
                        }
                    }, delay);
                    break;
                default:
                    break;
            }
        }
    }

    @SuppressWarnings("deprecation")
    public boolean setExtension(Block b) {
        BlockFace face = ((PistonBaseMaterial) b.getState().getData()).getFacing();
        Block l = b.getRelative(face);
        Material mat = l.getType();
        byte data = l.getData();
        // check if there is a block there
        if (!mat.equals(Material.PISTON_EXTENSION)) {
            if (mat.equals(Material.AIR)) {
                extend(b, l);
                return true;
            } else {
                // check the block further on for AIR
                Block two = b.getRelative(face, 2);
                if (two.getType().equals(Material.AIR)) {
                    two.setType(mat);
                    two.setData(data);
                    extend(b, l);
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private void extend(final Block b, final Block l) {
        l.setType(Material.PISTON_EXTENSION);
        if (b.getType().equals(Material.PISTON_STICKY_BASE)) {
            l.setData((byte) (b.getData() - 8));
        } else {
            l.setData(b.getData());
        }
        PistonExtensionMaterial extension = (PistonExtensionMaterial) l.getState().getData();
        l.setData(extension.getData());
        l.getState().update();
    }

    private void ignite(final Block b, Player p) {
        if (!checkBlockRespect(p, b)) {
            Block above = b.getRelative(BlockFace.UP);
            if (b.getType().equals(Material.TNT)) {
                b.setType(Material.AIR);
                b.getWorld().spawnEntity(b.getLocation().add(0.5d, 0.5d, 0.5d), EntityType.PRIMED_TNT);
                plugin.getPM().callEvent(new BlockIgniteEvent(b, IgniteCause.FLINT_AND_STEEL, (Entity) p));
                return;
            }
            if (above.getType().equals(Material.AIR)) {
                above.setType(Material.FIRE);
                // call a block ignite event
                plugin.getPM().callEvent(new BlockIgniteEvent(b, IgniteCause.FLINT_AND_STEEL, (Entity) p));
            }
        }
    }

    private boolean checkBlockRespect(Player p, Block b) {
        boolean gpr = false;
        boolean wgu = false;
        boolean lke = false;
        boolean lch = false;
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
            Lockette Lockette = (Lockette) plugin.getPM().getPlugin("Lockette");
            lke = Lockette.isProtected(b);
        }
        // LWC
        if (plugin.getPM().isPluginEnabled("LWC")) {
            LWCPlugin lwcplug = (LWCPlugin) plugin.getPM().getPlugin("LWC");
            LWC lwc = lwcplug.getLWC();
            lch = !lwc.canAccessProtection(p, b);
        }
        return (gpr || wgu || lke || lch);
    }
}
