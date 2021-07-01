/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.event.TARDISClaimEvent;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.move.TARDISDoorCloser;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static me.eccentric_nz.TARDIS.commands.tardis.TARDISAbandonCommand.getSign;

/**
 * The Ninth Doctor used the Cardiff rift to "re-charge" his TARDIS. The process took 2 days.
 *
 * @author eccentric_nz
 */
public class TARDISArtronCapacitorListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validBlocks = new ArrayList<>();

    public TARDISArtronCapacitorListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.add(Material.ACACIA_BUTTON);
        validBlocks.add(Material.BIRCH_BUTTON);
        validBlocks.add(Material.COMPARATOR);
        validBlocks.add(Material.DARK_OAK_BUTTON);
        validBlocks.add(Material.JUNGLE_BUTTON);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.OAK_BUTTON);
        validBlocks.add(Material.SPRUCE_BUTTON);
        validBlocks.add(Material.STONE_BUTTON);
    }

    /**
     * Listens for player interaction with the button on the Artron Energy Capacitor. If the button is right-clicked,
     * then the Artron levels are updated. Clicking with a Nether Star puts the capacitor to maximum, clicking with the
     * TARDIS key initialises the capacitor by spawning a charged creeper inside it and sets the level to 50%. Clicking
     * while sneaking transfers player Artron Energy into the capacitor.
     * <p>
     * If the button is just right-clicked, it displays the current capacitor level as percentage of full.
     *
     * @param event the player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCapacitorInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            // only proceed if they are clicking a button!
            if (validBlocks.contains(blockType)) {
                // we need to get this block's location and then get the tardis_id from it
                String buttonloc = block.getLocation().toString();
                HashMap<String, Object> where = new HashMap<>();
                where.put("type", 6);
                where.put("location", buttonloc);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    int id = rsc.getTardis_id();
                    if (action == Action.RIGHT_CLICK_BLOCK) {
                        // get tardis data
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            if (tardis.getPreset().equals(PRESET.JUNK)) {
                                return;
                            }
                            boolean abandoned = tardis.isAbandoned();
                            HashMap<String, Object> whereid = new HashMap<>();
                            whereid.put("tardis_id", id);
                            int current_level = tardis.getArtron_level();
                            boolean init = tardis.isTardis_init();
                            boolean lights = tardis.isLights_on();
                            int fc = plugin.getArtronConfig().getInt("full_charge");
                            Material item = player.getInventory().getItemInMainHand().getType();
                            Material full = Material.valueOf(plugin.getArtronConfig().getString("full_charge_item"));
                            Material cell = Material.valueOf(plugin.getRecipesConfig().getString("shaped.Artron Storage Cell.result"));
                            // determine key item
                            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, player.getUniqueId().toString());
                            String key;
                            boolean hasPrefs = false;
                            if (rsp.resultSet()) {
                                hasPrefs = true;
                                key = (!rsp.getKey().isEmpty()) ? rsp.getKey() : plugin.getConfig().getString("preferences.key");
                            } else {
                                key = plugin.getConfig().getString("preferences.key");
                            }
                            if (item.equals(full) || item.equals(cell)) {
                                if (!init) {
                                    TARDISMessage.send(player, "ENERGY_NO_INIT");
                                    return;
                                }
                                int amount = 0;
                                if (item.equals(full)) {
                                    if (plugin.getConfig().getBoolean("preferences.no_creative_condense")) {
                                        switch (plugin.getWorldManager()) {
                                            case MULTIVERSE:
                                                if (!plugin.getMVHelper().isWorldSurvival(block.getLocation().getWorld())) {
                                                    TARDISMessage.send(player, "ARTRON_FULL_CREATIVE");
                                                    return;
                                                }
                                                break;
                                            case NONE:
                                                if (plugin.getPlanetsConfig().getString("planets." + block.getLocation().getWorld().getName() + ".gamemode").equalsIgnoreCase("CREATIVE")) {
                                                    TARDISMessage.send(player, "ARTRON_FULL_CREATIVE");
                                                    return;
                                                }
                                                break;
                                        }
                                    }
                                    // remove the NETHER_STAR! (if appropriate)
                                    int a = player.getInventory().getItemInMainHand().getAmount();
                                    int a2 = a - 1;
                                    if (current_level < fc) {
                                        // There's room in the tank!
                                        amount = fc;
                                        if (a2 > 0) {
                                            player.getInventory().getItemInMainHand().setAmount(a2);
                                        } else {
                                            player.getInventory().removeItem(new ItemStack(full, 1));
                                        }
                                        TARDISMessage.send(player, "ENERGY_AT_MAX");
                                    } else {
                                        // We're either full or exceeding maximum, so don't do anything!
                                        amount = current_level;
                                        TARDISMessage.send(player, "ENERGY_MAX");
                                    }
                                } else {
                                    ItemStack is = player.getInventory().getItemInMainHand();
                                    if (is.hasItemMeta()) {
                                        ItemMeta im = is.getItemMeta();
                                        String name = im.getDisplayName();
                                        if (!name.equals("Artron Storage Cell")) {
                                            TARDISMessage.send(player, "CELL_NOT_VALID");
                                            return;
                                        }
                                        List<String> lore = im.getLore();
                                        int charge = TARDISNumberParsers.parseInt(lore.get(1)) * is.getAmount();
                                        if (charge <= 0) {
                                            TARDISMessage.send(player, "CELL_NOT_CHARGED");
                                            return;
                                        }
                                        amount = current_level + charge;
                                        lore.set(1, "0");
                                        im.setLore(lore);
                                        is.setItemMeta(im);
                                        is.getEnchantments().keySet().forEach(is::removeEnchantment);
                                        TARDISMessage.send(player, "CELL_TRANSFER");
                                    }
                                }
                                // update charge
                                HashMap<String, Object> set = new HashMap<>();
                                set.put("artron_level", amount);
                                plugin.getQueryFactory().doUpdate("tardis", set, whereid);
                            } else if (item.equals(Material.getMaterial(key))) {
                                // has the TARDIS been initialised?
                                if (!init) {
                                    // kickstart the TARDIS Artron Energy Capacitor
                                    TARDISSounds.playTARDISSound(block.getLocation(), "power_up");
                                    if (abandoned) {
                                        // transfer ownership to the player who clicked
                                        claimAbandoned(player, id, block, tardis);
                                    }
                                    // get locations from database
                                    String creeper = tardis.getCreeper();
                                    if (!creeper.isEmpty() && !creeper.equals(":")) {
                                        World w = block.getLocation().getWorld();
                                        Location cl = TARDISStaticLocationGetters.getLocationFromDB(creeper);
                                        plugin.setTardisSpawn(true);
                                        Entity e = w.spawnEntity(cl.add(0.0d, 1.0d, 0.0d), EntityType.CREEPER);
                                        Creeper c = (Creeper) e;
                                        c.setPowered(true);
                                        c.setRemoveWhenFarAway(false);
                                        if (tardis.getSchematic().hasBeacon()) {
                                            String beacon = tardis.getBeacon();
                                            Block bl = TARDISStaticLocationGetters.getLocationFromDB(beacon).getBlock();
                                            bl.setBlockData(TARDISConstants.GLASS);
                                        }
                                    }
                                    // set the capacitor to 50% charge
                                    HashMap<String, Object> set = new HashMap<>();
                                    int half = Math.round(fc / 2.0F);
                                    set.put("artron_level", half);
                                    set.put("tardis_init", 1);
                                    set.put("powered_on", 1);
                                    plugin.getQueryFactory().doUpdate("tardis", set, whereid);
                                    TARDISMessage.send(player, "ENERGY_INIT");
                                } else { // toggle power
                                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                                        boolean pu = true;
                                        if (abandoned) {
                                            // transfer ownership to the player who clicked
                                            pu = claimAbandoned(player, id, block, tardis);
                                        }
                                        if (pu) {
                                            new TARDISPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPowered_on(), tardis.isHidden(), lights, player.getLocation(), current_level, tardis.getSchematic().hasLanterns()).clickButton();
                                        }
                                    }
                                }
                            } else if (player.isSneaking()) {
                                if (!init) {
                                    TARDISMessage.send(player, "ENERGY_NO_INIT");
                                    return;
                                }
                                // transfer player artron energy into the capacitor
                                int ten_percent = Math.round(fc * 0.1F);
                                if (current_level >= ten_percent && plugin.getConfig().getBoolean("creation.create_worlds")) {
                                    TARDISMessage.send(player, "ENERGY_UNDER_10");
                                    return;
                                }
                                if (hasPrefs) {
                                    int level = rsp.getArtronLevel();
                                    if (level < 1) {
                                        TARDISMessage.send(player, "ENERGY_NONE");
                                        return;
                                    }
                                    int new_level = current_level + level;
                                    // set player level to 0
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("artron_level", 0);
                                    HashMap<String, Object> wherel = new HashMap<>();
                                    wherel.put("uuid", player.getUniqueId().toString());
                                    plugin.getQueryFactory().doUpdate("player_prefs", set, wherel);
                                    // add player level to TARDIS level
                                    HashMap<String, Object> sett = new HashMap<>();
                                    sett.put("artron_level", new_level);
                                    plugin.getQueryFactory().doUpdate("tardis", sett, whereid);
                                    int percent = Math.round((new_level * 100F) / fc);
                                    TARDISMessage.send(player, "ENERGY_CHARGED", String.format("%d", percent));
                                } else {
                                    TARDISMessage.send(player, "ENERGY_NONE");
                                }
                            } else {
                                if (!init) {
                                    TARDISMessage.send(player, "ENERGY_NO_INIT");
                                    return;
                                }
                                // just tell us how much energy we have
                                new TARDISArtronIndicator(plugin).showArtronLevel(player, id, 0);
                            }
                        }
                    } else if (action == Action.LEFT_CLICK_BLOCK && player.isSneaking()) {
                        event.setCancelled(true);
                        // check if the charged creeper in the TARDIS Artron Energy Capacitor is still there
                        new TARDISCreeperChecker(plugin, id).checkCreeper();
                    }
                }
            }
        }
    }

    private boolean claimAbandoned(Player player, int id, Block block, Tardis tardis) {
        // transfer ownership to the player who clicked
        boolean pu = plugin.getQueryFactory().claimTARDIS(player, id);
        // make sure player is added as owner of interior WorldGuard region
        if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
            plugin.getWorldGuardUtils().updateRegionForClaim(block.getLocation(), player.getUniqueId());
        }
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherec);
        if (rscl.resultSet()) {
            Location current = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
            if (pu) {
                new TARDISDoorCloser(plugin, player.getUniqueId(), id).closeDoors();
                TARDISMessage.send(player, "ABANDON_CLAIMED");
                plugin.getPM().callEvent(new TARDISClaimEvent(player, tardis, current));
            }
            if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                PRESET preset = tardis.getPreset();
                Sign sign = getSign(current, rscl.getDirection(), preset);
                if (sign != null) {
                    String player_name = TARDISStaticUtils.getNick(player);
                    String owner;
                    if (preset.equals(PRESET.GRAVESTONE) || preset.equals(PRESET.PUNKED) || preset.equals(PRESET.ROBOT)) {
                        owner = (player_name.length() > 14) ? player_name.substring(0, 14) : player_name;
                    } else {
                        owner = (player_name.length() > 14) ? player_name.substring(0, 12) + "'s" : player_name + "'s";
                    }
                    switch (preset) {
                        case GRAVESTONE -> sign.setLine(3, owner);
                        case ANGEL, JAIL -> sign.setLine(2, owner);
                        default -> sign.setLine(0, owner);
                    }
                    sign.update();
                }
            }
        }
        return pu;
    }
}
