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
package me.eccentric_nz.TARDIS.mobfarming;

/**
 * According to the Fourth Doctor, bees are "insects with stings on their tails", and a non-sentient species found on
 * Earth. They are domesticated by humans for the production of honey, and even an average human like Donna Noble knows
 * that they experienced a profound global downturn in their population during the early 21st century.
 */
public class TARDISBee extends TARDISMob {

    private boolean nectar;
    private boolean stung;
    private int anger;

    /**
     * Data storage class for TARDIS bee.
     */
    TARDISBee() {
    }

    boolean hasNectar() {
        return nectar;
    }

    void setHasNectar(boolean nectar) {
        this.nectar = nectar;
    }

    boolean hasStung() {
        return stung;
    }

    void setHasStung(boolean stung) {
        this.stung = stung;
    }

    public int getAnger() {
        return anger;
    }

    public void setAnger(int anger) {
        this.anger = anger;
    }
}
