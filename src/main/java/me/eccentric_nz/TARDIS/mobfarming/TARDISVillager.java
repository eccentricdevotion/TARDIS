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
package me.eccentric_nz.TARDIS.mobfarming;

import java.util.ArrayList;
import org.bukkit.entity.Villager.Profession;

/**
 *
 * @author eccentric_nz
 */
@SuppressWarnings("rawtypes")
public class TARDISVillager extends TARDISMob {

    private Profession profession;
    private ArrayList trades;
    private int career;

    /**
     * Data storage class for TARDIS villagers.
     */
    public TARDISVillager() {
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    @SuppressWarnings("rawtypes")
    public ArrayList getTrades() {
        return trades;
    }

    @SuppressWarnings("rawtypes")
    public void setTrades(ArrayList trades) {
        this.trades = trades;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }
}
