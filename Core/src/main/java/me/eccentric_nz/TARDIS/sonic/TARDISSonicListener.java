/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.custommodels.keys.SonicVariant;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.sonic.actions.*;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TARDISSonicListener implements Listener {

    private final TARDIS plugin;
    private final Set<Material> diamond = new HashSet<>();
    private final Set<Material> doors = new HashSet<>();
    private final Set<Material> redstone = new HashSet<>();
    private final Set<Material> ignite = new HashSet<>();
    private final Set<Material> suspicious = new HashSet<>();
    private final Set<Material> converts = new HashSet<>();

    public TARDISSonicListener(TARDIS plugin) {
        this.plugin = plugin;
        diamond.add(Material.COBWEB);
        diamond.add(Material.IRON_BARS);
        diamond.add(Material.SNOW);
        diamond.add(Material.SNOW_BLOCK);
        diamond.addAll(TARDISMaterials.glass);
        doors.addAll(Tag.DOORS.getValues());
        doors.addAll(Tag.TRAPDOORS.getValues());
        redstone.add(Material.DETECTOR_RAIL);
        redstone.add(Material.IRON_DOOR);
        redstone.add(Material.IRON_TRAPDOOR);
        redstone.add(Material.MUSHROOM_STEM);
        redstone.add(Material.PISTON);
        redstone.add(Material.STICKY_PISTON);
        redstone.add(Material.POWERED_RAIL);
        redstone.add(Material.REDSTONE_LAMP);
        redstone.add(Material.REDSTONE_WIRE);
        redstone.add(Material.COPPER_BULB);
        redstone.add(Material.EXPOSED_COPPER_BULB);
        redstone.add(Material.OXIDIZED_COPPER_BULB);
        redstone.add(Material.WAXED_COPPER_BULB);
        redstone.add(Material.WAXED_EXPOSED_COPPER_BULB);
        redstone.add(Material.WAXED_OXIDIZED_COPPER_BULB);
        redstone.add(Material.WAXED_WEATHERED_COPPER_BULB);
        redstone.add(Material.WEATHERED_COPPER_BULB);
        ignite.addAll(Tag.CAMPFIRES.getValues());
        ignite.addAll(Tag.CANDLES.getValues());
        ignite.addAll(Tag.CANDLE_CAKES.getValues());
        ignite.add(Material.NETHERRACK);
        ignite.add(Material.OBSIDIAN);
        ignite.add(Material.SOUL_SAND);
        ignite.add(Material.SOUL_SOIL);
        suspicious.add(Material.SUSPICIOUS_GRAVEL);
        suspicious.add(Material.SUSPICIOUS_SAND);
        converts.addAll(Tag.CONCRETE_POWDER.getValues());
        converts.add(Material.MUD);
        converts.add(Material.DIRT);
        converts.add(Material.COARSE_DIRT);
        converts.add(Material.ROOTED_DIRT);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        long now = System.currentTimeMillis();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is.getType().equals(Material.BLAZE_ROD) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.getDisplayName().endsWith("Sonic Screwdriver")) {
                // check they have charge
                if (plugin.getConfig().getBoolean("sonic.charge")) {
                    // get sonic UUID
                    PersistentDataContainer pdc = im.getPersistentDataContainer();
                    int needs = plugin.getConfig().getInt("sonic.usage");
                    int charge;
                    if (pdc.has(plugin.getSonicChargeKey(), PersistentDataType.INTEGER)) {
                        charge = pdc.get(plugin.getSonicChargeKey(), PersistentDataType.INTEGER);
                        if (needs > charge) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SONIC_RECHARGE");
                            return;
                        }
                    } else {
                        charge = 500;
                        HashMap<String, Object> set = new HashMap<>();
                        if (!pdc.has(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID())) {
                            UUID sonic_uuid = UUID.randomUUID();
                            pdc.set(plugin.getSonicUuidKey(), plugin.getPersistentDataTypeUUID(), sonic_uuid);
                            set.put("sonic_uuid", sonic_uuid.toString());
                            // get sonic data
                            set.put("uuid", player.getUniqueId().toString());
                            if (im.hasLore()) {
                                List<Integer> settings = TARDISSonicData.getSonicData(im.getLore());
                                for (int i = 0; i < settings.size(); i++) {
                                    set.put(SonicUpgradeData.upgrades.get(SonicUpgradeData.sonicKeys.get(i)), settings.get(i));
                                }
                            }
                            // insert
                            plugin.getQueryFactory().doInsert("sonic", set);
                        }
                    }
                    pdc.set(plugin.getSonicChargeKey(), PersistentDataType.INTEGER, charge - needs);
                    is.setItemMeta(im);
                    SonicLore.setChargeLevel(is);
                }
                List<String> lore = im.getLore();
                Action action = event.getAction();
                if (action.equals(Action.RIGHT_CLICK_AIR) && !player.isSneaking()) {
                    UUID uuid = player.getUniqueId();
                    // rebuild Police Box if dispersed by HADS
                    if (plugin.getTrackerKeeper().getDispersed().containsKey(uuid)) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        TARDISSonicDispersed.assemble(plugin, player);
                    } else if (lore != null && (lore.contains("Bio-scanner Upgrade") || lore.contains("Knockback Upgrade"))) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        if (TARDISPermission.hasPermission(player, "tardis.sonic.freeze")) {
                            // freeze target player
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                Player target = TARDISSonicFreeze.getTargetPlayer(player);
                                if (target != null) {
                                    TARDISSonicFreeze.immobilise(plugin, player, target);
                                }
                            }, 20L);
                        }
                        if (TARDISPermission.hasPermission(player, "tardis.sonic.knockback")) {
                            // knockback any monsters in line of sight
                            Entity target = TARDISSonicKnockback.getTargetEntity(player);
                            if (target != null) {
                                TARDISSonicKnockback.knockback(player, target);
                            }
                        }
                    }
                    if (TARDISPermission.hasPermission(player, "tardis.sonic.standard")) {
                        TARDISSonic.standardSonic(plugin, player, now);
                        if (plugin.getTrackerKeeper().getFlyingReturnLocation().containsKey(uuid)) {
                            // toggle door open / closed
                            if (plugin.getTrackerKeeper().getSonicDoorToggle().contains(uuid)) {
                                plugin.getTrackerKeeper().getSonicDoorToggle().remove(uuid);
                            } else {
                                plugin.getTrackerKeeper().getSonicDoorToggle().add(uuid);
                            }
                        }
                        return;
                    }
                }
                if (action.equals(Action.RIGHT_CLICK_AIR) && player.isSneaking() && TARDISPermission.hasPermission(player, "tardis.sonic.standard")) {
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
                    if (Tag.WALL_SIGNS.isTagged(block.getType()) && TARDISPermission.hasPermission(player, "tardis.atmospheric")) {
                        // make it snow
                        TARDISSonicAtmospheric.makeItSnow(plugin, player, block);
                        return;
                    }
                    if (TARDISPermission.hasPermission(player, "tardis.sonic.arrow") && lore != null && lore.contains("Pickup Arrows Upgrade")) {
                        if (!block.getType().isInteractable()) {
                            TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
                        }
                        // scan area around block for an arrow
                        for (Entity e : block.getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2)) {
                            if (e instanceof Arrow arrow) {
                                // pick up arrow
                                arrow.setPickupStatus(AbstractArrow.PickupStatus.ALLOWED);
                                return;
                            }
                        }
                    }
                    if (redstone.contains(block.getType()) && TARDISPermission.hasPermission(player, "tardis.sonic.redstone") && lore != null && lore.contains("Redstone Upgrade")) {
                        // toggle powered state
                        TARDISSonicRedstone.togglePoweredState(plugin, player, block);
                        return;
                    }
                    if (converts.contains(block.getType()) && TARDISPermission.hasPermission(player, "tardis.sonic.conversion") && lore != null && lore.contains("Conversion Upgrade")) {
                        // convert to water added block i.e. CONCRETE_POWDER -> CONCRETE
                        TARDISSonicBlockConverter.transform(plugin, block, player);
                        TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
                        return;
                    }
                    if (suspicious.contains(block.getType()) && TARDISPermission.hasPermission(player, "tardis.sonic.brush") && lore != null && lore.contains("Brush Upgrade")) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 600L, "sonic_short");
                        TARDISSonicBrush.dust(plugin, block, player);
                        return;
                    }
                    if (TARDISPermission.hasPermission(player, "tardis.sonic.emerald") && lore != null && lore.contains("Emerald Upgrade") && !block.getType().isInteractable()) {
                        TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_screwdriver");
                        // scan environment
                        Location scanned = block.getLocation();
                        TARDISSonicScanner.scan(plugin, scanned, player);
                        if (!plugin.getUtils().inTARDISWorld(player)) {
                            // save scanned location to sonic table
                            new TARDISSonicData().saveOrUpdate(plugin, scanned.add(0, 1, 0).toString(), 0, is, player);
                        }
                    }
                }
                if (action.equals(Action.LEFT_CLICK_BLOCK)) {
                    Block block = event.getClickedBlock();
                    if (!player.isSneaking()) {
                        if ((block.getType().isBurnable() || ignite.contains(block.getType())) && TARDISPermission.hasPermission(player, "tardis.sonic.ignite") && lore != null && lore.contains("Ignite Upgrade")) {
                            TARDISSonicSound.playSonicSound(plugin, player, now, 3050L, "sonic_short");
                            // ignite block
                            event.setCancelled(true);
                            TARDISSonicIgnite.ignite(plugin, block, player);
                        }
                        if (diamond.contains(block.getType()) && TARDISPermission.hasPermission(player, "tardis.sonic.diamond") && lore != null && lore.contains("Diamond Upgrade")) {
                            // break block
                            TARDISSonicDisruptor.breakBlock(plugin, player, block);
                        }
                    } else if (TARDISPermission.hasPermission(player, "tardis.sonic.paint") && lore != null && lore.contains("Painter Upgrade")) {
                        event.setCancelled(true);
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

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSonicDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack is = item.getItemStack();
        if (is.getType().equals(Material.BLAZE_ROD) && is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.getDisplayName().endsWith("Sonic Screwdriver")) {
                // set to off state
                if (im.hasItemModel()) {
                    NamespacedKey model = im.getItemModel();
                    if (model.getKey().endsWith("_open")) {
                        im.setItemModel(new NamespacedKey(plugin, model.getKey().replace("_open", "")));
                    }
                } else {
                    im.setItemModel(SonicVariant.ELEVENTH.getKey());
                }
                is.setItemMeta(im);
                item.setItemStack(is);
            }
        }
    }
}
