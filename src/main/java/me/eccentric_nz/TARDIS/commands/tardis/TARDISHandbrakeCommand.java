package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

public class TARDISHandbrakeCommand {

    private final TARDIS plugin;

    public TARDISHandbrakeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(Player player, int id, String[] args, boolean admin) {
        if (args.length < 2) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return true;
        }
        String tf = args[1];
        if (!admin && !tf.equalsIgnoreCase("on") && !tf.equalsIgnoreCase("off")) {
            TARDISMessage.send(player, "PREF_ON_OFF", "the handbrake");
            return true;
        }
        // actually toggle the lever block
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("type", 0);
        whereh.put("tardis_id", id);
        ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
        if (rsc.resultSet()) {
            boolean bool = tf.equalsIgnoreCase("on");
            int onoff = (bool) ? 1 : 0;
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<>();
            set.put("handbrake_on", onoff);
            plugin.getQueryFactory().doUpdate("tardis", set, where);
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            TARDISSounds.playTARDISSound(location, "tardis_handbrake_engage");
            // Changes the lever to on
            Switch lever = (Switch) location.getBlock().getBlockData();
            lever.setPowered(bool);
            location.getBlock().setBlockData(lever);
            if (bool) {
                plugin.getTrackerKeeper().getDestinationVortex().remove(id);
                plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singletonList(id));
                plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singletonList(id));
            }
            if (!admin) {
                TARDISMessage.send(player, "HANDBRAKE_ON_OFF", args[1].toUpperCase(Locale.ENGLISH));
            }
        }
        return true;
    }
}
