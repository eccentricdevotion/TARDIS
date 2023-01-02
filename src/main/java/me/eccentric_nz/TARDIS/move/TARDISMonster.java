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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.mobfarming.TARDISMob;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.EntityEquipment;

/**
 * @author eccentric_nz
 */
class TARDISMonster extends TARDISMob {

    private boolean aggressive;
    private int anger;
    private boolean charged;
    private Profession profession;
    private EntityEquipment equipment;
    private BlockData carried;
    private EntityType passenger;
    private String displayName;
    private int size;

    boolean isAggressive() {
        return aggressive;
    }

    void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    int getAnger() {
        return anger;
    }

    void setAnger(int anger) {
        this.anger = anger;
    }

    public boolean isCharged() {
        return charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    Profession getProfession() {
        return profession;
    }

    void setProfession(Profession profession) {
        this.profession = profession;
    }

    EntityEquipment getEquipment() {
        return equipment;
    }

    void setEquipment(EntityEquipment equipment) {
        this.equipment = equipment;
    }

    BlockData getCarried() {
        return carried;
    }

    void setCarried(BlockData carried) {
        this.carried = carried;
    }

    EntityType getPassenger() {
        return passenger;
    }

    void setPassenger(EntityType passenger) {
        this.passenger = passenger;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
