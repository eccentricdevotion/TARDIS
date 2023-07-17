package me.eccentric_nz.tardisweepingangels.monsters.ice_warriors;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;

public class IceWarriorEquipment {

    public static void setAnger(LivingEntity le) {
        PigZombie warrior = (PigZombie) le;
        warrior.setAngry(true);
        warrior.setAnger(Integer.MAX_VALUE);
    }
}
