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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.COMPASS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementNotify;
import me.eccentric_nz.TARDIS.achievement.TARDISXPRewarder;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.rooms.TARDISCondenserData;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomBuilder;
import me.eccentric_nz.tardischunkgenerator.TARDISChunkGenerator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * The Doctor kept some of the clothes from his previous regenerations, as well
 * as clothing for other people in the TARDIS wardrobe. At least some of the
 * clothes had pockets that were bigger on the inside.
 *
 * @author eccentric_nz
 */
public class TARDISRoomSeeder implements Listener {

    private final TARDIS plugin;

    public TARDISRoomSeeder(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with one of the blocks required to seed a
     * room. If the block is clicked with the TARDIS key after running the
     * command /tardis room [room type], the seed block will start growing into
     * a passageway or the room type specified.
     *
     * Requires the TARDIS to have sufficient Artron Energy to grow the room.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSeedBlockInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        String playerNameStr = player.getName();
        // check that player is in TARDIS
        if (!plugin.trackRoomSeed.containsKey(playerNameStr)) {
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
                key = (!rsp.getKey().equals("")) ? rsp.getKey() : plugin.getConfig().getString("key");
            } else {
                key = plugin.getConfig().getString("key");
            }
            // only proceed if they are clicking a seed block with the TARDIS key!
            if (plugin.seeds.containsKey(blockType) && inhand.equals(Material.getMaterial(key))) {
                // check they are still in the TARDIS world
                World world = block.getLocation().getWorld();
                String name = world.getName();
                ChunkGenerator gen = world.getGenerator();
                boolean special = name.contains("TARDIS_TimeVortex") && (world.getWorldType().equals(WorldType.FLAT) || gen instanceof TARDISChunkGenerator);
                if (!name.equals("TARDIS_WORLD_" + playerNameStr) && !special) {
                    player.sendMessage(plugin.pluginName + "You must be in a TARDIS world to grow a room!");
                    return;
                }
                // get schematic
                String r = plugin.seeds.get(blockType);
                // check that the blockType is the same as the one they ran the /tardis room [type] command for
                if (!plugin.trackRoomSeed.get(playerNameStr).equals(r)) {
                    player.sendMessage(plugin.pluginName + "That is not the correct seed block to grow a " + plugin.trackRoomSeed.get(playerNameStr) + "!");
                    return;
                }
                // get clicked block location
                Location b = block.getLocation();
                // get player's direction
                COMPASS d = COMPASS.valueOf(plugin.utils.getPlayersDirection(player));
                TARDISRoomBuilder builder = new TARDISRoomBuilder(plugin, r, b, d, player);
                if (builder.build()) {
                    plugin.trackRoomSeed.remove(playerNameStr);
                    // ok they clicked it, so take their energy!
                    int amount = plugin.getConfig().getInt("rooms." + r + ".cost");
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("owner", playerNameStr);
                    qf.alterEnergyLevel("tardis", -amount, set, player);
                    // remove blocks from condenser table if rooms_require_blocks is true
                    if (plugin.getConfig().getBoolean("rooms_require_blocks")) {
                        TARDISCondenserData c_data = plugin.roomCondenserData.get(playerNameStr);
                        for (Map.Entry<String, Integer> entry : c_data.getBlockIDCount().entrySet()) {
                            HashMap<String, Object> wherec = new HashMap<String, Object>();
                            wherec.put("tardis_id", c_data.getTardis_id());
                            wherec.put("block_data", entry.getKey());
                            qf.alterCondenserBlockCount(entry.getValue(), wherec);
                        }
                        plugin.roomCondenserData.remove(playerNameStr);
                    }
                    // are we doing an achievement?
                    if (plugin.ayml.getBoolean("rooms.enabled")) {
                        // have they started the achievement?
                        HashMap<String, Object> wherea = new HashMap<String, Object>();
                        wherea.put("player", playerNameStr);
                        wherea.put("name", "rooms");
                        wherea.put("completed", 0);
                        ResultSetAchievements rsa = new ResultSetAchievements(plugin, where, false);
                        HashMap<String, Object> seta = new HashMap<String, Object>();
                        if (rsa.resultSet()) {
                            // have they grown this room type before?
                            List<String> data = Arrays.asList(rsa.getAmount().split(":"));
                            HashMap<String, Object> wherem = new HashMap<String, Object>();
                            wherem.put("a_id", rsa.getA_id());
                            // check if this is the last room type
                            if ((data.size() + 1) == plugin.seeds.size()) {
                                // award achievement!
                                int reward_amount = plugin.ayml.getInt("rooms.reward_amount");
                                String reward_type = plugin.ayml.getString("rooms.reward_type");
                                TARDISAchievementNotify tan = new TARDISAchievementNotify(plugin);
                                tan.sendAchievement(player, plugin.ayml.getString("rooms.message"), Material.valueOf(plugin.ayml.getString("rooms.icon")));
                                if (reward_type.equalsIgnoreCase("XP")) {
                                    TARDISXPRewarder txr = new TARDISXPRewarder(player);
                                    txr.changeExp(reward_amount);
                                } else {
                                    ItemStack is = new ItemStack(Material.valueOf(reward_type), reward_amount);
                                    Inventory inv = player.getInventory();
                                    HashMap<Integer, ItemStack> excess = inv.addItem(is);
                                    for (Map.Entry<Integer, ItemStack> me : excess.entrySet()) {
                                        player.getWorld().dropItem(player.getLocation(), me.getValue());
                                    }
                                }
                                // set achievement as done
                                seta.put("completed", 1);
                            } else if (!data.contains(r)) {
                                seta.put("amount", rsa.getAmount() + ":" + r);
                            }
                            qf.doUpdate("achievements", seta, wherem);
                        } else {
                            // is it an auto achievement?
                            if (plugin.ayml.getBoolean("rooms.auto")) {
                                // insert a new record
                                seta.put("player", player.getName());
                                seta.put("name", "rooms");
                                seta.put("amount", r);
                                qf.doInsert("achievements", seta);
                            }
                        }
                    }
                }
            }
        }
    }
}
