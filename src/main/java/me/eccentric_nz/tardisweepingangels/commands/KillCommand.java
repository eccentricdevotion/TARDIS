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

import java.util.Collection;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class KillCommand {

    private final TARDISWeepingAngels plugin;

    public KillCommand(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
    }

    public boolean kill(CommandSender sender, String[] args) {
        if (args.length < 3) {
            return false;
        }
        String which = args[1].toUpperCase();
        World w = plugin.getServer().getWorld(args[2]);
        if (w == null) {
            sender.sendMessage(plugin.pluginName + "Could not find a world with that name!");
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
                    if (!m.getPassengers().isEmpty() && m.getPassengers().get(0) != null && m.getPassengers().get(0).getType().equals(EntityType.GUARDIAN)) {
                        m.getPassengers().get(0).remove();
                        m.remove();
                        count++;
                    }
                }
                sender.sendMessage(plugin.pluginName + "Removed " + count + " Silent Endermen in " + w.getName());
            } else {
                sender.sendMessage(plugin.pluginName + "Invalid monster type!");
            }
            return true;
        }
        switch (monster) {
            case CYBERMAN, EMPTY_CHILD, SONTARAN, VASHTA_NERADA, ZYGON -> {
                switch (monster) {
                    case CYBERMAN -> what = "Cybermen";
                    case EMPTY_CHILD -> what = "Empty Children";
                    case VASHTA_NERADA -> what = "Vashta Nerada";
                    default -> what = monster.getName() + "s";
                }
                Collection<Zombie> zombies = w.getEntitiesByClass(Zombie.class);
                for (Zombie z : zombies) {
                    EntityEquipment ee = z.getEquipment();
                    if (ee.getHelmet().getType().equals(monster.getMaterial())) {
                        ItemStack is = ee.getHelmet();
                        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(monster.getName())) {
                            z.remove();
                            count++;
                        }
                    }
                }
            }
            case WEEPING_ANGEL, DALEK, HEADLESS_MONK, SILURIAN, MIRE -> {
                what = monster.getName() + "s";
                Collection<Skeleton> skeletons = w.getEntitiesByClass(Skeleton.class);
                for (Skeleton s : skeletons) {
                    EntityEquipment ee = s.getEquipment();
                    if (ee.getHelmet().getType().equals(monster.getMaterial())) {
                        ItemStack is = ee.getHelmet();
                        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(monster.getName())) {
                            s.remove();
                            count++;
                        }
                    }
                }
            }
            case HATH, ICE_WARRIOR, STRAX -> {
                what = (monster.equals(Monster.ICE_WARRIOR)) ? "Ice Warriors" : monster.getName();
                Collection<PigZombie> pigZombies = w.getEntitiesByClass(PigZombie.class);
                for (PigZombie p : pigZombies) {
                    EntityEquipment ee = p.getEquipment();
                    if (ee.getHelmet().getType().equals(monster.getMaterial())) {
                        ItemStack is = ee.getHelmet();
                        if (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().startsWith(monster.getName())) {
                            p.remove();
                            count++;
                        }
                    }
                }
            }
            case SILENT -> {
                what = "Silence";
                Collection<Skeleton> silence = w.getEntitiesByClass(Skeleton.class);
                for (Skeleton m : silence) {
                    if (!m.getPassengers().isEmpty() && m.getPassengers().get(0) != null && m.getPassengers().get(0).getType().equals(EntityType.GUARDIAN)) {
                        m.getPassengers().get(0).remove();
                        m.remove();
                        count++;
                    }
                }
            }
            case OOD, JUDOON, K9, TOCLAFANE -> {
                Collection<ArmorStand> ood = w.getEntitiesByClass(ArmorStand.class);
                for (ArmorStand o : ood) {
                    if (o.getPersistentDataContainer().has(TARDISWeepingAngels.OOD, PersistentDataType.INTEGER)) {
                        what = "Ood";
                        o.remove();
                        count++;
                    } else if (o.getPersistentDataContainer().has(TARDISWeepingAngels.JUDOON, PersistentDataType.INTEGER)) {
                        what = "Judoon";
                        o.remove();
                        count++;
                    } else if (o.getPersistentDataContainer().has(TARDISWeepingAngels.K9, PersistentDataType.INTEGER)) {
                        what = "K9s";
                        o.remove();
                        count++;
                    } else if (o.getPersistentDataContainer().has(TARDISWeepingAngels.TOCLAFANE, PersistentDataType.INTEGER)) {
                        what = "Toclafane";
                        // also remove the bee
                        if (o.getVehicle() != null) {
                            Entity bee = o.getVehicle();
                            if (bee instanceof Bee) {
                                o.remove();
                                bee.remove();
                            }
                        } else {
                            o.remove();
                        }
                        count++;
                    }
                }
            }
            default -> {
            }
        }
        sender.sendMessage(plugin.pluginName + "Removed " + count + " " + what + " in " + w.getName());
        return true;
    }
}
