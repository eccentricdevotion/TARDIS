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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetColour;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.v1_21_R2.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISInstantPoliceBox {

    private final TARDIS plugin;
    private final BuildData bd;
    private final ChameleonPreset preset;

    public TARDISInstantPoliceBox(TARDIS plugin, BuildData bd, ChameleonPreset preset) {
        this.plugin = plugin;
        this.bd = bd;
        this.preset = preset;
    }

    /**
     * Builds the TARDIS Preset.
     */
    public void buildPreset() {
        World world = bd.getLocation().getWorld();
        // rescue player?
        if (plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisID())) {
            UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisID());
            Player saved = plugin.getServer().getPlayer(playerUUID);
            if (saved != null) {
                TARDISDoorLocation idl = TARDISDoorListener.getDoor(1, bd.getTardisID());
                Location l = idl.getL();
                plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds(), false);
                // put player into travellers table
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", bd.getTardisID());
                set.put("uuid", playerUUID.toString());
                plugin.getQueryFactory().doInsert("travellers", set);
            }
            plugin.getTrackerKeeper().getRescue().remove(bd.getTardisID());
        }
        TARDISBuilderUtility.saveDoorLocation(bd);
        TARDISBuilderUtility.updateChameleonDemat(preset.toString(), bd.getTardisID());
        plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisID());
        ItemFrame frame = null;
        ArmorStand stand = null;
        boolean found = false;
        for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
            if (e instanceof ArmorStand a) {
                if (((CraftArmorStand) a).getHandle() instanceof TARDISArmourStand) {
                    stand = a;
                } else {
                    stand = (ArmorStand) VehicleUtility.spawnStand(bd.getLocation()).getBukkitEntity();
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
        Block block = bd.getLocation().getBlock();
        if (!found || (stand == null && frame != null)) {
            if (frame != null) {
                frame.remove();
            }
            Block under = block.getRelative(BlockFace.DOWN);
            block.setBlockData(TARDISConstants.AIR);
            TARDISBlockSetters.setUnderDoorBlock(world, under.getX(), under.getY(), under.getZ(), bd.getTardisID(), false);
            // spawn armour stand
            stand = (ArmorStand) VehicleUtility.spawnStand(bd.getLocation()).getBukkitEntity();
        }
        stand.setRotation(bd.getDirection().getYaw(), 0.0f);
        Material dye = TARDISBuilderUtility.getMaterialForArmourStand(preset, bd.getTardisID(), true);
        ItemStack is = new ItemStack(dye, 1);
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(1001);
        if (bd.shouldAddSign()) {
            String pb = "";
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
            String name = bd.getPlayer().getName() + "'s " + pb;
            im.setDisplayName(name);
            stand.setCustomName(name);
            stand.setCustomNameVisible(true);
        }
        if (preset == ChameleonPreset.COLOURED) {
            // get the colour
            ResultSetColour rsc = new ResultSetColour(plugin, bd.getTardisID());
            if (rsc.resultSet()) {
                LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) im;
                leatherArmorMeta.setColor(Color.fromRGB(rsc.getRed(), rsc.getGreen(), rsc.getBlue()));
                is.setItemMeta(leatherArmorMeta);
            }
        } else {
            is.setItemMeta(im);
        }
        EntityEquipment ee = stand.getEquipment();
        ee.setHelmet(is, true);
//        stand.setInvulnerable(true);
//        stand.setInvisible(true);
//        stand.setGravity(false);
        // set a light block
        Levelled levelled = TARDISConstants.LIGHT;
        // set light level from exterior lamp control
        levelled.setLevel(bd.getExteriorLampLevel());
        block.getRelative(BlockFace.UP, 2).setBlockData(levelled);
        // add an interaction entity
        TARDISDisplayItemUtils.setInteraction(stand, bd.getTardisID());
    }
}
