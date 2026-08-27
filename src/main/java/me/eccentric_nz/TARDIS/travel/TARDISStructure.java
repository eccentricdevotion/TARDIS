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
package me.eccentric_nz.TARDIS.travel;

import org.bukkit.generator.structure.Structure;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISStructure {

    public static final List<Structure> netherStructures = new ArrayList<>();
    public static final List<Structure> overworldStructures = new ArrayList<>();

    static {
        netherStructures.add(Structure.BASTION_REMNANT);
        netherStructures.add(Structure.FORTRESS);
        netherStructures.add(Structure.NETHER_FOSSIL);
        netherStructures.add(Structure.RUINED_PORTAL_NETHER);
        overworldStructures.add(Structure.ANCIENT_CITY);
        overworldStructures.add(Structure.DESERT_PYRAMID);
        overworldStructures.add(Structure.IGLOO);
        overworldStructures.add(Structure.JUNGLE_PYRAMID);
        overworldStructures.add(Structure.MANSION);
        overworldStructures.add(Structure.MINESHAFT);
        overworldStructures.add(Structure.MINESHAFT_MESA);
        overworldStructures.add(Structure.MONUMENT);
        overworldStructures.add(Structure.OCEAN_RUIN_COLD);
        overworldStructures.add(Structure.OCEAN_RUIN_WARM);
        overworldStructures.add(Structure.PILLAGER_OUTPOST);
        overworldStructures.add(Structure.RUINED_PORTAL);
        overworldStructures.add(Structure.RUINED_PORTAL_DESERT);
        overworldStructures.add(Structure.RUINED_PORTAL_JUNGLE);
        overworldStructures.add(Structure.RUINED_PORTAL_SWAMP);
        overworldStructures.add(Structure.RUINED_PORTAL_MOUNTAIN);
        overworldStructures.add(Structure.RUINED_PORTAL_OCEAN);
        overworldStructures.add(Structure.SHIPWRECK);
        overworldStructures.add(Structure.SHIPWRECK_BEACHED);
        overworldStructures.add(Structure.STRONGHOLD);
        overworldStructures.add(Structure.SWAMP_HUT);
        overworldStructures.add(Structure.TRAIL_RUINS);
        overworldStructures.add(Structure.TRIAL_CHAMBERS);
        overworldStructures.add(Structure.VILLAGE_DESERT);
        overworldStructures.add(Structure.VILLAGE_PLAINS);
        overworldStructures.add(Structure.VILLAGE_SAVANNA);
        overworldStructures.add(Structure.VILLAGE_SNOWY);
        overworldStructures.add(Structure.VILLAGE_TAIGA);
    }
}
