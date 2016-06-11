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

import java.util.Arrays;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.artron.TARDISArtronIndicator;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonInventory;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISPrefsMenuInventory;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISDirectionCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.travel.TARDISAreasInventory;
import me.eccentric_nz.TARDIS.travel.TARDISSaveSignInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
            if (slot >= 0 && slot < 54) {
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
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (rs.resultSet()) {
                            final Tardis tardis = rs.getTardis();
                            // check they initialised
                            if (!tardis.isTardis_init()) {
                                TARDISMessage.send(player, "ENERGY_NO_INIT");
                                return;
                            }
                            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on() && slot != 6 && slot != 13 && slot != 20) {
                                TARDISMessage.send(player, "POWER_DOWN");
                                return;
                            }
                            if (!tardis.isHandbrake_on()) {
                                switch (slot) {
                                    case 2:
                                        TARDISMessage.send(player, "ARS_NO_TRAVEL");
                                        return;
                                    case 4:
                                    case 11:
                                    case 13:
                                    case 15:
                                    case 20:
                                    case 22:
                                    case 31:
                                    case 40:
                                        if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                            TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                            return;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                            boolean lights = tardis.isLights_on();
                            final int level = tardis.getArtron_level();
                            TARDISCircuitChecker tcc = null;
                            if (!plugin.getDifficulty().equals(DIFFICULTY.EASY)) {
                                tcc = new TARDISCircuitChecker(plugin, id);
                                tcc.getCircuits();
                            }
                            switch (slot) {
                                case 0:
                                    // random location
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TARDISMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    close(player);
                                    // give the GUI time to close first
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                        @Override
                                        public void run() {
                                            new TARDISRandomButton(plugin, player, id, level, 0, tardis.getCompanions(), tardis.getUuid()).clickButton();
                                        }
                                    }, 2L);
                                    break;
                                case 2:
                                    // ars
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    // check they're in a compatible world
                                    if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
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
                                case 4:
                                    // chameleon circuit
                                    if (tcc != null && !tcc.hasChameleon()) {
                                        TARDISMessage.send(player, "CHAM_MISSING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
                                        TARDISMessage.send(player, "NOT_WHILE_DISPERSED");
                                        return;
                                    }
                                    // open Chameleon Circuit GUI
                                    ItemStack[] cc = new TARDISChameleonInventory(plugin, tardis.isChamele_on(), tardis.isAdapti_on()).getTerminal();
                                    Inventory cc_gui = plugin.getServer().createInventory(player, 54, "§4Chameleon Circuit");
                                    cc_gui.setContents(cc);
                                    player.openInventory(cc_gui);
                                    break;
                                case 6:
                                    // artron level
                                    close(player);
                                    new TARDISArtronIndicator(plugin).showArtronLevel(player, id, 0);
                                    break;
                                case 8:
                                    // zero room
                                    int zero_amount = plugin.getArtronConfig().getInt("zero");
                                    if (level < zero_amount) {
                                        TARDISMessage.send(player, "NOT_ENOUGH_ZERO_ENERGY");
                                        return;
                                    }
                                    final Location zero = TARDISLocationGetters.getLocationFromDB(tardis.getZero(), 0.0F, 0.0F);
                                    if (zero != null) {
                                        close(player);
                                        TARDISMessage.send(player, "ZERO_READY");
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                            @Override
                                            public void run() {
                                                new TARDISExteriorRenderer(plugin).transmat(player, COMPASS.SOUTH, zero);
                                            }
                                        }, 20L);
                                        plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
                                        HashMap<String, Object> wherez = new HashMap<String, Object>();
                                        wherez.put("tardis_id", id);
                                        new QueryFactory(plugin).alterEnergyLevel("tardis", -zero_amount, wherez, player);
                                    } else {
                                        TARDISMessage.send(player, "NO_ZERO");
                                    }
                                    break;
                                case 9:
                                    // saves
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasMemory()) {
                                        TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                                        return;
                                    }
                                    TARDISSaveSignInventory tssi = new TARDISSaveSignInventory(plugin, tardis.getTardis_id());
                                    ItemStack[] saves = tssi.getTerminal();
                                    Inventory saved = plugin.getServer().createInventory(player, 54, "§4TARDIS saves");
                                    saved.setContents(saves);
                                    player.openInventory(saved);
                                    break;
                                case 11:
                                    // desktop theme
                                    if (!player.hasPermission("tardis.upgrade")) {
                                        TARDISMessage.send(player, "NO_PERM_UPGRADE");
                                        return;
                                    }
                                    new TARDISThemeButton(plugin, player, tardis.getSchematic(), level, id).clickButton();
                                    break;
                                case 13:
                                    // siege
                                    if (tcc != null && !tcc.hasMaterialisation()) {
                                        TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                                        return;
                                    }
                                    close(player);
                                    new TARDISSiegeButton(plugin, player, tardis.isPowered_on(), id).clickButton();
                                    break;
                                case 15:
                                    // scanner
                                    close(player);
                                    plugin.getGeneralKeeper().getScannerListener().scan(player, id, plugin.getServer().getScheduler());
                                    break;
                                case 17:
                                    //player prefs
                                    Inventory ppm = plugin.getServer().createInventory(player, 27, "§4Player Prefs Menu");
                                    ppm.setContents(new TARDISPrefsMenuInventory(plugin, player.getUniqueId()).getMenu());
                                    player.openInventory(ppm);
                                    break;
                                case 18:
                                    // fast return
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TARDISMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    close(player);
                                    new TARDISFastReturnButton(plugin, player, id, level).clickButton();
                                    break;
                                case 20:
                                    // power up/down
                                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                                        close(player);
                                        new TARDISPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPowered_on(), tardis.isHidden(), lights, player.getLocation(), level, tardis.getSchematic().hasLanterns()).clickButton();
                                    } else {
                                        TARDISMessage.send(player, "POWER_DOWN_DISABLED");
                                    }
                                    break;
                                case 22:
                                    // hide
                                    close(player);
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    new TARDISHideCommand(plugin).hide(player);
                                    break;
                                case 24:
                                    // TIS
                                    close(player);
                                    new TARDISInfoMenuButton(plugin, player).clickButton();
                                    break;
                                case 27:
                                    // areas
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
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
                                case 29:
                                    // light switch
                                    if (!lights && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                                        TARDISMessage.send(player, "POWER_DOWN");
                                        return;
                                    }
                                    close(player);
                                    new TARDISLightSwitch(plugin, id, lights, player, tardis.getSchematic().hasLanterns()).flickSwitch();
                                    break;
                                case 31:
                                    // rebuild
                                    close(player);
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    new TARDISRebuildCommand(plugin).rebuildPreset(player);
                                    break;
                                case 36:
                                    // destination terminal
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
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
                                case 38:
                                    // toggle wool
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    close(player);
                                    new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
                                    break;
                                case 40:
                                    // direction
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    HashMap<String, Object> whered = new HashMap<String, Object>();
                                    whered.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, whered);
                                    String direction = "EAST";
                                    if (rsc.resultSet()) {
                                        direction = rsc.getDirection().toString();
                                        int ordinal = COMPASS.valueOf(direction).ordinal() + 1;
                                        if (ordinal == 4) {
                                            ordinal = 0;
                                        }
                                        direction = COMPASS.values()[ordinal].toString();
                                    }
                                    String[] args = new String[]{"direction", direction};
                                    new TARDISDirectionCommand(plugin).changeDirection(player, args);
                                    // update the lore
                                    ItemStack d = inv.getItem(40);
                                    ItemMeta im = d.getItemMeta();
                                    im.setLore(Arrays.asList(direction));
                                    d.setItemMeta(im);
                                    break;
                                case 47:
                                    // tardis map
                                    Inventory new_inv = plugin.getServer().createInventory(player, 54, "§4TARDIS Map");
                                    // open new inventory
                                    new_inv.setContents(new TARDISARSMap(plugin).getMap());
                                    player.openInventory(new_inv);
                                    break;
                                case 49:
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
                                case 53:
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
