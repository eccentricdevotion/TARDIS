/*
 * Copyright (C) 2025 eccentric_nz
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

import me.eccentric_nz.TARDIS.blueprints.BlueprintType;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.travel.TARDISPluginRespect;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISUtils;
import me.eccentric_nz.tardisweepingangels.utils.FollowerChecker;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
public interface TardisAPI {

    /**
     * Fetches a map of TARDIS owners and ids.
     *
     * @return a map of TARDIS owner names and TARDIS ids
     */
    HashMap<String, Integer> getTimelordMap();

    /**
     * Retrieves a TARDIS's current location.
     *
     * @param id the TARDIS id to retrieve the location for
     * @return the current TARDIS location or null if not found
     */
    Location getTARDISCurrentLocation(int id);

    /**
     * Retrieves a TARDIS's current location.
     *
     * @param p the Time Lord of the TARDIS to retrieve the location for
     * @return the current TARDIS location or null if not found
     */
    Location getTARDISCurrentLocation(Player p);

    /**
     * Retrieves a TARDIS's current location.
     *
     * @param uuid the UUID of the TARDIS' Time Lord to retrieve the location
     * for
     * @return the current TARDIS location or null if not found
     */
    Location getTARDISCurrentLocation(UUID uuid);

    /**
     * Retrieves a TARDIS's next location.
     *
     * @param id the TARDIS id to retrieve the location for
     * @return the current TARDIS location or null if not found
     */
    Location getTARDISNextLocation(int id);

    /**
     * Retrieves the Current TARDIS location, chameleon preset, powered and siege
     * status for the TARDIS mapping provider plugin.
     *
     * @param id the TARDIS id to retrieve the data for
     * @return the current TARDIS data or null if not found
     */
    TARDISData getTARDISMapData(int id);

    /**
     * Get a random location from a provided list of worlds in the specified
     * environment. If environment is null, then it will choose a random one.
     *
     * @param worlds a List of world names to search
     * @param environment the world type to search
     * @param params a Parameters object determining what flags to check for
     * when getting the Location
     * @return a random Location or null if one could not be found
     */
    Location getRandomLocation(List<String> worlds, World.Environment environment, Parameters params);

    /**
     * Get a random location from a provided list of worlds in the specified
     * environment. If environment is null, then it will choose a random one.
     *
     * @param worlds a List of world names to search
     * @param environment the world type to search
     * @param p the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomLocation(List<String> worlds, World.Environment environment, Player p);

    /**
     * Get a random location from a provided list of worlds in a random
     * environment.
     *
     * @param worlds a List of world names to search
     * @param p the player to get the location for
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
     * @param p the player to get the location for
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
     * @param p the player to get the location for
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
     * @param p the player to get the location for
     * @return a random Location or null if one could not be found
     */
    Location getRandomEndLocation(String world, Player p);

    /**
     * Get a string list of TARDIS enabled worlds on the server.
     *
     * @return a list of worlds
     */
    List<String> getWorlds();

    /**
     * Get a string list of TARDIS enabled overworlds on the server.
     *
     * @return a list of worlds
     */
    List<String> getOverWorlds();

    /**
     * Get the TARDIS a player is in.
     *
     * @param p the player to query
     * @return a string containing the Time Lord (player name) of the TARDIS the
     * player is in
     */
    String getTARDISPlayerIsIn(Player p);

    /**
     * Get the TARDIS a player is in.
     *
     * @param uuid the UUID of the player
     * @return a string containing the Time Lord (player name) of the TARDIS the
     * player is in
     */
    String getTARDISPlayerIsIn(UUID uuid);

    /**
     * Get the id of the TARDIS a player is in.
     *
     * @param p the player to query
     * @return the id of the TARDIS the player is in
     */
    int getIdOfTARDISPlayerIsIn(Player p);

    /**
     * Get the id of the TARDIS a player is in.
     *
     * @param uuid the UUID of the player
     * @return the id of the TARDIS the player is in
     */
    int getIdOfTARDISPlayerIsIn(UUID uuid);

    /**
     * Get the players in a TARDIS.
     *
     * @param id the tardis_id of the TARDIS
     * @return a List of player names
     */
    List<String> getPlayersInTARDIS(int id);

    /**
     * Get the players in a TARDIS.
     *
     * @param p the Player who is the TARDIS' Time Lord
     * @return a List of (online) player names
     */
    List<String> getPlayersInTARDIS(Player p);

    /**
     * Get the players in a TARDIS.
     *
     * @param uuid the UUID of the TARDIS' Time Lord
     * @return a List of (online) player names
     */
    List<String> getPlayersInTARDIS(UUID uuid);

    /**
     * Get the companions of a TARDIS.
     *
     * @param id the tardis_id of the TARDIS
     * @return a List of companion names
     */
    List<String> getTARDISCompanions(int id);

    /**
     * Get the companions of a TARDIS.
     *
     * @param p the Player who is the TARDIS' Time Lord
     * @return a List of (online) companion names
     */
    List<String> getTARDISCompanions(Player p);

    /**
     * Get the companions of a TARDIS.
     *
     * @param uuid the UUID of the TARDIS' Time Lord
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
     * Get a handle for the TARDIS utilities.
     *
     * @return the TARDIS Utilities instance
     */
    TARDISUtils getUtils();

    /**
     * Get a handle for the TARDIS utilities.
     *
     * @return the TARDIS Utilities instance
     */
    TARDISLocationGetters getLocationUtils();

    /**
     * Get a handle for the TARDIS Plugin Respect.
     *
     * @return the TARDIS Plugin Respect instance
     */
    TARDISPluginRespect getRespect();

    /**
     * Get the TARDIS shaped recipes.
     *
     * @return a HashMap&lt;String, ShapedRecipe&gt; containing the TARDIS
     * shaped recipes
     */
    HashMap<String, ShapedRecipe> getShapedRecipes();

    /**
     * Get the TARDIS shapeless recipes.
     *
     * @return a HashMap&lt;String, ShapelessRecipe&gt; containing the TARDIS
     * shapeless recipes
     */
    HashMap<String, ShapelessRecipe> getShapelessRecipes();

    /**
     * Get a TARDIS item
     *
     * @param item the TARDIS item to get
     * @param player the player who will be receiving the item
     * @return an ItemStack of the TARDIS item ot null if an invalid item was
     * specified
     */
    ItemStack getTARDISShapeItem(String item, Player player);

    /**
     * Get the TARDIS seed recipes.
     *
     * @return a HashMap&lt;Schematic, ShapedRecipe&gt; containing the TARDIS
     * seed recipes
     */
    HashMap<Schematic, ShapedRecipe> getSeedRecipes();

    /**
     * Get a TARDIS Seed ItemStack
     *
     * @param schematic the console type to get
     * @return a TARDIS seed block item or null if an invalid schematic is
     * specified
     */
    ItemStack getTARDISSeedItem(String schematic);

    /**
     * Get the TARDIS blueprints.
     *
     * @return a list of TARDIS blueprints
     */
    List<BlueprintType> getBlueprints();

    /**
     * Get a TARDIS Blueprint Disk
     *
     * @param item the blueprint disk to get
     * @param player the player who will be receiving the item
     * @return a TARDIS Blueprint Disk item or null if an invalid schematic is
     * specified
     */
    ItemStack getTARDISBlueprintItem(String item, Player player);

    /**
     * Get the TARDIS wall and floor block names.
     *
     * @return a String List containing the TARDIS wall and floor block names
     */
    List<String> getWallFloorBlocks();

    /**
     * Set a TARDIS's next destination.
     *
     * @param id the TARDIS id to set the destination for
     * @param location the next location to travel to
     * @param travel whether the TARDIS should travel to the destination
     * @return true if the destination was set successfully
     */
    boolean setDestination(int id, Location location, boolean travel);

    /**
     * Convenience method to set a TARDIS's next destination.
     *
     * @param uuid the UUID of the Time Lord (player) to set the destination for
     * @param location the next location to travel to
     * @param travel whether the TARDIS should travel to the destination
     * @return true if the destination was set successfully
     */
    boolean setDestination(UUID uuid, Location location, boolean travel);

    /**
     * Convenience method to set a TARDIS's next destination.
     *
     * @param player the Time Lord (player) to set the destination for
     * @param location the next location to travel to
     * @param travel whether the TARDIS should travel to the destination
     * @return true if the destination was set successfully
     */
    boolean setDestination(Player player, Location location, boolean travel);

    /**
     * Get information from the database for a TARDIS.
     *
     * @param id the TARDIS id to get information for
     * @return return a {@link Tardis} data object, or null if no data was found
     */
    Tardis getTardisData(int id);

    /**
     * Convenience method to get information from the database for a TARDIS.
     *
     * @param uuid the UUID of the Time Lord (player) of the TARDIS id to get
     * information for
     * @return return a {@link Tardis} data object, or null if no data was found
     */
    Tardis getTardisData(UUID uuid);

    /**
     * Convenience method to get information from the database for a TARDIS.
     *
     * @param player the Time Lord (player) of the TARDIS to get information for
     * @return return a {@link Tardis} data object, or null if no data was found
     */
    Tardis getTardisData(Player player);

    /**
     * Set the Chameleon Preset for a TARDIS.
     *
     * @param id the TARDIS id to set the destination for
     * @param preset the exterior preset to use
     * @param rebuild whether to rebuild the TARDIS exterior
     * @return true if the preset was set
     */
    boolean setChameleonPreset(int id, ChameleonPreset preset, boolean rebuild);

    /**
     * Convenience method to set the Chameleon Preset for a TARDIS.
     *
     * @param uuid the UUID of the Time Lord (player) of the TARDIS to set the
     * destination for
     * @param preset the exterior preset to use
     * @param rebuild whether to rebuild the TARDIS exterior
     * @return true if the preset was set
     */
    boolean setChameleonPreset(UUID uuid, ChameleonPreset preset, boolean rebuild);

    /**
     * Convenience method to set the Chameleon Preset for a TARDIS.
     *
     * @param player the Time Lord (player) of the TARDIS to set the destination
     * for
     * @param preset the exterior preset to use
     * @param rebuild whether to rebuild the TARDIS exterior
     * @return true if the preset was set
     */
    boolean setChameleonPreset(Player player, ChameleonPreset preset, boolean rebuild);

    /**
     * Spawn an abandoned TARDIS at the specified Bukkit Location.
     *
     * @param location the location to spawn the TARDIS
     * @param type the type of interior to build
     * @param preset the Chameleon preset of the exterior
     * @param direction the direction of the TARDIS exterior ( this is the
     * direction the player is facing when looking at the door)
     * @throws TARDISException if the console type is not valid or TARDIS
     * abandonment is disabled on the server
     */
    void spawnAbandonedTARDIS(Location location, String type, ChameleonPreset preset, COMPASS direction) throws TARDISException;

    /**
     * Convenience method to spawn an abandoned TARDIS at the specified Bukkit
     * Location. The interior will default to BUDGET, the exterior Chameleon
     * Preset to FACTORY and the direction to SOUTH.
     *
     * @param location the location to spawn the TARDIS
     */
    void spawnAbandonedTARDIS(Location location);

    /**
     * Change the desktop theme of a TARDIS.
     *
     * @param id the TARDIS id to change the desktop for
     * @param wall the wall block type to change to
     * @param floor the floor block type to change to
     * @param artron whether to check for and charge Artron Energy
     * @return a comma separated String containing the wall and floor block
     * names (so they can be restored) if the walls and floor were successfully
     * changed, an empty String if not
     */
    String setDesktopWallAndFloor(int id, String wall, String floor, boolean artron);

    /**
     * Convenience method to change the desktop theme of a TARDIS.
     *
     * @param uuid the UUID of the Time Lord (player) of the TARDIS to change
     * the desktop for
     * @param wall the wall block type to change to
     * @param floor the floor block type to change to
     * @param artron whether to check for and charge Artron Energy
     * @return a comma separated String containing the wall and floor block
     * names (so they can be restored) if the walls and floor were successfully
     * changed, an empty String if not
     * @throws TARDISException if the wall or floor type is not valid
     */
    String setDesktopWallAndFloor(UUID uuid, String wall, String floor, boolean artron) throws TARDISException;

    /**
     * Add a recipe to TARDIS' shaped recipe list
     *
     * @param key the name of the recipe result
     * @param recipe the recipe to add
     */
    void addShapedRecipe(String key, ShapedRecipe recipe);

    /**
     * Add a recipe to TARDIS' shapeless recipe list
     *
     * @param key the name of the recipe result
     * @param recipe the recipe to add
     */
    void addShapelessRecipe(String key, ShapelessRecipe recipe);

    /*
    Weeping Angels
     */
    /**
     * Sets an entity as a Weeping Angel.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setAngelEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as an Ice Warrior.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setWarriorEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Cyberman.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setCyberEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Dalek.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setDalekEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as Dalek Sec.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setDalekSecEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as Davros.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setDavrosEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as an Empty Child.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setEmptyChildEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Hath.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setHathEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Headless Monk.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setHeadlessMonkEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Heavenly Host.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setHeavenlyHostEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Mire.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setMireEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Sea Devil.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setSeaDevilEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Slitheen.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setSlitheenEquipment(LivingEntity le, boolean disguise);

    /**
     * Disguises an entity as a Judoon.
     *
     * @param entity The entity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setJudoonEquipment(LivingEntity entity, boolean disguise);

    /**
     * Sets an armour stand, or disguises a player as K9.
     *
     * @param player The player that will own this K9 - may be null
     * @param armorStand The armour stand or player to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setK9Equipment(Player player, Entity armorStand, boolean disguise);

    /**
     * Sets an armour stand, or disguises a player as an Ood.
     *
     * @param entity The armour stand or player to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setOodEquipment(LivingEntity entity, boolean disguise);

    /**
     * Sets an entity as a Racnoss.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setRacnossEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Silent.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setSilentEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Silurian.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setSilurianEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Sontaran.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setSontaranEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as Strax (a Sontaran butler).
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setStraxEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an armour stand, or disguises a player as a Toclafane.
     *
     * @param armorStand The armour stand to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setToclafaneEquipment(Entity armorStand, boolean disguise);

    /**
     * Sets an entity as a Vashta Nerada.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setVashtaNeradaEquipment(LivingEntity le, boolean disguise);

    /**
     * Sets an entity as a Zygon.
     *
     * @param le The LivingEntity to disguise
     * @param disguise A boolean to determine if this is a player disguise
     */
    void setZygonEquipment(LivingEntity le, boolean disguise);

    /**
     * Removes a disguise from a Player.
     *
     * @param p The Player to un-disguise
     */
    void removeEquipment(Player p);

    /**
     * Returns whether an entity is a TARDISWeepingAngels entity.
     *
     * @param entity the entity to check
     * @return true if the entity is a TARDISWeepingAngels entity
     */
    boolean isWeepingAngelMonster(Entity entity);

    /**
     * Returns the Monster type for a TARDISWeepingAngels entity.
     *
     * @param entity the entity to get the Monster type for
     * @return the Monster type
     */
    Monster getWeepingAngelMonsterType(Entity entity);

    /**
     * Returns whether the specified entity is a claimed TARDISWeepingAngels
     * monster.
     *
     * @param entity the entity to check
     * @param uuid the UUID of the claiming player
     * @return a FollowerChecker containing the type of TARDISWeepingAngels
     * monster (JUDOON, K9, OOD) - if the monster is not claimable it will
     * return WEEPING_ANGEL - and an integer from its persistent data container
     */
    FollowerChecker isClaimedMonster(Entity entity, UUID uuid);

    /**
     * Start a following task for a claimed monster
     *
     * @param husk the entity that will follow the player
     * @param player the player that owns this Judoon / Ood / K9
     */
    void setFollowing(Entity husk, Player player);

    /**
     * Get a TARDISWeepingAngels monster head
     *
     * @param monster the type of monster head to get
     * @return a monster head itemstack
     */
    ItemStack getHead(Monster monster);

    /**
     * Get a K9 item
     *
     * @return a K9 itemstack
     */
    ItemStack getK9();
}
