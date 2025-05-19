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
package me.eccentric_nz.TARDIS.builders.interior;

import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTIPS;
import me.eccentric_nz.TARDIS.desktop.TARDISChunkUtils;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISInteriorPostioning {

    private final TARDIS plugin;

    public TARDISInteriorPostioning(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Get the TIPS slot from a player location
     *
     * @param location the player's current location in the TARDIS world
     * @return the TIPS slot number
     */
    public static int getTIPSSlot(Location location) {
        int px = location.getBlockX();
        int pz = location.getBlockZ();
        int col = px / 1024;
        int row = pz / 1024;
        return (col * 20) + row;
    }

    /**
     * Gets the next unused TIPS slot in a 20 x 20 grid.
     *
     * @return the first vacant slot
     */
    public int getFreeSlot() {
        int limit = plugin.getConfig().getInt("creation.tips_limit");
        int slot = -1;
        if (plugin.getConfig().getString("creation.tips_next", "HIGHEST").equalsIgnoreCase("FREE")) {
            for (int i = 0; i < limit; i++) {
                if (!TARDISBuilderInstanceKeeper.getTipsSlots().contains(i)) {
                    slot = i;
                    break;
                }
            }
        } else {
            // HIGHEST
            slot = new ResultSetTIPS(plugin).getHightestSlot() + 1;
        }
        return slot;
    }

    /**
     * Calculate the position data for a TIPS slot.
     *
     * @param slot the slot position in the grid (a number between 0, 399 inclusive)
     * @return a TIPS Data container
     */
    public TARDISTIPSData getTIPSData(int slot) {
        TARDISTIPSData data = new TARDISTIPSData();
        int factorX = 0;
        int factorZ = 0;
        int subtract = 0;
        if (slot > 399 && slot < 800) {
            factorX = 20480;
            subtract = 400;
        }
        if (slot > 799 && slot < 1200) {
            factorZ = 20480;
            subtract = 800;
        }
        if (slot > 1199 && slot < 1600) {
            factorX = 20480;
            factorZ = 20480;
            subtract = 1200;
        }
        int row = (slot - subtract) / 20;
        int col = (slot - subtract) % 20;
        data.setMinX((row * 1024) + factorX);
        data.setCentreX((row * 1024 + 496) + factorX);
        data.setMaxX((row * 1024 + 1023) + factorX);
        data.setMinZ((col * 1024) + factorZ);
        data.setCentreZ((col * 1024 + 496) + factorZ);
        data.setMaxZ((col * 1024 + 1023) + factorZ);
        data.setSlot(slot);
        return data;
    }

    /**
     * Calculate the position data for the Junk TARDIS TIPS slot.
     *
     * @return a TIPS Data container
     */
    public TARDISTIPSData getTIPSJunkData(int slot) {
        if (slot == -999) {
            // remove division preview record
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", -21);
            plugin.getQueryFactory().doDelete("transmats", where);
            return getTIPSData(-21);
        } else {
            return getTIPSData(-99);
        }
    }

    // won't remove manually grown rooms...
    public void reclaimChunks(World w, int id, Schematic s) {
        // get ARS data
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            // get the exit location
            TARDISDoorLocation dl = TARDISDoorListener.getDoor(0, id);
            Location exitLocation = dl.getL();
            String[][][] json = TARDISARSMethods.getGridFromJSON(rs.getJson());
            Chunk c = plugin.getLocationUtils().getTARDISChunk(id);
            List<Chunk> chunks = TARDISChunkUtils.getConsoleChunks(c, s);
            chunks.forEach((u) -> {
                // exit players & remove items
                for (Entity e : u.getEntities()) {
                    removeEntity(e, exitLocation);
                }
                if (s.getPermission().equals("mechanical") || s.getPermission().equals("cursed")) {
                    // remove lower console blocks
                    int cx = u.getX() * 16;
                    int cz = u.getZ() * 16;
                    for (int y = 62; y < 64; y++) {
                        for (int col = cx; col < cx + 16; col++) {
                            for (int row = cz; row < cz + 16; row++) {
                                w.getBlockAt(row, y, col).setBlockData(TARDISConstants.AIR);
                            }
                        }
                    }
                }
            });
            for (int l = 0; l < 3; l++) {
                for (int x = 0; x < 9; x++) {
                    for (int z = 0; z < 9; z++) {
                        if (!json[l][x][z].equalsIgnoreCase("STONE")) {
                            // get ARS slot
                            TARDISARSSlot slot = new TARDISARSSlot();
                            slot.setChunk(c);
                            slot.setY(l);
                            slot.setX(x);
                            slot.setZ(z);
                            Chunk tipsChunk = w.getBlockAt(slot.getX(), slot.getY(), slot.getZ()).getChunk();
                            // remove mobs
                            for (Entity e : tipsChunk.getEntities()) {
                                removeEntity(e, exitLocation);
                            }
                            for (int y = 0; y < 16; y++) {
                                for (int col = 0; col < 16; col++) {
                                    for (int row = 0; row < 16; row++) {
                                        w.getBlockAt(slot.getX() + row, slot.getY() + y, slot.getZ() + col).setBlockData(TARDISConstants.AIR);
                                    }
                                }
                            }
                            // remove dropped items
                            for (Entity e : tipsChunk.getEntities()) {
                                e.remove();
                            }
                        }
                    }
                }
            }
            chunks.forEach((u) -> {
                // remove dropped items
                for (Entity e : u.getEntities()) {
                    e.remove();
                }
            });
        }
    }

    private void removeEntity(Entity entity, Location exit) {
        if (entity instanceof Player p) {
            // teleport player and remove from travellers table
            teleportPlayerToExit(p, exit);
        } else {
            entity.remove();
        }
    }

    private void teleportPlayerToExit(Player player, Location exit) {
        plugin.getGeneralKeeper().getDoorListener().movePlayer(player, exit, true, player.getWorld(), false, 0, true, true);
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        plugin.getQueryFactory().doDelete("travellers", wheret);
    }

    public void reclaimZeroChunk(World w, TARDISTIPSData data) {
        // get starting chunk
        Location l = new Location(w, data.getMinX(), 0, data.getMinZ());
        Chunk chunk = w.getChunkAt(l);
        Block block = chunk.getBlock(0, 0, 0);
        int sx = block.getX();
        int sz = block.getZ();
        for (int y = 64; y < 80; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    int cx = sx + x;
                    int cz = sz + z;
                    w.getBlockAt(cx, y, cz).setBlockData(TARDISConstants.AIR);
                }
            }
        }
    }
}
