/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetControls;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.move.TardisDoorCloser;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisDirectionCommand {

    private final TardisPlugin plugin;

    public TardisDirectionCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean changeDirection(Player player, String[] args) {
        if (TardisPermission.hasPermission(player, "tardis.timetravel")) {
            if (args.length < 2 || (!args[1].equalsIgnoreCase("north") && !args[1].equalsIgnoreCase("west") && !args[1].equalsIgnoreCase("south") && !args[1].equalsIgnoreCase("east"))) {
                TardisMessage.send(player, "DIRECTION_NEED");
                return false;
            }
            UUID uuid = player.getUniqueId();
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                TardisMessage.send(player, "NO_TARDIS");
                return false;
            }
            Tardis tardis = rs.getTardis();
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered()) {
                TardisMessage.send(player, "POWER_DOWN");
                return true;
            }
            int id = tardis.getTardisId();
            TardisCircuitChecker tcc = null;
            if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                tcc = new TardisCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMaterialisation()) {
                TardisMessage.send(player, "NO_MAT_CIRCUIT");
                return true;
            }
            int level = tardis.getArtronLevel();
            int amount = plugin.getArtronConfig().getInt("random");
            if (level < amount) {
                TardisMessage.send(player, "ENERGY_NO_DIRECTION");
                return true;
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                TardisMessage.send(player.getPlayer(), "NOT_IN_VORTEX");
                return true;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                TardisMessage.send(player, "NOT_WHILE_MAT");
                return true;
            }
            if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                TardisMessage.send(player.getPlayer(), "NOT_WHILE_DISPERSED");
                return true;
            }
            boolean hid = tardis.isHidden();
            Preset demat = tardis.getDemat();
            String dir = args[1].toUpperCase(Locale.ENGLISH);
            HashMap<String, Object> wherecl = new HashMap<>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (!rsc.resultSet()) {
                TardisMessage.send(player, "CURRENT_NOT_FOUND");
                return true;
            }
            CardinalDirection old_d = rsc.getDirection();
            HashMap<String, Object> tid = new HashMap<>();
            HashMap<String, Object> set = new HashMap<>();
            tid.put("tardis_id", id);
            set.put("direction", dir);
            plugin.getQueryFactory().doUpdate("current", set, tid);
            HashMap<String, Object> did = new HashMap<>();
            HashMap<String, Object> setd = new HashMap<>();
            did.put("door_type", 0);
            did.put("tardis_id", id);
            setd.put("door_direction", dir);
            plugin.getQueryFactory().doUpdate("doors", setd, did);
            // close doors & therefore remove open portals...
            new TardisDoorCloser(plugin, uuid, id).closeDoors();
            Location l = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            CardinalDirection d = CardinalDirection.valueOf(dir);
            // destroy sign
            if (!hid) {
                if (demat.equals(Preset.DUCK)) {
                    plugin.getPresetDestroyer().destroyDuckEyes(l, old_d);
                }
                if (demat.equals(Preset.MINESHAFT)) {
                    plugin.getPresetDestroyer().destroyMineshaftTorches(l, old_d);
                }
                if (demat.equals(Preset.LAMP)) {
                    plugin.getPresetDestroyer().destroyLampTrapdoors(l, old_d);
                }
                if (demat.equals(Preset.JUNK_MODE)) {
                    plugin.getPresetDestroyer().destroyHandbrake(l, old_d);
                }
                plugin.getPresetDestroyer().destroyDoor(id);
                plugin.getPresetDestroyer().destroySign(l, old_d, demat);
                BuildData bd = new BuildData(uuid.toString());
                bd.setDirection(d);
                bd.setLocation(l);
                bd.setMalfunction(false);
                bd.setOutside(false);
                bd.setPlayer(player);
                bd.setRebuild(true);
                bd.setSubmarine(rsc.isSubmarine());
                bd.setTardisId(id);
                bd.setThrottle(SpaceTimeThrottle.REBUILD);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 10L);
            }
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, wherea, player);
            if (hid) {
                TardisMessage.send(player, "DIRECTION_CHANGED");
            }
            // if they have a Direction Frame, update the rotation
            HashMap<String, Object> wheredf = new HashMap<>();
            wheredf.put("tardis_id", id);
            wheredf.put("type", 18);
            ResultSetControls rsdf = new ResultSetControls(plugin, wheredf, false);
            if (rsdf.resultSet()) {
                String locToCheck = rsdf.getLocation();
                Location dfl = TardisStaticLocationGetters.getLocationFromBukkitString(locToCheck);
                if (dfl != null) {
                    Chunk chunk = dfl.getChunk();
                    if (!chunk.isLoaded()) {
                        chunk.load();
                    }
                    for (Entity e : chunk.getEntities()) {
                        if (e instanceof ItemFrame frame && e.getLocation().toString().equals(locToCheck)) {
                            Rotation r = switch (d) {
                                case EAST -> Rotation.COUNTER_CLOCKWISE;
                                case SOUTH -> Rotation.NONE;
                                case WEST -> Rotation.CLOCKWISE;
                                default -> Rotation.FLIPPED;
                            };
                            frame.setRotation(r);
                            break;
                        }
                    }
                }
            }
            return true;
        } else {
            TardisMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
