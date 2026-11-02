package me.eccentric_nz.TARDIS.discovery;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StructureRegistry {

    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;


    public StructureRegistry(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public boolean isOccupied(String world, int chunkX, int chunkZ) {
        PreparedStatement ps = null;
        long key = ChunkKey.of(chunkX, chunkZ);
        try {
            ps = connection.prepareStatement("SELECT 1 FROM " + prefix + "occupied_chunks WHERE world = ? AND chunk_key = ? LIMIT 1 ");
            ps.setString(1, world);
            ps.setLong(2, key);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for occupied_chunks table! " + e.getMessage());
            return true;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing occupied_chunks table! " + e.getMessage());
            }
        }
    }

    public void register(StructureData structure) {
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        try {
            connection.setAutoCommit(false);
            ps1 = connection.prepareStatement("INSERT INTO " + prefix + "structures (structure_id, structure_type, world, origin_chunk_x, origin_chunk_z, y) VALUES (?, ?, ?, ?, ?, ?)");
            ps1.setString(1, structure.id().toString());
            ps1.setString(2, structure.type());
            ps1.setString(3, structure.world());
            ps1.setInt(4, structure.originChunkX());
            ps1.setInt(5, structure.originChunkZ());
            ps1.setInt(6, structure.y());
            ps1.executeUpdate();
            ps2 = connection.prepareStatement("INSERT INTO " + prefix + "occupied_chunks (world, chunk_key, structure_id) VALUES (?, ?, ?)");
            for (long chunk : structure.occupiedChunks()) {
                ps2.setString(1, structure.world());
                ps2.setLong(2, chunk);
                ps2.setString(3, structure.id().toString());
                ps2.addBatch();
            }
            ps2.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            plugin.debug("Insert error for structures and occupied_chunks tables! " + e.getMessage());
        } finally {
            try {
                if (ps1 != null) {
                    ps1.close();
                }
                if (ps2 != null) {
                    ps2.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing structures and occupied_chunks tables! " + e.getMessage());
            }
        }
    }
}
