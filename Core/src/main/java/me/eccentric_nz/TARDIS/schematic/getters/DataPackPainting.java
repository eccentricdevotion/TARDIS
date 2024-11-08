package me.eccentric_nz.TARDIS.schematic.getters;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.bukkit.craftbukkit.v1_21_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftPainting;

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
