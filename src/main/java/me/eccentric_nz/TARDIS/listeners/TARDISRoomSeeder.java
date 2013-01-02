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
import me.eccentric_nz.TARDIS.TARDISConstants.ROOM;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
            if (seeds.containsKey(blockType) && inhand.getType().equals(Material.valueOf(plugin.getConfig().getString("key")))) {
                // check that player is in TARDIS
                if (!plugin.trackRoomSeed.containsKey(playerNameStr)) {
                    return;
                }
                // get schematic
                String r = seeds.get(blockType);
                // check that the blockType is the same as the one they ran the /tardis room [type] command for
                if (!plugin.trackRoomSeed.get(playerNameStr).equals(r)) {
                    player.sendMessage(plugin.pluginName + "That is not the correct seed block to grow a " + r + " room!");
                    return;
                }
                // get clicked block location
                Location b = block.getLocation();
                // get player's direction
                COMPASS d = COMPASS.valueOf(plugin.utils.getPlayersDirection(player));
                // get start locations
                switch (d) {
                    case NORTH:
                        if (r.equalsIgnoreCase("PASSAGE")) {
                            b.setX(b.getX() - 4);
                        } else {
                            b.setX(b.getX() - 6);
                            b.setZ(b.getZ() - 12);
                        }
                        break;
                    case WEST:
                        if (r.equalsIgnoreCase("PASSAGE")) {
                            b.setZ(b.getZ() + 4);
                        } else {
                            b.setX(b.getX() - 12);
                            b.setZ(b.getZ() - 6);
                        }
                        break;
                    case SOUTH:
                        if (r.equalsIgnoreCase("PASSAGE")) {
                            b.setX(b.getX() + 4);
                        } else {
                            b.setX(b.getX() - 6);
                        }
                        break;
                    default:
                        if (r.equalsIgnoreCase("PASSAGE")) {
                            b.setZ(b.getZ() - 4);
                        } else {
                            b.setZ(b.getZ() - 6);
                        }
                        break;
                }
                b.setY(b.getY() - 4);
                TARDISRoomBuilder builder = new TARDISRoomBuilder(plugin, ROOM.valueOf(r), b, d, player);
                builder.build();
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
