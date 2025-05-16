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

import org.bukkit.DyeColor;
import org.bukkit.entity.*;

/**
 * Alien is a broad, subjective term. It can be applied as a noun or an adjective for any entity, object, place or
 * practice which is not familiar. When referring to entities, it is used for sentient and non-sentient organic
 * creatures, as well as robots.
 * <p>
 * Data storage class for TARDIS mobs.
 *
 * @author eccentric_nz
 */
public class TARDISMob {

    private EntityType type;
    //    private Cat.Type catType; // should be moved to TARDISCat.java
//    private Wolf.Variant wolfType;
    private int age;
    private double health;
    private boolean baby;
    private boolean tamed;
    private DyeColor colour; // used by sheep, cat and wolf collars
    private String name;

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public boolean isBaby() {
        return baby;
    }

    public void setBaby(boolean baby) {
        this.baby = baby;
    }

    public boolean isTamed() {
        return tamed;
    }

    public void setTamed(boolean tamed) {
        this.tamed = tamed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
