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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.database.converters;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.commands.remote.TardisRemoteRebuildCommand;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;
import me.eccentric_nz.tardis.planets.TardisBiome;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TardisBiomeConverter {

    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TardisPlugin plugin;
    private final String prefix;

    public TardisBiomeConverter(TardisPlugin plugin) {
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
                            NamespacedKey nsk = NamespacedKey.fromString(rsr.getString("biome"));
                            TardisBiome biome = TardisBiome.get(nsk);
                            if (biome.equals(TardisBiome.DEEP_OCEAN)) {
                                // find the closet biome
                                biome = TardisStaticUtils.getBiomeAt(location.getBlock().getRelative(plugin.getGeneralKeeper().getFaces().get(TardisConstants.RANDOM.nextInt(4)), 6).getLocation());
                            }
                            restoreBiome(location, biome);
                            new TardisRemoteRebuildCommand(plugin).doRemoteRebuild(plugin.getConsole(), id, plugin.getServer().getOfflinePlayer(UUID.fromString(rs.getString("uuid"))), true);
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

    private void restoreBiome(Location l, TardisBiome tardisBiome) {
        Biome biome = null;
        if (tardisBiome != null && tardisBiome.getKey().getNamespace().equalsIgnoreCase("minecraft")) {
            try {
                biome = Biome.valueOf(tardisBiome.name());
            } catch (IllegalArgumentException e) {
                // ignore
            }
        }
        if (l != null && biome != null) {
            int sbx = l.getBlockX();
            int sbz = l.getBlockZ();
            World w = l.getWorld();
            List<Chunk> chunks = new ArrayList<>();
            Chunk chunk = l.getChunk();
            chunks.add(chunk);
            // reset biome and it's not The End
            TardisBiome blockBiome = TardisStaticUtils.getBiomeAt(l);
            if (blockBiome.equals(TardisBiome.DEEP_OCEAN) || blockBiome.equals(TardisBiome.THE_VOID) || (blockBiome.equals(TardisBiome.THE_END) && !Objects.requireNonNull(l.getWorld()).getEnvironment().equals(World.Environment.THE_END))) {
                // reset the biome
                for (int c = -3; c < 4; c++) {
                    for (int r = -3; r < 4; r++) {
                        try {
                            assert w != null;
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
