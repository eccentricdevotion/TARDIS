package me.eccentric_nz.TARDIS.flight;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FlyingAnimation implements Runnable {

    private final ArmorStand stand;
    private final boolean pandorica;
    int i = 0;

    public FlyingAnimation(ArmorStand stand, boolean pandorica) {
        this.stand = stand;
        this.pandorica = pandorica;
    }

    @Override
    public void run() {
        EntityEquipment ee = stand.getEquipment();
        ItemStack is = ee.getHelmet();
        ItemMeta im = is.getItemMeta();
        // switch the custom model - to simulate rotating the TARDIS while flying
        im.setCustomModelData((pandorica ? 1008 : 1005) + i);
        is.setItemMeta(im);
        ee.setHelmet(is);
        i++;
        if (i > 15) {
            i = 0;
        }
    }
}
