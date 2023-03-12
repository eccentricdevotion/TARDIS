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
package me.eccentric_nz.tardisweepingangels.equip;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelsAPI;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessMonkEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonWalkRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Equipment;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.silent.SilentEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneEquipment;
import me.eccentric_nz.tardisweepingangels.utils.FollowerChecker;
import me.eccentric_nz.tardisweepingangels.utils.HeadBuilder;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MonsterEquipment implements TARDISWeepingAngelsAPI {

    public static boolean isAnimatedMonster(Entity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        return pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)
                || pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER);
    }

    public static boolean isMonster(Entity entity) {
        if (entity instanceof Zombie || entity instanceof PigZombie || entity instanceof Skeleton) {
            PersistentDataContainer pdc = entity.getPersistentDataContainer();
            if (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                return true;
            }
        } else if (entity instanceof Skeleton && !entity.getPassengers().isEmpty()) {
            Entity passenger = entity.getPassengers().get(0);
            if (passenger != null && passenger.getType().equals(EntityType.GUARDIAN)) {
                return true;
            }
        } else if (entity instanceof Bee) {
            if (!entity.getPassengers().isEmpty()) {
                Entity passenger = entity.getPassengers().get(0);
                if (passenger != null && passenger.getType().equals(EntityType.ARMOR_STAND)) {
                    return true;
                }
            }
        } else if (entity instanceof ArmorStand) {
            if (entity.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                return true;
            }
        } else if (entity instanceof Guardian) {
            if (entity.getVehicle() != null && entity.getVehicle() instanceof Skeleton skeleton) {
                return skeleton.getPersistentDataContainer().has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER);
            }
        } else if (entity instanceof Drowned && entity.getPersistentDataContainer().has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
            return true;
        } else if (entity instanceof PiglinBrute && entity.getPersistentDataContainer().has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
            return true;
        }
        return false;
    }

    public static Monster getMonsterType(Entity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (entity instanceof Zombie) {
            if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                return Monster.CYBERMAN;
            }
            if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                return Monster.EMPTY_CHILD;
            }
            if (pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                return Monster.SLITHEEN;
            }
            if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                return Monster.SONTARAN;
            }
            if (pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)) {
                return Monster.VASHTA_NERADA;
            }
            if (pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                return Monster.ZYGON;
            }
        }
        if (entity instanceof PigZombie) {
            if (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)) {
                return Monster.DALEK_SEC;
            }
            if (pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)) {
                return Monster.DAVROS;
            }
            if (pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)) {
                return Monster.HATH;
            }
            if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                return Monster.ICE_WARRIOR;
            }
            if (pdc.has(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER)) {
                return Monster.STRAX;
            }
            if (pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
                return Monster.RACNOSS;
            }
        }
        if (entity instanceof Skeleton) {
            if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                return Monster.DALEK;
            }
            if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                return Monster.HEADLESS_MONK;
            }
            if (pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)) {
                return Monster.MIRE;
            }
            if (pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)) {
                return Monster.SILENT;
            }
            if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                return Monster.SILURIAN;
            }
            if (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                return Monster.WEEPING_ANGEL;
            }
        }
        if (entity instanceof Drowned && pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
            return Monster.SEA_DEVIL;
        }
        if (entity instanceof PiglinBrute && pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
            return Monster.RACNOSS;
        }
        return null;
    }

    @Override
    public void setAngelEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.WEEPING_ANGEL, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setWarriorEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.ICE_WARRIOR, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setCyberEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.CYBERMAN, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setDalekEquipment(LivingEntity le, boolean disguise) {
        DalekEquipment.set(le, disguise);
    }

    @Override
    public void setDalekSecEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.DALEK_SEC, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setDavrosEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.DAVROS, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setEmptyChildEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.EMPTY_CHILD, le, disguise, false).setHelmetAndInvisibilty();
        if (!disguise) {
            EmptyChildEquipment.setSpeed(le);
        }
    }

    @Override
    public void setHathEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.HATH, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setHeadlessMonkEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.HEADLESS_MONK, le, disguise, false).setHelmetAndInvisibilty();
        HeadlessMonkEquipment.setTasks(le);
    }

    @Override
    public void setMireEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.MIRE, le, disguise, true).setHelmetAndInvisibilty();
    }

    @Override
    public void setSeaDevilEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.SEA_DEVIL, le, disguise, true).setHelmetAndInvisibilty();
    }

    @Override
    public void setSlitheenEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.SLITHEEN, le, disguise, true).setHelmetAndInvisibilty();
    }

    @Override
    public void setJudoonEquipment(Player player, Entity armorStand, boolean disguise) {
        JudoonEquipment.set(player, armorStand, disguise);
    }

    @Override
    public void setJudoonEquipment(Player player, Entity armorStand, int ammunition) {
        setJudoonEquipment(player, armorStand, false);
        armorStand.getPersistentDataContainer().set(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER, ammunition);
    }

    @Override
    public void setK9Equipment(Player player, Entity armorStand, boolean disguise) {
        K9Equipment.set(player, armorStand, disguise);
    }

    @Override
    public void setOodEquipment(Player player, Entity armorStand, boolean disguise) {
        OodEquipment.set(player, armorStand, disguise);
    }

    @Override
    public void setRacnossEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.RACNOSS, le, disguise, true).setHelmetAndInvisibilty();
    }

    @Override
    public void setSilentEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.SILENT, le, disguise, false).setHelmetAndInvisibilty();
        SilentEquipment.setGuardian(le);
    }

    @Override
    public void setSilurianEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.SILURIAN, le, disguise, true).setHelmetAndInvisibilty();
    }

    @Override
    public void setSontaranEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.SONTARAN, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setStraxEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.STRAX, le, disguise, false).setHelmetAndInvisibilty();
        if (!disguise) {
            le.setCustomName("Strax");
        }
    }

    @Override
    public void setToclafaneEquipment(Entity armorStand, boolean disguise) {
        ToclafaneEquipment.set(armorStand, disguise);
    }

    @Override
    public void setVashtaNeradaEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.VASHTA_NERADA, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void setZygonEquipment(LivingEntity le, boolean disguise) {
        new Equipper(Monster.ZYGON, le, disguise, false).setHelmetAndInvisibilty();
    }

    @Override
    public void removeEquipment(Player p) {
        RemoveEquipment.set(p);
    }

    @Override
    public boolean isWeepingAngelMonster(Entity entity) {
        return isMonster(entity);
    }

    @Override
    public Monster getWeepingAngelMonsterType(Entity entity) {
        return getMonsterType(entity);
    }

    @Override
    public FollowerChecker isClaimedMonster(Entity entity, UUID uuid) {
        return new FollowerChecker(entity, uuid);
    }

    @Override
    public void setFollowing(ArmorStand stand, Player player) {
        int taskId = TARDISWeepingAngels.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(TARDISWeepingAngels.plugin, new JudoonWalkRunnable(stand, 0.15d, player), 2L, 2L);
        TARDISWeepingAngels.plugin.getFollowTasks().put(player.getUniqueId(), taskId);
    }

    @Override
    public ItemStack getHead(Monster monster) {
        return HeadBuilder.getItemStack(monster);
    }

    @Override
    public ItemStack getK9() {
        ItemStack is = new ItemStack(Material.BONE);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("K9");
        im.setCustomModelData(1);
        is.setItemMeta(im);
        return is;
    }
}
