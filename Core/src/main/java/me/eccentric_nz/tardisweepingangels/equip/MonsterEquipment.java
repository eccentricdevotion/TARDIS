/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MonsterEquipment {

    public static boolean isMonster(Entity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (entity instanceof Drowned) {
            return pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.VAMPIRE, PersistentDataType.INTEGER);
        } else if (entity instanceof PigZombie) {
            return (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.STRAX, PersistentDataType.INTEGER));
        } else if (entity instanceof Husk || entity instanceof ArmorStand) {
            return entity.getPersistentDataContainer().has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID);
        } else if (entity instanceof Zombie) {
            return (pdc.has(TARDISWeepingAngels.ANGEL_OF_LIBERTY, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.BEAST, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.CLOCKWORK_DROID, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.CYBERSHADE, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.OSSIFIED, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SCARECROW, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SMILER, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SYCORAX, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER));
        } else if (entity instanceof Skeleton) {
            if (!entity.getPassengers().isEmpty()) {
                Entity passenger = entity.getPassengers().getFirst();
                if (passenger != null && passenger.getType().equals(EntityType.GUARDIAN)) {
                    return true;
                }
            }
            return (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.OMEGA, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)
                    || pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER));
        } else if (entity instanceof Bee) {
            if (!entity.getPassengers().isEmpty()) {
                Entity passenger = entity.getPassengers().getFirst();
                return passenger != null && passenger.getType().equals(EntityType.ARMOR_STAND);
            }
        } else if (entity instanceof Guardian) {
            if (entity.getVehicle() != null && entity.getVehicle() instanceof Skeleton skeleton) {
                return skeleton.getPersistentDataContainer().has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER);
            }
        } else if (entity instanceof PiglinBrute) {
            return entity.getPersistentDataContainer().has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER);
        } else if (entity instanceof Stray) {
            return entity.getPersistentDataContainer().has(TARDISWeepingAngels.SUTEKH, PersistentDataType.INTEGER);
        }
        return false;
    }

    public static Monster getMonsterType(Entity entity) {
        PersistentDataContainer pdc = entity.getPersistentDataContainer();
        if (entity instanceof Husk) {
            if (pdc.has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                return Monster.OOD;
            }
            if (pdc.has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                return Monster.JUDOON;
            }
            if (pdc.has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                return Monster.K9;
            }
        }
        if (entity instanceof ArmorStand) {
            if (pdc.has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER)) {
                return Monster.OOD;
            }
            if (pdc.has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER)) {
                return Monster.JUDOON;
            }
            if (pdc.has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER)) {
                return Monster.K9;
            }
        }
        if (entity instanceof Zombie) {
            if (pdc.has(TARDISWeepingAngels.ANGEL_OF_LIBERTY, PersistentDataType.INTEGER)) {
                return Monster.ANGEL_OF_LIBERTY;
            }
            if (pdc.has(TARDISWeepingAngels.BEAST, PersistentDataType.INTEGER)) {
                return Monster.THE_BEAST;
            }
            if (pdc.has(TARDISWeepingAngels.CLOCKWORK_DROID, PersistentDataType.INTEGER)) {
                return Monster.CLOCKWORK_DROID;
            }
            if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                return Monster.CYBERMAN;
            }
            if (pdc.has(TARDISWeepingAngels.CYBERSHADE, PersistentDataType.INTEGER)) {
                return Monster.CYBERSHADE;
            }
            if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                return Monster.EMPTY_CHILD;
            }
            if (pdc.has(TARDISWeepingAngels.OSSIFIED, PersistentDataType.INTEGER)) {
                return Monster.OSSIFIED;
            }
            if (pdc.has(TARDISWeepingAngels.SCARECROW, PersistentDataType.INTEGER)) {
                return Monster.SCARECROW;
            }
            if (pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                return Monster.SLITHEEN;
            }
            if (pdc.has(TARDISWeepingAngels.SMILER, PersistentDataType.INTEGER)) {
                return Monster.SMILER;
            }
            if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                return Monster.SONTARAN;
            }
            if (pdc.has(TARDISWeepingAngels.SYCORAX, PersistentDataType.INTEGER)) {
                return Monster.SYCORAX;
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
        }
        if (entity instanceof PiglinBrute) {
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
            if (pdc.has(TARDISWeepingAngels.OMEGA, PersistentDataType.INTEGER)) {
                return Monster.OMEGA;
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
        if (entity instanceof Stray) {
            if (pdc.has(TARDISWeepingAngels.SUTEKH, PersistentDataType.INTEGER)) {
                return Monster.SUTEKH;
            }
        }
        if (entity instanceof Drowned) {
            if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                return Monster.SEA_DEVIL;
            }
            if (pdc.has(TARDISWeepingAngels.VAMPIRE, PersistentDataType.INTEGER)) {
                return Monster.VAMPIRE_OF_VENICE;
            }
        }
        if (entity instanceof PiglinBrute && pdc.has(TARDISWeepingAngels.RACNOSS, PersistentDataType.INTEGER)) {
            return Monster.RACNOSS;
        }
        return null;
    }
}
