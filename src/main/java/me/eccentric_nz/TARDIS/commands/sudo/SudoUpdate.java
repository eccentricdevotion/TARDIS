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
package me.eccentric_nz.tardis.commands.sudo;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.builders.TARDISTimeRotor;
import me.eccentric_nz.tardis.custommodeldata.TARDISMushroomBlockData;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.messaging.TARDISUpdateLister;
import me.eccentric_nz.tardis.update.TARDISUpdateChecker;
import me.eccentric_nz.tardis.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * The TARDIS interior goes through occasional metamorphoses, sometimes by choice, sometimes for other reasons, such as
 * the Doctor's own regeneration. Some of these changes were physical in nature (involving secondary control rooms,
 * etc.), but it was also possible to re-arrange the interior design of the TARDIS with ease, using the Architectural
 * Configuration system.
 *
 * @author eccentric_nz
 */
class SudoUpdate {

    private final TARDISPlugin plugin;

    SudoUpdate(TARDISPlugin plugin) {
        this.plugin = plugin;
    }

    boolean initiate(Player player, String[] args, int id, UUID uuid) {
        if (args.length < 3) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        String tardis_block = TARDISStringUtils.toScoredUppercase(args[2]);
        Updateable updateable;
        try {
            updateable = Updateable.valueOf(tardis_block);
        } catch (IllegalArgumentException e) {
            new TARDISUpdateLister(player).list();
            return true;
        }
        if (updateable.equals(Updateable.SIEGE) && !plugin.getConfig().getBoolean("siege.enabled")) {
            TARDISMessage.send(player, "SIEGE_DISABLED");
            return true;
        }
        // get TARDIS data
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 0);
        if (rs.resultSet()) {
            TARDIS tardis = rs.getTardis();
            if (updateable.equals(Updateable.HINGE)) {
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.IRON_DOOR)) {
                    Door door = (Door) block.getBlockData();
                    if (args.length == 3) {
                        Door.Hinge setHinge = Door.Hinge.valueOf(args[2].toUpperCase(Locale.ENGLISH));
                        door.setHinge(setHinge);
                    } else {
                        Door.Hinge hinge = door.getHinge();
                        if (hinge.equals(Door.Hinge.LEFT)) {
                            door.setHinge(Door.Hinge.RIGHT);
                        } else {
                            door.setHinge(Door.Hinge.LEFT);
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
                if (updateable.equals(Updateable.ROTOR) && args.length == 4 && args[3].equalsIgnoreCase("unlock")) {
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
                TARDISSudoTracker.SUDOERS.put(player.getUniqueId(), uuid);
                plugin.getTrackerKeeper().getPlayers().put(player.getUniqueId(), tardis_block);
                TARDISMessage.send(player, "UPDATE_CLICK", tardis_block);
                if (updateable.equals(Updateable.DIRECTION)) {
                    TARDISMessage.send(player, "HOOK_REMIND");
                }
            }
        }
        return true;
    }
}
