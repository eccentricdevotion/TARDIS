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
package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Locale;

public class CountCommand {

    private final TARDIS plugin;

    public CountCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean count(CommandSender sender, String[] args) {
        if (args.length < 3) {
            return false;
        }
        String which = args[1].toUpperCase(Locale.ROOT);
        String what = "Angels";
        int count = 0;
        World w = plugin.getServer().getWorld(args[2]);
        if (w == null) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "COULD_NOT_FIND_WORLD");
            return true;
        }
        if (which.equals("g")) {
            what = "Invisible Guardians without Endermen";
            Collection<Guardian> guardians = w.getEntitiesByClass(Guardian.class);
            for (Guardian g : guardians) {
                if (g.hasPotionEffect(PotionEffectType.INVISIBILITY) && g.getVehicle() == null) {
                    count++;
                }
            }
        } else {
            Monster monster;
            try {
                monster = Monster.valueOf(which);
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_INVALID");
                return true;
            }
            switch (monster) {
                case WEEPING_ANGEL, DALEK, SILURIAN, SILENT, HEADLESS_MONK, MIRE -> {
                    what = switch (monster) {
                        case SILENT -> "Silence";
                        case MIRE -> "Mire";
                        default -> monster.getName() + "s";
                    };
                    Collection<Skeleton> angels = w.getEntitiesByClass(Skeleton.class);
                    for (Skeleton a : angels) {
                        if (a.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case CYBERMAN, EMPTY_CHILD, SLITHEEN, SONTARAN, VASHTA_NERADA, ZYGON -> {
                    what = switch (monster) {
                        case CYBERMAN -> "Cybermen";
                        case EMPTY_CHILD -> "Empty Children";
                        case SLITHEEN -> "Slitheen";
                        case VASHTA_NERADA -> "Vashta Nerada";
                        default -> monster.getName() + "s";
                    };
                    Collection<Zombie> cybermen = w.getEntitiesByClass(Zombie.class);
                    for (Zombie c : cybermen) {
                        if (c.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case HATH, ICE_WARRIOR, STRAX, DAVROS, DALEK_SEC -> {
                    what = (monster.equals(Monster.ICE_WARRIOR)) ? "Ice Warriors" : monster.getName();
                    Collection<PigZombie> fish = w.getEntitiesByClass(PigZombie.class);
                    for (PigZombie h : fish) {
                        if (h.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case JUDOON, K9, OOD -> {
                    what = (monster.equals(Monster.K9)) ? "K9s" : monster.getName();
                    Collection<ArmorStand> galactic_police = w.getEntitiesByClass(ArmorStand.class);
                    for (ArmorStand g : galactic_police) {
                        if (g.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case SEA_DEVIL -> {
                    what = monster.getName() + "s";
                    Collection<Drowned> drowned = w.getEntitiesByClass(Drowned.class);
                    for (Drowned d : drowned) {
                        if (d.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case RACNOSS -> {
                    what = monster.getName();
                    Collection<PiglinBrute> brutes = w.getEntitiesByClass(PiglinBrute.class);
                    for (PiglinBrute b : brutes) {
                        if (b.getPersistentDataContainer().has(TARDISWeepingAngels.PDC_KEYS.get(monster), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case TOCLAFANE -> {
                    what = monster.getName();
                    Collection<Bee> bees = w.getEntitiesByClass(Bee.class);
                    for (Bee b : bees) {
                        if (b.getPassengers() != null && !b.getPassengers().isEmpty() && b.getPassengers().getFirst() instanceof ArmorStand) {
                            count++;
                        }
                    }
                }
                default -> {
                }
            }
        }
        plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_COUNT", count, what, w.getName());
        return true;
    }
}
