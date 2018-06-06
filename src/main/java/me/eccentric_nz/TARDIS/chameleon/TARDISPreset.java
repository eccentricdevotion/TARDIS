/*
 * Copyright (C) 2018 eccentric_nz
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

import java.util.EnumMap;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISPreset {

    private String blueprint_id;
    private String stained_id;
    private String glass_id;
    private final EnumMap<COMPASS, TARDISChameleonColumn> blueprint = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> stained = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> glass = new EnumMap<>(COMPASS.class);

    public TARDISPreset() {
    }

    public void makePresets(boolean assymetric, boolean duck) {
        for (COMPASS d : COMPASS.values()) {
            blueprint.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, blueprint_id, assymetric, duck));
            stained.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, stained_id, assymetric, duck));
            glass.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, glass_id, assymetric, duck));
        }
    }

    public EnumMap<COMPASS, TARDISChameleonColumn> getBlueprint() {
        return blueprint;
    }

    public EnumMap<COMPASS, TARDISChameleonColumn> getStained() {
        return stained;
    }

    public EnumMap<COMPASS, TARDISChameleonColumn> getGlass() {
        return glass;
    }

    public String getBlueprint_id() {
        return blueprint_id;
    }

    public void setBlueprint_id(String blueprint_id) {
        this.blueprint_id = blueprint_id;
    }

    public String getStained_id() {
        return stained_id;
    }

    public void setStained_id(String stained_id) {
        this.stained_id = stained_id;
    }

    public String getGlass_id() {
        return glass_id;
    }

    public void setGlass_id(String glass_id) {
        this.glass_id = glass_id;
    }
}
