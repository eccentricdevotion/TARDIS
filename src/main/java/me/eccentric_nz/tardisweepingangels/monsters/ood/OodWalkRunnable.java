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
package me.eccentric_nz.tardisweepingangels.monsters.ood;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class OodWalkRunnable implements Runnable {

    private final int[] walkCycle = new int[]{2, 5, 6, 5, 2, 7, 8, 7};
    private final ArmorStand stand;
    private final double speed;
    private final Player player;
    private int i = 0;

    public OodWalkRunnable(ArmorStand stand, double speed, Player player) {
        this.stand = stand;
        this.speed = speed;
        this.player = player;
    }

    @Override
    public void run() {
        Location location = stand.getLocation();
        Vector pos = location.toVector();
        if (player != null) {
            EntityEquipment ee = stand.getEquipment();
            if (ee != null) {
                ItemStack head = ee.getHelmet();
                ItemMeta im = head.getItemMeta();
                int colour = im.getCustomModelData() - (im.getCustomModelData() % 10);
                im.setCustomModelData(walkCycle[i] + colour);
                head.setItemMeta(im);
                ee.setHelmet(head);
                BoundingBox asBox = stand.getBoundingBox();
                BoundingBox pBox = player.getBoundingBox().expand(1.0);
                if (!asBox.overlaps(pBox) && location.getWorld() == player.getWorld()) {
                    Vector target = player.getLocation().toVector();
                    Vector velocity = target.subtract(pos);
                    stand.setVelocity(velocity.normalize().multiply(speed));
                    location.setDirection(velocity);
//                    stand.teleport(location);
                    stand.setRotation(location.getYaw(), location.getPitch());
                    i++;
                    if (i == walkCycle.length) {
                        i = 0;
                    }
                } else {
                    i = 0;
                }
            }
        }
    }
}
