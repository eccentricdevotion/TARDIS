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
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.rooms.TARDISSeedData;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTrackerInstanceKeeper {

    private final HashMap<Integer, Integer> trackDamage = new HashMap<Integer, Integer>();
    private final HashMap<Integer, Integer> trackHasDestination = new HashMap<Integer, Integer>();
    private final HashMap<Integer, String> trackRenderer = new HashMap<Integer, String>();
    private final HashMap<Integer, UUID> trackRescue = new HashMap<Integer, UUID>();
    private final HashMap<Integer, TARDISAntiBuild> trackAntiBuild = new HashMap<Integer, TARDISAntiBuild>();
    private final HashMap<UUID, Block> trackExterminate = new HashMap<UUID, Block>();
    private final HashMap<UUID, Block> trackLazarus = new HashMap<UUID, Block>();
    private final HashMap<UUID, Double[]> trackGravity = new HashMap<UUID, Double[]>();
    private final HashMap<UUID, Integer> trackBinder = new HashMap<UUID, Integer>();
    private final HashMap<UUID, Long> trackSetTime = new HashMap<UUID, Long>();
    private final HashMap<String, Sign> trackSign = new HashMap<String, Sign>();
    private final HashMap<UUID, String> trackBlock = new HashMap<UUID, String>();
    private final HashMap<UUID, UUID> trackChat = new HashMap<UUID, UUID>();
    private final HashMap<UUID, String> trackEnd = new HashMap<UUID, String>();
    private final HashMap<UUID, String> trackJettison = new HashMap<UUID, String>();
    private final HashMap<UUID, String> trackUUID = new HashMap<UUID, String>();
    private final HashMap<UUID, String> trackPerm = new HashMap<UUID, String>();
    private final HashMap<UUID, String> trackPlayers = new HashMap<UUID, String>();
    private final HashMap<UUID, String> trackPreset = new HashMap<UUID, String>();
    private final HashMap<UUID, String> trackSecondary = new HashMap<UUID, String>();
    private final HashMap<UUID, TARDISInfoMenu> trackInfoMenu = new HashMap<UUID, TARDISInfoMenu>();
    private final HashMap<UUID, TARDISSeedData> trackRoomSeed = new HashMap<UUID, TARDISSeedData>();
    private final List<Integer> trackDematerialising = new ArrayList<Integer>();
    private final List<Integer> trackInVortex = new ArrayList<Integer>();
    private final List<Integer> trackMaterialising = new ArrayList<Integer>();
    private final List<Integer> trackMinecart = new ArrayList<Integer>();
    private final List<Integer> trackSubmarine = new ArrayList<Integer>();
    private final List<UUID> trackArrangers = new ArrayList<UUID>();
    private final List<UUID> trackFarming = new ArrayList<UUID>();
    private final List<UUID> trackGeneticManipulation = new ArrayList<UUID>();
    private final List<UUID> trackRecipeView = new ArrayList<UUID>();
    private final List<String> trackReset = new ArrayList<String>();
    private final List<UUID> trackTransmat = new ArrayList<UUID>();
    private final List<UUID> trackZeroRoomOccupants = new ArrayList<UUID>();
    private String trackImmortalityGate = "";

    public HashMap<Integer, Integer> getTrackDamage() {
        return trackDamage;
    }

    public HashMap<Integer, Integer> getTrackHasDestination() {
        return trackHasDestination;
    }

    public HashMap<Integer, String> getTrackRenderer() {
        return trackRenderer;
    }

    public HashMap<Integer, UUID> getTrackRescue() {
        return trackRescue;
    }

    public HashMap<Integer, TARDISAntiBuild> getTrackAntiBuild() {
        return trackAntiBuild;
    }

    public HashMap<UUID, Block> getTrackExterminate() {
        return trackExterminate;
    }

    public HashMap<UUID, Block> getTrackLazarus() {
        return trackLazarus;
    }

    public HashMap<UUID, Double[]> getTrackGravity() {
        return trackGravity;
    }

    public HashMap<UUID, Integer> getTrackBinder() {
        return trackBinder;
    }

    public HashMap<UUID, Long> getTrackSetTime() {
        return trackSetTime;
    }

    public HashMap<String, Sign> getTrackSign() {
        return trackSign;
    }

    public HashMap<UUID, String> getTrackBlock() {
        return trackBlock;
    }

    public HashMap<UUID, UUID> getTrackChat() {
        return trackChat;
    }

    public HashMap<UUID, String> getTrackEnd() {
        return trackEnd;
    }

    public HashMap<UUID, String> getTrackJettison() {
        return trackJettison;
    }

    public HashMap<UUID, String> getTrackUUID() {
        return trackUUID;
    }

    public HashMap<UUID, String> getTrackPerm() {
        return trackPerm;
    }

    public HashMap<UUID, String> getTrackPlayers() {
        return trackPlayers;
    }

    public HashMap<UUID, String> getTrackPreset() {
        return trackPreset;
    }

    public HashMap<UUID, String> getTrackSecondary() {
        return trackSecondary;
    }

    public HashMap<UUID, TARDISInfoMenu> getTrackInfoMenu() {
        return trackInfoMenu;
    }

    public HashMap<UUID, TARDISSeedData> getTrackRoomSeed() {
        return trackRoomSeed;
    }

    public List<Integer> getTrackDematerialising() {
        return trackDematerialising;
    }

    public List<Integer> getTrackInVortex() {
        return trackInVortex;
    }

    public List<Integer> getTrackMaterialising() {
        return trackMaterialising;
    }

    public List<Integer> getTrackMinecart() {
        return trackMinecart;
    }

    public List<Integer> getTrackSubmarine() {
        return trackSubmarine;
    }

    public List<UUID> getTrackArrangers() {
        return trackArrangers;
    }

    public List<UUID> getTrackFarming() {
        return trackFarming;
    }

    public List<UUID> getTrackGeneticManipulation() {
        return trackGeneticManipulation;
    }

    public List<UUID> getTrackRecipeView() {
        return trackRecipeView;
    }

    public List<String> getTrackReset() {
        return trackReset;
    }

    public List<UUID> getTrackTransmat() {
        return trackTransmat;
    }

    public List<UUID> getTrackZeroRoomOccupants() {
        return trackZeroRoomOccupants;
    }

    public String getTrackImmortalityGate() {
        return trackImmortalityGate;
    }

    public void setTrackImmortalityGate(String trackImmortalityGate) {
        this.trackImmortalityGate = trackImmortalityGate;
    }
}
