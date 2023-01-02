/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.arch.TARDISWatchData;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.desktop.TARDISUpgradeData;
import me.eccentric_nz.TARDIS.display.TARDISDisplayType;
import me.eccentric_nz.TARDIS.enumeration.Bind;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import me.eccentric_nz.TARDIS.flight.TARDISRegulatorRunnable;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.move.TARDISMoveSession;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.portal.CastData;
import me.eccentric_nz.TARDIS.rooms.TARDISRoomData;
import me.eccentric_nz.TARDIS.rooms.TARDISSeedData;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeArea;
import me.eccentric_nz.TARDIS.travel.ComehereRequest;
import me.eccentric_nz.TARDIS.travel.TravelCostAndType;
import me.eccentric_nz.TARDIS.universaltranslator.TranslateData;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A central repository used to store various data values required to track what Time lords and TARDIS are doing
 * in-game, and provide easy access to the data in other classes. For example the spectacleWearers List tracks which
 * Time Lords are currently wearing 3d_glasses.
 *
 * @author eccentric_nz
 */
public class TARDISTrackerInstanceKeeper {

    private final HashMap<UUID, CastData> casters = new HashMap<>();
    private final HashMap<UUID, Set<Block>> castRestore = new HashMap<>();
    private final HashMap<UUID, Integer> rotorRestore = new HashMap<>();
    private final HashMap<Integer, Boolean> malfunction = new HashMap<>();
    private final HashMap<Integer, Integer> cloisterBells = new HashMap<>();
    private final HashMap<Integer, Integer> hadsDamage = new HashMap<>();
    private final HashMap<Integer, Integer> destinationVortex = new HashMap<>();
    private final HashMap<Integer, TravelCostAndType> hasDestination = new HashMap<>();
    private final HashMap<Integer, String> renderer = new HashMap<>();
    private final HashMap<Integer, TARDISAntiBuild> antiBuild = new HashMap<>();
    private final HashMap<Integer, TARDISRoomData> roomTasks = new HashMap<>();
    private final HashMap<Integer, UUID> rescue = new HashMap<>();
    private final HashMap<Location, TARDISTeleportLocation> portals = new HashMap<>();
    private final HashMap<String, List<TARDISSiegeArea>> siegeBreedingAreas = new HashMap<>();
    private final HashMap<String, List<TARDISSiegeArea>> siegeGrowthAreas = new HashMap<>();
    private final HashMap<String, Sign> sign = new HashMap<>();
    private final HashMap<UUID, Bind> bindRemoval = new HashMap<>();
    private final HashMap<UUID, Block> invisibleDoors = new HashMap<>();
    private final HashMap<UUID, Block> lazarus = new HashMap<>();
    private final HashMap<UUID, BuildData> flightData = new HashMap<>();
    private final HashMap<UUID, ComehereRequest> comehereRequests = new HashMap<>();
    private final HashMap<UUID, Double[]> gravity = new HashMap<>();
    private final HashMap<UUID, Integer> binder = new HashMap<>();
    private final HashMap<UUID, Integer> count = new HashMap<>();
    private final HashMap<UUID, Integer> ejecting = new HashMap<>();
    private final HashMap<UUID, Integer> junkPlayers = new HashMap<>();
    private final HashMap<UUID, Integer> secondaryRemovers = new HashMap<>();
    private final HashMap<UUID, Integer> siegeCarrying = new HashMap<>();
    private final HashMap<UUID, JsonObject> pastes = new HashMap<>();
    private final HashMap<UUID, List<Location>> repeaters = new HashMap<>();
    private final HashMap<UUID, List<UUID>> renderedNPCs = new HashMap<>();
    private final HashMap<UUID, Location> activeForceFields = new HashMap<>();
    private final HashMap<UUID, Location> dispersed = new HashMap<>();
    private final HashMap<UUID, Location> endLocation = new HashMap<>();
    private final HashMap<UUID, Location> junkRelog = new HashMap<>();
    private final HashMap<UUID, Location> sonicGenerators = new HashMap<>();
    private final HashMap<UUID, Location> startLocation = new HashMap<>();
    private final HashMap<UUID, Long> cooldown = new HashMap<>();
    private final HashMap<UUID, Long> hideCooldown = new HashMap<>();
    private final HashMap<UUID, Long> rebuildCooldown = new HashMap<>();
    private final HashMap<UUID, Long> setTime = new HashMap<>();
    private final HashMap<UUID, String> area = new HashMap<>();
    private final HashMap<UUID, String> areaStartBlock = new HashMap<>();
    private final HashMap<UUID, String> areaEndBlock = new HashMap<>();
    private final HashMap<UUID, String> flight = new HashMap<>();
    private final HashMap<UUID, String> jettison = new HashMap<>();
    private final HashMap<UUID, String> perm = new HashMap<>();
    private final HashMap<UUID, String> updatePlayers = new HashMap<>();
    private final HashMap<UUID, String> preset = new HashMap<>();
    private final HashMap<UUID, TranslateData> translators = new HashMap<>();
    private final HashMap<UUID, String> telepathicPlacements = new HashMap<>();
    private final HashMap<UUID, TARDISDisplayType> display = new HashMap<>();
    private final HashMap<UUID, TARDISInfoMenu> infoMenu = new HashMap<>();
    private final HashMap<UUID, TARDISMoveSession> moveSessions = new HashMap<>();
    private final HashMap<UUID, TARDISRegulatorRunnable> regulating = new HashMap<>();
    private final HashMap<UUID, TARDISSeedData> roomSeed = new HashMap<>();
    private final HashMap<UUID, TARDISUpgradeData> upgrades = new HashMap<>();
    private final HashMap<UUID, TARDISWatchData> johnSmith = new HashMap<>();
    private final HashMap<UUID, Updateable> secondary = new HashMap<>();
    private final HashMap<UUID, UUID> chatRescue = new HashMap<>();
    private final HashMap<UUID, UUID> telepathicRescue = new HashMap<>();
    private final HashMap<UUID, UUID> telepaths = new HashMap<>();
    private final Set<String> artronFurnaces = new HashSet<>();
    private final Set<String> heatBlocks = new HashSet<>();
    private final Set<String> resetWorlds = new HashSet<>();
    private final Set<Integer> dematerialising = new HashSet<>();
    private final Set<Integer> didDematToVortex = new HashSet<>();
    private final Set<Integer> dispersedTARDII = new HashSet<>();
    private final Set<Integer> hasClickedHandbrake = new HashSet<>();
    private final Set<Integer> hasNotClickedHandbrake = new HashSet<>();
    private final Set<Integer> hasRandomised = new HashSet<>();
    private final Set<Integer> inSiegeMode = new HashSet<>();
    private final Set<Integer> inVortex = new HashSet<>();
    private final Set<Integer> isSiegeCube = new HashSet<>();
    private final Set<Integer> materialising = new HashSet<>();
    private final Set<Integer> minecart = new HashSet<>();
    private final Set<Integer> submarine = new HashSet<>();
    private final Set<Integer> isGrowingRooms = new HashSet<>();
    private final Set<UUID> arrangers = new HashSet<>();
    private final Set<UUID> beaconColouring = new HashSet<>();
    private final Set<UUID> constructors = new HashSet<>();
    private final Set<UUID> eggs = new HashSet<>();
    private final Set<UUID> excitation = new HashSet<>();
    private final Set<UUID> farming = new HashSet<>();
    private final Set<UUID> frozenPlayers = new HashSet<>();
    private final Set<UUID> geneticallyModified = new HashSet<>();
    private final Set<UUID> geneticManipulation = new HashSet<>();
    private final Set<UUID> hasTravelled = new HashSet<>();
    private final Set<UUID> howTo = new HashSet<>();
    private final Set<UUID> mover = new HashSet<>();
    private final Set<UUID> recipeViewers = new HashSet<>();
    private final Set<UUID> renderRoomOccupants = new HashSet<>();
    private final Set<UUID> sonicDoors = new HashSet<>();
    private final Set<UUID> spectacleWearers = new HashSet<>();
    private final Set<UUID> temporallyLocated = new HashSet<>();
    private final Set<UUID> zeroRoomOccupants = new HashSet<>();
    private String immortalityGate = "";

