package me.eccentric_nz.TARDIS.doors.inner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOuterPortal;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class InnerMinecraftDoorOpener {

    private final TARDIS plugin;

    public InnerMinecraftDoorOpener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(Block block, int id, UUID uuid) {
        // open door
        if (Tag.DOORS.isTagged(block.getType())) {
            Openable openable = (Openable) block.getBlockData();
            openable.setOpen(true);
            block.setBlockData(openable, true);
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            ChameleonPreset preset = tardis.getPreset();
            // get portal location
            Location portal = block.getLocation();
            // get tp location
            ResultSetOuterPortal resultSetPortal = new ResultSetOuterPortal(plugin, id);
            if (resultSetPortal.resultSet()) {
                Location teleport = resultSetPortal.getLocation().clone();
                // adjust for teleport
                if (preset.usesArmourStand()) {
                    switch (resultSetPortal.getDirection()) {
                        case NORTH_EAST -> teleport.add(0, 0, 1);
                        case NORTH -> teleport.add(0.5d, 0.0d, 1.0d);
                        case NORTH_WEST -> teleport.add(1, 0, 1);
                        case WEST -> teleport.add(1.0d, 0.0d, 0.5d);
                        case SOUTH_WEST -> teleport.add(1, 0, -0.5);
                        case SOUTH -> teleport.add(0.5d, 0.0d, -1.0d);
                        case SOUTH_EAST -> teleport.add(-0.5, 0, 0);
                        default -> teleport.add(-1.0d, 0.0d, 0.5d);
                    }
                } else {
                    teleport.setX(teleport.getX() + 0.5);
                    teleport.setZ(teleport.getZ() + 0.5);
                }
                TARDISTeleportLocation tp_out = new TARDISTeleportLocation();
                tp_out.setLocation(teleport);
                tp_out.setTardisId(id);
                tp_out.setDirection(resultSetPortal.getDirection());
                tp_out.setAbandoned(tardis.isAbandoned());
                // create portal
                plugin.getTrackerKeeper().getPortals().put(portal, tp_out);
                // add movers
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    // add movers (all companion UUIDs)
                    plugin.getTrackerKeeper().getMovers().add(uuid);
                    if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            plugin.getTrackerKeeper().getMovers().add(p.getUniqueId());
                        }
                    } else {
                        String[] companions = tardis.getCompanions().split(":");
                        for (String c : companions) {
                            if (!c.isEmpty()) {
                                plugin.getTrackerKeeper().getMovers().add(UUID.fromString(c));
                            }
                        }
                    }
                }
            }
        }
    }
}
