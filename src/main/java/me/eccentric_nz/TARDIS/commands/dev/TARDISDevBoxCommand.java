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
package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.monsters.weeping_angels.Blink;
import me.eccentric_nz.tardisweepingangels.utils.Vector3D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 *
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
            if (!preset.usesItemFrame()) {
                plugin.getMessenger().message(sender, "The specified Chameleon preset must be a custom model!");
                return true;
            }
            if (sender instanceof Player player) {
                // get the armour stand the player is looking at
                Location observerPos = player.getEyeLocation();
                Vector3D observerDir = new Vector3D(observerPos.getDirection());
                Vector3D observerStart = new Vector3D(observerPos);
                Vector3D observerEnd = observerStart.add(observerDir.multiply(16));
                ArmorStand as = null;
                // Get nearby entities
                for (Entity target : player.getNearbyEntities(8.0d, 8.0d, 8.0d)) {
                    // Bounding box of the given player
                    Vector3D targetPos = new Vector3D(target.getLocation());
                    Vector3D minimum = targetPos.add(-0.5, 0, -0.5);
                    Vector3D maximum = targetPos.add(0.5, 1.67, 0.5);
                    if (target.getType().equals(EntityType.ARMOR_STAND) && Blink.hasIntersection(observerStart, observerEnd, minimum, maximum)) {
                        if (as == null || as.getLocation().distanceSquared(observerPos) > target.getLocation().distanceSquared(observerPos)) {
                            as = (ArmorStand) target;
                        }
                    }
                }
                if (as == null) {
                    plugin.getMessenger().send(sender, TardisModule.MONSTERS, "WA_STAND");
                    return true;
                }
                Material dye = TARDISBuilderUtility.getMaterialForItemFrame(preset, -1, true);
                ItemStack is = new ItemStack(dye, 1);
                ItemMeta im = is.getItemMeta();
                int cmd = 1001;
                if (args.length > 2) {
                    switch (args[2]) {
                        case "open" -> cmd = 1002;
                        case "stained" -> cmd = 1003;
                        case "glass" -> cmd = 1004;
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
                        // spawn a phantom
                        Phantom phantom = (Phantom) stand.getLocation().getWorld().spawnEntity(stand.getLocation(), EntityType.PHANTOM);
                        stand.addPassenger(player);
                        phantom.addPassenger(stand);
                        phantom.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
                        phantom.setSilent(true);
                        phantom.setInvulnerable(true);
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
