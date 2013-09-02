/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetControls;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.json.JSONArray;

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

    private TARDIS plugin;
    List<Material> validBlocks = new ArrayList<Material>();
    List<Material> validSigns = new ArrayList<Material>();
    HashMap<String, Integer> controls = new HashMap<String, Integer>();
    List<String> mustbeinworld = new ArrayList<String>();

    public TARDISUpdateListener(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.bukkitversion.compareTo(plugin.prewoodbuttonversion) >= 0) {
            validBlocks.add(Material.WOOD_BUTTON);
        }
        if (plugin.bukkitversion.compareTo(plugin.precomparatorversion) >= 0) {
            validBlocks.add(Material.REDSTONE_COMPARATOR_OFF);
            validBlocks.add(Material.REDSTONE_COMPARATOR_ON);
            validSigns.add(Material.REDSTONE_COMPARATOR_OFF);
            validSigns.add(Material.REDSTONE_COMPARATOR_ON);
        }
        validBlocks.add(Material.STONE_BUTTON);
        validBlocks.add(Material.LEVER);
        validSigns.add(Material.WALL_SIGN);
        validSigns.add(Material.SIGN_POST);
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
        mustbeinworld.add("farm");
        mustbeinworld.add("stable");
        mustbeinworld.add("village");
        mustbeinworld.add("rail");
        mustbeinworld.add("creeper");
        mustbeinworld.add("ars");
    }

    /**
     * Listens for player interaction with the TARDIS console and other specific
     * items. If the block is clicked and players name is contained in the
     * appropriate HashMap, then the blocks position is recorded in the
     * database.
     *
     * @param event a player clicking on a block
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onUpdateInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();
        String blockName;
        boolean secondary = false;
        if (plugin.trackPlayers.containsKey(playerNameStr)) {
            blockName = plugin.trackPlayers.get(playerNameStr);
        } else if (plugin.trackSecondary.containsKey(playerNameStr)) {
            blockName = plugin.trackSecondary.get(playerNameStr);
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
            where.put("owner", playerNameStr);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                player.sendMessage(plugin.pluginName + TARDISConstants.NO_TARDIS);
                return;
            }
            int id = rs.getTardis_id();
            TARDISConstants.SCHEMATIC schm = rs.getSchematic();
            QueryFactory qf = new QueryFactory(plugin);
            String table = "tardis";
            HashMap<String, Object> tid = new HashMap<String, Object>();
            HashMap<String, Object> set = new HashMap<String, Object>();
            tid.put("tardis_id", id);
            String blockLocStr = bw.getName() + ":" + bx + ":" + by + ":" + bz;
            if (controls.containsKey(blockName)) {
                if (!blockName.contains("repeater")) {
                    //blockLocStr = plugin.utils.makeLocationStr(bw, bx, by, bz);
                    blockLocStr = block_loc.toString();
                }
                table = "controls";
                tid.put("type", controls.get(blockName));
                tid.put("secondary", 0);
            }
            if (secondary) {
                plugin.trackSecondary.remove(playerNameStr);
            } else {
                plugin.trackPlayers.remove(playerNameStr);
            }
            if (blockName.equalsIgnoreCase("door") && blockType == Material.IRON_DOOR_BLOCK && !secondary) {
                // get door data this should let us determine the direction
                String d = getDirection(blockData);
                table = "doors";
                set.put("door_location", blockLocStr);
                set.put("door_direction", d);
                tid.put("door_type", 1);
            }
            if ((blockName.equalsIgnoreCase("backdoor") || (blockName.equalsIgnoreCase("door") && secondary)) && blockType == Material.IRON_DOOR_BLOCK) {
                // get door data - this should let us determine the direction
                String d = plugin.utils.getPlayersDirection(player, true);
                table = "doors";
                set.put("door_location", blockLocStr);
                set.put("door_direction", d);
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", id);
                wheret.put("player", playerNameStr);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                int type;
                if (rst.resultSet()) {
                    type = (secondary) ? 4 : 3;
                } else {
                    type = 2;
                    // check the world
                    String wor = (plugin.getConfig().getBoolean("default_world")) ? plugin.getConfig().getString("default_world_name") : "TARDIS_";
                    if (bw.getName().contains(wor)) {
                        player.sendMessage(plugin.pluginName + "You didn't enter the TARDIS by the regular door, aborting...");
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
            if (mustbeinworld.contains(blockName) && !inTARDISWorld(player)) {
                player.sendMessage(plugin.pluginName + "You must be in a TARDIS world to update this block!");
                return;
            }
            if (blockName.equalsIgnoreCase("button") && validBlocks.contains(blockType)) {
                if (secondary) {
                    qf.insertControl(id, 1, blockLocStr, 1);
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
                if (secondary) {
                    qf.insertControl(id, 5, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("x-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                if (secondary) {
                    qf.insertControl(id, 2, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("z-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                if (secondary) {
                    qf.insertControl(id, 3, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("y-repeater") && (blockType == Material.DIODE_BLOCK_OFF || blockType == Material.DIODE_BLOCK_ON)) {
                if (secondary) {
                    qf.insertControl(id, 4, blockLocStr, 1);
                } else {
                    set.put("location", blockLocStr);
                }
            }
            if (blockName.equalsIgnoreCase("artron") && validBlocks.contains(blockType)) {
                if (secondary) {
                    qf.insertControl(id, 6, blockLocStr, 1);
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
                    s.setLine(0, "Chameleon");
                    s.setLine(1, "Circuit");
                    s.setLine(3, ChatColor.RED + "OFF");
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("save-sign") && validSigns.contains(blockType)) {
                set.put("save_sign", blockLocStr);
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "TARDIS");
                    s.setLine(1, "Saved");
                    s.setLine(2, "Locations");
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
                s.setLine(0, "Keyboard");
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
                    s.setLine(1, "Destination");
                    s.setLine(2, "Terminal");
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
                    // create default json
                    int[][][] empty = new int[3][9][9];
                    for (int y = 0; y < 3; y++) {
                        for (int x = 0; x < 9; x++) {
                            for (int z = 0; z < 9; z++) {
                                empty[y][x][z] = 1;
                            }
                        }
                    }
                    int control = 42;
                    switch (schm) {
//                        case BIGGER:
//                            control = 41;
//                            break;
//                        case DELUXE:
//                            control = 57;
//                            break;
//                        case ELEVENTH:
//                            control = 133;
//                            break;
//                        case REDSTONE:
//                            control = 152;
//                            break;
                        case STEAMPUNK:
                            control = 173;
                            break;
                        case ARS:
                            control = 159;
                            break;
                        case PLANK:
                            control = 22;
                            break;
                        case TOM:
                            control = 155;
                            break;
                        default:
                            break;
                    }
                    empty[1][4][4] = control;
                    JSONArray json = new JSONArray(empty);

                    HashMap<String, Object> seta = new HashMap<String, Object>();
                    seta.put("tardis_id", id);
                    seta.put("player", playerNameStr);
                    seta.put("json", json.toString());
                    qf.doInsert("ars", seta);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
                }
                if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
                    // add text to sign
                    Sign s = (Sign) block.getState();
                    s.setLine(0, "TARDIS");
                    s.setLine(1, "Architectural");
                    s.setLine(2, "Reconfiguration");
                    s.setLine(3, "System");
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
                    s.setLine(1, "Temporal");
                    s.setLine(2, "Locator");
                    s.setLine(3, "");
                    s.update();
                }
            }
            if (blockName.equalsIgnoreCase("light") && validBlocks.contains(blockType)) {
                HashMap<String, Object> wherel = new HashMap<String, Object>();
                wherel.put("tardis_id", id);
                wherel.put("type", 12);
                ResultSetControls rsc = new ResultSetControls(plugin, wherel, false);
                if (!rsc.resultSet()) {
                    // insert control
                    qf.insertControl(id, 12, blockLocStr, 0);
                    secondary = true;
                } else {
                    set.put("location", blockLocStr);
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
                    s.setLine(2, "Information");
                    s.setLine(3, "System");
                    s.update();
                }
            }
            if (set.size() > 0 || secondary) {
                if (!secondary) {
                    qf.doUpdate(table, set, tid);
                }
                player.sendMessage(plugin.pluginName + "The position of the TARDIS " + blockName + " was updated successfully.");
            } else {
                player.sendMessage(plugin.pluginName + "You didn't click the correct type of block for the " + blockName + "! Try the command again.");
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

    private boolean inTARDISWorld(Player player) {
        // check they are still in the TARDIS world
        World world = player.getLocation().getWorld();
        String name = world.getName();
        String gen = world.getGenerator().getClass().getSimpleName();
        boolean special = name.contains("TARDIS_TimeVortex") && (gen.equals("TARDISChunkGenerator") || world.getWorldType().equals(WorldType.FLAT));
        if (!name.equals("TARDIS_WORLD_" + player.getName()) && !special) {
            return false;
        } else {
            return true;
        }
    }
}
