/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.achievement.TARDISAchievementFactory;
import me.eccentric_nz.TARDIS.api.event.TARDISCreationEvent;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.builders.TARDISSpace;
import me.eccentric_nz.TARDIS.database.*;
import me.eccentric_nz.TARDIS.enumeration.ADVANCEMENT;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * TARDISes are bioships that are grown from a species of coral presumably indigenous to Gallifrey.
 * <p>
 * The TARDIS had a drawing room, which the Doctor claimed to be his "private study". Inside it were momentos of his
 * many incarnations' travels.
 *
 * @author eccentric_nz
 */
public class TARDISBlockStackListener implements Listener {

    private final TARDIS plugin;

    private final List<Material> blocks = new ArrayList<>();
    private Material custom;

    public TARDISBlockStackListener(TARDIS plugin) {
        this.plugin = plugin;
        blocks.add(Material.IRON_BLOCK); // budget
        blocks.add(Material.GOLD_BLOCK); // bigger
        blocks.add(Material.DIAMOND_BLOCK); // deluxe
        blocks.add(Material.LAPIS_BLOCK); // tom baker
        blocks.add(Material.BOOKSHELF); // wood plank
        blocks.add(Material.EMERALD_BLOCK); // eleventh
        blocks.add(Material.REDSTONE_BLOCK); // redstone
        blocks.add(Material.QUARTZ_BLOCK); // ARS
        blocks.add(Material.COAL_BLOCK); // steampunk
        plugin.getCustomConsolesConfig().getKeys(false).forEach((console) -> {
            if (plugin.getCustomConsolesConfig().getBoolean(console + ".enabled")) {
                try {
                    custom = Material.valueOf(plugin.getCustomConsolesConfig().getString(console + ".seed"));
                    blocks.add(custom); // custom
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid custom seed block material for " + console + "!");
                }
            }
        });
    }

