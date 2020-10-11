/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.blueprints.BlueprintProcessor;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import multiworld.MultiWorldPlugin;
import multiworld.api.MultiWorldAPI;
import org.bukkit.ChatColor;
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
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * Following his disrupted resurrection, the Master was able to offensively use energy - presumably his own artron
 * energy - to strike his enemies with debilitating energy blasts, at the cost of reducing his own life force.
 *
 * @author eccentric_nz
 */
public class TARDISCondenserListener implements Listener {

    private final TARDIS plugin;
    private final List<String> zero;

    public TARDISCondenserListener(TARDIS plugin) {
        this.plugin = plugin;
        zero = this.plugin.getBlocksConfig().getStringList("no_artron_value");
    }

    /**
     * Listens for player interaction with the TARDIS condenser chest. When the chest is closed, any DIRT, SAND, GRAVEL,
     * COBBLESTONE or ROTTEN FLESH is converted to Artron Energy at a ratio of 1:1.
     *
     * @param event a chest closing
     */
    @EventHandler(ignoreCancelled = true)
    public void onChestClose(InventoryCloseEvent event) {
        InventoryView view = event.getView();
        InventoryHolder holder = event.getInventory().getHolder();
        String title = view.getTitle();
        if (holder instanceof Chest) {
            if (title.equals(ChatColor.DARK_RED + "Artron Condenser") || title.equals(ChatColor.DARK_RED + "Server Condenser")) {
                Player player = (Player) event.getPlayer();
                Chest chest = (Chest) holder;
                Location loc = chest.getLocation();
                String chest_loc = loc.toString();
                ResultSetTardis rs;
                boolean isCondenser;
                HashMap<String, Object> where = new HashMap<>();
                if (title.equals(ChatColor.DARK_RED + "Artron Condenser")) {
                    if (plugin.getConfig().getBoolean("preferences.no_creative_condense")) {
                        switch (plugin.getWorldManager()) {
                            case MULTIVERSE:
                                if (!plugin.getMVHelper().isWorldSurvival(loc.getWorld())) {
                                    TARDISMessage.send(player, "CONDENSE_NO_CREATIVE");
                                    return;
                                }
                                break;
                            case MULTIWORLD:
                                MultiWorldAPI multiworld = ((MultiWorldPlugin) plugin.getPM().getPlugin("MultiWorld")).getApi();
                                if (multiworld.isCreativeWorld(loc.getWorld().getName())) {
                                    TARDISMessage.send(player, "CONDENSE_NO_CREATIVE");
                                    return;
                                }
                                break;
                            case NONE:
                                if (plugin.getPlanetsConfig().getString("planets." + loc.getWorld().getName() + ".gamemode").equalsIgnoreCase("CREATIVE")) {
                                    TARDISMessage.send(player, "CONDENSE_NO_CREATIVE");
                                    return;
                                }
                                break;
                        }
                    }
                    where.put("type", 34);
                    where.put("location", chest_loc);
                    ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                    if (rsc.resultSet()) {
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", rsc.getTardis_id());
                        rs = new ResultSetTardis(plugin, wheret, "", false, 0);
                        isCondenser = rs.resultSet();
                    } else {
                        return;
                    }
                } else {
                    where.put("uuid", player.getUniqueId().toString());
                    rs = new ResultSetTardis(plugin, where, "", false, 0);
                    isCondenser = (plugin.getArtronConfig().contains("condenser") && plugin.getArtronConfig().getString("condenser").equals(chest_loc) && rs.resultSet());
                }
                if (isCondenser) {
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1, 1);
                    int amount = 0;
                    // get the stacks in the inventory
                    HashMap<String, Integer> item_counts = new HashMap<>();
                    Inventory inv = event.getInventory();
                    for (ItemStack is : inv.getContents()) {
                        if (is != null) {
                            String item = is.getType().toString();
                            if (plugin.getCondensables().containsKey(item) && !zero.contains(item)) {
                                int stack_size = is.getAmount();
                                if (item.equals("BLAZE_ROD") && isSonic(is)) {
                                    List<String> lore = is.getItemMeta().getLore();
                                    double full = plugin.getArtronConfig().getDouble("full_charge") / 75.0d;
                                    amount += plugin.getArtronConfig().getDouble("sonic_generator.standard") * full;
                                    if (lore.contains("Bio-scanner Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
                                    }
                                    if (lore.contains("Diamond Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.diamond") * full);
                                    }
                                    if (lore.contains("Emerald Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.emerald") * full);
                                    }
                                    if (lore.contains("Redstone Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
                                    }
                                    if (lore.contains("Painter Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.painter") * full);
                                    }
                                    if (lore.contains("Ignite Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.ignite") * full);
                                    }
                                    if (lore.contains("Pickup Arrows Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.arrow") * full);
                                    }
                                    if (lore.contains("Knockback Upgrade")) {
                                        amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.knockback") * full);
                                    }
                                } else if (item.equals("MUSIC_DISC_MELLOHI") && isBlueprint(is)) {
                                    // blueprint disk
                                    BlueprintProcessor.addPermission(plugin, is, player);
                                    amount += plugin.getCondensables().get(item);
                                } else {
                                    amount += stack_size * plugin.getCondensables().get(item);
                                }
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
                    if (tardis != null) {
                        // process item_counts
                        if (plugin.getConfig().getBoolean("growth.rooms_require_blocks") || plugin.getConfig().getBoolean("allow.repair")) {
                            item_counts.forEach((key, value) -> {
                                // check if the tardis has condensed this material before
                                HashMap<String, Object> wherec = new HashMap<>();
                                wherec.put("tardis_id", tardis.getTardis_id());
                                wherec.put("block_data", key);
                                ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
                                HashMap<String, Object> setc = new HashMap<>();
                                if (rsc.resultSet()) {
                                    int new_stack_size = value + rsc.getBlock_count();
                                    plugin.getQueryFactory().updateCondensedBlockCount(new_stack_size, tardis.getTardis_id(), key);
                                } else {
                                    setc.put("tardis_id", tardis.getTardis_id());
                                    setc.put("block_data", key);
                                    setc.put("block_count", value);
                                    plugin.getQueryFactory().doInsert("condenser", setc);
                                }
                            });
                        }
                        // halve it cause 1:1 is too much...
                        amount = Math.round(amount / 2.0F);
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", tardis.getTardis_id());
                        plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wheret, player);
                        if (amount > 0) {
                            // are we doing an achievement?
                            if (plugin.getAchievementConfig().getBoolean("energy.enabled")) {
                                // determine the current percentage
                                int current_level = tardis.getArtron_level() + amount;
                                int fc = plugin.getArtronConfig().getInt("full_charge");
                                int percent = Math.round((current_level * 100F) / fc);
                                TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, Advancement.ENERGY, 1);
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
                    } else {
                        plugin.debug("TARDIS data was null!");
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
            String chest_loc = loc.toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("type", 34);
            where.put("location", chest_loc);
            ResultSetControls rsc = new ResultSetControls(plugin, where, false);
            if (rsc.resultSet()) {
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
        Inventory aec = plugin.getServer().createInventory(holder, inv_size, ChatColor.DARK_RED + "" + title);
        // set the contents to what was in the chest
        aec.setContents(is);
        p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        p.openInventory(aec);
    }

    private boolean isSonic(ItemStack is) {
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {
                return (ChatColor.stripColor(im.getDisplayName()).equals("Sonic Screwdriver"));
            }
        }
        return false;
    }

    private boolean isBlueprint(ItemStack is) {
        if (!plugin.getConfig().getBoolean("blueprints.enabled")) {
            return false;
        }
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {
                return im.getDisplayName().equals("TARDIS Blueprint Disk");
            }
        }
        return false;
    }
}
