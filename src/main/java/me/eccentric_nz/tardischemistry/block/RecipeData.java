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
package me.eccentric_nz.tardischemistry.block;

import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeData {

    private final String displayName;
    private final String nameSpacedKey;
    private final List<TextComponent> lore;
    private final Material craftMaterial;
    private final TARDISDisplayItem displayItem;

    public RecipeData(String displayName, String nameSpacedKey, List<String> lore, Material craftMaterial, TARDISDisplayItem displayItem) {
        this.displayName = displayName;
        this.nameSpacedKey = nameSpacedKey;
        this.lore = new ArrayList<>();
        for (String s : lore) {
            this.lore.add(Component.text(s));
        }
        this.craftMaterial = craftMaterial;
        this.displayItem = displayItem;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getNameSpacedKey() {
        return nameSpacedKey;
    }

    public List<TextComponent> getLore() {
        return lore;
    }

    public Material getCraftMaterial() {
        return craftMaterial;
    }

    public TARDISDisplayItem getDisplayItem() {
        return displayItem;
    }
}
