/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;

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
        Rotor early = new Rotor("early", 10000002, Material.BLACK_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7}, 6, false);
        Rotor rotor = new Rotor("rotor", 10000003, Material.ORANGE_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 3, false);
        Rotor console = new Rotor("console", 10000100, Material.RED_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 2, false);
        Rotor copper = new Rotor("copper", 10000004, Material.BROWN_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 2, false);
        Rotor round = new Rotor("round", 10000005, Material.GRAY_DYE, new int[]{0}, 1, false);
        Rotor delta = new Rotor("delta", 10000006, Material.CYAN_DYE, new int[]{0, 1, 2, 3, 4, 5}, 4, false);
        Rotor engine = new Rotor("engine", 10000007, Material.LIGHT_BLUE_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12}, 2, false);
        Rotor engine_rotor = new Rotor("engine_rotor", 10000008, Material.BLUE_DYE, new int[]{0, 1, 2, 3, 4, 5, 4, 3, 2, 1}, 3, false);
        Rotor hospital = new Rotor("hospital", 10000009, Material.WHITE_DYE, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11}, 2, false);
        Rotor rustic = new Rotor("rustic", 10000101, Material.GREEN_DYE, new int[]{0, 0, 1, 2, 3, 4, 3, 2, 1}, 2, false);
        // add plugin rotors to look-ups
        Rotor.byMaterial.put(Material.BLACK_DYE, early);
        Rotor.byCustomModelData.put(10000002, early);
        Rotor.byName.put("TIME_ROTOR_EARLY", early);
        Rotor.byMaterial.put(Material.ORANGE_DYE, rotor);
        Rotor.byCustomModelData.put(10000003, rotor);
        Rotor.byName.put("TIME_ROTOR_ROTOR", rotor);
        Rotor.byMaterial.put(Material.RED_DYE, console);
        Rotor.byCustomModelData.put(10000100, console);
        Rotor.byName.put("TIME_ROTOR_CONSOLE", console);
        Rotor.byMaterial.put(Material.GREEN_DYE, rustic);
        Rotor.byCustomModelData.put(10000101, rustic);
        Rotor.byName.put("TIME_ROTOR_RUSTIC", rustic);
        Rotor.byMaterial.put(Material.BROWN_DYE, copper);
        Rotor.byCustomModelData.put(10000004, copper);
        Rotor.byName.put("TIME_ROTOR_COPPER", copper);
        Rotor.byMaterial.put(Material.GRAY_DYE, round);
        Rotor.byCustomModelData.put(10000005, round);
        Rotor.byName.put("TIME_ROTOR_ROUND", round);
        Rotor.byMaterial.put(Material.CYAN_DYE, delta);
        Rotor.byCustomModelData.put(10000006, delta);
        Rotor.byName.put("TIME_ROTOR_DELTA", delta);
        Rotor.byMaterial.put(Material.LIGHT_BLUE_DYE, engine);
        Rotor.byCustomModelData.put(10000007, engine);
        Rotor.byName.put("TIME_ENGINE", engine);
        Rotor.byMaterial.put(Material.BLUE_DYE, engine_rotor);
        Rotor.byCustomModelData.put(10000008, engine_rotor);
        Rotor.byName.put("TIME_ROTOR_ENGINE", engine_rotor);
        Rotor.byMaterial.put(Material.WHITE_DYE, hospital);
        Rotor.byCustomModelData.put(10000009, hospital);
        Rotor.byName.put("TIME_ROTOR_HOSPITAL", hospital);
        for (String r : plugin.getCustomRotorsConfig().getKeys(false)) {
            try {
                int cmd = plugin.getCustomRotorsConfig().getInt(r + ".off_custom_model_data");
                Material material = Material.valueOf(plugin.getCustomRotorsConfig().getString(r + ".animated_material"));
                int[] frames = getFrames(plugin.getCustomRotorsConfig().getString(r + ".animation_sequence", "0,0"));
                Rotor column = new Rotor(r, cmd, material, frames, plugin.getCustomRotorsConfig().getInt(r + ".frame_rate"), true);
                // add the rotor to look-ups
                Rotor.byMaterial.put(material, column);
                Rotor.byCustomModelData.put(cmd, column);
                Rotor.byName.put("TIME_ROTOR_" + r.toUpperCase(), column);
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
