/*
 * Copyright (C) 2021 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.files.TARDISFileCopier;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.logging.Level;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISCustomPreset {

    private final EnumMap<COMPASS, TARDISChameleonColumn> blueprint = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> stained = new EnumMap<>(COMPASS.class);
    private final EnumMap<COMPASS, TARDISChameleonColumn> glass = new EnumMap<>(COMPASS.class);
    private String firstLine;
    private String secondLine;

    public void makePresets() {
        // get the custom preset file and read the contents
        // ignore lines that start with a #
        String[] custom_data = new String[6];
        File custom_file;
        if (!TARDIS.plugin.getConfig().getBoolean("conversions.custom_preset")) {
            custom_file = TARDISFileCopier.copy(TARDIS.plugin.getDataFolder() + File.separator + "custom_preset.txt", TARDIS.plugin.getResource("custom_preset.txt"), true);
            TARDIS.plugin.getConfig().set("conversions.custom_preset", true);
            TARDIS.plugin.saveConfig();
        } else {
            custom_file = TARDIS.plugin.getTardisCopier().copy("custom_preset.txt");
        }
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
            Bukkit.getLogger().log(Level.WARNING, "Could not read custom preset file! " + io.getMessage());
        } finally {
            if (bufRdr != null) {
                try {
                    bufRdr.close();
                } catch (IOException e) {
                    TARDIS.plugin.debug("Error closing custom preset reader! " + e.getMessage());
                }
            }
        }
        for (COMPASS d : COMPASS.values()) {
            blueprint.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, custom_data[0]));
            stained.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, custom_data[1]));
            glass.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, custom_data[2]));
        }
        if (custom_data[3] != null && !custom_data[3].isEmpty()) {
            firstLine = custom_data[3];
            secondLine = custom_data[4];
        } else {
            firstLine = "CUSTOM TEXT";
            secondLine = "GOES HERE";
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
