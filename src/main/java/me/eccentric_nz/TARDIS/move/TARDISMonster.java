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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.mobfarming.TARDISMob;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.material.MaterialData;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMonster extends TARDISMob {

    private boolean aggressive;
    private int anger;
    private boolean charged;
    private Profession profession;
    private EntityEquipment equipment;
    private MaterialData carried;
    private EntityType passenger;
    private String displayName;
    private int size;

    public boolean isAggressive() {
        return aggressive;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public int getAnger() {
        return anger;
    }

    public void setAnger(int anger) {
        this.anger = anger;
    }

    public boolean isCharged() {
        return charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public EntityEquipment getEquipment() {
        return equipment;
    }

    public void setEquipment(EntityEquipment equipment) {
        this.equipment = equipment;
    }

    public MaterialData getCarried() {
        return carried;
    }

    public void setCarried(MaterialData carried) {
        this.carried = carried;
    }

    public EntityType getPassenger() {
        return passenger;
    }

    public void setPassenger(EntityType passenger) {
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