    /**
     * Listens for player block placing. If the player places a stack of blocks in a certain pattern for example (but
     * not limited to): IRON_BLOCK, LAPIS_BLOCK, RESTONE_TORCH the pattern of blocks is turned into a TARDIS.
     *
     * @param event a player placing a block
     */
    @EventHandler(ignoreCancelled = true)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();
        // only listen for redstone torches
        if (block.getType() == Material.REDSTONE_TORCH) {
            Player player = event.getPlayer();
            Block blockBelow = block.getRelative(BlockFace.DOWN);
            Material wall_type = blockBelow.getType();
            Block blockBottom = blockBelow.getRelative(BlockFace.DOWN);
            // only continue if the redstone torch is placed on top of [JUST ABOUT ANY] BLOCK on top of an IRON/GOLD/DIAMOND_BLOCK
            if (plugin.getBlocksConfig().getStringList("tardis_blocks").contains(blockBelow.getType().toString()) && blocks.contains(blockBottom.getType())) {
                if (!plugin.getConfig().getBoolean("worlds." + block.getLocation().getWorld().getName())) {
                    TARDISMessage.send(player, "WORLD_NO_TARDIS");
                    return;
                }
                if (!plugin.getConfig().getString("creation.area").equals("none")) {
                    String area = plugin.getConfig().getString("creation.area");
                    if (plugin.getTardisArea().areaCheckInExile(area, block.getLocation())) {
                        TARDISMessage.send(player, "TARDIS_ONLY_AREA", area);
                        return;
                    }
                }
                SCHEMATIC schm;
                int max_count = plugin.getConfig().getInt("creation.count");
                int player_count = 0;
                int grace_count = 0;
                boolean has_count = false;
                ResultSetCount rsc = new ResultSetCount(plugin, player.getUniqueId().toString());
                if (rsc.resultSet()) {
                    player_count = rsc.getCount();
                    grace_count = rsc.getGrace();
                    has_count = true;
                    if (player_count == max_count && max_count > 0) {
                        TARDISMessage.send(player, "COUNT_QUOTA");
                        return;
                    }
                }
                Location block_loc = blockBottom.getLocation();
                // check it is not another Time Lords home location
                HashMap<String, Object> where = new HashMap<>();
                where.put("world", block_loc.getWorld().getName());
                where.put("x", block_loc.getBlockX());
                where.put("y", block_loc.getBlockY());
                where.put("z", block_loc.getBlockZ());
                ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, where);
                if (rsh.resultSet()) {
                    TARDISMessage.send(player, "TARDIS_NO_HOME");
                    return;
                }
                schm = CONSOLES.SCHEMATICFor(blockBottom.getType());
                if (schm == null) {
                    schm = CONSOLES.getBY_NAMES().get("BUDGET");
                }
                // check perms
                if (!schm.getPermission().equals("budget") && !player.hasPermission("tardis." + schm.getPermission())) {
                    TARDISMessage.send(player, "NO_PERM_TARDIS", schm.getPermission().toUpperCase(Locale.ENGLISH));
                    return;
                }
                if (player.hasPermission("tardis.create")) {
                    String playerNameStr = player.getName();
                    // check to see if they already have a TARDIS
                    ResultSetTardisID rs = new ResultSetTardisID(plugin);
                    if (!rs.fromUUID(player.getUniqueId().toString())) {
                        Chunk chunk = blockBottom.getChunk();
                        // get this chunk co-ords
                        int cx;
                        int cz;
                        String cw;
                        World chunkworld;
                        boolean tips = false;
                        // TODO name worlds without player name
                        if (plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) {
                            // create a new world to store this TARDIS
                            cw = "TARDIS_WORLD_" + playerNameStr;
                            TARDISSpace space = new TARDISSpace(plugin);
                            chunkworld = space.getTardisWorld(cw);
                            cx = 0;
                            cz = 0;
                        } else if (plugin.getConfig().getBoolean("creation.default_world") && plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && player.hasPermission("tardis.create_world")) {
                            // create a new world to store this TARDIS
                            cw = "TARDIS_WORLD_" + playerNameStr;
                            TARDISSpace space = new TARDISSpace(plugin);
                            chunkworld = space.getTardisWorld(cw);
                            cx = 0;
                            cz = 0;
                        } else {
                            // check config to see whether we are using a default world to store TARDISes
                            if (plugin.getConfig().getBoolean("creation.default_world")) {
                                cw = plugin.getConfig().getString("creation.default_world_name");
                                chunkworld = plugin.getServer().getWorld(cw);
                                tips = true;
                            } else {
                                chunkworld = chunk.getWorld();
                                cw = chunkworld.getName();
                            }
                            cx = chunk.getX();
                            cz = chunk.getZ();
                            if (!plugin.getConfig().getBoolean("creation.default_world") && plugin.getLocationUtils().checkChunk(cw, cx, cz, schm)) {
                                TARDISMessage.send(player, "TARDIS_EXISTS");
                                return;
                            }
                        }
                        // get player direction
                        String d = TARDISStaticUtils.getPlayersDirection(player, false);
                        // save data to database (tardis table)
                        String biome = block_loc.getBlock().getBiome().toString();
                        String chun = cw + ":" + cx + ":" + cz;
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("uuid", player.getUniqueId().toString());
                        set.put("owner", playerNameStr);
                        set.put("chunk", chun);
                        set.put("size", schm.getPermission().toUpperCase(Locale.ENGLISH));
                        Long now;
                        if (player.hasPermission("tardis.prune.bypass")) {
                            now = Long.MAX_VALUE;
                        } else {
                            now = System.currentTimeMillis();
                        }
                        set.put("lastuse", now);
                        // set preset if default is not 'FACTORY'
                        String preset = plugin.getConfig().getString("police_box.default_preset").toUpperCase(Locale.ENGLISH);
                        set.put("chameleon_preset", preset);
                        set.put("chameleon_demat", preset);
                        HashMap<String, Object> setpp = new HashMap<>();
                        if (wall_type.equals(Material.LAPIS_BLOCK)) {
                            if (blockBottom.getType().equals(Material.EMERALD_BLOCK)) {
                                setpp.put("wall", "LIGHT_GRAY_WOOL");
                            } else {
                                setpp.put("wall", "ORANGE_WOOL");
                            }
                        } else {
                            // determine wall block material from HashMap
                            setpp.put("wall", wall_type.toString());
                        }
                        setpp.put("lanterns_on", (schm.getPermission().equals("eleventh") || schm.getPermission().equals("twelfth")) ? 1 : 0);
                        int lastInsertId = qf.doSyncInsert("tardis", set);
                        // insert/update  player prefs
                        HashMap<String, Object> wherep = new HashMap<>();
                        wherep.put("uuid", player.getUniqueId().toString());
                        ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherep);
                        if (!rsp.resultSet()) {
                            setpp.put("uuid", player.getUniqueId().toString());
                            qf.doInsert("player_prefs", setpp);
                        } else {
                            HashMap<String, Object> wherepp = new HashMap<>();
                            wherepp.put("uuid", player.getUniqueId().toString());
                            qf.doUpdate("player_prefs", setpp, wherepp);
                        }
                        // populate home, current, next and back tables
                        HashMap<String, Object> setlocs = new HashMap<>();
                        setlocs.put("tardis_id", lastInsertId);
                        setlocs.put("world", block_loc.getWorld().getName());
                        setlocs.put("x", block_loc.getBlockX());
                        setlocs.put("y", block_loc.getBlockY());
                        setlocs.put("z", block_loc.getBlockZ());
                        setlocs.put("direction", d);
                        qf.insertLocations(setlocs, biome, lastInsertId);
                        // remove redstone torch/lapis and iron blocks
                        block.setBlockData(TARDISConstants.AIR);
                        blockBelow.setBlockData(TARDISConstants.AIR);
                        blockBottom.setBlockData(TARDISConstants.AIR);
                        // turn the block stack into a TARDIS
                        BuildData bd = new BuildData(plugin, player.getUniqueId().toString());
                        bd.setDirection(COMPASS.valueOf(d));
                        bd.setLocation(block_loc);
                        bd.setMalfunction(false);
                        bd.setOutside(true);
                        bd.setPlayer(player);
                        bd.setRebuild(false);
                        bd.setSubmarine(isSub(blockBottom));
                        bd.setTardisID(lastInsertId);
                        plugin.getPM().callEvent(new TARDISCreationEvent(player, lastInsertId, block_loc));
                        plugin.getPresetBuilder().buildPreset(bd);
                        plugin.getInteriorBuilder().buildInner(schm, chunkworld, lastInsertId, player, wall_type, Material.LIGHT_GRAY_WOOL, tips);
                        // set achievement completed
                        if (player.hasPermission("tardis.book")) {
                            HashMap<String, Object> seta = new HashMap<>();
                            seta.put("completed", 1);
                            HashMap<String, Object> wherea = new HashMap<>();
                            wherea.put("uuid", player.getUniqueId().toString());
                            wherea.put("name", "tardis");
                            qf.doUpdate("achievements", seta, wherea);
                            // award advancement
                            TARDISAchievementFactory.grantAdvancement(ADVANCEMENT.TARDIS, player);
                        }
                        if (max_count > 0) {
                            TARDISMessage.send(player, "COUNT", String.format("%d", (player_count + 1)), String.format("%d", max_count));
                        }
                        HashMap<String, Object> setc = new HashMap<>();
                        setc.put("count", player_count + 1);
                        setc.put("grace", grace_count);
                        if (has_count) {
                            // update the player's TARDIS count
                            HashMap<String, Object> wheretc = new HashMap<>();
                            wheretc.put("uuid", player.getUniqueId().toString());
                            qf.doUpdate("t_count", setc, wheretc);
                        } else {
                            // insert new TARDIS count record
                            setc.put("uuid", player.getUniqueId().toString());
                            qf.doInsert("t_count", setc);
                        }
                    } else {
                        HashMap<String, Object> wherecl = new HashMap<>();
                        wherecl.put("tardis_id", rs.getTardis_id());
                        ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
                        if (rscl.resultSet()) {
                            TARDISMessage.send(player, "TARDIS_HAVE", rscl.getWorld().getName() + " at x:" + rscl.getX() + " y:" + rscl.getY() + " z:" + rscl.getZ());
                        } else {
                            TARDISMessage.send(player, "HAVE_TARDIS");
                        }
                    }
                } else {
                    TARDISMessage.send(player, "NO_PERM_TARDIS");
                }
            }
        }
    }

    private boolean isSub(Block b) {
        return b.getRelative(BlockFace.EAST).getType().equals(Material.WATER);
    }
}
