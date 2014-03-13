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
package me.eccentric_nz.TARDIS.utility;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Due to the age of the TARDIS, it is inclined to break down. The Doctor is
 * often seen with his head stuck in a panel carrying out maintenance of some
 * kind or another, and he occasionally has to give it "percussive maintenance"
 * (a good thump on the console) to get it to start working properly.
 *
 * @author eccentric_nz
 */
public class TARDISTownyChecker {

    private Towny towny;
    private TownyRegion tr;

    public TARDISTownyChecker(TARDIS plugin, boolean onServer) {
        if (onServer) {
            towny = (Towny) plugin.getPM().getPlugin("Towny");
            try {
                tr = TownyRegion.valueOf(plugin.getConfig().getString("preferences.respect_towny"));
            } catch (IllegalArgumentException e) {
                plugin.debug("Could not get TownyRegion from config!");
                tr = TownyRegion.nation;
            }
        }
    }

    /**
     * Checks whether a player can land in a location that may be in a Towny
     * town. If the player is a resident of the town or nation, then it will be
     * allowed.
     *
     * @param p the player
     * @param l the location instance to check.
     * @return true or false depending on whether the player can build in this
     * location
     */
    public boolean checkTowny(Player p, Location l) {
        if (towny != null) {
            switch (tr) {
                case wilderness:
                    // allow if wilderness, deny if a claimed town
                    return (TownyUniverse.isWilderness(l.getBlock()));
                case town:
                    // allow wilderness and the player's own town
                    return playerIsResident(p, l).canTravel();
                default:
                    // allow wilderness, the player's own town and any town in the player's nation
                    return playerIsCompatriot(p, l);
            }
        }
        return false;
    }

    public TownyData playerIsResident(Player p, Location l) {

        TownyData td = new TownyData();

        TownBlock tb = TownyUniverse.getTownBlock(l);
        if (tb == null) {
            // allow, location is not within a town
            td.setCanTravel(true);
            return td;
        }
        td.setTownBlock(tb);
        Resident res;
        try {
            res = TownyUniverse.getDataSource().getResident(p.getName());
            td.setResident(res);
        } catch (NotRegisteredException ex) {
            // deny, player is not a resident
            td.setCanTravel(false);
            return td;
        }
        if (res != null) {
            try {
                List<Resident> residents = tb.getTown().getResidents();
                if (residents.contains(res)) {
                    // allow, player is resident
                    td.setCanTravel(true);
                    return td;
                }
            } catch (NotRegisteredException ex) {
                // allow, town is not registered
                td.setCanTravel(true);
                return td;
            }
        }
        td.setCanTravel(TownyUniverse.isWilderness(l.getBlock()));
        return td;
    }

    public boolean playerIsCompatriot(Player p, Location l) {
        TownyData td = playerIsResident(p, l);
        if (td.canTravel()) {
            return true;
        } else {
            if (td.getResident().hasNation() && td.getResident().hasTown()) {
                try {
                    Nation nation = td.getResident().getTown().getNation();
                    if (td.getTownBlock().hasTown() && td.getTownBlock().getTown().getNation().equals(nation)) {
                        return true;
                    }
                } catch (NotRegisteredException ex) {
                    // no nation, return false
                    return false;
                }
            }
        }
        return false;
    }

    private enum TownyRegion {

        wilderness, town, nation
    }

    public class TownyData {

        private boolean travel;
        private TownBlock townBlock;
        private Resident resident;

        public boolean canTravel() {
            return travel;
        }

        public void setCanTravel(boolean travel) {
            this.travel = travel;
        }

        public TownBlock getTownBlock() {
            return townBlock;
        }

        public void setTownBlock(TownBlock townBlock) {
            this.townBlock = townBlock;
        }

        public Resident getResident() {
            return resident;
        }

        public void setResident(Resident resident) {
            this.resident = resident;
        }
    }
}
