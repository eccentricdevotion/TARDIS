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
package me.eccentric_nz.tardischemistry.product;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

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
                    ItemMeta im = sparkler.getItemMeta();
                    Product cmd = Product.getByName().get(im.displayName());
                    im.setItemModel(cmd.getModel());
                    im.removeEnchant(Enchantment.LOYALTY);
                    im.setEnchantmentGlintOverride(null);
                    sparkler.setItemMeta(im);
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
        if (!sparkler.hasItemMeta()) {
            return false;
        }
        ItemMeta im = sparkler.getItemMeta();
        return SparklerMaterial.isCorrectMaterial(sparkler.getType())
                && im.hasItemModel()
                && (im.hasEnchantmentGlintOverride() || sparkler.containsEnchantment(Enchantment.LOYALTY));
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
