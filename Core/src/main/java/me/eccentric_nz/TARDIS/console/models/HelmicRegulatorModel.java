package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodeldata.keys.ArmadilloScute;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HelmicRegulatorModel {

    public void setState(ItemDisplay display, int state) {
        ItemStack is = display.getItemStack();
        ItemMeta im = is.getItemMeta();
        switch (state) {
            case 7 -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_7.getKey());
            case 6 -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_6.getKey());
            case 5 -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_5.getKey());
            case 4 -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_4.getKey());
            case 3 -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_3.getKey());
            case 2 -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_2.getKey());
            case 1 -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_1.getKey());
            default -> im.setItemModel(ArmadilloScute.HELMIC_REGULATOR_0.getKey());
        }
        is.setItemMeta(im);
        display.setItemStack(is);
    }
}
