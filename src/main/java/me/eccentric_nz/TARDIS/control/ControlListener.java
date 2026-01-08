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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.CircuitChecker;
import me.eccentric_nz.TARDIS.control.actions.*;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetJunk;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.floodgate.FloodgateSavesForm;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.move.BlackWoolToggler;
import me.eccentric_nz.TARDIS.rooms.architectural.ArchitecturalReconfiguration;
import me.eccentric_nz.TARDIS.rooms.eye.EyeOfHarmonyAction;
import me.eccentric_nz.TARDIS.rooms.happy.HappyGhastRelease;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
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

import java.util.*;

/**
 * The various systems of the console room are fairly well-understood. According to one account, each of the six panels
 * controls a discrete function. The navigation panel contains a time and space forward/back control, directional
 * pointer, atom accelerator and the spatial location input.
 *
 * @author eccentric_nz
 */
public class ControlListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validBlocks = new ArrayList<>();
    private final List<Integer> onlythese = List.of(1, 2, 3, 4, 5, 8, 9, 10, 11, 12, 13, 14, 16, 17, 20, 21, 22, 25, 26, 28, 29, 30, 31, 32, 33, 35, 38, 39, 40, 41, 42, 43, 47, 54, 55, 58, 59);
    private final Set<UUID> cooldown = new HashSet<>();

    public ControlListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.add(Material.COMPARATOR);
        validBlocks.add(Material.DISPENSER);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.MUSHROOM_STEM);
        validBlocks.add(Material.BARRIER); // new Item Display custom blocks
        validBlocks.add(Material.NOTE_BLOCK);
        validBlocks.add(Material.REPEATER);
        validBlocks.add(Material.STONE_PRESSURE_PLATE);
        validBlocks.add(Material.GRAY_SHULKER_BOX);
        validBlocks.addAll(Tag.ALL_SIGNS.getValues());
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
                        locStr = blockLocation.getWorld().getName() + ":" + blockLocation.getBlockX() + ":" + blockLocation.getBlockY() + ":" + blockLocation.getBlockZ();
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
                    ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false);
                    if (rs.resultSet()) {
                        Tardis tardis = rs.getTardis();
                        if (tardis.getPreset().equals(ChameleonPreset.JUNK)) {
                            return;
                        }
                        // check they initialised
                        if (!tardis.isTardisInit()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NO_INIT");
                            return;
                        }
                        // check isomorphic controls
                        if (tardis.isIsomorphicOn() && !player.getUniqueId().equals(tardis.getUuid())) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "ISO_HANDS_OFF");
                            return;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn() && !control.allowUnpowered()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return;
                        }
                        if (plugin.getTrackerKeeper().getInSiegeMode().contains(id) && control.isNoSiege()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "SIEGE_NO_CONTROL");
                            return;
                        }
                        boolean lights = tardis.isLightsOn();
                        if (!lights && type == 12 && plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "POWER_DOWN");
                            return;
                        }
                        UUID ownerUUID = tardis.getUuid();
                        UUID playerUUID = player.getUniqueId();
                        CircuitChecker tcc = null;
                        if (plugin.getConfig().getBoolean("difficulty.circuits")) {
                            tcc = new CircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        if (action == Action.RIGHT_CLICK_BLOCK) {
                            switch (type) {
                                // random location button
                                case 1 -> new RandomAction(plugin).process(cooldown, player, id, tardis, rsc.getSecondary());
                                // console repeaters
                                case 2, 3, 4, 5 -> new RepeaterAction(plugin).announce(player, block, type);
                                // fast return button
                                case 8 -> new FastReturnAction(plugin).clickButton(player, id, tardis);
                                // terminal sign
                                case 9 -> new TerminalAction(plugin).openGUI(player, id, tardis, tcc);
                                // ARS sign
                                case 10 -> new ARSAction(plugin).openGUI(player, tardis, tcc, id);
                                // Temporal Locator sign
                                case 11 -> new TemporarlLocatorAction(plugin).openGUI(player, tcc);
                                // Control room light switch
                                case 12 -> new LightSwitchAction(plugin, id, lights, player, tardis.getSchematic().getLights()).flickSwitch();
                                // TIS
                                case 13 -> new TARDISInfoMenuButton(plugin, player).clickButton();
                                // Disk Storage
                                case 14 -> new DiskStorageAction(plugin).openGUI(ownerUUID, player, id, block);
                                // enter zero room
                                case 16 -> new ZeroRoomAction(plugin).doEntry(player, tardis, id);
                                // exit zero room
                                case 17 -> new ZeroRoomAction(plugin).doExit(player, id);
                                // toggle black wool blocks behind door
                                case 20 -> new BlackWoolToggler(plugin).toggleBlocks(id, player);
                                // siege lever
                                case 21 -> new SiegeAction(plugin).clickButton(tcc, player, tardis.isPoweredOn(), id);
                                // open control menu GUI
                                case 22 -> {
                                    event.setCancelled(true);
                                    new ControlMenuAction(plugin).openGUI(player, id);
                                }
                                // shell room button
                                case 25 -> new ShellRoomAction(plugin).openGUI(player, id);
                                // Handles
                                case 26 -> new HandlesAction(plugin).cyberIt(player);
                                case 28 -> {
                                    // Custard Cream Dispenser
                                    event.setCancelled(true);
                                    new CustardCreamAction(plugin, player, block, id).dispense();
                                }
                                // force field
                                case 29 -> new ForceFieldAction(plugin).toggleSheilds(player, blockLocation, tardis.getArtronLevel());
                                // flight mode button
                                case 30 -> new FlightModeAction(plugin).setMode(ownerUUID.toString(), player);
                                // chameleon sign
                                case 31 -> new ChameleonSignAction(plugin).openGUI(player, tardis, id);
                                case 32 -> {
                                    // save_sign
                                    if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && !new SystemUpgradeChecker(plugin).has(playerUUID.toString(), SystemTree.SAVES)) {
                                        plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Saves");
                                        return;
                                    }
                                    if (TARDISFloodgate.isFloodgateEnabled() && TARDISFloodgate.isBedrockPlayer(playerUUID)) {
                                        new FloodgateSavesForm(plugin, playerUUID, id).send();
                                    } else {
                                        new TARDISSaveSign(plugin).openGUI(player, id);
                                    }
                                }
                                // scanner
                                case 33 -> new TARDISScanner(plugin).scan(id, player, tardis.getRenderer(), tardis.getArtronLevel());
                                // cloister bell
                                case 35 -> new CloisterBellAction(plugin).ring(id, tardis);
                                // weather menu
                                case 38 -> new WeatherAction(plugin).openGUI(player);
                                // space/time throttle
                                case 39 -> new ThrottleAction(plugin).setSpaceTime(block, player);
                                // relativity differentiator
                                case 47 -> new DifferentiatorAction(plugin).bleep(block, id, player);
                                case 54 -> {
                                    event.setCancelled(true);
                                    new EyeOfHarmonyAction(plugin).openGUI(id, player);
                                }
                                case 55 -> new TelevisionAction(plugin).openGUI(player);
                                case 58 -> new HappyGhastRelease(plugin).undock(block, id, player);
                                case 59 -> new ArchitecturalReconfiguration(plugin).open(id, player);
                                default -> { }
                            }
                        } else if (action.equals(Action.PHYSICAL)) {
                            switch (type) {
                                // zero room entry
                                case 16 -> new ZeroRoomAction(plugin).doEntry(player, tardis, id);
                                // maze exits
                                case 40, 41, 42, 43 -> new MazeAction(plugin).reconfigure(type, player, id, blockLocation);
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
}
