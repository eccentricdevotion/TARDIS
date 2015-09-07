/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.control;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.travel.TARDISAreasInventory;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author eccentric_nz
 */
public class TARDISControlMenuListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISControlMenuListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onControlMenuInteract(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4TARDIS Control Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 18) {
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<String, Object>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        final int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("tardis_id", id);
                        final ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (rs.resultSet()) {
                            // check they initialised
                            if (!rs.isTardis_init()) {
                                TARDISMessage.send(player, "ENERGY_NO_INIT");
                                return;
                            }
                            if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on() && slot != 6 && slot != 7) {
                                TARDISMessage.send(player, "POWER_DOWN");
                                return;
                            }
                            if (!rs.isHandbrake_on()) {
                                String message = (slot == 9) ? "ARS_NO_TRAVEL" : "NOT_WHILE_TRAVELLING";
                                TARDISMessage.send(player, message);
                                return;
                            }
                            boolean lights = rs.isLights_on();
                            final int level = rs.getArtron_level();
                            TARDISCircuitChecker tcc = null;
                            if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                                tcc = new TARDISCircuitChecker(plugin, id);
                                tcc.getCircuits();
                            }
                            switch (slot) {
                                case 0:
                                    // random location
                                    close(player);
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TARDISMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    // give the GUI time to close first
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            new TARDISRandomButton(plugin, player, id, level, 0, rs.getCompanions(), rs.getUuid()).clickButton();
                                        }
                                    }, 2L);
                                    break;
                                case 1:
                                    // fast return
                                    close(player);
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TARDISMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    new TARDISFastReturnButton(plugin, player, id, level).clickButton();
                                    break;
                                case 2:
                                    // destination terminal
                                    if (level < plugin.getArtronConfig().getInt("travel")) {
                                        TARDISMessage.send(player, "NOT_ENOUGH_ENERGY");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TARDISMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    ItemStack[] items = new TARDISTerminalInventory(plugin).getTerminal();
                                    Inventory aec = plugin.getServer().createInventory(player, 54, "§4Destination Terminal");
                                    aec.setContents(items);
                                    player.openInventory(aec);
                                    break;
                                case 3:
                                    // saves
                                    if (tcc != null && !tcc.hasMemory()) {
                                        TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                                        return;
                                    }
                                    TARDISSaveSignInventory tssi = new TARDISSaveSignInventory(plugin, rs.getTardis_id());
                                    ItemStack[] saves = tssi.getTerminal();
                                    Inventory saved = plugin.getServer().createInventory(player, 54, "§4TARDIS saves");
                                    saved.setContents(saves);
                                    player.openInventory(saved);
                                    break;
                                case 4:
                                    // areas
                                    if (tcc != null && !tcc.hasMemory()) {
                                        TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                                        return;
                                    }
                                    TARDISAreasInventory tai = new TARDISAreasInventory(plugin, player);
                                    ItemStack[] areas = tai.getTerminal();
                                    Inventory areainv = plugin.getServer().createInventory(player, 54, "§4TARDIS areas");
                                    areainv.setContents(areas);
                                    player.openInventory(areainv);
                                    break;
                                case 6:
                                    // artron level
                                    close(player);
                                    new TARDISArtronIndicator(plugin).showArtronLevel(player, id, 0);
                                    break;
                                case 7:
                                    // power up/down
                                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                                        close(player);
                                        new TARDISPowerButton(plugin, id, player, rs.getPreset(), rs.isPowered_on(), rs.isHidden(), lights, player.getLocation(), level, rs.getSchematic().hasLanterns()).clickButton();
                                    } else {
                                        TARDISMessage.send(player, "POWER_DOWN_DISABLED");
                                    }
                                    break;
                                case 8:
                                    // siege
                                    close(player);
                                    if (tcc != null && !tcc.hasMaterialisation()) {
                                        TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                                        return;
                                    }
                                    new TARDISSiegeButton(plugin, player, rs.isPowered_on(), id).clickButton();
                                    break;
                                case 9:
                                    // ars
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    // check they're in a compatible world
                                    if (!plugin.getUtils().canGrowRooms(rs.getChunk())) {
                                        TARDISMessage.send(player, "ROOM_OWN_WORLD");
                                        return;
                                    }
                                    // check they have permission to grow rooms
                                    if (!player.hasPermission("tardis.architectural")) {
                                        TARDISMessage.send(player, "NO_PERM_ROOMS");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                                        TARDISMessage.send(player, "ARS_MISSING");
                                        return;
                                    }
                                    ItemStack[] tars = new TARDISARSInventory(plugin).getARS();
                                    Inventory ars = plugin.getServer().createInventory(player, 54, "§4Architectural Reconfiguration");
                                    ars.setContents(tars);
                                    player.openInventory(ars);
                                    break;
                                case 10:
                                    // desktop theme
                                    new TARDISThemeButton(plugin, player, rs.getSchematic(), level).clickButton();
                                    break;
                                case 12:
                                    // temporal
                                    if (!player.hasPermission("tardis.temporal")) {
                                        TARDISMessage.send(player, "NO_PERM_TEMPORAL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasTemporal() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TARDISMessage.send(player, "TEMP_MISSING");
                                        return;
                                    }
                                    ItemStack[] clocks = new TARDISTemporalLocatorInventory(plugin).getTemporal();
                                    Inventory tmpl = plugin.getServer().createInventory(player, 27, "§4Temporal Locator");
                                    tmpl.setContents(clocks);
                                    player.openInventory(tmpl);
                                    break;
                                case 13:
                                    // TIS
                                    new TARDISInfoMenuButton(plugin, player).clickButton();
                                    close(player);
                                    break;
                                case 14:
                                    // light switch
                                    close(player);
                                    if (!lights && plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                                        TARDISMessage.send(player, "POWER_DOWN");
                                        return;
                                    }
                                    new TARDISLightSwitch(plugin, id, lights, player, rs.getSchematic().hasLanterns()).flickSwitch();
                                    break;
                                case 15:
                                    // toggle wool
                                    close(player);
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
                                    break;
                                case 17:
                                    // close
                                    close(player);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