    /**
     * Gets the Move Session for a player, this is used to see if they have actually moved
     *
     * @param p the player to track
     * @return the session for the player
     */
    public TARDISMoveSession getTARDISMoveSession(Player p) {
        if (moveSessions.containsKey(p.getUniqueId())) {
            return moveSessions.get(p.getUniqueId());
        }
        TARDISMoveSession session = new TARDISMoveSession(p);
        moveSessions.put(p.getUniqueId(), session);
        return session;
    }

    /**
     * Gets a map of uuids with open door portal locations.
     *
     * @return a map of uuids with locations
     */
    public HashMap<UUID, CastData> getCasters() {
        return casters;
    }

    /**
     * Gets a map of uuids with Blocks to restore for the open door portal interior projection.
     *
     * @return a map of uuids with Blocks to restore
     */
    public HashMap<UUID, Set<Block>> getCastRestore() {
        return castRestore;
    }

    /**
     * Gets a map of player uuids with item frame ids to remove from the open door portal interior projection.
     *
     * @return a map of uuids with ids
     */
    public HashMap<UUID, Integer> getRotorRestore() {
        return rotorRestore;
    }

    /**
     * Tracks who is using the the immortality gate
     *
     * @return the name of the player using the immortality gate or an empty string if not in use
     */
    public String getImmortalityGate() {
        return immortalityGate;
    }

