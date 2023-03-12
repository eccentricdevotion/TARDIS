/*
 * Copyright (C) 2020 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischunkgenerator.disguise;

import net.minecraft.world.entity.animal.MushroomCow;

public enum MUSHROOM_COW {

    BROWN(MushroomCow.MushroomType.BROWN), // BROWN
    RED(MushroomCow.MushroomType.RED); // RED

    private final MushroomCow.MushroomType nmsType;

    MUSHROOM_COW(MushroomCow.MushroomType nmsType) {
        this.nmsType = nmsType;
    }

    public static MUSHROOM_COW getFromMushroomCowType(org.bukkit.entity.MushroomCow.Variant variant) {
        return MUSHROOM_COW.valueOf(variant.toString());
    }

    public MushroomCow.MushroomType getNmsType() {
        return nmsType;
    }
}
