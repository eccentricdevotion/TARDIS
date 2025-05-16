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
package me.eccentric_nz.TARDIS.mobfarming;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.MerchantRecipe;

import java.util.List;

/**
 * @author eccentric_nz
 */
class TARDISVillager extends TARDISMob {

    private Villager.Profession profession;
    private List<MerchantRecipe> trades;
    private int level;
    private Villager.Type villagerType;
    private int experience;
    private int[] reputation;

    public TARDISVillager() {
        super.setType(EntityType.VILLAGER);
    }

    /**
     * Data storage class for TARDIS villagers.
     */


    Villager.Profession getProfession() {
        return profession;
    }

    void setProfession(Villager.Profession profession) {
        this.profession = profession;
    }

    List<MerchantRecipe> getTrades() {
        return trades;
    }

    void setTrades(List<MerchantRecipe> trades) {
        this.trades = trades;
    }

    int getLevel() {
        return level;
    }

    void setLevel(int level) {
        this.level = level;
    }

    Villager.Type getVillagerType() {
        return villagerType;
    }

    void setVillagerType(Villager.Type villagerType) {
        this.villagerType = villagerType;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int[] getReputation() {
        return reputation;
    }

    public void setReputation(int[] reputation) {
        this.reputation = reputation;
    }
}
