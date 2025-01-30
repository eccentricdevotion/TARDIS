package me.eccentric_nz.TARDIS.doors.outer;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOuterPortal;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public class OuterDoor {

    private final TARDIS plugin;
    private final int id;

    public OuterDoor(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
    }

    public Block getMinecraft(ChameleonPreset preset) {
        // get from door record
        ResultSetOuterPortal resultSetPortal = new ResultSetOuterPortal(plugin, id);
        if (resultSetPortal.resultSet()) {
            Location location = resultSetPortal.getLocation();
            return location.getBlock();
        }
        return null;
    }

    public ArmorStand getDisplay() {
        // get from current location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        rsc.resultSet();
        Location location = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        for (Entity e : location.getWorld().getNearbyEntities(location, 1.0d, 1.0d, 1.0d)) {
            if (e instanceof ArmorStand a && ((CraftArmorStand) a).getHandle() instanceof TARDISArmourStand) {
                return a;
            }
        }
        return null;
    }
}
