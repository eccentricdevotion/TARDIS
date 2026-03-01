package me.eccentric_nz.TARDIS.commands.remote;

import io.papermc.paper.math.BlockPosition;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.CircuitChecker;
import me.eccentric_nz.TARDIS.advanced.CircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class RemoteUtility {

    public static boolean check(TARDIS plugin, CommandSender sender, int id) {
        if (sender instanceof Player player && !sender.hasPermission("tardis.admin")) {
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
            if (!rst.resultSet()) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return true;
            }
            Tardis t = rst.getTardis();
            int tardis_id = t.getTardisId();
            if (tardis_id != id) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ONLY_TL_REMOTE");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !t.isPoweredOn()) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "POWER_DOWN");
                return true;
            }
            // must have circuits
            CircuitChecker tcc = new CircuitChecker(plugin, id);
            tcc.getCircuits();
            if (plugin.getConfig().getBoolean("difficulty.circuits") && !tcc.hasMaterialisation()) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                return true;
            }
            // damage circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                // decrement uses
                int uses_left = tcc.getMaterialisationUses();
                new CircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
            }
        }
        return false;
    }

    public static boolean travelCheck(TARDIS plugin, CommandSender sender, boolean handbrake, int level) {
        // already travelling
        if (!handbrake) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
            return true;
        }
        // check artron energy if not admin
        if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
            int travel = plugin.getArtronConfig().getInt("travel");
            if (level < travel) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                return true;
            }
        }
        return false;
    }

    public static void home(TARDIS plugin, int id, CommandSender sender, UUID uuid) {
        // get home location
        HashMap<String, Object> whereHome = new HashMap<>();
        whereHome.put("tardis_id", id);
        ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereHome);
        if (!rsh.resultSet()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "HOME_NOT_FOUND");
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", rsh.getWorld().getName());
        set.put("x", rsh.getX());
        set.put("y", rsh.getY());
        set.put("z", rsh.getZ());
        set.put("direction", rsh.getDirection().toString());
        set.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
        if (!rsh.getPreset().isEmpty()) {
            // set the chameleon preset
            HashMap<String, Object> whereId = new HashMap<>();
            whereId.put("tardis_id", id);
            HashMap<String, Object> setPreset = new HashMap<>();
            setPreset.put("chameleon_preset", rsh.getPreset());
            // set chameleon adaption to OFF
            setPreset.put("adapti_on", 0);
            plugin.getQueryFactory().doSyncUpdate("tardis", setPreset, whereId);
        }
        HashMap<String, Object> whereId = new HashMap<>();
        whereId.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("next", set, whereId);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
            String success = (new TravelCommand(plugin).doTravel(id, offlinePlayer, sender)) ? plugin.getLanguage().getString("SUCCESS_Y") : plugin.getLanguage().getString("SUCCESS_N");
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "REMOTE_SUCCESS", success);
        }, 5L);
    }

    public static void area(TARDIS plugin, CommandSender sender, String area, OfflinePlayer p, int id, boolean invisible) {
        // check area name
        HashMap<String, Object> whereArea = new HashMap<>();
        whereArea.put("area_name", area);
        ResultSetAreas rsa = new ResultSetAreas(plugin, whereArea, false, false);
        if (!rsa.resultSet()) {
            plugin.getMessenger().sendColouredCommand(sender, "AREA_NOT_FOUND", "/tardis list areas", plugin);
            return;
        }
        if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
            // must use advanced console if difficulty hard
            if (plugin.getConfig().getBoolean("difficulty.disks")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "ADV_AREA");
                return;
            }
            // check permission
            String perm = "tardis.area." + area;
            if ((!TARDISPermission.hasPermission(p, perm) && !TARDISPermission.hasPermission(p, "tardis.area.*"))) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TRAVEL_NO_AREA_PERM", area);
                return;
            }
        }
        // check whether this is a no invisibility area
        String invisibility = rsa.getArea().invisibility();
        if (invisibility.equals("DENY") && invisible) {
            // check preset
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "AREA_NO_INVISIBLE");
            return;
        } else if (!invisibility.equals("ALLOW")) {
            // force preset
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "AREA_FORCE_PRESET", invisibility);
            HashMap<String, Object> whereId = new HashMap<>();
            whereId.put("tardis_id", id);
            HashMap<String, Object> setPreset = new HashMap<>();
            setPreset.put("chameleon_preset", invisibility);
            // set chameleon adaption to OFF
            setPreset.put("adapti_on", 0);
            plugin.getQueryFactory().doSyncUpdate("tardis", setPreset, whereId);
        }
        // get a landing spot
        Location l;
        if (rsa.getArea().grid()) {
            l = plugin.getTardisArea().getNextSpot(rsa.getArea().areaName());
        } else {
            l = plugin.getTardisArea().getSemiRandomLocation(rsa.getArea().areaId());
        }
        // returns null if full!
        if (l == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_MORE_SPOTS");
            return;
        }
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", l.getWorld().getName());
        set.put("x", l.getBlockX());
        set.put("y", l.getBlockY());
        set.put("z", l.getBlockZ());
        // set the direction of the TARDIS
        if (!rsa.getArea().direction().isEmpty()) {
            set.put("direction", rsa.getArea().direction());
        } else {
            // get current direction
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
                return;
            }
            set.put("direction", rsc.getCurrent().direction().toString());
        }
        set.put("submarine", 0);
        HashMap<String, Object> whereId = new HashMap<>();
        whereId.put("tardis_id", id);
        plugin.getQueryFactory().doUpdate("next", set, whereId);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            String success = (new TravelCommand(plugin).doTravel(id, p, sender)) ? plugin.getLanguage().getString("SUCCESS_Y") : plugin.getLanguage().getString("SUCCESS_N");
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "REMOTE_SUCCESS", success);
        }, 5L);

    }

    public static void coordinates(TARDIS plugin, CommandSender sender, World w, BlockPosition pos, OfflinePlayer p, int id) {
        if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
            if (!TARDISPermission.hasPermission(p, "tardis.timetravel.location")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                return;
            }
        }
        int x, y, z;
        if (w == null) {
            plugin.getMessenger().sendColouredCommand(sender, "WORLD_NOT_FOUND", "/tardisworld", plugin);
            return;
        }
        if (!plugin.getPlanetsConfig().getBoolean("planets." + w.getName() + ".time_travel")) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return;
        }
        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && w.getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return;
        }
        x = pos.blockX();
        y = pos.blockY();
        if (y < -64 || ((w.getEnvironment().equals(World.Environment.NORMAL) && y > 310) || (!w.getEnvironment().equals(World.Environment.NORMAL) && y > 240))) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "Y_NOT_VALID");
            return;
        }
        z = pos.blockZ();
        Location location = new Location(w, x, y, z);
        // check location
        if (plugin.getTardisArea().isInExistingArea(location)) {
            plugin.getMessenger().sendColouredCommand(sender, "TRAVEL_IN_AREA", "/tardisremote [player] travel area [area name]", plugin);
            return;
        }
        // check respect if not admin
        if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
            if (!plugin.getPluginRespect().getRespect(location, new Parameters(p.getPlayer(), Flag.getDefaultFlags()))) {
                return;
            }
        }
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
            return;
        }
        // check location
        int[] start_loc = TARDISTimeTravel.getStartLocation(location, rsc.getCurrent().direction());
        int count = TARDISTimeTravel.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getCurrent().direction());
        if (count > 0) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_SAFE");
        } else {
            HashMap<String, Object> set = new HashMap<>();
            set.put("world", location.getWorld().getName());
            set.put("x", location.getBlockX());
            set.put("y", location.getBlockY());
            set.put("z", location.getBlockZ());
            set.put("submarine", 0);
            HashMap<String, Object> whereId = new HashMap<>();
            whereId.put("tardis_id", id);
            plugin.getQueryFactory().doUpdate("next", set, whereId);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                String success = (new TravelCommand(plugin).doTravel(id, p, sender)) ? plugin.getLanguage().getString("SUCCESS_Y") : plugin.getLanguage().getString("SUCCESS_N");
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "REMOTE_SUCCESS", success);
            }, 5L);


        }
    }
}
