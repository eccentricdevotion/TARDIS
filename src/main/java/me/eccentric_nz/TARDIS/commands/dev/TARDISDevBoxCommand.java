/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.builders.exterior.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

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
                RayTraceResult result = observerPos.getWorld().rayTraceEntities(observerPos, observerPos.getDirection(), 16.0d, (s) -> s.getType() == EntityType.ARMOR_STAND);
                if (result == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "WA_STAND");
                    return true;
                }
                ArmorStand as = (ArmorStand) result.getHitEntity();
                if (as != null) {
                    Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, -1, true);
                    ItemStack is = ItemStack.of(dye, 1);
                    ItemMeta im = is.getItemMeta();
                    NamespacedKey model = ChameleonVariant.BLUE_CLOSED.getKey();
                    if (args.length > 2) {
                        switch (dye) {
                            case BLUE_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.BLUE_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.BLUE_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.BLUE_CAMERA.getKey();
                                    default -> {
                                    } // already assigned to ChameleonVariant.BLUE_CLOSED.getKey()
                                }
                            }
                            case BLACK_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.BLACK_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.BLACK_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.BLACK_CAMERA.getKey();
                                    default -> model = ChameleonVariant.BLACK_CLOSED.getKey();
                                }
                            }
                            case BROWN_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.BROWN_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.BROWN_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.BROWN_CAMERA.getKey();
                                    default -> model = ChameleonVariant.BROWN_CLOSED.getKey();
                                }
                            }
                            case LIGHT_BLUE_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.LIGHT_BLUE_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.LIGHT_BLUE_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.LIGHT_BLUE_CAMERA.getKey();
                                    default -> model = ChameleonVariant.LIGHT_BLUE_CLOSED.getKey();
                                }
                            }
                            case LIGHT_GRAY_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.LIGHT_GRAY_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.LIGHT_GRAY_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.LIGHT_GRAY_CAMERA.getKey();
                                    default -> model = ChameleonVariant.LIGHT_GRAY_CLOSED.getKey();
                                }
                            }
                            case GRAY_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.GRAY_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.GRAY_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.GRAY_CAMERA.getKey();
                                    default -> model = ChameleonVariant.GRAY_CLOSED.getKey();
                                }
                            }
                            case CYAN_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.CYAN_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.CYAN_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.CYAN_CAMERA.getKey();
                                    default -> model = ChameleonVariant.CYAN_CLOSED.getKey();
                                }
                            }
                            case MAGENTA_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.MAGENTA_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.MAGENTA_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.MAGENTA_CAMERA.getKey();
                                    default -> model = ChameleonVariant.MAGENTA_CLOSED.getKey();
                                }
                            }
                            case YELLOW_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.YELLOW_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.YELLOW_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.YELLOW_CAMERA.getKey();
                                    default -> model = ChameleonVariant.YELLOW_CLOSED.getKey();
                                }
                            }
                            case LIME_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.LIME_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.LIME_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.LIME_CAMERA.getKey();
                                    default -> model = ChameleonVariant.LIME_CLOSED.getKey();
                                }
                            }
                            case GREEN_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.GREEN_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.GREEN_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.GREEN_CAMERA.getKey();
                                    default -> model = ChameleonVariant.GREEN_CLOSED.getKey();
                                }
                            }
                            case PINK_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.PINK_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.PINK_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.PINK_CAMERA.getKey();
                                    default -> model = ChameleonVariant.PINK_CLOSED.getKey();
                                }
                            }
                            case PURPLE_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.PURPLE_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.PURPLE_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.PURPLE_CAMERA.getKey();
                                    default -> model = ChameleonVariant.PURPLE_CLOSED.getKey();
                                }
                            }
                            case RED_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.RED_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.RED_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.RED_CAMERA.getKey();
                                    default -> model = ChameleonVariant.RED_CLOSED.getKey();
                                }
                            }
                            case ORANGE_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.ORANGE_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.ORANGE_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.ORANGE_CAMERA.getKey();
                                    default -> model = ChameleonVariant.ORANGE_CLOSED.getKey();
                                }
                            }
                            case WHITE_DYE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.WHITE_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.WHITE_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.WHITE_CAMERA.getKey();
                                    default -> model = ChameleonVariant.WHITE_CLOSED.getKey();
                                }
                            }
                            case WOLF_SPAWN_EGG -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.BAD_WOLF_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.BAD_WOLF_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.BAD_WOLF_CAMERA.getKey();
                                    default -> model = ChameleonVariant.BAD_WOLF_CLOSED.getKey();
                                }
                            }
                            case CYAN_STAINED_GLASS_PANE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.TENNANT_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.TENNANT_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.TENNANT_CAMERA.getKey();
                                    default -> model = ChameleonVariant.TENNANT_CLOSED.getKey();
                                }
                            }
                            case CLAY_BALL -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.TYPE_40_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.TYPE_40_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.TYPE_40_GLASS.getKey();
                                    case "camera" -> model = ChameleonVariant.TYPE_40_CAMERA.getKey();
                                    default -> model = ChameleonVariant.TYPE_40_CLOSED.getKey();
                                }
                            }
                            case ENDER_PEARL -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.PANDORICA_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.PANDORICA_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.PANDORICA_GLASS.getKey();
                                    default -> model = ChameleonVariant.PANDORICA_CLOSED.getKey();
                                }
                            }
                            case GREEN_STAINED_GLASS_PANE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.SIDRAT_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.SIDRAT_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.SIDRAT_GLASS.getKey();
                                    default -> model = ChameleonVariant.SIDRAT_CLOSED.getKey();
                                }
                            }
                            case RED_STAINED_GLASS_PANE -> {
                                switch (args[2]) {
                                    case "open" -> model = ChameleonVariant.BATTLE_OPEN.getKey();
                                    case "stained" -> model = ChameleonVariant.BATTLE_STAINED.getKey();
                                    case "glass" -> model = ChameleonVariant.BATTLE_GLASS.getKey();
                                    default -> model = ChameleonVariant.BATTLE_CLOSED.getKey();
                                }
                            }
                        }
                    }
                    im.setItemModel(model);
                    is.setItemMeta(im);
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        EntityEquipment ee = as.getEquipment();
                        ee.setHelmet(is);
                        as.setInvisible(true);
                        as.setInvulnerable(true);
                        if (args[2].equals("camera")) {
                            as.addPassenger(player);
                        }
                    }, 2L);
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.getMessenger().message(sender, "Not a valid Chameleon preset!");
            return true;
        }
        return true;
    }
}
