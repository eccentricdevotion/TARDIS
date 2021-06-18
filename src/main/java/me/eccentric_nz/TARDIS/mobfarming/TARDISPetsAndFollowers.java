/*
 * Copyright (C) 2021 eccentric_nz
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

import java.util.List;

public class TardisPetsAndFollowers {

    private final List<TardisPet> pets;
    private final List<TardisFollower> followers;

    TardisPetsAndFollowers(List<TardisPet> pets, List<TardisFollower> followers) {
        this.pets = pets;
        this.followers = followers;
    }

    public List<TardisPet> getPets() {
        return pets;
    }

    public List<TardisFollower> getFollowers() {
        return followers;
    }
}
