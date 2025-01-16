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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.ModelledControl;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISDecommissionCommand {

    private final TARDIS plugin;

    TARDISDecommissionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean withdraw(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.update")) {
            if (args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return false;
            }
            HashMap<String, Object> where = new HashMap<>();
            UUID playerUUID = player.getUniqueId();
            String uuid = (TARDISSudoTracker.SUDOERS.containsKey(playerUUID)) ? TARDISSudoTracker.SUDOERS.get(playerUUID).toString() : playerUUID.toString();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return false;
            }
            String tardis_block = TARDISStringUtils.toScoredUppercase(args[1]);
            Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
            String l = block.getLocation().toString();
            Updateable updateable;
            try {
                updateable = Updateable.valueOf(tardis_block);
                if (updateable.equals(Updateable.ROTOR)) {
                    // use update unlock command
                    plugin.getMessenger().sendColouredCommand(player,"ROTOR_UNLOCK", "/tardis update rotor unlock", plugin);
                    return true;
                }
                if (updateable.equals(Updateable.STORAGE)) {
                    // withdraw record if there is one for this location
                    block.setType(Material.NOTE_BLOCK);
                    TARDISDisplayItemUtils.remove(block);
                }
                if (updateable.equals(Updateable.ADVANCED)) {
                    // withdraw record if there is one for this location
                    block.setType(Material.JUKEBOX);
                    TARDISDisplayItemUtils.remove(block);
                }
                if (updateable.usesItemFrame()) {
                    // get frame location
                    ItemFrame itemFrame = null;
                    faces:
                    for (BlockFace face : plugin.getGeneralKeeper().getBlockFaces()) {
                        Block b = block.getRelative(face);
                        for (Entity e : b.getWorld().getNearbyEntities(b.getLocation(), 1,1,1, (d) -> d.getType() == EntityType.ITEM_FRAME)) {
                            if (e instanceof ItemFrame frame) {
                                itemFrame = frame;
                                break faces;
                            }
                        }
                    }
                    if (itemFrame != null) {
                        switch (updateable) {
                            case MONITOR -> {
                                ItemStack monitor = new ItemStack(Material.MAP);
                                ItemMeta im = monitor.getItemMeta();
                                im.setDisplayName("TARDIS Monitor");
                                im.setItemModel(ModelledControl.MONITOR.getKey());
                                monitor.setItemMeta(im);
                                itemFrame.setItem(monitor);
                            }
                            case MONITOR_FRAME -> {
                                // reinstate display name
                                ItemStack glass = itemFrame.getItem();
                                ItemMeta im = glass.getItemMeta();
                                im.setDisplayName("Monitor Frame");
                                glass.setItemMeta(im);
                            }
                            case SONIC_DOCK -> {
                                ItemDisplay display = TARDISDisplayItemUtils.getFromBoundingBox(itemFrame.getLocation().getBlock());
                                // if contains a sonic return...
                                if (display != null) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DOCK_REMOVE");
                                    return true;
                                }
                            }
                            default -> {
                            }
                        }
                        TARDISTimeRotor.unlockItemFrame(itemFrame);
                        // also need to withdraw the item frame protection
                        plugin.getGeneralKeeper().getTimeRotors().remove(itemFrame.getUniqueId());
                        // and block protection
                        block = itemFrame.getLocation().getBlock();
                        l = itemFrame.getLocation().toString();

                    }
                    plugin.getGeneralKeeper().getProtectBlockMap().remove(l);
                    String under = block.getRelative(BlockFace.DOWN).getLocation().toString();
                    plugin.getGeneralKeeper().getProtectBlockMap().remove(under);
                    // withdraw record for this location if there is one
                    int control = Control.getUPDATE_CONTROLS().get(updateable.getName());
                    removeRecord(control, l);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "DECOMMISSIONED");
                    return true;
                }
            } catch (IllegalArgumentException e) {
                return true;
            }
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }

    private void removeRecord(int id, String location) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("type", id);
        where.put("location", location);
        plugin.getQueryFactory().doDelete("controls", where);
    }
}
