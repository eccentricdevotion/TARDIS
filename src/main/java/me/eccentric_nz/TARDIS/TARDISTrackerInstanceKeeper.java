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
import me.eccentric_nz.TARDIS.rooms.TARDISRoomData;
import me.eccentric_nz.TARDIS.rooms.TARDISSeedData;
import me.eccentric_nz.TARDIS.siegemode.TARDISSiegeArea;
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

    private final HashMap<Integer, Boolean> malfunction = new HashMap<>();
    private final HashMap<Integer, Integer> cloisterBells = new HashMap<>();
    private final HashMap<Integer, Integer> damage = new HashMap<>();
    private final HashMap<Integer, Integer> destinationVortex = new HashMap<>();
    private final HashMap<Integer, Integer> hasDestination = new HashMap<>();
    private final HashMap<Integer, String> renderer = new HashMap<>();
    private final HashMap<Integer, TARDISAntiBuild> antiBuild = new HashMap<>();
    private final HashMap<Integer, TARDISRoomData> roomTasks = new HashMap<>();
    private final HashMap<Integer, UUID> rescue = new HashMap<>();
    private final HashMap<Location, TARDISTeleportLocation> portals = new HashMap<>();
    private final HashMap<String, List<TARDISSiegeArea>> siegeBreedingAreas = new HashMap<>();
    private final HashMap<String, List<TARDISSiegeArea>> siegeGrowthAreas = new HashMap<>();
    private final HashMap<String, Sign> sign = new HashMap<>();
    private final HashMap<UUID, Bind> bindRemoval = new HashMap<>();
    private final HashMap<UUID, Block> exterminate = new HashMap<>();
    private final HashMap<UUID, Block> invisibleDoors = new HashMap<>();
    private final HashMap<UUID, Block> lazarus = new HashMap<>();
    private final HashMap<UUID, BuildData> flightData = new HashMap<>();
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
    private final HashMap<UUID, String> block = new HashMap<>();
    private final HashMap<UUID, String> end = new HashMap<>();
    private final HashMap<UUID, String> flight = new HashMap<>();
    private final HashMap<UUID, String> jettison = new HashMap<>();
    private final HashMap<UUID, String> perm = new HashMap<>();
    private final HashMap<UUID, String> players = new HashMap<>();
    private final HashMap<UUID, String> preset = new HashMap<>();
    private final HashMap<UUID, String> telepathicPlacements = new HashMap<>();
    private final HashMap<UUID, TARDISDisplayType> display = new HashMap<>();
    private final HashMap<UUID, TARDISInfoMenu> infoMenu = new HashMap<>();
    private final HashMap<UUID, TARDISMoveSession> moveSessions = new HashMap<>();
    private final HashMap<UUID, TARDISRegulatorRunnable> regulating = new HashMap<>();
    private final HashMap<UUID, TARDISSeedData> roomSeed = new HashMap<>();
    private final HashMap<UUID, TARDISUpgradeData> upgrades = new HashMap<>();
    private final HashMap<UUID, TARDISWatchData> johnSmith = new HashMap<>();
    private final HashMap<UUID, Updateable> secondary = new HashMap<>();
    private final HashMap<UUID, UUID> chat = new HashMap<>();
    private final HashMap<UUID, UUID> telepathicRescue = new HashMap<>();
    private final HashMap<UUID, UUID> telepaths = new HashMap<>();
    private final Set<String> artronFurnaces = new HashSet<>();
    private final Set<String> heatBlocks = new HashSet<>();
    private final Set<String> reset = new HashSet<>();
    private final Set<Integer> dematerialising = new HashSet<>();
    private final Set<Integer> didDematToVortex = new HashSet<>();
    private final Set<Integer> dispersedTARDII = new HashSet<>();
    private final Set<Integer> hasClickedHandbrake = new HashSet<>();
    private final Set<Integer> hasNotClickedHandbrake = new HashSet<>();
    private final Set<Integer> hasRandomised = new HashSet<>();
    private final Set<Integer> inSiegeMode = new HashSet<>();
    private final Set<Integer> inVortex = new HashSet<>();
    private final Set<Integer> isSiegeCube = new HashSet<>();
    private final Set<Integer> keyboard = new HashSet<>();
    private final Set<Integer> materialising = new HashSet<>();
    private final Set<Integer> minecart = new HashSet<>();
    private final Set<Integer> submarine = new HashSet<>();
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
    private final Set<UUID> recipeView = new HashSet<>();
    private final Set<UUID> renderRoomOccupants = new HashSet<>();
    private final Set<UUID> sonicDoors = new HashSet<>();
    private final Set<UUID> spectacleWearers = new HashSet<>();
    private final Set<UUID> temporallyLocated = new HashSet<>();
    private final Set<UUID> zeroRoomOccupants = new HashSet<>();
    private String immortalityGate = "";

    public TARDISMoveSession getTARDISMoveSession(Player p) {
        if (moveSessions.containsKey(p.getUniqueId())) {
            return moveSessions.get(p.getUniqueId());
        }
        TARDISMoveSession session = new TARDISMoveSession(p);
        moveSessions.put(p.getUniqueId(), session);
        return session;
    }

    public String getImmortalityGate() {
        return immortalityGate;
    }

    public void setImmortalityGate(String immortalityGate) {
        this.immortalityGate = immortalityGate;
    }

    public HashMap<Integer, Boolean> getMalfunction() {
        return malfunction;
    }

    public HashMap<Integer, Integer> getCloisterBells() {
        return cloisterBells;
    }

    public HashMap<Integer, Integer> getDamage() {
        return damage;
    }

    public HashMap<Integer, Integer> getDestinationVortex() {
        return destinationVortex;
    }

    public HashMap<Integer, Integer> getHasDestination() {
        return hasDestination;
    }

    public HashMap<Integer, String> getRenderer() {
        return renderer;
    }

    public HashMap<Integer, TARDISAntiBuild> getAntiBuild() {
        return antiBuild;
    }

    public HashMap<Integer, TARDISRoomData> getRoomTasks() {
        return roomTasks;
    }

    public HashMap<Integer, UUID> getRescue() {
        return rescue;
    }

    public HashMap<Location, TARDISTeleportLocation> getPortals() {
        return portals;
    }

    public HashMap<String, List<TARDISSiegeArea>> getSiegeBreedingAreas() {
        return siegeBreedingAreas;
    }

    public HashMap<String, List<TARDISSiegeArea>> getSiegeGrowthAreas() {
        return siegeGrowthAreas;
    }

    public HashMap<String, Sign> getSign() {
        return sign;
    }

    public HashMap<UUID, Bind> getBindRemoval() {
        return bindRemoval;
    }

    public HashMap<UUID, Block> getExterminate() {
        return exterminate;
    }

    public HashMap<UUID, Block> getInvisibleDoors() {
        return invisibleDoors;
    }

    public HashMap<UUID, Block> getLazarus() {
        return lazarus;
    }

    public HashMap<UUID, BuildData> getFlightData() {
        return flightData;
    }

    public HashMap<UUID, Double[]> getGravity() {
        return gravity;
    }

    public HashMap<UUID, Integer> getBinder() {
        return binder;
    }

    public HashMap<UUID, Integer> getCount() {
        return count;
    }

    public HashMap<UUID, Integer> getEjecting() {
        return ejecting;
    }

    public HashMap<UUID, Integer> getJunkPlayers() {
        return junkPlayers;
    }

    public HashMap<UUID, Integer> getSecondaryRemovers() {
        return secondaryRemovers;
    }

    public HashMap<UUID, Integer> getSiegeCarrying() {
        return siegeCarrying;
    }

    public HashMap<UUID, JsonObject> getPastes() {
        return pastes;
    }

    public HashMap<UUID, List<Location>> getRepeaters() {
        return repeaters;
    }

    public HashMap<UUID, List<UUID>> getRenderedNPCs() {
        return renderedNPCs;
    }

    public HashMap<UUID, Location> getActiveForceFields() {
        return activeForceFields;
    }

    public HashMap<UUID, Location> getDispersed() {
        return dispersed;
    }

    public HashMap<UUID, Location> getEndLocation() {
        return endLocation;
    }

    public HashMap<UUID, Location> getJunkRelog() {
        return junkRelog;
    }

    public HashMap<UUID, Location> getSonicGenerators() {
        return sonicGenerators;
    }

    public HashMap<UUID, Location> getStartLocation() {
        return startLocation;
    }

    public HashMap<UUID, Long> getCooldown() {
        return cooldown;
    }

    public HashMap<UUID, Long> getHideCooldown() {
        return hideCooldown;
    }

    public HashMap<UUID, Long> getRebuildCooldown() {
        return rebuildCooldown;
    }

    public HashMap<UUID, Long> getSetTime() {
        return setTime;
    }

    public HashMap<UUID, String> getArea() {
        return area;
    }

    public HashMap<UUID, String> getBlock() {
        return block;
    }

    public HashMap<UUID, String> getEnd() {
        return end;
    }

    public HashMap<UUID, String> getFlight() {
        return flight;
    }

    public HashMap<UUID, String> getJettison() {
        return jettison;
    }

    public HashMap<UUID, String> getPerm() {
        return perm;
    }

    public HashMap<UUID, String> getPlayers() {
        return players;
    }

    public HashMap<UUID, String> getPreset() {
        return preset;
    }

    public HashMap<UUID, String> getTelepathicPlacements() {
        return telepathicPlacements;
    }

    public HashMap<UUID, TARDISDisplayType> getDisplay() {
        return display;
    }

    public HashMap<UUID, TARDISInfoMenu> getInfoMenu() {
        return infoMenu;
    }

    public HashMap<UUID, TARDISRegulatorRunnable> getRegulating() {
        return regulating;
    }

    public HashMap<UUID, TARDISSeedData> getRoomSeed() {
        return roomSeed;
    }

    public HashMap<UUID, TARDISUpgradeData> getUpgrades() {
        return upgrades;
    }

    public HashMap<UUID, TARDISWatchData> getJohnSmith() {
        return johnSmith;
    }

    public HashMap<UUID, Updateable> getSecondary() {
        return secondary;
    }

    public HashMap<UUID, UUID> getChat() {
        return chat;
    }

    public HashMap<UUID, UUID> getTelepathicRescue() {
        return telepathicRescue;
    }

    public HashMap<UUID, UUID> getTelepaths() {
        return telepaths;
    }

    public Set<String> getArtronFurnaces() {
        return artronFurnaces;
    }

    public Set<String> getHeatBlocks() {
        return heatBlocks;
    }

    public Set<String> getReset() {
        return reset;
    }

    public Set<Integer> getDematerialising() {
        return dematerialising;
    }

    public Set<Integer> getDidDematToVortex() {
        return didDematToVortex;
    }

    public Set<Integer> getDispersedTARDII() {
        return dispersedTARDII;
    }

    public Set<Integer> getHasClickedHandbrake() {
        return hasClickedHandbrake;
    }

    public Set<Integer> getHasNotClickedHandbrake() {
        return hasNotClickedHandbrake;
    }

    public Set<Integer> getHasRandomised() {
        return hasRandomised;
    }

    public Set<Integer> getInSiegeMode() {
        return inSiegeMode;
    }

    public Set<Integer> getInVortex() {
        return inVortex;
    }

    public Set<Integer> getIsSiegeCube() {
        return isSiegeCube;
    }

    public Set<Integer> getKeyboard() {
        return keyboard;
    }

    public Set<Integer> getMaterialising() {
        return materialising;
    }

    public Set<Integer> getMinecart() {
        return minecart;
    }

    public Set<Integer> getSubmarine() {
        return submarine;
    }

    public Set<UUID> getArrangers() {
        return arrangers;
    }

    public Set<UUID> getBeaconColouring() {
        return beaconColouring;
    }

    public Set<UUID> getConstructors() {
        return constructors;
    }

    public Set<UUID> getEggs() {
        return eggs;
    }

    public Set<UUID> getExcitation() {
        return excitation;
    }

    public Set<UUID> getFarming() {
        return farming;
    }

    public Set<UUID> getFrozenPlayers() {
        return frozenPlayers;
    }

    public Set<UUID> getGeneticallyModified() {
        return geneticallyModified;
    }

    public Set<UUID> getGeneticManipulation() {
        return geneticManipulation;
    }

    public Set<UUID> getHasTravelled() {
        return hasTravelled;
    }

    public Set<UUID> getHowTo() {
        return howTo;
    }

    public Set<UUID> getMover() {
        return mover;
    }

    public Set<UUID> getRecipeView() {
        return recipeView;
    }

    public Set<UUID> getRenderRoomOccupants() {
        return renderRoomOccupants;
    }

    public Set<UUID> getSonicDoors() {
        return sonicDoors;
    }

    public Set<UUID> getSpectacleWearers() {
        return spectacleWearers;
    }

    public Set<UUID> getTemporallyLocated() {
        return temporallyLocated;
    }

    public Set<UUID> getZeroRoomOccupants() {
        return zeroRoomOccupants;
    }
}
