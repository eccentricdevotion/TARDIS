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
package me.eccentric_nz.TARDIS.ARS;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorage;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.RoomCleaner;
import me.eccentric_nz.tardischunkgenerator.custombiome.BiomeHelper;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;

import java.util.HashMap;

/**
 * Performs Architectural Reconfiguration System room jettisons.
 *
 * @author eccentric_nz
 */
class TARDISARSJettisonRunnable implements Runnable {

    private final TARDIS plugin;
    private final TARDISARSJettison slot;
    private final ARS room;
    private final int id;
    private final Player player;

    TARDISARSJettisonRunnable(TARDIS plugin, TARDISARSJettison slot, ARS room, int id, Player player) {
        this.plugin = plugin;
        this.slot = slot;
        this.room = room;
        this.id = id;
        this.player = player;
    }

    @Override
    public void run() {
        String r = room.getConfigPath();
        // remove the room
        World world = slot.getChunk().getWorld();
        int x = slot.getX();
        int y = slot.getY();
        int z = slot.getZ();
        for (int yy = y; yy < (y + 16); yy++) {
            for (int xx = x; xx < (x + 16); xx++) {
                for (int zz = z; zz < (z + 16); zz++) {
                    Block b = world.getBlockAt(xx, yy, zz);
                    // remove display items & item frames
                    TARDISDisplayItemUtils.remove(b);
                    // if it is a GRAVITY or ANTIGRAVITY well remove it from the database
                    if ((r.equals("GRAVITY") || r.equals("ANTIGRAVITY")) && (b.getType().equals(Material.LIME_WOOL) || b.getType().equals(Material.PINK_WOOL))) {
                        String l = new Location(world, xx, yy, zz).toString();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("location", l);
                        where.put("tardis_id", id);
                        plugin.getQueryFactory().doDelete("gravity_well", where);
                        // remove trackers
                        if (b.getType().equals(Material.LIME_WOOL)) {
                            plugin.getGeneralKeeper().getGravityUpList().remove(l);
                        } else {
                            plugin.getGeneralKeeper().getGravityDownList().remove(l);
                        }
                    }
                    if (r.equals("EYE")) {
                        // reset biome
                        world.setBiome(xx, yy, zz, Biome.THE_VOID);
                    }
                    BlockState state = b.getState();
                    if (state instanceof BlockState) {
                        plugin.getTardisHelper().removeTileEntity(state);
                    }
                    b.setBlockData(TARDISConstants.AIR);
                }
            }
        }
        if (r.equals("EYE")) {
            // refresh chunk
            BiomeHelper.refreshChunk(slot.getChunk());
            // get current task and abort it
            ResultSetArtronStorage rsa = new ResultSetArtronStorage(plugin);
            if (rsa.fromID(id)) {
                // abort
                plugin.getServer().getScheduler().cancelTask(rsa.getTask());
                // update eyes record
                HashMap<String, Object> set = new HashMap<>();
                set.put("task", -1);
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                plugin.getQueryFactory().doSyncUpdate("eyes", set, where);
            }
        }
        if (r.equals("LIBRARY")) {
            // remove entities
            Chunk chunk = slot.getChunk();
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof ItemDisplay || entity instanceof Interaction || entity instanceof TextDisplay) {
                    entity.remove();
                }
            }
        }
        // give them their energy!
        if (room != TARDISARS.SLOT) {
            int amount = Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + r + ".cost"));
            if (r.equals("GRAVITY") || r.equals("ANTIGRAVITY")) {
                // halve it because they have to jettison top and bottom
                amount /= 2;
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", amount, set, null);
            if (player.isOnline()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_RECOVERED", String.format("%d", amount));
            }
            new RoomCleaner(plugin).removeRecords(r, id, world, player);
        }
    }
}
