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
package me.eccentric_nz.tardis.sonic.actions;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetBackLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TardisSonicAdmin {

    public static void displayInfo(TardisPlugin plugin, Player player, Block block) {
        TardisSonicSound.playSonicSound(plugin, player, System.currentTimeMillis(), 600L, "sonic_short");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            HashMap<String, Object> wheredoor = new HashMap<>();
            Location loc = block.getLocation();
            String bw = Objects.requireNonNull(loc.getWorld()).getName();
            int bx = loc.getBlockX();
            int by = loc.getBlockY();
            int bz = loc.getBlockZ();
            if (Tag.DOORS.isTagged(block.getType())) {
                Bisected bisected = (Bisected) block.getBlockData();
                if (bisected.getHalf().equals(Bisected.Half.TOP)) {
                    by = (by - 1);
                }
            }
            String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
            wheredoor.put("door_location", doorloc);
            wheredoor.put("door_type", 0);
            ResultSetDoors rsd = new ResultSetDoors(plugin, wheredoor, false);
            if (rsd.resultSet()) {
                int id = rsd.getTardisId();
                // get the TARDIS owner's name
                HashMap<String, Object> wheren = new HashMap<>();
                wheren.put("tardis_id", id);
                ResultSetTardis rsn = new ResultSetTardis(plugin, wheren, "", false, 0);
                if (rsn.resultSet()) {
                    Tardis tardis = rsn.getTardis();
                    String name = plugin.getServer().getOfflinePlayer(tardis.getUuid()).getName();
                    TardisMessage.send(player, "TARDIS_WHOSE", name);
                    int percent = Math.round((tardis.getArtronLevel() * 100F) / plugin.getArtronConfig().getInt("full_charge"));
                    TardisMessage.send(player, "ENERGY_LEVEL", String.format("%d", percent));
                    HashMap<String, Object> whereb = new HashMap<>();
                    whereb.put("tardis_id", id);
                    ResultSetBackLocation rsb = new ResultSetBackLocation(plugin, whereb);
                    if (rsb.resultSet()) {
                        TardisMessage.send(player, "SCAN_LAST", rsb.getWorld().getName() + " " + rsb.getX() + ":" + rsb.getY() + ":" + rsb.getZ());
                    }
                }
                HashMap<String, Object> whereid = new HashMap<>();
                whereid.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, whereid, true);
                if (rst.resultSet()) {
                    List<UUID> data = rst.getData();
                    TardisMessage.send(player, "SONIC_INSIDE");
                    data.forEach((s) -> {
                        Player p = plugin.getServer().getPlayer(s);
                        if (p != null) {
                            player.sendMessage(p.getDisplayName());
                        } else {
                            player.sendMessage(plugin.getServer().getOfflinePlayer(s).getName() + " (Offline)");
                        }
                    });
                } else {
                    TardisMessage.send(player, "SONIC_OCCUPY");
                }
            }
        }, 60L);
    }
}
