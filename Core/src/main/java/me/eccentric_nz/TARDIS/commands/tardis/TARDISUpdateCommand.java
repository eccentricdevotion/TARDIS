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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISUpdateLister;
import me.eccentric_nz.TARDIS.monitor.MonitorUtils;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicDock;
import me.eccentric_nz.TARDIS.update.TARDISUpdateBlocks;
import me.eccentric_nz.TARDIS.update.TARDISUpdateableChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISUpdateCommand {

    private final TARDIS plugin;

    TARDISUpdateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean startUpdate(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.update")) {
            if (args.length == 1) {
                return plugin.getUpdateGUI().showInterface(player, args);
            } else if (args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return false;
            }
            if (args[1].equalsIgnoreCase("list")) {
                for (Updateable u : Updateable.values()) {
                    System.out.println(u.getName() + " valid blocks:");
                    for (Material m : u.getMaterialChoices()) {
                        String s = m.toString();
                        if (s.equals("SPAWNER")) {
                            System.out.println("   ANY BLOCK");
                        } else {
                            System.out.println("   " + s);
                        }
                    }
                }
                return true;
            }
            UUID playerUUID = player.getUniqueId();
            if (args[1].equalsIgnoreCase("handles")) {
                if (args.length > 2) {
                    if (args[2].equalsIgnoreCase("lock")) {
                        plugin.getTrackerKeeper().getHandlesRotation().remove(playerUUID);
                        plugin.getMessenger().send(player, TardisModule.HANDLES, "HANDLES_LOCKED");
                    } else {
                        plugin.getTrackerKeeper().getHandlesRotation().add(playerUUID);
                        plugin.getMessenger().send(player, TardisModule.HANDLES, "HANDLES_ROTATE");
                    }
                    return true;
                }
                return false;
            }
            HashMap<String, Object> where = new HashMap<>();
            String uuid = (TARDISSudoTracker.SUDOERS.containsKey(playerUUID)) ? TARDISSudoTracker.SUDOERS.get(playerUUID).toString() : playerUUID.toString();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return false;
            }
            Tardis tardis = rs.getTardis();
            String tardis_block = TARDISStringUtils.toScoredUppercase(args[1]);
            Updateable updateable;
            try {
                updateable = Updateable.valueOf(tardis_block);
            } catch (IllegalArgumentException e) {
                new TARDISUpdateLister(plugin, player).list();
                return true;
            }
            if (args.length == 3 && args[2].equalsIgnoreCase("blocks")) {
                TARDISUpdateBlocks.showOptions(player, updateable);
                return true;
            }
            if (updateable.equals(Updateable.HINGE)) {
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.IRON_DOOR)) {
                    Door door = (Door) block.getBlockData();
                    if (args.length == 3) {
                        Hinge setHinge = Hinge.valueOf(args[2].toUpperCase(Locale.ROOT));
                        door.setHinge(setHinge);
                    } else {
                        Hinge hinge = door.getHinge();
                        if (hinge.equals(Hinge.LEFT)) {
                            door.setHinge(Hinge.RIGHT);
                        } else {
                            door.setHinge(Hinge.LEFT);
                        }
                    }
                    block.setBlockData(door);
                }
                return true;
            }
            if (updateable.equals(Updateable.STORAGE)) {
                // update note block if it's not BARRIER
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.NOTE_BLOCK) || block.getType().equals(Material.MUSHROOM_STEM)) {
                    block.setBlockData(TARDISConstants.BARRIER, true);
                    TARDISDisplayItemUtils.set(TARDISDisplayItem.DISK_STORAGE, block, tardis.getTardisId());
                }
            }
            if (new TARDISUpdateableChecker(plugin, updateable, player, tardis, tardis_block).canUpdate()) {
                if ((updateable.equals(Updateable.ROTOR) || updateable.equals(Updateable.MONITOR) || updateable.equals(Updateable.MONITOR_FRAME) || updateable.equals(Updateable.SONIC_DOCK)) && args.length == 3 && args[2].equalsIgnoreCase("unlock")) {
                    // get frame location
                    ItemFrame itemFrame = null;
                    switch (updateable) {
                        case ROTOR -> itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                        case MONITOR -> itemFrame = MonitorUtils.getItemFrameFromLocation(tardis.getTardisId(), true);
                        case MONITOR_FRAME -> {
                            itemFrame = MonitorUtils.getItemFrameFromLocation(tardis.getTardisId(), false);
                            // reinstate display name
                            ItemStack glass = itemFrame.getItem();
                            ItemMeta im = glass.getItemMeta();
                            im.setDisplayName("Monitor Frame");
                            glass.setItemMeta(im);
                        }
                        case SONIC_DOCK -> itemFrame = TARDISSonicDock.getItemFrame(tardis.getTardisId());
                    }
                    if (itemFrame != null) {
                        TARDISTimeRotor.unlockItemFrame(itemFrame);
                        // also need to remove the item frame protection
                        plugin.getGeneralKeeper().getTimeRotors().remove(itemFrame.getUniqueId());
                        // and block protection
                        Block block = itemFrame.getLocation().getBlock();
                        String location = block.getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().remove(location);
                        String under = block.getRelative(BlockFace.DOWN).getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().remove(under);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ROTOR_UNFIXED");
                    }
                    return true;
                }
                plugin.getTrackerKeeper().getUpdatePlayers().put(playerUUID, tardis_block);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_CLICK", tardis_block);
                if (updateable.equals(Updateable.DIRECTION)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "HOOK_REMIND");
                }
            }
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }

    private boolean isConsole(String str) {
        return Consoles.getBY_MATERIALS().containsKey(str);
    }
}
