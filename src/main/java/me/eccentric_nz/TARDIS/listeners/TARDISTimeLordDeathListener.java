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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISPresetBuilderData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.destroyers.TARDISPresetDestroyerData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.travel.TARDISEPSRunnable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Several events can trigger an Automatic Emergency Landing. Under these
 * circumstances a TARDIS will use the coordinate override to initiate an
 * Automatic Emergency Landing on the "nearest" available habitable planet.
 *
 * @author eccentric_nz
 */
public class TARDISTimeLordDeathListener implements Listener {

    private final TARDIS plugin;

    public TARDISTimeLordDeathListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player death. If the player is a time lord and the autonomous
     * circuit is engaged, then the TARDIS will automatically return to its
     * 'home' location, or the nearest Recharge area.
     *
     * @param event a player dying
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onTimeLordDeath(PlayerDeathEvent event) {
        if (plugin.getConfig().getBoolean("allow.autonomous")) {
            Player player = event.getEntity();
            if (player.hasPermission("tardis.autonomous")) {
                String playerNameStr = player.getName();
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", playerNameStr);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                // are they a time lord?
                if (rs.resultSet()) {
                    int id = rs.getTardis_id();
                    String eps = rs.getEps();
                    String creeper = rs.getCreeper();
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("player", playerNameStr);
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                    if (rsp.resultSet()) {
                        // do they have the autonomous circuit on?
                        if (rsp.isAutoOn()) {
                            Location death_loc = player.getLocation();
                            if (plugin.getPM().isPluginEnabled("Citizens") && plugin.getConfig().getBoolean("allow.emergency_npc") && rsp.isEpsOn()) {
                                // check if there are players in the TARDIS
                                HashMap<String, Object> wherev = new HashMap<String, Object>();
                                wherev.put("tardis_id", id);
                                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherev, true);
                                if (rst.resultSet()) {
                                    List data = rst.getData();
                                    if (data.size() > 0 && !data.contains(playerNameStr)) {
                                        // schedule the NPC to appear
                                        TARDISEPSRunnable EPS_runnable = new TARDISEPSRunnable(plugin, rsp.getEpsMessage(), player, data, id, eps, creeper);
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, EPS_runnable, 20L);
                                    }
                                }
                            }
                            String death_world = death_loc.getWorld().getName();
                            // where is the TARDIS Police Box?
                            HashMap<String, Object> wherecl = new HashMap<String, Object>();
                            wherecl.put("tardis_id", id);
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                            if (!rsc.resultSet()) {
                                plugin.debug("Current record not found!");
                                return;
                            }
                            COMPASS cd = rsc.getDirection();
                            Location sl = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                            // where is home?
                            HashMap<String, Object> wherehl = new HashMap<String, Object>();
                            wherehl.put("tardis_id", id);
                            ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                            if (!rsh.resultSet()) {
                                plugin.debug("Home record not found!");
                                return;
                            }
                            World hw = rsh.getWorld();
                            Location home_loc = new Location(hw, rsh.getX(), rsh.getY(), rsh.getZ());
                            COMPASS hd = rsh.getDirection();
                            boolean sub = rsh.isSubmarine();
                            Location goto_loc;
                            boolean going_home = false;
                            // if home world is NOT the death world
                            if (!hw.getName().equals(death_world)) {
                                // look for a recharge location
                                goto_loc = getRecharger(death_world, player);
                                if (goto_loc == null) {
                                    // no parking spots - default to TARDIS home location
                                    goto_loc = home_loc;
                                    going_home = true;
                                }
                            } else {
                                // died in home world get closest location
                                Location recharger = getRecharger(death_world, player);
                                if (recharger != null) {
                                    // which is closer?
                                    boolean closer = death_loc.distanceSquared(home_loc) > death_loc.distanceSquared(recharger);
                                    goto_loc = (closer) ? recharger : home_loc;
                                    if (!closer) {
                                        going_home = true;
                                    }
                                } else {
                                    // no parking spots - set to TARDIS home location
                                    goto_loc = home_loc;
                                    going_home = true;
                                }
                            }
                            // if the TARDIS is already at the home location, do nothing
                            if (!compareCurrentToHome(rsc, rsh)) {
                                QueryFactory qf = new QueryFactory(plugin);
                                boolean cham = rs.isChamele_on();
                                COMPASS fd = (going_home) ? hd : cd;
                                // destroy police box
                                final TARDISPresetDestroyerData pdd = new TARDISPresetDestroyerData();
                                pdd.setChameleon(cham);
                                pdd.setDirection(cd);
                                pdd.setLocation(sl);
                                pdd.setDematerialise(plugin.getConfig().getBoolean("police_box.materialise"));
                                pdd.setPlayer(player);
                                pdd.setHide(false);
                                pdd.setSubmarine(rsc.isSubmarine());
                                pdd.setTardisID(id);
                                if (!rs.isHidden()) {
                                    plugin.getPresetDestroyer().destroyPreset(pdd);
                                } else {
                                    plugin.getPresetDestroyer().removeBlockProtection(id, qf);
                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                    set.put("hidden", 0);
                                    HashMap<String, Object> tid = new HashMap<String, Object>();
                                    tid.put("tardis_id", id);
                                    qf.doUpdate("tardis", set, tid);
                                }
                                final TARDISPresetBuilderData pbd = new TARDISPresetBuilderData();
                                pbd.setChameleon(cham);
                                pbd.setDirection(fd);
                                pbd.setLocation(goto_loc);
                                pbd.setMalfunction(false);
                                pbd.setPlayer(player);
                                pbd.setRebuild(false);
                                pbd.setSubmarine(sub);
                                pbd.setTardisID(id);
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        // rebuild police box - needs to be a delay
                                        plugin.getPresetBuilder().buildPreset(pbd);
                                    }
                                }, 200L);
                                // set current
                                HashMap<String, Object> setc = new HashMap<String, Object>();
                                setc.put("world", goto_loc.getWorld().getName());
                                setc.put("x", goto_loc.getBlockX());
                                setc.put("y", goto_loc.getBlockY());
                                setc.put("z", goto_loc.getBlockZ());
                                setc.put("direction", fd.toString());
                                setc.put("submarine", (sub) ? 1 : 0);
                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                wherec.put("tardis_id", id);
                                qf.doUpdate("current", setc, wherec);
                                // set back
                                HashMap<String, Object> setb = new HashMap<String, Object>();
                                setb.put("world", rsc.getWorld().getName());
                                setb.put("x", rsc.getX());
                                setb.put("y", rsc.getY());
                                setb.put("z", rsc.getZ());
                                setb.put("direction", rsc.getDirection().toString());
                                setb.put("submarine", (rsc.isSubmarine()) ? 1 : 0);
                                HashMap<String, Object> whereb = new HashMap<String, Object>();
                                whereb.put("tardis_id", id);
                                qf.doUpdate("back", setb, whereb);
                                // take energy
                                HashMap<String, Object> wherea = new HashMap<String, Object>();
                                wherea.put("tardis_id", id);
                                int amount = plugin.getArtronConfig().getInt("autonomous") * -1;
                                qf.alterEnergyLevel("tardis", amount, wherea, player);
                            }
                        }
                    }
                }
            }
        }
    }

    private Location getRecharger(String world, Player player) {
        Location l = null;
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("world", world);
        ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
        if (rsa.resultSet()) {
            String area = rsa.getArea_name();
            if (!player.hasPermission("tardis.area." + area) || !player.isPermissionSet("tardis.area." + area)) {
                return null;
            }
            l = plugin.getTardisArea().getNextSpot(rsa.getArea_name());
        }
        return l;
    }

    private boolean compareCurrentToHome(ResultSetCurrentLocation c, ResultSetHomeLocation h) {
        return (c.getWorld().equals(h.getWorld())
                && c.getX() == h.getX()
                && c.getY() == h.getY()
                && c.getZ() == h.getZ());
    }
}
