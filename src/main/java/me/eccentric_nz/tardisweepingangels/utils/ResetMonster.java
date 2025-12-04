/*
 * Copyright (C) 2025 eccentric_nz
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
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Equipment;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.silent.SilentEquipment;
import me.eccentric_nz.tardisweepingangels.nms.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class ResetMonster {

    private final TARDIS plugin;
    private final Entity entity;

    public ResetMonster(TARDIS plugin, Entity entity) {
        this.plugin = plugin;
        this.entity = entity;
    }

    public void reset() {
        Monster monster = MonsterEquipment.getMonsterType(entity);
        if (monster == null || monster.isFollower()) {
            return;
        }
        Location location = entity.getLocation();
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        LivingEntity a = null;
        OfflinePlayer player = null;
        if (monster.isFollower()) {
            // retrieve entity from followers table and get attributes
            Follower follower = null;
            UUID eid = null;
            if (pdc.has(TARDISWeepingAngels.PDC_KEYS.get(monster), TARDISWeepingAngels.PersistentDataTypeUUID)) {
                eid = pdc.get(TARDISWeepingAngels.PDC_KEYS.get(monster), TARDISWeepingAngels.PersistentDataTypeUUID);
            }
            if (eid != null) {
                ResultSetFollowers rsf = new ResultSetFollowers(plugin, eid.toString());
                if (rsf.resultSet()) {
                    follower = rsf.getEntity();
                    a = (LivingEntity) new MonsterSpawner().createFollower(location, follower).getBukkitEntity();
                }
            }
            if (a == null || entity.getType() == EntityType.ARMOR_STAND) {
                UUID uuid = TARDISWeepingAngels.UNCLAIMED;
                if (pdc.has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                    uuid = pdc.get(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
                }
                plugin.setTardisSpawn(true);
                a = (LivingEntity) new MonsterSpawner().createFollower(entity.getLocation(), new Follower(UUID.randomUUID(), uuid, monster)).getBukkitEntity();
            }
            if (follower != null) {
                if (monster == Monster.OOD) {
                    TWAOod ood = (TWAOod) ((CraftEntity) a).getHandle();
                    ood.setColour(follower.getColour());
                    ood.setRedeye(follower.hasOption());
                    ood.setFollowing(follower.isFollowing());
                } else if (monster == Monster.JUDOON) {
                    TWAJudoon judoon = (TWAJudoon) ((CraftEntity) a).getHandle();
                    judoon.setAmmo(follower.getAmmo());
                    judoon.setGuard(follower.hasOption());
                    judoon.setFollowing(follower.isFollowing());
                    if (follower.hasOption()) {
                        // add guard trackers
                        TARDISWeepingAngels.getGuards().add(a.getUniqueId());
                        TARDISWeepingAngels.getPlayersWithGuards().add(judoon.getOwnerUUID());
                    }
                } else if (monster == Monster.K9) {
                    TWAK9 k9 = (TWAK9) ((CraftEntity) a).getHandle();
                    k9.setFollowing(follower.isFollowing());
                }
                TWAFollower twaf = (TWAFollower) ((CraftEntity) a).getHandle();
                twaf.setOwnerUUID(follower.getOwner());
                a.getPersistentDataContainer().set(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID, follower.getOwner());
                player = plugin.getServer().getOfflinePlayer(follower.getOwner());
                // remove database entry
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", eid.toString());
                plugin.getQueryFactory().doDelete("followers", where);
            }
        } else {
            plugin.setTardisSpawn(true);
            a = new MonsterSpawner().create(location, monster);
        }
        entity.remove();
        // set ood / judoon / k9 equipment
        if (monster.isFollower()) {
            TeamAdder.joinTeam(a);
            Husk husk = (Husk) a;
            switch (monster) {
                case K9 -> K9Equipment.set(player, a, false);
                case JUDOON -> JudoonEquipment.set(player, husk, false);
                default -> OodEquipment.set(player, a, false);
            }
        } else {
            // set monster equipment
            if (monster != Monster.DALEK) {
                new Equipper(monster, a, false).setHelmetAndInvisibility();
            }
            switch (monster) {
                case EMPTY_CHILD -> EmptyChildEquipment.setSpeed(a);
                case HEADLESS_MONK -> {
                    HeadlessMonkEquipment.setTasks(a);
                    // start flame runnable
                    int flameID = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable(a), 1, 20);
                    a.getPersistentDataContainer().set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
                }
                case ICE_WARRIOR -> {
                    PigZombie pigman = (PigZombie) a;
                    pigman.setAngry(true);
                    pigman.setAnger(Integer.MAX_VALUE);
                }
                case SILENT -> SilentEquipment.setGuardian(a);
                case STRAX -> {
                    PigZombie strax = (PigZombie) a;
                    strax.setAngry(false);
                    a.customName(Component.text("Strax"));
                }
                default -> { }
            }
        }
    }
}
