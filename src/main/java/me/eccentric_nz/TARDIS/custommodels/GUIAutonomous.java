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
package me.eccentric_nz.TARDIS.custommodels;

import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;

public enum GUIAutonomous {

    // TARDIS Autonomous
    AUTONOMOUS_TYPE(SwitchVariant.AUTO_TYPE.getKey(), 0, Material.REPEATER),
    TYPE_INFO(GuiVariant.INFO.getKey(), 1, List.of(Component.text("Choose the location you want"), Component.text("the TARDIS to automatically"), Component.text("return to when you die."))),
    HOME(GuiVariant.HOME.getKey(), 3, List.of(Component.text("Always go to the"), Component.text("TARDIS's home location"))),
    AREAS(GuiVariant.AREAS.getKey(), 4, List.of(Component.text("Go to the an area"), Component.text("in the death world."), Component.text("If a parking spot cannot"), Component.text("be found, use the fallback"), Component.text("specified below."))),
    CONFIGURED_AREAS(GuiVariant.CONFIGURED.getKey(), 5, List.of(Component.text("Go to the server's"), Component.text("default area(s)."), Component.text("If a parking spot cannot"), Component.text("be found, use the fallback"), Component.text("specified below."))),
    CLOSEST(GuiVariant.CLOSEST.getKey(), 6, List.of(Component.text("Go to the TARDIS's"), Component.text("home location or an area"), Component.text("whichever is closest."))),
    SAVE(GuiVariant.SAVE.getKey(), 7, List.of(Component.text("Go to a TARDIS"), Component.text("saved destination."))),
    SELECTED_TYPE(null, -1, Material.LIME_WOOL),
    FALLBACK(SwitchVariant.AUTO_DEFAULT.getKey(), 18, Material.REPEATER),
    FALLBACK_INFO(GuiVariant.INFO.getKey(), 19, List.of(Component.text("If a preferred location"), Component.text("cannot be found, choose"), Component.text("what the TARDIS should do."))),
    GO_HOME(GuiVariant.HOME.getKey(), 21, Material.BOWL),
    STAY(GuiVariant.STAY.getKey(), 22, Material.BOWL),
    SAVE_SELECTOR(GuiVariant.AUTO_SAVE_SELECTOR.getKey(), 25, List.of(Component.text("Choose a save"), Component.text("to travel to -"), Component.text("click to set."))),
    SELECTED_DEFAULT(null, -1, Material.LIME_WOOL),
    CLOSE(GuiVariant.CLOSE.getKey(), 35, Material.BOWL);

    private final NamespacedKey model;
    private final int slot;
    private final Material material;
    private final List<Component> lore;

    GUIAutonomous(NamespacedKey model, int slot, List<Component> lore) {
        this.model = model;
        this.slot = slot;
        this.material = Material.BOWL;
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

    public List<Component> getLore() {
        return lore;
    }
}
