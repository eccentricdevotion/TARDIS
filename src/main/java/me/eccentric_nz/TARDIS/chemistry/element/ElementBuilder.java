package me.eccentric_nz.TARDIS.chemistry.element;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ElementBuilder {

    public static ItemStack getElement(Element element) {
        ItemStack is = new ItemStack(Material.FEATHER, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(element.toString());
        if (element.equals(Element.Unknown)) {
            im.setLore(Arrays.asList("?", "?"));
        } else {
            im.setLore(Arrays.asList(element.getSymbol(), "" + element.getAtomicNumber()));
        }
        im.setCustomModelData(10000000 + element.getAtomicNumber());
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getElement(int atomicNumber) {
        return getElement(Element.getByAtomicNumber().get(atomicNumber));
    }

    public static ItemStack getElement(String symbol) {
        return getElement(Element.getBySymbol().get(symbol));
    }
}
