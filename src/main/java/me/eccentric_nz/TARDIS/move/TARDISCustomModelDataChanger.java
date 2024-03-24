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
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.doors.TARDISInnerDoorCloser;
import me.eccentric_nz.TARDIS.doors.TARDISInnerDoorOpener;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class TARDISCustomModelDataChanger {

    private final TARDIS plugin;
    private final Player player;
    private final int id;
    private final ChameleonPreset preset;

    public TARDISCustomModelDataChanger(TARDIS plugin, Player player, int id, ChameleonPreset preset) {
        this.plugin = plugin;
        this.player = player;
        this.id = id;
        this.preset = preset;
    }

    /**
     * Toggle the door open and closed by setting the custom model data.
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
                if (im.hasCustomModelData()) {
                    int cmd = im.getCustomModelData();
                    if (cmd == 1001 || cmd == 1002) {
                        boolean open = (cmd == 1001);
                        int newData;
                        if (open) {
                            new TARDISInnerDoorOpener(plugin, uuid, id).openDoor(!plugin.getUtils().inTARDISWorld(player));
                            newData = 1002;
                        } else {
                            new TARDISInnerDoorCloser(plugin, uuid, id).closeDoor();
                            newData = 1001;
                        }
                        if (preset != ChameleonPreset.PANDORICA) {
                            TARDISSounds.playDoorSound(open, stand.getLocation());
                        }
                        im.setCustomModelData(newData);
                        is.setItemMeta(im);
                        ee.setHelmet(is, true);
                    }
                }
            }
        }
    }
}
