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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.custommodeldata.keys.PoliceBox;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author eccentric_nz
 */
public class TARDISDevBoxCommand {

    private final TARDIS plugin;

    public TARDISDevBoxCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean setPreset(CommandSender sender, String[] args) {
        try {
            ChameleonPreset preset = ChameleonPreset.valueOf(args[1]);
            if (!preset.usesArmourStand()) {
                plugin.getMessenger().message(sender, "The specified Chameleon preset must be a custom model!");
                return true;
            }
            if (sender instanceof Player player) {
                // get the armour stand the player is looking at
                Location observerPos = player.getEyeLocation();
                TARDISVector3D observerDir = new TARDISVector3D(observerPos.getDirection());
                TARDISVector3D observerStart = new TARDISVector3D(observerPos);
                TARDISVector3D observerEnd = observerStart.add(observerDir.multiply(16));
                ArmorStand as = null;
                // Get nearby entities
                for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
                    // Bounding box of the given player
                    TARDISVector3D targetPos = new TARDISVector3D(target.getLocation());
                    TARDISVector3D minimum = targetPos.add(-0.5, 0, -0.5);
                    TARDISVector3D maximum = targetPos.add(0.5, 1.67, 0.5);
                    if (target.getType().equals(EntityType.ARMOR_STAND) && TARDISSonicFreeze.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                        if (as == null || as.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                            as = (ArmorStand) target;
                        }
                    }
                }
                if (as == null) {
                    plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_STAND");
                    return true;
                }
                Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, -1, true);
                ItemStack is = new ItemStack(dye, 1);
                ItemMeta im = is.getItemMeta();
                NamespacedKey model = PoliceBox.BLUE.getKey();
                if (args.length > 2) {
                    switch (dye) {
                        case BLUE_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.BLUE_OPEN.getKey();
                                case "stained" -> model = PoliceBox.BLUE_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.BLUE_FLYING.getKey();
                                default -> model = PoliceBox.BLUE.getKey();
                            }
                        }
                        case BLACK_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.BLACK_OPEN.getKey();
                                case "stained" -> model = PoliceBox.BLACK_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.BLACK_FLYING.getKey();
                                default -> model = PoliceBox.BLACK.getKey();
                            }
                        }
                        case BROWN_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.BROWN_OPEN.getKey();
                                case "stained" -> model = PoliceBox.BROWN_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.BROWN_FLYING.getKey();
                                default -> model = PoliceBox.BROWN.getKey();
                            }
                        }
                        case LIGHT_BLUE_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.LIGHT_BLUE_OPEN.getKey();
                                case "stained" -> model = PoliceBox.LIGHT_BLUE_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.LIGHT_BLUE_FLYING.getKey();
                                default -> model = PoliceBox.LIGHT_BLUE.getKey();
                            }
                        }
                        case LIGHT_GRAY_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.LIGHT_GRAY_OPEN.getKey();
                                case "stained" -> model = PoliceBox.LIGHT_GRAY_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.LIGHT_GRAY_FLYING.getKey();
                                default -> model = PoliceBox.LIGHT_GRAY.getKey();
                            }
                        }
                        case GRAY_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.GRAY_OPEN.getKey();
                                case "stained" -> model = PoliceBox.GRAY_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.GRAY_FLYING.getKey();
                                default -> model = PoliceBox.GRAY.getKey();
                            }
                        }
                        case CYAN_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.CYAN_OPEN.getKey();
                                case "stained" -> model = PoliceBox.CYAN_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.CYAN_FLYING.getKey();
                                default -> model = PoliceBox.CYAN.getKey();
                            }
                        }
                        case MAGENTA_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.MAGENTA_OPEN.getKey();
                                case "stained" -> model = PoliceBox.MAGENTA_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.MAGENTA_FLYING.getKey();
                                default -> model = PoliceBox.MAGENTA.getKey();
                            }
                        }
                        case YELLOW_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.YELLOW_OPEN.getKey();
                                case "stained" -> model = PoliceBox.YELLOW_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.YELLOW_FLYING.getKey();
                                default -> model = PoliceBox.YELLOW.getKey();
                            }
                        }
                        case LIME_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.LIME_OPEN.getKey();
                                case "stained" -> model = PoliceBox.LIME_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.LIME_FLYING.getKey();
                                default -> model = PoliceBox.LIME.getKey();
                            }
                        }
                        case GREEN_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.GREEN_OPEN.getKey();
                                case "stained" -> model = PoliceBox.GREEN_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.GREEN_FLYING.getKey();
                                default -> model = PoliceBox.GREEN.getKey();
                            }
                        }
                        case PINK_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.PINK_OPEN.getKey();
                                case "stained" -> model = PoliceBox.PINK_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.PINK_FLYING.getKey();
                                default -> model = PoliceBox.PINK.getKey();
                            }
                        }
                        case PURPLE_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.PURPLE_OPEN.getKey();
                                case "stained" -> model = PoliceBox.PURPLE_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.PURPLE_FLYING.getKey();
                                default -> model = PoliceBox.PURPLE.getKey();
                            }
                        }
                        case RED_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.RED_OPEN.getKey();
                                case "stained" -> model = PoliceBox.RED_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.RED_FLYING.getKey();
                                default -> model = PoliceBox.RED.getKey();
                            }
                        }
                        case ORANGE_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.ORANGE_OPEN.getKey();
                                case "stained" -> model = PoliceBox.ORANGE_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.ORANGE_FLYING.getKey();
                                default -> model = PoliceBox.ORANGE.getKey();
                            }
                        }
                        case WHITE_DYE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.WHITE_OPEN.getKey();
                                case "stained" -> model = PoliceBox.WHITE_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.WHITE_FLYING.getKey();
                                default -> model = PoliceBox.WHITE.getKey();
                            }
                        }
                        case WOLF_SPAWN_EGG -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.BAD_WOLF_OPEN.getKey();
                                case "stained" -> model = PoliceBox.BAD_WOLF_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.BAD_WOLF_FLYING.getKey();
                                default -> model = PoliceBox.BAD_WOLF_CLOSED.getKey();
                            }
                        }
                        case CYAN_STAINED_GLASS_PANE -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.TENNANT_OPEN.getKey();
                                case "stained" -> model = PoliceBox.TENNANT_STAINED.getKey();
                                case "glass" -> model = PoliceBox.GLASS.getKey();
                                case "fly" -> model = PoliceBox.TENNANT_FLYING.getKey();
                                default -> model = PoliceBox.TENNANT.getKey();
                            }
                        }
                        case CLAY_BALL -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.TYPE_40_OPEN.getKey();
                                case "stained" -> model = PoliceBox.TYPE_40_STAINED.getKey();
                                case "glass" -> model = PoliceBox.TYPE_40_GLASS.getKey();
                                case "fly" -> model = PoliceBox.TYPE_40_FLYING.getKey();
                                default -> model = PoliceBox.TYPE_40_CLOSED.getKey();
                            }
                        }
                        case ENDER_PEARL -> {
                            switch (args[2]) {
                                case "open" -> model = PoliceBox.PANDORICA_OPEN.getKey();
                                case "stained" -> model = PoliceBox.PANDORICA_STAINED.getKey();
                                case "glass" -> model = PoliceBox.PANDORICA_GLASS.getKey();
                                case "fly" -> model = PoliceBox.PANDORICA_FLYING.getKey();
                                default -> model = PoliceBox.PANDORICA.getKey();
                            }
                        }
                    }
                }
                im.setItemModel(model);
                is.setItemMeta(im);
                ArmorStand stand = as;
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    EntityEquipment ee = stand.getEquipment();
                    ee.setItemInMainHand(is);
                    stand.setInvisible(true);
                    stand.setInvulnerable(true);
                    if (args[2].equals("fly")) {
                        // spawn a chicken
                        Chicken chicken = (Chicken) stand.getLocation().getWorld().spawnEntity(stand.getLocation(), EntityType.CHICKEN);
                        stand.addPassenger(player);
                        chicken.addPassenger(stand);
                        chicken.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
                        chicken.setSilent(true);
                        chicken.setInvulnerable(true);
                    }
                }, 2L);
            }
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(sender, "Not a valid Chameleon preset!");
            return true;
        }
        return true;
    }
}
