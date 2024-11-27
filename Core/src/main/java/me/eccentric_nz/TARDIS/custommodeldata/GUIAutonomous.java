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
package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.custommodeldata.keys.Bowl;
import me.eccentric_nz.TARDIS.custommodeldata.keys.Repeater;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.List;

public enum GUIAutonomous {

    // TARDIS Autonomous
    AUTONOMOUS_TYPE(Repeater.AUTO_TYPE.getKey(), 0, Material.REPEATER),
    TYPE_INFO(Bowl.INFO.getKey(), 1, Material.BOWL, Arrays.asList("Choose the location you want", "the TARDIS to automatically", "return to when you die.")),
    HOME(Bowl.HOME.getKey(), 3, Material.BOWL, Arrays.asList("Always go to the", "TARDIS's home location")),
    AREAS(Bowl.AREAS.getKey(), 4, Material.BOWL, Arrays.asList("Go to the an area", "in the death world.", "If a parking spot cannot", "be found, use the fallback", "specified below.")),
    CONFIGURED_AREAS(Bowl.CONFIGURED.getKey(), 5, Material.BOWL, Arrays.asList("Go to the server's", "default area(s).", "If a parking spot cannot", "be found, use the fallback", "specified below.")),
    CLOSEST(Bowl.CLOSEST.getKey(), 6, Material.BOWL, Arrays.asList("Go to the TARDIS's", "home location or an area", "whichever is closest.")),
    SAVE(Bowl.SAVE.getKey(), 7, Material.BOWL, Arrays.asList("Go to a TARDIS", "saved destination.")),
    SELECTED_TYPE(null, -1, Material.LIME_WOOL),
    FALLBACK(Repeater.AUTO_DEFAULT.getKey(), 18, Material.REPEATER),
    FALLBACK_INFO(Bowl.INFO.getKey(), 19, Material.BOWL, Arrays.asList("If a preferred location", "cannot be found, choose", "what the TARDIS should do.")),
    GO_HOME(Bowl.HOME.getKey(), 21, Material.BOWL),
    STAY(Bowl.STAY.getKey(), 22, Material.BOWL),
    SAVE_SELECTOR(Bowl.AUTO_SAVE_SELECTOR.getKey(), 25, Material.BOWL, Arrays.asList("Choose a save", "to travel to -", "click to set.")),
    SELECTED_DEFAULT(null, -1, Material.LIME_WOOL),
    CLOSE(Bowl.CLOSE.getKey(), 35, Material.BOWL);

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final List<String> lore;

    GUIAutonomous(NamespacedKey model, int slot, Material material, List<String> lore) {
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.lore = lore;
    }

    GUIAutonomous(NamespacedKey model, int slot, Material material) {
        this.model = model;
        this.slot = slot;
        this.material = material;
        this.lore = null;
    }

    public NamespacedKey getModel() {
        return model;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        String s = toString();
        return TARDISStringUtils.sentenceCase(s);
    }

    public List<String> getLore() {
        return lore;
    }
}
