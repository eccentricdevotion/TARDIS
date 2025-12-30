/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.lazarus.disguise;

import net.minecraft.world.entity.animal.panda.Panda;

public enum GENE {

    NORMAL(Panda.Gene.NORMAL), // NORMAL
    LAZY(Panda.Gene.LAZY), // LAZY
    WORRIED(Panda.Gene.WORRIED), // WORRIED
    PLAYFUL(Panda.Gene.PLAYFUL), // PLAYFUL
    BROWN(Panda.Gene.BROWN), // BROWN
    WEAK(Panda.Gene.WEAK), // WEAK
    AGGRESSIVE(Panda.Gene.AGGRESSIVE); // AGGRESSIVE

    private final Panda.Gene nmsGene;

    GENE(Panda.Gene nmsGene) {
        this.nmsGene = nmsGene;
    }

    public static GENE getFromPandaGene(org.bukkit.entity.Panda.Gene gene) {
        return GENE.valueOf(gene.toString());
    }

    public Panda.Gene getNmsGene() {
        return nmsGene;
    }
}
