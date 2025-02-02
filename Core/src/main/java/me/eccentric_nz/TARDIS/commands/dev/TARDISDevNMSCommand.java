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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessMonkEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.ice_warriors.IceWarriorEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Equipment;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.silent.SilentEquipment;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.nms.TWAFollower;
import me.eccentric_nz.tardisweepingangels.nms.TWAJudoon;
import me.eccentric_nz.tardisweepingangels.nms.TWAOod;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

public class TARDISDevNMSCommand {

    private final TARDIS plugin;

    public TARDISDevNMSCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean spawn(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            try {
                Monster monster = Monster.valueOf(args[1].toUpperCase(Locale.ROOT));
                UUID uuid = (args.length>2) ? TARDISWeepingAngels.UNCLAIMED: player.getUniqueId();
                Location location = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 16).getLocation();
                if (monster == Monster.OOD || monster == Monster.JUDOON || monster == Monster.K9) {
                    Follower follower = new Follower(UUID.randomUUID(), uuid, monster, false, true, OodColour.BLUE, 0);
                    TWAFollower species = new MonsterSpawner().createFollower(location, follower);
                    switch (monster) {
                        case OOD -> {
                            TWAOod ood = (TWAOod) species;
                            ood.setRedeye(TARDISConstants.RANDOM.nextBoolean());
                            int chance = TARDISConstants.RANDOM.nextInt(10);
                            if (chance < 3) {
                                ood.setColour(OodColour.BLUE);
                            }
                            if (chance > 6) {
                                ood.setColour(OodColour.BROWN);
                            }
                            OodEquipment.set(player, (LivingEntity) species.getBukkitEntity(), false);
                        }
                        case JUDOON -> {
                            TWAJudoon judoon = (TWAJudoon) species;
                            judoon.setAmmo(100);
                            judoon.setGuard(false);
                            JudoonEquipment.set(player, (LivingEntity) species.getBukkitEntity(), false);
                        }
                        // K9
                        default -> K9Equipment.set(player, (LivingEntity) species.getBukkitEntity(), false);
                    }
                } else {
                    LivingEntity le = new MonsterSpawner().create(location, monster);
                    new Equipper(monster, le, false).setHelmetAndInvisibility();
                    if (monster == Monster.SILENT) {
                        SilentEquipment.setGuardian(le);
                    }
                    if (monster == Monster.EMPTY_CHILD) {
                        EmptyChildEquipment.setSpeed(le);
                    }
                    if (monster == Monster.HEADLESS_MONK) {
                        HeadlessMonkEquipment.setTasks(le);
                    }
                    if (monster == Monster.ICE_WARRIOR) {
                        IceWarriorEquipment.setAnger(le);
                    }
                }
            } catch (IllegalArgumentException ignored) {
            }
        }
        return true;
    }
}
