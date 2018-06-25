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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISSpectaclesRunnable implements Runnable {

    private final TARDIS plugin;
    private final HashMap<COMPASS, Door> bottom = new HashMap<>();
    private final Door upper;
//    private final Door lower;

    public TARDISSpectaclesRunnable(TARDIS plugin) {
        this.plugin = plugin;
        // TODO set door facing, hinge, half in BlockData for each compass direction - use Material.createBlockData(String data) method?
        bottom.put(COMPASS.EAST, (Door) Material.IRON_DOOR.createBlockData());
        bottom.put(COMPASS.SOUTH, (Door) Material.IRON_DOOR.createBlockData());
        bottom.put(COMPASS.WEST, (Door) Material.IRON_DOOR.createBlockData());
        bottom.put(COMPASS.NORTH, (Door) Material.IRON_DOOR.createBlockData());
//        this.bottom.put(COMPASS.EAST, (byte) 0);
//        this.bottom.put(COMPASS.SOUTH, (byte) 1);
//        this.bottom.put(COMPASS.WEST, (byte) 2);
//        this.bottom.put(COMPASS.NORTH, (byte) 3);
        upper = (Door) Material.IRON_DOOR.createBlockData();
        upper.setHalf(Bisected.Half.TOP);
//        lower = (Door) Material.IRON_DOOR.createBlockData();
//        lower.setHalf(Bisected.Half.BOTTOM);
//        lower.setHinge(Door.Hinge.RIGHT);
//        lower.setFacing(BlockFace.EAST);
    }

    @Override

    public void run() {
        plugin.getTrackerKeeper().getInvisibleDoors().forEach((key, value) -> {
            Player p = plugin.getServer().getPlayer(key);
            if (p != null && p.isOnline() && plugin.getTrackerKeeper().getSpectacleWearers().contains(key)) {
                String b = p.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getRelative(BlockFace.UP).toString();
                if (b.equals(value.toString())) {
                    ResultSetTardisID rs = new ResultSetTardisID(plugin);
                    if (rs.fromUUID(key.toString())) {
                        HashMap<String, Object> wherec = new HashMap<>();
                        wherec.put("tardis_id", rs.getTardis_id());
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                        if (rsc.resultSet()) {
                            p.sendBlockChange(value.getLocation(), bottom.get(rsc.getDirection()));
                            p.sendBlockChange(value.getRelative(BlockFace.UP).getLocation(), upper);
                        }
                    }
                }
            }
        });
    }
}
