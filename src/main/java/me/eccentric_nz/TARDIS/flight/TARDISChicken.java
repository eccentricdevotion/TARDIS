package me.eccentric_nz.TARDIS.flight;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.Level;

public class TARDISChicken extends Chicken {

    public TARDISChicken(EntityType<? extends Chicken> entitytypes, Level world) {
        super(entitytypes, world);
    }

    @Override
    public boolean isChickenJockey() {
        return false;
    }

    @Override
    public boolean isSensitiveToWater() {
        return false;
    }

    @Override
    public boolean dismountsUnderwater() {
        return false;
    }
}
