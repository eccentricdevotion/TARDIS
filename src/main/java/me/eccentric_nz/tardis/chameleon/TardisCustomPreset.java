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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.files.TardisFileCopier;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;

/**
 * A chameleon conversion is a repair procedure that technicians perform on tardis chameleon circuits. The Fourth Doctor
 * once said that the reason the tardis' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TardisCustomPreset {

    private final EnumMap<CardinalDirection, TardisChameleonColumn> blueprint = new EnumMap<>(CardinalDirection.class);
    private final EnumMap<CardinalDirection, TardisChameleonColumn> stained = new EnumMap<>(CardinalDirection.class);
    private final EnumMap<CardinalDirection, TardisChameleonColumn> glass = new EnumMap<>(CardinalDirection.class);
    private String firstLine;
    private String secondLine;

    public void makePresets() {
        // get the custom preset file and read the contents
        // ignore lines that start with a #
        String[] custom_data = new String[6];
        File custom_file;
        if (!TardisPlugin.plugin.getConfig().getBoolean("conversions.custom_preset")) {
            custom_file = TardisFileCopier.copy(TardisPlugin.plugin.getDataFolder() + File.separator + "custom_preset.txt", TardisPlugin.plugin.getResource("custom_preset.txt"), true);
            TardisPlugin.plugin.getConfig().set("conversions.custom_preset", true);
            TardisPlugin.plugin.saveConfig();
        } else {
            custom_file = TardisPlugin.plugin.getTardisCopier().copy("custom_preset.txt");
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
            TardisPlugin.plugin.getConsole().sendMessage(TardisPlugin.plugin.getPluginName() + "Could not read custom preset file! " + io.getMessage());
        } finally {
            if (bufRdr != null) {
                try {
                    bufRdr.close();
                } catch (IOException e) {
                    TardisPlugin.plugin.debug("Error closing custom preset reader! " + e.getMessage());
                }
            }
        }
        boolean asymmetric;
        if (custom_data[5] != null && !custom_data[5].isEmpty()) {
            asymmetric = Boolean.parseBoolean(custom_data[5]);
        } else {
            // assume true if not set
            asymmetric = true;
        }
        for (CardinalDirection d : CardinalDirection.values()) {
            blueprint.put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, custom_data[0], asymmetric, false));
            stained.put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, custom_data[1], asymmetric, false));
            glass.put(d, TardisChameleonPreset.buildTardisChameleonColumn(d, custom_data[2], asymmetric, false));
        }
        if (custom_data[3] != null && !custom_data[3].isEmpty()) {
            firstLine = custom_data[3];
            secondLine = custom_data[4];
        } else {
            firstLine = "CUSTOM TEXT";
            secondLine = "GOES HERE";
        }
    }

    public EnumMap<CardinalDirection, TardisChameleonColumn> getBlueprint() {
        return blueprint;
    }

    public EnumMap<CardinalDirection, TardisChameleonColumn> getStained() {
        return stained;
    }

    public EnumMap<CardinalDirection, TardisChameleonColumn> getGlass() {
        return glass;
    }

    public String getFirstLine() {
        return firstLine;
    }

    public String getSecondLine() {
        return secondLine;
    }
}
