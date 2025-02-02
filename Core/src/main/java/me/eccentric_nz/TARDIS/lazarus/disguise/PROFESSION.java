/*
 * Copyright (C) 2025 eccentric_nz
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

import net.minecraft.world.entity.npc.VillagerProfession;
import org.bukkit.entity.Villager;

public enum PROFESSION {

    NONE(VillagerProfession.NONE), // NONE
    ARMORER(VillagerProfession.ARMORER), // ARMORER
    BUTCHER(VillagerProfession.BUTCHER), // BUTCHER
    CARTOGRAPHER(VillagerProfession.CARTOGRAPHER), // CARTOGRAPHER
    CLERIC(VillagerProfession.CLERIC), // CLERIC
    FARMER(VillagerProfession.FARMER), // FARMER
    FISHERMAN(VillagerProfession.FISHERMAN), // FISHERMAN
    FLETCHER(VillagerProfession.FLETCHER), // FLETCHER
    LEATHERWORKER(VillagerProfession.LEATHERWORKER), // LEATHERWORKER
    LIBRARIAN(VillagerProfession.LIBRARIAN), // LIBRARIAN
    MASON(VillagerProfession.MASON), // MASON
    NITWIT(VillagerProfession.NITWIT), // NITWIT
    SHEPHERD(VillagerProfession.SHEPHERD), // SHEPHERD
    TOOLSMITH(VillagerProfession.TOOLSMITH), // TOOLSMITH
    WEAPONSMITH(VillagerProfession.WEAPONSMITH); // WEAPONSMITH

    private final VillagerProfession nms;

    PROFESSION(VillagerProfession nmsProfession) {
        nms = nmsProfession;
    }

    public static PROFESSION getFromVillagerProfession(Villager.Profession profession) {
        return PROFESSION.valueOf(profession.toString());
    }

    public VillagerProfession getNmsProfession() {
        return nms;
    }
}
