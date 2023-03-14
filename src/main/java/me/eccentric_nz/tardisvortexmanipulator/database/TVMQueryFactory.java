/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.tardisvortexmanipulator.database;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;

/**
 * Do basic SQL INSERT, UPDATE and DELETE queries.
 *
 * @author eccentric_nz
 */
public class TVMQueryFactory {

    private final TARDIS plugin;

    public TVMQueryFactory(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Save a beacon block.
     *
     * @param uuid the uuid of the player who has set the beacon
     * @param b    the block to save
     */
    public void saveBeaconBlock(String uuid, Block b) {
        Location loc = b.getLocation();
        plugin.getTvmSettings().getBlocks().add(loc);
        String data = b.getBlockData().getAsString();
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", uuid);
        set.put("location", loc.toString());
        set.put("block_data", data);
        plugin.getQueryFactory().doSyncInsert("beacons", set);
    }

    /**
     * Alter tachyon levels. This method executes the SQL in a separate thread.
     *
     * @param uuid   the player's string UUID
     * @param amount the amount add tachyons to add or remove
     */
    public void alterTachyons(String uuid, int amount) {
        TVMAlterTachyon alter = new TVMAlterTachyon(plugin, amount, uuid);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, alter);
    }

    /**
     * Update message read status. This method executes the SQL in a separate thread.
     *
     * @param id the message_id to alter
     */
    public void setReadStatus(int id) {
        TVMSetReadStatus set = new TVMSetReadStatus(plugin, id);
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, set);
    }
}
