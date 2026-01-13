/*
 * Copyright (C) 2026 eccentric_nz
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

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.blueprints.BlueprintProcessor;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronStorageAndLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCondenser;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Advancement;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Following his disrupted resurrection, the Master was able to offensively use energy - presumably his own artron
 * energy - to strike his enemies with debilitating energy blasts, at the cost of reducing his own life force.
 *
 * @author eccentric_nz
 */
public class ArtronCondenserListener implements Listener {

    private final TARDIS plugin;
    private final List<String> zero;

    public ArtronCondenserListener(TARDIS plugin) {
        this.plugin = plugin;
        zero = this.plugin.getBlocksConfig().getStringList("no_artron_value");
    }

    /**
     * Listens for player interaction with the TARDIS condenser chest. When the chest is closed, any condensable items
     * {@see TARDISCondensables} are converted to Artron Energy at a ratio of 2:1 e.g. 2 COBBLESTONE gives 1 Artron
     * energy.
     *
     * @param event a chest closing
     */
    @EventHandler(ignoreCancelled = true)
    public void onCondenserClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (holder instanceof ArtronCondenserInventory chest) {
            if (!chest.getTitle().equals("Artron Condenser") && !chest.getTitle().equals("Server Condenser")) {
                return;
            }
            Player player = (Player) event.getPlayer();
            Location location = chest.getLocation();
            String chestLocation = location.toString();
            ResultSetTardis rs;
            boolean isCondenser;
            HashMap<String, Object> where = new HashMap<>();
            if (chest.getTitle().equals("Artron Condenser")) {
                if (plugin.getConfig().getBoolean("preferences.no_creative_condense")) {
                    switch (plugin.getWorldManager()) {
                        case MULTIVERSE -> {
                            if (!plugin.getMVHelper().isWorldSurvival(location.getWorld())) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "CONDENSE_NO_CREATIVE");
                                return;
                            }
                        }
                        case NONE -> {
                            if (plugin.getPlanetsConfig().getString("planets." + location.getWorld().getName() + ".gamemode", "SURVIVAL").equalsIgnoreCase("CREATIVE")) {
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "CONDENSE_NO_CREATIVE");
                                return;
                            }
                        }
                    }
                }
                where.put("type", 34);
                where.put("location", chestLocation);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", rsc.getTardis_id());
                    rs = new ResultSetTardis(plugin, wheret, "", false);
                    isCondenser = rs.resultSet();
                } else {
                    return;
                }
            } else {
                where.put("uuid", player.getUniqueId().toString());
                rs = new ResultSetTardis(plugin, where, "", false);
                isCondenser = (plugin.getArtronConfig().contains("condenser") && plugin.getArtronConfig().getString("condenser", "").equals(chestLocation) && rs.resultSet());
            }
            if (!isCondenser) {
                return;
            }
            player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 0.5f, 1);
            int amount = 0;
            // non-condensable items we need to return to the player
            ArrayList<ItemStack> returnedItems = new ArrayList<>();
            // how many items we neglected to condense due to them being enchanted
            int savedEnchantedItems = 0;
            // get the stacks in the inventory
            HashMap<String, Integer> item_counts = new HashMap<>();
            Inventory inv = event.getInventory();
            ItemStack[] contents = inv.getContents();
            for (ItemStack is : contents) {
                // skip empty slots
                if (is == null) {
                    continue;
                }
                String item = is.getType().toString();
                // condense sonic screwdriver
                if (item.equals("BLAZE_ROD") && TARDISStaticUtils.isSonic(is)) {
                    // add artron for base screwdriver
                    double full = plugin.getArtronConfig().getDouble("full_charge") / 75.0d;
                    amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.standard") * full);
                    // add extra artron for any sonic upgrades
                    if (is.getItemMeta().hasLore()) {
                        List<Component> lore = is.getItemMeta().lore();
                        if (lore.contains(Component.text("Bio-scanner Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
                        }
                        if (lore.contains(Component.text("Diamond Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.diamond") * full);
                        }
                        if (lore.contains(Component.text("Emerald Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.emerald") * full);
                        }
                        if (lore.contains(Component.text("Redstone Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.bio") * full);
                        }
                        if (lore.contains(Component.text("Painter Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.painter") * full);
                        }
                        if (lore.contains(Component.text("Ignite Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.ignite") * full);
                        }
                        if (lore.contains(Component.text("Pickup Arrows Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.arrow") * full);
                        }
                        if (lore.contains(Component.text("Knockback Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.knockback") * full);
                        }
                        if (lore.contains(Component.text("Brush Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.brush") * full);
                        }
                        if (lore.contains(Component.text("Conversion Upgrade"))) {
                            amount += (int) (plugin.getArtronConfig().getDouble("sonic_generator.conversion") * full);
                        }
                    }
                    inv.remove(is);
                    continue;
                }
                // condense blueprint disk
                if (item.equals("MUSIC_DISC_MELLOHI") && isBlueprint(is)) {
                    BlueprintProcessor.addPermission(plugin, is, player);
                    amount += plugin.getCondensables().get(item);
                    inv.remove(is);
                    continue;
                }
                // don't condense enchanted items so players don't accidentally condense their
                // gear ignores curse enchantments
                if (plugin.getConfig().getBoolean("preferences.no_enchanted_condense")) {
                    if (!is.getEnchantments().keySet().stream().allMatch(ench -> ench.equals(Enchantment.BINDING_CURSE) || ench.equals(Enchantment.VANISHING_CURSE))) {
                        savedEnchantedItems++;
                        returnedItems.add(is);
                        inv.remove(is);
                        continue;
                    }
                }
                // condense other blocks and items
                if (plugin.getCondensables().containsKey(item) && !zero.contains(item)) {
                    int stack_size = is.getAmount();
                    // add item artron value
                    amount += stack_size * plugin.getCondensables().get(item);
                    // count blocks towards room growth and repair if enabled
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
                    continue;
                }
                // give the player the item back since it wasn't condensed
                returnedItems.add(is);
                // remove the item from the condenser
                inv.remove(is);
            }
            // return non-condensed items to the player
            HashMap<Integer, ItemStack> didntFit = player.getInventory().addItem(returnedItems.toArray(new ItemStack[0]));
            player.updateInventory();
            // drop items that didn't fit in the player's inventory on the ground
            for (ItemStack is : didntFit.values()) {
                player.getWorld().dropItem(player.getLocation(), is);
            }
//            }
            Tardis tardis = rs.getTardis();
            if (tardis == null) {
                plugin.debug("TARDIS data was null!");
                return;
            }
            int id = tardis.getTardisId();
            // process item_counts
            if (plugin.getConfig().getBoolean("growth.rooms_require_blocks") || plugin.getConfig().getBoolean("allow.repair")) {
                item_counts.forEach((key, value) -> {
                    // check if the tardis has condensed this material before
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    wherec.put("block_data", key);
                    ResultSetCondenser rsc = new ResultSetCondenser(plugin, wherec);
                    HashMap<String, Object> setc = new HashMap<>();
                    if (rsc.resultSet()) {
                        int new_stack_size = value + rsc.getBlock_count();
                        plugin.getQueryFactory().updateCondensedBlockCount(new_stack_size, id, key);
                    } else {
                        setc.put("tardis_id", id);
                        setc.put("block_data", key);
                        setc.put("block_count", value);
                        plugin.getQueryFactory().doInsert("condenser", setc);
                    }
                });
            }
            // warn players about not condensing enchanted items
            if (savedEnchantedItems > 0) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CONDENSE_NO_ENCHANTED", Integer.toString(savedEnchantedItems));
            }
            // halve it cause 1:1 is too much...
            amount = Math.round(amount / 2.0F);
            // only add energy up to capacitors * max level - damage
            ResultSetArtronStorageAndLevel rsas = new ResultSetArtronStorageAndLevel(plugin);
            int full = plugin.getArtronConfig().getInt("full_charge", 5000);
            if (rsas.fromID(id)) {
                int damage = (full / 2) * rsas.getDamageCount();
                int max = (full * rsas.getCapacitorCount()) - damage;
                int current = rsas.getCurrentLevel();
                if (current + amount <= max) {
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("tardis_id", id);
                    plugin.getQueryFactory().alterEnergyLevel("tardis", amount, wheret, player);
                } else {
                    int toMax = max - current;
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_CONDENSE", max);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_TRY", toMax);
                    if (rsas.getCapacitorCount() > 1) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_ADD");
                    } else {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_ADD_ROOM");
                    }
                    // give artron cells back
                    giveBack(player, amount, full, contents);
                    return;
                }
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_NOT_FOUND");
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CAPACITOR_ADD_ROOM");
                // give artron cells back
                giveBack(player, amount, full, contents);
                return;
            }
            if (amount > 0) {
                // are we doing an achievement?
                if (plugin.getAchievementConfig().getBoolean("energy.enabled")) {
                    // determine the current percentage
                    int current_level = tardis.getArtronLevel() + amount;
                    int fc = plugin.getArtronConfig().getInt("full_charge");
                    int percent = Math.round((current_level * 100F) / fc);
                    TARDISAchievementFactory taf = new TARDISAchievementFactory(plugin, player, Advancement.ENERGY, 1);
                    if (percent >= plugin.getAchievementConfig().getInt("energy.required")) {
                        taf.doAchievement(percent);
                    } else {
                        taf.doAchievement(Math.round((amount * 100F) / fc));
                    }
                }
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_CONDENSED", String.format("%d", amount));
            } else {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "CONDENSE_NO_VALID");
            }
        }
    }

    private void giveBack(Player player, int amount, int full, ItemStack[] items) {
        // create an artron storage cell
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get("Artron Storage Cell");
        ItemStack result = recipe.getResult();
        // determine cost per cell
        int cellCost = (plugin.getCraftingDifficulty() == CraftingDifficulty.HARD) ? 406 : 86;
        // calculate how many artron storage cells we should give
        int initialFullCellCount = amount / full;
        int totalCost = cellCost * (initialFullCellCount + 1);
        int remainder = 0;
        int finalFullCellCount = 0;
        // subtract the cost of the cells
        if (amount - totalCost > 0) {
            amount -= totalCost;
            // recalculate number of cells to give
            finalFullCellCount = amount / full;
            remainder = amount % full;
        }
        if (finalFullCellCount == 0 && remainder == 0) {
            // return items
            for (ItemStack item : items) {
                if (item != null) {
                    player.getInventory().addItem(item);
                }
            }
            return;
        }
        if (remainder > 0) {
            // give one partially filled cell
            ItemStack leftover = result.clone();
            ItemMeta lim = leftover.getItemMeta();
            List<Component> lore = lim.lore();
            lore.set(1, Component.text(remainder));
            lim.lore(lore);
            lim.setEnchantmentGlintOverride(true);
            lim.addItemFlags(ItemFlag.values());
            lim.setAttributeModifiers(Multimaps.forMap(Map.of()));
            leftover.setItemMeta(lim);
            player.getInventory().addItem(leftover);
        }
        if (finalFullCellCount > 0) {
            // give full cells
            result.setAmount(finalFullCellCount);
            ItemMeta im = result.getItemMeta();
            List<Component> lore = im.lore();
            lore.set(1, Component.text(full));
            im.lore(lore);
            im.setEnchantmentGlintOverride(true);
            im.addItemFlags(ItemFlag.values());
            im.setAttributeModifiers(Multimaps.forMap(Map.of()));
            result.setItemMeta(im);
            player.getInventory().addItem(result);
        }
        if (finalFullCellCount > 0 || remainder > 0) {
            player.updateInventory();
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
                if (plugin.getArtronConfig().getString("condenser", "").equals(loc.toString())) {
                    event.setCancelled(true);
                    openCondenser(b, event.getPlayer(), "Server Condenser");
                }
            }
        }
    }

    private void openCondenser(Block block, Player player, String title) {
        InventoryHolder holder = (Chest) block.getState();
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 0.5f, 1);
        player.openInventory(new ArtronCondenserInventory(plugin, holder, title, block.getLocation()).getInventory());
    }

    private boolean isBlueprint(ItemStack is) {
        if (!plugin.getConfig().getBoolean("modules.blueprints")) {
            return false;
        }
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasDisplayName()) {
                return ComponentUtils.endsWith(im.displayName(),"TARDIS Blueprint Disk");
            }
        }
        return false;
    }
}
