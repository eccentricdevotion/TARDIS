package me.eccentric_nz.TARDIS.mobfarming;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class TARDISHappyGhast extends TARDISMob {

    private ItemStack harness;
    private Location home;
    private TARDISBoat boat;
    private int slotIndex;
    private int tardis_id;

    public TARDISHappyGhast() {
        super.setType(EntityType.HAPPY_GHAST);
    }

    public ItemStack getHarness() {
        return harness;
    }

    public void setHarness(ItemStack harness) {
        this.harness = harness;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public TARDISBoat getBoat() {
        return boat;
    }

    public void setBoat(TARDISBoat boat) {
        this.boat = boat;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public void setSlotIndex(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    public int getTardis_id() {
        return tardis_id;
    }

    public void setTardis_id(int tardis_id) {
        this.tardis_id = tardis_id;
    }
}
