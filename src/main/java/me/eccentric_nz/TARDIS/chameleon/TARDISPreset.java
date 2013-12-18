/*
 * Copyright (C) 2013 eccentric_nz
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
import me.eccentric_nz.TARDIS.TARDISConstants;

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISPreset {

    private String blueprint_id;
    private String blueprint_data;
    private String stained_id;
    private String stained_data;
    private String glass_id;
    private String glass_data;
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> blueprint = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> stained = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);

    public TARDISPreset() {
    }

    public void makePresets() {
        TARDISChameleonPreset tcp = new TARDISChameleonPreset();
        for (TARDISConstants.COMPASS d : TARDISConstants.COMPASS.values()) {
            blueprint.put(d, tcp.buildTARDISChameleonColumn(d, blueprint_id, blueprint_data, true));
            stained.put(d, tcp.buildTARDISChameleonColumn(d, stained_id, stained_data, true));
            glass.put(d, tcp.buildTARDISChameleonColumn(d, glass_id, glass_data, true));
        }
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getBlueprint() {
        return blueprint;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getStained() {
        return stained;
    }

    public EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> getGlass() {
        return glass;
    }

    public String getBlueprint_id() {
        return blueprint_id;
    }

    public void setBlueprint_id(String blueprint_id) {
        this.blueprint_id = blueprint_id;
    }

    public String getBlueprint_data() {
        return blueprint_data;
    }

    public void setBlueprint_data(String blueprint_data) {
        this.blueprint_data = blueprint_data;
    }

    public String getStained_id() {
        return stained_id;
    }

    public void setStained_id(String stained_id) {
        this.stained_id = stained_id;
    }

    public String getStained_data() {
        return stained_data;
    }

    public void setStained_data(String stained_data) {
        this.stained_data = stained_data;
    }

    public String getGlass_id() {
        return glass_id;
    }

    public void setGlass_id(String glass_id) {
        this.glass_id = glass_id;
    }

    public String getGlass_data() {
        return glass_data;
    }

    public void setGlass_data(String glass_data) {
        this.glass_data = glass_data;
    }
}
