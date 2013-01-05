/*
 * Copyright (C) 2012 eccentric_nz
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

import me.eccentric_nz.TARDIS.database.TARDISDatabase;
import java.util.HashMap;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.artron.TARDISArtronLevels;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.utility.TARDISItemRenamer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

/**
 * @author eccentric_nz
 */
public class TARDISDoorListener implements Listener {

    private TARDIS plugin;
    float[][] adjustYaw = new float[4][4];
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISDoorListener(TARDIS plugin) {
        this.plugin = plugin;
        // yaw adjustments if inner and outer door directions are different
        adjustYaw[0][0] = 0;
        adjustYaw[0][1] = -90;
        adjustYaw[0][2] = 180;
        adjustYaw[0][3] = 90;
        adjustYaw[1][0] = 90;
        adjustYaw[1][1] = 0;
        adjustYaw[1][2] = -90;
        adjustYaw[1][3] = 180;
        adjustYaw[2][0] = 180;
        adjustYaw[2][1] = 90;
        adjustYaw[2][2] = 0;
        adjustYaw[2][3] = -90;
        adjustYaw[3][0] = -90;
        adjustYaw[3][1] = 180;
        adjustYaw[3][2] = 90;
        adjustYaw[3][3] = 0;
    }

    /**
     * Listens for player interaction with TARDIS doors. If the door is
     * right-clicked with the TARDIS key (configurable) it will teleport the
     * player either into or out of the TARDIS.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDoorInteract(PlayerInteractEvent event) {
        if (event.isCancelled()) {
            return;
        }
        QueryFactory qf = new QueryFactory(plugin);
        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();
        int cx = 0, cy = 0, cz = 0;
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                World playerWorld = player.getLocation().getWorld();
                ItemStack stack = player.getItemInHand();
                Material material = stack.getType();
                // get key material from config
                Material key = Material.getMaterial(plugin.getConfig().getString("key"));
                // only proceed if they are clicking an iron door with a TARDIS key!
                if (blockType == Material.IRON_DOOR_BLOCK) {
                    if (material == key) {
                        if (block != null) {
                            if (player.hasPermission("tardis.enter")) {
                                Location block_loc = block.getLocation();
                                String bw = block_loc.getWorld().getName();
                                int bx = block_loc.getBlockX();
                                int by = block_loc.getBlockY();
                                int bz = block_loc.getBlockZ();
                                byte doorData = block.getData();
                                if (doorData >= 8) {
                                    by = (by - 1);
                                }
                                String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                                HashMap<String, Object> where = new HashMap<String, Object>();
                                where.put("door_location", doorloc);
                                ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                                if ((rsd.resultSet())) {
                                    int id = rsd.getTardis_id();
                                    int doortype = rsd.getDoor_type();
                                    TARDISConstants.COMPASS dd = rsd.getDoor_direction();
                                    HashMap<String, Object> tid = new HashMap<String, Object>();
                                    tid.put("tardis_id", id);
                                    ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false);
                                    if (rs.resultSet()) {
                                        TARDISConstants.COMPASS d = rs.getDirection();
                                        String tl = rs.getOwner();
                                        String save = rs.getSave();
                                        String cl = rs.getCurrent();
                                        boolean cham = rs.getChamele_on();
                                        float yaw = player.getLocation().getYaw();
                                        float pitch = player.getLocation().getPitch();
                                        String companions = rs.getCompanions();
                                        // get quotes player prefs
                                        HashMap<String, Object> whereq = new HashMap<String, Object>();
                                        whereq.put("player", playerNameStr);
                                        ResultSetPlayerPrefs rsq = new ResultSetPlayerPrefs(plugin, whereq);
                                        boolean userQuotes;
                                        if (rsq.resultSet()) {
                                            userQuotes = rsq.getQuotes_on();
                                        } else {
                                            userQuotes = true;
                                        }
                                        if (doortype == 1) {
                                            // player is in the TARDIS
                                            // change the yaw if the door directions are different
                                            if (!dd.equals(d)) {
                                                switch (dd) {
                                                    case NORTH:
                                                        yaw = yaw + adjustYaw[0][d.ordinal()];
                                                        break;
                                                    case WEST:
                                                        yaw = yaw + adjustYaw[1][d.ordinal()];
                                                        break;
                                                    case SOUTH:
                                                        yaw = yaw + adjustYaw[2][d.ordinal()];
                                                        break;
                                                    case EAST:
                                                        yaw = yaw + adjustYaw[3][d.ordinal()];
                                                        break;
                                                }
                                            }
                                            // get location from database
                                            final Location exitTardis = plugin.utils.getLocationFromDB(save, yaw, pitch);
                                            // make location safe ie. outside of the bluebox
                                            double ex = exitTardis.getX();
                                            double ez = exitTardis.getZ();
                                            double ey = exitTardis.getY();
                                            switch (d) {
                                                case NORTH:
                                                    exitTardis.setX(ex + 0.5);
                                                    exitTardis.setZ(ez + 2.5);
                                                    break;
                                                case EAST:
                                                    exitTardis.setX(ex - 1.5);
                                                    exitTardis.setZ(ez + 0.5);
                                                    break;
                                                case SOUTH:
                                                    exitTardis.setX(ex + 0.5);
                                                    exitTardis.setZ(ez - 1.5);
                                                    break;
                                                case WEST:
                                                    exitTardis.setX(ex + 2.5);
                                                    exitTardis.setZ(ez + 0.5);
                                                    break;
                                            }
                                            World exitWorld = exitTardis.getWorld();
                                            // destroy current TARDIS location
                                            Location newl = null;
                                            // need some sort of check here to sort out who has exited
                                            // count number of travellers - if 1 less than number counted at start
                                            // then build and remember else just exit?
                                            int count = 1;
                                            HashMap<String, Object> whert = new HashMap<String, Object>();
                                            whert.put("tardis_id", id);
                                            ResultSetTravellers rst = new ResultSetTravellers(plugin, whert, true);
                                            if (rst.resultSet()) {
                                                count = rst.getData().size();
                                            }
                                            if (!save.equals(cl) && (count == plugin.trackTravellers.get(id))) {
                                                Location l = plugin.utils.getLocationFromDB(cl, 0, 0);
                                                newl = plugin.utils.getLocationFromDB(save, 0, 0);
                                                // remove torch
                                                plugin.destroyPB.destroyTorch(l);
                                                // remove sign
                                                plugin.destroyPB.destroySign(l, d);
                                                // remove blue box
                                                plugin.destroyPB.destroyPoliceBox(l, d, id, false);
                                            }
                                            // try preloading destination chunk
                                            while (!exitWorld.getChunkAt(exitTardis).isLoaded()) {
                                                exitWorld.getChunkAt(exitTardis).load();
                                            }
                                            // rebuild blue box
                                            if (newl != null && (count == plugin.trackTravellers.get(id))) {
                                                plugin.buildPB.buildPoliceBox(id, newl, d, cham, player, false);
                                            }
                                            // exit TARDIS!
                                            movePlayer(player, exitTardis, true, playerWorld, userQuotes, id);
                                            // remove player from traveller table
                                            HashMap<String, Object> wherd = new HashMap<String, Object>();
                                            wherd.put("player", playerNameStr);
                                            qf.doDelete("travellers", wherd);
                                        } else {
                                            boolean chkCompanion = false;
                                            if (!playerNameStr.equals(tl)) {
                                                if (plugin.getServer().getPlayer(tl) != null) {
                                                    if (companions != null && !companions.equals("")) {
                                                        // is the timelord in the TARDIS?
                                                        HashMap<String, Object> whert = new HashMap<String, Object>();
                                                        whert.put("tardis_id", id);
                                                        ResultSetTravellers rst = new ResultSetTravellers(plugin, whert, true);
                                                        if (rst.resultSet()) {
                                                            // is the player in the comapnion list
                                                            String[] companionData = companions.split(":");
                                                            for (String c : companionData) {
                                                                //String lc_name = c.toLowerCase();
                                                                if (c.equalsIgnoreCase(playerNameStr)) {
                                                                    chkCompanion = true;
                                                                    break;
                                                                }
                                                            }
                                                        } else {
                                                            player.sendMessage(plugin.pluginName + TARDISConstants.TIMELORD_NOT_IN);
                                                        }
                                                    }
                                                } else {
                                                    player.sendMessage(plugin.pluginName + TARDISConstants.TIMELORD_OFFLINE);
                                                }
                                            }
                                            if (playerNameStr.equals(tl) || chkCompanion == true || player.hasPermission("tardis.skeletonkey")) {
                                                // get INNER TARDIS location
                                                HashMap<String, Object> wherei = new HashMap<String, Object>();
                                                wherei.put("door_type", 1);
                                                wherei.put("tardis_id", id);
                                                ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
                                                if (rsi.resultSet()) {
                                                    TARDISConstants.COMPASS innerD = rsi.getDoor_direction();
                                                    String doorLocStr = rsi.getDoor_location();
                                                    String[] split = doorLocStr.split(":");
                                                    World cw = plugin.getServer().getWorld(split[0]);
                                                    try {
                                                        cx = Integer.parseInt(split[1]);
                                                        cy = Integer.parseInt(split[2]);
                                                        cz = Integer.parseInt(split[3]);
                                                    } catch (NumberFormatException nfe) {
                                                        plugin.console.sendMessage(plugin.pluginName + "Could not convert to number!");
                                                    }
                                                    Location tmp_loc = cw.getBlockAt(cx, cy, cz).getLocation();
                                                    int getx = tmp_loc.getBlockX();
                                                    int getz = tmp_loc.getBlockZ();
                                                    switch (innerD) {
                                                        case NORTH:
                                                            // z -ve
                                                            tmp_loc.setX(getx + 0.5);
                                                            tmp_loc.setZ(getz - 0.5);
                                                            break;
                                                        case EAST:
                                                            // x +ve
                                                            tmp_loc.setX(getx + 1.5);
                                                            tmp_loc.setZ(getz + 0.5);
                                                            break;
                                                        case SOUTH:
                                                            // z +ve
                                                            tmp_loc.setX(getx + 0.5);
                                                            tmp_loc.setZ(getz + 1.5);
                                                            break;
                                                        case WEST:
                                                            // x -ve
                                                            tmp_loc.setX(getx - 0.5);
                                                            tmp_loc.setZ(getz + 0.5);
                                                            break;
                                                    }
                                                    // enter TARDIS!
                                                    cw.getChunkAt(tmp_loc).load();
                                                    tmp_loc.setPitch(pitch);
                                                    // get inner door direction so we can adjust yaw if necessary
                                                    if (!innerD.equals(d)) {
                                                        switch (d) {
                                                            case NORTH:
                                                                yaw = yaw + adjustYaw[0][innerD.ordinal()];
                                                                break;
                                                            case WEST:
                                                                yaw = yaw + adjustYaw[1][innerD.ordinal()];
                                                                break;
                                                            case SOUTH:
                                                                yaw = yaw + adjustYaw[2][innerD.ordinal()];
                                                                break;
                                                            case EAST:
                                                                yaw = yaw + adjustYaw[3][innerD.ordinal()];
                                                                break;
                                                        }
                                                    }
                                                    tmp_loc.setYaw(yaw);
                                                    final Location tardis_loc = tmp_loc;
                                                    movePlayer(player, tardis_loc, false, playerWorld, userQuotes, id);
                                                    // put player into travellers table
                                                    HashMap<String, Object> set = new HashMap<String, Object>();
                                                    set.put("tardis_id", id);
                                                    set.put("player", playerNameStr);
                                                    qf.doInsert("travellers", set);
                                                    // update current TARDIS location
                                                    HashMap<String, Object> setc = new HashMap<String, Object>();
                                                    setc.put("current", save);
                                                    HashMap<String, Object> wherec = new HashMap<String, Object>();
                                                    wherec.put("tardis_id", id);
                                                    qf.doUpdate("tardis", setc, wherec);
                                                    if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                                        SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_hum.mp3", false, tardis_loc, 9, 25);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                            }
                        } else {
                            plugin.console.sendMessage(plugin.pluginName + "Could not get block");
                        }
                    } else {
                        Block blockAbove = block.getRelative(BlockFace.UP);
                        Material baType = blockAbove.getType();
                        byte baData = blockAbove.getData();
                        if (baType == Material.WOOL && (baData == 1 || baData == 11)) {
                            player.sendMessage(plugin.pluginName + TARDISConstants.WRONG_MATERIAL + plugin.TARDIS_KEY + ". You have a " + material + " in your hand!");
                        }
                    }
                }
            }
        }
    }
    Random r = new Random();

    private void movePlayer(final Player p, Location l, final boolean exit, final World from, boolean q, final int id) {

        final int i = r.nextInt(plugin.quotelen);
        final Location theLocation = l;
        final World to = theLocation.getWorld();
        final boolean allowFlight = p.getAllowFlight();
        final boolean crossWorlds = from != to;
        final boolean quotes = q;

        // try loading chunk
        World world = l.getWorld();
        Chunk chunk = world.getChunkAt(l);
        if (!world.isChunkLoaded(chunk)) {
            world.loadChunk(chunk);
        }
        world.refreshChunk(chunk.getX(), chunk.getZ());

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.teleport(theLocation);
            }
        }, 5L);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            @SuppressWarnings("deprecation")
            public void run() {
                p.teleport(theLocation);
                if (p.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds)) {
                    p.setAllowFlight(true);
                }
                if (quotes) {
                    p.sendMessage(plugin.pluginName + plugin.quote.get(i));
                }
                if (exit == true) {
                    // check if at a recharge point
                    TARDISArtronLevels tal = new TARDISArtronLevels(plugin);
                    tal.recharge(id, p);
                    // give some artron energy
                    String name = p.getName();
                    if (plugin.tardisHasTravelled.containsKey(name)) {
                        QueryFactory qf = new QueryFactory(plugin);
                        // add energy to player
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("player", name);
                        qf.alterEnergyLevel("player_prefs", 2, where, p);
                        // remove energy from TARDIS
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("tardis_id", id);
                        int amount = plugin.tardisHasTravelled.get(name) * -1;
                        qf.alterEnergyLevel("tardis", amount, wheret, p);
                        plugin.tardisHasTravelled.remove(name);
                    }
                    // give a key
                    Inventory inv = p.getInventory();
                    Material m = Material.valueOf(plugin.TARDIS_KEY);
                    if (!inv.contains(m) && plugin.getConfig().getBoolean("give_key") == true) {
                        ItemStack is = new ItemStack(m, 1);
                        TARDISItemRenamer ir = new TARDISItemRenamer(is);
                        ir.setName("Sonic Screwdriver", true);
                        inv.addItem(is);
                        p.updateInventory();
                        p.sendMessage(plugin.pluginName + "Don't forget your TARDIS key!");
                    }
                }
            }
        }, 10L);
    }
}