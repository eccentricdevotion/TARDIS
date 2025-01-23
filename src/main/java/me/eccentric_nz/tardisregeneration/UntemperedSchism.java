package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.custommodels.keys.Schism;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * The Doctor noted on more than one occasion that it was direct exposure to the Time Vortex through the Untempered
 * Schism that caused Gallifreyans to develop more quickly than other societies. Over "billions of years", it caused
 * them to evolve into Time Lords. This is comparable to the way that having been conceived within the Time Vortex
 * itself allowed the Silence to manipulate Melody Pond's biodata to make her into a proto-Time Lord capable of
 * regeneration.
 */
public class UntemperedSchism {

    public static ItemStack create() {
        ItemStack untempered = new ItemStack(Material.ANCIENT_DEBRIS);
        ItemMeta im = untempered.getItemMeta();
        im.setItemModel(Schism.UNTEMPERED_SCHISM_BLOCK.getKey());
        im.displayName(Component.text(NamedTextColor.WHITE + "Untempered Schism"));
        im.lore(List.of(Component.text("Renew regenerations when"), Component.text("you have used them all.")));
        untempered.setItemMeta(im);
        return untempered;
    }

    public static boolean is(ItemStack is) {
        if (is == null || is.getType() != Material.ANCIENT_DEBRIS || !is.hasItemMeta()) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasItemModel()) {
            return false;
        }
        return im.getDisplayName().endsWith("Untempered Schism");
    }
}
