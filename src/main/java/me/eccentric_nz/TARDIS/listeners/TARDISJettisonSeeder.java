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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomDirection;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomRemover;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Artron energy is to normal energy what movements within the deeps of the sea
 * are to the waves on the surface.
 *
 * @author eccentric_nz
 */
public class TARDISJettisonSeeder implements Listener {

    private final TARDIS plugin;

    public TARDISJettisonSeeder(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with a TNT block. If the block is clicked
     * with the TARDIS key after running the command /tardis jettison [room
     * type], the TNT block's location and the room type are used to determine a
     * cuboid region that is set to AIR. The room walls are left in place as
     * they maybe attached to other rooms/passage ways.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSeedBlockInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        String playerNameStr = player.getName();
        // check that player is in TARDIS
        if (!plugin.trackJettison.containsKey(playerNameStr)) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Material inhand = player.getItemInHand().getType();
            String key;
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("player", playerNameStr);
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
            if (rsp.resultSet()) {
                key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
            } else {
                key = plugin.getConfig().getString("preferences.key");
            }
            // only proceed if they are clicking a seed block with the TARDIS key!
            if (blockType.equals(Material.getMaterial(plugin.getArtronConfig().getString("jettison_seed"))) && inhand.equals(Material.getMaterial(key))) {
                String r = plugin.trackJettison.get(playerNameStr);
                // get jettison direction
                TARDISRoomDirection trd = new TARDISRoomDirection(block);
                trd.getDirection();
                if (!trd.isFound()) {
                    player.sendMessage(plugin.pluginName + "Could not find the door pressure plate! Check the seed block position.");
                    return;
                }
                COMPASS d = trd.getCompass();
                BlockFace facing = trd.getFace();
                // get clicked block location
                Location l = block.getRelative(facing, 3).getLocation();
                // get the TARDIS id
                HashMap<String, Object> whereid = new HashMap<String, Object>();
                whereid.put("owner", playerNameStr);
                ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false);
                if (rs.resultSet()) {
                    int id = rs.getTardis_id();
                    TARDISRoomRemover remover = new TARDISRoomRemover(plugin, r, l, d, id);
                    if (remover.remove()) {
                        plugin.trackJettison.remove(playerNameStr);
                        block.setType(Material.AIR);
                        l.getWorld().playEffect(l, Effect.POTION_BREAK, 9);
                        // ok they clicked it, so give them their energy!
                        int amount = Math.round((plugin.getArtronConfig().getInt("jettison") / 100F) * plugin.getRoomsConfig().getInt("rooms." + r + ".cost"));
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("owner", playerNameStr);
                        qf.alterEnergyLevel("tardis", amount, set, player);
                        // if it is a secondary console room remove the controls
                        if (r.equals("BAKER") || r.equals("WOOD")) {
                            int secondary = (r.equals("BAKER")) ? 1 : 2;
                            HashMap<String, Object> del = new HashMap<String, Object>();
                            del.put("tardis_id", id);
                            del.put("secondary", secondary);
                            qf.doDelete("controls", del);
                        }
                        if (plugin.getConfig().getBoolean("growth.return_room_seed")) {
                            // give the player back the room seed block
                            ItemStack is = new ItemStack(Material.getMaterial(plugin.getRoomsConfig().getString("rooms." + r + ".seed")));
                            Inventory inv = player.getInventory();
                            inv.addItem(is);
                            player.updateInventory();
                        }
                        player.sendMessage(plugin.pluginName + "You added " + amount + " to the Artron Energy Capacitor");
                    } else {
                        player.sendMessage(plugin.pluginName + "The room has already been jettisoned!");
                    }
                } else {
                    player.sendMessage(plugin.pluginName + "Could not get TARDIS id!");
                }
            }
        }
    }
}
