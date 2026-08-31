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

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.generator.structure.Structure;

import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author eccentric_nz
 */
public class TARDISStructure {

    private static final Comparator<Structure> STRUCTURE_COMPARATOR = Comparator.comparing(structure -> {
        NamespacedKey key = RegistryAccess.registryAccess()
                .getRegistry(RegistryKey.STRUCTURE).getKey(structure);
        // Fallback to "unknown" string if the structure is dynamically generated without a key
        return key != null ? key.asString() : "unknown";
    });
    public static final TreeMap<Structure, Material> netherStructures = new TreeMap<>(STRUCTURE_COMPARATOR);
    public static final TreeMap<Structure, Material> overworldStructures = new TreeMap<>(STRUCTURE_COMPARATOR);

    static {
        netherStructures.put(Structure.BASTION_REMNANT, Material.POLISHED_BLACKSTONE_BRICKS);
        netherStructures.put(Structure.FORTRESS, Material.NETHER_BRICKS);
        netherStructures.put(Structure.NETHER_FOSSIL, Material. BONE_BLOCK);
        netherStructures.put(Structure.RUINED_PORTAL_NETHER, Material.OBSIDIAN);
        overworldStructures.put(Structure.ANCIENT_CITY, Material.DEEPSLATE_BRICKS);
        overworldStructures.put(Structure.DESERT_PYRAMID, Material.CHISELED_SANDSTONE);
        overworldStructures.put(Structure.IGLOO, Material.SNOW_BLOCK);
        overworldStructures.put(Structure.JUNGLE_PYRAMID, Material.MOSSY_COBBLESTONE);
        overworldStructures.put(Structure.MANSION, Material.DARK_OAK_LOG);
        overworldStructures.put(Structure.MINESHAFT, Material.OAK_PLANKS);
        overworldStructures.put(Structure.MINESHAFT_MESA, Material.DARK_OAK_PLANKS);
        overworldStructures.put(Structure.MONUMENT, Material.PRISMARINE);
        overworldStructures.put(Structure.OCEAN_RUIN_COLD, Material.STONE_BRICKS);
        overworldStructures.put(Structure.OCEAN_RUIN_WARM, Material.CUT_SANDSTONE);
        overworldStructures.put(Structure.PILLAGER_OUTPOST, Material.DARK_OAK_PLANKS);
        overworldStructures.put(Structure.RUINED_PORTAL, Material.CHISELED_STONE_BRICKS);
        overworldStructures.put(Structure.RUINED_PORTAL_DESERT, Material.STONE);
        overworldStructures.put(Structure.RUINED_PORTAL_JUNGLE, Material.SAND);
        overworldStructures.put(Structure.RUINED_PORTAL_SWAMP, Material.CRYING_OBSIDIAN);
        overworldStructures.put(Structure.RUINED_PORTAL_MOUNTAIN, Material.NETHERRACK);
        overworldStructures.put(Structure.RUINED_PORTAL_OCEAN, Material.MAGMA_BLOCK);
        overworldStructures.put(Structure.SHIPWRECK, Material.OAK_PLANKS);
        overworldStructures.put(Structure.SHIPWRECK_BEACHED, Material.SPRUCE_PLANKS);
        overworldStructures.put(Structure.STRONGHOLD, Material.CRACKED_STONE_BRICKS);
        overworldStructures.put(Structure.SWAMP_HUT, Material.OAK_LOG);
        overworldStructures.put(Structure.TRAIL_RUINS, Material.LIGHT_GRAY_TERRACOTTA);
        overworldStructures.put(Structure.TRIAL_CHAMBERS, Material.OXIDIZED_CUT_COPPER);
        overworldStructures.put(Structure.VILLAGE_DESERT, Material.SANDSTONE);
        overworldStructures.put(Structure.VILLAGE_PLAINS, Material.COBBLESTONE);
        overworldStructures.put(Structure.VILLAGE_SAVANNA, Material.ACACIA_PLANKS);
        overworldStructures.put(Structure.VILLAGE_SNOWY, Material.ICE);
        overworldStructures.put(Structure.VILLAGE_TAIGA, Material.SPRUCE_LOG);
    }

    public static Structure getRandom(Location current) {
        Structure structure;
        switch (current.getWorld().getEnvironment()) {
            case NETHER -> structure = getRandomKey(netherStructures);
            case THE_END -> structure = Structure.END_CITY;
            // NORMAL
            default -> structure = getRandomKey(overworldStructures);
        }
        return structure;
    }

    public static <S, M> S getRandomKey(SortedMap<S, M> map) {
        int randomIndex = TARDISConstants.RANDOM.nextInt(map.size());
        Iterator<S> iterator = map.keySet().iterator();
        // Advance the iterator to the random index
        S key = null;
        for (int i = 0; i <= randomIndex; i++) {
            key = iterator.next();
        }
        return key;
    }
}
