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
package me.eccentric_nz.TARDIS.travel;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
//import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * Alien is a broad, subjective term. It can be applied as a noun or an
 * adjective for any entity, object, place or practice which is not familiar.
 * When referring to entities, it is used for sentient and non-sentient organic
 * creatures, as well as robots.
 *
 * @author eccentric_nz
 */
public class TARDISHorse {

    private EntityType type;
    private int age;
    private double health;
    private boolean baby;
    private boolean tamed;
    private String name;
    private Color horsecolor;
    private Style horsestyle;
    private Variant horsevariant;
    private ItemStack[] horseinventory;
    private boolean haschest;
    private boolean leashed;
    private int domesticity;
    private double jumpstrength;
    private double speed;

    /**
     * Data storage class for TARDIS mobs.
     */
    public TARDISHorse() {
    }

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

    public Color getHorseColour() {
        return horsecolor;
    }

    public void setHorseColour(Color horsecolor) {
        this.horsecolor = horsecolor;
    }

    public Style getHorseStyle() {
        return horsestyle;
    }

    public void setHorseStyle(Style horsestyle) {
        this.horsestyle = horsestyle;
    }

    public Variant getHorseVariant() {
        return horsevariant;
    }

    public void setHorseVariant(Variant horsevariant) {
        this.horsevariant = horsevariant;
    }

    public ItemStack[] getHorseinventory() {
        return horseinventory;
    }

    public void setHorseInventory(ItemStack[] horseinventory) {
        this.horseinventory = horseinventory;
    }

    public boolean hasChest() {
        return haschest;
    }

    public void setHasChest(boolean haschest) {
        this.haschest = haschest;
    }

    public boolean isLeashed() {
        return leashed;
    }

    public void setLeashed(boolean leashed) {
        this.leashed = leashed;
    }

    public int getDomesticity() {
        return domesticity;
    }

    public void setDomesticity(int domesticity) {
        this.domesticity = domesticity;
    }

    public double getJumpStrength() {
        return jumpstrength;
    }

    public void setJumpStrength(double jumpstrength) {
        this.jumpstrength = jumpstrength;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
