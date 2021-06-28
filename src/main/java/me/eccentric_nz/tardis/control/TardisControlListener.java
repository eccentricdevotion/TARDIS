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
import me.eccentric_nz.tardis.advanced.TardisInventorySerializer;
import me.eccentric_nz.tardis.api.event.TardisZeroRoomEnterEvent;
import me.eccentric_nz.tardis.api.event.TardisZeroRoomExitEvent;
import me.eccentric_nz.tardis.ars.TardisArsInventory;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.chameleon.TardisShellRoomConstructor;
import me.eccentric_nz.tardis.commands.utils.TardisWeatherInventory;
import me.eccentric_nz.tardis.custommodeldata.TardisMushroomBlockData;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.enumeration.*;
import me.eccentric_nz.tardis.forcefield.TardisForceField;
import me.eccentric_nz.tardis.hads.TardisCloisterBell;
import me.eccentric_nz.tardis.handles.TardisHandlesProcessor;
import me.eccentric_nz.tardis.handles.TardisHandlesProgramInventory;
import me.eccentric_nz.tardis.listeners.TardisKeyboardListener;
import me.eccentric_nz.tardis.maze.TardisMazeBuilder;
import me.eccentric_nz.tardis.maze.TardisMazeGenerator;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.move.TardisBlackWoolToggler;
import me.eccentric_nz.tardis.rooms.TardisExteriorRenderer;
import me.eccentric_nz.tardis.travel.TardisTemporalLocatorInventory;
import me.eccentric_nz.tardis.travel.TardisTerminalInventory;
import me.eccentric_nz.tardis.utility.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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
 * The various systems of the console room are fairly well-understood. According to one account, each of the six panels
 * controls a discrete function. The navigation panel contains a time and space forward/back control, directional
 * pointer, atom accelerator and the spatial location input.
 *
 * @author eccentric_nz
 */
public class TardisControlListener implements Listener {

    private final TardisPlugin plugin;
    private final List<Material> validBlocks = new ArrayList<>();
    private final List<Integer> onlythese = Arrays.asList(1, 8, 9, 10, 11, 12, 13, 14, 16, 17, 20, 21, 22, 25, 26, 28, 29, 30, 31, 32, 33, 35, 38, 39, 40, 41, 42, 43);

