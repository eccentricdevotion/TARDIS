package me.eccentric_nz.TARDIS.flight;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlyingInit {

    private final ArmorStand stand;

    public FlyingInit(ArmorStand stand) {
        this.stand = stand;
    }

    public void run() {
        EntityEquipment ee = stand.getEquipment();
        ItemStack is = ee.getHelmet();
        ItemMeta im = is.getItemMeta();
        // remove the current custom model - we'll use a rotating item display while flying
        im.setItemModel(null);
        is.setItemMeta(im);
        ee.setHelmet(is);
    }
}
