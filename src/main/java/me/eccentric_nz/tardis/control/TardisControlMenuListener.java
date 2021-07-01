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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.ars.TardisArsInventory;
import me.eccentric_nz.tardis.ars.TardisArsMap;
import me.eccentric_nz.tardis.artron.TardisArtronIndicator;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.chameleon.TardisChameleonInventory;
import me.eccentric_nz.tardis.commands.preferences.TardisPrefsMenuInventory;
import me.eccentric_nz.tardis.commands.tardis.TardisDirectionCommand;
import me.eccentric_nz.tardis.commands.tardis.TardisHideCommand;
import me.eccentric_nz.tardis.commands.tardis.TardisRebuildCommand;
import me.eccentric_nz.tardis.companiongui.TardisCompanionInventory;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.listeners.TardisMenuListener;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.move.TardisBlackWoolToggler;
import me.eccentric_nz.tardis.rooms.TardisExteriorRenderer;
import me.eccentric_nz.tardis.transmat.TardisTransmatInventory;
import me.eccentric_nz.tardis.travel.TardisAreasInventory;
import me.eccentric_nz.tardis.travel.TardisSaveSignInventory;
import me.eccentric_nz.tardis.travel.TardisTemporalLocatorInventory;
import me.eccentric_nz.tardis.travel.TardisTerminalInventory;
import me.eccentric_nz.tardis.utility.TardisStaticLocationGetters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TardisControlMenuListener extends TardisMenuListener implements Listener {

    private final TardisPlugin plugin;

    public TardisControlMenuListener(TardisPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onControlMenuInteract(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Control Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 54) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardisId();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            // check they initialised
                            if (!tardis.isTardisInit()) {
                                TardisMessage.send(player, "ENERGY_NO_INIT");
                                return;
                            }
                            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered() && slot != 6 && slot != 13 && slot != 20) {
                                TardisMessage.send(player, "POWER_DOWN");
                                return;
                            }
                            if (!tardis.isHandbrakeOn()) {
                                switch (slot) {
                                    case 2:
                                        TardisMessage.send(player, "ARS_NO_TRAVEL");
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
                                            TardisMessage.send(player, "NOT_WHILE_TRAVELLING");
                                            return;
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                            boolean lights = tardis.isLightsOn();
                            int level = tardis.getArtronLevel();
                            TardisCircuitChecker tcc = null;
                            if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                                tcc = new TardisCircuitChecker(plugin, id);
                                tcc.getCircuits();
                            }
                            switch (slot) {
                                case 0:
                                    // random location
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TardisMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    close(player, false);
                                    // give the GUI time to close first
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisRandomButton(plugin, player, id, level, 0, tardis.getCompanions(), tardis.getUuid()).clickButton(), 2L);
                                    break;
                                case 2:
                                    // ars
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    // check they're in a compatible world
                                    if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                                        TardisMessage.send(player, "ROOM_OWN_WORLD");
                                        return;
                                    }
                                    // check they have permission to grow rooms
                                    if (!TardisPermission.hasPermission(player, "tardis.architectural")) {
                                        TardisMessage.send(player, "NO_PERM_ROOMS");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasArs() && !plugin.getUtils().inGracePeriod(player, true)) {
                                        TardisMessage.send(player, "ARS_MISSING");
                                        return;
                                    }
                                    ItemStack[] tars = new TardisArsInventory(plugin).getArs();
                                    Inventory ars = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
                                    ars.setContents(tars);
                                    player.openInventory(ars);
                                    break;
                                case 4:
                                    // chameleon circuit
                                    if (tcc != null && !tcc.hasChameleon()) {
                                        TardisMessage.send(player, "CHAM_MISSING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDispersedTardises().contains(id)) {
                                        TardisMessage.send(player, "NOT_WHILE_DISPERSED");
                                        return;
                                    }
                                    // open Chameleon Circuit GUI
                                    ItemStack[] cc = new TardisChameleonInventory(plugin, tardis.getAdaption(), tardis.getPreset()).getMenu();
                                    Inventory cc_gui = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Chameleon Circuit");
                                    cc_gui.setContents(cc);
                                    player.openInventory(cc_gui);
                                    break;
                                case 6:
                                    // artron level
                                    close(player, true);
                                    new TardisArtronIndicator(plugin).showArtronLevel(player, id, 0);
                                    break;
                                case 8:
                                    // zero room
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    int zero_amount = plugin.getArtronConfig().getInt("zero");
                                    if (level < zero_amount) {
                                        TardisMessage.send(player, "NOT_ENOUGH_ZERO_ENERGY");
                                        return;
                                    }
                                    Location zero = TardisStaticLocationGetters.getLocationFromDB(tardis.getZero());
                                    if (zero != null) {
                                        close(player, false);
                                        TardisMessage.send(player, "ZERO_READY");
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TardisExteriorRenderer(plugin).transmat(player, CardinalDirection.SOUTH, zero), 20L);
                                        plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
                                        HashMap<String, Object> wherez = new HashMap<>();
                                        wherez.put("tardis_id", id);
                                        plugin.getQueryFactory().alterEnergyLevel("tardis", -zero_amount, wherez, player);
                                    } else {
                                        TardisMessage.send(player, "NO_ZERO");
                                    }
                                    break;
                                case 9:
                                    // saves
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasMemory()) {
                                        TardisMessage.send(player, "NO_MEM_CIRCUIT");
                                        return;
                                    }
                                    TardisSaveSignInventory tssi = new TardisSaveSignInventory(plugin, tardis.getTardisId(), player);
                                    ItemStack[] saves = tssi.getTerminal();
                                    Inventory saved = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS saves");
                                    saved.setContents(saves);
                                    player.openInventory(saved);
                                    break;
                                case 11:
                                    // desktop theme
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (!TardisPermission.hasPermission(player, "tardis.upgrade")) {
                                        TardisMessage.send(player, "NO_PERM_UPGRADE");
                                        return;
                                    }
                                    new TardisThemeButton(plugin, player, tardis.getSchematic(), level, id).clickButton();
                                    break;
                                case 13:
                                    // siege
                                    if (tcc != null && !tcc.hasMaterialisation()) {
                                        TardisMessage.send(player, "NO_MAT_CIRCUIT");
                                        return;
                                    }
                                    close(player, true);
                                    new TardisSiegeButton(plugin, player, tardis.isPowered(), id).clickButton();
                                    break;
                                case 15:
                                    // scanner
                                    close(player, false);
                                    TardisScanner.scan(player, id, plugin.getServer().getScheduler());
                                    break;
                                case 17:
                                    //player prefs
                                    Inventory ppm = plugin.getServer().createInventory(player, 36, ChatColor.DARK_RED + "Player Prefs Menu");
                                    ppm.setContents(new TardisPrefsMenuInventory(plugin, player.getUniqueId()).getMenu());
                                    player.openInventory(ppm);
                                    break;
                                case 18:
                                    // fast return
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TardisMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    close(player, false);
                                    new TardisFastReturnButton(plugin, player, id, level).clickButton();
                                    break;
                                case 20:
                                    // power up/down
                                    if (plugin.getConfig().getBoolean("allow.power_down")) {
                                        close(player, true);
                                        new TardisPowerButton(plugin, id, player, tardis.getPreset(), tardis.isPowered(), tardis.isHidden(), lights, player.getLocation(), level, tardis.getSchematic().hasLanterns()).clickButton();
                                    } else {
                                        TardisMessage.send(player, "POWER_DOWN_DISABLED");
                                    }
                                    break;
                                case 22:
                                    // hide
                                    close(player, true);
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    new TardisHideCommand(plugin).hide(player);
                                    break;
                                case 24:
                                    // TIS
                                    close(player, false);
                                    new TardisInfoMenuButton(plugin, player).clickButton();
                                    break;
                                case 26:
                                    // Companions Menu
                                    String comps = tardis.getCompanions();
                                    if (comps == null || comps.isEmpty()) {
                                        close(player, true);
                                        TardisMessage.send(player, "COMPANIONS_NONE");
                                        return;
                                    }
                                    String[] companionData = comps.split(":");
                                    ItemStack[] heads = new TardisCompanionInventory(plugin, companionData).getSkulls();
                                    Inventory companions = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Companions");
                                    companions.setContents(heads);
                                    player.openInventory(companions);
                                    break;
                                case 27:
                                    // areas
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasMemory()) {
                                        TardisMessage.send(player, "NO_MEM_CIRCUIT");
                                        return;
                                    }
                                    TardisAreasInventory tai = new TardisAreasInventory(plugin, player);
                                    ItemStack[] areas = tai.getTerminal();
                                    Inventory areainv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS areas");
                                    areainv.setContents(areas);
                                    player.openInventory(areainv);
                                    break;
                                case 29:
                                    // light switch
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (!lights && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered()) {
                                        TardisMessage.send(player, "POWER_DOWN");
                                        return;
                                    }
                                    close(player, true);
                                    new TardisLightSwitch(plugin, id, lights, player, tardis.getSchematic().hasLanterns()).flickSwitch();
                                    break;
                                case 31:
                                    // rebuild
                                    close(player, true);
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    new TardisRebuildCommand(plugin).rebuildPreset(player);
                                    break;
                                case 33:
                                    // transmat
                                    ItemStack[] tran = new TardisTransmatInventory(plugin, id).getMenu();
                                    Inventory smat = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS transmats");
                                    smat.setContents(tran);
                                    player.openInventory(smat);
                                    break;
                                case 36:
                                    // destination terminal
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    if (level < plugin.getArtronConfig().getInt("travel")) {
                                        TardisMessage.send(player, "NOT_ENOUGH_ENERGY");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TardisMessage.send(player, "INPUT_MISSING");
                                        return;
                                    }
                                    ItemStack[] items = new TardisTerminalInventory(plugin).getTerminal();
                                    Inventory aec = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Destination Terminal");
                                    aec.setContents(items);
                                    player.openInventory(aec);
                                    break;
                                case 38:
                                    // toggle wool
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    close(player, true);
                                    new TardisBlackWoolToggler(plugin).toggleBlocks(id, player);
                                    break;
                                case 40:
                                    // direction
                                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                                        TardisMessage.send(player, "SIEGE_NO_CONTROL");
                                        return;
                                    }
                                    HashMap<String, Object> whered = new HashMap<>();
                                    whered.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, whered);
                                    String direction = "EAST";
                                    if (rsc.resultSet()) {
                                        direction = rsc.getDirection().toString();
                                        int ordinal = CardinalDirection.valueOf(direction).ordinal() + 1;
                                        if (ordinal == 4) {
                                            ordinal = 0;
                                        }
                                        direction = CardinalDirection.values()[ordinal].toString();
                                    }
                                    String[] args = new String[]{"direction", direction};
                                    new TardisDirectionCommand(plugin).changeDirection(player, args);
                                    // update the lore
                                    ItemStack d = view.getItem(40);
                                    assert d != null;
                                    ItemMeta im = d.getItemMeta();
                                    assert im != null;
                                    im.setLore(Collections.singletonList(direction));
                                    d.setItemMeta(im);
                                    break;
                                case 47:
                                    // tardis map
                                    Inventory new_inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Map");
                                    // open new inventory
                                    new_inv.setContents(new TardisArsMap(plugin).getMap());
                                    player.openInventory(new_inv);
                                    break;
                                case 49:
                                    // temporal
                                    if (!TardisPermission.hasPermission(player, "tardis.temporal")) {
                                        TardisMessage.send(player, "NO_PERM_TEMPORAL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasTemporal() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TardisMessage.send(player, "TEMP_MISSING");
                                        return;
                                    }
                                    ItemStack[] clocks = new TardisTemporalLocatorInventory(plugin).getTemporal();
                                    Inventory tmpl = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Temporal Locator");
                                    tmpl.setContents(clocks);
                                    player.openInventory(tmpl);
                                    break;
                                case 53:
                                    // close
                                    close(player, false);
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

    private void close(Player p, boolean check) {
        boolean close = true;
        if (check) {
            ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, p.getUniqueId().toString());
            if (rspp.resultSet()) {
                close = rspp.isCloseGuiOn();
            }
        }
        if (close) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory, 1L);
        }
    }
}