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
package me.eccentric_nz.TARDIS.builders.exterior;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.database.data.ParticleData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetColour;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetParticlePrefs;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.flight.FlightEnd;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.particles.Emitter;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.v1_21_R4.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

public class TARDISMaterialisePoliceBox implements Runnable {

    private final TARDIS plugin;
    private final BuildData bd;
    private final int loops;
    private final ChameleonPreset preset;
    private int task;
    private int i;
    private ItemFrame frame;
    private ArmorStand stand;
    private ItemStack is;
    private Color colour = null;
    private String pb;

    public TARDISMaterialisePoliceBox(TARDIS plugin, BuildData bd, ChameleonPreset preset) {
        this.plugin = plugin;
        this.bd = bd;
        loops = this.bd.getThrottle().getLoops();
        this.preset = preset;
        i = 0;
    }

    @Override
    public void run() {
        if (plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisID())) {
            return;
        }
        World world = bd.getLocation().getWorld();
        Block block = bd.getLocation().getBlock();
        Block light = block.getRelative(BlockFace.UP, 2);
        NamespacedKey model;
        Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, bd.getTardisID(), true);
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
                    case GRAY_STAINED_GLASS_PANE -> ChameleonVariant.WEEPING_ANGEL_STAINED.getKey();
                    case ENDER_PEARL -> ChameleonVariant.PANDORICA_STAINED.getKey();
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
                    case ENDER_PEARL -> ChameleonVariant.PANDORICA_CLOSED.getKey();
                    case LEATHER_HORSE_ARMOR -> ColouredVariant.TINTED_CLOSED.getKey();
                    default -> new NamespacedKey(plugin, TARDISBuilderUtility.getCustomModelPath(dye.toString()) + "_closed");
                };
                // set a light block
                Levelled levelled = TARDISConstants.LIGHT;
                // set light level from exterior lamp control
                levelled.setLevel(bd.getExteriorLampLevel());
                light.setBlockData(levelled);
            }
            i++;
            // first run
            if (i == 1) {
                Block under = block.getRelative(BlockFace.DOWN);
                boolean slab = Tag.SLABS.isTagged(under.getType());
                TARDISBuilderUtility.saveDoorLocation(bd, slab);
                Location spawn = slab ? bd.getLocation().subtract(0, 0.5d, 0) : bd.getLocation();
                // add under block to Protected Block Map
                TARDISBlockSetters.rememberBlock(under, bd.getTardisID());
                boolean found = false;
                for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                    if (e instanceof ArmorStand a) {
                        if (((CraftArmorStand) a).getHandle() instanceof TARDISArmourStand) {
                            stand = a;
                        } else {
                            stand = (ArmorStand) VehicleUtility.spawnStand(spawn).getBukkitEntity();
                            a.remove();
                        }
                        found = true;
                        break;
                    }
                    if (e instanceof ItemFrame f) {
                        frame = f;
                        found = true;
                        break;
                    }
                }
                if (!found || (stand == null && frame != null)) {
                    if (frame != null) {
                        frame.remove();
                    }
                    if (!slab) {
                        TARDISBlockSetters.setUnderDoorBlock(world, under.getX(), under.getY(), under.getZ(), bd.getTardisID(), false);
                    }
                    // spawn an armour stand
                    stand = (ArmorStand) VehicleUtility.spawnStand(spawn).getBukkitEntity();
                }
                stand.setRotation(bd.getDirection().getYaw(), 0.0f);
                is = new ItemStack(dye, 1);
                if (bd.isOutside()) {
                    if (!bd.useMinecartSounds()) {
                        String sound;
                        if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                            sound = "junk_land";
                        } else {
                            sound = switch (bd.getThrottle()) {
                                case WARP, RAPID, FASTER ->
                                        "tardis_land_" + bd.getThrottle().toString().toLowerCase(Locale.ROOT);
                                default -> "tardis_land"; // NORMAL
                            };
                        }
                        TARDISSounds.playTARDISSound(bd.getLocation(), sound);
                    } else {
                        world.playSound(bd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                    }
                }
                switch (preset) {
                    case WEEPING_ANGEL -> pb = "Weeping Angel";
                    case PANDORICA -> pb = "Pandorica";
                    case ITEM -> {
                        for (String k : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
                            if (dye.toString().equals(plugin.getCustomModelConfig().getString("models." + k + ".item"))) {
                                pb = k;
                                break;
                            }
                        }
                    }
                    default -> pb = "Police Box";
                }
                if (preset == ChameleonPreset.COLOURED) {
                    // get the colour
                    ResultSetColour rsc = new ResultSetColour(plugin, bd.getTardisID());
                    if (rsc.resultSet()) {
                        colour = Color.fromRGB(rsc.getRed(), rsc.getGreen(), rsc.getBlue());
                    }
                }
                if (bd.hasParticles()) {
                    ResultSetParticlePrefs rspp = new ResultSetParticlePrefs(plugin);
                    UUID uuid = bd.getPlayer().getUniqueId();
                    if (rspp.fromUUID(uuid.toString())) {
                        ParticleData data = rspp.getData();
                        // display particles
                        Emitter emitter = new Emitter(plugin, uuid, spawn, data, bd.getThrottle().getFlightTime());
                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, emitter, 0, data.getShape().getPeriod());
                        emitter.setTaskID(task);
                    }
                }
            }
            ItemMeta im = is.getItemMeta();
            im.setItemModel(model);
            if (bd.shouldAddSign()) {
                String name = bd.getPlayer().getName() + "'s " + pb;
                im.setDisplayName(name);
                stand.setCustomName(name);
                stand.setCustomNameVisible(true);
            }
            if (model == ColouredVariant.TINTED_CLOSED.getKey() && preset == ChameleonPreset.COLOURED && colour != null) {
                // set the colour
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) im;
                leatherArmorMeta.setColor(colour);
                is.setItemMeta(leatherArmorMeta);
            } else {
                is.setItemMeta(im);
            }
            EntityEquipment ee = stand.getEquipment();
            ee.setHelmet(is, true);
        } else {
            // remove trackers
            plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(bd.getTardisID()));
            plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(bd.getTardisID()));
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            plugin.getTrackerKeeper().getMalfunction().remove(bd.getTardisID());
            if (plugin.getTrackerKeeper().getDidDematToVortex().contains(bd.getTardisID())) {
                plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(bd.getTardisID()));
            }
            if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(bd.getTardisID())) {
                int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(bd.getTardisID());
                plugin.getServer().getScheduler().cancelTask(taskID);
                plugin.getTrackerKeeper().getDestinationVortex().remove(bd.getTardisID());
            }
            if (!bd.isRebuild()) {
                plugin.getTrackerKeeper().getActiveForceFields().remove(bd.getPlayer().getPlayer().getUniqueId());
            }
            // add an interaction entity
            TARDISDisplayItemUtils.setInteraction(stand, bd.getTardisID());
            // message travellers in tardis
            if (loops > 3) {
                new FlightEnd(plugin).process(bd.getTardisID(), bd.getPlayer().getPlayer(), bd.isMalfunction(), bd.isRebuild());
                // update demat field in database
                String update = preset.toString();
                if (preset == ChameleonPreset.ITEM) {
                    update = "ITEM:" + pb;
                }
                TARDISBuilderUtility.updateChameleonDemat(update, bd.getTardisID());
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
