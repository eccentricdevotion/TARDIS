/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.List;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.MerchantRecipe;

/**
 *
 * @author eccentric_nz
 */
@SuppressWarnings("rawtypes")
public class TARDISVillager extends TARDISMob {

    private Profession profession;
    private List<MerchantRecipe> trades;
    private int career;
    private int careerLevel;
    private boolean willing;
    private int riches;

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

    public List<MerchantRecipe> getTrades() {
        return trades;
    }

    public void setTrades(List<MerchantRecipe> trades) {
        this.trades = trades;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getCareerLevel() {
        return careerLevel;
    }

    public void setCareerLevel(int careerLevel) {
        this.careerLevel = careerLevel;
    }

    public boolean isWilling() {
        return willing;
    }

    public void setWilling(boolean willing) {
        this.willing = willing;
    }

    public int getRiches() {
        return riches;
    }

    public void setRiches(int riches) {
        this.riches = riches;
    }
}
