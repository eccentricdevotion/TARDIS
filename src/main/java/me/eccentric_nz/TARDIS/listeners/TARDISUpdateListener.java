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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import me.eccentric_nz.TARDIS.JSON.JSONArray;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetARS;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The TARDIS interior goes through occasional metamorphoses, sometimes by
 * choice, sometimes for other reasons, such as the Doctor's own regeneration.
 * Some of these changes were physical in nature (involving secondary control
 * rooms, etc.), but it was also possible to re-arrange the interior design of
 * the TARDIS with ease, using the Architectural Configuration system.
 *
 * @author eccentric_nz
 */
public class TARDISUpdateListener implements Listener {

    private final TARDIS plugin;
    List<Material> validBlocks = new ArrayList<Material>();
    List<Material> validSigns = new ArrayList<Material>();
    List<Material> plates = new ArrayList<Material>();
    HashMap<String, Integer> controls = new HashMap<String, Integer>();

    public TARDISUpdateListener(TARDIS plugin) {
        this.plugin = plugin;
        controls.put("handbrake", 0);
        controls.put("button", 1);
        controls.put("world-repeater", 2);
        controls.put("x-repeater", 3);
        controls.put("z-repeater", 4);
        controls.put("y-repeater", 5);
        controls.put("artron", 6);
        controls.put("keyboard", 7);
        controls.put("back", 8);
        controls.put("terminal", 9);
        controls.put("ars", 10);
        controls.put("temporal", 11);
        controls.put("light", 12);
        controls.put("info", 13);
        controls.put("storage", 14);
        controls.put("advanced", 15);
        controls.put("zero", 16); // entry control
        // zero room exit control = 17
        // direction item frame = 18
        // lazarus plate = 19
        controls.put("toggle_wool", 20);
        controls.put("siege", 21);
        controls.put("control", 22);
        validBlocks.add(Material.LEVER);
        validBlocks.add(Material.REDSTONE_COMPARATOR_OFF);
        validBlocks.add(Material.REDSTONE_COMPARATOR_ON);
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.WOOD_BUTTON);
        validSigns.add(Material.REDSTONE_COMPARATOR_OFF);
        validSigns.add(Material.REDSTONE_COMPARATOR_ON);
        validSigns.add(Material.SIGN_POST);
        validSigns.add(Material.WALL_SIGN);
        plates.add(Material.STONE_PLATE);
        plates.add(Material.WOOD_PLATE);
    }

    /**
     * Listens for player interaction with the TARDIS console and other specific
     * items. If the block is clicked and players name is contained in the
     * appropriate HashMap, then the blocks position is recorded in the
     * database.
     *
     * @param event a player clicking on a block
     */
    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onUpdateInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();
        final String playerUUID = uuid.toString();
        String blockName;
        boolean secondary = false;
        if (plugin.getTrackerKeeper().getPlayers().containsKey(uuid)) {
            blockName = plugin.getTrackerKeeper().getPlayers().get(uuid);
        } else if (plugin.getTrackerKeeper().getSecondary().containsKey(uuid)) {
            blockName = plugin.getTrackerKeeper().getSecondary().get(uuid);
            secondary = true;
        } else {
            return;
        }
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Location block_loc = block.getLocation();
            World bw = block_loc.getWorld();
            int bx = block_loc.getBlockX();
            int by = block_loc.getBlockY();
            int bz = block_loc.getBlockZ();
            byte blockData = block.getData();
            if (blockData >= 8 && blockType == Material.IRON_DOOR_BLOCK) {
                by = (by - 1);
                blockData = block.getRelative(BlockFace.DOWN).getData();
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", playerUUID);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NO_TARDIS");
                return;
            }
            int id = rs.getTardis_id();
            String preset = rs.getPreset().toString();
            SCHEMATIC schm = rs.getSchematic();
            QueryFactory qf = new QueryFactory(plugin);
            String table = "tardis";
            HashMap<String, Object> tid = new HashMap<String, Object>();
            HashMap<String, Object> set = new HashMap<String, Object>();
            tid.put("tardis_id", id);
            String blockLocStr = bw.getName() + ":" + bx + ":" + by + ":" + bz;
            if (controls.containsKey(blockName)) {
                if (!blockName.contains("repeater")) {
                    blockLocStr = block_loc.toString();
                }
                table = "controls";
                tid.put("type", controls.get(blockName));
                tid.put("secondary", 0);
            }
            if (secondary) {
                plugin.getTrackerKeeper().getSecondary().remove(uuid);
            } else {
                plugin.getTrackerKeeper().getPlayers().remove(uuid);
            }
            if (blockName.equalsIgnoreCase("door") && blockType == Material.IRON_DOOR_BLOCK && !secondary) {
                // if portals are on, remove the current portal first
                if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
                    ResultSetDoorBlocks rsdb = new ResultSetDoorBlocks(plugin, id);
                    if (rsdb.resultSet()) {
                        plugin.getTrackerKeeper().getPortals().remove(rsdb.getInnerBlock().getLocation());
                    }
                }
                // get door data this should let us determine the direction
                String d = getDirection(blockData);
                table = "doors";
                set.put("door_location", blockLocStr);
                set.put("door_direction", d);
                tid.put("door_type", 1);
            }
            if ((blockName.equalsIgnoreCase("backdoor") || (blockName.equalsIgnoreCase("door") && secondary)) && blockType == Material.IRON_DOOR_BLOCK) {
                // get door data - this should let us determine the direction
                String d = TARDISStaticUtils.getPlayersDirection(player, true);
                table = "doors";
                set.put("door_location", blockLocStr);
                set.put("door_direction", d);
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", id);
                wheret.put("uuid", playerUUID);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                int type;
                if (rst.resultSet()) {
                    type = (secondary) ? 4 : 3;
                    // check the world
                    if (!plugin.getUtils().inTARDISWorld(player)) {
                        TARDISMessage.send(player, "NOT_IN_TARDIS");
                        return;
                    }
                } else {
                    type = 2;
                    if (plugin.getUtils().inTARDISWorld(player)) {
                        TARDISMessage.send(player, "TARDIS_OUTSIDE");
                        return;
                    }
                }
                tid.put("door_type", type);
                // check if we have a backdoor yet
                HashMap<String, Object> whered = new HashMap<String, Object>();
                whered.put("tardis_id", id);
                whered.put("door_type", type);
                ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
                if (!rsd.resultSet()) {
                    // insert record
                    HashMap<String, Object> setd = new HashMap<String, Object>();
                    setd.put("tardis_id", id);
                    setd.put("door_type", type);
                    setd.put("door_location", blockLocStr);
                    setd.put("door_direction", d);
                    qf.doInsert("doors", setd);
                }
            }
            // check they are still in the TARDIS world
            if (!blockName.equals("backdoor") && !plugin.getUtils().inTARDISWorld(player)) {
                TARDISMessage.send(player, "UPDATE_IN_WORLD");
                return;
            }
            if ((blockName.equalsIgnoreCase("button") || blockName.equalsIgnoreCase("artron")) && validBlocks.contains(blockType)) {
                if (secondary) {
                    qf.insertControl(id, controls.get(blockName), blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("scanner") && validBlocks.contains(blockType)) {
                set.put("scanner", blockLocStr);
            }
            if (blockName.equalsIgnoreCase("handbrake") && blockType == Material.LEVER) {
                // check for existing handbrake - there may not be one, as custom schematic may not have CAKE block
                HashMap<String, Object> whereh = new HashMap<String, Object>();
                whereh.put("tardis_id", id);
                whereh.put("type", 0);
                ResultSetControls rsc = new ResultSetControls(plugin, whereh, false);
                if (!rsc.resultSet()) {
                    qf.insertControl(id, 0, blockLocStr, 0);
                }
                if (secondary) {
                    qf.insertControl(id, 0, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("beacon")) {
                set.put("beacon", blockLocStr);
            }
            if (blockName.equalsIgnoreCase("condenser") && blockType == Material.CHEST) {
                set.put("condenser", blockLocStr);
            }
            if (blockName.equalsIgnoreCase("eps")) {
                blockLocStr = bw.getName() + ":" + bx + ":" + (by + 1) + ":" + (bz - 1);
                set.put("eps", blockLocStr);
            }
            if (blockName.equalsIgnoreCase("farm") || blockName.equalsIgnoreCase("stable") || blockName.equalsIgnoreCase("village")) {
                blockLocStr = bw.getName() + ":" + bx + ":" + by + ":" + bz;
                set.put(blockName.toLowerCase(Locale.ENGLISH), blockLocStr);
            }
            if (blockName.equalsIgnoreCase("creeper")) {
                blockLocStr = bw.getName() + ":" + bx + ".5:" + by + ":" + bz + ".5";
                set.put("creeper", blockLocStr);
            }
            if (blockName.equalsIgnoreCase("rail") && blockType == Material.FENCE) {
                blockLocStr = bw.getName() + ":" + bx + ":" + by + ":" + bz;
                set.put("rail", blockLocStr);
            }
            if (blockName.equalsIgnoreCase("world-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 2);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (secondary || !rsc.resultSet()) {
                    qf.insertControl(id, 2, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("x-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 3);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (secondary || !rsc.resultSet()) {
                    qf.insertControl(id, 3, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("z-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 4);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (secondary || !rsc.resultSet()) {
                    qf.insertControl(id, 4, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("y-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 5);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (secondary || !rsc.resultSet()) {
                    qf.insertControl(id, 5, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("chameleon") && validSigns.contains(blockType)) {
                set.put("chameleon", blockLocStr);
                set.put("chamele_on", 0);
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, plugin.getSigns().getStringList("chameleon").get(0));
                    s.setLine(1, plugin.getSigns().getStringList("chameleon").get(1));
                    s.setLine(2, ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
                    s.setLine(3, preset);
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("save-sign") && validSigns.contains(blockType)) {
                set.put("save_sign", blockLocStr);
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "TARDIS");
                    s.setLine(1, plugin.getSigns().getStringList("saves").get(0));
                    s.setLine(2, plugin.getSigns().getStringList("saves").get(1));
                    s.setLine(3, "");
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("keyboard") && (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 7);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (!rsc.resultSet()) {
                    qf.insertControl(id, 7, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                // add text to sign
                Sign s = (Sign) block.getState();
                s.setLine(0, plugin.getSigns().getStringList("keyboard").get(0));
                for (int i = 1; i < 4; i++) {
                    s.setLine(i, "");
                }
                s.update();
            }
            if (blockName.equalsIgnoreCase("back") && validBlocks.contains(blockType)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 8);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (!rsc.resultSet()) {
                    // insert current into back
                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                    wherecl.put("tardis_id", id);
                    ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
                    if (rscl.resultSet()) {
                        HashMap<String, Object> setb = new HashMap<String, Object>();
                        setb.put("world", rscl.getWorld().getName());
                        setb.put("x", rscl.getX());
                        setb.put("y", rscl.getY());
                        setb.put("z", rscl.getZ());
                        setb.put("direction", rscl.getDirection().toString());
                        setb.put("submarine", (rscl.isSubmarine()) ? 1 : 0);
                        HashMap<String, Object> whereb = new HashMap<String, Object>();
                        whereb.put("tardis_id", id);
                        qf.doUpdate("back", setb, whereb);
                    }
                    // insert control
                    qf.insertControl(id, 8, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("terminal") && validSigns.contains(blockType)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 9);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 9, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "");
                    s.setLine(1, plugin.getSigns().getStringList("terminal").get(0));
                    s.setLine(2, plugin.getSigns().getStringList("terminal").get(1));
                    s.setLine(3, "");
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("control") && validSigns.contains(blockType)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 22);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 22, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "");
                    s.setLine(1, plugin.getSigns().getStringList("control").get(0));
                    s.setLine(2, plugin.getSigns().getStringList("control").get(1));
                    s.setLine(3, "");
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("ars") && validSigns.contains(blockType)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 10);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 10, blockLocStr, 0);
                    // check if they already have an ARS record (they may have used `/tardis arsremove`)
                    HashMap<String, Object> wherer = new HashMap<String, Object>();
                    wherer.put("tardis_id", id);
                    ResultSetARS rsa = new ResultSetARS(plugin, wherer);
                    if (!rsa.resultSet()) {
                        // create default json
                        int[][][] empty = new int[3][9][9];
                        for (int y = 0; y < 3; y++) {
                            for (int x = 0; x < 9; x++) {
                                for (int z = 0; z < 9; z++) {
                                    empty[y][x][z] = 1;
                                }
                            }
                        }
                        int control = schm.getSeedId();
                        if (schm.getPermission().equals("deluxe") || schm.getPermission().equals("eleventh") || schm.getPermission().equals("master")) {
                            empty[0][4][4] = control;
                            empty[0][4][5] = control;
                            empty[0][5][4] = control;
                            empty[0][5][5] = control;
                            empty[1][4][5] = control;
                            empty[1][5][4] = control;
                            empty[1][5][5] = control;
                        } else if (schm.getPermission().equals("bigger") || schm.getPermission().equals("redstone") || schm.getPermission().equals("twelfth")) {
                            empty[1][4][5] = control;
                            empty[1][5][4] = control;
                            empty[1][5][5] = control;
                        }
                        empty[1][4][4] = control;
                        JSONArray json = new JSONArray(empty);
                        HashMap<String, Object> seta = new HashMap<String, Object>();
                        seta.put("tardis_id", id);
                        seta.put("uuid", playerUUID);
                        seta.put("json", json.toString());
                        qf.doInsert("ars", seta);
                    }
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "TARDIS");
                    s.setLine(1, plugin.getSigns().getStringList("ars").get(0));
                    s.setLine(2, plugin.getSigns().getStringList("ars").get(1));
                    s.setLine(3, plugin.getSigns().getStringList("ars").get(2));
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("temporal") && validSigns.contains(blockType)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 11);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 11, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "");
                    s.setLine(1, plugin.getSigns().getStringList("temporal").get(0));
                    s.setLine(2, plugin.getSigns().getStringList("temporal").get(1));
                    s.setLine(3, "");
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("info") && validSigns.contains(blockType)) {
                HashMap<String, Object> wherec = new HashMap<String, Object>();
                wherec.put("tardis_id", id);
                wherec.put("type", 13);
                ResultSetControls rsc = new ResultSetControls(plugin, wherec, false);
                if (!rsc.resultSet()) {
                    qf.insertControl(id, 13, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "-----");
                    s.setLine(1, "TARDIS");
                    s.setLine(2, plugin.getSigns().getStringList("info").get(0));
                    s.setLine(3, plugin.getSigns().getStringList("info").get(1));
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("storage") && blockType.equals(Material.NOTE_BLOCK)) {
                HashMap<String, Object> wherel = new HashMap<String, Object>();
                wherel.put("tardis_id", id);
                wherel.put("type", 14);
                ResultSetControls rsc = new ResultSetControls(plugin, wherel, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 14, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                // check if player has storage record, and update the tardis_id field
                plugin.getUtils().updateStorageId(playerUUID, id, qf);
            }
            if (blockName.equalsIgnoreCase("advanced") && blockType.equals(Material.JUKEBOX)) {
                HashMap<String, Object> wherel = new HashMap<String, Object>();
                wherel.put("tardis_id", id);
                wherel.put("type", 15);
                ResultSetControls rsc = new ResultSetControls(plugin, wherel, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 15, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                // check if player has storage record, and update the tardis_id field
                plugin.getUtils().updateStorageId(playerUUID, id, qf);
            }
            if (blockName.equalsIgnoreCase("zero") && (validBlocks.contains(blockType) || validSigns.contains(blockType) || plates.contains(blockType))) {
                HashMap<String, Object> wherez = new HashMap<String, Object>();
                wherez.put("tardis_id", id);
                wherez.put("type", 16);
                ResultSetControls rsc = new ResultSetControls(plugin, wherez, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 16, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                // check if player has storage record, and update the tardis_id field
                plugin.getUtils().updateStorageId(playerUUID, id, qf);
            }
            if ((blockName.equalsIgnoreCase("light") || blockName.equalsIgnoreCase("siege") || blockName.equalsIgnoreCase("toggle_wool")) && validBlocks.contains(blockType)) {
                HashMap<String, Object> wherel = new HashMap<String, Object>();
                wherel.put("tardis_id", id);
                wherel.put("type", controls.get(blockName));
                ResultSetControls rsc = new ResultSetControls(plugin, wherel, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, controls.get(blockName), blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (set.size() > 0 || secondary) {
                if (!secondary) {
                    qf.doUpdate(table, set, tid);
                }
                TARDISMessage.send(player, "UPDATE_SET", blockName);
            } else {
                TARDISMessage.send(player, "UPDATE_BAD_CLICK", blockName);
            }
        }
    }

    private String getDirection(Byte blockData) {
        switch (blockData) {
            case 1:
                return "SOUTH";
            case 2:
                return "WEST";
            case 3:
                return "NORTH";
            default:
                return "EAST";
        }
    }
}
