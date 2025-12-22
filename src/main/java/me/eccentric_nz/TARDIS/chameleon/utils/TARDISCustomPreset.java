/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Material;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * A chameleon conversion is a repair procedure that technicians perform on TARDIS chameleon circuits. The Fourth Doctor
 * once said that the reason the TARDIS' chameleon circuit was stuck was because he had "borrowed" it from Gallifrey
 * before the chameleon conversion was completed.
 *
 * @author eccentric_nz
 */
public class TARDISCustomPreset {

    public static HashMap<String, CustomPreset> CUSTOM_PRESETS = new HashMap<>();

    public void makePresets() {
        // get the custom presets file and read the contents to json
        File file = new File(TARDIS.plugin.getDataFolder() + File.separator + "custom_presets.json");
        try (FileReader reader = new FileReader(file)) {
            JsonObject rootObject = JsonParser.parseReader(new JsonReader(reader)).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : rootObject.entrySet()) {
                TARDIS.plugin.debug("Adding custom chameleon preset: " + entry.getKey());
                EnumMap<COMPASS, TARDISChameleonColumn> blueprint = new EnumMap<>(COMPASS.class);
                EnumMap<COMPASS, TARDISChameleonColumn> stained = new EnumMap<>(COMPASS.class);
                EnumMap<COMPASS, TARDISChameleonColumn> glass = new EnumMap<>(COMPASS.class);
                List<String> lines = new ArrayList<>();
                JsonObject custom = entry.getValue().getAsJsonObject();
                JsonArray b = custom.get("blueprint").getAsJsonArray();
                JsonArray s = custom.get("stained").getAsJsonArray();
                JsonArray g = custom.get("glass").getAsJsonArray();
                for (COMPASS d : COMPASS.values()) {
                    blueprint.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, b));
                    stained.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, s));
                    glass.put(d, TARDISChameleonPreset.buildTARDISChameleonColumn(d, g));
                }
                for (JsonElement l : custom.get("sign").getAsJsonArray()) {
                    lines.add(l.getAsString());
                }
                Material icon;
                try {
                    icon = Material.valueOf(custom.get("icon").getAsString());
                } catch (IllegalArgumentException e) {
                    icon = Material.ENDER_CHEST;
                }
                CUSTOM_PRESETS.put(entry.getKey(), new CustomPreset(blueprint, stained, glass, lines, icon));
            }
        } catch (IOException io) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "Could not read custom presets file! " + io.getMessage());
        }
    }
}
