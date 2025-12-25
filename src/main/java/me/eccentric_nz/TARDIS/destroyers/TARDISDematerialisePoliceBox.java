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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISBuilderUtility;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.particles.Emitter;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;
import java.util.UUID;

public class TARDISDematerialisePoliceBox implements Runnable {

    private final TARDIS plugin;
    private final DestroyData dd;
    private final int loops;
    private final ChameleonPreset preset;
    private int task;
    private int i;
    private ItemFrame frame;
    private ArmorStand stand;
    private ItemStack is;

    TARDISDematerialisePoliceBox(TARDIS plugin, DestroyData dd, ChameleonPreset preset) {
        this.plugin = plugin;
        this.dd = dd;
        loops = dd.getThrottle().getLoops();
        this.preset = preset;
    }

    @Override
    public void run() {
        World world = dd.getLocation().getWorld();
        Block light = dd.getLocation().getBlock().getRelative(BlockFace.UP, 2);
        NamespacedKey model;
        Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, dd.getTardisID(), false);
        if (i < loops) {
            if (i % 2 == 0) { // stained
                model = switch (dye) {
                    case BLACK_DYE -> ChameleonVariant.BLACK_STAINED.getKey();
                    case BLUE_DYE -> ChameleonVariant.BLUE_STAINED.getKey();
                    case BROWN_DYE -> ChameleonVariant.BROWN_STAINED.getKey();
                    case CYAN_DYE -> ChameleonVariant.CYAN_STAINED.getKey();
                    case GRAY_DYE -> ChameleonVariant.GRAY_STAINED.getKey();
                    case GREEN_DYE -> ChameleonVariant.GREEN_STAINED.getKey();
                    case LIGHT_BLUE_DYE -> ChameleonVariant.LIGHT_BLUE_STAINED.getKey();
                    case LIGHT_GRAY_DYE -> ChameleonVariant.LIGHT_GRAY_STAINED.getKey();
                    case LIME_DYE -> ChameleonVariant.LIME_STAINED.getKey();
                    case MAGENTA_DYE -> ChameleonVariant.MAGENTA_STAINED.getKey();
                    case ORANGE_DYE -> ChameleonVariant.ORANGE_STAINED.getKey();
                    case PINK_DYE -> ChameleonVariant.PINK_STAINED.getKey();
                    case PURPLE_DYE -> ChameleonVariant.PURPLE_STAINED.getKey();
                    case RED_DYE -> ChameleonVariant.RED_STAINED.getKey();
                    case WHITE_DYE -> ChameleonVariant.WHITE_STAINED.getKey();
                    case YELLOW_DYE -> ChameleonVariant.YELLOW_STAINED.getKey();
                    case CYAN_STAINED_GLASS_PANE -> ChameleonVariant.TENNANT_STAINED.getKey();
                    case GRAY_STAINED_GLASS_PANE -> ChameleonVariant.WEEPING_ANGEL_STAINED.getKey();
                    case GREEN_STAINED_GLASS_PANE -> ChameleonVariant.SIDRAT_STAINED.getKey();
                    case RED_STAINED_GLASS_PANE -> ChameleonVariant.BATTLE_STAINED.getKey();
                    case ENDER_PEARL -> ChameleonVariant.PANDORICA_STAINED.getKey();
                    case LEATHER_HORSE_ARMOR -> ColouredVariant.TINTED_STAINED.getKey();
                    default -> new NamespacedKey(plugin, TARDISBuilderUtility.getCustomModelPath(dye.toString()) + "_stained");
                };
                light.setBlockData(TARDISConstants.AIR);
            } else if (i % 4 == 1) { // glass
                model = switch (dye) {
                    case BLACK_DYE, BLUE_DYE, BROWN_DYE,
                         CYAN_DYE, GRAY_DYE, GREEN_DYE, LIGHT_BLUE_DYE,
                         LIGHT_GRAY_DYE, LIME_DYE, MAGENTA_DYE, ORANGE_DYE,
                         PINK_DYE, PURPLE_DYE, RED_DYE, WHITE_DYE,
                         YELLOW_DYE, LEATHER_HORSE_ARMOR, CYAN_STAINED_GLASS_PANE -> ChameleonVariant.GLASS.getKey();
                    case GRAY_STAINED_GLASS_PANE -> ChameleonVariant.WEEPING_ANGEL_GLASS.getKey();
                    case GREEN_STAINED_GLASS_PANE -> ChameleonVariant.SIDRAT_GLASS.getKey();
                    case RED_STAINED_GLASS_PANE -> ChameleonVariant.BATTLE_GLASS.getKey();
                    case ENDER_PEARL -> ChameleonVariant.PANDORICA_GLASS.getKey();
                    default -> new NamespacedKey(plugin, TARDISBuilderUtility.getCustomModelPath(dye.toString()) + "_glass");
                };
                light.setBlockData(TARDISConstants.AIR);
            } else { // preset
                model = switch (dye) {
                    case BLACK_DYE -> ChameleonVariant.BLACK_CLOSED.getKey();
                    case BLUE_DYE -> ChameleonVariant.BLUE_CLOSED.getKey();
                    case BROWN_DYE -> ChameleonVariant.BROWN_CLOSED.getKey();
                    case CYAN_DYE -> ChameleonVariant.CYAN_CLOSED.getKey();
                    case GRAY_DYE -> ChameleonVariant.GRAY_CLOSED.getKey();
                    case GREEN_DYE -> ChameleonVariant.GREEN_CLOSED.getKey();
                    case LIGHT_BLUE_DYE -> ChameleonVariant.LIGHT_BLUE_CLOSED.getKey();
                    case LIGHT_GRAY_DYE -> ChameleonVariant.LIGHT_GRAY_CLOSED.getKey();
                    case LIME_DYE -> ChameleonVariant.LIME_CLOSED.getKey();
                    case MAGENTA_DYE -> ChameleonVariant.MAGENTA_CLOSED.getKey();
                    case ORANGE_DYE -> ChameleonVariant.ORANGE_CLOSED.getKey();
                    case PINK_DYE -> ChameleonVariant.PINK_CLOSED.getKey();
                    case PURPLE_DYE -> ChameleonVariant.PURPLE_CLOSED.getKey();
                    case RED_DYE -> ChameleonVariant.RED_CLOSED.getKey();
                    case WHITE_DYE -> ChameleonVariant.WHITE_CLOSED.getKey();
                    case YELLOW_DYE -> ChameleonVariant.YELLOW_CLOSED.getKey();
                    case CYAN_STAINED_GLASS_PANE -> ChameleonVariant.TENNANT_CLOSED.getKey();
                    case GRAY_STAINED_GLASS_PANE -> ChameleonVariant.WEEPING_ANGEL_CLOSED.getKey();
                    case GREEN_STAINED_GLASS_PANE -> ChameleonVariant.SIDRAT_CLOSED.getKey();
                    case RED_STAINED_GLASS_PANE -> ChameleonVariant.BATTLE_CLOSED.getKey();
                    case ENDER_PEARL -> ChameleonVariant.PANDORICA_CLOSED.getKey();
                    case LEATHER_HORSE_ARMOR -> ColouredVariant.TINTED_CLOSED.getKey();
                    default -> new NamespacedKey(plugin, TARDISBuilderUtility.getCustomModelPath(dye.toString()) + "_closed");
                };
                Levelled levelled = TARDISConstants.LIGHT;
                // set light level from exterior lamp control
                levelled.setLevel(dd.getExteriorLampLevel());
                light.setBlockData(levelled);
            }
            i++;
            // first run - play sound
            if (i == 1) {
                // remove interaction entity
                Interaction interaction = TARDISDisplayItemUtils.getInteraction(dd.getLocation());
                if (interaction != null) {
                    interaction.remove();
                }
                boolean found = false;
                for (Entity e : world.getNearbyEntities(dd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                    if (e instanceof ArmorStand a) {
                        stand = a;
                        found = true;
                        break;
                    }
                    if (e instanceof ItemFrame f) {
                        frame = f;
                        found = true;
                    }
                }
                if (!found || (stand == null && frame != null)) {
                    if (frame != null) {
                        frame.remove();
                    }
                    // spawn armour stand
                    stand = (ArmorStand) world.spawnEntity(dd.getLocation().clone().add(0.5d, 0, 0.5d), EntityType.ARMOR_STAND);
                }
                stand.setRotation(dd.getDirection().getYaw(), 0.0f);
                is = ItemStack.of(dye, 1);
                // only play the sound if the player is outside the TARDIS
                if (dd.isOutside()) {
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, dd.getPlayer().getUniqueId().toString());
                    boolean minecart = false;
                    SpaceTimeThrottle spaceTimeThrottle = SpaceTimeThrottle.NORMAL;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                        spaceTimeThrottle = SpaceTimeThrottle.getByDelay().get(rsp.getThrottle());
                    }
                    if (!minecart) {
                        String sound = switch (spaceTimeThrottle) {
                            case WARP, RAPID, FASTER -> "tardis_takeoff_" + spaceTimeThrottle.toString().toLowerCase(Locale.ROOT);
                            default -> "tardis_takeoff"; // NORMAL
                        };
                        TARDISSounds.playTARDISSound(dd.getLocation(), sound);
                    } else {
                        world.playSound(dd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                    }
                }
                if (dd.hasParticles()) {
                    ResultSetParticlePrefs rspp = new ResultSetParticlePrefs(plugin);
                    UUID uuid = dd.getPlayer().getUniqueId();
                    if (rspp.fromUUID(uuid.toString())) {
                        ParticleData data = rspp.getData();
                        // display particles
                        Emitter emitter = new Emitter(plugin, uuid, dd.getLocation(), data, dd.getThrottle().getFlightTime());
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, data.getShape().getPeriod());
                        emitter.setTaskID(task);
                    }
                }
            }
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                im.setItemModel(model);
                is.setItemMeta(im);
                stand.getEquipment().setHelmet(is, true);
                stand.setInvulnerable(true);
                stand.setInvisible(true);
            }
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            light.setBlockData(TARDISConstants.AIR);
            new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, dd.isHide(), preset);
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
