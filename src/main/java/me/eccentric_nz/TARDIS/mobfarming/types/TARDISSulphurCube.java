package me.eccentric_nz.TARDIS.mobfarming.types;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class TARDISSulphurCube extends TARDISMob {

    private ItemStack swallowed;
    private int size;
    private boolean wander;

    public TARDISSulphurCube() {
        super.setType(EntityType.SULFUR_CUBE);
    }

    public ItemStack getSwallowed() {
        return swallowed;
    }

    public void setSwallowed(ItemStack swallowed) {
        this.swallowed = swallowed;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean canWander() {
        return wander;
    }

    public void setWander(boolean wander) {
        this.wander = wander;
    }
}