    /**
     * Sets who is using the immortality gate
     *
     * @param immortalityGate the name of the player using the immortality gate
     */
    public void setImmortalityGate(String immortalityGate) {
        this.immortalityGate = immortalityGate;
    }

    /**
     * Track whether a TARDIS is malfunctioning
     *
     * @return a Map of TARDIS ids and whether a malfunction has occurred
     */
    public HashMap<Integer, Boolean> getMalfunction() {
        return malfunction;
    }

    /**
     * Track whether a TARDIS is running a cloister bell task
     *
     * @return a Map of TARDIS ids and the Bukkit task id
     */
    public HashMap<Integer, Integer> getCloisterBells() {
        return cloisterBells;
    }

    /**
     * Track TARDISes that have their HADS circuit engaged
     *
     * @return a Map of TARDIS ids and the number of hits the TARDIS has taken
     */
    public HashMap<Integer, Integer> getHadsDamage() {
        return hadsDamage;
    }

    /**
     * Track TARDISes floating in the vortex
     *
     * @return a Map of TARDIS ids and the Bukkit task id of the repeating SFX
     */
    public HashMap<Integer, Integer> getDestinationVortex() {
        return destinationVortex;
    }

    /**
     * Track whether a TARDIS has a travel destination
     *
     * @return a Map of TARDIS ids and the Artron cost of the travel
     */
    public HashMap<Integer, TravelCostAndType> getHasDestination() {
        return hasDestination;
    }

    /**
     * Tracks whether the Render room has rendered the exterior
     *
     * @return a Map of TARDIS ids and whether the exterior has been rendered
     */
    public HashMap<Integer, String> getRenderer() {
        return renderer;
    }

    /**
     * Tracks whether a TARDIS has interior building disabled
     *
     * @return a Map of TARDIS ids and the {@link TARDISAntiBuild} parameters
     */
    public HashMap<Integer, TARDISAntiBuild> getAntiBuild() {
        return antiBuild;
    }

    /**
     * Tracks room growing tasks, so they can be resumed in the event of a server restart
     *
     * @return a Map of Bukkit task ids and the associated {@link TARDISRoomData}
     */
    public HashMap<Integer, TARDISRoomData> getRoomTasks() {
        return roomTasks;
    }

    /**
     * Tracks TARDISes rescuing players
     *
     * @return a Map of TARDIS ids and the UUID of the player being rescued
     */
    public HashMap<Integer, UUID> getRescue() {
        return rescue;
    }

