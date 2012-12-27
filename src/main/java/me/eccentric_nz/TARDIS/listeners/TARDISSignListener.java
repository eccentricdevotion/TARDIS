/*
 * Copyright (C) 2012 eccentric_nz
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

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Listens for player interaction with the TARDIS chameleon or save-sign Signs.
 * If the signs are clicked, they trigger the appropriate actions, for example
 * turning the Chameleon Circuit on and off.
 *
 * @author eccentric_nz
 */
public class TARDISSignListener implements Listener {

    private TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISSignListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSignInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                ItemStack stack = player.getItemInHand();
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
                    // get tardis from saved button location
                    ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                    if (rs.resultSet()) {
                        QueryFactory qf = new QueryFactory(plugin);
                        int id = rs.getTardis_id();
                        HashMap<String, Object> tid = new HashMap<String, Object>();
                        tid.put("tardis_id", id);
                        if (line1.equals("Chameleon")) {
                            HashMap<String, Object> set = new HashMap<String, Object>();
                            if (rs.getChameleon_on()) {
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
                                // set destination to currently displayed save
                                HashMap<String, Object> sets = new HashMap<String, Object>();
                                if (s.getLine(1).equals("Home")) {
                                    sets.put("save", rs.getHome());
                                } else {
                                    // get location from sign
                                    String[] coords = s.getLine(3).split(",");
                                    String loc = s.getLine(2) + ":" + coords[0] + ":" + coords[1] + ":" + coords[2];
                                    sets.put("save", loc);
                                }
                                HashMap<String, Object> sid = new HashMap<String, Object>();
                                sid.put("tardis_id", id);
                                qf.doUpdate("tardis", sets, sid);
                                plugin.utils.updateTravellerCount(id);
                                player.sendMessage(plugin.pluginName + " Exit location set");
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
                                        if (home.equals("")) {
                                            home = "Home:" + map.get("home");
                                        }
                                        dests.add(map.get("dest_name") + ":" + map.get("world") + ":" + map.get("x") + ":" + map.get("y") + ":" + map.get("z"));
                                    }
                                    dests.add(home);
                                    String[] display;
                                    if (plugin.trackDest.containsKey(player.getName())) {
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

    public void reOrder(List<String> list, String current) {
        int i = list.size();
        while (i-- > 0 && !list.get(0).equals(current)) {
            list.add(list.remove(0));
        }
    }
}
