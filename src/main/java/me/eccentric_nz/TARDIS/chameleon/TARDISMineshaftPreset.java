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
public class TARDISMineshaftPreset {

    private final String blueprint_id = "[[85,85,5,0],[0,30,50,0],[85,85,5,0],[66,0,5,0],[85,85,5,0],[0,0,50,0],[85,85,5,0],[66,0,5,0],[66,0,96,0],[0,0,68,0]]";
    private final String blueprint_data = "[[0,0,0,0],[0,0,2,0],[0,0,0,0],[1,0,0,0],[0,0,0,0],[0,0,2,0],[0,0,0,0],[1,0,0,0],[1,0,2,0],[0,0,4,0]]";
    private final String stained_id = "[[95,95,95,0],[0,95,0,0],[95,95,95,0],[0,0,95,0],[95,95,95,0],[0,0,0,0],[95,95,95,0],[0,0,95,0],[0,0,96,0],[0,0,68,0]]";
    private final String stained_data = "[[12,12,12,0],[0,0,0,0],[12,12,12,0],[0,0,12,0],[12,12,12,0],[0,0,0,0],[12,12,12,0],[0,0,12,0],[0,0,2,0],[0,0,4,0]]";
    private final String glass_id = "[[20,20,20,0],[0,20,0,0],[20,20,20,0],[0,0,20,0],[20,20,20,0],[0,0,0,0],[20,20,20,0],[0,0,20,0],[0,0,96,0],[0,0,68,0]]";
    private final String glass_data = "[[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,2,0],[0,0,4,0]]";
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> blueprint = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> stained = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);
    private final EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn> glass = new EnumMap<TARDISConstants.COMPASS, TARDISChameleonColumn>(TARDISConstants.COMPASS.class);

    public TARDISMineshaftPreset() {
    }

    public void makePresets() {
        TARDISChameleonPreset tcp = new TARDISChameleonPreset();
        for (TARDISConstants.COMPASS d : TARDISConstants.COMPASS.values()) {
            blueprint.put(d, tcp.buildTARDISChameleonColumn(d, blueprint_id, blueprint_data, false));
            glass.put(d, tcp.buildTARDISChameleonColumn(d, glass_id, glass_data, false));
            stained.put(d, tcp.buildTARDISChameleonColumn(d, stained_id, stained_data, false));
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
}
