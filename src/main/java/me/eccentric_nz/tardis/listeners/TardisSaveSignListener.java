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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.builders.TardisInteriorPositioning;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.flight.TardisLand;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.travel.TardisAreaCheck;
import me.eccentric_nz.tardis.travel.TardisAreasInventory;
import me.eccentric_nz.tardis.travel.TardisSaveSignInventory;
import me.eccentric_nz.tardis.travel.TardisSaveSignPageTwo;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisSaveSignListener extends TardisMenuListener implements Listener {

    private final TardisPlugin plugin;

    public TardisSaveSignListener(TardisPlugin plugin) {
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
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.startsWith(ChatColor.DARK_RED + "TARDIS saves")) {
            boolean isSecondPage = name.equals(ChatColor.DARK_RED + "TARDIS saves 2");
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            // get the TARDIS the player is in
            boolean allow = false;
            int id = -1;
            if (plugin.getTrackerKeeper().getJunkPlayers().containsKey(uuid)) {
                allow = true;
                id = plugin.getTrackerKeeper().getJunkPlayers().get(uuid);
            } else {
                HashMap<String, Object> wheres = new HashMap<>();
                wheres.put("uuid", uuid.toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                if (rst.resultSet()) {
                    allow = true;
                    id = rst.getTardisId();
                }
            }
            if (!allow) {
                event.setCancelled(true);
            } else {
                int slot = event.getRawSlot();
                if (plugin.getTrackerKeeper().getArrangers().contains(uuid)) {
                    if (((slot >= 1 || (slot == 0 && isSecondPage)) && slot < 45) || slot == 47) {
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
                                assert cim != null;
                                String save = cim.getDisplayName();
                                HashMap<String, Object> where = new HashMap<>();
                                where.put("tardis_id", id);
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
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
                        Location current = null;
                        if (rsc.resultSet()) {
                            current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                        }
                        ItemStack is = view.getItem(slot);
                        if (is != null) {
                            ItemMeta im = is.getItemMeta();
                            assert im != null;
                            List<String> lore = im.getLore();
                            assert lore != null;
                            Location save_dest = getLocation(lore);
                            if (save_dest != null) {
                                if (lore.get(0).startsWith("TARDIS_")) {
                                    close(player);
                                    TardisMessage.send(player, "SAVE_NO_TARDIS");
                                    return;
                                }
                                // check the player is allowed!
                                if (!plugin.getPluginRespect().getRespect(save_dest, new Parameters(player, Flag.getDefaultFlags()))) {
                                    close(player);
                                    return;
                                }
                                // get tardis artron level
                                ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
                                if (!rs.fromID(id)) {
                                    close(player);
                                    return;
                                }
                                int level = rs.getArtronLevel();
                                int travel = plugin.getArtronConfig().getInt("travel");
                                if (level < travel) {
                                    TardisMessage.send(player, "NOT_ENOUGH_ENERGY");
                                    close(player);
                                    return;
                                }
                                TardisAreaCheck tac = plugin.getTardisArea().areaCheckInExistingArea(save_dest);
                                if (tac.isInArea()) {
                                    // save is in a TARDIS area, so check that the spot is not occupied
                                    HashMap<String, Object> wheresave = new HashMap<>();
                                    wheresave.put("world", lore.get(0));
                                    wheresave.put("x", lore.get(1));
                                    wheresave.put("y", lore.get(2));
                                    wheresave.put("z", lore.get(3));
                                    ResultSetCurrentLocation rsz = new ResultSetCurrentLocation(plugin, wheresave);
                                    if (rsz.resultSet()) {
                                        TardisMessage.send(player, "TARDIS_IN_SPOT", ChatColor.AQUA + "/tardistravel area [name]" + ChatColor.RESET + " command instead.");
                                        close(player);
                                        return;
                                    }
                                    String invisibility = tac.getArea().getInvisibility();
                                    HashMap<String, Object> wheret = new HashMap<>();
                                    wheret.put("tardis_id", id);
                                    ResultSetTardis resultSetTardis = new ResultSetTardis(plugin, wheret, "", false, 2);
                                    if (resultSetTardis.resultSet()) {
                                        if (invisibility.equals("DENY") && resultSetTardis.getTardis().getPreset().equals(Preset.INVISIBLE)) {
                                            // check preset
                                            TardisMessage.send(player, "AREA_NO_INVISIBLE");
                                            return;
                                        } else if (!invisibility.equals("ALLOW")) {
                                            // force preset
                                            TardisMessage.send(player, "AREA_FORCE_PRESET", invisibility);
                                            HashMap<String, Object> wherei = new HashMap<>();
                                            wherei.put("tardis_id", id);
                                            HashMap<String, Object> seti = new HashMap<>();
                                            seti.put("chameleon_preset", invisibility);
                                            // set chameleon adaption to OFF
                                            seti.put("adapti_on", 0);
                                            plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                                        }
                                    }
                                }
                                if (!save_dest.equals(current) || plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                    HashMap<String, Object> set = new HashMap<>();
                                    set.put("world", lore.get(0));
                                    set.put("x", TardisNumberParsers.parseInt(lore.get(1)));
                                    set.put("y", TardisNumberParsers.parseInt(lore.get(2)));
                                    set.put("z", TardisNumberParsers.parseInt(lore.get(3)));
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
                                        wheret.put("tardis_id", id);
                                        plugin.getQueryFactory().doSyncUpdate("tardis", sett, wheret);
                                    }
                                    HashMap<String, Object> wheret = new HashMap<>();
                                    wheret.put("tardis_id", id);
                                    plugin.getQueryFactory().doSyncUpdate("next", set, wheret);
                                    plugin.getTrackerKeeper().getHasDestination().put(id, travel);
                                    plugin.getTrackerKeeper().getRescue().remove(id);
                                    close(player);
                                    TardisMessage.send(player, "DEST_SET_TERMINAL", im.getDisplayName(), !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id));
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        new TardisLand(plugin, id, player).exitVortex();
                                    }
                                } else if (!lore.contains(ChatColor.GOLD + "Current location")) {
                                    lore.add(ChatColor.GOLD + "Current location");
                                    im.setLore(lore);
                                    is.setItemMeta(im);
                                }
                            } else {
                                close(player);
                                TardisMessage.send(player, "DEST_NOT_VALID", im.getDisplayName());
                            }
                        }
                    }
                }
                if (slot == 45) {
                    // check it is this player's TARDIS
                    HashMap<String, Object> wherez = new HashMap<>();
                    wherez.put("tardis_id", id);
                    wherez.put("uuid", uuid.toString());
                    ResultSetTardis rs = new ResultSetTardis(plugin, wherez, "", false, 0);
                    if (rs.resultSet()) {
                        plugin.getTrackerKeeper().getArrangers().add(uuid);
                        TardisMessage.send(player, "SAVE_ARRANGE");
                    } else {
                        TardisMessage.send(player, "NOT_OWNER");
                    }
                }
                ItemStack own = Objects.requireNonNull(event.getClickedInventory()).getItem(49);
                if (slot == 49 && own != null) {
                    // custom model data of item
                    int cmd = Objects.requireNonNull(own.getItemMeta()).getCustomModelData();
                    int ownId = -1;
                    if (cmd == 138) {
                        // get player's TARDIS id
                        ResultSetTardisId rstid = new ResultSetTardisId(plugin);
                        if (rstid.fromUUID(uuid.toString())) {
                            ownId = rstid.getTardisId();
                        }
                    } else {
                        // get id of TARDIS player is in
                        ownId = TardisInteriorPositioning.getTardisIdFromLocation(player.getLocation());
                    }
                    if (ownId != -1) {
                        int saveId = ownId;
                        // load own/tardis saves
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            Inventory inv;
                            ItemStack[] items;
                            if (isSecondPage) {
                                TardisSaveSignPageTwo sst = new TardisSaveSignPageTwo(plugin, saveId, player);
                                items = sst.getPageTwo();
                                inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves 2");
                            } else {
                                TardisSaveSignInventory sst = new TardisSaveSignInventory(plugin, saveId, player);
                                items = sst.getTerminal();
                                inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
                            }
                            inv.setContents(items);
                            player.openInventory(inv);
                        }, 2L);
                    }
                }
                if (slot == 51 && event.getClickedInventory().getItem(51) != null) {
                    // load page 2
                    int finalId = id;
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Inventory inv;
                        ItemStack[] items;
                        if (isSecondPage) {
                            TardisSaveSignInventory sst = new TardisSaveSignInventory(plugin, finalId, player);
                            items = sst.getTerminal();
                            inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
                        } else {
                            TardisSaveSignPageTwo sst = new TardisSaveSignPageTwo(plugin, finalId, player);
                            items = sst.getPageTwo();
                            inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves 2");
                        }
                        inv.setContents(items);
                        player.openInventory(inv);
                    }, 2L);
                }
                if (slot == 53) {
                    // load TARDIS areas
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        TardisAreasInventory sst = new TardisAreasInventory(plugin, player);
                        ItemStack[] items = sst.getTerminal();
                        Inventory areainv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS areas");
                        areainv.setContents(items);
                        player.openInventory(areainv);
                    }, 2L);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSaveSignClose(InventoryCloseEvent event) {
        String inv_name = event.getView().getTitle();
        if (inv_name.startsWith(ChatColor.DARK_RED + "tardis saves")) {
            boolean isPageTwo = inv_name.equals(ChatColor.DARK_RED + "tardis saves 2");
            UUID uuid = event.getPlayer().getUniqueId();
            // get the tardis the player is in
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("uuid", uuid.toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
            if (rst.resultSet()) {
                int id = rst.getTardisId();
                ItemStack[] stack = event.getInventory().getContents();
                int start = (isPageTwo) ? 0 : 1;
                for (int i = start; i < 45; i++) {
                    if (stack[i] != null) {
                        ItemMeta im = stack[i].getItemMeta();
                        assert im != null;
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
        World w = TardisAliasResolver.getWorldFromAlias(lore.get(0));
        if (w == null) {
            return null;
        }
        int x = TardisNumberParsers.parseInt(lore.get(1));
        int y = TardisNumberParsers.parseInt(lore.get(2));
        int z = TardisNumberParsers.parseInt(lore.get(3));
        return new Location(w, x, y, z);
    }
}
