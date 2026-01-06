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
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.exterior.MaterialisationData;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.doors.inner.Inner;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDoor;
import me.eccentric_nz.TARDIS.doors.inner.InnerMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSponge;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemFrame;

import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

/**
 * A police box is a telephone kiosk that can be used by members of the public wishing to get help from the police.
 * Early in the First Doctor's travels, the TARDIS assumed the exterior shape of a police box during a five-month
 * stopover in 1963 London. Due to a malfunction in its chameleon circuit, the TARDIS became locked into that shape.
 *
 * @author eccentric_nz
 */
public class InstantPresetDestroyer {

    private final TARDIS plugin;

    public InstantPresetDestroyer(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Destroys the TARDIS Police Box. A 3 x 3 x 3 block area.
     *
     * @param dd     the MaterialisationData
     * @param hide   boolean determining whether to forget the protected Police Box blocks.
     * @param preset the preset to destroy
     */
    public void instaDestroyPreset(MaterialisationData dd, boolean hide, ChameleonPreset preset) {

        Location l = dd.getLocation();
        COMPASS d = dd.getDirection().forPreset();
        int id = dd.getTardisID();
        boolean sub = dd.isSubmarine();
        // always remove the portal
        plugin.getTrackerKeeper().getPortals().remove(l);
        // get preset
        ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
        if (rs.fromID(id)) {
            UUID playerUUID = dd.getPlayer().getUniqueId();
            // toggle the doors if necessary
            Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
            // close inner
            if (innerDisplayDoor.display()) {
                new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID, true);
            } else {
                new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, playerUUID);
            }
            boolean outerDisplayDoor = rs.getPreset().usesArmourStand();
            // close outer
            if (outerDisplayDoor) {
                new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, playerUUID);
            } else if (rs.getPreset().hasDoor()) {
                new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, playerUUID);
            }
        }
        // remove interaction entity
        Interaction interaction = TARDISDisplayItemUtils.getInteraction(dd.getLocation());
        if (interaction != null) {
            interaction.remove();
        }
        World w = l.getWorld();
        // make sure chunk is loaded
        Chunk chunk = w.getChunkAt(l);
        while (!chunk.isLoaded()) {
            chunk.load();
        }
        if (preset.usesArmourStand()) {
            // remove item frame
            for (Entity e : w.getNearbyEntities(dd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                if (e instanceof ItemFrame frame) {
                    frame.setItem(null, false);
                    frame.remove();
                }
                if (e instanceof ArmorStand stand) {
                    stand.remove();
                }
            }
            if (!hide) {
                plugin.getPresetDestroyer().removeBlockProtection(id);
            }
        } else {
            int sbx = l.getBlockX() - 1;
            int sby;
            if (preset.equals(ChameleonPreset.SUBMERGED)) {
                sby = l.getBlockY() - 1;
            } else {
                sby = l.getBlockY();
            }
            int sbz = l.getBlockZ() - 1;
            // remove problem blocks first
            switch (preset) {
                case GRAVESTONE -> {
                    // remove flower
                    int flowerx;
                    int flowery = (l.getBlockY() + 1);
                    int flowerz;
                    switch (d) {
                        case NORTH -> {
                            flowerx = l.getBlockX();
                            flowerz = l.getBlockZ() + 1;
                        }
                        case WEST -> {
                            flowerx = l.getBlockX() + 1;
                            flowerz = l.getBlockZ();
                        }
                        case SOUTH -> {
                            flowerx = l.getBlockX();
                            flowerz = l.getBlockZ() - 1;
                        }
                        default -> {
                            flowerx = l.getBlockX() - 1;
                            flowerz = l.getBlockZ();
                        }
                    }
                    TARDISBlockSetters.setBlock(w, flowerx, flowery, flowerz, Material.AIR);
                }
                case DUCK -> plugin.getPresetDestroyer().destroyDuckEyes(l, d);
                case MINESHAFT -> plugin.getPresetDestroyer().destroyMineshaftTorches(l, d);
                case LAMP -> plugin.getPresetDestroyer().destroyLampTrapdoors(l, d);
                case JUNK_MODE -> plugin.getPresetDestroyer().destroyHandbrake(l, d);
                case SWAMP -> {
                    int swampYTop = (dd.getLocation().getBlockY() + 2);
                    int swampYBottom = (dd.getLocation().getBlockY() + 1);
                    int swampYUnder = (dd.getLocation().getBlockY());
                    TARDISBlockSetters.setBlock(w, dd.getLocation().getBlockX(), swampYTop, dd.getLocation().getBlockZ(), Material.AIR);
                    TARDISBlockSetters.setBlock(w, dd.getLocation().getBlockX(), swampYBottom, dd.getLocation().getBlockZ(), Material.AIR);
                    TARDISBlockSetters.setBlock(w, dd.getLocation().getBlockX(), swampYUnder, dd.getLocation().getBlockZ(), Material.AIR);
                }
                case PUNKED -> plugin.getPresetDestroyer().destroyPistons(l);
                default -> {
                }
            }
            // remove door
            plugin.getPresetDestroyer().destroyDoor(id);
            // remove torch
            plugin.getPresetDestroyer().destroyLamp(l, preset);
            // remove sign
            plugin.getPresetDestroyer().destroySign(l, d, preset);
            // remove blue wool and door
            for (int yy = 3; yy >= 0; yy--) {
                for (int xx = 0; xx < 3; xx++) {
                    for (int zz = 0; zz < 3; zz++) {
                        Block b = w.getBlockAt((sbx + xx), (sby + yy), (sbz + zz));
                        if (!b.getType().isAir()) {
                            b.setBlockData(TARDISConstants.AIR);
                            if (preset == ChameleonPreset.JUNK_MODE) {
                                TARDISDisplayItemUtils.remove(b);
                            }
                        }
                    }
                }
            }
            l.getChunk().removePluginChunkTicket(plugin);
            if (sub && plugin.isWorldGuardOnServer()) {
                // replace the block under the door if there is one
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                where.put("data", "minecraft:sponge");
                ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, false);
                rsb.resultSetAsync((hasResult, resultSetBlocks) -> {
                    if (hasResult) {
                        if (rsb.getReplacedBlock().getLocation() != null) {
                            Block b = rsb.getReplacedBlock().getLocation().getBlock();
                            TARDISSponge.addWater(b);
                        }
                    }
                });
            }
        }
        // check protected blocks - if block has data stored then put the block back!
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        tid.put("police_box", 1);
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, tid, true);
        if (rsb.resultSet()) {
            rsb.getData().forEach((rb) -> TARDISBlockSetters.setBlock(rb.getLocation(), rb.getBlockData()));
        }
        // if just hiding don't remove block protection
        if (!hide) {
            plugin.getPresetDestroyer().removeBlockProtection(id);
        }
        if (dd.isSiege()) {
            Block siege = dd.getLocation().getBlock();
            siege.setBlockData(TARDISConstants.BARRIER);
            TARDISDisplayItemUtils.remove(siege);
            TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.SIEGE_CUBE, siege, id);
        }
        plugin.getTrackerKeeper().getDematerialising().removeAll(Collections.singleton(id));
        plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(id));
    }
}
