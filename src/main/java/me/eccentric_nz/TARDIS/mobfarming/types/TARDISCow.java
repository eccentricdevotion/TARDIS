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
package me.eccentric_nz.TARDIS.mobfarming.types;

import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;

/**
 * Alien is a broad, subjective term. It can be applied as a noun or an adjective for any entity, object, place or
 * practice which is not familiar. When referring to entities, it is used for sentient and non-sentient organic
 * creatures, as well as robots.
 * <p>
 * Data storage class for TARDIS cow.
 *
 * @author eccentric_nz
 */
public class TARDISCow extends TARDISMob {

    private Cow.Variant cowVariant;

    public TARDISCow() {
        super.setType(EntityType.COW);
    }

    public Cow.Variant getCowVariant() {
        return cowVariant;
    }

    public void setCowVariant(Cow.Variant cowVariant) {
        this.cowVariant = cowVariant;
    }
}
