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

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockConverter;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayBlockRoomConverter;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISUpdateLister;
import me.eccentric_nz.TARDIS.monitor.MonitorUtils;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.update.TARDISUpdateBlocks;
import me.eccentric_nz.TARDIS.update.TARDISUpdateableChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Chunk;
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
                    for (Material m : u.getMaterialChoice().getChoices()) {
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
            if (args[1].equalsIgnoreCase("item_display_lights")) {
                TARDISDisplayBlockConverter converter = new TARDISDisplayBlockConverter(plugin, player);
                int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, converter, 5, 1);
                converter.setTaskId(taskId);
                // also find any rooms and convert the mushroom blocks there
                // get players tardis_id
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (rst.fromUUID(player.getUniqueId().toString())) {
                    int id = rst.getTardis_id();
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    ResultSetARS rsa = new ResultSetARS(plugin, where);
                    if (rsa.resultSet()) {
                        String[][][] json = TARDISARSMethods.getGridFromJSON(rsa.getJson());
                        Chunk c = plugin.getLocationUtils().getTARDISChunk(id);
                        for (int l = 0; l < 3; l++) {
                            for (int row = 0; row < 9; row++) {
                                for (int col = 0; col < 9; col++) {
                                    if (!json[l][row][col].equalsIgnoreCase("STONE") && !isConsole(json[l][row][col])) {
                                        // get ARS slot
                                        TARDISARSSlot slot = new TARDISARSSlot();
                                        slot.setChunk(c);
                                        slot.setY(l);
                                        slot.setX(row);
                                        slot.setZ(col);
                                        TARDISDisplayBlockRoomConverter roomConverter = new TARDISDisplayBlockRoomConverter(plugin, player, slot);
                                        int roomTaskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, roomConverter, 5, 1);
                                        roomConverter.setTaskId(roomTaskId);
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
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
                        Hinge setHinge = Hinge.valueOf(args[2].toUpperCase(Locale.ENGLISH));
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
                if ((updateable.equals(Updateable.ROTOR) || updateable.equals(Updateable.MONITOR) || updateable.equals(Updateable.MONITOR_FRAME))
                        && args.length == 3 && args[2].equalsIgnoreCase("unlock")) {
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
