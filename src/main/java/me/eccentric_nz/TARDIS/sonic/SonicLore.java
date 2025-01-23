package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SonicLore {

    public static void setChargeLevel(ItemStack sonic) {
        ItemMeta im = sonic.getItemMeta();
        PersistentDataContainer pdc = im.getPersistentDataContainer();
        if (pdc.has(TARDIS.plugin.getSonicChargeKey(), PersistentDataType.INTEGER)) {
            int charge = pdc.get(TARDIS.plugin.getSonicChargeKey(), PersistentDataType.INTEGER);
            List<Component> lore;
            int index = -1;
            if (im.hasLore()) {
                lore = im.lore();
                for (int i = lore.size() - 1; i >= 0; i--) {
                    if (((TextComponent) lore.get(i)).content().startsWith("Charge: ")) {
                        index = i;
                        break;
                    }
                }
                if (index != -1) {
                    lore.set(index, Component.text("Charge: " + charge));
                } else {
                    lore.add(Component.text("Charge: " + charge));
                }
            } else {
                lore = List.of(Component.text("Charge: " + charge));
            }
            im.lore(lore);
            sonic.setItemMeta(im);
        }
    }

    public static ItemStack addUpgrade(List<Component> lore, String dn, NamespacedKey model, ItemStack result, String upgrade) {
        int index = -1;
        Component charge = null;
        for (int i = lore.size() - 1; i >= 0; i--) {
            if (((TextComponent) lore.get(i)).content().startsWith("Charge: ")) {
                charge = lore.get(i);
                index = i;
                break;
            }
        }
        if (index != -1 && charge != null) {
            lore.remove(index);
            lore.add(Component.text(upgrade));
            lore.add(charge);
        } else {
            lore.add(Component.text(upgrade));
        }
        ItemMeta rim = result.getItemMeta();
        rim.displayName(Component.text(dn));
        rim.setItemModel(model);
        rim.lore(lore);
        result.setItemMeta(rim);
        return result;
    }
}
