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
package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.Locale;

public class TARDISCustomDoorLoader {

    private final TARDIS plugin;

    public TARDISCustomDoorLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addDoors() {
        // create plugin doors
        Door police_box = new Door("tardis_door", Material.IRON_DOOR, new int[]{0,1,2}, 6, true, "tardis_door_open", "tardis_door_close", false);
        Door classic = new Door("classic_door", Material.CHERRY_DOOR, new int[]{0,1,2,3,4}, 4, false, "classic_door", "classic_door", false);
        Door bone = new Door("bone_door", Material.BIRCH_DOOR, new int[]{0,1,2,3,4}, 4, false, "classic_door", "classic_door", false);
        Door sidrat = new Door("sidrat_door", Material.PALE_OAK_DOOR, new int[]{0,1,2}, 4, false, "sidrat_open", "sidrat_close", false);
        Door.byMaterial.put(Material.IRON_DOOR, police_box);
        Door.byName.put("POLICE_BOX_DOOR", police_box);
        Door.byName.put("DOOR", police_box);
        Door.byName.put("TARDIS_DOOR", police_box);
        Door.byMaterial.put(Material.CHERRY_DOOR, classic);
        Door.byName.put("CLASSIC_DOOR", classic);
        Door.byMaterial.put(Material.BIRCH_DOOR, bone);
        Door.byName.put("BONE_DOOR", bone);
        Door.byMaterial.put(Material.PALE_OAK_DOOR, sidrat);
        Door.byName.put("SIDRAT_DOOR", sidrat);
        for (String r : plugin.getCustomDoorsConfig().getKeys(false)) {
            try {
                Material material = Material.valueOf(plugin.getCustomDoorsConfig().getString(r + ".material"));
                if (Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(material)) {
                    int[] frames = getFrames(plugin.getCustomDoorsConfig().getString(r + ".animation_sequence", "0,1"));
                    Door door = new Door(r.toUpperCase(Locale.ROOT), material, frames, plugin.getCustomDoorsConfig().getInt(r + ".frame_rate"), plugin.getCustomDoorsConfig().getBoolean(r + ".extra"), plugin.getCustomDoorsConfig().getString(r + ".open_sound"), plugin.getCustomDoorsConfig().getString(r + ".close_sound"), true);
                    Door.byMaterial.put(material, door);
                    Door.byName.put("DOOR_" + r.toUpperCase(Locale.ROOT), door);
                } else {
                    plugin.getMessenger().message(plugin.getConsole(), "Custom door [" + r + "] material must be a pottery sherd!");
                }
            } catch (IllegalArgumentException e) {
                plugin.debug("Invalid custom door item material for " + r + "!");
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
