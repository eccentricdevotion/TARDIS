/*
 * Copyright (C) 2023 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessFlameRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessMonkEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.silent.SilentEquipment;
import me.eccentric_nz.tardisweepingangels.nms.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class MonsterLoadListener implements Listener {

    private final List<EntityType> justThese = Arrays.asList(EntityType.DROWNED, EntityType.PIGLIN_BRUTE, EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIFIED_PIGLIN);

    @EventHandler
    public void onMonsterLoad(EntitiesLoadEvent event) {
        for (Entity e : event.getEntities()) {
            try {
                if (e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWADrowned
                        || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWAPiglinBrute
                        || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWASkeleton
                        || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWAZombie
                        || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWAZombifiedPiglin
                ) {
                    return;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignore) {
            }
            resetMonster(e);
        }
    }

    public void resetMonster(Entity entity) {
        if (!justThese.contains(entity.getType()) && !MonsterEquipment.isMonster(entity)) {
            return;
        }
        Monster monster = MonsterEquipment.getMonsterType(entity);
        if (monster != null && entity.getType() != EntityType.ARMOR_STAND) {
            if (!monster.isCustom()) {
                return;
            }
            Location location = entity.getLocation();
            entity.remove();
            TARDIS.plugin.debug("Re-spawning a " + monster);
            LivingEntity a = new MonsterSpawner().create(location, monster);
            new Equipper(monster, a, false).setHelmetAndInvisibilty();
            switch (monster) {
                case EMPTY_CHILD -> EmptyChildEquipment.setSpeed(a);
                case HEADLESS_MONK -> {
                    HeadlessMonkEquipment.setTasks(a);
                    // start flame runnable
                    int flameID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, new HeadlessFlameRunnable(a), 1, 20);
                    a.getPersistentDataContainer().set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
                }
                case ICE_WARRIOR -> {
                    PigZombie pigman = (PigZombie) a;
                    pigman.setAngry(true);
                    pigman.setAnger(Integer.MAX_VALUE);
                }
                case MIRE, SILURIAN -> new Equipper(monster, a, false, true).setHelmetAndInvisibilty();
                case SEA_DEVIL -> new Equipper(monster, a, false, false, true).setHelmetAndInvisibilty();
                case SILENT -> {
                    new Equipper(monster, a, false, false).setHelmetAndInvisibilty();
                    SilentEquipment.setGuardian(a);
                }
                case STRAX -> {
                    PigZombie strax = (PigZombie) a;
                    strax.setAngry(false);
                    new Equipper(monster, a, false, false).setHelmetAndInvisibilty();
                    a.setCustomName("Strax");
                }
                default -> {
                }
            }
        }
    }
}
