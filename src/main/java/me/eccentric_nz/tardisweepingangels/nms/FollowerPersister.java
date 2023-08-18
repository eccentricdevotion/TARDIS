package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FollowerPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final String prefix;
    private PreparedStatement ps = null;
    private int count = 0;

    public FollowerPersister(TARDIS plugin) {
        this.plugin = plugin;
        prefix = this.plugin.getPrefix();
    }

    public void save(TWAFollower follower) {
        try {
            PersistentDataContainer pdc = follower.getBukkitEntity().getPersistentDataContainer();
            String species = "";
            int following = follower.isFollowing() ? 1 : 0;
            String colour = "BLACK";
            int option = 0;
            int ammo = 0;
            if (pdc.has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER)) {
                species = "OOD";
                TWAOod ood = ((TWAOod) follower);
                colour = ood.getColour().toString();
                option = ood.isRedeye() ? 1 : 0;
            }
            if (pdc.has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER)) {
                species = "JUDOON";
                TWAJudoon judoon = ((TWAJudoon) follower);
                ammo = judoon.getAmmo();
                option = judoon.isGuard() ? 1 : 0;
            }
            if (pdc.has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER)) {
                species = "K9";
            }
            // save players who have logged out while using the external camera / or were junk TARDIS travellers
            ps = connection.prepareStatement("INSERT INTO " + prefix + "followers (uuid, owner, species, following, option, colour, ammo) VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, follower.getUUID().toString());
            ps.setString(2, follower.getOwnerUUID() != null ? follower.getOwnerUUID().toString() : TARDISWeepingAngels.UNCLAIMED.toString());
            ps.setString(3, species);
            ps.setInt(4, following);
            ps.setInt(5, option);
            ps.setString(6, colour);
            ps.setInt(7, ammo);
            count += ps.executeUpdate();
            if (count > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Saved " + count + " camera/junk players.");
            }
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
