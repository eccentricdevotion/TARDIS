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
package me.eccentric_nz.tardis.api;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.TardisTrackerInstanceKeeper;
import me.eccentric_nz.tardis.blueprints.*;
import me.eccentric_nz.tardis.builders.BuildData;
import me.eccentric_nz.tardis.builders.TardisAbandoned;
import me.eccentric_nz.tardis.custommodeldata.TardisSeedModel;
import me.eccentric_nz.tardis.database.TardisDatabaseConnection;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.*;
import me.eccentric_nz.tardis.desktop.TardisUpgradeData;
import me.eccentric_nz.tardis.desktop.TardisWallFloorRunnable;
import me.eccentric_nz.tardis.enumeration.*;
import me.eccentric_nz.tardis.flight.TardisTakeoff;
import me.eccentric_nz.tardis.move.TardisTeleportLocation;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.rooms.TardisWalls;
import me.eccentric_nz.tardis.travel.TardisPluginRespect;
import me.eccentric_nz.tardis.utility.TardisLocationGetters;
import me.eccentric_nz.tardis.utility.TardisStringUtils;
import me.eccentric_nz.tardis.utility.TardisUtils;
import me.eccentric_nz.tardis.utility.WeightedChoice;
import org.bukkit.*;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class Tardises implements TardisApi {

    private static final WeightedChoice<Environment> WEIGHTED_CHOICE = new WeightedChoice<Environment>().add(70, Environment.NORMAL).add(15, Environment.NETHER).add(15, Environment.THE_END);
    private final TardisDatabaseConnection service = TardisDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();

    @Override
    public HashMap<String, Integer> getTimeLordMap() {
        HashMap<String, Integer> timeLords = new HashMap<>();
        Statement statement = null;
        ResultSet resultSet = null;
        String query = "SELECT tardis_id, owner FROM " + TardisPlugin.plugin.getPrefix() + "tardis";
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    timeLords.put(resultSet.getString("owner"), resultSet.getInt("tardis_id"));
                }
            }
        } catch (SQLException sqlException) {
            TardisPlugin.plugin.debug("ResultSet error for tardis table! " + sqlException.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException sqlException) {
                TardisPlugin.plugin.debug("Error closing tardis table! " + sqlException.getMessage());
            }
        }
        return timeLords;
    }

    @Override
    public Location getTardisCurrentLocation(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation resultSetCurrentLocation = new ResultSetCurrentLocation(TardisPlugin.plugin, where);
        if (resultSetCurrentLocation.resultSet()) {
            return new Location(resultSetCurrentLocation.getWorld(), resultSetCurrentLocation.getX(), resultSetCurrentLocation.getY(), resultSetCurrentLocation.getZ());
        }
        return null;
    }

    @Override
    public Location getTardisCurrentLocation(Player player) {
        return getTardisCurrentLocation(player.getUniqueId());
    }

    @Override
    public Location getTardisCurrentLocation(UUID uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetCurrentLocation resultSetCurrentLocation = new ResultSetCurrentLocation(TardisPlugin.plugin, where);
        if (resultSetCurrentLocation.resultSet()) {
            return new Location(resultSetCurrentLocation.getWorld(), resultSetCurrentLocation.getX(), resultSetCurrentLocation.getY(), resultSetCurrentLocation.getZ());
        }
        return null;
    }

    @Override
    public Location getTardisNextLocation(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetNextLocation resultSetNextLocation = new ResultSetNextLocation(TardisPlugin.plugin, where);
        if (resultSetNextLocation.resultSet()) {
            return new Location(resultSetNextLocation.getWorld(), resultSetNextLocation.getX(), resultSetNextLocation.getY(), resultSetNextLocation.getZ());
        }
        return null;
    }

    @Override
    public TardisData getTardisMapData(int id) {
        TardisData tardisData = null;
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis resultSetTardis = new ResultSetTardis(TardisPlugin.plugin, where, "", false, 2);
        if (resultSetTardis.resultSet()) {
            Tardis tardis = resultSetTardis.getTardis();
            Location current = getTardisCurrentLocation(id);
            String console = tardis.getSchematic().getPermission().toUpperCase(Locale.ENGLISH);
            String chameleon = tardis.getPreset().toString();
            String door = "Closed";
            for (Map.Entry<Location, TardisTeleportLocation> map : TardisPlugin.plugin.getTrackerKeeper().getPortals().entrySet()) {
                if (!Objects.requireNonNull(map.getKey().getWorld()).getName().contains("TARDIS") && !map.getValue().isAbandoned()) {
                    if (id == map.getValue().getTardisId()) {
                        door = "Open";
                        break;
                    }
                }
            }
            String powered = (tardis.isPowered()) ? "Yes" : "No";
            String siege = (tardis.isSiegeOn()) ? "Yes" : "No";
            String abandoned = (tardis.isAbandoned()) ? "Yes" : "No";
            List<String> occupants = getPlayersInTardis(id);
            tardisData = new TardisData(current, console, chameleon, door, powered, siege, abandoned, occupants);
        }
        return tardisData;
    }

    @Override
    public Location getRandomLocation(List<String> worlds, Environment environment, Parameters parameters) {
        if (environment == null) {
            // choose random environment - weighted towards normal!
            environment = WEIGHTED_CHOICE.next();
            // check if environment is enabled
            if ((environment.equals(Environment.NETHER) && !TardisPlugin.plugin.getConfig().getBoolean("travel.nether")) || (environment.equals(Environment.THE_END) && !TardisPlugin.plugin.getConfig().getBoolean("travel.the_end"))) {
                environment = Environment.NORMAL;
            }
        }
        return switch (environment) {
            case NETHER -> new TardisRandomNether(TardisPlugin.plugin, worlds, parameters).getLocation();
            case THE_END -> new TardisRandomTheEnd(TardisPlugin.plugin, worlds, parameters).getLocation();
            default -> new TardisRandomOverworld(TardisPlugin.plugin, worlds, parameters).getLocation();
        };
    }

    @Override
    public Location getRandomLocation(List<String> worlds, Environment environment, Player player) {
        return getRandomLocation(getWorlds(), null, new Parameters(player, Flag.getApiFlags()));
    }

    @Override
    public Location getRandomLocation(List<String> worlds, Player player) {
        return getRandomLocation(getWorlds(), null, new Parameters(player, Flag.getApiFlags()));
    }

    @Override
    public Location getRandomOverworldLocation(Player player) {
        return getRandomLocation(getWorlds(), Environment.NORMAL, player);
    }

    @Override
    public Location getRandomOverworldLocation(String world, Player player) {
        return getRandomLocation(Collections.singletonList(world), Environment.NORMAL, player);
    }

    @Override
    public Location getRandomNetherLocation(Player player) {
        return getRandomLocation(getWorlds(), Environment.NETHER, player);
    }

    @Override
    public Location getRandomNetherLocation(String world, Player player) {
        return getRandomLocation(Collections.singletonList(world), Environment.NETHER, player);
    }

    @Override
    public Location getRandomEndLocation(Player player) {
        return getRandomLocation(getWorlds(), Environment.THE_END, player);
    }

    @Override
    public Location getRandomEndLocation(String world, Player player) {
        return getRandomLocation(Collections.singletonList(world), Environment.THE_END, player);
    }

    @Override
    public List<String> getWorlds() {
        List<String> worlds = new ArrayList<>();
        Bukkit.getWorlds().forEach((world) -> {
            String name = world.getName();
            if (TardisPlugin.plugin.getPlanetsConfig().getBoolean("planets." + name + ".time_travel")) {
                if (TardisPlugin.plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                    worlds.add(TardisPlugin.plugin.getMultiverseHelper().getAlias(name));
                } else {
                    worlds.add(TardisAliasResolver.getWorldAlias(name));
                }
            }
        });
        return worlds;
    }

    @Override
    public List<String> getOverworlds() {
        List<String> worlds = new ArrayList<>();
        Bukkit.getWorlds().forEach((world) -> {
            String name = world.getName();
            if (TardisPlugin.plugin.getPlanetsConfig().getBoolean("planets." + name + ".time_travel") && !world.getEnvironment().equals(Environment.NETHER) && !world.getEnvironment().equals(Environment.THE_END)) {
                if (TardisPlugin.plugin.getWorldManager().equals(WorldManager.MULTIVERSE)) {
                    worlds.add(TardisPlugin.plugin.getMultiverseHelper().getAlias(name));
                } else {
                    worlds.add(TardisAliasResolver.getWorldAlias(name));
                }
            }
        });
        return worlds;
    }

    @Override
    public String getTardisPlayerIsIn(Player player) {
        return getTardisPlayerIsIn(player.getUniqueId());
    }

    @Override
    public String getTardisPlayerIsIn(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            String string = " is not in any TARDIS.";
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTravellers resultSetTravellers = new ResultSetTravellers(TardisPlugin.plugin, where, false);
            if (resultSetTravellers.resultSet()) {
                HashMap<String, Object> whereTardis = new HashMap<>();
                whereTardis.put("tardis_id", resultSetTravellers.getTardisId());
                ResultSetTardis rst = new ResultSetTardis(TardisPlugin.plugin, whereTardis, "", false, 2);
                if (rst.resultSet()) {
                    if (rst.getTardis().isAbandoned()) {
                        string = " is in an abandoned TARDIS.";
                    } else {
                        string = " is in " + rst.getTardis().getOwner() + "'s TARDIS.";
                    }
                }
            }
            return player.getName() + string;
        }
        return "Player is not online.";
    }

    @Override
    public int getIdOfTardisPlayerIsIn(Player player) {
        return getIdOfTardisPlayerIsIn(player.getUniqueId());
    }

    @Override
    public int getIdOfTardisPlayerIsIn(UUID uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTravellers resultSetTravellers = new ResultSetTravellers(TardisPlugin.plugin, where, false);
        if (resultSetTravellers.resultSet()) {
            return resultSetTravellers.getTardisId();
        }
        return -1;
    }

    @Override
    public List<String> getPlayersInTardis(int id) {
        List<String> list = new ArrayList<>();
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTravellers resultSetTravellers = new ResultSetTravellers(TardisPlugin.plugin, where, true);
        if (resultSetTravellers.resultSet()) {
            resultSetTravellers.getData().forEach((uuid) -> {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    list.add(player.getName());
                }
            });
        }
        return list;
    }

    @Override
    public List<String> getPlayersInTardis(Player player) {
        return getPlayersInTardis(player.getUniqueId());
    }

    @Override
    public List<String> getPlayersInTardis(UUID uuid) {
        ResultSetTardisId resultSetTardisId = new ResultSetTardisId(TardisPlugin.plugin);
        if (resultSetTardisId.fromUuid(uuid.toString())) {
            return getPlayersInTardis(resultSetTardisId.getTardisId());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<String> getTardisCompanions(int id) {
        List<String> list = new ArrayList<>();
        ResultSetTardisCompanions resultSetTardisCompanions = new ResultSetTardisCompanions(TardisPlugin.plugin);
        if (resultSetTardisCompanions.fromId(id)) {
            String companions = resultSetTardisCompanions.getCompanions();
            if (!companions.isEmpty()) {
                for (String string : companions.split(":")) {
                    Player player = Bukkit.getPlayer(string);
                    if (player != null && player.isOnline()) {
                        list.add(player.getName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getTardisCompanions(Player player) {
        return getTardisCompanions(player.getUniqueId());
    }

    @Override
    public List<String> getTardisCompanions(UUID uuid) {
        List<String> list = new ArrayList<>();
        ResultSetTardisCompanions resultSetTardisCompanions = new ResultSetTardisCompanions(TardisPlugin.plugin);
        if (resultSetTardisCompanions.fromUuid(uuid.toString())) {
            String companions = resultSetTardisCompanions.getCompanions();
            if (!companions.isEmpty()) {
                for (String string : companions.split(":")) {
                    Player player = Bukkit.getPlayer(string);
                    if (player != null && player.isOnline()) {
                        list.add(player.getName());
                    }
                }
            }
        }
        return list;
    }

    @Override
    public boolean isPlayerInZeroRoom(Player player) {
        return isPlayerInZeroRoom(player.getUniqueId());
    }

    @Override
    public boolean isPlayerInZeroRoom(UUID uuid) {
        return TardisPlugin.plugin.getTrackerKeeper().getZeroRoomOccupants().contains(uuid);
    }

    @Override
    public boolean isPlayerGeneticallyModified(Player player) {
        return isPlayerGeneticallyModified(player.getUniqueId());
    }

    @Override
    public boolean isPlayerGeneticallyModified(UUID uuid) {
        return TardisPlugin.plugin.getTrackerKeeper().getGeneticallyModified().contains(uuid);
    }

    @Override
    public TardisUtils getUtils() {
        return TardisPlugin.plugin.getUtils();
    }

    @Override
    public TardisLocationGetters getLocationUtils() {
        return TardisPlugin.plugin.getLocationUtils();
    }

    @Override
    public TardisPluginRespect getRespect() {
        return TardisPlugin.plugin.getPluginRespect();
    }

    @Override
    public HashMap<String, ShapedRecipe> getShapedRecipes() {
        return TardisPlugin.plugin.getShapedRecipe().getShapedRecipes();
    }

    @Override
    public HashMap<String, ShapelessRecipe> getShapelessRecipes() {
        return TardisPlugin.plugin.getShapelessRecipe().getShapelessRecipes();
    }

    @Override
    public ItemStack getTardisShapeItem(String item, Player player) {
        ItemStack result;
        if (item.equals("Save Storage Disk") || item.equals("Preset Storage Disk") || item.equals("Biome Storage Disk") || item.equals("Player Storage Disk") || item.equals("Bowl of Custard") || item.endsWith("Jelly Baby")) {
            ShapelessRecipe recipe = TardisPlugin.plugin.getShapelessRecipe().getShapelessRecipes().get(item);
            result = recipe.getResult();
        } else {
            ShapedRecipe recipe = TardisPlugin.plugin.getShapedRecipe().getShapedRecipes().get(item);
            if (recipe == null) {
                return null;
            }
            result = recipe.getResult();
        }
        if (item.equals("TARDIS Invisibility Circuit")) {
            // set the second line of lore
            ItemMeta itemMeta = result.getItemMeta();
            assert itemMeta != null;
            List<String> lore = itemMeta.getLore();
            String uses = (Objects.equals(TardisPlugin.plugin.getConfig().getString("circuits.uses.invisibility"), "0") || !TardisPlugin.plugin.getConfig().getBoolean("circuits.damage")) ? ChatColor.YELLOW + "unlimited" : ChatColor.YELLOW + TardisPlugin.plugin.getConfig().getString("circuits.uses.invisibility");
            assert lore != null;
            lore.set(1, uses);
            itemMeta.setLore(lore);
            result.setItemMeta(itemMeta);
        }
        if (item.equals("Blank Storage Disk") || item.equals("Save Storage Disk") || item.equals("Preset Storage Disk") || item.equals("Biome Storage Disk") || item.equals("Player Storage Disk") || item.equals("Authorised Control Disk")) {
            ItemMeta itemMeta = result.getItemMeta();
            assert itemMeta != null;
            itemMeta.addItemFlags(ItemFlag.values());
            result.setItemMeta(itemMeta);
        }
        if (item.equals("TARDIS Key") || item.equals("Authorised Control Disk")) {
            ItemMeta itemMeta = result.getItemMeta();
            assert itemMeta != null;
            itemMeta.getPersistentDataContainer().set(TardisPlugin.plugin.getTimeLordUuidKey(), TardisPlugin.plugin.getPersistentDataTypeUuid(), player.getUniqueId());
            List<String> lore = itemMeta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
            String what = item.equals("key") ? "key" : "disk";
            lore.add(format + "This " + what + " belongs to");
            lore.add(format + player.getName());
            itemMeta.setLore(lore);
            result.setItemMeta(itemMeta);
        }
        return result;
    }

    @Override
    public HashMap<Schematic, ShapedRecipe> getSeedRecipes() {
        return TardisPlugin.plugin.getSeedRecipe().getSeedRecipes();
    }

    @Override
    public ItemStack getTardisSeedItem(String schematic) {
        if (Consoles.getBY_NAMES().containsKey(schematic)) {
            ItemStack itemStack;
            int model = TardisSeedModel.modelByString(schematic);
            if (Consoles.getBY_NAMES().get(schematic).isCustom() || schematic.equalsIgnoreCase("DELTA") || schematic.equalsIgnoreCase("ROTOR") || schematic.equalsIgnoreCase("COPPER") || schematic.equalsIgnoreCase("CAVE") || schematic.equalsIgnoreCase("WEATHERED")) {
                itemStack = new ItemStack(Material.MUSHROOM_STEM, 1);
            } else {
                itemStack = new ItemStack(Material.RED_MUSHROOM_BLOCK, 1);
            }
            ItemMeta itemMeta = itemStack.getItemMeta();
            assert itemMeta != null;
            itemMeta.setCustomModelData(10000000 + model);
            itemMeta.getPersistentDataContainer().set(TardisPlugin.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
            // set display name
            itemMeta.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
            List<String> lore = new ArrayList<>();
            lore.add(schematic);
            lore.add("Walls: ORANGE_WOOL");
            lore.add("Floors: LIGHT_GRAY_WOOL");
            lore.add("Chameleon: FACTORY");
            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
        return null;
    }

    @Override
    public List<BlueprintType> getBlueprints() {
        // TODO
        return null;
    }

    @Override
    public ItemStack getTardisBlueprintItem(String item, Player player) {
        String[] split = item.split("_");
        if (split.length < 3) {
            return null;
        }
        try {
            BlueprintType type = BlueprintType.valueOf(split[1].toUpperCase());
            int substring = 11 + split[1].length(); // BLUEPRINT_+length()+_
            String upper = item.toUpperCase().substring(substring);
            String permission;
            switch (type) {
                case CONSOLE -> {
                    BlueprintConsole console = BlueprintConsole.valueOf(upper);
                    permission = console.getPermission();
                }
                case FEATURE -> {
                    BlueprintFeature feature = BlueprintFeature.valueOf(upper);
                    permission = feature.getPermission();
                }
                case PRESET -> {
                    BlueprintPreset preset = BlueprintPreset.valueOf(upper);
                    permission = preset.getPermission();
                }
                case ROOM -> {
                    BlueprintRoom room = BlueprintRoom.valueOf(upper);
                    permission = room.getPermission();
                }
                case SONIC -> {
                    BlueprintSonic sonic = BlueprintSonic.valueOf(upper);
                    permission = sonic.getPermission();
                }
                case TRAVEL -> {
                    BlueprintTravel travel = BlueprintTravel.valueOf(upper);
                    permission = travel.getPermission();
                }
                default -> { // BASE
                    BlueprintBase base = BlueprintBase.valueOf(upper);
                    permission = base.getPermission();
                }
            }
            if (permission != null) {
                ItemStack itemStack = new ItemStack(Material.MUSIC_DISC_MELLOHI, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                assert itemMeta != null;
                itemMeta.setCustomModelData(10000001);
                PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
                persistentDataContainer.set(TardisPlugin.plugin.getTimeLordUuidKey(), TardisPlugin.plugin.getPersistentDataTypeUuid(), player.getUniqueId());
                persistentDataContainer.set(TardisPlugin.plugin.getBlueprintKey(), PersistentDataType.STRING, permission);
                itemMeta.setDisplayName("TARDIS Blueprint Disk");
                List<String> lore = Arrays.asList(TardisStringUtils.capitalise(item), "Valid only for", player.getName());
                itemMeta.setLore(lore);
                itemMeta.addItemFlags(ItemFlag.values());
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }
            return null;
        } catch (IllegalArgumentException illegalArgumentException) {
            return null;
        }
    }

    @Override
    public List<String> getWallFloorBlocks() {
        List<String> blocks = new ArrayList<>();
        TardisWalls.BLOCKS.forEach((material) -> blocks.add(material.toString()));
        return blocks;
    }

    @Override
    public boolean setDestination(int id, Location location, boolean travel) {
        // get current direction
        HashMap<String, Object> whereCurrentDirection = new HashMap<>();
        whereCurrentDirection.put("tardis_id", id);
        ResultSetCurrentLocation resultSetCurrentLocation = new ResultSetCurrentLocation(TardisPlugin.plugin, whereCurrentDirection);
        if (resultSetCurrentLocation.resultSet()) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<>();
            set.put("world", Objects.requireNonNull(location.getWorld()).getName());
            set.put("x", location.getBlockX());
            set.put("y", location.getBlockY());
            set.put("z", location.getBlockZ());
            set.put("direction", resultSetCurrentLocation.getDirection().toString());
            set.put("submarine", 0);
            TardisPlugin.plugin.getQueryFactory().doUpdate("next", set, where);
            if (travel) {
                // get TARDIS data
                HashMap<String, Object> whereTardis = new HashMap<>();
                whereTardis.put("tardis_id", id);
                ResultSetTardis resultSetTardis = new ResultSetTardis(TardisPlugin.plugin, whereTardis, "", false, 2);
                if (resultSetTardis.resultSet()) {
                    Player player = Bukkit.getServer().getPlayer(resultSetTardis.getTardis().getUuid());
                    // travel
                    TardisPlugin.plugin.getTrackerKeeper().getHasDestination().put(id, TardisPlugin.plugin.getArtronConfig().getInt("random"));
                    new TardisTakeoff(TardisPlugin.plugin).run(id, player, resultSetTardis.getTardis().getBeacon());
                    return true;
                } else {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setDestination(UUID uuid, Location location, boolean travel) {
        // get tardis_id
        ResultSetTardisId resultSetTardisId = new ResultSetTardisId(TardisPlugin.plugin);
        if (resultSetTardisId.fromUuid(uuid.toString())) {
            return setDestination(resultSetTardisId.getTardisId(), location, travel);
        } else {
            return false;
        }
    }

    @Override
    public boolean setDestination(Player player, Location location, boolean travel) {
        return setDestination(player.getUniqueId(), location, travel);
    }

    @Override
    public Tardis getTardisData(int id) {
        // get TARDIS data
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis resultSetTardis = new ResultSetTardis(TardisPlugin.plugin, where, "", false, 2);
        if (resultSetTardis.resultSet()) {
            return resultSetTardis.getTardis();
        } else {
            return null;
        }
    }

    @Override
    public Tardis getTardisData(UUID uuid) {
        // get TARDIS data
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis resultSetTardis = new ResultSetTardis(TardisPlugin.plugin, where, "", false, 2);
        if (resultSetTardis.resultSet()) {
            return resultSetTardis.getTardis();
        } else {
            return null;
        }
    }

    @Override
    public Tardis getTardisData(Player player) {
        return getTardisData(player.getUniqueId());
    }

    @Override
    public boolean setChameleonPreset(int id, Preset preset, boolean rebuild) {
        // check not travelling
        TardisTrackerInstanceKeeper keeper = TardisPlugin.plugin.getTrackerKeeper();
        if (keeper.getDematerialising().contains(id) || keeper.getMaterialising().contains(id) || keeper.getDestinationVortex().containsKey(id) || keeper.getInVortex().contains(id)) {
            return false;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("chameleon_preset", preset.toString());
        TardisPlugin.plugin.getQueryFactory().doSyncUpdate("tardis", set, where);
        if (rebuild) {
            // rebuild exterior
            HashMap<String, Object> whereCurrentLocation = new HashMap<>();
            whereCurrentLocation.put("tardis_id", id);
            ResultSetCurrentLocation resultSetCurrentLocation = new ResultSetCurrentLocation(TardisPlugin.plugin, whereCurrentLocation);
            if (!resultSetCurrentLocation.resultSet()) {
                return false;
            }
            HashMap<String, Object> whereTardis = new HashMap<>();
            whereTardis.put("tardis_id", id);
            ResultSetTardis resultSetTardis = new ResultSetTardis(TardisPlugin.plugin, whereTardis, "", false, 2);
            if (resultSetTardis.resultSet()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(resultSetTardis.getTardis().getUuid());
                Location location = new Location(resultSetCurrentLocation.getWorld(), resultSetCurrentLocation.getX(), resultSetCurrentLocation.getY(), resultSetCurrentLocation.getZ());
                BuildData buildData = new BuildData(player.getUniqueId().toString());
                buildData.setDirection(resultSetCurrentLocation.getDirection());
                buildData.setLocation(location);
                buildData.setMalfunction(false);
                buildData.setOutside(false);
                buildData.setPlayer(player);
                buildData.setRebuild(true);
                buildData.setSubmarine(false);
                buildData.setTardisId(id);
                buildData.setThrottle(SpaceTimeThrottle.REBUILD);
                TardisPlugin.plugin.getPresetBuilder().buildPreset(buildData);
                TardisPlugin.plugin.getTrackerKeeper().getInVortex().add(id);
                HashMap<String, Object> whereHidden = new HashMap<>();
                whereHidden.put("tardis_id", id);
                HashMap<String, Object> setHidden = new HashMap<>();
                setHidden.put("hidden", 0);
                TardisPlugin.plugin.getQueryFactory().doUpdate("tardis", setHidden, whereHidden);
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setChameleonPreset(UUID uuid, Preset preset, boolean rebuild) {
        // get tardis_id
        ResultSetTardisId resultSetTardisId = new ResultSetTardisId(TardisPlugin.plugin);
        if (resultSetTardisId.fromUuid(uuid.toString())) {
            return setChameleonPreset(resultSetTardisId.getTardisId(), preset, rebuild);
        } else {
            return false;
        }
    }

    @Override
    public boolean setChameleonPreset(Player player, Preset preset, boolean rebuild) {
        return setChameleonPreset(player.getUniqueId(), preset, rebuild);
    }

    @Override
    public void spawnAbandonedTardis(Location location, String type, Preset preset, CardinalDirection direction) throws TardisException {
        if (!Consoles.getBY_NAMES().containsKey(type.toUpperCase(Locale.ENGLISH))) {
            throw new TardisException("Not a valid Console type");
        }
        if (!TardisPlugin.plugin.getConfig().getBoolean("abandon.enabled")) {
            throw new TardisException("Abandoned TARDISes are not allowed on this server");
        }
        if (!TardisPlugin.plugin.getConfig().getBoolean("creation.default_world")) {
            throw new TardisException("TARDIS must be configured to create TARDISes in a default world");
        }
        Schematic schematic = Consoles.getBY_NAMES().get(type.toUpperCase(Locale.ENGLISH));
        new TardisAbandoned(TardisPlugin.plugin).spawn(location, schematic, preset, direction, null);
    }

    @Override
    public void spawnAbandonedTardis(Location location) {
        try {
            spawnAbandonedTardis(location, "BUDGET", Preset.FACTORY, CardinalDirection.SOUTH);
        } catch (TardisException tardisException) {
            Bukkit.getLogger().log(Level.SEVERE, null, tardisException);
        }
    }

    @Override
    public String setDesktopWallAndFloor(int id, String wall, String floor, boolean artron) {
        // get uuid
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis resultSetTardis = new ResultSetTardis(TardisPlugin.plugin, where, "", false, 2);
        if (resultSetTardis.resultSet()) {
            try {
                return setDesktopWallAndFloor(resultSetTardis.getTardis().getUuid(), wall, floor, artron);
            } catch (TardisException tardisException) {
                Bukkit.getLogger().log(Level.SEVERE, null, tardisException);
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public String setDesktopWallAndFloor(UUID uuid, String wall, String floor, boolean artron) throws TardisException {
        Material wallMaterial = Material.getMaterial(wall);
        Material floorMaterial = Material.getMaterial(floor);
        if (!TardisWalls.BLOCKS.contains(wallMaterial)) {
            throw new TardisException("Not a valid wall type");
        }
        if (!TardisWalls.BLOCKS.contains(floorMaterial)) {
            throw new TardisException("Not a valid wall type");
        }
        // get current Schematic
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis resultSetTardis = new ResultSetTardis(TardisPlugin.plugin, where, "", false, 2);
        if (resultSetTardis.resultSet()) {
            Schematic currentConsole = resultSetTardis.getTardis().getSchematic();
            TardisUpgradeData tardisUpgradeData = new TardisUpgradeData();
            tardisUpgradeData.setSchematic(currentConsole);
            tardisUpgradeData.setPrevious(currentConsole);
            tardisUpgradeData.setLevel(resultSetTardis.getTardis().getArtronLevel());
            tardisUpgradeData.setWall(wall);
            tardisUpgradeData.setFloor(floor);
            // change the wall and floor
            TardisWallFloorRunnable tardisWallFloorRunnable = new TardisWallFloorRunnable(TardisPlugin.plugin, uuid, tardisUpgradeData);
            long delay = Math.round(20 / TardisPlugin.plugin.getConfig().getDouble("growth.room_speed"));
            int task = TardisPlugin.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(TardisPlugin.plugin, tardisWallFloorRunnable, 5L, delay);
            tardisWallFloorRunnable.setTaskID(task);
            ResultSetPlayerPrefs resultSetPlayerPrefs = new ResultSetPlayerPrefs(TardisPlugin.plugin, uuid.toString());
            if (resultSetPlayerPrefs.resultSet()) {
                return resultSetPlayerPrefs.getWall() + "," + resultSetPlayerPrefs.getFloor();
            } else {
                return "ORANGE_WOOL,LIGHT_GRAY_WOOL";
            }
        } else {
            return "";
        }
    }
}
