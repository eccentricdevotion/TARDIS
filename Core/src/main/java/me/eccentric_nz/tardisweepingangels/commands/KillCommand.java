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
package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.Locale;

public class KillCommand {

    private final TARDIS plugin;

    public KillCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean kill(CommandSender sender, String[] args) {
        if (args.length < 3) {
            return false;
        }
        String which = args[1].toUpperCase(Locale.ROOT);
        World w = plugin.getServer().getWorld(args[2]);
        if (w == null) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "COULD_NOT_FIND_WORLD");
            return true;
        }
        int count = 0;
        String what = "Angels";
        Monster monster;
        try {
            monster = Monster.valueOf(which);
        } catch (IllegalArgumentException e) {
            // kill old silent
            if (which.equals("OLD_SILENT")) {
                Collection<Enderman> silence = w.getEntitiesByClass(Enderman.class);
                for (Enderman m : silence) {
                    if (!m.getPassengers().isEmpty() && m.getPassengers().getFirst() != null && m.getPassengers().getFirst().getType().equals(EntityType.GUARDIAN)) {
                        m.getPassengers().getFirst().remove();
                        m.remove();
                        count++;
                    }
                }
                plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_SILENT", count, "Silent Endermen", w.getName());
            } else {
                plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_INVALID");
            }
            return true;
        }
        switch (monster) {
            case ANGEL_OF_LIBERTY, CLOCKWORK_DROID, CYBERMAN, CYBERSHADE, EMPTY_CHILD, OSSIFIED, SCARECROW,
                 SLITHEEN, SMILER, SONTARAN, SYCORAX, THE_BEAST, VASHTA_NERADA, ZYGON -> {
                switch (monster) {
                    case ANGEL_OF_LIBERTY -> what = "Angels of Liberty";
                    case THE_BEAST -> what = "The Beast";
                    case CYBERMAN -> what = "Cybermen";
                    case EMPTY_CHILD -> what = "Empty Children";
                    case VASHTA_NERADA -> what = "Vashta Nerada";
                    default -> what = monster.getName() + "s";
                }
                Collection<Zombie> zombies = w.getEntitiesByClass(Zombie.class);
                for (Zombie z : zombies) {
                    if (z.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                        z.remove();
                        count++;
                    }
                }
            }
            case DALEK, HEADLESS_MONK, MIRE, OMEGA, SILURIAN, WEEPING_ANGEL -> {
                what = monster.getName() + "s";
                Collection<Skeleton> skeletons = w.getEntitiesByClass(Skeleton.class);
                for (Skeleton s : skeletons) {
                    if (s.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                        s.remove();
                        count++;
                    }
                }
            }
            case SEA_DEVIL, VAMPIRE_OF_VENICE -> {
                what = (monster == Monster.VAMPIRE_OF_VENICE) ? "Vampires of Venice" : "Sea Devils";
                Collection<Drowned> drowned = w.getEntitiesByClass(Drowned.class);
                for (Drowned d : drowned) {
                    if (d.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                        d.remove();
                        count++;
                    }
                }
            }
            case DALEK_SEC, DAVROS, HATH, ICE_WARRIOR, STRAX -> {
                what = (monster.equals(Monster.ICE_WARRIOR)) ? "Ice Warriors" : monster.getName();
                Collection<PigZombie> pigZombies = w.getEntitiesByClass(PigZombie.class);
                for (PigZombie p : pigZombies) {
                    if (p.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                        p.remove();
                        count++;
                    }
                }
            }
            case SILENT -> {
                what = "Silence";
                Collection<Skeleton> silence = w.getEntitiesByClass(Skeleton.class);
                for (Skeleton m : silence) {
                    if (!m.getPassengers().isEmpty() && m.getPassengers().getFirst() != null && m.getPassengers().getFirst().getType().equals(EntityType.GUARDIAN)) {
                        m.getPassengers().getFirst().remove();
                        m.remove();
                        count++;
                    }
                }
            }
            case SUTEKH -> {
                what = "Sutekh";
                Collection<Stray> strays = w.getEntitiesByClass(Stray.class);
                for (Stray s : strays) {
                    if (s.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                        s.remove();
                        count++;
                    }
                }
            }
            case OOD, JUDOON, K9 -> {
                Collection<Husk> ood = w.getEntitiesByClass(Husk.class);
                for (Husk o : ood) {
                    if (o.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        what = "Ood";
                        o.remove();
                        count++;
                    } else if (o.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        what = "Judoon";
                        o.remove();
                        count++;
                    } else if (o.getPersistentDataContainer().has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        what = "K9s";
                        o.remove();
                        count++;
                    }
                }
            }
            case TOCLAFANE -> {
                Collection<ArmorStand> toclafanes = w.getEntitiesByClass(ArmorStand.class);
                for (ArmorStand t : toclafanes) {
                    if (t.getPersistentDataContainer().has(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER)) {
                        what = "Toclafane";
                        // also remove the bee
                        if (t.getVehicle() != null) {
                            Entity bee = t.getVehicle();
                            if (bee instanceof Bee) {
                                t.remove();
                                bee.remove();
                            }
                        } else {
                            t.remove();
                        }
                        count++;
                    }
                }
            }
            default -> {
            }
        }
        plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_REMOVED", count, what, w.getName());
        return true;
    }
}
