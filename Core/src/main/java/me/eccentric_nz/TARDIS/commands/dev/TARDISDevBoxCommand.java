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
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.sonic.actions.TARDISSonicFreeze;
import me.eccentric_nz.TARDIS.utility.TARDISVector3D;
import org.bukkit.Location;
import org.bukkit.Material;
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
                int cmd = 1001;
                if (args.length > 2) {
                    switch (args[2]) {
                        case "open" -> cmd = 1002;
                        case "stained" -> cmd = 1003;
                        case "glass" -> cmd = 1004;
                        case "fly" -> cmd = 1005;
                        default -> cmd = 1001;
                    }
                }
                im.setCustomModelData(cmd);
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
