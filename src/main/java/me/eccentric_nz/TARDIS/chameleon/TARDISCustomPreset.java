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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;

/**
 * A chameleon conversion is a repair procedure that technicians perform on
 * TARDIS chameleon circuits. The Fourth Doctor once said that the reason the
 * TARDIS' chameleon circuit was stuck was because he had "borrowed" it from
 * Gallifrey before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISCustomPreset {

    private final EnumMap<COMPASS, TARDISChameleonColumn> blueprint = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> stained = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> glass = new EnumMap<>(COMPASS.class);
    String firstLine;
    String secondLine;

    public TARDISCustomPreset() {
    }

    public void makePresets() {
        // get the custom preset file and read the contents
        // ignore lines that start with a #
        String[] custom_data = new String[9];
        File custom_file = TARDIS.plugin.getTardisCopier().copy("custom_preset.txt");
        BufferedReader bufRdr = null;
        int i = 0;
        try {
            bufRdr = new BufferedReader(new FileReader(custom_file));
            String line;
            // read each line of text file
            while ((line = bufRdr.readLine()) != null) {
                if (!line.startsWith("#")) {
                    custom_data[i] = line;
                    i++;
                }
            }
        } catch (IOException io) {
            TARDIS.plugin.getConsole().sendMessage(TARDIS.plugin.getPluginName() + "Could not read custom preset file! " + io.getMessage());
        } finally {
            if (bufRdr != null) {
                try {
                    bufRdr.close();
                } catch (IOException e) {
                    TARDIS.plugin.debug("Error closing custom preset reader! " + e.getMessage());
                }
            }
        }
        boolean asymmetric;
        if (custom_data[5] != null && !custom_data[5].isEmpty()) {
            asymmetric = Boolean.valueOf(custom_data[5]);
        } else {
            // assume true if not set
            asymmetric = true;
        }
        for (COMPASS d : COMPASS.values()) {
            blueprint.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, custom_data[0], asymmetric, false));
            stained.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, custom_data[1], asymmetric, false));
            glass.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, custom_data[2], asymmetric, false));
        }
        if (custom_data[3] != null && !custom_data[3].isEmpty()) {
            this.firstLine = custom_data[3];
            this.secondLine = custom_data[4];
        } else {
            this.firstLine = "CUSTOM TEXT";
            this.secondLine = "GOES HERE";
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

    public String getFirstLine() {
        return firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }
}
