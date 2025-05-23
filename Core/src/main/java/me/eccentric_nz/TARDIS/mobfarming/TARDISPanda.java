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
import org.bukkit.entity.Panda;

public class TARDISPanda extends TARDISMob {

    public TARDISPanda() {
        super.setType(EntityType.PANDA);
    }

    private Panda.Gene mainGene;
    private Panda.Gene hiddenGene;

    public Panda.Gene getMainGene() {
        return mainGene;
    }

    public void setMainGene(Panda.Gene mainGene) {
        this.mainGene = mainGene;
    }

    public Panda.Gene getHiddenGene() {
        return hiddenGene;
    }

    public void setHiddenGene(Panda.Gene hiddenGene) {
        this.hiddenGene = hiddenGene;
    }
}
