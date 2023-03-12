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
package me.eccentric_nz.tardisweepingangels.commands;

import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class CountCommand {

    private final TARDISWeepingAngels plugin;

    public CountCommand(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    public boolean count(CommandSender sender, String[] args) {
        if (args.length < 3) {
            return false;
        }
        String which = args[1].toUpperCase();
        String what = "Angels";
        int count = 0;
        World w = plugin.getServer().getWorld(args[2]);
        if (w == null) {
            sender.sendMessage(plugin.pluginName + "Could not find a world with that name!");
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
                sender.sendMessage(plugin.pluginName + "Invalid monster type!");
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
                        if (a.getPersistentDataContainer().has(monster.getKey(), PersistentDataType.INTEGER)) {
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
                        if (c.getPersistentDataContainer().has(monster.getKey(), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case HATH, ICE_WARRIOR, STRAX -> {
                    what = (monster.equals(Monster.ICE_WARRIOR)) ? "Ice Warriors" : monster.getName();
                    Collection<PigZombie> fish = w.getEntitiesByClass(PigZombie.class);
                    for (PigZombie h : fish) {
                        if (h.getPersistentDataContainer().has(monster.getKey(), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case JUDOON, K9, OOD -> {
                    what = (monster.equals(Monster.K9)) ? "K9s" : monster.getName();
                    Collection<ArmorStand> galactic_police = w.getEntitiesByClass(ArmorStand.class);
                    for (ArmorStand g : galactic_police) {
                        if (g.getPersistentDataContainer().has(monster.getKey(), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                case SEA_DEVIL -> {
                    what = monster.getName() + "s";
                    Collection<Drowned> drowned = w.getEntitiesByClass(Drowned.class);
                    for (Drowned d : drowned) {
                        if (d.getPersistentDataContainer().has(monster.getKey(), PersistentDataType.INTEGER)) {
                            count++;
                        }
                    }
                }
                default -> {
                }
            }
        }
        sender.sendMessage(plugin.pluginName + "There are " + count + " " + what + " in " + w.getName());
        return true;
    }
}
