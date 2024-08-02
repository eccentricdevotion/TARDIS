package me.eccentric_nz.tardisregeneration;

import org.bukkit.ChatColor;
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
        im.setCustomModelData(1);
        im.setDisplayName(ChatColor.WHITE + "Untempered Schism");
        im.setLore(List.of("Renew regenerations when", "you have used them all."));
        untempered.setItemMeta(im);
        return untempered;
    }

    public static boolean is(ItemStack is) {
        if (is == null || is.getType() != Material.ANCIENT_DEBRIS || !is.hasItemMeta()) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasCustomModelData()) {
            return false;
        }
        return im.getDisplayName().endsWith("Untempered Schism");
    }
}
