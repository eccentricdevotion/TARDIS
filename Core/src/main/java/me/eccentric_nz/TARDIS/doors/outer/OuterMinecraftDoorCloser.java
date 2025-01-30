package me.eccentric_nz.TARDIS.doors.outer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
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

public class OuterMinecraftDoorCloser {

    private final TARDIS plugin;

    public OuterMinecraftDoorCloser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void close(Block block, int id, UUID uuid) {
        if (block != null && Tag.DOORS.isTagged(block.getType())) {
            Openable closeable = (Openable) block.getBlockData();
            closeable.setOpen(false);
            block.setBlockData(closeable, true);
        }
        // hide the interior if it is being cast
        if (plugin.getConfig().getBoolean("police_box.view_interior")) {
            plugin.getTrackerKeeper().getCasters().remove(uuid);
            // remove fake blocks
            if (plugin.getTrackerKeeper().getCastRestore().containsKey(uuid)) {
                for (Block b : plugin.getTrackerKeeper().getCastRestore().get(uuid)) {
                    b.getState().update();
                }
                plugin.getTrackerKeeper().getCastRestore().remove(uuid);
                // remove fake item frame if necessary
                if (plugin.getTrackerKeeper().getRotorRestore().containsKey(uuid)) {
                    int rotorId = plugin.getTrackerKeeper().getRotorRestore().get(uuid);
                    plugin.getTardisHelper().removeFakeItemFrame(rotorId, Bukkit.getPlayer(uuid));
                    plugin.getTrackerKeeper().getRotorRestore().remove(uuid);
                }
            }
        }
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                // get exterior portal (from current location)
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                rsc.resultSet();
                Location portal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                if (tardis.getPreset().equals(ChameleonPreset.SWAMP)) {
                    portal.add(0.0d, 1.0d, 0.0d);
                }
                // remove exterior portal
                TARDISTeleportLocation removed = plugin.getTrackerKeeper().getPortals().remove(portal);
                if (removed == null) {
                    DoorUtility.debugPortal(portal.toString());
                }
                // remove movers
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            plugin.getTrackerKeeper().getMovers().remove(p.getUniqueId());
                        }
                    } else {
                        String[] companions = tardis.getCompanions().split(":");
                        for (String c : companions) {
                            if (!c.isEmpty()) {
                                plugin.getTrackerKeeper().getMovers().remove(UUID.fromString(c));
                            }
                        }
                        plugin.getTrackerKeeper().getMovers().remove(uuid);
                    }
                }
            }
        }
    }
}
