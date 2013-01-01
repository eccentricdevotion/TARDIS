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
import java.util.Set;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRoomSeeder {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();
    HashMap<Material, String> seeds;

    public TARDISRoomSeeder(TARDIS plugin) {
        this.plugin = plugin;
        this.seeds = getSeeds();
    }

    /**
     * Listens for player interaction with one of the blocks required to seed a
     * room. If the block is clicked with the TARDIS key after running the
     * command /tardis room [room type], the seed block will start growing into
     * a passageway and the room type specified.
     *
     * Requires the TARDIS to have sufficient Artron Energy to grow the room.
     *
     * @author eccentric_nz
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCapacitorInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            // only proceed if they are clicking a button!
            if (seeds.containsKey(blockType)) {
                // get clicked block location
                Location b = block.getLocation();
                // get player's direction
                COMPASS d = COMPASS.valueOf(plugin.utils.getPlayersDirection(player));
                // get schematic
                String r = seeds.get(blockType);
                // get start locations
                switch (d) {
                    case NORTH:

                        break;
                    case SOUTH:

                        break;
                    case WEST:

                        break;
                    default:

                        break;
                }
            }
        }
    }

    private HashMap<Material, String> getSeeds() {
        HashMap<Material, String> map = new HashMap<Material, String>();
        Set<String> rooms = plugin.getConfig().getConfigurationSection("rooms").getKeys(false);
        for (String s : rooms) {
            Material m = Material.valueOf(plugin.getConfig().getString("rooms." + s + ".seed"));
            map.put(m, s);
        }
        return map;
    }
}
