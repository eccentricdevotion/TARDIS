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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author eccentric_nz
 */
public class TARDISRoomSeeder implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISRoomSeeder(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with one of the blocks required to seed a
     * room. If the block is clicked with the TARDIS key after running the
     * command /tardis room [room type], the seed block will start growing into
     * a passageway and the room type specified.
     *
     * Requires the TARDIS to have sufficient Artron Energy to grow the room.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSeedBlockInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        String playerNameStr = player.getName();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            ItemStack inhand = player.getItemInHand();
            // only proceed if they are clicking a seed block with the TARDIS key!
            if (plugin.seeds.containsKey(blockType) && inhand.getType().equals(Material.valueOf(plugin.getConfig().getString("key")))) {
                // check that player is in TARDIS
                if (!plugin.trackRoomSeed.containsKey(playerNameStr)) {
                    return;
                }
                // get schematic
                String r = plugin.seeds.get(blockType);
                // check that the blockType is the same as the one they ran the /tardis room [type] command for
                if (!plugin.trackRoomSeed.get(playerNameStr).equals(r)) {
                    player.sendMessage(plugin.pluginName + "That is not the correct seed block to grow a " + r + " room!");
                    return;
                }
                // get clicked block location
                Location b = block.getLocation();
                // get player's direction
                COMPASS d = COMPASS.valueOf(plugin.utils.getPlayersDirection(player));
                TARDISRoomBuilder builder = new TARDISRoomBuilder(plugin, r, b, d, player);
                if (builder.build()) {
                    // ok they clicked it, so take their energy!
                    int amount = plugin.getConfig().getInt("rooms." + r + ".cost");
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("owner", playerNameStr);
                    qf.alterEnergyLevel("tardis", -amount, set, player);
                }
            }
        }
    }
}