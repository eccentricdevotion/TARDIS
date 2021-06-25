/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.move;

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

class TardisCustomModelDataChanger {

    private final TardisPlugin plugin;
    private final Block block;
    private final Player player;
    private final int id;

    TardisCustomModelDataChanger(TardisPlugin plugin, Block block, Player player, int id) {
        this.plugin = plugin;
        this.block = block;
        this.player = player;
        this.id = id;
    }

    /**
     * Toggle the door open and closed by setting the custom model data.
     */
    void toggleOuterDoor() {
        UUID uuid = player.getUniqueId();
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            return;
        }
        Location outer = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        while (!outer.getChunk().isLoaded()) {
            outer.getChunk().load();
        }
        ItemFrame itemFrame = null;
        for (Entity e : Objects.requireNonNull(outer.getWorld()).getNearbyEntities(outer, 1.0d, 1.0d, 1.0d)) {
            if (e instanceof ItemFrame) {
                itemFrame = (ItemFrame) e;
                break;
            }
        }
        if (itemFrame != null) {
            ItemStack is = itemFrame.getItem();
            if (TardisConstants.DYES.contains(is.getType()) && is.hasItemMeta()) {
                ItemMeta im = is.getItemMeta();
                assert im != null;
                if (im.hasCustomModelData()) {
                    int cmd = im.getCustomModelData();
                    if (cmd == 1001 || cmd == 1002) {
                        boolean open = (cmd == 1001);
                        int newData;
                        if (open) {
                            new TardisInnerDoorOpener(plugin, uuid, id).openDoor();
                            newData = 1002;
                        } else {
                            new TardisInnerDoorCloser(plugin, uuid, id).closeDoor();
                            newData = 1001;
                        }
                        playDoorSound(open, block.getLocation());
                        im.setCustomModelData(newData);
                        is.setItemMeta(im);
                        itemFrame.setItem(is, false);
                    }
                }
            }
        }
    }

    /**
     * Plays a door sound when a police box door is clicked.
     *
     * @param open which sound to play, open (true), close (false)
     * @param l    a location to play the sound at
     */
    private void playDoorSound(boolean open, Location l) {
        if (open) {
            TardisSounds.playTardisSound(l, "tardis_door_open");
        } else {
            TardisSounds.playTardisSound(l, "tardis_door_close");
        }
    }
}
