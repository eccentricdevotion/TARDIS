/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.ARS;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;

/**
 * Performs Architectural Reconfiguration System room jettisons.
 *
 * @author eccentric_nz
 */
public class TARDISARSJettisonRunnable implements Runnable {

    private final TARDIS plugin;
    private final TARDISARSJettison slot;
    private final ARS room;
    private final int id;
    private final Player p;

    public TARDISARSJettisonRunnable(TARDIS plugin, TARDISARSJettison slot, ARS room, int id, Player p) {
        this.plugin = plugin;
        this.slot = slot;
        this.room = room;
        this.id = id;
        this.p = p;
    }

    @Override
    public void run() {
        QueryFactory qf = new QueryFactory(plugin);
        String r = room.getActualName();
        // remove the room
        World w = slot.getChunk().getWorld();
        int x = slot.getX();
        int y = slot.getY();
        int z = slot.getZ();
        for (int yy = y; yy < (y + 16); yy++) {
            for (int xx = x; xx < (x + 16); xx++) {
                for (int zz = z; zz < (z + 16); zz++) {
                    Block b = w.getBlockAt(xx, yy, zz);
                    // if it is a GRAVITY or ANTIGRAVITY well remove it from the database
                    if (r.equals("GRAVITY") || r.equals("ANTIGRAVITY")) {
                        if (b.getType().equals(Material.LIME_WOOL) || b.getType().equals(Material.PINK_WOOL)) {
                            String l = new Location(w, xx, yy, zz).toString();
                            HashMap<String, Object> where = new HashMap<>();
                            where.put("location", l);
                            where.put("tardis_id", id);
                            qf.doDelete("gravity_well", where);
                            // remove trackers
                            if (b.getType().equals(Material.LIME_WOOL)) {
                                plugin.getGeneralKeeper().getGravityUpList().remove(l);
                            } else {
                                plugin.getGeneralKeeper().getGravityDownList().remove(l);
                            }
                        }
                    }
                    b.setType(Material.AIR);
                }
            }
        }
        // give them their energy!
        if (room != TARDISARS.SLOT) {
            int amount = Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + r + ".cost"));
            if (r.equals("GRAVITY") || r.equals("ANTIGRAVITY")) {
                // halve it because they have to jettison top and bottom
                amount /= 2;
            }
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            qf.alterEnergyLevel("tardis", amount, set, null);
            if (p.isOnline()) {
                TARDISMessage.send(p, "ENERGY_RECOVERED", String.format("%d", amount));
            }
            // if it is a secondary console room remove the controls
            if (r.equals("BAKER") || r.equals("WOOD")) {
                // get tardis_id
                int secondary = (r.equals("BAKER")) ? 1 : 2;
                HashMap<String, Object> del = new HashMap<>();
                del.put("tardis_id", id);
                del.put("secondary", secondary);
                qf.doDelete("controls", del);
            }
            if (r.equals("RENDERER")) {
                // remove stored location from the database
                HashMap<String, Object> setd = new HashMap<>();
                setd.put("renderer", "");
                HashMap<String, Object> where = new HashMap<>();
                where.put("tardis_id", id);
                qf.doUpdate("tardis", setd, where);
                // remove WorldGuard protection
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    plugin.getWorldGuardUtils().removeRoomRegion(w, p.getName(), "renderer");
                }
            }
            // remove mob farming locations
            if (r.equals("FARM") || r.equals("HUTCH") || r.equals("IGLOO") || r.equals("RAIL") || r.equals("STABLE") || r.equals("STALL") || r.equals("VILLAGE") || r.equals("BIRDCAGE") || r.equals("AQUARIUM")) {
                HashMap<String, Object> wheref = new HashMap<>();
                wheref.put("tardis_id", id);
                HashMap<String, Object> setf = new HashMap<>();
                setf.put(r.toLowerCase(Locale.ENGLISH), "");
                qf.doUpdate("farming", setf, wheref);
            }
        }
    }
}