    /**
     * Tracks open TARDIS portals so players can walk in
     *
     * @return a Map of portal locations and the teleport location data
     */
    public HashMap<Location, TARDISTeleportLocation> getPortals() {
        return portals;
    }

    /**
     * Tracks TARDIS areas that are in siege mode so that animals have a chance of having twins instead of a single birth
     *
     * @return a Map of world names and siege areas
     */
    public HashMap<String, List<TARDISSiegeArea>> getSiegeBreedingAreas() {
        return siegeBreedingAreas;
    }

    /**
     * Tracks TARDIS areas that are in siege mode so that food grows faster
     *
     * @return a Map of world names and siege areas
     */
    public HashMap<String, List<TARDISSiegeArea>> getSiegeGrowthAreas() {
        return siegeGrowthAreas;
    }

    /**
     * Tracks TARDIS keyboard signs so they can be processed for travel commands
     *
     * @return a Map of locations and signs
     */
    public HashMap<String, Sign> getSign() {
        return sign;
    }

    /**
     * Trackers players using the <code>/tardisbind remove</code> command
     *
     * @return a Map of player UUIDs and the command to unbind
     */
    public HashMap<UUID, Bind> getBindRemoval() {
        return bindRemoval;
    }

    /**
     * Tracks players whose TARDIS is invisible and the config option <code>allow.3d_doors</code> is true
     *
     * @return a Map of player UUIDs and the door block of their TARDIS
     */
    public HashMap<UUID, Block> getInvisibleDoors() {
        return invisibleDoors;
    }

    /**
     * Tracks the block that triggered opening the Lazarus Device GUI, the block is used to determine the location of the genetic manipulator walls
     *
     * @return a Map of player UUIDs and the pressure plate block
     */
    public HashMap<UUID, Block> getLazarus() {
        return lazarus;
    }

    /**
     * Tracks players using Manual or Regulator flight modes
     *
     * @return a Map of player UUIDs and the TARDIS {@link BuildData}.
     * The location stored in the BuildData is adjusted depending on how well the player flys their TARDIS
     */
    public HashMap<UUID, BuildData> getFlightData() {
        return flightData;
    }

    /**
     * Tracks players using the <code>/tardis comehere [player]</code> command
     */
    public HashMap<UUID, ComehereRequest> getComehereRequests() {
        return comehereRequests;
    }

    /**
     * Tracks players using the <code>/tardisgravity</code> command
     *
     * @return a Map of player UUIDs and the arguments from the command
     */
    public HashMap<UUID, Double[]> getGravity() {
        return gravity;
    }

    /**
     * Tracks players using the <code>/tardisbind</code> command
     *
     * @return a Map of player UUIDs and the id of the bind record
     */
    public HashMap<UUID, Integer> getBinder() {
        return binder;
    }

    /**
     * Another tracker for TARDIS Manual flight mode
     *
     * @return a Map of player UUIDs and the number of hits in the flight GUI
     */
    public HashMap<UUID, Integer> getCount() {
        return count;
    }

    /**
     * Tracks players using the <code>/tardis eject</code> command
     *
     * @return a Map of player UUIDs and their TARDIS id
     */
    public HashMap<UUID, Integer> getEjecting() {
        return ejecting;
    }

    /**
     * Tracks players using the Junk TARDIS preset
     *
     * @return a Map of player UUIDs and their TARDIS id
     */
    public HashMap<UUID, Integer> getJunkPlayers() {
        return junkPlayers;
    }

    /**
     * Trackers players removing secondary controls
     *
     * @return a Map of player UUIDs and their TARDIS id
     */
    public HashMap<UUID, Integer> getSecondaryRemovers() {
        return secondaryRemovers;
    }

    /**
     * Tracks players carrying a TARDIS siege block
     *
     * @return a Map of player UUIDs and their TARDIS id
     */
    public HashMap<UUID, Integer> getSiegeCarrying() {
        return siegeCarrying;
    }

    /**
     * Tracks players using the <code>/tardisschematic load</code> command
     *
     * @return a Map of player UUIDs and the schematic they loaded
     */
    public HashMap<UUID, JsonObject> getPastes() {
        return pastes;
    }

