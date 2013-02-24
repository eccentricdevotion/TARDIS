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

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import java.util.HashMap;
import java.util.Random;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.thirdparty.Version;
import me.eccentric_nz.TARDIS.utility.TARDISItemRenamer;
import multiworld.MultiWorldPlugin;
import multiworld.api.MultiWorldAPI;
import multiworld.api.MultiWorldWorldData;
import multiworld.api.flag.FlagName;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.material.Door;
import org.getspout.spoutapi.SpoutManager;

/**
 * During TARDIS operation, a distinctive grinding and whirring sound is usually
 * heard. River Song once demonstrated a TARDIS was capable of materialising
 * silently, teasing the Doctor that the noise was actually caused by him
 * leaving the brakes on.
 *
 * @author eccentric_nz
 */
public class TARDISDoorListener implements Listener {

    private TARDIS plugin;
    public float[][] adjustYaw = new float[4][4];
    Version bukkitversion;
    Version preIMversion = new Version("1.4.5");
    Version SUBversion;
    Version preSUBversion = new Version("1.0");

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

        String[] v = Bukkit.getServer().getBukkitVersion().split("-");
        bukkitversion = new Version(v[0]);
        SUBversion = new Version(v[1].substring(1, v[1].length()));
    }

    /**
     * Listens for player interaction with TARDIS doors. If the door is
     * right-clicked with the TARDIS key (configurable) it will teleport the
     * player either into or out of the TARDIS.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDoorInteract(PlayerInteractEvent event) {
        QueryFactory qf = new QueryFactory(plugin);
        final Player player = event.getPlayer();
        final String playerNameStr = player.getName();
        int cx = 0, cy = 0, cz = 0;
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            // only proceed if they are clicking an iron door with a TARDIS key!
            if (blockType == Material.IRON_DOOR_BLOCK) {
                if (player.hasPermission("tardis.enter")) {
                    World playerWorld = player.getLocation().getWorld();
                    Location block_loc = block.getLocation();
                    byte doorData = block.getData();
                    String bw = block_loc.getWorld().getName();
                    int bx = block_loc.getBlockX();
                    int by = block_loc.getBlockY();
                    int bz = block_loc.getBlockZ();
                    if (doorData >= 8) {
                        by = (by - 1);
                    }
                    String doorloc = bw + ":" + bx + ":" + by + ":" + bz;
                    ItemStack stack = player.getItemInHand();
                    Material material = stack.getType();
                    // get key material
                    HashMap<String, Object> wherepp = new HashMap<String, Object>();
                    wherepp.put("player", playerNameStr);
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                    String key;
                    boolean hasPrefs = false;
                    if (rsp.resultSet()) {
                        hasPrefs = true;
                        key = (!rsp.getKey().equals("")) ? rsp.getKey() : plugin.getConfig().getString("key");
                    } else {
                        key = plugin.getConfig().getString("key");
                    }
                    Material m = Material.getMaterial(key);
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("door_location", doorloc);
                    ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                    if ((rsd.resultSet())) {
                        if (material == m) {
                            TARDISConstants.COMPASS dd = rsd.getDoor_direction();
                            if (action == Action.RIGHT_CLICK_BLOCK && player.isSneaking()) {
                                // toogle the door open/closed
                                Block door_bottom;
                                Door door = (Door) block.getState().getData();
                                door_bottom = (door.isTopHalf()) ? block.getRelative(BlockFace.DOWN) : block;
                                byte door_data = door_bottom.getData();
                                switch (dd) {
                                    case NORTH:
                                        if (door_data == 3) {
                                            door_bottom.setData((byte) 7, false);
                                        } else {
                                            door_bottom.setData((byte) 3, false);
                                        }
                                        break;
                                    case WEST:
                                        if (door_data == 2) {
                                            door_bottom.setData((byte) 6, false);
                                        } else {
                                            door_bottom.setData((byte) 2, false);
                                        }
                                        break;
                                    case SOUTH:
                                        if (door_data == 1) {
                                            door_bottom.setData((byte) 5, false);
                                        } else {
                                            door_bottom.setData((byte) 1, false);
                                        }
                                        break;
                                    default:
                                        if (door_data == 0) {
                                            door_bottom.setData((byte) 4, false);
                                        } else {
                                            door_bottom.setData((byte) 0, false);
                                        }
                                        break;
                                }
                                playerWorld.playEffect(block_loc, Effect.DOOR_TOGGLE, 0);
                            } else if (action == Action.RIGHT_CLICK_BLOCK) {
                                int id = rsd.getTardis_id();
                                int doortype = rsd.getDoor_type();
                                HashMap<String, Object> tid = new HashMap<String, Object>();
                                tid.put("tardis_id", id);
                                ResultSetTardis rs = new ResultSetTardis(plugin, tid, "", false);
                                if (rs.resultSet()) {
                                    TARDISConstants.COMPASS d = rs.getDirection();
                                    String tl = rs.getOwner();
                                    String save = rs.getSave();
                                    String current = rs.getCurrent();
                                    float yaw = player.getLocation().getYaw();
                                    float pitch = player.getLocation().getPitch();
                                    String companions = rs.getCompanions();
                                    // get quotes player prefs
                                    boolean userQuotes;
                                    if (hasPrefs) {
                                        userQuotes = rsp.isQuotes_on();
                                    } else {
                                        userQuotes = true;
                                    }
                                    if (doortype == 1) {
                                        Location exitLoc = plugin.utils.getLocationFromDB(save, yaw, pitch);
                                        boolean hasDest = plugin.tardisHasDestination.containsKey(Integer.valueOf(id));
                                        boolean hasTrav = plugin.tardisHasTravelled.contains(Integer.valueOf(id));
                                        if (hasDest && !hasTrav) {
                                            exitLoc = plugin.utils.getLocationFromDB(current, yaw, pitch);
                                        }
                                        if (rs.isHandbrake_on()) {
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
                                            exitLoc.setYaw(yaw);
                                            // get location from database
                                            final Location exitTardis = exitLoc;
                                            // make location safe ie. outside of the bluebox
                                            double ex = exitTardis.getX();
                                            double ez = exitTardis.getZ();
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
                                            // exit TARDIS!
                                            player.playSound(player.getLocation(), Sound.DOOR_CLOSE, 1, 1);
                                            movePlayer(player, exitTardis, true, playerWorld, userQuotes);
                                            // remove player from traveller table
                                            HashMap<String, Object> wherd = new HashMap<String, Object>();
                                            wherd.put("player", playerNameStr);
                                            qf.doDelete("travellers", wherd);
                                            if (hasTrav) {
                                                plugin.tardisHasTravelled.remove(Integer.valueOf(id));
                                            }
                                        } else {
                                            player.sendMessage(plugin.pluginName + "The TARDIS is still travelling... you would get lost in the time vortex!");
                                        }
                                    } else {
                                        // is the TARDIS materialising?
                                        if (plugin.tardisMaterialising.contains(id)) {
                                            player.sendMessage(plugin.pluginName + "The TARDIS is still travelling... you would get lost in the time vortex!");
                                            return;
                                        }
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
                                                            //String lc_name = c.toLowerCase(Locale.ENGLISH);
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
                                                    plugin.debug(plugin.pluginName + "Could not convert to number!");
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
                                                player.playSound(player.getLocation(), Sound.DOOR_OPEN, 1, 1);
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
                                                movePlayer(player, tardis_loc, false, playerWorld, userQuotes);
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
                            } else {
                                String grammar;
                                if (!material.equals(Material.AIR)) {
                                    grammar = (TARDISConstants.vowels.contains(material.toString().substring(0, 1))) ? "an " + material : "a " + material;
                                } else {
                                    grammar = "nothing";
                                }
                                player.sendMessage(plugin.pluginName + "The TARDIS key is a " + key + ". You have " + grammar + " in your hand!");
                            }
                        }
                    }
                } else {
                    player.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                }
            }
        }
    }
    Random r = new Random();

    public void movePlayer(final Player p, Location l, final boolean exit, final World from, boolean q) {

        final int i = r.nextInt(plugin.quotelen);
        final Location theLocation = l;
        final World to = theLocation.getWorld();
        final boolean allowFlight = p.getAllowFlight();
        final boolean crossWorlds = from != to;
        final boolean quotes = q;
        final String name = p.getName();
        // try loading chunk
        World world = l.getWorld();
        final boolean isSurvival = checkSurvival(world);

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
                if (p.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds && !isSurvival)) {
                    p.setAllowFlight(true);
                }
                if (quotes) {
                    p.sendMessage(plugin.pluginName + plugin.quote.get(i));
                }
                if (exit) {
                    // give some artron energy
                    QueryFactory qf = new QueryFactory(plugin);
                    // add energy to player
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", name);
                    int player_artron = (plugin.getConfig().getBoolean("create_worlds")) ? plugin.getConfig().getInt("player") : plugin.getConfig().getInt("player") * 10;
                    qf.alterEnergyLevel("player_prefs", player_artron, where, p);
                }
                // give a key
                giveKey(p);
            }
        }, 10L);
    }

    private boolean checkSurvival(World w) {
        boolean bool = false;
        if (plugin.pm.isPluginEnabled("Multiverse-Core")) {
            MultiverseCore mv = (MultiverseCore) plugin.pm.getPlugin("Multiverse-Core");
            MultiverseWorld mvw = mv.getCore().getMVWorldManager().getMVWorld(w);
            GameMode gm = mvw.getGameMode();
            if (gm.equals(GameMode.SURVIVAL)) {
                bool = true;
            }
        }
        if (plugin.pm.isPluginEnabled("MultiWorld")) {
            MultiWorldAPI mw = ((MultiWorldPlugin) plugin.pm.getPlugin("MultiWorld")).getApi();
            MultiWorldWorldData mww = mw.getWorld(w.getName());
            if (!mww.isOptionSet(FlagName.CREATIVEWORLD)) {
                bool = true;
            }
        }
        return bool;
    }

    @SuppressWarnings("deprecation")
    private void giveKey(Player p) {
        String key;
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", p.getName());
        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
        if (rsp.resultSet()) {
            key = (!rsp.getKey().equals("")) ? rsp.getKey() : plugin.getConfig().getString("key");
        } else {
            key = plugin.getConfig().getString("key");
        }
        if (plugin.getConfig().getBoolean("give_key") && (bukkitversion.compareTo(preIMversion) > 0 || (bukkitversion.compareTo(preIMversion) == 0 && SUBversion.compareTo(preSUBversion) >= 0)) && !key.equals("AIR")) {
            Inventory inv = p.getInventory();
            Material m = Material.valueOf(key);
            if (!inv.contains(m)) {
                ItemStack is = new ItemStack(m, 1);
                TARDISItemRenamer ir = new TARDISItemRenamer(is);
                ir.setName("Sonic Screwdriver", true);
                inv.addItem(is);
                p.updateInventory();
                p.sendMessage(plugin.pluginName + "Don't forget your TARDIS key!");
            }
        }
    }
}
