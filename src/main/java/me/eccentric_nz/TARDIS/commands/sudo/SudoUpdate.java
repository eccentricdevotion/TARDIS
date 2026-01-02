/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.messaging.TARDISUpdateLister;
import me.eccentric_nz.TARDIS.monitor.MonitorUtils;
import me.eccentric_nz.TARDIS.rotors.TARDISTimeRotor;
import me.eccentric_nz.TARDIS.update.TARDISUpdateableChecker;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    private final TARDIS plugin;

    SudoUpdate(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean initiate(Player player, String[] args, int id, UUID uuid) {
        if (args.length < 3) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return false;
        }
        String tardis_block = TARDISStringUtils.toScoredUppercase(args[2]);
        Updateable updateable;
        try {
            updateable = Updateable.valueOf(tardis_block);
        } catch (IllegalArgumentException e) {
            new TARDISUpdateLister(plugin, player).list();
            return true;
        }
        if (updateable.equals(Updateable.SIEGE) && !plugin.getConfig().getBoolean("siege.enabled")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_DISABLED");
            return true;
        }
        // get TARDIS data
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (updateable.equals(Updateable.HINGE)) {
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.IRON_DOOR)) {
                    Door door = (Door) block.getBlockData();
                    if (args.length == 3) {
                        Door.Hinge setHinge = Door.Hinge.valueOf(args[2].toUpperCase(Locale.ROOT));
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
                // update note block if it's not BARRIER
                Block block = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 10);
                if (block.getType().equals(Material.NOTE_BLOCK) || block.getType().equals(Material.MUSHROOM_STEM)) {
                    block.setBlockData(TARDISConstants.BARRIER, true);
                    TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.DISK_STORAGE, block, id);
                }
            }
            if (new TARDISUpdateableChecker(plugin, updateable, player, tardis, tardis_block).canUpdate()) {
                if ((updateable.equals(Updateable.ROTOR) || updateable.equals(Updateable.MONITOR) || updateable.equals(Updateable.MONITOR_FRAME))
                        && args.length == 4 && args[3].equalsIgnoreCase("unlock")) {
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
                            im.displayName(ComponentUtils.toWhite("Monitor Frame"));
                            glass.setItemMeta(im);
                        }
                        default -> {
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
                TARDISSudoTracker.SUDOERS.put(player.getUniqueId(), uuid);
                plugin.getTrackerKeeper().getUpdatePlayers().put(player.getUniqueId(), tardis_block);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "UPDATE_CLICK", tardis_block);
                if (updateable.equals(Updateable.DIRECTION)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "HOOK_REMIND");
                }
            }
        }
        return true;
    }
}
