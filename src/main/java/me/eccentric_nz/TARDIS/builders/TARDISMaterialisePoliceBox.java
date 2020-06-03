/*
 * Copyright (C) 2020 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.ReplacedBlock;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISMaterialisePoliceBox implements Runnable {

    private final TARDIS plugin;
    private final BuildData bd;
    private final int loops;
    private final PRESET preset;
    private int task;
    private int i;
    private ItemFrame frame;
    private ItemStack is;
    private ItemMeta im;
    private Material dye;

    public TARDISMaterialisePoliceBox(TARDIS plugin, BuildData bd, int loops, PRESET preset) {
        this.plugin = plugin;
        this.bd = bd;
        this.loops = loops;
        this.preset = preset;
        i = 0;
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisID())) {
            World world = bd.getLocation().getWorld();
            if (i < loops) {
                i++;
                int cmd;
                switch (i % 3) {
                    case 2: // stained
                        cmd = 1003;
                        break;
                    case 1: // glass
                        cmd = 1004;
                        break;
                    default: // preset
                        cmd = 1001;
                        break;
                }
                // first run
                if (i == 1) {
                    TARDISBuilderUtility.saveDoorLocation(bd);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisID());
                    boolean found = false;
                    for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                        if (e instanceof ItemFrame) {
                            frame = (ItemFrame) e;
                            found = true;
                        }
                    }
                    if (!found) {
                        Block block = bd.getLocation().getBlock();
                        Block under = block.getRelative(BlockFace.DOWN);
                        block.setBlockData(TARDISConstants.AIR);
                        if (under.getType().isAir()) {
                            TARDISBlockSetters.setBlockAndRemember(under, Material.BARRIER, bd.getTardisID(), 1);
                        }
                        // spawn item frame
                        frame = (ItemFrame) world.spawnEntity(bd.getLocation(), EntityType.ITEM_FRAME);
                    }
                    frame.setFacingDirection(BlockFace.UP);
                    frame.setRotation(bd.getDirection().getRotation());
                    dye = TARDISBuilderUtility.getDyeMaterial(preset);
                    is = new ItemStack(dye, 1);
                    if (bd.isOutside()) {
                        if (!bd.useMinecartSounds()) {
                            String sound = (preset.equals(PRESET.JUNK_MODE)) ? "junk_land" : "tardis_land";
                            TARDISSounds.playTARDISSound(bd.getLocation(), sound);
                        } else {
                            world.playSound(bd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                        }
                    }
                }
                im = is.getItemMeta();
                im.setCustomModelData(cmd);
                if (bd.shouldAddSign()) {
                    im.setDisplayName(bd.getPlayer().getName() + "'s Police Box");
                }
                is.setItemMeta(im);
                frame.setItem(is);
            } else {
                // remove trackers
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
                plugin.getServer().getScheduler().cancelTask(task);
                task = 0;
                plugin.getTrackerKeeper().getMalfunction().remove(bd.getTardisID());
                if (plugin.getTrackerKeeper().getDidDematToVortex().contains(bd.getTardisID())) {
                    plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(bd.getTardisID()));
                }
                if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisID())) {
                    int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(bd.getTardisID());
                    plugin.getServer().getScheduler().cancelTask(taskID);
                    plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
                }
                if (!bd.isRebuild() && plugin.getTrackerKeeper().getActiveForceFields().containsKey(bd.getPlayer().getPlayer().getUniqueId())) {
                    plugin.getTrackerKeeper().getActiveForceFields().remove(bd.getPlayer().getPlayer().getUniqueId());
                }
                // message travellers in tardis
                if (loops > 3) {
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", bd.getTardisID());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, where, true);
                    if (rst.resultSet()) {
                        List<UUID> travellers = rst.getData();
                        travellers.forEach((s) -> {
                            Player p = plugin.getServer().getPlayer(s);
                            if (p != null) {
                                String message = (bd.isMalfunction()) ? "MALFUNCTION" : "HANDBRAKE_LEFT_CLICK";
                                TARDISMessage.send(p, message);
                                // TARDIS has travelled so add players to list so they can receive Artron on exit
                                if (!plugin.getTrackerKeeper().getHasTravelled().contains(s)) {
                                    plugin.getTrackerKeeper().getHasTravelled().add(s);
                                }
                            }
                        });
                    } else if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(bd.getPlayer().getUniqueId())) {
                        TARDISMessage.send(bd.getPlayer().getPlayer(), "JUNK_HANDBRAKE_LEFT_CLICK");
                    }
                    // restore beacon up block if present
                    HashMap<String, Object> whereb = new HashMap<>();
                    whereb.put("tardis_id", bd.getTardisID());
                    whereb.put("police_box", 2);
                    ResultSetBlocks rs = new ResultSetBlocks(plugin, whereb, false);
                    if (rs.resultSet()) {
                        ReplacedBlock rb = rs.getReplacedBlock();
                        TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData());
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", bd.getTardisID());
                        whered.put("police_box", 2);
                        plugin.getQueryFactory().doDelete("blocks", whered);
                    }
                    // tardis has moved so remove HADS damage count
                    plugin.getTrackerKeeper().getDamage().remove(bd.getTardisID());
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
