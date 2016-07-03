/*
 * Copyright (C) 2016 eccentric_nz
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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
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
                TARDISMessage.send(player, "DIRECTION_NEED");
                return false;
            }
            UUID uuid = player.getUniqueId();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return false;
            }
            Tardis tardis = rs.getTardis();
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                TARDISMessage.send(player, "POWER_DOWN");
                return true;
            }
            int id = tardis.getTardis_id();
            TARDISCircuitChecker tcc = null;
            if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMaterialisation()) {
                TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                return true;
            }
            int level = tardis.getArtron_level();
            int amount = plugin.getArtronConfig().getInt("random");
            if (level < amount) {
                TARDISMessage.send(player, "ENERGY_NO_DIRECTION");
                return true;
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                TARDISMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
                return true;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
                TARDISMessage.send(player, "NOT_WHILE_MAT");
                return true;
            }
            if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                TARDISMessage.send(player.getPlayer(), "NOT_WHILE_DISPERSED");
                return true;
            }
            boolean tmp_cham = false;
            if (plugin.getConfig().getBoolean("travel.chameleon")) {
                tmp_cham = tardis.isChamele_on();
            }
            boolean cham = tmp_cham;
            boolean hid = tardis.isHidden();
            PRESET demat = tardis.getDemat();
            String dir = args[1].toUpperCase(Locale.ENGLISH);
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
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
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            COMPASS d = COMPASS.valueOf(dir);
            // destroy sign
            if (!hid) {
                if (demat.equals(PRESET.DUCK)) {
                    plugin.getPresetDestroyer().destroyDuckEyes(l, old_d);
                }
                if (demat.equals(PRESET.MINESHAFT)) {
                    plugin.getPresetDestroyer().destroyMineshaftTorches(l, old_d);
                }
                if (demat.equals(PRESET.LAMP)) {
                    plugin.getPresetDestroyer().destroyLampTrapdoors(l, old_d);
                }
                if (demat.equals(PRESET.JUNK_MODE)) {
                    plugin.getPresetDestroyer().destroyHandbrake(l, old_d);
                }
                plugin.getPresetDestroyer().destroyDoor(id);
                plugin.getPresetDestroyer().destroySign(l, old_d, demat);
                final BuildData bd = new BuildData(plugin, uuid.toString());
                bd.setChameleon(cham);
                bd.setDirection(d);
                bd.setLocation(l);
                bd.setMalfunction(false);
                bd.setOutside(false);
                bd.setPlayer(player);
                bd.setRebuild(true);
                bd.setSubmarine(rsc.isSubmarine());
                bd.setTardisID(id);
                bd.setBiome(rsc.getBiome());
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.getPresetBuilder().buildPreset(bd);
                    }
                }, 10L);
            }
            HashMap<String, Object> wherea = new HashMap<String, Object>();
            wherea.put("tardis_id", id);
            qf.alterEnergyLevel("tardis", -amount, wherea, player);
            if (hid) {
                TARDISMessage.send(player, "DIRECTION_CHANGED");
            }
            // if they have a Direction Frame, update the rotation
            HashMap<String, Object> wheredf = new HashMap<String, Object>();
            wheredf.put("tardis_id", id);
            wheredf.put("type", 18);
            ResultSetControls rsdf = new ResultSetControls(plugin, wheredf, false);
            if (rsdf.resultSet()) {
                String locToCheck = rsdf.getLocation();
                Location dfl = plugin.getLocationUtils().getLocationFromBukkitString(locToCheck);
                if (dfl != null) {
                    Chunk chunk = dfl.getChunk();
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    for (Entity e : chunk.getEntities()) {
                        if (e instanceof ItemFrame && e.getLocation().toString().equals(locToCheck)) {
                            ItemFrame frame = (ItemFrame) e;
                            Rotation r;
                            switch (d) {
                                case EAST:
                                    r = Rotation.COUNTER_CLOCKWISE;
                                    break;
                                case SOUTH:
                                    r = Rotation.NONE;
                                    break;
                                case WEST:
                                    r = Rotation.CLOCKWISE;
                                    break;
                                default:
                                    r = Rotation.FLIPPED;
                                    break;
                            }
                            frame.setRotation(r);
                            break;
                        }
                    }
                }
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
