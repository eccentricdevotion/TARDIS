/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.ADVANCEMENT;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import multiworld.MultiWorldPlugin;
import multiworld.api.MultiWorldAPI;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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
    private List<String> zero = new ArrayList<>();

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
    
    @EventHandler(ignoreCancelled = true)
    public void onChestClose(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();
        String title = inv.getName();
        if (holder instanceof Chest) {
            if (title.equals("§4Artron Condenser") || title.equals("§4Server Condenser")) {
                final Player player = (Player) event.getPlayer();
                Chest chest = (Chest) holder;
                Location loc = chest.getLocation();
                String chest_loc;
                ResultSetTardis rs;
                boolean isCondenser;
                HashMap<String, Object> where = new HashMap<>();
                if (title.equals("§4Artron Condenser")) {
                    if (plugin.getConfig().getBoolean("preferences.no_creative_condense")) {
                        if (plugin.isMVOnServer() && !plugin.getMVHelper().isWorldSurvival(loc.getWorld())) {
                            TARDISMessage.send(player, "CONDENSE_NO_CREATIVE");
                            return;
                        }
                        if (plugin.getPM().isPluginEnabled("MultiWorld")) {
                            MultiWorldAPI multiworld = ((MultiWorldPlugin) plugin.getPM().getPlugin("MultiWorld")).getApi();
                            if (multiworld.isCreativeWorld(loc.getWorld().getName())) {
                                TARDISMessage.send(player, "CONDENSE_NO_CREATIVE");
                                return;
                            }
                        }
                    }
                    chest_loc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
                    where.put("condenser", chest_loc);
                    rs = new ResultSetTardis(plugin, where, "", false, 0);
                    isCondenser = rs.resultSet();
                } else {
                    chest_loc = loc.toString();
                    where.put("uuid", player.getUniqueId().toString());
                    rs = new ResultSetTardis(plugin, where, "", false, 0);
                    isCondenser = (plugin.getArtronConfig().contains("condenser") && plugin.getArtronConfig().getString("condenser").equals(chest_loc) && rs.resultSet());
                }
                if (isCondenser) {
                    try {
                        Class.forName("org.bukkit.Sound");
                        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
                    } catch (ClassNotFoundException e) {
                        loc.getWorld().playEffect(loc, Effect.CLICK2, 0);
                    }
                    QueryFactory qf = new QueryFactory(plugin);
                    int amount = 0;
                    // get the stacks in the inventory
                    HashMap<String, Integer> item_counts = new HashMap<>();
                    for (ItemStack is : inv.getContents()) {
                        if (is != null) {
                            String item = is.getType().toString();
                            if (plugin.getCondensables().containsKey(item) && !zero.contains(item)) {
                                int stack_size = is.getAmount();
                                amount += stack_size * plugin.getCondensables().get(item);
                                String block_data = is.getType().toString();
                                if (plugin.getConfig().getBoolean("growth.rooms_require_blocks") || plugin.getConfig().getBoolean("allow.repair")) {
                                    if (item_counts.containsKey(block_data)) {
                                        Integer add_this = (item_counts.get(block_data) + stack_size);
                                        item_counts.put(block_data, add_this);
                                    } else {
                                        item_counts.put(block_data, stack_size);
                                    }
                                }
                                inv.remove(is);
                            } else {
                                // return items that can't be condensed
                                player.getInventory().addItem(is);
                                player.updateInventory();
                            }
                        }
                    }
                    Tardis tardis = rs.getTardis();
                    // process item_counts
                    if (plugin.getConfig().getBoolean("growth.rooms_require_blocks") || plugin.getConfig().getBoolean("allow.repair")) {
                        item_counts.entrySet().forEach((map) -> {
                            // check if the tardis has condensed this material before
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("tardis_id", tardis.getTardis_id());
                            wherec.put("block_data", map.getKey());
                            ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
                            HashMap<String, Object> setc = new HashMap<>();
                            if (rsc.resultSet()) {
                                int new_stack_size = (int) map.getValue() + rsc.getBlock_count();
                                qf.updateCondensedBlockCount(new_stack_size, tardis.getTardis_id(), map.getKey());
                            } else {
                                setc.put("tardis_id", tardis.getTardis_id());
                                setc.put("block_data", map.getKey());
                                setc.put("block_count", map.getValue());
                                qf.doInsert("condenser", setc);
                            }
                        });
                    }
                    // halve it cause 1:1 is too much...
                    amount = Math.round(amount / 2.0F);
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", tardis.getTardis_id());
                    qf.alterEnergyLevel("tardis", amount, wheret, player);
                    if (amount > 0) {
                        // are we doing an achievement?
                        if (plugin.getAchievementConfig().getBoolean("energy.enabled")) {
                            // determine the current percentage
                            int current_level = tardis.getArtron_level() + amount;
                            int fc = plugin.getArtronConfig().getInt("full_charge");
                            int percent = Math.round((current_level * 100F) / fc);
                            TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, ADVANCEMENT.ENERGY, 1);
                            if (percent >= plugin.getAchievementConfig().getInt("energy.required")) {
                                taf.doAchievement(percent);
                            } else {
                                taf.doAchievement(Math.round((amount * 100F) / fc));
                            }
                        }
                        TARDISMessage.send(player, "ENERGY_CONDENSED", String.format("%d", amount));
                    } else {
                        TARDISMessage.send(player, "CONDENSE_NO_VALID");
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onChestOpen(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b != null && b.getType().equals(Material.CHEST) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Location loc = b.getLocation();
            String chest_loc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
            HashMap<String, Object> where = new HashMap<>();
            where.put("condenser", chest_loc);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                event.setCancelled(true);
                openCondenser(b, event.getPlayer(), "Artron Condenser");
            } else {
                // is it the server condenser
                if (!plugin.getArtronConfig().contains("condenser")) {
                    return;
                }
                if (plugin.getArtronConfig().getString("condenser").equals(loc.toString())) {
                    event.setCancelled(true);
                    openCondenser(b, event.getPlayer(), "Server Condenser");
                }
            }
        }
    }

    private void openCondenser(Block b, Player p, String title) {
        InventoryHolder holder = (Chest) b.getState();
        ItemStack[] is = holder.getInventory().getContents();
        // check inv size
        int inv_size = (is.length > 27) ? 54 : 27;
        holder.getInventory().clear();
        Inventory aec = plugin.getServer().createInventory(holder, inv_size, "§4" + title);
        // set the contents to what was in the chest
        aec.setContents(is);
        try {
            Class.forName("org.bukkit.Sound");
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        } catch (ClassNotFoundException e) {
            b.getLocation().getWorld().playEffect(b.getLocation(), Effect.CLICK1, 0);
        }
        p.openInventory(aec);
    }
}
