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
package me.eccentric_nz.TARDIS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.flyingmodes.TARDISRegulatorRunnable;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.move.TARDISMoveSession;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.rooms.TARDISSeedData;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTrackerInstanceKeeper {

    private String immortalityGate = "";
    private final HashMap<Integer, Integer> damage = new HashMap<Integer, Integer>();
    private final HashMap<Integer, Integer> hasDestination = new HashMap<Integer, Integer>();
    private final HashMap<Integer, String> renderer = new HashMap<Integer, String>();
    private final HashMap<Integer, TARDISAntiBuild> antiBuild = new HashMap<Integer, TARDISAntiBuild>();
    private final HashMap<Integer, UUID> rescue = new HashMap<Integer, UUID>();
    private final HashMap<Location, TARDISTeleportLocation> portals = new HashMap<Location, TARDISTeleportLocation>();
    private final HashMap<String, Sign> sign = new HashMap<String, Sign>();
    private final HashMap<UUID, Block> exterminate = new HashMap<UUID, Block>();
    private final HashMap<UUID, Block> lazarus = new HashMap<UUID, Block>();
    private final HashMap<UUID, Double[]> gravity = new HashMap<UUID, Double[]>();
    private final HashMap<UUID, Integer> binder = new HashMap<UUID, Integer>();
    private final HashMap<UUID, Integer> count = new HashMap<UUID, Integer>();
    private final HashMap<UUID, List<Location>> repeaters = new HashMap<UUID, List<Location>>();
    private final HashMap<UUID, Long> setTime = new HashMap<UUID, Long>();
    private final HashMap<UUID, String> area = new HashMap<UUID, String>();
    private final HashMap<UUID, String> block = new HashMap<UUID, String>();
    private final HashMap<UUID, String> end = new HashMap<UUID, String>();
    private final HashMap<UUID, String> flight = new HashMap<UUID, String>();
    private final HashMap<UUID, String> jettison = new HashMap<UUID, String>();
    private final HashMap<UUID, String> perm = new HashMap<UUID, String>();
    private final HashMap<UUID, String> players = new HashMap<UUID, String>();
    private final HashMap<UUID, String> preset = new HashMap<UUID, String>();
    private final HashMap<UUID, String> secondary = new HashMap<UUID, String>();
    private final HashMap<UUID, TARDISInfoMenu> infoMenu = new HashMap<UUID, TARDISInfoMenu>();
    private final HashMap<UUID, TARDISMaterialisationData> flightData = new HashMap<UUID, TARDISMaterialisationData>();
    private final HashMap<UUID, TARDISMoveSession> moveSessions = new HashMap<UUID, TARDISMoveSession>();
    private final HashMap<UUID, TARDISRegulatorRunnable> regulating = new HashMap<UUID, TARDISRegulatorRunnable>();
    private final HashMap<UUID, TARDISSeedData> roomSeed = new HashMap<UUID, TARDISSeedData>();
    private final HashMap<UUID, UUID> chat = new HashMap<UUID, UUID>();
    private final List<Integer> dematerialising = new ArrayList<Integer>();
    private final List<Integer> inVortex = new ArrayList<Integer>();
    private final List<Integer> materialising = new ArrayList<Integer>();
    private final List<Integer> minecart = new ArrayList<Integer>();
    private final List<Integer> submarine = new ArrayList<Integer>();
    private final List<String> reset = new ArrayList<String>();
    private final List<UUID> arrangers = new ArrayList<UUID>();
    private final List<UUID> farming = new ArrayList<UUID>();
    private final List<UUID> geneticManipulation = new ArrayList<UUID>();
    private final List<UUID> mover = new ArrayList<UUID>();
    private final List<UUID> recipeView = new ArrayList<UUID>();
    private final List<UUID> sonicDoors = new ArrayList<UUID>();
    private final List<UUID> transmat = new ArrayList<UUID>();
    private final List<UUID> zeroRoomOccupants = new ArrayList<UUID>();

    public HashMap<Integer, Integer> getDamage() {
        return damage;
    }

    public HashMap<Integer, Integer> getHasDestination() {
        return hasDestination;
    }

    public HashMap<Integer, String> getRenderer() {
        return renderer;
    }

    public HashMap<Integer, UUID> getRescue() {
        return rescue;
    }

    public HashMap<Location, TARDISTeleportLocation> getPortals() {
        return portals;
    }

    public HashMap<Integer, TARDISAntiBuild> getAntiBuild() {
        return antiBuild;
    }

    public HashMap<UUID, Block> getExterminate() {
        return exterminate;
    }

    public HashMap<UUID, Block> getLazarus() {
        return lazarus;
    }

    public HashMap<UUID, Double[]> getGravity() {
        return gravity;
    }

    public HashMap<UUID, Integer> getBinder() {
        return binder;
    }

    public HashMap<UUID, Long> getSetTime() {
        return setTime;
    }

    public HashMap<String, Sign> getSign() {
        return sign;
    }

    public HashMap<UUID, String> getBlock() {
        return block;
    }

    public HashMap<UUID, UUID> getChat() {
        return chat;
    }

    public HashMap<UUID, String> getFlight() {
        return flight;
    }

    public HashMap<UUID, Integer> getCount() {
        return count;
    }

    public HashMap<UUID, TARDISRegulatorRunnable> getRegulating() {
        return regulating;
    }

    public HashMap<UUID, TARDISMaterialisationData> getFlightData() {
        return flightData;
    }

    public HashMap<UUID, List<Location>> getRepeaters() {
        return repeaters;
    }

    public HashMap<UUID, String> getEnd() {
        return end;
    }

    public HashMap<UUID, String> getJettison() {
        return jettison;
    }

    public HashMap<UUID, String> getArea() {
        return area;
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

    public HashMap<UUID, String> getSecondary() {
        return secondary;
    }

    public HashMap<UUID, TARDISInfoMenu> getInfoMenu() {
        return infoMenu;
    }

    public TARDISMoveSession getTARDISMoveSession(Player p) {
        if (this.moveSessions.containsKey(p.getUniqueId())) {
            return this.moveSessions.get(p.getUniqueId());
        }
        TARDISMoveSession session = new TARDISMoveSession(p);
        this.moveSessions.put(p.getUniqueId(), session);
        return session;
    }

    public HashMap<UUID, TARDISSeedData> getRoomSeed() {
        return roomSeed;
    }

    public List<Integer> getDematerialising() {
        return dematerialising;
    }

    public List<Integer> getInVortex() {
        return inVortex;
    }

    public List<Integer> getMaterialising() {
        return materialising;
    }

    public List<Integer> getMinecart() {
        return minecart;
    }

    public List<Integer> getSubmarine() {
        return submarine;
    }

    public List<UUID> getArrangers() {
        return arrangers;
    }

    public List<UUID> getFarming() {
        return farming;
    }

    public List<UUID> getGeneticManipulation() {
        return geneticManipulation;
    }

    public List<UUID> getMover() {
        return mover;
    }

    public List<UUID> getRecipeView() {
        return recipeView;
    }

    public List<UUID> getSonicDoors() {
        return sonicDoors;
    }

    public List<String> getReset() {
        return reset;
    }

    public List<UUID> getTransmat() {
        return transmat;
    }

    public List<UUID> getZeroRoomOccupants() {
        return zeroRoomOccupants;
    }

    public String getImmortalityGate() {
        return immortalityGate;
    }

    public void setImmortalityGate(String immortalityGate) {
        this.immortalityGate = immortalityGate;
    }
}