    /**
     * Another tracker for TARDIS Manual flight mode
     *
     * @return a Map of player UUIDs and their TARDIS repeater control locations
     */
    public HashMap<UUID, List<Location>> getRepeaters() {
        return repeaters;
    }

    /**
     * Tracks entities rendered inside the Render room
     *
     * @return a Map of player UUIDs and a list of the entity UUIDs
     */
    public HashMap<UUID, List<UUID>> getRenderedNPCs() {
        return renderedNPCs;
    }

    /**
     * Tracks active TARDIS force fields
     *
     * @return a Map of player UUIDs and their TARDIS location
     */
    public HashMap<UUID, Location> getActiveForceFields() {
        return activeForceFields;
    }

    /**
     * Tracks dispersed TARDISes
     *
     * @return a Map of player UUIDs and the location their TARDIS was dispersed
     */
    public HashMap<UUID, Location> getDispersed() {
        return dispersed;
    }

    /**
     * Tracks a schematic end location when using the TARDIS schematic wand
     *
     * @return a Map of player UUIDs and the selected end block location
     */
    public HashMap<UUID, Location> getEndLocation() {
        return endLocation;
    }

    /**
     * Tracks players who logged out while traveklling in the Junk TARDIS
     *
     * @return a Map of player UUIDs and the location the Junk TARDIS was travelling to
     */
    public HashMap<UUID, Location> getJunkRelog() {
        return junkRelog;
    }

    /**
     * Tracks players using the sonic generator
     *
     * @return a Map of player UUIDs and the location of the generator
     */
    public HashMap<UUID, Location> getSonicGenerators() {
        return sonicGenerators;
    }

    /**
     * Tracks a schematic start location when using the TARDIS schematic wand
     *
     * @return a Map of player UUIDs and the selected start block location
     */
    public HashMap<UUID, Location> getStartLocation() {
        return startLocation;
    }

    /**
     * Tracks players using the sonc screwdriver freeze player function
     *
     * @return a Map of player UUIDs and the time in milliseconds they last used the freeze function
     */
    public HashMap<UUID, Long> getCooldown() {
        return cooldown;
    }

    /**
     * Tracks players using the <code>/tardis hide</code> command
     *
     * @return a Map of player UUIDs and the time in milliseconds they last used the command
     */
    public HashMap<UUID, Long> getHideCooldown() {
        return hideCooldown;
    }

    /**
     * Tracks players using the <code>/tardis rebuild</code> command
     *
     * @return a Map of player UUIDs and the time in milliseconds they last used the command
     */
    public HashMap<UUID, Long> getRebuildCooldown() {
        return rebuildCooldown;
    }

    /**
     * Tracks players using the Temporal Locator
     *
     * @return a Map of player UUIDs and the time to relocate to
     */
    public HashMap<UUID, Long> getSetTime() {
        return setTime;
    }

    /**
     * Tracks players using the <code>/tardisarea</code> command
     *
     * @return a Map of player UUIDs and the area name
     */
    public HashMap<UUID, String> getArea() {
        return area;
    }

    /**
     * Tracks players using the <code>/tardisarea</code> command
     *
     * @return a Map of player UUIDs and the start block location string
     */
    public HashMap<UUID, String> getAreaStartBlock() {
        return areaStartBlock;
    }

    /**
     * Tracks players using the <code>/tardisarea</code> command
     *
     * @return a Map of player UUIDs and the end block location string
     */
    public HashMap<UUID, String> getAreaEndBlock() {
        return areaEndBlock;
    }

    /**
     * Another tracker for TARDIS Manual flight mode
     *
     * @return a Map of player UUIDs and the repeater they have to hit
     */
    public HashMap<UUID, String> getFlight() {
        return flight;
    }

    /**
     * Tracks players using the <code>/tardis jettison</code> command
     *
     * @return a Map of player UUIDs and the room name to jettison
     */
    public HashMap<UUID, String> getJettison() {
        return jettison;
    }

