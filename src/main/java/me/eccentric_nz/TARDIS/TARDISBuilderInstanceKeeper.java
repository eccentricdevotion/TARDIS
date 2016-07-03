/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.chameleon.TARDISStainedGlassLookup;
import org.bukkit.Material;

/**
 * Keeps track of various building related lookups. these include: Room block
 * counts, Room seed blocks and Stained Glass block colour equivalents for
 * regular blocks.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInstanceKeeper {

    private final HashMap<String, HashMap<String, Integer>> roomBlockCounts = new HashMap<String, HashMap<String, Integer>>();
    private final TARDISStainedGlassLookup stainedGlassLookup = new TARDISStainedGlassLookup();
    private HashMap<Material, String> seeds;
    private static final List<Material> precious = new ArrayList<Material>();

    static {
        precious.add(Material.BEACON);
        precious.add(Material.DIAMOND_BLOCK);
        precious.add(Material.EMERALD_BLOCK);
        precious.add(Material.GOLD_BLOCK);
        precious.add(Material.IRON_BLOCK);
        precious.add(Material.REDSTONE_BLOCK);
        precious.add(Material.BEDROCK);
    }

    public HashMap<String, HashMap<String, Integer>> getRoomBlockCounts() {
        return roomBlockCounts;
    }

    public TARDISStainedGlassLookup getStainedGlassLookup() {
        return stainedGlassLookup;
    }

    public HashMap<Material, String> getSeeds() {
        return seeds;
    }

    public void setSeeds(HashMap<Material, String> t_seeds) {
        seeds = t_seeds;
    }

    public static List<Material> getPrecious() {
        return precious;
    }
}
