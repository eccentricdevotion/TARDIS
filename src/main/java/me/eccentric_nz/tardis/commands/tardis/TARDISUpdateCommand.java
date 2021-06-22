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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.TardisTimeRotor;
import me.eccentric_nz.tardis.chatgui.TardisChatGuiUpdater;
import me.eccentric_nz.tardis.commands.sudo.TardisSudoTracker;
import me.eccentric_nz.tardis.custommodeldata.TardisMushroomBlockData;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.messaging.TardisUpdateLister;
import me.eccentric_nz.tardis.update.TardisUpdateBlocks;
import me.eccentric_nz.tardis.update.TardisUpdateChecker;
import me.eccentric_nz.tardis.utility.TardisStringUtils;
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
class TardisUpdateCommand {

    private final TardisPlugin plugin;

    TardisUpdateCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }


    boolean startUpdate(Player player, String[] args) {
        if (TardisPermission.hasPermission(player, "tardis.update")) {
            if (args.length == 1) {
                return new TardisChatGuiUpdater(plugin).showInterface(player, args);
            } else if (args.length < 2) {
                TardisMessage.send(player, "TOO_FEW_ARGS");
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
            String uuid = (TardisSudoTracker.SUDOERS.containsKey(playerUUID)) ? TardisSudoTracker.SUDOERS.get(playerUUID).toString() : playerUUID.toString();
            where.put("uuid", uuid);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TardisMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            Tardis tardis = rs.getTardis();
            String tardis_block = TardisStringUtils.toScoredUppercase(args[1]);
            Updateable updateable;
            try {
                updateable = Updateable.valueOf(tardis_block);
            } catch (IllegalArgumentException e) {
                new TardisUpdateLister(player).list();
                return true;
            }
            if (args.length == 3 && args[2].equalsIgnoreCase("blocks")) {
                TardisUpdateBlocks.showOptions(player, updateable);
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
                    BlockData mushroom = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
                    block.setBlockData(mushroom, true);
                }
            }
            if (new TardisUpdateChecker(plugin, updateable, player, tardis, tardis_block).canUpdate()) {
                if (updateable.equals(Updateable.ROTOR) && args.length == 3 && args[2].equalsIgnoreCase("unlock")) {
                    // get Time Rotor frame location
                    ItemFrame itemFrame = TardisTimeRotor.getItemFrame(tardis.getRotor());
                    if (itemFrame != null) {
                        TardisTimeRotor.unlockRotor(itemFrame);
                        // also need to remove the item frame protection
                        plugin.getGeneralKeeper().getTimeRotors().remove(itemFrame.getUniqueId());
                        // and block protection
                        Block block = itemFrame.getLocation().getBlock();
                        String location = block.getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().remove(location);
                        String under = block.getRelative(BlockFace.DOWN).getLocation().toString();
                        plugin.getGeneralKeeper().getProtectBlockMap().remove(under);
                        TardisMessage.send(player, "ROTOR_UNFIXED");
                    }
                    return true;
                }
                plugin.getTrackerKeeper().getPlayers().put(playerUUID, tardis_block);
                TardisMessage.send(player, "UPDATE_CLICK", tardis_block);
                if (updateable.equals(Updateable.DIRECTION)) {
                    TardisMessage.send(player, "HOOK_REMIND");
                }
            }
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
