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

import me.eccentric_nz.tardis.blueprints.BlueprintType;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.travel.TardisPluginRespect;
import me.eccentric_nz.tardis.utility.TardisLocationGetters;
import me.eccentric_nz.tardis.utility.TardisUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public interface TardisApi {

    /**
     * Fetches a map of tardis owners and ids.
     *
     * @return a map of tardis owner names and tardis ids
     */
    HashMap<String, Integer> getTimelordMap();

    /**
     * Retrieves a tardis's current location.
     *
     * @param id the tardis id to retrieve the location for
     * @return the current tardis location or null if not found
     */
    Location getTARDISCurrentLocation(int id);

    /**
     * Retrieves a tardis's current location.
     *
     * @param p the Time Lord of the tardis to retrieve the location for
     * @return the current tardis location or null if not found
     */
    Location getTARDISCurrentLocation(Player p);

    /**
     * Retrieves a tardis's current location.
     *
     * @param uuid the UUID of the tardis' Time Lord to retrieve the location for
     * @return the current tardis location or null if not found
     */
    Location getTARDISCurrentLocation(UUID uuid);

    /**
     * Retrieves a tardis's next location.
     *
     * @param id the tardis id to retrieve the location for
     * @return the current tardis location or null if not found
     */
    Location getTARDISNextLocation(int id);

    /**
     * Retries the Current tardis location, chameleon preset, powered and siege status for the Dynmap-tardis plugin.
     *
     * @param id the tardis id to retrieve the data for
     * @return the current tardis data or null if not found
     */
    TardisData getTARDISMapData(int id);

    /**
     * Get a random location from a provided list of worlds in the specified environment. If environment is null, then
     * it will choose a random one.
     *
     * @param worlds      a List of world names to search
     * @param environment the world type to search
     * @param params      a Parameters object determining what flags to check for when getting the Location
     * @return a random Location or null if one could not be found
     */
    Location getRandomLocation(List<String> worlds, World.Environment environment, Parameters params);

    /**
     * Get a random location from a provided list of worlds in the specified environment. If environment is null, then
     * it will choose a random one.
     *
     * @param worlds      a List of world names to search
     * @param environment the world type to search
     * @param p           the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomLocation(List<String> worlds, World.Environment environment, Player p);

    /**
     * Get a random location from a provided list of worlds in a random environment.
     *
     * @param worlds a List of world names to search
     * @param p      the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomLocation(List<String> worlds, Player p);

    /**
     * Get a random OVERWORLD location from a random world.
     *
     * @param p the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomOverworldLocation(Player p);

    /**
     * Get a random OVERWORLD location from a specific world.
     *
     * @param world the world to search
     * @param p     the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomOverworldLocation(String world, Player p);

    /**
     * Get a random OVERWORLD location from a random world.
     *
     * @param p the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomNetherLocation(Player p);

    /**
     * Get a random NETHER location from a specific world.
     *
     * @param world the world to search
     * @param p     the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomNetherLocation(String world, Player p);

    /**
     * Get a random THE_END location from a random world.
     *
     * @param p the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomEndLocation(Player p);

    /**
     * Get a random THE_END location from a specific world.
     *
     * @param world the world to search
     * @param p     the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomEndLocation(String world, Player p);

    /**
     * Get a string list of tardis enabled worlds on the server.
     *
     * @return a list of worlds
     */
    List<String> getWorlds();

    /**
     * Get a string list of tardis enabled overworlds on the server.
     *
     * @return a list of worlds
     */
    List<String> getOverWorlds();

    /**
     * Get the tardis a player is in.
     *
     * @param p the player to query
     * @return a string containing the Time Lord (player name) of the tardis the player is in
     */
    String getTARDISPlayerIsIn(Player p);

    /**
     * Get the tardis a player is in.
     *
     * @param uuid the UUID of the player
     * @return a string containing the Time Lord (player name) of the tardis the player is in
     */
    String getTARDISPlayerIsIn(UUID uuid);

    /**
     * Get the id of the tardis a player is in.
     *
     * @param p the player to query
     * @return the id of the tardis the player is in
     */
    int getIdOfTARDISPlayerIsIn(Player p);

    /**
     * Get the id of the tardis a player is in.
     *
     * @param uuid the UUID of the player
     * @return the id of the tardis the player is in
     */
    int getIdOfTARDISPlayerIsIn(UUID uuid);

    /**
     * Get the players in a tardis.
     *
     * @param id the tardis_id of the tardis
     * @return a List of player names
     */
    List<String> getPlayersInTARDIS(int id);

    /**
     * Get the players in a tardis.
     *
     * @param p the Player who is the tardis' Time Lord
     * @return a List of (online) player names
     */
    List<String> getPlayersInTARDIS(Player p);

    /**
     * Get the players in a tardis.
     *
     * @param uuid the UUID of the tardis' Time Lord
     * @return a List of (online) player names
     */
    List<String> getPlayersInTARDIS(UUID uuid);

    /**
     * Get the companions of a tardis.
     *
     * @param id the tardis_id of the tardis
     * @return a List of companion names
     */
    List<String> getTARDISCompanions(int id);

    /**
     * Get the companions of a tardis.
     *
     * @param p the Player who is the tardis' Time Lord
     * @return a List of (online) companion names
     */
    List<String> getTARDISCompanions(Player p);

    /**
     * Get the companions of a tardis.
     *
     * @param uuid the UUID of the tardis' Time Lord
     * @return a List of (online) companion names
     */
    List<String> getTARDISCompanions(UUID uuid);

    /**
     * Get whether a player is in a Zero room.
     *
     * @param p the player to query
     * @return true if the player is in the Zero room, otherwise false
     */
    boolean isPlayerInZeroRoom(Player p);

    /**
     * Get whether a player is in a Zero room.
     *
     * @param uuid the UUID of the player to query
     * @return true if the player is in the Zero room, otherwise false
     */
    boolean isPlayerInZeroRoom(UUID uuid);

    /**
     * Get whether a player is currently genetically modified.
     *
     * @param p the player to query
     * @return true if the player is in the Zero room, otherwise false
     */
    boolean isPlayerGeneticallyModified(Player p);

    /**
     * Get whether a player is currently genetically modified.
     *
     * @param uuid the UUID of the player to query
     * @return true if the player is in the Zero room, otherwise false
     */
    boolean isPlayerGeneticallyModified(UUID uuid);

    /**
     * Get a handle for the tardis utilities.
     *
     * @return the tardis Utilities instance
     */
    TardisUtils getUtils();

    /**
     * Get a handle for the tardis utilities.
     *
     * @return the tardis Utilities instance
     */
    TardisLocationGetters getLocationUtils();

    /**
     * Get a handle for the tardis Plugin Respect.
     *
     * @return the tardis Plugin Respect instance
     */
    TardisPluginRespect getRespect();

    /**
     * Get the tardis shaped recipes.
     *
     * @return a HashMap&lt;String, ShapedRecipe&gt; containing the tardis shaped recipes
     */
    HashMap<String, ShapedRecipe> getShapedRecipes();

    /**
     * Get the tardis shapeless recipes.
     *
     * @return a HashMap&lt;String, ShapelessRecipe&gt; containing the tardis shapeless recipes
     */
    HashMap<String, ShapelessRecipe> getShapelessRecipes();

    /**
     * Get a tardis item
     *
     * @param item   the tardis item to get
     * @param player the player who will be receiving the item
     * @return an ItemStack of the tardis item ot null if an invalid item was specified
     */
    ItemStack getTARDISShapeItem(String item, Player player);

    /**
     * Get the tardis seed recipes.
     *
     * @return a HashMap&lt;Schematic, ShapedRecipe&gt; containing the tardis seed recipes
     */
    HashMap<Schematic, ShapedRecipe> getSeedRecipes();

    /**
     * Get a tardis Seed ItemStack
     *
     * @param schematic the console type to get
     * @return a tardis seed block item or null if an invalid schematic is specified
     */
    ItemStack getTARDISSeedItem(String schematic);

    /**
     * Get the tardis blueprints.
     *
     * @return a list of tardis blueprints
     */
    List<BlueprintType> getBlueprints();

    /**
     * Get a tardis Blueprint Disk
     *
     * @param item   the blueprint disk to get
     * @param player the player who will be receiving the item
     * @return a tardis Blueprint Disk item or null if an invalid schematic is specified
     */
    ItemStack getTARDISBlueprintItem(String item, Player player);

    /**
     * Get the tardis wall and floor block names.
     *
     * @return a String List containing the tardis wall and floor block names
     */
    List<String> getWallFloorBlocks();

    /**
     * Set a tardis's next destination.
     *
     * @param id       the tardis id to set the destination for
     * @param location the next location to travel to
     * @param travel   whether the tardis should travel to the destination
     * @return true if the destination was set successfully
     */
    boolean setDestination(int id, Location location, boolean travel);

    /**
     * Convenience method to set a tardis's next destination.
     *
     * @param uuid     the UUID of the Time Lord (player) to set the destination for
     * @param location the next location to travel to
     * @param travel   whether the tardis should travel to the destination
     * @return true if the destination was set successfully
     */
    boolean setDestination(UUID uuid, Location location, boolean travel);

    /**
     * Convenience method to set a tardis's next destination.
     *
     * @param player   the Time Lord (player) to set the destination for
     * @param location the next location to travel to
     * @param travel   whether the tardis should travel to the destination
     * @return true if the destination was set successfully
     */
    boolean setDestination(Player player, Location location, boolean travel);

    /**
     * Get information from the database for a tardis.
     *
     * @param id the tardis id to get information for
     * @return return a {@link Tardis} data object, or null if no data was found
     */
    Tardis getTardisData(int id);

    /**
     * Convenience method to get information from the database for a tardis.
     *
     * @param uuid the UUID of the Time Lord (player) of the tardis id to get information for
     * @return return a {@link Tardis} data object, or null if no data was found
     */
    Tardis getTardisData(UUID uuid);

    /**
     * Convenience method to get information from the database for a tardis.
     *
     * @param player the Time Lord (player) of the tardis to get information for
     * @return return a {@link Tardis} data object, or null if no data was found
     */
    Tardis getTardisData(Player player);

    /**
     * Set the Chameleon Preset for a tardis.
     *
     * @param id      the tardis id to set the destination for
     * @param preset  the exterior preset to use
     * @param rebuild whether to rebuild the tardis exterior
     * @return true if the preset was set
     */
    boolean setChameleonPreset(int id, Preset preset, boolean rebuild);

    /**
     * Convenience method to set the Chameleon Preset for a tardis.
     *
     * @param uuid    the UUID of the Time Lord (player) of the tardis to set the destination for
     * @param preset  the exterior preset to use
     * @param rebuild whether to rebuild the tardis exterior
     * @return true if the preset was set
     */
    boolean setChameleonPreset(UUID uuid, Preset preset, boolean rebuild);

    /**
     * Convenience method to set the Chameleon Preset for a tardis.
     *
     * @param player  the Time Lord (player) of the tardis to set the destination for
     * @param preset  the exterior preset to use
     * @param rebuild whether to rebuild the tardis exterior
     * @return true if the preset was set
     */
    boolean setChameleonPreset(Player player, Preset preset, boolean rebuild);

    /**
     * Spawn an abandoned tardis at the specified Bukkit Location.
     *
     * @param location  the location to spawn the tardis
     * @param type      the type of interior to build
     * @param preset    the Chameleon preset of the exterior
     * @param direction the direction of the tardis exterior ( this is the direction the player is facing when looking
     *                  at the door)
     * @throws TardisException if the console type is not valid or tardis abandonment is disabled on the server
     */
    void spawnAbandonedTARDIS(Location location, String type, Preset preset, CardinalDirection direction) throws TardisException;

    /**
     * Convenience method to spawn an abandoned tardis at the specified Bukkit Location. The interior will default to
     * BUDGET, the exterior Chameleon Preset to FACTORY and the direction to SOUTH.
     *
     * @param location the location to spawn the tardis
     */
    void spawnAbandonedTARDIS(Location location);

    /**
     * Change the desktop theme of a tardis.
     *
     * @param id     the tardis id to change the desktop for
     * @param wall   the wall block type to change to
     * @param floor  the floor block type to change to
     * @param artron whether to check for and charge Artron Energy
     * @return a comma separated String containing the wall and floor block names (so they can be restored) if the walls
     * and floor were successfully changed, an empty String if not
     */
    String setDesktopWallAndFloor(int id, String wall, String floor, boolean artron);

    /**
     * Convenience method to change the desktop theme of a tardis.
     *
     * @param uuid   the UUID of the Time Lord (player) of the tardis to change the desktop for
     * @param wall   the wall block type to change to
     * @param floor  the floor block type to change to
     * @param artron whether to check for and charge Artron Energy
     * @return a comma separated String containing the wall and floor block names (so they can be restored) if the walls
     * and floor were successfully changed, an empty String if not
     * @throws TardisException if the wall or floor type is not valid
     */
    String setDesktopWallAndFloor(UUID uuid, String wall, String floor, boolean artron) throws TardisException;
}
