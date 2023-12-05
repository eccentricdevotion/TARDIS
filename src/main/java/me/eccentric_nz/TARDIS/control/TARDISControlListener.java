/*
 * Copyright (C) 2023 eccentric_nz
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

import me.eccentric_nz.TARDIS.ARS.TARDISARSInventory;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISSerializeInventory;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.utils.TARDISWeatherInventory;
import me.eccentric_nz.TARDIS.control.actions.*;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.floodgate.*;
import me.eccentric_nz.TARDIS.hads.TARDISCloisterBell;
import me.eccentric_nz.TARDIS.maze.TARDISMazeBuilder;
import me.eccentric_nz.TARDIS.maze.TARDISMazeGenerator;
import me.eccentric_nz.TARDIS.move.TARDISBlackWoolToggler;
import me.eccentric_nz.TARDIS.travel.TARDISTemporalLocatorInventory;
import me.eccentric_nz.TARDIS.travel.TARDISTerminalInventory;
import me.eccentric_nz.TARDIS.utility.TARDISCustardCreamDispenser;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.*;

/**
 * The various systems of the console room are fairly well-understood. According
 * to one account, each of the six panels controls a discrete function. The
 * navigation panel contains a time and space forward/back control, directional
 * pointer, atom accelerator and the spatial location input.
 *
 * @author eccentric_nz
 */