    public TardisControlListener(TardisPlugin plugin) {
        this.plugin = plugin;
        validBlocks.add(Material.COMPARATOR);
        validBlocks.add(Material.DISPENSER);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.MUSHROOM_STEM);
        validBlocks.add(Material.NOTE_BLOCK);
        validBlocks.add(Material.REPEATER);
        validBlocks.add(Material.STONE_PRESSURE_PLATE);
        validBlocks.addAll(Tag.SIGNS.getValues());
        validBlocks.addAll(Tag.BUTTONS.getValues());
        validBlocks.addAll(Tag.WOODEN_PRESSURE_PLATES.getValues());
    }

    /**
     * Listens for player interaction with the TARDIS console button. If the button is clicked it will return a random
     * destination based on the settings of the four TARDIS console repeaters.
     *
     * @param event the player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onControlInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if ((event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND) || TardisKeyboardListener.isKeyboardEditor(event.getPlayer().getInventory().getItemInMainHand())) && action != Action.PHYSICAL) {
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
                // get tardis from saved button location
                HashMap<String, Object> where = new HashMap<>();
                where.put("location", blockLocation.toString());
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    int id = rsc.getTardisId();
                    int type = rsc.getType();
                    if (plugin.getTrackerKeeper().getJohnSmith().containsKey(player.getUniqueId()) && type != 13) {
                        TardisMessage.send(player, "ISO_HANDS_OFF");
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
                        if (tardis.getPreset().equals(Preset.JUNK)) {
                            return;
                        }
                        // check they initialised
                        if (!tardis.isTardisInit()) {
                            TardisMessage.send(player, "ENERGY_NO_INIT");
                            return;
                        }
                        // check isomorphic controls
                        if (tardis.isIsoOn() && !player.getUniqueId().equals(tardis.getUuid())) {
                            TardisMessage.send(player, "ISO_HANDS_OFF");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered() && !control.allowUnpowered()) {
                            TardisMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id) && control.isNoSiege()) {
                            TardisMessage.send(player, "SIEGE_NO_CONTROL");
                            return;
                        }
                        boolean lights = tardis.isLightsOn();
                        if (!lights && type == 12 && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered()) {
                            TardisMessage.send(player, "POWER_DOWN");
                            return;
                        }
                        int level = tardis.getArtronLevel();
                        boolean hb = tardis.isHandbrakeOn();
                        UUID ownerUuid = tardis.getUuid();
                        TardisCircuitChecker tcc = null;
                        if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                            tcc = new TardisCircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        if (action == Action.RIGHT_CLICK_BLOCK) {
                            switch (type) {
                                case 1: // random location button
                                    if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!hb && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        TardisMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().add(id);
                                    }
                                    new TardisRandomButton(plugin, player, id, level, rsc.getSecondary(), tardis.getCompanions(), tardis.getUuid()).clickButton();
                                    break;
                                case 8: // fast return button
                                    if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!hb && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        TardisMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().add(id);
                                    }
                                    new TardisFastReturnButton(plugin, player, id, level).clickButton();
                                    break;
                                case 9: // terminal sign
                                    if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!hb && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
                                        TardisMessage.send(player, "NOT_WHILE_TRAVELLING");
                                        return;
                                    }
                                    if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                                        plugin.getTrackerKeeper().getHasRandomised().add(id);
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
                                case 10: // ARS sign
                                    if (!hb) {
                                        TardisMessage.send(player, "ARS_NO_TRAVEL");
                                        return;
                                    }
                                    // check they're in a compatible world
                                    if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
                                        TardisMessage.send(player, "ROOM_OWN_WORLD");
                                        return;
                                    }
                                    if (player.isSneaking()) {
                                        // check they have permission to change the desktop
                                        if (!TardisPermission.hasPermission(player, "tardis.upgrade")) {
                                            TardisMessage.send(player, "NO_PERM_UPGRADE");
                                            return;
                                        }
                                        if (tcc != null && !tcc.hasArs() && !plugin.getUtils().inGracePeriod(player, true)) {
                                            TardisMessage.send(player, "ARS_MISSING");
                                            return;
                                        }
                                        // upgrade menu
                                        new TardisThemeButton(plugin, player, tardis.getSchematic(), level, id).clickButton();
                                    } else {
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
                                    }
                                    break;
                                case 11: // Temporal Locator sign
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
                                case 12: // Control room light switch
                                    new TardisLightSwitch(plugin, id, lights, player, tardis.getSchematic().hasLanterns()).flickSwitch();
                                    break;
                                case 13: // TIS
                                    new TardisInfoMenuButton(plugin, player).clickButton();
                                    break;
                                case 14: // Disk Storage
                                    UUID playerUuid = player.getUniqueId();
                                    if (plugin.getTrackerKeeper().getPlayers().containsKey(playerUuid)) {
                                        return;
                                    }
                                    // only the time lord of this tardis
                                    if (!ownerUuid.equals(playerUuid)) {
                                        TardisMessage.send(player, "NOT_OWNER");
                                        return;
                                    }
                                    // do they have a storage record?
                                    HashMap<String, Object> wherestore = new HashMap<>();
                                    wherestore.put("uuid", playerUuid);
                                    ResultSetDiskStorage rsstore = new ResultSetDiskStorage(plugin, wherestore);
                                    ItemStack[] stack = new ItemStack[54];
                                    if (rsstore.resultSet()) {
                                        try {
                                            if (!rsstore.getSavesOne().isEmpty()) {
                                                stack = TardisInventorySerializer.itemStacksFromString(rsstore.getSavesOne());
                                            } else {
                                                stack = TardisInventorySerializer.itemStacksFromString(Storage.SAVE_1.getEmpty());
                                            }
                                        } catch (IOException ex) {
                                            plugin.debug("Could not get Storage Inventory: " + ex.getMessage());
                                        }
                                    } else {
                                        try {
                                            stack = TardisInventorySerializer.itemStacksFromString(Storage.SAVE_1.getEmpty());
                                            for (ItemStack is : stack) {
                                                if (is != null && is.hasItemMeta()) {
                                                    ItemMeta im = is.getItemMeta();
                                                    assert im != null;
                                                    if (im.hasDisplayName()) {
                                                        if (is.getType().equals(Material.FILLED_MAP)) {
                                                            GlowstoneCircuit glowstone = GlowstoneCircuit.getByName().get(im.getDisplayName());
                                                            if (glowstone != null) {
                                                                im.setCustomModelData(glowstone.getCustomModelData());
                                                                is.setType(Material.GLOWSTONE_DUST);
                                                                is.setItemMeta(im);
                                                            }
                                                        } else {
                                                            if (TardisStaticUtils.isMusicDisk(is)) {
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
                                    // update note block if it's not MUSHROOM_STEM
                                    if (blockType.equals(Material.NOTE_BLOCK)) {
                                        BlockData mushroom = plugin.getServer().createBlockData(TardisMushroomBlockData.MUSHROOM_STEM_DATA.get(51));
                                        block.setBlockData(mushroom, true);
                                    }
                                    break;
                                case 16: // enter zero room
                                    doZero(level, player, tardis.getZero(), id);
                                    break;
                                case 17:
                                    // exit zero room
                                    plugin.getTrackerKeeper().getZeroRoomOccupants().remove(player.getUniqueId());
                                    plugin.getGeneralKeeper().getRendererListener().transmat(player);
                                    plugin.getPluginManager().callEvent(new TardisZeroRoomExitEvent(player, id));
                                    break;
                                case 20:
                                    // toggle black wool blocks behind door
                                    new TardisBlackWoolToggler(plugin).toggleBlocks(id, player);
                                    break;
                                case 21:
                                    // siege lever
                                    if (tcc != null && !tcc.hasMaterialisation()) {
                                        TardisMessage.send(player, "NO_MAT_CIRCUIT");
                                        return;
                                    }
                                    new TardisSiegeButton(plugin, player, tardis.isPowered(), id).clickButton();
                                    break;
                                case 22:
                                    if (player.isSneaking()) {
                                        return;
                                        // keyboard
                                    } else {
                                        // controls GUI
                                        ItemStack[] controls = new TardisControlInventory(plugin, player.getUniqueId()).getControls();
                                        Inventory cgui = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Control Menu");
                                        cgui.setContents(controls);
                                        player.openInventory(cgui);
                                    }
                                    break;
                                case 25:
                                    // shell room button
                                    new TardisShellRoomConstructor(plugin).createShell(player, id, block);
                                    break;
                                case 26:
                                    // Handles
                                    if (!TardisPermission.hasPermission(player, "tardis.handles.use")) {
                                        TardisMessage.send(player, "NO_PERMS");
                                        return;
                                    }
                                    TardisSounds.playTardisSound(player, "handles", 5L);
                                    if (!TardisPermission.hasPermission(player, "tardis.handles.program")) {
                                        TardisMessage.send(player, "NO_PERMS");
                                        return;
                                    }
                                    if (player.isSneaking()) {
                                        // open programming GUI
                                        ItemStack[] handles = new TardisHandlesProgramInventory(plugin, 0).getHandles();
                                        Inventory hgui = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Handles Program");
                                        hgui.setContents(handles);
                                        player.openInventory(hgui);
                                    } else {
                                        // check if item in hand is a Handles program disk
                                        ItemStack disk = player.getInventory().getItemInMainHand();
                                        if (disk.getType().equals(Material.MUSIC_DISC_WARD) && disk.hasItemMeta()) {
                                            ItemMeta dim = disk.getItemMeta();
                                            assert dim != null;
                                            if (dim.hasDisplayName() && ChatColor.stripColor(dim.getDisplayName()).equals("Handles Program Disk")) {
                                                // get the program_id from the disk
                                                int pid = TardisNumberParsers.parseInt(Objects.requireNonNull(dim.getLore()).get(1));
                                                // query the database
                                                ResultSetProgram rsp = new ResultSetProgram(plugin, pid);
                                                if (rsp.resultSet()) {
                                                    // send program to processor
                                                    new TardisHandlesProcessor(plugin, rsp.getProgram(), player, pid).processDisk();
                                                    // check in the disk
                                                    HashMap<String, Object> set = new HashMap<>();
                                                    set.put("checked", 0);
                                                    HashMap<String, Object> wherep = new HashMap<>();
                                                    wherep.put("program_id", pid);
                                                    plugin.getQueryFactory().doUpdate("programs", set, wherep);
                                                    player.getInventory().setItemInMainHand(null);
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case 28:
                                    // Custard Cream Dispenser
                                    event.setCancelled(true);
                                    new TardisCustardCreamDispenser(plugin, player, block, id).dispense();
                                    break;
                                case 29:
                                    // force field
                                    if (TardisPermission.hasPermission(player, "tardis.forcefield")) {
                                        if (plugin.getTrackerKeeper().getActiveForceFields().containsKey(player.getUniqueId())) {
                                            plugin.getTrackerKeeper().getActiveForceFields().remove(player.getUniqueId());
                                            TardisSounds.playTardisSound(blockLocation, "tardis_force_field_down");
                                            TardisMessage.send(player, "FORCE_FIELD", "OFF");
                                        } else {
                                            // check there is enough artron
                                            if (level <= plugin.getArtronConfig().getInt("standby")) {
                                                TardisMessage.send(player, "POWER_LOW");
                                                return;
                                            }
                                            if (TardisForceField.addToTracker(player)) {
                                                TardisSounds.playTardisSound(blockLocation, "tardis_force_field_up");
                                                TardisMessage.send(player, "FORCE_FIELD", "ON");
                                            }
                                        }
                                    }
                                    break;
                                case 30:
                                    // flight mode button
                                    // get current flight mode
                                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, ownerUuid.toString());
                                    if (rsp.resultSet()) {
                                        int mode = rsp.getFlightMode() + 1;
                                        if (mode > 3) {
                                            mode = 1;
                                        }
                                        // set flight mode
                                        HashMap<String, Object> setf = new HashMap<>();
                                        setf.put("flying_mode", mode);
                                        HashMap<String, Object> wheref = new HashMap<>();
                                        wheref.put("uuid", player.getUniqueId().toString());
                                        plugin.getQueryFactory().doUpdate("player_prefs", setf, wheref);
                                        TardisMessage.send(player, "FLIGHT_TOGGLED", FlightMode.getByMode().get(mode).toString());
                                    }
                                    break;
                                case 31:
                                    // chameleon sign
                                    new TardisChameleonControl(plugin).openGui(player, id, tardis.getAdaption(), tardis.getPreset());
                                    break;
                                case 32:
                                    // save_sign
                                    new TardisSaveSign(plugin).openGui(player, id);
                                    break;
                                case 33:
                                    // scanner
                                    new TardisScanner(plugin).scan(player, id, tardis.getRenderer(), level);
                                    break;
                                case 35:
                                    // cloister bell
                                    if (plugin.getTrackerKeeper().getCloisterBells().containsKey(id)) {
                                        plugin.getServer().getScheduler().cancelTask(plugin.getTrackerKeeper().getCloisterBells().get(id));
                                        plugin.getTrackerKeeper().getCloisterBells().remove(id);
                                    } else {
                                        TardisCloisterBell bell = new TardisCloisterBell(plugin, Integer.MAX_VALUE, id, plugin.getServer().getPlayer(tardis.getUuid()));
                                        int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, bell, 2L, 70L);
                                        bell.setTask(taskId);
                                        plugin.getTrackerKeeper().getCloisterBells().put(id, taskId);
                                    }
                                    break;
                                case 38:
                                    // weather menu
                                    ItemStack[] weather = new TardisWeatherInventory(plugin).getWeatherButtons();
                                    Inventory forecast = plugin.getServer().createInventory(player, 9, ChatColor.DARK_RED + "TARDIS Weather Menu");
                                    forecast.setContents(weather);
                                    player.openInventory(forecast);
                                    break;
                                case 39:
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
                                    TardisMessage.send(player, "THROTTLE", SpaceTimeThrottle.getByDelay().get(delay).toString());
                                    break;
                                default:
                                    break;
                            }
                        } else if (action.equals(Action.PHYSICAL)) {
                            switch (type) {
                                case 16:
                                    doZero(level, player, tardis.getZero(), id);
                                    break;
                                case 40: // WEST
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        // has player moved out of the maze  in a northerly direction
                                        Location playerLocation = player.getLocation();
                                        if (playerLocation.getBlockX() < blockLocation.getBlockX()) {
                                            // reconfigure maze
                                            reconfigureMaze(id);
                                        }
                                    }, 20L);
                                    break;
                                case 41: // NORTH
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        // has player moved out of the maze  in a westerly direction
                                        Location playerLocation = player.getLocation();
                                        if (playerLocation.getBlockZ() < blockLocation.getBlockZ()) {
                                            // reconfigure maze
                                            reconfigureMaze(id);
                                        }
                                    }, 20L);
                                    break;
                                case 42: // SOUTH
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        // has player moved out of the maze  in an easterly direction
                                        Location playerLocation = player.getLocation();
                                        if (playerLocation.getBlockZ() > blockLocation.getBlockZ()) {
                                            // reconfigure maze
                                            reconfigureMaze(id);
                                        }
                                    }, 20L);
                                    break;
                                case 43: // EAST
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        // has player moved out of the maze  in a southerly direction
                                        Location playerLocation = player.getLocation();
                                        if (playerLocation.getBlockX() > blockLocation.getBlockX()) {
                                            // reconfigure maze
                                            reconfigureMaze(id);
                                        }
                                    }, 20L);
                                    break;
                                default:
                                    break;
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
                            new TardisSaveSign(plugin).openGui(player, rsj.getTardisId());
                        }
                    }
                }
            }
        }
    }

    private void doZero(int level, Player player, String z, int id) {
        int zero_amount = plugin.getArtronConfig().getInt("zero");
        if (level < zero_amount) {
            TardisMessage.send(player, "NOT_ENOUGH_ZERO_ENERGY");
            return;
        }
        Location zero = TardisStaticLocationGetters.getLocationFromDB(z);
        if (zero != null) {
            TardisMessage.send(player, "ZERO_READY");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                new TardisExteriorRenderer(plugin).transmat(player, CardinalDirection.SOUTH, zero);
                plugin.getPluginManager().callEvent(new TardisZeroRoomEnterEvent(player, id));
            }, 20L);
            plugin.getTrackerKeeper().getZeroRoomOccupants().add(player.getUniqueId());
            HashMap<String, Object> wherez = new HashMap<>();
            wherez.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("tardis", -zero_amount, wherez, player);
        } else {
            TardisMessage.send(player, "NO_ZERO");
        }
    }

    private void reconfigureMaze(int id) {
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        wherec.put("type", 44);
        ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
        if (rsc.resultSet()) {
            Location location = TardisStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
            if (location != null) {
                TardisMazeGenerator generator = new TardisMazeGenerator();
                generator.makeMaze();
                TardisMazeBuilder builder = new TardisMazeBuilder(generator.getMaze(), location);
                builder.build(true);
            }
        }
    }
}