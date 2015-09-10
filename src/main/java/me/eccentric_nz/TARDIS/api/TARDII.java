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
package me.eccentric_nz.TARDIS.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/**
 *
 * @author eccentric_nz
 */
public class TARDII implements TardisAPI {

    private HashMap<String, Integer> timelords;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    Random random = new Random();

    @Override
    public HashMap<String, Integer> getTimelordMap() {
        timelords = new HashMap<String, Integer>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT tardis_id, owner FROM " + TARDIS.plugin.getPrefix() + "tardis";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    timelords.put(rs.getString("owner"), rs.getInt("tardis_id"));
                }
            }
        } catch (SQLException e) {
            TARDIS.plugin.debug("ResultSet error for tardis table! " + e.getMessage());

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                TARDIS.plugin.debug("Error closing tardis table! " + e.getMessage());
            }
        }
        return timelords;
    }

    @Override
    public Location getTARDISCurrentLocation(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(TARDIS.plugin, where);
        if (rs.resultSet()) {
            return new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
        }
        return null;
    }

    @Override
    public Location getTARDISCurrentLocation(Player p) {
        return getTARDISCurrentLocation(p.getUniqueId());
    }

    @Override
    public Location getTARDISCurrentLocation(UUID uuid) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(TARDIS.plugin, where);
        if (rs.resultSet()) {
            return new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
        }
        return null;
    }

    @Override
    public Location getTARDISNextLocation(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetNextLocation rs = new ResultSetNextLocation(TARDIS.plugin, where);
        if (rs.resultSet()) {
            return new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
        }
        return null;
    }

    @Override
    public Location getRandomLocation(List<String> worlds, Environment environment, Parameters param) {
        if (environment == null) {
            // choose random environment
            environment = Environment.values()[random.nextInt(Environment.values().length)];
        }
        switch (environment) {
            case NETHER:
                return new TARDISRandomNether(TARDIS.plugin, worlds, param).getlocation();
            case THE_END:
                return new TARDISRandomTheEnd(TARDIS.plugin, worlds, param).getlocation();
            default:
                return new TARDISRandomOverworld(TARDIS.plugin, worlds, param).getlocation();
        }
    }

    @Override
    public Location getRandomLocation(List<String> worlds, Environment environment, Player p) {
        return getRandomLocation(getWorlds(), null, new Parameters(p, FLAG.getAPIFlags()));
    }

    @Override
    public Location getRandomLocation(List<String> worlds, Player p) {
        return getRandomLocation(getWorlds(), null, new Parameters(p, FLAG.getAPIFlags()));
    }

    @Override
    public Location getRandomOverworldLocation(Player p) {
        return getRandomLocation(getWorlds(), Environment.NORMAL, p);
    }

    @Override
    public Location getRandomOverworldLocation(String world, Player p) {
        return getRandomLocation(Arrays.asList(world), Environment.NORMAL, p);
    }

    @Override
    public Location getRandomNetherLocation(Player p) {
        return getRandomLocation(getWorlds(), Environment.NETHER, p);
    }

    @Override
    public Location getRandomNetherLocation(String world, Player p) {
        return getRandomLocation(Arrays.asList(world), Environment.NETHER, p);
    }

    @Override
    public Location getRandomEndLocation(Player p) {
        return getRandomLocation(getWorlds(), Environment.THE_END, p);
    }

    @Override
    public Location getRandomEndLocation(String world, Player p) {
        return getRandomLocation(Arrays.asList(world), Environment.THE_END, p);
    }

    @Override
    public List<String> getWorlds() {
        List<String> worlds = new ArrayList<String>();
        for (World w : Bukkit.getWorlds()) {
            String name = w.getName();
            if (TARDIS.plugin.getConfig().getBoolean("worlds." + name)) {
                worlds.add(name);
            }
        }
        return worlds;
    }

    @Override
    public List<String> getOverWorlds() {
        List<String> worlds = new ArrayList<String>();
        for (World w : Bukkit.getWorlds()) {
            String name = w.getName();
            if (TARDIS.plugin.getConfig().getBoolean("worlds." + name) && !w.getEnvironment().equals(Environment.NETHER) && !w.getEnvironment().equals(Environment.THE_END)) {
                worlds.add(name);
            }
        }
        return worlds;
    }

    @Override
    public String getTARDISPlayerIsIn(Player p) {
        return getTARDISPlayerIsIn(p.getUniqueId());
    }

    @Override
    public String getTARDISPlayerIsIn(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null && p.isOnline()) {
            String str = " is not in any TARDIS.";
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers rs = new ResultSetTravellers(TARDIS.plugin, where, false);
            if (rs.resultSet()) {
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", rs.getTardis_id());
                ResultSetTardis rst = new ResultSetTardis(TARDIS.plugin, wheret, "", false);
                if (rst.resultSet()) {
                    str = " is in " + rst.getOwner() + "'s TARDIS.";
                }
            }
            return p.getName() + str;
        }
        return "Player is not online.";
    }

    @Override
    public int getIdOfTARDISPlayerIsIn(Player p) {
        return getIdOfTARDISPlayerIsIn(p.getUniqueId());
    }

    @Override
    public int getIdOfTARDISPlayerIsIn(UUID uuid) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetTravellers rs = new ResultSetTravellers(TARDIS.plugin, where, false);
        if (rs.resultSet()) {
            return rs.getTardis_id();
        }
        return -1;
    }

    @Override
    public List<String> getPlayersInTARDIS(int id) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTravellers rs = new ResultSetTravellers(TARDIS.plugin, where, true);
        if (rs.resultSet()) {
            for (UUID u : rs.getData()) {
                Player p = Bukkit.getPlayer(u);
                if (p != null && p.isOnline()) {
                    list.add(p.getName());
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getPlayersInTARDIS(Player p) {
        return getPlayersInTARDIS(p.getUniqueId());
    }

    @Override
    public List<String> getPlayersInTARDIS(UUID uuid) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(TARDIS.plugin, where, "", false);
        if (rs.resultSet()) {
            return getPlayersInTARDIS(rs.getTardis_id());
        } else {
            return new ArrayList<String>();
        }
    }

    @Override
    public List<String> getTARDISCompanions(int id) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(TARDIS.plugin, where, "", false);
        if (rs.resultSet()) {
            String companions = rs.getCompanions();
            if (!companions.isEmpty()) {
                for (String s : companions.split(":")) {
                    Player p = Bukkit.getPlayer(s);
                    if (p != null && p.isOnline()) {
                        list.add(p.getName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getTARDISCompanions(Player p) {
        return getTARDISCompanions(p.getUniqueId());
    }

    @Override
    public List<String> getTARDISCompanions(UUID uuid) {
        List<String> list = new ArrayList<String>();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(TARDIS.plugin, where, "", false);
        if (rs.resultSet()) {
            String companions = rs.getCompanions();
            if (!companions.isEmpty()) {
                for (String s : companions.split(":")) {
                    Player p = Bukkit.getPlayer(s);
                    if (p != null && p.isOnline()) {
                        list.add(p.getName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public boolean isPlayerInZeroRoom(Player p) {
        return isPlayerInZeroRoom(p.getUniqueId());
    }

    @Override
    public boolean isPlayerInZeroRoom(UUID uuid) {
        return TARDIS.plugin.getTrackerKeeper().getZeroRoomOccupants().contains(uuid);
    }

    @Override
    public boolean isPlayerGeneticallyModified(Player p) {
        return isPlayerGeneticallyModified(p.getUniqueId());
    }

    @Override
    public boolean isPlayerGeneticallyModified(UUID uuid) {
        return TARDIS.plugin.getTrackerKeeper().getGeneticallyModified().contains(uuid);
    }

    @Override
    public TARDISUtils getUtils() {
        return TARDIS.plugin.getUtils();
    }

    @Override
    public TARDISLocationGetters getLocationUtils() {
        return TARDIS.plugin.getLocationUtils();
    }

    @Override
    public TARDISBlockSetters getBlockUtils() {
        return TARDIS.plugin.getBlockUtils();
    }

    @Override
    public TARDISPluginRespect getRespect() {
        return TARDIS.plugin.getPluginRespect();
    }

    @Override
    public HashMap<String, ShapedRecipe> getShapedRecipes() {
        return TARDIS.plugin.getFigura().getShapedRecipes();
    }

    @Override
    public HashMap<String, ShapelessRecipe> getShapelessRecipes() {
        return TARDIS.plugin.getIncomposita().getShapelessRecipes();
    }
}