public class TARDISControlListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validBlocks = new ArrayList<>();
    private final List<Integer> onlythese = Arrays.asList(1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13, 14, 16, 17, 20, 21, 22, 25, 26, 28, 29, 30, 31, 32, 33, 35, 38, 39, 40, 41, 42, 43);
    private final Set<UUID> cooldown = new HashSet<>();

    public TARDISControlListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.add(Material.COMPARATOR);
        validBlocks.add(Material.DISPENSER);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.MUSHROOM_STEM);
        validBlocks.add(Material.BARRIER); // new Item Display custom blocks
        validBlocks.add(Material.NOTE_BLOCK);
        validBlocks.add(Material.REPEATER);
        validBlocks.add(Material.STONE_PRESSURE_PLATE);
        validBlocks.addAll(Tag.SIGNS.getValues());
        validBlocks.addAll(Tag.BUTTONS.getValues());
        validBlocks.addAll(Tag.WOODEN_PRESSURE_PLATES.getValues());
    }

    /**
     * Listens for player interaction with the TARDIS console button. If the
     * button is clicked it will return a random destination based on the
     * settings of the four TARDIS console repeaters.
     *
     * @param event the player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onControlInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if ((event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) && action != Action.PHYSICAL) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            // only proceed if they are clicking a valid block type!
            if (validBlocks.contains(blockType)) {
                // get clicked block location
                Location blockLocation = block.getLocation();
                // get tardis from control block location
                String locStr = blockLocation.toString();
                if (blockType.equals(Material.REPEATER)) {
                    Repeater repeater = (Repeater) block.getBlockData();
                    if (!repeater.isLocked()) {
                        locStr = blockLocation.getWorld().getName() + ":" + blockLocation.blockX() + ":" + blockLocation.blockY() + ":" + blockLocation.blockZ();
                    }
                }
                HashMap<String, Object> where = new HashMap<>();
                where.put("location", locStr);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    int id = rsc.getTardis_id();
                    int type = rsc.getType();
                    if (plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId()) && type != 13) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ISO_HANDS_OFF");
                        return;
                    }
                    if (!onlythese.contains(type)) {
                        return;
                    }
                    Control control = Control.getById().get(type);
                    HashMap<String, Object> whereid = new HashMap<>();
                    whereid.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false, 0);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        if (tardis.getPreset().equals(ChameleonPreset.JUNK)) {
                            return;
                        }
                        // check they initialised
                        if (!tardis.isTardis_init()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                            return;
                        }
                        // check isomorphic controls
                        if (tardis.isIso_on() && !player.getUniqueId().equals(tardis.getUuid())) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ISO_HANDS_OFF");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on() && !control.allowUnpowered()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id) && control.isNoSiege()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                            return;
                        }
                        boolean lights = tardis.isLights_on();
                        if (!lights && type == 12 && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return;
                        }
                        int level = tardis.getArtron_level();
                        boolean hb = tardis.isHandbrake_on();
                        UUID ownerUUID = tardis.getUuid();
                        UUID playerUUID = player.getUniqueId();
                        TARDISCircuitChecker tcc = null;
                        if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                            tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        if (action == Action.RIGHT_CLICK_BLOCK) {
                            switch (type) {
                                case 1 -> { // random location button
                                    if (cooldown.contains(player.getUniqueId())) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SPAM_WAIT");
                                        return;
                                    }
                                    cooldown.add(player.getUniqueId());
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> cooldown.remove(player.getUniqueId()), 60L);
                                    if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!hb && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().add(id);
                                    }
                                    new TARDISRandomButton(plugin, player, id, level, rsc.getSecondary(), tardis.getCompanions(), tardis.getUuid()).clickButton();
                                }
                                case 2, 3, 4, 5 -> { // console repeaters - message setting when clicked
                                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, playerUUID.toString());
                                    if (rsp.resultSet() && rsp.isAnnounceRepeatersOn()) {
                                        Repeater repeater = (Repeater) block.getBlockData();
                                        int delay = repeater.getDelay();
                                        if (delay == 4) {
                                            delay = 0;
                                        }
                                        RepeaterControl rc = RepeaterControl.getControl(type);
                                        plugin.getMessenger().announceRepeater(player, rc.getDescriptions().get(delay));
                                    }
                                }
                                case 8 -> { // fast return button
                                    if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!hb && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().add(id);
                                    }
                                    new TARDISFastReturnButton(plugin, player, id, level).clickButton();
                                }
                                case 9 -> { // terminal sign
                                    if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!hb && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().add(id);
                                    }
                                    if (level < plugin.getArtronConfig().getInt("travel")) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasInput() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "INPUT_MISSING");
                                        return;
                                    }
                                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                                        new FloodgateDestinationTerminalForm(plugin, playerUUID).send();
                                    } else {
                                        ItemStack[] items = new TARDISTerminalInventory(plugin).getTerminal();
                                        Inventory aec = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Destination Terminal");
                                        aec.setContents(items);
                                        player.openInventory(aec);
                                    }
                                }
                                case 10 -> { // ARS sign
                                    if (!hb) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_NO_TRAVEL");
                                        return;
                                    }
                                    // check they're in a compatible world
                                    if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ROOM_OWN_WORLD");
                                        return;
                                    }
                                    if (player.isSneaking()) {
                                        // check they have permission to change the desktop
                                        if (!TARDISPermission.hasPermission(player, "tardis.upgrade")) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_UPGRADE");
                                            return;
                                        }
                                        if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_MISSING");
                                            return;
                                        }
                                        // upgrade menu
                                        new TARDISThemeButton(plugin, player, tardis.getSchematic(), level, id).clickButton();
                                    } else {
                                        // check they have permission to grow rooms
                                        if (!TARDISPermission.hasPermission(player, "tardis.architectural")) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_ROOMS");
                                            return;
                                        }
                                        if (tcc != null && !tcc.hasARS() && !plugin.getUtils().inGracePeriod(player, true)) {
                                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARS_MISSING");
                                            return;
                                        }
                                        ItemStack[] tars = new TARDISARSInventory(plugin, player).getARS();
                                        Inventory ars = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Architectural Reconfiguration");
                                        ars.setContents(tars);
                                        player.openInventory(ars);
                                    }
                                }
                                case 11 -> { // Temporal Locator sign
                                    if (!TARDISPermission.hasPermission(player, "tardis.temporal")) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERM_TEMPORAL");
                                        return;
                                    }
                                    if (tcc != null && !tcc.hasTemporal() && !plugin.getUtils().inGracePeriod(player, false)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TEMP_MISSING");
                                        return;
                                    }
                                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                                        new FloodgateTemporalForm(plugin, playerUUID).send();
                                    } else {
                                        ItemStack[] clocks = new TARDISTemporalLocatorInventory(plugin).getTemporal();
                                        Inventory tmpl = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Temporal Locator");
                                        tmpl.setContents(clocks);
                                        player.openInventory(tmpl);
                                    }
                                }
                                case 12 -> // Control room light switch
                                        new TARDISLightSwitch(plugin, id, lights, player, tardis.getSchematic().getLights()).flickSwitch();
                                case 13 -> // TIS
                                        new TARDISInfoMenuButton(plugin, player).clickButton();
                                case 14 -> { // Disk Storage
                                    if (plugin.getTrackerKeeper().getUpdatePlayers().containsKey(playerUUID)) {
                                        return;
                                    }
                                    // only the time lord of this tardis
                                    if (!ownerUUID.equals(playerUUID)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_OWNER");
                                        return;
                                    }
                                    // do they have a storage record?
                                    HashMap<String, Object> wherestore = new HashMap<>();
                                    wherestore.put("uuid", playerUUID);
                                    ResultSetDiskStorage rsstore = new ResultSetDiskStorage(plugin, wherestore);
                                    ItemStack[] stack = new ItemStack[54];
                                    if (rsstore.resultSet()) {
                                        try {
                                            if (!rsstore.getSavesOne().isEmpty()) {
                                                stack = TARDISSerializeInventory.itemStacksFromString(rsstore.getSavesOne());
                                            } else {
                                                stack = TARDISSerializeInventory.itemStacksFromString(Storage.SAVE_1.getEmpty());
                                            }
                                        } catch (IOException ex) {
                                            plugin.debug("Could not get Storage Inventory: " + ex.getMessage());
                                        }
                                    } else {
                                        try {
                                            stack = TARDISSerializeInventory.itemStacksFromString(Storage.SAVE_1.getEmpty());
                                            for (ItemStack is : stack) {
                                                if (is != null && is.hasItemMeta()) {
                                                    ItemMeta im = is.getItemMeta();
                                                    if (im.hasDisplayName()) {
                                                        if (is.getType().equals(Material.FILLED_MAP)) {
                                                            GlowstoneCircuit glowstone = GlowstoneCircuit.getByName().get(im.getDisplayName());
                                                            if (glowstone != null) {
                                                                im.setCustomModelData(glowstone.getCustomModelData());
                                                                is.setType(Material.GLOWSTONE_DUST);
                                                                is.setItemMeta(im);
                                                            }
                                                        } else {
                                                            if (TARDISStaticUtils.isMusicDisk(is)) {
                                                                im.setCustomModelData(10000001);
                                                            } else if (is.getType().equals(Material.LIME_WOOL)) {
                                                                im.setCustomModelData(86);
                                                                is.setType(Material.BOWL);
                                                                is.setItemMeta(im);
                                                            } else if (is.getType().equals(Material.RED_WOOL)) {
                                                                im.setCustomModelData(87);
                                                                is.setType(Material.BOWL);
                                                                is.setItemMeta(im);
                                                            } else if (is.getType().equals(Material.GLOWSTONE_DUST) && !im.hasCustomModelData() && im.getDisplayName().equals("Circuits")) {
                                                                im.setCustomModelData(10001985);
                                                            }
                                                            is.setItemMeta(im);
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (IOException ex) {
                                            plugin.debug("Could not get default Storage Inventory: " + ex.getMessage());
                                        }
                                        // make a record
                                        HashMap<String, Object> setstore = new HashMap<>();
                                        setstore.put("uuid", player.getUniqueId().toString());
                                        setstore.put("tardis_id", id);
                                        plugin.getQueryFactory().doInsert("storage", setstore);
                                    }
                                    Inventory inv = plugin.getServer().createInventory(player, 54, Storage.SAVE_1.getTitle());
                                    inv.setContents(stack);
                                    player.openInventory(inv);
                                    // update note block if it's not BARRIER + Item Display
                                    if (!TARDISFloodgate.isFloodgateEnabled() || !TARDISFloodgate.isBedrockPlayer(player.getUniqueId())) {
                                        if (blockType.equals(Material.NOTE_BLOCK) || block.getType().equals(Material.MUSHROOM_STEM)) {
                                            block.setBlockData(TARDISConstants.BARRIER, true);
                                            TARDISDisplayItemUtils.set(TARDISDisplayItem.DISK_STORAGE, block);
                                        }
                                    }
                                }
                                // enter zero room
                                case 16 -> new ZeroRoomAction(plugin).doEntry(level, player, tardis.getZero(), id);
                                // exit zero room
                                case 17 -> new ZeroRoomAction(plugin).doExit(player, id);
                                // toggle black wool blocks behind door
                                case 20 -> new TARDISBlackWoolToggler(plugin).toggleBlocks(id, player);
                                case 21 -> {
                                    // siege lever
                                    if (tcc != null && !tcc.hasMaterialisation()) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MAT_CIRCUIT");
                                        return;
                                    }
                                    new TARDISSiegeButton(plugin, player, tardis.isPowered_on(), id).clickButton();
                                }
                                // open control menu GUI
                                case 22 -> new ControlMenuAction(plugin).openGUI(player, id);
                                // shell room button
                                case 25 -> new ShellRoomAction(plugin).openGUI(player, id);
                                // Handles
                                case 26 -> new HandlesAction(plugin).cyberIt(player);
                                case 28 -> {
                                    // Custard Cream Dispenser
                                    event.setCancelled(true);
                                    new TARDISCustardCreamDispenser(plugin, player, block, id).dispense();
                                }
                                // force field
                                case 29 -> new ForceFieldAction(plugin).toggleSheilds(player, blockLocation, level);
                                // flight mode button
                                case 30 -> new FlightModeAction(plugin).setMode(ownerUUID.toString(), player);
                                // chameleon sign
                                case 31 -> new ChameleonSignAction(plugin).openGUI(player, tardis, id);
                                case 32 -> {
                                    // save_sign
                                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                                        new FloodgateSavesForm(plugin, playerUUID, id).send();
                                    } else {
                                        new TARDISSaveSign(plugin).openGUI(player, id);
                                    }
                                }
                                case 33 ->
                                    // scanner
                                        new TARDISScanner(plugin).scan(player, id, tardis.getRenderer(), level);
                                case 35 -> {
                                    // cloister bell
                                    if (plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
                                        plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getCloisterBells().get(id));
                                        plugin.getTrackerKeeper().getCloisterBells().remove(id);
                                    } else {
                                        TARDISCloisterBell bell = new TARDISCloisterBell(plugin, Integer.MAX_VALUE, id, plugin.getServer().getPlayer(tardis.getUuid()));
                                        int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
                                        bell.setTask(taskID);
                                        plugin.getTrackerKeeper().getCloisterBells().put(id, taskID);
                                    }
                                }
                                case 38 -> {
                                    // weather menu
                                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                                        new FloodgateWeatherForm(plugin, playerUUID).send();
                                    } else {
                                        ItemStack[] weather = new TARDISWeatherInventory(plugin).getWeatherButtons();
                                        Inventory forecast = plugin.getServer().createInventory(player, 9, ChatColor.DARK_RED + "TARDIS Weather Menu");
                                        forecast.setContents(weather);
                                        player.openInventory(forecast);
                                    }
                                }
                                case 39 -> {
                                    // space/time throttle
                                    Repeater repeater = (Repeater) block.getBlockData();
                                    // get delay
                                    int delay = repeater.getDelay() + 1;
                                    if (delay > 4) {
                                        delay = 1;
                                    }
                                    // update player prefs
                                    HashMap<String, Object> wherer = new HashMap<>();
                                    wherer.put("uuid", player.getUniqueId().toString());
                                    HashMap<String, Object> setr = new HashMap<>();
                                    setr.put("throttle", delay);
                                    plugin.getQueryFactory().doUpdate("player_prefs", setr, wherer);
                                    plugin.getMessenger().send(player, TardisModule.TARDIS, "THROTTLE", SpaceTimeThrottle.getByDelay().get(delay).toString());
                                }
                                default -> {
                                }
                            }
                        } else if (action.equals(Action.PHYSICAL)) {
                            switch (type) {
                                case 16 -> new ZeroRoomAction(plugin).doEntry(level, player, tardis.getZero(), id);
                                case 40 -> // WEST
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                            // has player moved out of the maze in a northerly direction
                                            Location playerLocation = player.getLocation();
                                            if (playerLocation.getBlockX() < blockLocation.getBlockX()) {
                                                // reconfigure maze
                                                reconfigureMaze(id);
                                            }
                                        }, 20L);
                                case 41 -> // NORTH
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                            // has player moved out of the maze in a westerly direction
                                            Location playerLocation = player.getLocation();
                                            if (playerLocation.getBlockZ() < blockLocation.getBlockZ()) {
                                                // reconfigure maze
                                                reconfigureMaze(id);
                                            }
                                        }, 20L);
                                case 42 -> // SOUTH
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                            // has player moved out of the maze in an easterly direction
                                            Location playerLocation = player.getLocation();
                                            if (playerLocation.getBlockZ() > blockLocation.getBlockZ()) {
                                                // reconfigure maze
                                                reconfigureMaze(id);
                                            }
                                        }, 20L);
                                case 43 -> // EAST
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                            // has player moved out of the maze in a southerly direction
                                            Location playerLocation = player.getLocation();
                                            if (playerLocation.getBlockX() > blockLocation.getBlockX()) {
                                                // reconfigure maze
                                                reconfigureMaze(id);
                                            }
                                        }, 20L);
                                default -> {
                                }
                            }
                        }
                    }
                } else {
                    // check for junk mode save sign?
                    if (blockType.equals(Material.OAK_WALL_SIGN)) {
                        HashMap<String, Object> wherej = new HashMap<>();
                        wherej.put("save_sign", blockLocation.toString());
                        ResultSetJunk rsj = new ResultSetJunk(plugin, wherej);
                        if (rsj.resultSet()) {
                            // save_sign
                            new TARDISSaveSign(plugin).openGUI(player, rsj.getTardis_id());
                        }
                    }
                }
            }
        }
    }

    private void reconfigureMaze(int id) {
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        wherec.put("type", 44);
        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
        if (rsc.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (location != null) {
                TARDISMazeGenerator generator = new TARDISMazeGenerator();
                generator.makeMaze();
                TARDISMazeBuilder builder = new TARDISMazeBuilder(generator.getMaze(), location);
                builder.build(true);
            }
        }
    }
}
