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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisSpectaclesRunnable implements Runnable {

    private final TardisPlugin plugin;
    private final HashMap<CardinalDirection, Door> lower = new HashMap<>();
    private final Door upper;

    public TardisSpectaclesRunnable(TardisPlugin plugin) {
        this.plugin = plugin;
        Door door = (Door) Material.IRON_DOOR.createBlockData();
        door.setHalf(Bisected.Half.BOTTOM);
        door.setHinge(Door.Hinge.RIGHT);
        lower.put(CardinalDirection.EAST, calculateFacing(door, CardinalDirection.EAST));
        lower.put(CardinalDirection.SOUTH, calculateFacing(door, CardinalDirection.SOUTH));
        lower.put(CardinalDirection.WEST, calculateFacing(door, CardinalDirection.WEST));
        lower.put(CardinalDirection.NORTH, calculateFacing(door, CardinalDirection.NORTH));
        upper = (Door) Material.IRON_DOOR.createBlockData();
        upper.setHalf(Bisected.Half.TOP);
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
                        wherec.put("tardis_id", rs.getTardisId());
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                        if (rsc.resultSet()) {
                            p.sendBlockChange(value.getLocation(), lower.get(rsc.getDirection()));
                            p.sendBlockChange(value.getRelative(BlockFace.UP).getLocation(), upper);
                        }
                    }
                }
            }
        });
    }

    private Door calculateFacing(Door door, CardinalDirection cardinalDirection) {
        switch (cardinalDirection) {
            case SOUTH -> door.setFacing(BlockFace.SOUTH);
            case WEST -> door.setFacing(BlockFace.WEST);
            case NORTH -> door.setFacing(BlockFace.NORTH);
            default -> door.setFacing(BlockFace.EAST);
        }
        return door;
    }
}
