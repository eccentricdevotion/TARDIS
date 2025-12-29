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
import me.eccentric_nz.TARDIS.custommodels.keys.*;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.ArmourStandEquipment;
import me.eccentric_nz.tardisweepingangels.monsters.headless_monks.HeadlessFlameRunnable;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import java.util.Locale;

public class EquipCommand {

    private final TARDIS plugin;

    public EquipCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean equip(CommandSender sender, String[] args) {
        if (args.length < 2) {
            return false;
        }
        // check monster type
        String upper = args[1].toUpperCase(Locale.ROOT);
        Monster monster;
        try {
            monster = Monster.valueOf(upper);
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_INVALID");
            return true;
        }
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            plugin.getMessenger().send(sender, TardisModule.MONSTERS, "CMD_PLAYER");
            return true;
        }
        // get the armour stand player is looking at
        Location observerPos = player.getEyeLocation();
        RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.ARMOR_STAND);
        if (result == null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WA_STAND");
            return true;
        }
        ArmorStand as = (ArmorStand) result.getHitEntity();
        if (as != null) {
            new ArmourStandEquipment().setStandEquipment(as, monster, (monster == Monster.EMPTY_CHILD));
            if (args.length > 2) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    EntityEquipment ee = as.getEquipment();
                    ItemStack head = ee.getHelmet();
                    ItemMeta meta = head.getItemMeta();
                    if (monster == Monster.HEADLESS_MONK) {
                        if (args[2].equals("flaming")) {
                            int flameID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new HeadlessFlameRunnable(as), 1, 20);
                            as.getPersistentDataContainer().set(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER, flameID);
                            // set helmet to sword version
                            meta.setItemModel(MonkVariant.HEADLESS_MONK_STATIC.getKey());
                        } else {
                            meta.setItemModel(MonkVariant.HEADLESS_MONK_ALTERNATE.getKey());
                        }
                    }
                    if (monster == Monster.MIRE || monster == Monster.SLITHEEN) {
                        // set no helmet!
                        meta.setItemModel((monster == Monster.MIRE ? MireVariant.THE_MIRE_HELMETLESS.getKey() : SlitheenVariant.SLITHEEN_SUIT.getKey()));
                    }
                    if (monster == Monster.CLOCKWORK_DROID) {
                        meta.setItemModel(DroidVariant.CLOCKWORK_DROID_FEMALE_STATIC.getKey());
                    }
                    if (monster == Monster.DALEK) {
                        try {
                            DyeColor colour = DyeColor.valueOf(args[2].toUpperCase(Locale.ROOT));
                            NamespacedKey c = DalekVariant.DALEK_BRASS.getKey();
                            switch (colour) {
                                case BLACK -> c = DalekVariant.DALEK_BLACK.getKey();
                                case WHITE -> c = DalekVariant.DALEK_WHITE.getKey();
                                case RED -> c = DalekVariant.DALEK_RED.getKey();
                                case BROWN -> c = DalekVariant.DALEK_BROWN.getKey();
                                case GREEN -> c = DalekVariant.DALEK_GREEN.getKey();
                                case BLUE -> c = DalekVariant.DALEK_BLUE.getKey();
                                case PURPLE -> c = DalekVariant.DALEK_PURPLE.getKey();
                                case CYAN -> c = DalekVariant.DALEK_CYAN.getKey();
                                case LIGHT_GRAY -> c = DalekVariant.DALEK_LIGHT_GRAY.getKey();
                                case GRAY -> c = DalekVariant.DALEK_GRAY.getKey();
                                case PINK -> c = DalekVariant.DALEK_PINK.getKey();
                                case LIME -> c = DalekVariant.DALEK_LIME.getKey();
                                case YELLOW -> c = DalekVariant.DALEK_YELLOW.getKey();
                                case LIGHT_BLUE -> c = DalekVariant.DALEK_LIGHT_BLUE.getKey();
                                case MAGENTA -> c = DalekVariant.DALEK_MAGENTA.getKey();
                                case ORANGE -> c = DalekVariant.DALEK_ORANGE.getKey();
                            }
                            meta.setItemModel(c);
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                    if (monster == Monster.CYBERMAN) {
                        try {
                            CybermanVariant variant = CybermanVariant.valueOf(args[2].toUpperCase(Locale.ROOT) + "_STATIC");
                            meta.setItemModel(variant.getKey());
                        } catch (IllegalArgumentException ignored) {
                        }
                    }
                    if (monster == Monster.TOCLAFANE) {
                        meta.setItemModel(ToclafaneVariant.TOCLAFANE_ATTACK.getKey());
                    }
                    head.setItemMeta(meta);
                    ee.setHelmet(head);
                }, 2L);
            }
        }
        return true;
    }
}
