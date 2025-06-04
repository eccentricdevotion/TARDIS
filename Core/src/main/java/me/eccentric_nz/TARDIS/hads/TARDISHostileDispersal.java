/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.hads;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISHADSEvent;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISStainedGlassLookup;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.doors.inner.Inner;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDoor;
import me.eccentric_nz.TARDIS.doors.inner.InnerMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.HADS;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * The Hostile Action Displacement System, or HADS, was one of the defence mechanisms of the Doctor's TARDIS. When the
 * outer shell of the vessel came under attack, the unit dematerialised the TARDIS and re-materialised it a short
 * distance away after the attacker had gone, in a safer locale. The HADS had to be manually set, and the Doctor often
 * forgot to do so.
 *
 * @author eccentric_nz
 */
class TARDISHostileDispersal {

    private final TARDIS plugin;
    private final List<Material> replace_with_barrier;

    TARDISHostileDispersal(TARDIS plugin) {
        this.plugin = plugin;
        replace_with_barrier = buildList();
    }

    void disperseTARDIS(int id, UUID uuid, Player hostile, ChameleonPreset preset) {
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            plugin.debug("Could not get current TARDIS location for HADS!");
            return;
        }
        Current current = rsc.getCurrent();
        if (current.submarine() || preset.usesArmourStand()) {
            // underwater use displacement
            new TARDISHostileDisplacement(plugin).moveTARDIS(id, uuid, hostile, preset);
            return;
        }
        // sound the cloister bell
        TARDISCloisterBell bell = new TARDISCloisterBell(plugin, 3, id, current.location(), plugin.getServer().getPlayer(uuid), "HADS dispersal");
        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
        bell.setTask(taskID);
        plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
        // disperse
        // always remove the portal
        plugin.getTrackerKeeper().getPortals().remove(current.location());
        // toggle the doors if necessary
        // get preset
        ResultSetTardisPreset rs = new ResultSetTardisPreset(plugin);
        if (rs.fromID(id)) {
            // toggle the doors if necessary
            Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
            // close inner
            if (innerDisplayDoor.display()) {
                new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid, true);
            } else {
                new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid);
            }
            boolean outerDisplayDoor = rs.getPreset().usesArmourStand();
            // close outer
            if (outerDisplayDoor) {
                new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, uuid);
            } else if (rs.getPreset().hasDoor()) {
                new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(), id, uuid);
            }
        }
        World w = current.location().getWorld();
        // make sure chunk is loaded
        Chunk chunk = w.getChunkAt(current.location());
        while (!chunk.isLoaded()) {
            chunk.load();
        }
        int sbx = current.location().getBlockX() - 1;
        int sby;
        if (preset.equals(ChameleonPreset.SUBMERGED)) {
            sby = current.location().getBlockY() - 1;
        } else {
            sby = current.location().getBlockY();
        }
        int sbz = current.location().getBlockZ() - 1;
        // remove problem blocks first
        switch (preset) {
            case GRAVESTONE -> {
                // remove flower
                int flowerx;
                int flowery = (current.location().getBlockY() + 1);
                int flowerz;
                switch (current.direction()) {
                    case NORTH -> {
                        flowerx = current.location().getBlockX();
                        flowerz = current.location().getBlockZ() + 1;
                    }
                    case WEST -> {
                        flowerx = current.location().getBlockX() + 1;
                        flowerz = current.location().getBlockZ();
                    }
                    case SOUTH -> {
                        flowerx = current.location().getBlockX();
                        flowerz = current.location().getBlockZ() - 1;
                    }
                    default -> {
                        flowerx = current.location().getBlockX() - 1;
                        flowerz = current.location().getBlockZ();
                    }
                }
                TARDISBlockSetters.setBlock(w, flowerx, flowery, flowerz, Material.AIR);
            }
            case DUCK -> plugin.getPresetDestroyer().destroyDuckEyes(current.location(), current.direction());
            case MINESHAFT -> plugin.getPresetDestroyer().destroyMineshaftTorches(current.location(), current.direction());
            case LAMP -> plugin.getPresetDestroyer().destroyLampTrapdoors(current.location(), current.direction());
            default -> {
            }
        }
        plugin.getTrackerKeeper().getDematerialising().remove(id);
        current.location().getChunk().removePluginChunkTicket(plugin);
        // remove door
        plugin.getPresetDestroyer().destroyDoor(id);
        // remove torch
        plugin.getPresetDestroyer().destroyLamp(current.location(), preset);
        // remove sign
        plugin.getPresetDestroyer().destroySign(current.location(), current.direction(), preset);
        if (preset.equals(ChameleonPreset.JUNK_MODE)) {
            plugin.getPresetDestroyer().destroyHandbrake(current.location(), current.direction());
        }
        // remove blue wool
        List<FallingBlock> falls = new ArrayList<>();
        Material tmp = Material.BLUE_CARPET;
        for (int yy = 0; yy < 4; yy++) {
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    Block b = w.getBlockAt((sbx + xx), (sby + yy), (sbz + zz));
                    if (yy == 0) {
                        Block under = b.getRelative(BlockFace.DOWN);
                        if (replace_with_barrier.contains(under.getType())) {
                            TARDISBlockSetters.setUnderDoorBlock(w, (sbx + xx), (sby + yy) - 1, (sbz + zz), id, false);
                        }
                    }
                    if (!b.getType().isAir()) {
                        float v = (float) -0.5 + (float) (TARDISConstants.RANDOM.nextFloat() * ((0.5 + 0.5) + 1));
                        // get the appropriate carpet colour
                        String stainedGlass = TARDISStainedGlassLookup.stainedGlassFromMaterial(b.getWorld(), b.getType()).toString();
                        String colour = stainedGlass.replace("STAINED_GLASS", "CARPET");
                        Material carpet = Material.valueOf(colour);
                        if (yy == 1 && xx == 0 && zz == 0) {
                            tmp = carpet;
                        }
                        FallingBlock fb = w.spawnFallingBlock(b.getLocation(), carpet.createBlockData());
                        falls.add(fb);
                        fb.setDropItem(false);
                        fb.setVelocity(new Vector(v, v, v));
                        b.setBlockData(TARDISConstants.AIR);
                    }
                }
            }
        }
        BlockData carpetBlockData = tmp.createBlockData();
        // schedule task to remove fallen blocks
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> falls.forEach((f) -> {
            f.getLocation().getBlock().setBlockData(TARDISConstants.AIR);
            f.remove();
        }), 10L);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // set carpet base at location
            for (int xx = 0; xx < 3; xx++) {
                for (int zz = 0; zz < 3; zz++) {
                    Block block = w.getBlockAt((sbx + xx), (sby), (sbz + zz));
                    block.setBlockData(carpetBlockData);
                }
            }
        }, 15L);
        plugin.getTrackerKeeper().getDispersed().put(uuid, current.location());
        plugin.getTrackerKeeper().getDispersedTARDII().add(id);
        plugin.getPM().callEvent(new TARDISHADSEvent(hostile, id, current.location(), HADS.DISPERSAL));
    }

    private List<Material> buildList() {
        List<Material> list = new ArrayList<>();
        plugin.getBlocksConfig().getStringList("under_door_blocks").forEach((str) -> list.add(Material.valueOf(str)));
        return list;
    }
}
