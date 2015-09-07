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
package me.eccentric_nz.TARDIS.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.control.TARDISControlInventory;
import me.eccentric_nz.TARDIS.control.TARDISFastReturnButton;
import me.eccentric_nz.TARDIS.control.TARDISInfoMenuButton;
import me.eccentric_nz.TARDIS.control.TARDISLightSwitch;
import me.eccentric_nz.TARDIS.control.TARDISRandomButton;
import me.eccentric_nz.TARDIS.control.TARDISSiegeButton;
import me.eccentric_nz.TARDIS.control.TARDISThemeButton;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetDiskStorage;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.enumeration.STORAGE;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.rooms.TARDISExteriorRenderer;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * The various systems of the console room are fairly well-understood. According
 * to one account, each of the six panels controls a discrete function. The
 * navigation panel contains a time and space forward/back control, directional
 * pointer, atom accelerator and the spatial location input.
 *
 * @author eccentric_nz
 */
public class TARDISButtonListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validBlocks = new ArrayList<Material>();
    private final List<Integer> onlythese = Arrays.asList(1, 8, 9, 10, 11, 12, 13, 14, 16, 17, 20, 21, 22);
    private final List<Integer> allow_unpowered = Arrays.asList(13, 17, 22);
    private final List<Integer> no_siege = Arrays.asList(0, 10, 12, 16, 19, 20);

    public TARDISButtonListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.add(Material.WOOD_BUTTON);
        validBlocks.add(Material.REDSTONE_COMPARATOR_OFF);
        validBlocks.add(Material.REDSTONE_COMPARATOR_ON);
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.WALL_SIGN);
        validBlocks.add(Material.NOTE_BLOCK);
        validBlocks.add(Material.JUKEBOX);
        validBlocks.add(Material.STONE_PLATE);
        validBlocks.add(Material.WOOD_PLATE);
    }

    /**
     * Listens for player interaction with the TARDIS console button. If the
     * button is clicked it will return a random destination based on the
     * settings of the four TARDIS console repeaters.
     *
     * @param event the player clicking a block
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onButtonInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            // only proceed if they are clicking a type of a button or a lever!
            if (validBlocks.contains(blockType)) {
                // get clicked block location
                String buttonloc = block.getLocation().toString();
                // get tardis from saved button location
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("location", buttonloc);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    int id = rsc.getTardis_id();
                    int type = rsc.getType();
                    if (plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId()) && type != 13) {
                        TARDISMessage.send(player, "ISO_HANDS_OFF");
                        return;
                    }
                    if (!onlythese.contains(type)) {
                        return;
                    }
                    HashMap<String, Object> whereid = new HashMap<String, Object>();
                    whereid.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false);
                    if (rs.resultSet()) {
                        if (rs.getPreset().equals(PRESET.JUNK)) {
                            return;
                        }
                        // check they initialised
                        if (!rs.isTardis_init()) {
                            TARDISMessage.send(player, "ENERGY_NO_INIT");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on() && !allow_unpowered.contains(type)) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id) && no_siege.contains(type)) {
                            TARDISMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        boolean lights = rs.isLights_on();
                        if (!lights && type == 12 && plugin.getConfig().getBoolean("allow.power_down") && !rs.isPowered_on()) {
                            TARDISMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        int level = rs.getArtron_level();
                        boolean hb = rs.isHandbrake_on();
                        UUID ownerUUID = rs.getUuid();
                        TARDISCircuitChecker tcc = null;
                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                            tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        QueryFactory qf = new QueryFactory(plugin);
                        if (action == Action.RIGHT_CLICK_BLOCK) {
                            switch (type) {
                                case 1: // random location button
                                    if (!hb) {
                                        TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    new TARDISRandomButton(plugin, player, id, level, 0, rs.getCompanions(), rs.getUuid()).clickButton();
                                    break;
                                case 8: // fast return button
                                    if (!hb) {
                                        TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    new TARDISFastReturnButton(plugin, player, id, level).clickButton();
                                    break;
                                case 9: // terminal sign
                                    if (!hb) {
                                        TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
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
                                    ItemStack[] items = new TARDISTerminalInventory(this.plugin).getTerminal();
                                    Inventory aec = plugin.getServer().createInventory(player, 54, "§4Destination Terminal");
                                    aec.setContents(items);
                                    player.openInventory(aec);
                                    break;
                                case 10: // ARS sign
                                    if (!hb) {
                                        TARDISMessage.send(player, "ARS_NO_TRAVEL");
                                        return;
                                    }
                                    // check they're in a compatible world
                                    if (!plugin.getUtils().canGrowRooms(rs.getChunk())) {
                                        TARDISMessage.send(player, "ROOM_OWN_WORLD");
                                        return;
                                    }
                                    if (player.isSneaking()) {
                                        // check they have permission to change the desktop
                                        if (!player.hasPermission("tardis.upgrade")) {
                                            TARDISMessage.send(player, "NO_PERM_UPGRADE");
                                            return;
                                        }
                                        if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                                            TARDISMessage.send(player, "ARS_MISSING");
                                            return;
                                        }
                                        // upgrade menu
                                        new TARDISThemeButton(plugin, player, rs.getSchematic(), level).clickButton();
                                    } else {
                                        // check they have permission to grow rooms
                                        if (!player.hasPermission("tardis.architectural")) {
                                            TARDISMessage.send(player, "NO_PERM_ROOMS");
                                            return;
                                        }
                                        if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                                            TARDISMessage.send(player, "ARS_MISSING");
                                            return;
                                        }
                                        ItemStack[] tars = new TARDISARSInventory(this.plugin).getARS();
                                        Inventory ars = plugin.getServer().createInventory(player, 54, "§4Architectural Reconfiguration");
                                        ars.setContents(tars);
                                        player.openInventory(ars);
                                    }
                                    break;
                                case 11: // Temporal Locator sign
                                    if (!player.hasPermission("tardis.temporal")) {
                                        TARDISMessage.send(player, "NO_PERM_TEMPORAL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasTemporal() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        TARDISMessage.send(player, "TEMP_MISSING");
                                        return;
                                    }
                                    ItemStack[] clocks = new TARDISTemporalLocatorInventory(this.plugin).getTemporal();
                                    Inventory tmpl = plugin.getServer().createInventory(player, 27, "§4Temporal Locator");
                                    tmpl.setContents(clocks);
                                    player.openInventory(tmpl);
                                    break;
                                case 12: // Control room light switch
                                    new TARDISLightSwitch(plugin, id, lights, player, rs.getSchematic().hasLanterns()).flickSwitch();
                                    break;
                                case 13: // TIS
                                    new TARDISInfoMenuButton(plugin, player).clickButton();
                                    break;
                                case 14: // Disk Storage
                                    UUID playerUUID = player.getUniqueId();
                                    // only the time lord of this tardis
                                    if (!ownerUUID.equals(playerUUID)) {
                                        TARDISMessage.send(player, "NOT_OWNER");
                                        return;
                                    }
                                    // do they have a storage record?
                                    HashMap<String, Object> wherestore = new HashMap<String, Object>();
                                    wherestore.put("uuid", playerUUID);
                                    ResultSetDiskStorage rsstore = new ResultSetDiskStorage(plugin, wherestore);
                                    ItemStack[] stack = new ItemStack[54];
                                    if (rsstore.resultSet()) {
                                        try {
                                            if (!rsstore.getSavesOne().isEmpty()) {
                                                stack = TARDISSerializeInventory.itemStacksFromString(rsstore.getSavesOne());
                                            } else {
                                                stack = TARDISSerializeInventory.itemStacksFromString(STORAGE.SAVE_1.getEmpty());
                                            }
                                        } catch (IOException ex) {
                                            plugin.debug("Could not get Storage Inventory: " + ex.getMessage());
                                        }
                                    } else {
                                        try {
                                            stack = TARDISSerializeInventory.itemStacksFromString(STORAGE.SAVE_1.getEmpty());
                                        } catch (IOException ex) {
                                            plugin.debug("Could not get default Storage Inventory: " + ex.getMessage());
                                        }
                                        // make a record
                                        HashMap<String, Object> setstore = new HashMap<String, Object>();
                                        setstore.put("uuid", player.getUniqueId().toString());
                                        setstore.put("tardis_id", id);
                                        qf.doInsert("storage", setstore);
                                    }
                                    Inventory inv = plugin.getServer().createInventory(player, 54, STORAGE.SAVE_1.getTitle());
                                    inv.setContents(stack);
                                    player.openInventory(inv);
                                    break;
                                case 16: // enter zero room
                                    doZero(level, player, rs.getZero(), id, qf);
                                    break;
                                case 17:
                                    // exit zero room
                                    plugin.getTrackerKeeper().getZeroRoomOccupants().remove(player.getUniqueId());
                                    plugin.getGeneralKeeper().getRendererListener().transmat(player);
                                    break;
                                case 20:
                                    // toggle black wool blocks behind door
                                    new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
                                    break;
                                case 21:
                                    // siege lever
                                    if (tcc != null && !tcc.hasMaterialisation()) {
                                        TARDISMessage.send(player, "NO_MAT_CIRCUIT");
                                        return;
                                    }
                                    new TARDISSiegeButton(plugin, player, rs.isPowered_on(), id).clickButton();
                                    break;
                                case 22:
                                    // controls GUI
                                    ItemStack[] controls = new TARDISControlInventory(plugin, player.getUniqueId()).getControls();
                                    Inventory cgui = plugin.getServer().createInventory(player, 18, "§4TARDIS Control Menu");
                                    cgui.setContents(controls);
                                    player.openInventory(cgui);
                                    break;
                                default:
                                    break;
                            }
                        } else if (action.equals(Action.PHYSICAL) && type == 16) {
                            doZero(level, player, rs.getZero(), id, qf);
                        }
                    }
                }
            }
        }
    }

    private void doZero(int level, final Player player, String z, int id, QueryFactory qf) {
        int zero_amount = plugin.getArtronConfig().getInt("zero");
        if (level < zero_amount) {
            TARDISMessage.send(player, "NOT_ENOUGH_ZERO_ENERGY");
            return;
        }
        final Location zero = TARDISLocationGetters.getLocationFromDB(z, 0.0F, 0.0F);
        if (zero != null) {
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
            qf.alterEnergyLevel("tardis", -zero_amount, wherez, player);
        } else {
            TARDISMessage.send(player, "NO_ZERO");
        }
    }
}
