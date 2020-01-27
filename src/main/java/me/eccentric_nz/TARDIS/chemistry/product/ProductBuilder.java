package me.eccentric_nz.TARDIS.chemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ProductBuilder {

    public static ItemStack getProduct(Product product) {
        ItemStack is = new ItemStack(product.getItemMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(product.toString().replace("_", " "));
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        int which = 10000001 + product.ordinal();
        im.setCustomModelData(which);
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which);
        is.setItemMeta(im);
        return is;
    }
}
