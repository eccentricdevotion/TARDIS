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
package me.eccentric_nz.tardischemistry.product;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class SparklerRunnable implements Runnable {

    private final Player player;
    private final BlockData colour;
    private final long startTime;
    private int taskId;

    public SparklerRunnable(Player player, BlockData colour, long startTime) {
        this.player = player;
        this.colour = colour;
        this.startTime = startTime;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    @Override
    public void run() {
        PlayerInventory inventory = player.getInventory();
        // item in hands
        ItemStack mainHand = inventory.getItemInMainHand();
        if (isSparkler(mainHand)) {
            if (System.currentTimeMillis() < startTime + 30000) {
                Location rightHand = getHandLocation();
                player.spawnParticle(Particle.DUST, rightHand, 5, colour);
            } else {
                ItemStack sparkler = inventory.getItemInMainHand();
                int amount = sparkler.getAmount();
                if (amount > 1) {
                    Product cmd = Product.getByName().get(ComponentUtils.stripColour(sparkler.getData(DataComponentTypes.CUSTOM_NAME)));
                    sparkler.setData(DataComponentTypes.ITEM_MODEL, cmd.getModel());
                    sparkler.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
                    sparkler.setAmount(amount - 1);
                    inventory.setItemInMainHand(sparkler);
                } else {
                    inventory.setItemInMainHand(null);
                }
                player.updateInventory();
                player.playSound(player.getLocation(), Sound.ENTITY_CREEPER_DEATH, 1.0f, 1.0f);
                Bukkit.getScheduler().cancelTask(taskId);
                taskId = 0;
            }
        }
    }

    private boolean isSparkler(ItemStack sparkler) {
        if (sparkler == null) {
            return false;
        }
        return SparklerMaterial.isCorrectMaterial(sparkler.getType())
                && ComponentUtils.isModelled(sparkler)
                && sparkler.hasData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
    }

    private Location getHandLocation() {
        double degrees = toThreeSixty(Location.normalizeYaw(player.getLocation().getYaw()));
        double yaw = Math.toRadians(degrees);
        double handRadius = 0.55d;
        double realXOffset = Math.cos(yaw) * handRadius;
        double realZOffset = Math.sin(yaw) * handRadius;
        double staticYOffset = 1.2d;
        return player.getLocation().clone().add(realXOffset, staticYOffset, realZOffset);
    }

    private double toThreeSixty(double angle) {
        double threeSixty = (angle < 0 && angle >= -180) ? angle + 360 : angle;
        return (threeSixty + 180) % 360;
    }
}
