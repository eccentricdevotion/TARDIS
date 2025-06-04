/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.travel.save;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.flight.TARDISLand;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.travel.TARDISAreaCheck;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISSavesListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISSavesListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onSaveTerminalClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            // player clicked outside inventory
            event.setCancelled(plugin.getTrackerKeeper().getArrangers().contains(event.getWhoClicked().getUniqueId()));
            return;
        }
        InventoryView view = event.getView();
        if (view.getTitle().startsWith(ChatColor.DARK_RED + "TARDIS saves")) {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            // get the TARDIS the player is in
            boolean allow = false;
            int occupiedTardisId = -1;
            int playerTardisId = -1;
            if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(uuid)) {
                allow = true;
                occupiedTardisId = plugin.getTrackerKeeper().getJunkPlayers().get(uuid);
                playerTardisId = occupiedTardisId;
            } else if (plugin.getTrackerKeeper().getSavesIds().containsKey(uuid)) {
                // player wants own saves
                playerTardisId = plugin.getTrackerKeeper().getSavesIds().get(uuid);
                allow = true;
            }
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
            if (rst.resultSet()) {
                occupiedTardisId = rst.getTardis_id();
                allow = true;
            }
            if (!allow) {
                event.setCancelled(true);
            } else {
                int slot = event.getRawSlot();
                if (plugin.getTrackerKeeper().getArrangers().contains(uuid)) {
                    if ((slot >= 0 && slot < 45) || slot == 47) {
                        if (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
                            event.setCancelled(true);
                            return;
                        }
                        // allow
                        if (slot == 47) {
                            // get item on cursor
                            ItemStack cursor = player.getItemOnCursor();
                            if (cursor.getType().isAir()) {
                                event.setCancelled(true);
                            } else {
                                ItemMeta cim = cursor.getItemMeta();
                                String save = cim.getDisplayName();
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("tardis_id", occupiedTardisId);
                                where.put("dest_name", save);
                                plugin.getQueryFactory().doDelete("destinations", where);
                                player.setItemOnCursor(null);
                            }
                            event.setCancelled(true);
                        }
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                    if (slot >= 0 && slot < 45) {
                        Location exterior = null;
                        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, occupiedTardisId);
                        if (rsc.resultSet()) {
                            exterior = rsc.getCurrent().location();
                        }
                        ItemStack is = view.getItem(slot);
                        if (is != null) {
                            ItemMeta im = is.getItemMeta();
                            List<String> lore = im.getLore();
                            if (lore.getFirst().startsWith("TARDIS_")) {
                                close(player);
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NO_TARDIS");
                                return;
                            }
                            Location save_dest = getLocation(lore);
                            if (save_dest != null) {
                                // check the player is allowed!
                                if (!plugin.getPluginRespect().getRespect(save_dest, new Parameters(player, Flag.getDefaultFlags()))) {
                                    close(player);
                                    return;
                                }
                                // get tardis artron level
                                ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                                if (!rs.fromID(occupiedTardisId)) {
                                    close(player);
                                    return;
                                }
                                int level = rs.getArtronLevel();
                                int travel = plugin.getArtronConfig().getInt("travel");
                                if (level < travel) {
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                                    close(player);
                                    return;
                                }
                                TARDISAreaCheck tac = plugin.getTardisArea().isSaveInArea(save_dest);
                                if (tac.isInArea()) {
                                    // save is in a TARDIS area, so check that the spot is not occupied
                                    HashMap<String, Object> wheresave = new HashMap<>();
                                    wheresave.put("world", lore.getFirst());
                                    wheresave.put("x", lore.get(1));
                                    wheresave.put("y", lore.get(2));
                                    wheresave.put("z", lore.get(3));
                                    ResultSetCurrentLocation rsz = new ResultSetCurrentLocation(plugin, wheresave);
                                    if (rsz.resultSet()) {
                                        plugin.getMessenger().sendColouredCommand(player, "TARDIS_IN_SPOT", "/tardistravel area [name]", plugin);
                                        close(player);
                                        return;
                                    }
                                    String invisibility = tac.getArea().invisibility();
                                    HashMap<String, Object> wheret = new HashMap<>();
                                    wheret.put("tardis_id", occupiedTardisId);
                                    ResultSetTardis resultSetTardis = new ResultSetTardis(plugin, wheret, "", false);
                                    if (resultSetTardis.resultSet()) {
                                        if (invisibility.equals("DENY") && resultSetTardis.getTardis().getPreset().equals(ChameleonPreset.INVISIBLE)) {
                                            // check preset
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_NO_INVISIBLE");
                                            return;
                                        } else if (!invisibility.equals("ALLOW")) {
                                            // force preset
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "AREA_FORCE_PRESET", invisibility);
                                            HashMap<String, Object> wherei = new HashMap<>();
                                            wherei.put("tardis_id", occupiedTardisId);
                                            HashMap<String, Object> seti = new HashMap<>();
                                            seti.put("chameleon_preset", invisibility);
                                            // set chameleon adaption to OFF
                                            seti.put("adapti_on", 0);
                                            plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                                        }
                                    }
                                }
                                if (!save_dest.equals(exterior) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(occupiedTardisId)) {
                                    // damage circuit if configured
                                    if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.materialisation") > 0) {
                                        TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, occupiedTardisId);
                                        tcc.getCircuits();
                                        // decrement uses
                                        int uses_left = tcc.getMemoryUses();
                                        new TARDISCircuitDamager(plugin, DiskCircuit.MEMORY, uses_left, occupiedTardisId, player).damage();
                                    }
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("world", lore.getFirst());
                                    set.put("x", TARDISNumberParsers.parseInt(lore.get(1)));
                                    set.put("y", TARDISNumberParsers.parseInt(lore.get(2)));
                                    set.put("z", TARDISNumberParsers.parseInt(lore.get(3)));
                                    int l_size = lore.size();
                                    if (l_size >= 5) {
                                        if (!lore.get(4).isEmpty() && !lore.get(4).equals(ChatColor.GOLD + "Current location")) {
                                            set.put("direction", lore.get(4));
                                        }
                                        if (l_size > 5 && !lore.get(5).isEmpty() && lore.get(5).equals("true")) {
                                            set.put("submarine", 1);
                                        } else {
                                            set.put("submarine", 0);
                                        }
                                    }
                                    if (l_size >= 7 && !lore.get(6).equals(ChatColor.GOLD + "Current location")) {
                                        HashMap<String, Object> sett = new HashMap<>();
                                        sett.put("chameleon_preset", lore.get(6));
                                        // set chameleon adaption to OFF
                                        sett.put("adapti_on", 0);
                                        HashMap<String, Object> wheret = new HashMap<>();
                                        wheret.put("tardis_id", occupiedTardisId);
                                        plugin.getQueryFactory().doSyncUpdate("tardis", sett, wheret);
                                    }
                                    HashMap<String, Object> wheret = new HashMap<>();
                                    wheret.put("tardis_id", occupiedTardisId);
                                    plugin.getQueryFactory().doSyncUpdate("next", set, wheret);
                                    TravelType travelType = (is.getItemMeta().getDisplayName().equals("Home")) ? TravelType.HOME : TravelType.SAVE;
                                    plugin.getTrackerKeeper().getHasDestination().put(occupiedTardisId, new TravelCostAndType(travel, travelType));
                                    plugin.getTrackerKeeper().getRescue().remove(occupiedTardisId);
                                    close(player);
                                    plugin.getMessenger().sendJoined(player, "DEST_SET_TERMINAL", im.getDisplayName(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(occupiedTardisId));
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(occupiedTardisId)) {
                                        new TARDISLand(plugin, occupiedTardisId, player).exitVortex();
                                        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, travelType, occupiedTardisId));
                                    }
                                } else if (!lore.contains(ChatColor.GOLD + "Current location")) {
                                    lore.add(ChatColor.GOLD + "Current location");
                                    im.setLore(lore);
                                    is.setItemMeta(im);
                                }
                            } else {
                                close(player);
                                plugin.getMessenger().send(player, TardisModule.TARDIS, "DEST_NOT_VALID", im.getDisplayName());
                            }
                        }
                    }
                    if (slot == 45) {
                        // check it is this player's TARDIS
                        HashMap<String, Object> wherez = new HashMap<>();
                        wherez.put("tardis_id", occupiedTardisId);
                        wherez.put("uuid", uuid.toString());
                        ResultSetTardis rs = new ResultSetTardis(plugin, wherez, "", false);
                        if (rs.resultSet()) {
                            plugin.getTrackerKeeper().getArrangers().add(uuid);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_ARRANGE");
                        } else {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_OWNER");
                        }
                    }
                    if (slot == 53) {
                        int finalId = playerTardisId != -1 ? playerTardisId : occupiedTardisId;
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Dimension Map");
                            TARDISSavesPlanetInventory tspi = new TARDISSavesPlanetInventory(plugin, finalId, player);
                            ItemStack[] items = tspi.getPlanets();
                            inv.setContents(items);
                            player.openInventory(inv);
                        }, 2L);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSaveSignClose(InventoryCloseEvent event) {
        String inv_name = event.getView().getTitle();
        if (inv_name.startsWith(ChatColor.DARK_RED + "TARDIS saves")) {
            boolean isPageTwo = inv_name.equals(ChatColor.DARK_RED + "TARDIS saves 2");
            UUID uuid = event.getPlayer().getUniqueId();
            // get the TARDIS the player is in
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
            if (rst.resultSet()) {
                int id = rst.getTardis_id();
                ItemStack[] stack = event.getInventory().getContents();
                int start = (isPageTwo) ? 0 : 1;
                for (int i = start; i < 45; i++) {
                    if (stack[i] != null) {
                        ItemMeta im = stack[i].getItemMeta();
                        String save = im.getDisplayName();
                        HashMap<String, Object> set = new HashMap<>();
                        int slot = (isPageTwo) ? 45 + i : i;
                        set.put("slot", slot);
                        set.put("icon", stack[i].getType().toString());
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        where.put("dest_name", save);
                        plugin.getQueryFactory().doUpdate("destinations", set, where);
                    }
                }
            }
            if (plugin.getTrackerKeeper().getArrangers().contains(uuid)) {
                event.getPlayer().setItemOnCursor(new ItemStack(Material.AIR));
                plugin.getTrackerKeeper().getArrangers().remove(uuid);
            }
        }
    }

    /**
     * Converts an Item Stacks lore to a Location.
     *
     * @param lore the lore to read
     * @return a Location
     */
    private Location getLocation(List<String> lore) {
        World w = TARDISAliasResolver.getWorldFromAlias(lore.getFirst());
        if (w == null) {
            return null;
        }
        int x = TARDISNumberParsers.parseInt(lore.get(1));
        int y = TARDISNumberParsers.parseInt(lore.get(2));
        int z = TARDISNumberParsers.parseInt(lore.get(3));
        return new Location(w, x, y, z);
    }
}
