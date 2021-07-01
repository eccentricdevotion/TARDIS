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

import org.bukkit.entity.Axolotl;

/**
 * The smiling axolotl is hard to beat. This little salamander, which is perpetually in a larval state
 * (hence the adorable gills), can come back from almost any beating it takes. Axolotls can recover muscle function
 * after being paralyzed. They can regrow their limbs, eyes, tails, and parts of the heart. You can induce a stroke in
 * an axolotl and part of its brain will grow back.
 * <p>
 * Data storage class for TARDIS axolotl.
 *
 * @author eccentric_nz
 */
class TardisAxolotl extends TardisMob {

    private Axolotl.Variant axolotlVariant;

    Axolotl.Variant getAxolotlVariant() {
        return axolotlVariant;
    }

    void setAxolotlVariant(Axolotl.Variant axolotl) {
        axolotlVariant = axolotl;
    }
}