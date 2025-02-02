/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisPreset;
import me.eccentric_nz.TARDIS.doors.inner.Inner;
import me.eccentric_nz.TARDIS.doors.inner.InnerDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.inner.InnerDoor;
import me.eccentric_nz.TARDIS.doors.inner.InnerMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDisplayDoorCloser;
import me.eccentric_nz.TARDIS.doors.outer.OuterDoor;
import me.eccentric_nz.TARDIS.doors.outer.OuterMinecraftDoorCloser;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
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
public class TARDISDirectionCommand {

    private final TARDIS plugin;

    public TARDISDirectionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean changeDirection(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
            if (args.length < 2) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DIRECTION_NEED");
                return true;
            }
            COMPASS compass;
            try {
                compass = COMPASS.valueOf(args[1].toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DIRECTION_NEED");
                return true;
            }
            UUID uuid = player.getUniqueId();
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (!rs.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
                return true;
            }
            Tardis tardis = rs.getTardis();
            if (!tardis.getPreset().usesArmourStand()
                    && (args[1].equalsIgnoreCase("north_east") || args[1].equalsIgnoreCase("north_west") || args[1].equalsIgnoreCase("south_west") || args[1].equalsIgnoreCase("south_east"))) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DIRECTION_PRESET");
                return true;
            }
            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                return true;
            }
            int id = tardis.getTardisId();
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, true) && !tcc.hasMaterialisation()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                return true;
            }
            // damage circuit if configured
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                // decrement uses
                int uses_left = tcc.getMaterialisationUses();
                new TARDISCircuitDamager(plugin, DiskCircuit.MATERIALISATION, uses_left, id, player).damage();
            }
            int level = tardis.getArtronLevel();
            int amount = plugin.getArtronConfig().getInt("random");
            if (level < amount) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_DIRECTION");
                return true;
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_IN_VORTEX");
                return true;
            }
            if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT");
                return true;
            }
            if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_DISPERSED");
                return true;
            }
            boolean hid = tardis.isHidden();
            ChameleonPreset demat = tardis.getDemat();
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            if (!rsc.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CURRENT_NOT_FOUND");
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
            ResultSetTardisPreset rsp = new ResultSetTardisPreset(plugin);
            if (rsp.fromID(id)) {
                boolean outerDisplayDoor = rsp.getPreset().usesArmourStand();
                Inner innerDisplayDoor = new InnerDoor(plugin, id).get();
                // close inner
                if (innerDisplayDoor.display()) {
                    new InnerDisplayDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid, true);
                } else {
                    new InnerMinecraftDoorCloser(plugin).close(innerDisplayDoor.block(), id, uuid);
                }
                // close outer
                if (outerDisplayDoor) {
                    new OuterDisplayDoorCloser(plugin).close(new OuterDoor(plugin, id).getDisplay(), id, uuid);
                } else if (rsp.getPreset().hasDoor()) {
                    new OuterMinecraftDoorCloser(plugin).close(new OuterDoor(plugin, id).getMinecraft(rsp.getPreset()), id, uuid);
                }
            }
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
                plugin.getMessenger().send(player, TardisModule.TARDIS, "DIRECTION_CHANGED");
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
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }
}
