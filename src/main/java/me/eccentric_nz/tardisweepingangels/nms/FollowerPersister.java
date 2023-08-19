package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.persistence.PersistentDataContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class FollowerPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;

    public FollowerPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save(TWAFollower follower) {
        try {
            PersistentDataContainer pdc = follower.getBukkitEntity().getPersistentDataContainer();
            String uuid = follower.getUUID().toString();
            String owner = follower.getOwnerUUID() != null ? follower.getOwnerUUID().toString() : TARDISWeepingAngels.UNCLAIMED.toString();
            String species = "";
            int following = follower.isFollowing() ? 1 : 0;
            String colour = "BLACK";
            int option = 0;
            int ammo = 0;
            if (pdc.has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                species = "OOD";
                TWAOod ood = (TWAOod) follower;
                colour = ood.getColour().toString();
                option = ood.isRedeye() ? 1 : 0;
            }
            if (pdc.has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                species = "JUDOON";
                TWAJudoon judoon = (TWAJudoon) follower;
                ammo = judoon.getAmmo();
                option = judoon.isGuard() ? 1 : 0;
            }
            if (pdc.has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                species = "K9";
            }
            // save custom follower
            ps = connection.prepareStatement("REPLACE INTO " + prefix + "followers (uuid, owner, species, following, option, colour, ammo) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, uuid);
            ps.setString(2, owner);
            ps.setString(3, species);
            ps.setInt(4, following);
            ps.setInt(5, option);
            ps.setString(6, colour);
            ps.setInt(7, ammo);
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("Insert error for follower persistence: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing follower persistence statement: " + ex.getMessage());
            }
        }
    }

    public void save(Follower follower, UUID uuid) {
        try {
            // save custom follower
            ps = connection.prepareStatement("INSERT INTO " + prefix + "followers (uuid, owner, species, following, option, colour, ammo) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, follower.getOwner().toString());
            ps.setString(3, follower.getSpecies().toString());
            ps.setInt(4, follower.isFollowing()? 1: 0);
            ps.setInt(5, follower.hasOption()? 1: 0);
            ps.setString(6, follower.getColour().toString());
            ps.setInt(7, follower.getAmmo());
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("Insert error for follower persistence: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing follower persistence statement: " + ex.getMessage());
            }
        }
    }
}
