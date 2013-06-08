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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * A TARDIS with a functioning chameleon circuit can appear as almost anything
 * desired. The owner can program the circuit to make it assume a specific
 * shape.
 *
 * @author eccentric_nz
 */
public class TARDISSignListener implements Listener {

    private TARDIS plugin;

    public TARDISSignListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with the TARDIS chameleon or save-sign
     * Signs. If the signs are clicked, they trigger the appropriate actions,
     * for example turning the Chameleon Circuit on and off.
     *
     * @param event the player clicking a sign
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                // only proceed if they are clicking a sign!
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // get clicked block location
                    Location b = block.getLocation();
                    Sign s = (Sign) block.getState();
                    String line1 = s.getLine(0);
                    String bw = b.getWorld().getName();
                    int bx = b.getBlockX();
                    int by = b.getBlockY();
                    int bz = b.getBlockZ();
                    String signloc = bw + ":" + bx + ":" + by + ":" + bz;
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    if (line1.equals("Chameleon")) {
                        where.put("chameleon", signloc);
                    } else {
                        where.put("save_sign", signloc);
                    }
                    // get tardis from saved sign location
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        QueryFactory qf = new QueryFactory(plugin);
                        int id = rs.getTardis_id();
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        if (line1.equals("Chameleon")) {
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            if (rs.isChamele_on()) {
                                set.put("chamele_on", 0);
                                s.setLine(3, ChatColor.RED + "OFF");
                                s.update();
                            } else {
                                set.put("chamele_on", 1);
                                s.setLine(3, ChatColor.GREEN + "ON");
                                s.update();
                            }
                            qf.doUpdate("tardis", set, tid);
                        } else {
                            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && player.isSneaking()) {
                                if (!rs.isHandbrake_on()) {
                                    player.sendMessage(plugin.pluginName + ChatColor.RED + "You cannot set a destination while the TARDIS is travelling!");
                                    return;
                                }
                                // check they have enough artron energy to travel
                                int level = rs.getArtron_level();
                                if (level < plugin.getArtronConfig().getInt("travel")) {
                                    player.sendMessage(plugin.pluginName + ChatColor.RED + "The TARDIS does not have enough Artron Energy to make this trip!");
                                    return;
                                }
                                // set destination to currently displayed save
                                HashMap<String, Object> sets = new HashMap<String, Object>();
                                if (s.getLine(1).equals("Home")) {
                                    sets.put("save", rs.getHome());
                                } else {
                                    // get location from sign
                                    String[] coords = s.getLine(3).split(",");
                                    String loc = s.getLine(2) + ":" + coords[0] + ":" + coords[1] + ":" + coords[2];
                                    sets.put("save", loc);
                                    World w = plugin.getServer().getWorld(s.getLine(2));
                                    int x, y, z;
                                    x = plugin.utils.parseNum(coords[0]);
                                    y = plugin.utils.parseNum(coords[1]);
                                    z = plugin.utils.parseNum(coords[2]);
                                    Location l = new Location(w, x, y, z);
                                    TARDISPluginRespect respect = new TARDISPluginRespect(plugin);
                                    if (!respect.getRespect(player, l, true)) {
                                        return;
                                    }
                                }
                                HashMap<String, Object> sid = new HashMap<String, Object>();
                                sid.put("tardis_id", id);
                                qf.doUpdate("tardis", sets, sid);
                                plugin.tardisHasDestination.put(id, plugin.getArtronConfig().getInt("travel"));
                                if (plugin.trackRescue.containsKey(Integer.valueOf(id))) {
                                    plugin.trackRescue.remove(Integer.valueOf(id));
                                }
                                player.sendMessage(plugin.pluginName + "Exit location set to " + s.getLine(1));
                            }
                            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && !player.isSneaking()) {
                                List<String> dests = new ArrayList<String>();
                                HashMap<String, Object> did = new HashMap<String, Object>();
                                did.put("tardis_id", id);
                                ResultSetDestinations rsd = new ResultSetDestinations(plugin, did, true);
                                if (rsd.resultSet()) {
                                    ArrayList<HashMap<String, String>> data = rsd.getData();
                                    String home = "";
                                    // cycle through saves
                                    for (HashMap<String, String> map : data) {
                                        if (home.isEmpty()) {
                                            home = "Home:" + rs.getHome();
                                        }
                                        if (map.get("type").equals("0")) {
                                            dests.add(map.get("dest_name") + ":" + map.get("world") + ":" + map.get("x") + ":" + map.get("y") + ":" + map.get("z"));
                                        }
                                    }
                                    dests.add(home);
                                    String[] display;
                                    if (plugin.trackDest.containsKey(player.getName()) && dests.size() > 1) {
                                        reOrder(dests, plugin.trackDest.get(player.getName()));
                                        plugin.trackDest.put(player.getName(), dests.get(1));
                                        display = dests.get(1).split(":");
                                    } else {
                                        display = dests.get(dests.size() - 1).split(":");
                                        plugin.trackDest.put(player.getName(), dests.get(dests.size() - 1));
                                    }
                                    s.setLine(1, display[0]);
                                    s.setLine(2, display[1]);
                                    s.setLine(3, display[2] + "," + display[3] + "," + display[4]);
                                    s.update();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Reorders a list so the the first item is moved to the end.
     *
     * @param list
     * @param current
     */
    public void reOrder(List<String> list, String current) {
        int i = list.size();
        while (i-- > 0 && !list.get(0).equals(current)) {
            list.add(list.remove(0));
        }
    }
}