    /**
     * Tracks travel permissions for TARDIS areas
     *
     * @return a Map of player UUIDs and the permission to check
     */
    public HashMap<UUID, String> getPerm() {
        return perm;
    }

    /**
     * Tracks players using the <code>/tardis update</code> command
     *
     * @return a Map of player UUIDs and the control name they are trying to update
     */
    public HashMap<UUID, String> getUpdatePlayers() {
        return updatePlayers;
    }

    /**
     * Tracks players using the <code>/tardisadmin make_preset</code> command
     *
     * @return a Map of player UUIDs and the preset name
     */
    public HashMap<UUID, String> getPreset() {
        return preset;
    }

    /**
     * Tracks players that are in wanting to translate chat
     *
     * @return a map of player UUIDs and language codes
     */
    public HashMap<UUID, TranslateData> getTranslators() {
        return translators;
    }

    /**
     * Tracks players placing TARDIS telepathic circuits
     *
     * @return a Map of player UUIDs and the location of the placed circut
     */
    public HashMap<UUID, String> getTelepathicPlacements() {
        return telepathicPlacements;
    }

    /**
     * Tracks players using the TARDIS HUD
     *
     * @return a Map of player UUIDs and the {@link TARDISDisplayType} they are using
     */
    public HashMap<UUID, TARDISDisplayType> getDisplay() {
        return display;
    }

    /**
     * Tracks players using the TARDIS Information System
     *
     * @return a Map of player UUIDs and the TIS entry they are referencing
     */
    public HashMap<UUID, TARDISInfoMenu> getInfoMenu() {
        return infoMenu;
    }

    /**
     * Track players using Regulator flight mode
     *
     * @return a Map of player UUIDs and the Regulator flight task
     */
    public HashMap<UUID, TARDISRegulatorRunnable> getRegulating() {
        return regulating;
    }

    /**
     * Track players manually growing rooms
     *
     * @return a Map of player UUIDs and the room seed data
     */
    public HashMap<UUID, TARDISSeedData> getRoomSeed() {
        return roomSeed;
    }

    /**
     * Track players using the TARDIS desktop theme
     *
     * @return a Map of player UUIDs and the upgrade data
     */
    public HashMap<UUID, TARDISUpgradeData> getUpgrades() {
        return upgrades;
    }

    /**
     * Track players using the chameleon arch fob watch
     *
     * @return a Map of player UUIDs and the watch data
     */
    public HashMap<UUID, TARDISWatchData> getJohnSmith() {
        return johnSmith;
    }

    /**
     * Tracks players using the <code>/tardis secondary</code> command
     *
     * @return a Map of player UUIDs and the secondary control name they are trying to update
     */
    public HashMap<UUID, Updateable> getSecondary() {
        return secondary;
    }

    /**
     * Track players using the <code>/tardis rescue</code> command
     *
     * @return a Map of player UUIDs and the UUID of the player they are trying to rescue
     */
    public HashMap<UUID, UUID> getChatRescue() {
        return chatRescue;
    }

    /**
     * Tracks performing a telepathic rescue
     *
     * @return a Map of player UUIDs and the UUID of the player they are trying to rescue
     */
    public HashMap<UUID, UUID> getTelepathicRescue() {
        return telepathicRescue;
    }

    /**
     * Tracks players using the TARDIS telepathic circuit
     *
     * @return a Map of player UUIDs and the UUID of the TARDIS owner
     */
    public HashMap<UUID, UUID> getTelepaths() {
        return telepaths;
    }

    /**
     * Tracks the locations of Artron furnaces
     *
     * @return a collection of location strings
     */
    public Set<String> getArtronFurnaces() {
        return artronFurnaces;
    }

    /**
     * Tracks the locations of Chemistry heat blocks
     *
     * @return a collection of location strings
     */
    public Set<String> getHeatBlocks() {
        return heatBlocks;
    }

    /**
     * Tracks reset worlds
     *
     * @return a collection of worlds
     */
    public Set<String> getResetWorlds() {
        return resetWorlds;
    }

