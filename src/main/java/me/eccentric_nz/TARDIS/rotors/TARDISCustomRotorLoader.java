/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.rotors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.RotorVariant;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Locale;

/**
 * @author eccentric_nz
 */
public class TARDISCustomRotorLoader {

    private final TARDIS plugin;

    public TARDISCustomRotorLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    //    new Rotor(String name, int offModelData, Material material, int[] frames, long frameTick, boolean custom)
    public void addRotors() {
        // create plugin rotors
        Rotor early = new Rotor("early", RotorVariant.TIME_ROTOR_EARLY_OFF.getKey(), Material.BLACK_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 6, false);
        Rotor console = new Rotor("console", RotorVariant.TIME_ROTOR_CONSOLE_OFF.getKey(), Material.RED_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 2, false);
        Rotor delta = new Rotor("delta", RotorVariant.TIME_ROTOR_DELTA_OFF.getKey(), Material.CYAN_DYE, new int[]{0, 1, 2, 3, 4, 5}, 4, false);
        Rotor eleventh = new Rotor("eleventh", RotorVariant.TIME_ROTOR_ELEVENTH_OFF.getKey(), Material.BROWN_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 2, false);
        Rotor engine = new Rotor("engine", RotorVariant.ENGINE_OFF.getKey(), Material.LIGHT_BLUE_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, 2, false);
        Rotor engine_rotor = new Rotor("engine_rotor", RotorVariant.ENGINE_ROTOR_OFF.getKey(), Material.BLUE_DYE, new int[]{0, 1, 2, 3, 4, 5, 4, 3, 2, 1}, 3, false);
        Rotor hospital = new Rotor("hospital", RotorVariant.HOSPITAL_OFF.getKey(), Material.WHITE_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, 2, false);
        Rotor twelfth = new Rotor("twelfth", RotorVariant.TIME_ROTOR_TWELFTH_OFF.getKey(), Material.GRAY_DYE, new int[]{0}, 1, false);
        Rotor rustic = new Rotor("rustic", RotorVariant.TIME_ROTOR_RUSTIC_OFF.getKey(), Material.GREEN_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 2, false);
        Rotor tennant = new Rotor("tennant", RotorVariant.TIME_ROTOR_TENNANT_OFF.getKey(), Material.ORANGE_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 3, false);
        // add plugin rotors to look-ups
        Rotor.byMaterial.put(Material.BLACK_DYE, early);
        Rotor.byCustomModel.put(RotorVariant.TIME_ROTOR_EARLY_OFF.getKey(), early);
        Rotor.byName.put("TIME_ROTOR_EARLY", early);
        Rotor.byMaterial.put(Material.ORANGE_DYE, tennant);
        Rotor.byCustomModel.put(RotorVariant.TIME_ROTOR_TENNANT_OFF.getKey(), tennant);
        Rotor.byName.put("TIME_ROTOR_TENNANT", tennant);
        Rotor.byMaterial.put(Material.RED_DYE, console);
        Rotor.byCustomModel.put(RotorVariant.TIME_ROTOR_CONSOLE_OFF.getKey(), console);
        Rotor.byName.put("TIME_ROTOR_CONSOLE", console);
        Rotor.byMaterial.put(Material.GREEN_DYE, rustic);
        Rotor.byCustomModel.put(RotorVariant.TIME_ROTOR_RUSTIC_OFF.getKey(), rustic);
        Rotor.byName.put("TIME_ROTOR_RUSTIC", rustic);
        Rotor.byMaterial.put(Material.BROWN_DYE, eleventh);
        Rotor.byCustomModel.put(RotorVariant.TIME_ROTOR_ELEVENTH_OFF.getKey(), eleventh);
        Rotor.byName.put("TIME_ROTOR_ELEVENTH", eleventh);
        Rotor.byMaterial.put(Material.GRAY_DYE, twelfth);
        Rotor.byCustomModel.put(RotorVariant.TIME_ROTOR_TWELFTH_OFF.getKey(), twelfth);
        Rotor.byName.put("TIME_ROTOR_TWELFTH", twelfth);
        Rotor.byMaterial.put(Material.CYAN_DYE, delta);
        Rotor.byCustomModel.put(RotorVariant.TIME_ROTOR_DELTA_OFF.getKey(), delta);
        Rotor.byName.put("TIME_ROTOR_DELTA", delta);
        Rotor.byMaterial.put(Material.LIGHT_BLUE_DYE, engine);
        Rotor.byCustomModel.put(RotorVariant.ENGINE_OFF.getKey(), engine);
        Rotor.byName.put("TIME_ENGINE", engine);
        Rotor.byMaterial.put(Material.BLUE_DYE, engine_rotor);
        Rotor.byCustomModel.put(RotorVariant.ENGINE_ROTOR_OFF.getKey(), engine_rotor);
        Rotor.byName.put("TIME_ROTOR_ENGINE", engine_rotor);
        Rotor.byMaterial.put(Material.WHITE_DYE, hospital);
        Rotor.byCustomModel.put(RotorVariant.HOSPITAL_OFF.getKey(), hospital);
        Rotor.byName.put("TIME_ROTOR_HOSPITAL", hospital);
        for (String r : plugin.getCustomRotorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomRotorsConfig().getString(r + ".animated_material"));
                int[] frames = getFrames(plugin.getCustomRotorsConfig().getString(r + ".animation_sequence", "0,0"));
                NamespacedKey key = new NamespacedKey(plugin, "time_rotor_" + TARDISStringUtils.toDashedLowercase(r) + "_off");
                Rotor column = new Rotor(r, key, material, frames, plugin.getCustomRotorsConfig().getInt(r + ".frame_rate"), true);
                // add the rotor to look-ups
                Rotor.byMaterial.put(material, column);
                Rotor.byCustomModel.put(key, column);
                Rotor.byName.put("TIME_ROTOR_" + r.toUpperCase(Locale.ROOT), column);
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom rotor item material for " + r + "!");
            }
        }
    }

    private int[] getFrames(String sequence) {
        String[] split = sequence.split(",");
        int[] frames = new int[split.length];
        for (int n = 0; n < split.length; n++) {
            frames[n] = TARDISNumberParsers.parseInt(split[n]);
        }
        return frames;
    }
}
