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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;

/**
 * The Junk TARDIS is a makeshift TARDIS created by the Eleventh Doctor and
 * Idris (the embodied matrix of his TARDIS).
 *
 * They built it from bits of TARDISes in the Bubble universe's junkyard, a task
 * which Idris called "impossible". This TARDIS consisted solely of a console
 * (complete with time rotor) and only three walls. Because it lacked a proper
 * shell, the Doctor said it would be very dangerous to fly.
 *
 * @author eccentric_nz
 */
public class TARDISJunkPreset extends TARDISPreset {

    private final String blueprint_id = "[[35,98,35,98],[35,98,35,98],[35,35,35,35],[35,35,35,35],[35,35,35,35],[44,0,68,139],[159,44,69,35],[44,0,0,139],[35,0,0,0],[0,0,0,0]]";
    private final String blueprint_data = "[[8,0,1,0],[8,0,1,0],[8,1,1,1],[8,1,1,1],[8,1,1,1],[0,0,4,0],[14,8,5,8],[0,0,0,0],[8,0,0,0],[0,0,0,0]]";
    private final String stained_id = "[[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,0,68,95],[95,95,95,95],[95,0,0,95],[95,0,0,0],[0,0,0,0]]";
    private final String stained_data = "[[8,8,1,8],[8,8,1,8],[8,1,1,1],[8,1,1,1],[8,1,1,1],[8,0,4,8],[14,8,8,8],[8,0,0,8],[8,0,0,0],[0,0,0,0]]";
    private final String glass_id = "[[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,0,68,20],[20,20,20,20],[20,0,0,20],[20,0,0,0],[0,0,0,0]]";
    private final String glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,4,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]";
    private final String west_blueprint_id = "[[35,35,35,35],[44,0,68,139],[159,44,69,35],[44,0,0,139],[35,98,35,98],[35,98,35,98],[35,35,35,35],[35,35,35,35],[35,0,0,0],[0,0,0,0]]";
    private final String west_blueprint_data = "[[8,1,1,1],[0,0,5,0],[14,8,5,8],[0,0,0,0],[8,0,1,0],[8,0,1,0],[8,1,1,1],[8,1,1,1],[8,0,0,0],[0,0,0,0]]";
    private final String west_stained_id = "[[95,95,95,95],[95,0,68,95],[95,95,95,95],[95,0,0,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,95,95,95],[95,0,0,0],[0,0,0,0]]";
    private final String west_stained_data = "[[8,1,1,1],[8,0,5,8],[14,8,8,8],[8,0,0,8],[8,8,1,8],[8,8,1,8],[8,1,1,1],[8,1,1,1],[8,0,0,0],[0,0,0,0]]";
    private final String west_glass_id = "[[20,20,20,20],[20,0,68,20],[20,20,20,20],[20,0,0,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,20,20,20],[20,0,0,0],[0,0,0,0]]";
    private final String west_glass_data = "[[0,0,0,0],[0,0,5,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0]]";

    public TARDISJunkPreset() {
        setBlueprint_id(blueprint_id);
        setBlueprint_data(blueprint_data);
        setStained_id(stained_id);
        setStained_data(stained_data);
        setGlass_id(glass_id);
        setGlass_data(glass_data);
    }

    @Override
    public void makePresets(boolean assymetric, boolean duck, boolean torch) {
        for (COMPASS d : COMPASS.values()) {
            if (d.equals(COMPASS.WEST)) {
                getBlueprint().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, west_blueprint_id, west_blueprint_data, false, false, false));
                getStained().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, west_stained_id, west_stained_data, false, false, false));
                getGlass().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, west_glass_id, west_glass_data, false, false, false));
            } else {
                getBlueprint().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, blueprint_id, blueprint_data, false, false, false));
                getStained().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, stained_id, stained_data, false, false, false));
                getGlass().put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, glass_id, glass_data, false, false, false));
            }
        }
    }
}
