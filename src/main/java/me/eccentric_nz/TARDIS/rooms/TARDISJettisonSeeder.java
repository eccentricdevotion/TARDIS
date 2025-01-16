/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * Artron energy is to normal energy what movements within the deeps of the sea are to the waves on the surface.
 *
 * @author eccentric_nz
 */
public class TARDISJettisonSeeder implements Listener {

    private final TARDIS plugin;

    public TARDISJettisonSeeder(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with a TNT block. If the block is clicked with the TARDIS key after running the
     * command /tardis jettison [room type], the TNT block's location and the room type are used to determine a cuboid
     * region that is set to AIR. The room walls are left in place as they maybe attached to other rooms/passage ways.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRoomSeedBlockInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        String playerNameStr = player.getName();
        UUID uuid = player.getUniqueId();
        // check that player is in TARDIS
        if (!plugin.getTrackerKeeper().getJettison().containsKey(uuid)) {
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
            // only proceed if they are clicking a room seed block with the TARDIS key!
            if (blockType.equals(Material.getMaterial(plugin.getArtronConfig().getString("jettison_seed"))) && inhand.equals(Material.getMaterial(key))) {
                String room = plugin.getTrackerKeeper().getJettison().get(uuid);
                // get jettison direction
                TARDISRoomDirection trd = new TARDISRoomDirection(block);
                trd.getDirection();
                if (!trd.isFound()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PLATE_NOT_FOUND");
                    return;
                }
                COMPASS d = trd.getCompass();
                BlockFace facing = trd.getFace();
                // get clicked block location
                Location l = block.getRelative(facing, 3).getLocation();
                // get the TARDIS id
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (rs.fromUUID(player.getUniqueId().toString())) {
                    int id = rs.getTardisId();
                    TARDISRoomRemover remover = new TARDISRoomRemover(plugin, room, l, d, id);
                    if (remover.remove()) {
                        plugin.getTrackerKeeper().getJettison().remove(uuid);
                        block.setBlockData(TARDISConstants.AIR);
                        l.getWorld().playEffect(l, Effect.POTION_BREAK, 9);
                        // ok they clicked it, so give them their energy!
                        int amount = Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + room + ".cost"));
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("uuid", player.getUniqueId().toString());
                        plugin.getQueryFactory().alterEnergyLevel("tardis", amount, set, player);
                        new RoomCleaner(plugin).removeRecords(room, id, block.getWorld(), player);
                        if (plugin.getConfig().getBoolean("growth.return_room_seed")) {
                            // give the player back the room seed block
                            ItemStack is = new ItemStack(Material.getMaterial(plugin.getRoomsConfig().getString("rooms." + room + ".seed")));
                            Inventory inv = player.getInventory();
                            inv.addItem(is);
                            player.updateInventory();
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_AMOUNT", String.format("%d", amount));
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_HAS_JETT");
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ID_NOT_FOUND");
                }
            }
        }
    }
}
