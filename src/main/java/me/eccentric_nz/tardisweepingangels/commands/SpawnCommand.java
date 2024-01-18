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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Follower;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.monsters.daleks.DalekEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.empty_child.EmptyChildEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessFlameRunnable;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessMonkEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.judoon.JudoonEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.k9.K9Equipment;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.silent.SilentEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.toclafane.ToclafaneEquipment;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Set;
import java.util.UUID;

public class SpawnCommand {

    private final TARDIS plugin;
    private final Set<Material> trans = null;

    public SpawnCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean spawn(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        String upper = args[1].toUpperCase();
        // check monster type
        Monster monster;
        try {
            monster = Monster.valueOf(upper);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_INVALID");
            return true;
        }
        if (sender instanceof Player player) {
            // check player has permission for this monster
            if (!TARDISPermission.hasPermission(player, "tardisweepingangels.spawn." + monster.getPermission())) {
                plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_PERM_SPAWN", monster.toString());
                return true;
            }
            Location eyeLocation = player.getTargetBlock(trans, 50).getLocation();
            eyeLocation.add(0.5, 1.0, 0.5);
            eyeLocation.setYaw(player.getLocation().getYaw() - 180.0f);
            LivingEntity a;
            if (monster.isFollower()) {
                a = (LivingEntity) new MonsterSpawner().createFollower(eyeLocation, new Follower(UUID.randomUUID(), player.getUniqueId(), monster)).getBukkitEntity();
            } else {
                a = new MonsterSpawner().create(eyeLocation, monster);
            }
            a.setNoDamageTicks(75);
            switch (monster) {
                case DALEK -> {
                    DalekEquipment.set(a, false);
                    if (args.length > 2 && args[2].equalsIgnoreCase("flying")) {
                        // make the Dalek fly
                        EntityEquipment ee = a.getEquipment();
                        ee.setChestplate(new ItemStack(Material.ELYTRA, 1));
                        // teleport them straight up
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            a.teleport(a.getLocation().add(0.0d, 20.0d, 0.0d));
                            a.setGliding(true);
                        }, 2L);
                    }
                }
                case EMPTY_CHILD -> {
                    new Equipper(monster, a, false).setHelmetAndInvisibilty();
                    EmptyChildEquipment.setSpeed(a);
                }
                case HEADLESS_MONK -> {
                    new Equipper(monster, a, false).setHelmetAndInvisibilty();
                    HeadlessMonkEquipment.setTasks(a);
                    // start flame runnable
                    int flameID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable(a), 1, 20);
                    a.getPersistentDataContainer().set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
                }
                case ICE_WARRIOR -> {
                    new Equipper(monster, a, false).setHelmetAndInvisibilty();
                    PigZombie pigman = (PigZombie) a;
                    pigman.setAngry(true);
                    pigman.setAnger(Integer.MAX_VALUE);
                }
                case JUDOON -> JudoonEquipment.set(null, a, false);
                case K9 -> K9Equipment.set(player, a, false);
                case MIRE, SILURIAN -> new Equipper(monster, a, false, true).setHelmetAndInvisibilty();
                case OOD -> OodEquipment.set(player, a, false, true);
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
                case TOCLAFANE -> ToclafaneEquipment.set(a, false);
                // WEEPING_ANGEL, CYBERMAN, HATH, SLITHEEN, SONTARAN, VASHTA_NERADA, ZYGON
                default -> new Equipper(monster, a, false).setHelmetAndInvisibilty();
            }
            String sound = switch (monster) {
                case EMPTY_CHILD -> "empty";
                case HEADLESS_MONK -> "headliess_monk";
                case ICE_WARRIOR -> "warrior";
                case MIRE -> "item.trident.thunder";
                case SEA_DEVIL -> "sea_devil";
                case SILENT -> "silence";
                case WEEPING_ANGEL -> "blink";
                default -> monster.getPermission();
            };
            player.playSound(a.getLocation(), sound, 1.0f, 1.0f);
            plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(a, monster.getEntityType(), monster, eyeLocation));
        } else {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "CMD_PLAYER");
        }
        return true;
    }
}
