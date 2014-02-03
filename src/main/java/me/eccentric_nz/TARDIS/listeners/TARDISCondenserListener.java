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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Following his disrupted resurrection, the Master was able to offensively use
 * energy - presumably his own artron energy - to strike his enemies with
 * debilitating energy blasts, at the cost of reducing his own life force.
 *
 * @author eccentric_nz
 */
public class TARDISCondenserListener implements Listener {

    private final TARDIS plugin;
    private List<String> zero = new ArrayList<String>();

    public TARDISCondenserListener(TARDIS plugin) {
        this.plugin = plugin;
        zero = this.plugin.getBlocksConfig().getStringList("no_artron_value");
    }

    /**
     * Listens for player interaction with the TARDIS condenser chest. When the
     * chest is closed, any DIRT, SAND, GRAVEL, COBBLESTONE or ROTTEN FLESH is
     * converted to Artron Energy at a ratio of 1:1.
     *
     * @param event a chest closing
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        if (holder instanceof Chest && inv.getName().equals("§4Artron Condenser")) {
            Chest chest = (Chest) holder;
            Location loc = chest.getLocation();
            String chest_loc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("condenser", chest_loc);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                final Player player = (Player) event.getPlayer();
                try {
                    Class.forName("org.bukkit.Sound");
                    player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1, 1);
                } catch (ClassNotFoundException e) {
                    loc.getWorld().playEffect(loc, Effect.CLICK2, 0);
                }
                QueryFactory qf = new QueryFactory(plugin);
                int amount = 0;
                // get the stacks in the inventory
                HashMap<Integer, Integer> item_counts = new HashMap<Integer, Integer>();
                for (ItemStack is : inv.getContents()) {
                    if (is != null) {
                        String item = is.getType().name();
                        if (plugin.getCondensables().containsKey(item)) {
                            int stack_size = is.getAmount();
                            if (!zero.contains(item)) {
                                amount += stack_size * plugin.getCondensables().get(item);
                            }
                            int block_data = is.getTypeId();
                            if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                                if (item_counts.containsKey(Integer.valueOf(block_data))) {
                                    Integer add_this = (item_counts.get(Integer.valueOf(block_data)) + Integer.valueOf(stack_size));
                                    item_counts.put(Integer.valueOf(block_data), add_this);
                                } else {
                                    item_counts.put(Integer.valueOf(block_data), Integer.valueOf(stack_size));
                                }
                            }
                            inv.remove(is);
                        } else {
                            // return items that can't be condensed
                            player.getInventory().addItem(is);
                        }
                    }
                }
                // process item_counts
                if (plugin.getConfig().getBoolean("growth.rooms_require_blocks")) {
                    for (Map.Entry<Integer, Integer> map : item_counts.entrySet()) {
                        // check if the tardis has condensed this material before
                        HashMap<String, Object> wherec = new HashMap<String, Object>();
                        wherec.put("tardis_id", rs.getTardis_id());
                        wherec.put("block_data", (int) map.getKey());
                        ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
                        HashMap<String, Object> setc = new HashMap<String, Object>();
                        if (rsc.resultSet()) {
                            int new_stack_size = (int) map.getValue() + rsc.getBlock_count();
                            qf.updateCondensedBlockCount(new_stack_size, rs.getTardis_id(), (int) map.getKey());
                        } else {
                            setc.put("tardis_id", rs.getTardis_id());
                            setc.put("block_data", (int) map.getKey());
                            setc.put("block_count", (int) map.getValue());
                            qf.doInsert("condenser", setc);
                        }
                    }
                }
                // halve it cause 1:1 is too much...
                amount = Math.round(amount / 2.0F);
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", rs.getTardis_id());
                qf.alterEnergyLevel("tardis", amount, wheret, player);
                String message;
                if (amount > 0) {
                    message = "You condensed the molecules of the universe itself into " + amount + " artron energy!";
                    // are we doing an achievement?
                    if (plugin.getAchievementConfig().getBoolean("energy.enabled")) {
                        // determine the current percentage
                        int current_level = rs.getArtron_level() + amount;
                        int fc = plugin.getArtronConfig().getInt("full_charge");
                        int percent = Math.round((current_level * 100F) / fc);
                        TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, "energy", 1);
                        if (percent >= plugin.getAchievementConfig().getInt("energy.required")) {
                            taf.doAchievement(percent);
                        } else {
                            taf.doAchievement(Math.round((amount * 100F) / fc));
                        }
                    }
                } else {
                    message = "There were no valid materials to condense!";
                }
                player.sendMessage(plugin.getPluginName() + message);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChestOpen(PlayerInteractEvent event) {
        Block b = event.getClickedBlock();
        if (b != null && b.getType().equals(Material.CHEST) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Location loc = b.getLocation();
            String chest_loc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("condenser", chest_loc);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                event.setCancelled(true);
                InventoryHolder holder = (Chest) b.getState();
                // the chest may have been filled by a hopper so get its contents and then clear it
                ItemStack[] is = holder.getInventory().getContents();
                // check inv size
                int inv_size = (is.length > 27) ? 54 : 27;
                holder.getInventory().clear();
                Inventory aec = plugin.getServer().createInventory(holder, inv_size, "§4Artron Condenser");
                // set the contents to what was in the chest
                aec.setContents(is);
                Player p = event.getPlayer();
                try {
                    Class.forName("org.bukkit.Sound");
                    p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 1);
                } catch (ClassNotFoundException e) {
                    loc.getWorld().playEffect(loc, Effect.CLICK1, 0);
                }
                p.openInventory(aec);
            }
        }
    }
}
