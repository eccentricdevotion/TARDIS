/*
 * Copyright (C) 2023 eccentric_nz
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
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISDirectionCommand {

    private final TARDIS plugin;

    public TARDISDirectionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean changeDirection(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
            if (args.length < 2) {
                TARDISMessage.send(player, "DIRECTION_NEED");
                return true;
            }
            COMPASS compass;
            try {
                compass = COMPASS.valueOf(args[1].toUpperCase());
            } catch (IllegalArgumentException e) {
                TARDISMessage.send(player, "DIRECTION_NEED");
                return true;
            }
            UUID uuid = player.getUniqueId();
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return true;
            }
            Tardis tardis = rs.getTardis();
            if (!tardis.getPreset().usesItemFrame()
                    && (args[1].equalsIgnoreCase("north_east") || args[1].equalsIgnoreCase("north_west") || args[1].equalsIgnoreCase("south_west") || args[1].equalsIgnoreCase("south_east"))) {
                TARDISMessage.send(player, "DIRECTION_PRESET");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                TARDISMessage.send(player, "POWER_DOWN");
                return true;
            }
            int id = tardis.getTardis_id();
            TARDISCircuitChecker tcc = null;
            if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
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
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                TARDISMessage.send(player, "NOT_WHILE_MAT");
                return true;
            }
            if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                TARDISMessage.send(player.getPlayer(), "NOT_WHILE_DISPERSED");
                return true;
            }
            boolean hid = tardis.isHidden();
            ChameleonPreset demat = tardis.getDemat();
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                return true;
            }
            COMPASS old_d = rsc.getDirection();
            HashMap<String, Object> tid = new HashMap<>();
            HashMap<String, Object> set = new HashMap<>();
            tid.put("tardis_id", id);
            set.put("direction", compass.toString());
            plugin.getQueryFactory().doUpdate("current", set, tid);
            HashMap<String, Object> did = new HashMap<>();
            HashMap<String, Object> setd = new HashMap<>();
            did.put("door_type", 0);
            did.put("tardis_id", id);
            setd.put("door_direction", compass.forPreset().toString());
            plugin.getQueryFactory().doUpdate("doors", setd, did);
            // close doors & therefore remove open portals...
            new TARDISDoorCloser(plugin, uuid, id).closeDoors();
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            // destroy sign
            if (!hid) {
                if (demat.equals(ChameleonPreset.DUCK)) {
                    plugin.getPresetDestroyer().destroyDuckEyes(l, old_d);
                }
                if (demat.equals(ChameleonPreset.MINESHAFT)) {
                    plugin.getPresetDestroyer().destroyMineshaftTorches(l, old_d);
                }
                if (demat.equals(ChameleonPreset.LAMP)) {
                    plugin.getPresetDestroyer().destroyLampTrapdoors(l, old_d);
                }
                if (demat.equals(ChameleonPreset.JUNK_MODE)) {
                    plugin.getPresetDestroyer().destroyHandbrake(l, old_d);
                }
                plugin.getPresetDestroyer().destroyDoor(id);
                plugin.getPresetDestroyer().destroySign(l, old_d, demat);
                BuildData bd = new BuildData(uuid.toString());
                bd.setDirection(compass);
                bd.setLocation(l);
                bd.setMalfunction(false);
                bd.setOutside(false);
                bd.setPlayer(player);
                bd.setRebuild(true);
                bd.setSubmarine(rsc.isSubmarine());
                bd.setTardisID(id);
                bd.setThrottle(SpaceTimeThrottle.REBUILD);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 10L);
                TARDISMessage.send(player, "DIRECTION_CHANGED");
            }
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wherea, player);
            // if they have a Direction Frame, update the rotation
            HashMap<String, Object> wheredf = new HashMap<>();
            wheredf.put("tardis_id", id);
            wheredf.put("type", 18);
            ResultSetControls rsdf = new ResultSetControls(plugin, wheredf, false);
            if (rsdf.resultSet()) {
                String locToCheck = rsdf.getLocation();
                Location dfl = TARDISStaticLocationGetters.getLocationFromBukkitString(locToCheck);
                if (dfl != null) {
                    Chunk chunk = dfl.getChunk();
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    for (Entity e : chunk.getEntities()) {
                        if (e instanceof ItemFrame frame && e.getLocation().toString().equals(locToCheck)) {
                            Rotation r = switch (compass) {
                                case EAST -> Rotation.COUNTER_CLOCKWISE;
                                case SOUTH_EAST -> Rotation.COUNTER_CLOCKWISE_45;
                                case SOUTH -> Rotation.NONE;
                                case SOUTH_WEST -> Rotation.CLOCKWISE_45;
                                case WEST -> Rotation.CLOCKWISE;
                                case NORTH_WEST -> Rotation.CLOCKWISE_135;
                                case NORTH -> Rotation.FLIPPED;
                                default -> Rotation.FLIPPED_45;
                            };
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
