package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetThrottle;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ComehereAction {

    private final TARDIS plugin;

    public ComehereAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void doTravel(ComehereRequest request) {
        // get players
        Player acceptor = plugin.getServer().getPlayer(request.getAccepter());
        Player requester = plugin.getServer().getPlayer(request.getRequester());
        boolean hidden = request.isHidden();
        // get space time throttle
        SpaceTimeThrottle spaceTimeThrottle = new ResultSetThrottle(plugin).getSpeed(request.getAccepter().toString());
        int ch = Math.round(plugin.getArtronConfig().getInt("comehere") * spaceTimeThrottle.getArtronMultiplier());
        if (request.getLevel() < ch) {
            TARDISMessage.send(acceptor, "NOT_ENOUGH_ENERGY");
            TARDISMessage.send(requester, "NOT_ENOUGH_ENERGY");
            return;
        }
        World w = request.getCurrent().getWorld();
        Location oldSave = null;
        HashMap<String, Object> bid = new HashMap<>();
        bid.put("tardis_id", request.getId());
        HashMap<String, Object> bset = new HashMap<>();
        if (w != null) {
            oldSave = new Location(w, request.getCurrent().getX(), request.getCurrent().getY(), request.getCurrent().getZ());
            // set fast return location
            bset.put("world", request.getCurrent().getWorld().getName());
            bset.put("x", request.getCurrent().getX());
            bset.put("y", request.getCurrent().getY());
            bset.put("z", request.getCurrent().getZ());
            bset.put("direction", request.getCurrentDirection().toString());
            bset.put("submarine", request.isSubmarine());
        } else {
            hidden = true;
            // set fast return location
            bset.put("world", request.getDestination().getWorld().getName());
            bset.put("x", request.getDestination().getX());
            bset.put("y", request.getDestination().getY());
            bset.put("z", request.getDestination().getZ());
            bset.put("submarine", (request.isSubmarine()) ? 1 : 0);
        }
        plugin.getQueryFactory().doUpdate("back", bset, bid);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", request.getId());
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", request.getDestination().getWorld().getName());
        set.put("x", request.getDestination().getBlockX());
        set.put("y", request.getDestination().getBlockY());
        set.put("z", request.getDestination().getBlockZ());
        set.put("direction", request.getDestinationDirection().toString());
        set.put("submarine", (request.isSubmarine()) ? 1 : 0);
        if (hidden) {
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("hidden", 0);
            HashMap<String, Object> ttid = new HashMap<>();
            ttid.put("tardis_id", request.getId());
            plugin.getQueryFactory().doUpdate("tardis", sett, ttid);
        }
        plugin.getQueryFactory().doUpdate("current", set, tid);
        TARDISMessage.send(requester, "TARDIS_COMING");
        long delay = 1L;
        plugin.getTrackerKeeper().getInVortex().add(request.getId());
        boolean hid = request.isHidden();
        if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(request.getId())) {
            DestroyData dd = new DestroyData();
            dd.setDirection(request.getCurrentDirection());
            dd.setLocation(oldSave);
            dd.setPlayer(acceptor);
            dd.setHide(false);
            dd.setOutside(true);
            dd.setSubmarine(request.isSubmarine());
            dd.setTardisID(request.getId());
            dd.setThrottle(spaceTimeThrottle);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (!hid) {
                    plugin.getTrackerKeeper().getDematerialising().add(request.getId());
                    plugin.getPresetDestroyer().destroyPreset(dd);
                } else {
                    plugin.getPresetDestroyer().removeBlockProtection(request.getId());
                }
            }, delay);
        }
        BuildData bd = new BuildData(request.getAccepter().toString());
        bd.setDirection(request.getDestinationDirection());
        bd.setLocation(request.getDestination());
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setPlayer(acceptor);
        bd.setRebuild(false);
        bd.setSubmarine(request.isSubmarine());
        bd.setTardisID(request.getId());
        bd.setThrottle(spaceTimeThrottle);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), delay * 2);
        // remove energy from TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", request.getId());
        plugin.getQueryFactory().alterEnergyLevel("tardis", -ch, wheret, acceptor);
        plugin.getTrackerKeeper().getHasDestination().remove(request.getId());
        plugin.getTrackerKeeper().getRescue().remove(request.getId());
    }
}
