/*
 * Copyright (C) 2013 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDirectionCommand {

    private final TARDIS plugin;

    public TARDISDirectionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean changeDirection(final Player player, String[] args) {
        if (player.hasPermission("tardis.timetravel")) {
            if (args.length < 2 || (!args[1].equalsIgnoreCase("north") && !args[1].equalsIgnoreCase("west") && !args[1].equalsIgnoreCase("south") && !args[1].equalsIgnoreCase("east"))) {
                player.sendMessage(plugin.pluginName + "You need to specify the compass direction e.g. north, west, south or east!");
                return false;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.pluginName + MESSAGE.NO_TARDIS.getText());
                return false;
            }
            final int id = rs.getTardis_id();
            TARDISCircuitChecker tcc = null;
            if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMaterialisation()) {
                player.sendMessage(plugin.pluginName + MESSAGE.NO_MAT_CIRCUIT.getText());
                return true;
            }
            int level = rs.getArtron_level();
            int amount = plugin.getArtronConfig().getInt("random");
            if (level < amount) {
                player.sendMessage(plugin.pluginName + "The TARDIS does not have enough Artron Energy to change the Police Box direction!");
                return true;
            }
            if (plugin.inVortex.contains(Integer.valueOf(id))) {
                player.sendMessage(plugin.pluginName + MESSAGE.NOT_WHILE_MAT.getText());
                return true;
            }
            boolean tmp_cham = false;
            if (plugin.getConfig().getBoolean("travel.chameleon")) {
                tmp_cham = rs.isChamele_on();
            }
            final boolean cham = tmp_cham;
            boolean hid = rs.isHidden();
            PRESET demat = rs.getDemat();
            String dir = args[1].toUpperCase(Locale.ENGLISH);
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            final ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                player.sendMessage(plugin.pluginName + MESSAGE.NO_CURRENT.getText());
                return true;
            }
            COMPASS old_d = rsc.getDirection();
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> tid = new HashMap<String, Object>();
            HashMap<String, Object> set = new HashMap<String, Object>();
            tid.put("tardis_id", id);
            set.put("direction", dir);
            qf.doUpdate("current", set, tid);
            HashMap<String, Object> did = new HashMap<String, Object>();
            HashMap<String, Object> setd = new HashMap<String, Object>();
            did.put("door_type", 0);
            did.put("tardis_id", id);
            setd.put("door_direction", dir);
            qf.doUpdate("doors", setd, did);
            final Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            final COMPASS d = COMPASS.valueOf(dir);
            // destroy sign
            if (!hid) {
                if (demat.equals(PRESET.DUCK)) {
                    plugin.destroyerP.destroyDuckEyes(l, old_d);
                }
                if (demat.equals(PRESET.MINESHAFT)) {
                    plugin.destroyerP.destroyMineshaftTorches(l, old_d);
                }
                plugin.destroyerP.destroyDoor(id);
                plugin.destroyerP.destroySign(l, old_d, demat);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.builderP.buildPreset(id, l, d, cham, player, true, false, rsc.isSubmarine());
                    }
                }, 10L);
            }
            HashMap<String, Object> wherea = new HashMap<String, Object>();
            wherea.put("tardis_id", id);
            qf.alterEnergyLevel("tardis", -amount, wherea, player);
            new TARDISArtronIndicator(plugin).showArtronLevel(player, id, true, amount);
            if (hid) {
                player.sendMessage(plugin.pluginName + "Direction changed.");
            }
            return true;
        } else {
            player.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }
}
