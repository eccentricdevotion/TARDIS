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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.commands.sudo.TARDISSudoTracker;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.messaging.TARDISUpdateLister;
import me.eccentric_nz.TARDIS.update.TARDISUpdateBlocks;
import me.eccentric_nz.TARDIS.update.TARDISUpdateChecker;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Door.Hinge;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

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
                return new TARDISUpdateChatGUI(plugin).showInterface(player, args);
            } else if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
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
            HashMap<String, Object> where = new HashMap<>();
            UUID playerUUID = player.getUniqueId();
            String uuid = (TARDISSudoTracker.SUDOERS.containsKey(playerUUID)) ? TARDISSudoTracker.SUDOERS.get(playerUUID).toString() : playerUUID.toString();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            Tardis tardis = rs.getTardis();
            String tardis_block = TARDISStringUtils.toScoredUppercase(args[1]);
            Updateable updateable;
            try {
                updateable = Updateable.valueOf(tardis_block);
            } catch (IllegalArgumentException e) {
                new TARDISUpdateLister(player).list();
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
                // update note block if it's not MUSHROOM_STEM
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.NOTE_BLOCK)) {
                    BlockData mushroom = plugin.getServer().createBlockData(TARDISMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
                    block.setBlockData(mushroom, true);
                }
            }
            if (new TARDISUpdateChecker(plugin, updateable, player, tardis, tardis_block).canUpdate()) {
                if (updateable.equals(Updateable.ROTOR) && args.length == 3 && args[2].equalsIgnoreCase("unlock")) {
                    // get Time Rotor frame location
                    ItemFrame itemFrame = TARDISTimeRotor.getItemFrame(tardis.getRotor());
                    if (itemFrame != null) {
                        TARDISTimeRotor.unlockRotor(itemFrame);
                        // also need to remove the item frame protection
                        plugin.getGeneralKeeper().getTimeRotors().remove(itemFrame.getUniqueId());
                        // and block protection
                        Block block = itemFrame.getLocation().getBlock();
                        String location = block.getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().remove(location);
                        String under = block.getRelative(BlockFace.DOWN).getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().remove(under);
                        TARDISMessage.send(player, "ROTOR_UNFIXED");
                    }
                    return true;
                }
                plugin.getTrackerKeeper().getUpdatePlayers().put(playerUUID, tardis_block);
                TARDISMessage.send(player, "UPDATE_CLICK", tardis_block);
                if (updateable.equals(Updateable.DIRECTION)) {
                    TARDISMessage.send(player, "HOOK_REMIND");
                }
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
