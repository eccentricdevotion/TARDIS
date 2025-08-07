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
import org.bukkit.entity.Parrot;

/**
 * @author eccentric_nz
 */
public class TARDISParrot extends TARDISPet {

    public TARDISParrot() {
        super.setType(EntityType.PARROT);
    }

    private Parrot.Variant parrotVariant;
    private boolean onLeftShoulder;
    private boolean onRightShoulder;

    public Parrot.Variant getParrotVariant() {
        return parrotVariant;
    }

    void setParrotVariant(Parrot.Variant parrotVariant) {
        this.parrotVariant = parrotVariant;
    }

    public boolean isOnLeftShoulder() {
        return onLeftShoulder;
    }

    void setOnLeftShoulder(boolean onLeftShoulder) {
        this.onLeftShoulder = onLeftShoulder;
    }

    public boolean isOnRightShoulder() {
        return onRightShoulder;
    }

    void setOnRightShoulder(boolean onRightShoulder) {
        this.onRightShoulder = onRightShoulder;
    }
}
