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
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.artron.TARDISCondensables;
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

    public TARDISCondenserListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player interaction with the TARDIS condenser chest. When the
     * chest is closed, any DIRT, SAND, GRAVEL, COBBLESTONE or ROTTEN FLESH is
     * converted to Artron Energy at a ratio of 1:1.
     *
     * @param event a chest closing
     */
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
                TARDISCondensables tc = new TARDISCondensables();
                // get the stacks in the inventory
                for (ItemStack is : inv.getContents()) {
                    if (is != null) {
                        String item = is.getType().name();
                        if (tc.condensables.containsKey(item)) {
                            int stack_size = is.getAmount();
                            amount += stack_size * tc.condensables.get(item);
                            inv.remove(is);
                            if (plugin.getConfig().getBoolean("rooms_require_blocks")) {
                                String block_data = String.format("%s", is.getTypeId());
                                // check if the tardis has condensed this material before
                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                wherec.put("tardis_id", rs.getTardis_id());
                                wherec.put("block_data", block_data);
                                ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec, false);
                                HashMap<String, Object> setc = new HashMap<String, Object>();
                                if (rsc.resultSet()) {
                                    setc.put("block_count", stack_size + rsc.getBlock_count());
                                    HashMap<String, Object> wheret = new HashMap<String, Object>();
                                    wheret.put("tardis_id", rs.getTardis_id());
                                    wheret.put("block_data", block_data);
                                    qf.doUpdate("condenser", setc, wheret);
                                } else {
                                    setc.put("tardis_id", rs.getTardis_id());
                                    setc.put("block_data", block_data);
                                    setc.put("block_count", stack_size);
                                    qf.doInsert("condenser", setc);
                                }
                            }
                        } else {
                            // return items that can't be condensed
                            player.getInventory().addItem(is);
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
                    if (plugin.getAchivementConfig().getBoolean("energy.enabled")) {
                        // determine the current percentage
                        int current_level = rs.getArtron_level() + amount;
                        int fc = plugin.getArtronConfig().getInt("full_charge");
                        int percent = Math.round((current_level * 100F) / fc);
                        TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, "energy", 1);
                        if (percent >= plugin.getAchivementConfig().getInt("energy.required")) {
                            taf.doAchievement(percent);
                        } else {
                            taf.doAchievement(Math.round((amount * 100F) / fc));
                        }
                    }
                } else {
                    message = "There were no valid materials to condense!";
                }
                player.sendMessage(plugin.pluginName + message);
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
                Inventory aec = plugin.getServer().createInventory(holder, 27, "§4Artron Condenser");
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
