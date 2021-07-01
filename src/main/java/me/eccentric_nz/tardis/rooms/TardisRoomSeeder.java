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
package me.eccentric_nz.tardis.rooms;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advancement.TardisAdvancementFactory;
import me.eccentric_nz.tardis.database.resultset.ResultSetChunks;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.enumeration.Advancement;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * The Doctor kept some of the clothes from his previous regenerations, as well as clothing for other people in the
 * TARDIS wardrobe. At least some of the clothes had pockets that were bigger on the inside.
 *
 * @author eccentric_nz
 */
public class TardisRoomSeeder implements Listener {

    private final TardisPlugin plugin;

    public TardisRoomSeeder(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with one of the blocks required to seed a room. If the block is clicked with the
     * tardis key after running the command /tardis room [room type], the seed block will start growing into the room
     * type specified.
     * <p>
     * Requires the TARDIS to have sufficient Artron Energy to grow the room.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSeedBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        // check the player has run the /tardis room command
        if (!plugin.getTrackerKeeper().getRoomSeed().containsKey(uuid)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Material inhand = player.getInventory().getItemInMainHand().getType();
            String key;
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
            if (rsp.resultSet()) {
                key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
            } else {
                key = plugin.getConfig().getString("preferences.key");
            }
            // only proceed if they are clicking a seed block with the TARDIS key!
            if (plugin.getBuildKeeper().getSeeds().containsKey(blockType)) {
                assert key != null;
                if (inhand.equals(Material.getMaterial(key))) {
                    // check they are still in the TARDIS world
                    if (!plugin.getUtils().inTardisWorld(player)) {
                        TardisMessage.send(player, "ROOM_IN_WORLD");
                        plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                        return;
                    }
                    // get clicked block location
                    Location b = block.getLocation();
                    // get the growing direction
                    TardisRoomDirection trd = new TardisRoomDirection(block);
                    trd.getDirection();
                    if (!trd.isFound()) {
                        TardisMessage.send(player, "PLATE_NOT_FOUND");
                        plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                        return;
                    }
                    if (!trd.isAir()) {
                        TardisMessage.send(player, "AIR_NOT_FOUND");
                        plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                        return;
                    }
                    CardinalDirection d = trd.getCompass();
                    BlockFace facing = trd.getFace();
                    // get seed data
                    TardisSeedData sd = plugin.getTrackerKeeper().getRoomSeed().get(uuid);
                    Chunk c = Objects.requireNonNull(b.getWorld()).getChunkAt(block.getRelative(BlockFace.valueOf(d.toString()), 4));
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", sd.getId());
                    where.put("world", c.getWorld().getName());
                    where.put("x", c.getX());
                    where.put("z", c.getZ());
                    ResultSetChunks rsc = new ResultSetChunks(plugin, where, false);
                    if (rsc.resultSet()) {
                        TardisMessage.send(player, "ROOM_CONSOLE");
                        plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                        return;
                    }
                    // check they are not in an ars chunk
                    if (sd.hasArs()) {
                        int cx = c.getX();
                        int cy = block.getY();
                        int cz = c.getZ();
                        if ((cx >= sd.getMinx() && cx <= sd.getMaxx()) && (cy >= 48 && cy <= 96) && (cz >= sd.getMinz() && cz <= sd.getMaxz())) {
                            TardisMessage.send(player, "ROOM_USE_ARS");
                            plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                            return;
                        }
                    }
                    // get room schematic
                    String r = plugin.getBuildKeeper().getSeeds().get(blockType);
                    // check that the blockType is the same as the one they ran the /tardis room [type] command for
                    if (!sd.getRoom().equals(r)) {
                        TardisMessage.send(player, "ROOM_SEED_NOT_VALID", plugin.getTrackerKeeper().getRoomSeed().get(uuid).getRoom());
                        plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                        return;
                    }
                    // adjust the location three blocks out
                    Location l = block.getRelative(facing, 3).getLocation();
                    // build the room
                    TardisRoomBuilder builder = new TardisRoomBuilder(plugin, r, l, d, player);
                    if (builder.build()) {
                        // remove seed block and set door blocks to AIR as well
                        block.setBlockData(TardisConstants.AIR);
                        Block doorway = block.getRelative(facing, 2);
                        doorway.setBlockData(TardisConstants.AIR);
                        doorway.getRelative(BlockFace.UP).setBlockData(TardisConstants.AIR);
                        plugin.getTrackerKeeper().getRoomSeed().remove(uuid);
                        // ok, room growing was successful, so take their energy!
                        int amount = plugin.getRoomsConfig().getInt("rooms." + r + ".cost");
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("uuid", player.getUniqueId().toString());
                        plugin.getQueryFactory().alterEnergyLevel("tardis", -amount, set, player);
                        // remove blocks from condenser table if rooms_require_blocks is true
                        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                            TardisCondenserData c_data = plugin.getGeneralKeeper().getRoomCondenserData().get(uuid);
                            c_data.getBlockIDCount().forEach((key1, value) -> {
                                HashMap<String, Object> wherec = new HashMap<>();
                                wherec.put("tardis_id", c_data.getTardisId());
                                wherec.put("block_data", key1);
                                plugin.getQueryFactory().alterCondenserBlockCount(value, wherec);
                            });
                            plugin.getGeneralKeeper().getRoomCondenserData().remove(uuid);
                        }
                        // are we doing an advancement?
                        if (plugin.getAdvancementConfig().getBoolean("rooms.enabled")) {
                            TardisAdvancementFactory taf = new TardisAdvancementFactory(plugin, player, Advancement.ROOMS, plugin.getBuildKeeper().getSeeds().size());
                            taf.doAdvancement(r);
                        }
                    }
                }
            }
        }
    }
}
