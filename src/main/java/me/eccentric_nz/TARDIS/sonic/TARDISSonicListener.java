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
import me.eccentric_nz.TARDIS.commands.admin.TARDISAdminMenuInventory;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.database.ResultSetBackLocation;
import me.eccentric_nz.TARDIS.database.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import static me.eccentric_nz.TARDIS.listeners.TARDISScannerListener.getNearbyEntities;
import me.eccentric_nz.TARDIS.move.TARDISDoorToggler;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
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

    public TARDISSonicListener(TARDIS plugin) {
        this.plugin = plugin;
        String[] split = plugin.getRecipesConfig().getString("shaped.Sonic Screwdriver.result").split(":");
        this.sonic = Material.valueOf(split[0]);
        diamond.add(Material.GLASS);
        diamond.add(Material.IRON_FENCE);
        diamond.add(Material.STAINED_GLASS);
        diamond.add(Material.STAINED_GLASS_PANE);
        diamond.add(Material.THIN_GLASS);
        diamond.add(Material.WEB);
        distance.add(Material.IRON_DOOR_BLOCK);
        distance.add(Material.LEVER);
        distance.add(Material.STONE_BUTTON);
        distance.add(Material.WOODEN_DOOR);
        distance.add(Material.WOOD_BUTTON);
        doors.add(Material.IRON_DOOR_BLOCK);
        doors.add(Material.TRAP_DOOR);
        doors.add(Material.WOODEN_DOOR);
        interactables.add(Material.ANVIL);
        interactables.add(Material.BEACON);
        interactables.add(Material.BED_BLOCK);
        interactables.add(Material.BURNING_FURNACE);
        interactables.add(Material.CHEST);
        interactables.add(Material.DIODE_BLOCK_OFF);
        interactables.add(Material.DIODE_BLOCK_ON);
        interactables.add(Material.DISPENSER);
        interactables.add(Material.DROPPER);
        interactables.add(Material.FURNACE);
        interactables.add(Material.HOPPER);
        interactables.add(Material.IRON_DOOR_BLOCK);
        interactables.add(Material.JUKEBOX);
        interactables.add(Material.LEVER);
        interactables.add(Material.NOTE_BLOCK);
        interactables.add(Material.REDSTONE_COMPARATOR_OFF);
        interactables.add(Material.REDSTONE_COMPARATOR_ON);
        interactables.add(Material.STONE_BUTTON);
        interactables.add(Material.TRAPPED_CHEST);
        interactables.add(Material.TRAP_DOOR);
        interactables.add(Material.WOODEN_DOOR);
        interactables.add(Material.WOOD_BUTTON);
        interactables.add(Material.WORKBENCH);
        redstone.add(Material.DETECTOR_RAIL);
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
                    if (player.hasPermission("tardis.admin") && lore != null && lore.contains("Admin Upgrade")) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                ItemStack[] items = new TARDISAdminMenuInventory(plugin).getMenu();
                                Inventory menu = plugin.getServer().createInventory(player, 54, "§4Admin Menu");
                                menu.setContents(items);
                                player.openInventory(menu);
                            }
                        }, 35L);
                        return;
                    }
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
                                        TARDISMessage.send(hit, plugin.getPluginName() + player.getName() + " froze you with their Sonic Screwdriver!");
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
                            TARDISMessage.send(player, plugin.getPluginName() + player.getName() + " You cannot freeze another player yet!");
                        }
                        return;
                    }
                    if (player.hasPermission("tardis.sonic.standard")) {
                        standardSonic(player);
                        return;
                    }
                }
                if (action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking()) {
                    Inventory ppm = plugin.getServer().createInventory(player, 18, "§4Player Prefs Menu");
                    ppm.setContents(new TARDISPrefsMenuInventory(plugin, player.getUniqueId().toString()).getMenu());
                    player.openInventory(ppm);
                    return;
                }
                if (action.equals(Action.RIGHT_CLICK_BLOCK)) {
                    final Block b = event.getClickedBlock();
                    if (doors.contains(b.getType()) && player.hasPermission("tardis.admin") && lore != null && lore.contains("Admin Upgrade")) {
                        playSonicSound(player, now, 3050L, "sonic_screwdriver");
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
                                        String name = plugin.getServer().getPlayer(rsn.getUuid()).getName();
                                        TARDISMessage.send(player, plugin.getPluginName() + "This is " + name + "'s TARDIS");
                                        int percent = Math.round((rsn.getArtron_level() * 100F) / plugin.getArtronConfig().getInt("full_charge"));
                                        TARDISMessage.send(player, plugin.getPluginName() + "The Artron Energy Capacitor is at " + percent + "%");
                                        HashMap<String, Object> whereb = new HashMap<String, Object>();
                                        whereb.put("tardis_id", id);
                                        ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, whereb);
                                        if (rsb.resultSet()) {
                                            TARDISMessage.send(player, plugin.getPluginName() + "Its last location was: " + rsb.getWorld().getName() + " " + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ());
                                        }
                                    }
                                    HashMap<String, Object> whereid = new HashMap<String, Object>();
                                    whereid.put("tardis_id", id);
                                    ResultSetTravellers rst = new ResultSetTravellers(plugin, whereid, true);
                                    if (rst.resultSet()) {
                                        List<UUID> data = rst.getData();
                                        TARDISMessage.send(player, plugin.getPluginName() + "The players inside this TARDIS are:");
                                        for (UUID s : data) {
                                            TARDISMessage.send(player, plugin.getServer().getPlayer(s).getDisplayName());
                                        }
                                    } else {
                                        TARDISMessage.send(player, plugin.getPluginName() + "The TARDIS is unoccupied.");
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
                                    b.setType(Material.REDSTONE_LAMP_ON);
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
                    if (diamond.contains(b.getType()) && player.hasPermission("tardis.sonic.diamond") && lore != null && lore.contains("Diamond Upgrade")) {
                        // check the block is not protected by WorldGuard
                        if (plugin.isWorldGuardOnServer()) {
                            if (!plugin.getWorldGuardUtils().canBreakBlock(player, b)) {
                                TARDISMessage.send(player, plugin.getPluginName() + "That block is protected by WorldGuard!");
                                return;
                            }
                        }
                        playSonicSound(player, now, 600L, "sonic_short");

                        // drop appropriate material
                        if (player.hasPermission("tardis.sonic.silktouch")) {
                            Location l = b.getLocation();
                            switch (b.getType()) {
                                case GLASS:
                                    l.getWorld().dropItemNaturally(l, new ItemStack(Material.GLASS, 1));
                                    break;
                                case IRON_FENCE:
                                    l.getWorld().dropItemNaturally(l, new ItemStack(Material.IRON_FENCE, 1));
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
                            b.breakNaturally();
                            b.getLocation().getWorld().playSound(b.getLocation(), Sound.SHEEP_SHEAR, 1.0F, 1.5F);
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
            player.getItemInHand().setItemMeta(im);
            timeout.put(player.getUniqueId(), now + cooldown);
            plugin.getUtils().playTARDISSound(player.getLocation(), player, sound);
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
        final String daynight = plugin.getUtils().getTime(time);
        // message the player
        TARDISMessage.send(player, plugin.getPluginName() + "Sonic Screwdriver environmental scan started...");
        BukkitScheduler bsched = plugin.getServer().getScheduler();
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, "World: " + scan_loc.getWorld().getName());
                TARDISMessage.send(player, "Co-ordinates: " + scan_loc.getBlockX() + ":" + scan_loc.getBlockY() + ":" + scan_loc.getBlockZ());
            }
        }, 20L);
        // get biome
        final Biome biome = scan_loc.getBlock().getBiome();
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, "Biome type: " + biome);
            }
        }, 40L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, "Time of day: " + daynight + " / " + time + " ticks");
            }
        }, 60L);
        // get weather
        final String weather;
        if (biome.equals(Biome.DESERT) || biome.equals(Biome.DESERT_HILLS) || biome.equals(Biome.SAVANNA) || biome.equals(Biome.SAVANNA_MOUNTAINS) || biome.equals(Biome.SAVANNA_PLATEAU) || biome.equals(Biome.SAVANNA_PLATEAU_MOUNTAINS) || biome.equals(Biome.MESA) || biome.equals(Biome.MESA_BRYCE) || biome.equals(Biome.MESA_PLATEAU) || biome.equals(Biome.MESA_PLATEAU_MOUNTAINS)) {
            weather = "dry as a bone";
        } else if (biome.equals(Biome.ICE_PLAINS) || biome.equals(Biome.ICE_PLAINS_SPIKES) || biome.equals(Biome.FROZEN_OCEAN) || biome.equals(Biome.FROZEN_RIVER) || biome.equals(Biome.COLD_BEACH) || biome.equals(Biome.COLD_TAIGA) || biome.equals(Biome.COLD_TAIGA_HILLS) || biome.equals(Biome.COLD_TAIGA_MOUNTAINS)) {
            weather = (scan_loc.getWorld().hasStorm()) ? "snowing" : "clear, but cold";
        } else {
            weather = (scan_loc.getWorld().hasStorm()) ? "raining" : "clear";
        }
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, "Weather: " + weather);
            }
        }, 80L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, "Humidity: " + String.format("%.2f", scan_loc.getBlock().getHumidity()));
            }
        }, 100L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                TARDISMessage.send(player, "Temperature: " + String.format("%.2f", scan_loc.getBlock().getTemperature()));
            }
        }, 120L);
        bsched.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (scannedentities.size() > 0) {
                    TARDISMessage.send(player, "Nearby entities:");
                    for (Map.Entry<EntityType, Integer> entry : scannedentities.entrySet()) {
                        String message = "";
                        StringBuilder buf = new StringBuilder();
                        if (entry.getKey().equals(EntityType.PLAYER) && playernames.size() > 0) {
                            for (String p : playernames) {
                                buf.append(", ").append(p);
                            }
                            message = " (" + buf.toString().substring(2) + ")";
                        }
                        TARDISMessage.send(player, "    " + entry.getKey() + ": " + entry.getValue() + message);
                    }
                    scannedentities.clear();
                } else {
                    TARDISMessage.send(player, "Nearby entities: none");
                }
            }
        }, 140L);
    }

    @SuppressWarnings("deprecation")
    private void standardSonic(Player player) {
        Block targetBlock = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation().getBlock();
        Material blockType = targetBlock.getType();
        if (distance.contains(blockType)) {
            final BlockState bs = targetBlock.getState();
            switch (blockType) {
                case IRON_DOOR_BLOCK:
                case WOODEN_DOOR:
                    Block lowerdoor;
                    if (targetBlock.getData() >= 8) {
                        lowerdoor = targetBlock.getRelative(BlockFace.DOWN);
                    } else {
                        lowerdoor = targetBlock;
                    }
                    boolean allow = true;
                    // is Lockette or LWC on the server?
                    if (plugin.getPM().isPluginEnabled("Lockette")) {
                        Lockette Lockette = (Lockette) plugin.getPM().getPlugin("Lockette");
                        if (Lockette.isProtected(lowerdoor)) {
                            allow = false;
                        }
                    }
                    if (plugin.getPM().isPluginEnabled("LWC")) {
                        LWCPlugin lwcplug = (LWCPlugin) plugin.getPM().getPlugin("LWC");
                        LWC lwc = lwcplug.getLWC();
                        if (!lwc.canAccessProtection(player, lowerdoor)) {
                            allow = false;
                        }
                    }
                    // is it a TARDIS door?
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    String bw = lowerdoor.getLocation().getWorld().getName();
                    int bx = lowerdoor.getLocation().getBlockX();
                    int by = lowerdoor.getLocation().getBlockY();
                    int bz = lowerdoor.getLocation().getBlockZ();
                    String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                    where.put("door_location", doorloc);
                    ResultSetDoors rs = new ResultSetDoors(plugin, where, false);
                    if (rs.resultSet()) {
                        COMPASS dd = rs.getDoor_direction();
                        int id = rs.getTardis_id();
                        // is it the time lords or a companions door?
                        ResultSetCompanions rsc = new ResultSetCompanions(plugin, id);
                        if (rsc.getCompanions().contains(player.getUniqueId())) {
                            int type = rs.getDoor_type();
                            if (!rs.isLocked() && (type == 0 || type == 1)) { // only preset doors
                                // yes, yes and it is not deadlocked
                                new TARDISDoorToggler(plugin, lowerdoor, dd, player, false, id).toggleDoors();
                                return;
                            } else {
                                TARDISMessage.send(player, plugin.getPluginName() + "The door is deadlocked!");
                                return;
                            }
                        } else {
                            TARDISMessage.send(player, plugin.getPluginName() + "The door is deadlocked!");
                            return;
                        }
                    }
                    if (allow) {
                        BlockState bsl = lowerdoor.getState();
                        Door door = (Door) bsl.getData();
                        door.setOpen(!door.isOpen());
                        bsl.setData(door);
                        bsl.update(true);
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
}
