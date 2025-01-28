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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.doors.TARDISInnerDoorCloser;
import me.eccentric_nz.TARDIS.doors.TARDISInnerDoorOpener;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class TARDISCustomModelChanger {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final ChameleonPreset preset;

    public TARDISCustomModelChanger(TARDIS plugin, Player player, int id, ChameleonPreset preset) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.preset = preset;
    }

    /**
     * Toggle the door open and closed by setting the custom model.
     */
    public void toggleOuterDoor() {
        UUID uuid = player.getUniqueId();
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            return;
        }
        Location outer = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        while (!outer.getChunk().isLoaded()) {
            outer.getChunk().load();
        }
        ArmorStand stand = null;
        for (Entity e : outer.getWorld().getNearbyEntities(outer, 1.0d, 1.0d, 1.0d)) {
            if (e instanceof ArmorStand s) {
                stand = s;
                break;
            }
        }
        if (stand != null) {
            EntityEquipment ee = stand.getEquipment();
            ItemStack is = ee.getHelmet();
            if ((TARDISConstants.DYES.contains(is.getType()) || plugin.getUtils().isCustomModel(is)) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                if (im.hasItemModel()) {
                    NamespacedKey model = im.getItemModel();
                    boolean open = model.getKey().endsWith("_open");
                    NamespacedKey newData;
                    boolean outside = !plugin.getUtils().inTARDISWorld(player);
                    if (open) {
                        new TARDISInnerDoorOpener(plugin, uuid, id).openDoor(outside);
                        newData = switch (is.getType()) {
                            case BLACK_DYE -> ChameleonVariant.BLACK_CLOSED.getKey();
                            case RED_DYE -> ChameleonVariant.RED_CLOSED.getKey();
                            case BROWN_DYE -> ChameleonVariant.BROWN_CLOSED.getKey();
                            case GREEN_DYE -> ChameleonVariant.GREEN_CLOSED.getKey();
                            case BLUE_DYE -> ChameleonVariant.BLUE_CLOSED.getKey();
                            case PURPLE_DYE -> ChameleonVariant.PURPLE_CLOSED.getKey();
                            case CYAN_DYE -> ChameleonVariant.CYAN_CLOSED.getKey();
                            case LIGHT_GRAY_DYE -> ChameleonVariant.LIGHT_GRAY_CLOSED.getKey();
                            case GRAY_DYE -> ChameleonVariant.GRAY_CLOSED.getKey();
                            case PINK_DYE -> ChameleonVariant.PINK_CLOSED.getKey();
                            case LIME_DYE -> ChameleonVariant.LIME_CLOSED.getKey();
                            case YELLOW_DYE -> ChameleonVariant.YELLOW_CLOSED.getKey();
                            case LIGHT_BLUE_DYE -> ChameleonVariant.LIGHT_BLUE_CLOSED.getKey();
                            case MAGENTA_DYE -> ChameleonVariant.MAGENTA_CLOSED.getKey();
                            case ORANGE_DYE -> ChameleonVariant.ORANGE_CLOSED.getKey();
                            case WHITE_DYE -> ChameleonVariant.WHITE_CLOSED.getKey();
                            case CYAN_STAINED_GLASS_PANE -> ChameleonVariant.TENNANT_CLOSED.getKey();
                            case LEATHER_HORSE_ARMOR -> ColouredVariant.TINTED_CLOSED.getKey();
                            case WOLF_SPAWN_EGG -> ChameleonVariant.BAD_WOLF_CLOSED.getKey();
                            case ENDER_PEARL -> ChameleonVariant.PANDORICA_CLOSED.getKey();
                            case GRAY_STAINED_GLASS_PANE -> ChameleonVariant.WEEPING_ANGEL_CLOSED.getKey();
                            // get CUSTOM model path
                            default -> new NamespacedKey(plugin, TARDISBuilderUtility.getCustomModelPath(is.getType().toString()) + "_closed");
                        };
                    } else {
                        new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor(outside);
                        newData = switch (is.getType()) {
                            case BLACK_DYE -> ChameleonVariant.BLACK_OPEN.getKey();
                            case RED_DYE -> ChameleonVariant.RED_OPEN.getKey();
                            case BROWN_DYE -> ChameleonVariant.BROWN_OPEN.getKey();
                            case GREEN_DYE -> ChameleonVariant.GREEN_OPEN.getKey();
                            case BLUE_DYE -> ChameleonVariant.BLUE_OPEN.getKey();
                            case PURPLE_DYE -> ChameleonVariant.PURPLE_OPEN.getKey();
                            case CYAN_DYE -> ChameleonVariant.CYAN_OPEN.getKey();
                            case LIGHT_GRAY_DYE -> ChameleonVariant.LIGHT_GRAY_OPEN.getKey();
                            case GRAY_DYE -> ChameleonVariant.GRAY_OPEN.getKey();
                            case PINK_DYE -> ChameleonVariant.PINK_OPEN.getKey();
                            case LIME_DYE -> ChameleonVariant.LIME_OPEN.getKey();
                            case YELLOW_DYE -> ChameleonVariant.YELLOW_OPEN.getKey();
                            case LIGHT_BLUE_DYE -> ChameleonVariant.LIGHT_BLUE_OPEN.getKey();
                            case MAGENTA_DYE -> ChameleonVariant.MAGENTA_OPEN.getKey();
                            case ORANGE_DYE -> ChameleonVariant.ORANGE_OPEN.getKey();
                            case WHITE_DYE -> ChameleonVariant.WHITE_OPEN.getKey();
                            case CYAN_STAINED_GLASS_PANE -> ChameleonVariant.TENNANT_OPEN.getKey();
                            case LEATHER_HORSE_ARMOR -> ColouredVariant.TINTED_OPEN.getKey();
                            case WOLF_SPAWN_EGG -> ChameleonVariant.BAD_WOLF_OPEN.getKey();
                            case ENDER_PEARL -> ChameleonVariant.PANDORICA_OPEN.getKey();
                            case GRAY_STAINED_GLASS_PANE -> ChameleonVariant.WEEPING_ANGEL_OPEN.getKey();
                            // get CUSTOM model path
                            default -> new NamespacedKey(plugin, TARDISBuilderUtility.getCustomModelPath(is.getType().toString()) + "_open");
                        };
                    }
                    if (preset != ChameleonPreset.PANDORICA) {
                        TARDISSounds.playDoorSound(open, stand.getLocation());
                    }
                    im.setItemModel(newData);
                    is.setItemMeta(im);
                    ee.setHelmet(is, true);
                }
            }
        }
    }
}
