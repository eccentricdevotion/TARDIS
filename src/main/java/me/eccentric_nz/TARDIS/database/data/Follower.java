/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.database.data;

import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import me.eccentric_nz.tardisweepingangels.utils.Monster;

import java.util.UUID;

public class Follower {

    private final UUID uuid;
    private final UUID owner;
    private final Monster species;
    private final boolean following;
    private final boolean option;
    private final OodColour colour;
    private final int ammo;

    public Follower(UUID uuid, UUID owner, Monster species, boolean following, boolean option, OodColour colour, int ammo) {
        this.uuid = uuid;
        this.owner = owner;
        this.species = species;
        this.following = following;
        this.option = option;
        this.colour = colour;
        this.ammo = ammo;
    }

    public Follower(UUID uuid, UUID owner, Monster species) {
        this.uuid = uuid;
        this.owner = owner;
        this.species = species;
        this.following = false;
        this.option = false;
        this.colour = OodColour.BLACK;
        this.ammo = 0;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getOwner() {
        return owner;
    }

    public Monster getSpecies() {
        return species;
    }

    public boolean isFollowing() {
        return following;
    }

    public boolean hasOption() {
        return option;
    }

    public OodColour getColour() {
        return colour;
    }

    public int getAmmo() {
        return ammo;
    }
}
