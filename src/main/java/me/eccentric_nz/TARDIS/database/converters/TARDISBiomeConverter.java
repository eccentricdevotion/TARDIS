/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.converters;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteRebuildCommand;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISBiomeConverter {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;

    public TARDISBiomeConverter(TARDIS plugin) {
        this.plugin = plugin;
        prefix = plugin.getPrefix();
    }

    public void convertBiomes() {
        Statement statement = null;
        String query = "SELECT tardis_id, uuid, chameleon_preset FROM " + prefix + "tardis WHERE chameleon_preset IN ('OLD', 'NEW')";
        PreparedStatement ps = null;
        String prepare = "UPDATE tardis SET chameleon_preset = 'ADAPTIVE', chameleon_demat = 'ADAPTIVE' WHERE tardis_id = ?";
        PreparedStatement rps = null;
        String restore = "SELECT * FROM current WHERE tardis_id = ?";
        ResultSet rs = null;
        ResultSet rsr = null;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            ps = connection.prepareStatement(prepare);
            rps = connection.prepareStatement(restore);
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    int id = rs.getInt("tardis_id");
                    ps.setInt(1, id);
                    ps.executeUpdate();
                    rps.setInt(1, id);
                    rsr = rps.executeQuery();
                    if (rsr.isBeforeFirst()) {
                        World world = plugin.getServer().getWorld(rsr.getString("world"));
                        if (world != null) {
                            Location location = new Location(world, rsr.getInt("x"), rsr.getInt("y"), rsr.getInt("z"));
                            Biome biome = location.getBlock().getBiome();
                            if (biome.equals(Biome.DEEP_OCEAN)) {
                                // find the closet biome
                                biome = location.getBlock().getRelative(plugin.getGeneralKeeper().getFaces().get(TARDISConstants.RANDOM.nextInt(4)), 6).getBiome();
                            }
                            restoreBiome(location, biome);
                            new TARDISRemoteRebuildCommand(plugin).doRemoteRebuild(plugin.getConsole(), id, plugin.getServer().getOfflinePlayer(UUID.fromString(rs.getString("uuid"))), true);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for tardis [chameleon_preset for biome restore] table! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (rsr != null) {
                    rsr.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rps != null) {
                    rps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing tardis [chameleon_preset for biome restore] table! " + e.getMessage());
            }
        }
    }

    private void restoreBiome(Location l, Biome biome) {
        if (l != null && biome != null) {
            int sbx = l.getBlockX();
            int sbz = l.getBlockZ();
            World w = l.getWorld();
            List<Chunk> chunks = new ArrayList<>();
            Chunk chunk = l.getChunk();
            chunks.add(chunk);
            // reset biome and it's not The End
            Biome blockBiome = l.getBlock().getBiome();
            if (blockBiome.equals(Biome.DEEP_OCEAN) || blockBiome.equals(Biome.THE_VOID) || (blockBiome.equals(Biome.THE_END) && !l.getWorld().getEnvironment().equals(World.Environment.THE_END))) {
                // reset the biome
                for (int c = -3; c < 4; c++) {
                    for (int r = -3; r < 4; r++) {
                        try {
                            w.setBiome(sbx + c, sbz + r, biome);
                            Chunk tmp_chunk = w.getChunkAt(new Location(w, sbx + c, 64, sbz + r));
                            if (!chunks.contains(tmp_chunk)) {
                                chunks.add(tmp_chunk);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
