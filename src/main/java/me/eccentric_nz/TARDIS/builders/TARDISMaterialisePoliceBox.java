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
package me.eccentric_nz.TARDIS.builders;

import java.util.Collections;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetColour;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.flight.FlightEnd;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

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
    private Material dye;
    private Color colour = null;
    private String pb;

    TARDISMaterialisePoliceBox(TARDIS plugin, BuildData bd, ChameleonPreset preset) {
        this.plugin = plugin;
        this.bd = bd;
        loops = this.bd.getThrottle().getLoops();
        this.preset = preset;
        i = 0;
    }

    @Override
    public void run() {
        if (!plugin.getTrackerKeeper().getDematerialising().contains(bd.getTardisID())) {
            World world = bd.getLocation().getWorld();
            Block block = bd.getLocation().getBlock();
            Block light = block.getRelative(BlockFace.UP, 2);
            if (i < loops) {
                i++;
                int cmd;
                switch (i % 3) {
                    case 2 -> { // stained
                        cmd = 1003;
                        light.setBlockData(TARDISConstants.AIR);
                    }
                    case 1 -> { // glass
                        cmd = 1004;
                        light.setBlockData(TARDISConstants.AIR);
                    }
                    default -> { // preset
                        cmd = 1001;
                        // set a light block
                        Levelled levelled = TARDISConstants.LIGHT;
                        levelled.setLevel(7);
                        light.setBlockData(levelled);
                    }
                }
                // first run
                if (i == 1) {
                    TARDISBuilderUtility.saveDoorLocation(bd);
                    plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisID());
                    boolean found = false;
                    for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                        if (e instanceof ArmorStand a) {
                            stand = a;
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
                        Block under = block.getRelative(BlockFace.DOWN);
                        block.setBlockData(TARDISConstants.AIR);
                        TARDISBlockSetters.setUnderDoorBlock(world, under.getX(), under.getY(), under.getZ(), bd.getTardisID(), false);
                        // spawn armour stand
                        stand = (ArmorStand) world.spawnEntity(bd.getLocation().clone().add(0.5d, 0, 0.5d), EntityType.ARMOR_STAND);
                    }
                    stand.setRotation(bd.getDirection().getYaw(), 0.0f);
                    dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, bd.getTardisID(), true);
                    is = new ItemStack(dye, 1);
                    if (bd.isOutside()) {
                        if (!bd.useMinecartSounds()) {
                            String sound;
                            if (preset.equals(ChameleonPreset.JUNK_MODE)) {
                                sound = "junk_land";
                            } else {
                                sound = switch (bd.getThrottle()) {
                                    case WARP, RAPID, FASTER ->
                                        "tardis_land_" + bd.getThrottle().toString().toLowerCase();
                                    default ->
                                        "tardis_land"; // NORMAL
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
                                if (plugin.getCustomModelConfig().getString("models." + k + ".item").equals(dye.toString())) {
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
                }
                ItemMeta im = is.getItemMeta();
                im.setCustomModelData(cmd);
                if (bd.shouldAddSign()) {
                    String name = bd.getPlayer().getName() + "'s " + pb;
                    im.setDisplayName(name);
                    stand.setCustomName(name);
                    stand.setCustomNameVisible(true);
                }
                if (cmd == 1001 && preset == ChameleonPreset.COLOURED && colour != null) {
                    // set the colour
                    LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) im;
                    leatherArmorMeta.setColor(colour);
                    is.setItemMeta(leatherArmorMeta);
                } else {
                    is.setItemMeta(im);
                }
                EntityEquipment ee = stand.getEquipment();
                ee.setHelmet(is, true);
                stand.setInvulnerable(true);
                stand.setInvisible(true);
                stand.setGravity(false);
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
                // message travellers in tardis
                if (loops > 3) {
                    new FlightEnd(plugin).process(bd.getTardisID(), bd.getPlayer().getPlayer(), bd.isMalfunction());
                    // update demat field in database
                    String update = preset.toString();
                    if (preset == ChameleonPreset.ITEM) {
                        update = "ITEM:" + pb;
                    }
                    TARDISBuilderUtility.updateChameleonDemat(update, bd.getTardisID());
                }
            }
        }
    }

    public void setTask(int task) {
        this.task = task;
    }
}
