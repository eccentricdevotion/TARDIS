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

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISExtremeHillsPreset extends TARDISPreset {

    private final String blueprint_id = "[[3,3,2,0],[3,1,1,0],[3,1,0,0],[13,3,2,0],[13,3,0,0],[3,3,2,18],[3,17,17,18],[193,193,2,18],[0,0,1,0],[0,0,0,0]]";
    private final String blueprint_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,5],[0,1,1,5],[0,9,0,5],[0,0,0,0],[0,0,0,0]]";
    private final String stained_id = "[[95,95,95,0],[95,95,95,0],[95,95,0,0],[95,95,95,0],[95,95,0,0],[95,95,95,95],[95,95,95,95],[193,193,95,95],[0,0,95,0],[0,0,0,0]]";
    private final String stained_data = "[[12,12,13,0],[12,8,8,0],[12,8,0,0],[8,12,13,0],[8,12,0,0],[12,12,13,13],[12,12,12,13],[0,9,13,13],[0,0,8,0],[0,0,0,0]]";
    private final String glass_id = "[[20,20,20,0],[20,20,20,0],[20,20,0,0],[20,20,20,0],[20,20,0,0],[20,20,20,20],[20,20,20,20],[193,193,20,20],[0,0,20,0],[0,0,0,0]]";
    private final String glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,9,0,0],[0,0,0,0],[0,0,0,0]]";

    public TARDISExtremeHillsPreset() {
        setBlueprint_id(blueprint_id);
        setStained_id(stained_id);
        setGlass_id(glass_id);
    }
}
