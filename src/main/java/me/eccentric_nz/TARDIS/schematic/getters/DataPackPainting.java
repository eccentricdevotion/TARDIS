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
package me.eccentric_nz.TARDIS.schematic.getters;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPainting;

public class DataPackPainting {

    public static String getCustomVariant(org.bukkit.entity.Painting painting) {
        Painting nmsPainting = ((CraftPainting) painting).getHandle();
        Holder<PaintingVariant> variantHolder = nmsPainting.getVariant();
        return variantHolder.getRegisteredName();
    }

    public static void setCustomVariant(org.bukkit.entity.Painting painting, String key) {
        Painting nmsPainting = ((CraftPainting) painting).getHandle();
        ServerLevel world = ((CraftWorld) painting.getWorld()).getHandle();
        Holder<PaintingVariant> variantHolder = null;
        for (Holder<PaintingVariant> holder : world.registryAccess().lookupOrThrow(Registries.PAINTING_VARIANT).getTagOrEmpty(PaintingVariantTags.PLACEABLE)) {
            if (holder.getRegisteredName().equals(key)) {
                variantHolder = holder;
                break;
            }
        }
        if (variantHolder != null) {
            nmsPainting.setVariant(variantHolder);
        }
    }
}
