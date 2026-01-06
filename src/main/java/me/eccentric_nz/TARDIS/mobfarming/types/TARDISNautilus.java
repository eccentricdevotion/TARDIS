package me.eccentric_nz.TARDIS.mobfarming.types;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class TARDISNautilus extends TARDISMob {

    private ItemStack saddle;
    private ItemStack armour;

    public TARDISNautilus() {
        super.setType(EntityType.NAUTILUS);
    }

    public ItemStack getSaddle() {
        return saddle;
    }

    public void setSaddle(ItemStack saddle) {
        this.saddle = saddle;
    }

    public ItemStack getArmour() {
        return armour;
    }

    public void setArmour(ItemStack armour) {
        this.armour = armour;
    }
}
