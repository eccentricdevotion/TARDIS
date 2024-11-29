package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodeldata.keys.ModelledControl;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LightLevelModel {

    public void setState(ItemDisplay display, int state, boolean interior) {
        if (display == null) {
            return;
        }
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (state) {
            case 7 -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_7.getKey() : ModelledControl.MODELLED_LAMP_7.getKey());
            case 6 -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_6.getKey() : ModelledControl.MODELLED_LAMP_6.getKey());
            case 5 -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_5.getKey() : ModelledControl.MODELLED_LAMP_5.getKey());
            case 4 -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_4.getKey() : ModelledControl.MODELLED_LAMP_4.getKey());
            case 3 -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_3.getKey() : ModelledControl.MODELLED_LAMP_3.getKey());
            case 2 -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_2.getKey() : ModelledControl.MODELLED_LAMP_2.getKey());
            case 1 -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_1.getKey() : ModelledControl.MODELLED_LAMP_1.getKey());
            default -> im.setItemModel(interior ? ModelledControl.MODELLED_LIGHT_0.getKey() : ModelledControl.MODELLED_LAMP_0.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