    /**
     * Tracks TARDISes that are dematerialising
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getDematerialising() {
        return dematerialising;
    }

    /**
     * Tracks TARDISes that dematerialised to the Time Vortex (no travel destination set)
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getDidDematToVortex() {
        return didDematToVortex;
    }

    /**
     * Tracks TARDISes that are dispersed
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getDispersedTARDII() {
        return dispersedTARDII;
    }

    /**
     * Tracks TARDISes that have had their handbrake disengaged
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getHasClickedHandbrake() {
        return hasClickedHandbrake;
    }

    /**
     * Tracks TARDISes that still have their handbrake engaged
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getHasNotClickedHandbrake() {
        return hasNotClickedHandbrake;
    }

    /**
     * Tracks TARDISes that have had their destination set randomly (these TARDIS es will not be able to scan the random destination)
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getHasRandomised() {
        return hasRandomised;
    }

    /**
     * Tracks TARDISes that are in siege mode
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getInSiegeMode() {
        return inSiegeMode;
    }

    /**
     * Tracks TARDISes that are currently passing through the Time Vortex
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getInVortex() {
        return inVortex;
    }

    /**
     * Tracks TARDISes that are a siege cube block (not being carried)
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getIsSiegeCube() {
        return isSiegeCube;
    }

    /**
     * Tracks TARDISes that are materialising
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getMaterialising() {
        return materialising;
    }

    /**
     * Tracks TARDISes that have a mincart entering the RAil room
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getMinecart() {
        return minecart;
    }

    /**
     * Tracks TARDISes in submarine mode
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getSubmarine() {
        return submarine;
    }

    /**
     * Tracks TARDISes that are growing rooms
     *
     * @return a collection of TARDIS ids
     */
    public Set<Integer> getIsGrowingRooms() {
        return isGrowingRooms;
    }

    /**
     * Tracks players that are rearranging saves in the Saves GUI
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getArrangers() {
        return arrangers;
    }

    /**
     * Tracks players that are colouring TARDIS beacon glass blocks
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getBeaconColouring() {
        return beaconColouring;
    }

    /**
     * Tracks players that are using the Chameleon Construction GUI
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getConstructors() {
        return constructors;
    }

    /**
     * Tracks players that have discovered the TARDIS easter egg. Used for cooldown purposes
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getEggs() {
        return eggs;
    }

    /**
     * Tracks players that have used TARDIS atmospheric excitation to make it snow. Used for cooldown purposes
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getExcitation() {
        return excitation;
    }

    /**
     * Tracks players that are farming mobs while entering the TARDIS
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getFarming() {
        return farming;
    }

    /**
     * Tracks players that are frozen by the sonic screwdriver
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    /**
     * Tracks players that are genetically modified
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getGeneticallyModified() {
        return geneticallyModified;
    }

    /**
     * Tracks players that are using the genetic manipulator GUI
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getGeneticManipulation() {
        return geneticManipulation;
    }

    /**
     * Tracks players that have time travelled
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getHasTravelled() {
        return hasTravelled;
    }

    /**
     * Tracks players that using the How to build a TARDIS helper GUI
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getHowTo() {
        return howTo;
    }

    /**
     * Tracks players entering TARDISes
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getMover() {
        return mover;
    }

    /**
     * Tracks players that are viewing TARDIS recipes
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getRecipeViewers() {
        return recipeViewers;
    }

    /**
     * Tracks players that are inside the Render room
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getRenderRoomOccupants() {
        return renderRoomOccupants;
    }

    /**
     * Tracks players that are sonicing doors
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getSonicDoors() {
        return sonicDoors;
    }

    /**
     * Tracks players that are wearing 3-D glasses
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getSpectacleWearers() {
        return spectacleWearers;
    }

    /**
     * Tracks players that are temporally (re)located
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getTemporallyLocated() {
        return temporallyLocated;
    }

    /**
     * Tracks players that are in the Zero room
     *
     * @return a collection of player UUIDs
     */
    public Set<UUID> getZeroRoomOccupants() {
        return zeroRoomOccupants;
    }
}
