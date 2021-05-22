/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardis.mobfarming;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Style;
import org.bukkit.inventory.ItemStack;

/**
 * Alien is a broad, subjective term. It can be applied as a noun or an adjective for any entity, object, place or
 * practice which is not familiar. When referring to entities, it is used for sentient and non-sentient organic
 * creatures, as well as robots.
 *
 * @author eccentric_nz
 */
public class TARDISHorse extends TARDISMob {

	private Color horsecolor;
	private Style horsestyle;
	private EntityType horsevariant;
	private ItemStack[] horseinventory;
	private boolean haschest;
	private boolean leashed;
	private int domesticity;
	private double jumpstrength;
	private double speed;
	private double horseHealth;

	/**
	 * Data storage class for tardis mobs.
	 */
	public TARDISHorse() {
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

	public EntityType getHorseVariant() {
		return horsevariant;
	}

	public void setHorseVariant(EntityType horsevariant) {
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

	public double getHorseHealth() {
		return horseHealth;
	}

	public void setHorseHealth(double horseHealth) {
		this.horseHealth = horseHealth;
	}
}
