package me.eccentric_nz.TARDIS.doors.outer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInnerDoorLocations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.portal.Capture;
import me.eccentric_nz.TARDIS.portal.Cast;
import me.eccentric_nz.TARDIS.portal.CastData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class OuterMinecraftDoorOpener {

    private final TARDIS plugin;

    public OuterMinecraftDoorOpener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(Block block, int id, Player player) {
        if (block != null && Tag.DOORS.isTagged(block.getType())) {
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
            // get locations
            // exterior portal (from current location)
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            rsc.resultSet();
            Location portal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            if (preset != null && preset.equals(ChameleonPreset.SWAMP)) {
                portal.add(0.0d, 1.0d, 0.0d);
            }
            // get interior teleport location
            ResultSetInnerDoorLocations resultSetPortal = new ResultSetInnerDoorLocations(plugin, id);
            if (resultSetPortal.resultSet()) {
                Location teleport = resultSetPortal.getTeleportLocation();
                TARDISTeleportLocation tp_in = new TARDISTeleportLocation();
                tp_in.setLocation(teleport);
                tp_in.setTardisId(id);
                tp_in.setDirection(resultSetPortal.getDirection());
                tp_in.setAbandoned(tardis.isAbandoned());
                // create portal
                plugin.getTrackerKeeper().getPortals().put(portal, tp_in);
                // add movers (all companion UUIDs)
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    plugin.getTrackerKeeper().getMovers().add(player.getUniqueId());
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
                if (preset != null && preset.equals(ChameleonPreset.INVISIBLE) && plugin.getConfig().getBoolean("allow.3d_doors")) {
                    // remember door location
                    plugin.getTrackerKeeper().getInvisibleDoors().put(tardis.getUuid(), block);
                }
                if (plugin.getConfig().getBoolean("police_box.view_interior") && preset != null && !preset.usesArmourStand()) {
                    UUID uuid = player.getUniqueId();
                    ConsoleSize consoleSize = tardis.getSchematic().getConsoleSize();
                    plugin.getTrackerKeeper().getCasters().put(uuid, new CastData(resultSetPortal.getDoorLocation(), portal, rsc.getDirection(), tardis.getRotor(), consoleSize));
                    // get distance from door
                    Location location = player.getLocation();
                    double distance = (location.getWorld() == portal.getWorld()) ? location.distanceSquared(portal) : 1; // or exdoor?
                    if (distance <= 9) {
                        // start casting
                        Capture capture = new Capture();
                        BlockData[][][] data = capture.captureInterior(resultSetPortal.getDoorLocation(), (int) distance, tardis.getRotor(), consoleSize);
                        Cast cast = new Cast(plugin, portal);
                        cast.castInterior(uuid, data);
                        if (capture.getRotorData().getFrame() != null) {
                            // get vector of rotor
                            cast.castRotor(capture.getRotorData().getFrame(), player, capture.getRotorData().getOffset(), rsc.getDirection());
                        }
                    }
                }
            }
        }
    }
}
