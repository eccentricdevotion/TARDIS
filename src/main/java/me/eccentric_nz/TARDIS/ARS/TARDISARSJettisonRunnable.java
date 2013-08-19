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
package me.eccentric_nz.TARDIS.ARS;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISARSJettisonRunnable implements Runnable {

    private final TARDIS plugin;
    private TARDISARSJettison slot;
    private TARDISARS room;
    private int id;
    private Player p;

    public TARDISARSJettisonRunnable(TARDIS plugin, TARDISARSJettison slot, TARDISARS room, int id, Player p) {
        this.plugin = plugin;
        this.slot = slot;
        this.room = room;
        this.id = id;
        this.p = p;
    }

    @Override
    public void run() {
        // remove the room
        World w = slot.getChunk().getWorld();
        int x = slot.getX();
        int y = slot.getY();
        int z = slot.getZ();
        for (int yy = y; yy < (y + 16); yy++) {
            for (int xx = x; xx < (x + 16); xx++) {
                for (int zz = z; zz < (z + 16); zz++) {
                    w.getBlockAt(xx, yy, zz).setTypeId(0);
                }
            }
        }
        // give them their energy!
        if (room != null) {
            String r = room.toString();
            int amount = Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + r + ".cost"));
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("tardis_id", id);
            qf.alterEnergyLevel("tardis", amount, set, null);
            if (p.isOnline()) {
                p.sendMessage(plugin.pluginName + amount + " Artron Energy recovered.");
            }
            // if it is a secondary console room remove the controls
            if (r.equals("BAKER") || r.equals("WOOD")) {
                // get tardis_id
                int secondary = (r.equals("BAKER")) ? 1 : 2;
                HashMap<String, Object> del = new HashMap<String, Object>();
                del.put("tardis_id", id);
                del.put("secondary", secondary);
                qf.doDelete("controls", del);
            }
        }
    }
}
