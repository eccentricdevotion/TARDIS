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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISBuildData;
import me.eccentric_nz.TARDIS.builders.TARDISSeedBlockProcessor;
import me.eccentric_nz.TARDIS.console.ConsoleBuilder;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.VariableLight;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInteractionCheck;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISSeedBlockListener implements Listener {

    private final TARDIS plugin;

    public TARDISSeedBlockListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Store the TARDIS Seed block's values for use when clicked with the TARDIS key to activate growing, or to return
     * the block if broken.
     *
     * @param event The TARDIS Seed block placement event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack is = player.getInventory().getItemInMainHand();
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasLore()) {
            return;
        }
        String dn = im.getDisplayName();
        if (dn.equals(ChatColor.GOLD + "TARDIS Seed Block")) {
            Block block = event.getBlockPlaced();
            if (im.getPersistentDataContainer().has(plugin.getCustomBlockKey(), PersistentDataType.STRING)) {
                String key = im.getPersistentDataContainer().get(plugin.getCustomBlockKey(), PersistentDataType.STRING);
                NamespacedKey which = new NamespacedKey(plugin, key);
                block.setBlockData(TARDISConstants.BARRIER);
                TARDISDisplayItem tdi = TARDISDisplayItem.getByModel(which);
                if (tdi == null) {
                    tdi = TARDISDisplayItem.CUSTOM;
                }
                TARDISDisplayItemUtils.setSeed(tdi, block, im);
            }
            List<String> lore = im.getLore();
            Schematic schm = Consoles.getBY_NAMES().get(lore.getFirst());
            Material wall = Material.valueOf(TARDISStringUtils.getValuesFromWallString(lore.get(1)));
            Material floor = Material.valueOf(TARDISStringUtils.getValuesFromWallString(lore.get(2)));
            TARDISBuildData seed = new TARDISBuildData();
            seed.setSchematic(schm);
            seed.setWallType(wall);
            seed.setFloorType(floor);
            Location l = block.getLocation();
            plugin.getBuildKeeper().getTrackTARDISSeed().put(l, seed);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SEED_PLACE");
            // send fake block change for bedrock players
            if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    player.sendBlockChange(l, is.getType().createBlockData());
                }, 3L);
            }
            // now the player has to click the block with the TARDIS key
        } else if (dn.endsWith(" Console") && (is.getType().toString().endsWith("_CONCRETE") || is.getType() == Material.WAXED_OXIDIZED_COPPER)) {
            // must be in TARDIS world
            if (!plugin.getUtils().inTARDISWorld(player)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_IN_WORLD");
                event.setCancelled(true);
                return;
            }
            // must not have a console already
            ResultSetInteractionCheck rsi = new ResultSetInteractionCheck(plugin);
            if (rsi.resultSetFromUUID(player.getUniqueId())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CONSOLE_HAS");
                event.setCancelled(true);
                return;
            }
            // build a console of the correct colour
            String type = im.getPersistentDataContainer().getOrDefault(plugin.getCustomBlockKey(), PersistentDataType.STRING, "LIGHT_GRAY");
            // get TARDIS id
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            String uuid = player.getUniqueId().toString();
            if (rs.fromUUID(uuid)) {
                new ConsoleBuilder(plugin).create(event.getBlockPlaced(), type, rs.getTardisId(), uuid);
            }
        } else if (dn.endsWith("Variable Light") && is.getType() == Material.GLASS) {
            List<String> lore = im.getLore();
            Material variable = Material.valueOf(lore.getFirst());
            // place the variable light
            new VariableLight(variable, event.getBlockPlaced().getLocation().add(0.5d, 0.5d, 0.5d)).set();
        }
    }

    /**
     * Return the TARDIS seed block to the player after it is broken.
     *
     * @param event a block break event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedBlockBreak(BlockBreakEvent event) {
        Location l = event.getBlock().getLocation();
        Player p = event.getPlayer();
        if (plugin.getBuildKeeper().getTrackTARDISSeed().containsKey(l)) {
            if (!p.getGameMode().equals(GameMode.CREATIVE)) {
                // get the Seed block data
                TARDISBuildData data = plugin.getBuildKeeper().getTrackTARDISSeed().get(l);
                // drop a TARDIS Seed Block
                World w = l.getWorld();
                // give back a new display item
                String console = data.getSchematic().getPermission().toUpperCase(Locale.ROOT);
                NamespacedKey model = TARDISDisplayItem.CUSTOM.getCustomModel();
                ItemStack is;
                if (data.getSchematic().isCustom()) {
                    is = new ItemStack(data.getSchematic().getSeedMaterial(), 1);
                } else {
                    try {
                        TARDISDisplayItem tdi = TARDISDisplayItem.valueOf(console);
                        is = new ItemStack(tdi.getMaterial(), 1);
                        model = tdi.getCustomModel();
                    } catch (IllegalArgumentException e) {
                        return;
                    }
                }
                ItemMeta im = is.getItemMeta();
                if (im == null) {
                    return;
                }
                im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, model.getKey());
                im.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
                im.setItemModel(model);
                List<String> lore = new ArrayList<>();
                lore.add(console);
                lore.add("Walls: " + data.getWallType().toString());
                lore.add("Floors: " + data.getFloorType().toString());
                im.setLore(lore);
                is.setItemMeta(im);
                // set the block to AIR
                event.getBlock().setBlockData(TARDISConstants.AIR);
                w.dropItemNaturally(l, is);
            }
            plugin.getBuildKeeper().getTrackTARDISSeed().remove(l);
        }
    }

    /**
     * Process the TARDIS seed block and turn it into a TARDIS!
     *
     * @param event a block interact event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        if (event.getClickedBlock() != null) {
            Location l = event.getClickedBlock().getLocation();
            if (plugin.getBuildKeeper().getTrackTARDISSeed().containsKey(l)) {
                Player player = event.getPlayer();
                String key;
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                if (rsp.resultSet()) {
                    key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
                } else {
                    key = plugin.getConfig().getString("preferences.key");
                }
                if (player.getInventory().getItemInMainHand().getType().equals(Material.valueOf(key))) {
                    if (!plugin.getPlanetsConfig().getBoolean("planets." + l.getWorld().getName() + ".time_travel")) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "WORLD_NO_TARDIS");
                        return;
                    }
                    if (!plugin.getConfig().getString("creation.area").equals("none")) {
                        String area = plugin.getConfig().getString("creation.area");
                        if (plugin.getTardisArea().areaCheckInExile(area, l)) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_ONLY_AREA", area);
                            return;
                        }
                    }
                    // grow a TARDIS
                    TARDISBuildData seed = plugin.getBuildKeeper().getTrackTARDISSeed().get(l);
                    // process seed data
                    if (new TARDISSeedBlockProcessor(plugin).processBlock(seed, l, player)) {
                        // remove seed data
                        plugin.getBuildKeeper().getTrackTARDISSeed().remove(l);
                        // replace seed block with animated grow block
                        Block block = event.getClickedBlock();
                        TARDISDisplayItemUtils.remove(block);
                        block.setBlockData(TARDISConstants.BARRIER);
                        TARDISDisplayItemUtils.set(TARDISDisplayItem.GROW, block, -1);
                        // send fake block change for bedrock players
                        if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                player.sendBlockChange(l, Material.LIGHT_GRAY_TERRACOTTA.createBlockData());
                            }, 3L);
                        }
                    }
                }
            }
        }
    }
}
