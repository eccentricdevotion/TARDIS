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
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFollowers;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.equip.MonsterEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessFlameRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessMonkEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class MonsterLoadListener implements Listener {

    private final TARDIS plugin;
    private final List<EntityType> justThese = Arrays.asList(EntityType.DROWNED, EntityType.PIGLIN_BRUTE, EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIFIED_PIGLIN, EntityType.ARMOR_STAND);

    public MonsterLoadListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMonsterLoad(EntitiesLoadEvent event) {
        for (Entity e : event.getEntities()) {
            try {
                if (e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWADrowned || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWAPiglinBrute || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWASkeleton || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWAZombie || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWAZombifiedPiglin || e.getClass().getDeclaredMethod("getHandle").invoke(e) instanceof TWAFollower) {
                    return;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            }
            resetMonster(e);
        }
    }

    public void resetMonster(Entity entity) {
        if (!justThese.contains(entity.getType()) || !MonsterEquipment.isMonster(entity)) {
            return;
        }
        Monster monster = MonsterEquipment.getMonsterType(entity);
        if (monster != null) {
            if (!monster.isAnimated()) {
                return;
            }
            Location location = entity.getLocation();
            entity.remove();
            LivingEntity a = null;
            if (monster == Monster.OOD || monster == Monster.JUDOON || monster == Monster.K9) {
                // retrieve entity from followers table and get attributes
                Follower follower = null;
                ResultSetFollowers rsf = new ResultSetFollowers(plugin, entity.getUniqueId().toString());
                if (rsf.resultSet()) {
                    follower = rsf.getEntity();
                    a = (LivingEntity) new MonsterSpawner().createFollower(location, follower).getBukkitEntity();
                }
                if (a == null || entity.getType() == EntityType.ARMOR_STAND) {
                    UUID uuid = TARDISWeepingAngels.UNCLAIMED;
                    PersistentDataContainer pdc = entity.getPersistentDataContainer();
                    if (pdc.has(TARDISWeepingAngels.OWNER_UUID)) {
                        uuid = pdc.get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                    }
                    a = (LivingEntity) new MonsterSpawner().createFollower(entity.getLocation(), new Follower(UUID.randomUUID(), uuid, monster, false, false, OodColour.BLACK, 0));
                }
                if (follower != null) {
                    if (monster == Monster.OOD) {
                        TWAOod ood = (TWAOod) a;
                        ood.setColour(follower.getColour());
                        ood.setRedeye(follower.hasOption());
                        ood.setFollowing(follower.isFollowing());
                    } else if (monster == Monster.JUDOON) {
                        TWAJudoon judoon = (TWAJudoon) a;
                        judoon.setAmmo(follower.getAmmo());
                        judoon.setGuard(follower.hasOption());
                        judoon.setFollowing(follower.isFollowing());
                    }
                    if (monster == Monster.K9) {
                        TWAK9 k9 = (TWAK9) a;
                        k9.setFollowing(follower.isFollowing());
                    }
                }
            } else {
                a = new MonsterSpawner().create(location, monster);
            }
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
                case SILENT -> SilentEquipment.setGuardian(a);
                case STRAX -> {
                    PigZombie strax = (PigZombie) a;
                    strax.setAngry(false);
                    a.setCustomName("Strax");
                }
                default -> {
                }
            }
        }
    }
}
