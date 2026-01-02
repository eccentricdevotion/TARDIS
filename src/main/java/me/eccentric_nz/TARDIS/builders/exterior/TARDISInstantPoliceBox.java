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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.builders.exterior;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.flight.vehicle.TARDISArmourStand;
import me.eccentric_nz.TARDIS.flight.vehicle.VehicleUtility;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

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
        Block block = bd.getLocation().getBlock();
        Block under = block.getRelative(BlockFace.DOWN);
        boolean slab = Tag.SLABS.isTagged(under.getType());
        TARDISBuilderUtility.saveDoorLocation(bd, slab);
        TARDISBuilderUtility.updateChameleonDemat(preset.toString(), bd.getTardisID());
        plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisID());
        ItemFrame frame = null;
        ArmorStand stand = null;
        boolean found = false;
        Location spawn = slab ? bd.getLocation().subtract(0, 0.5d, 0) : bd.getLocation();
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
            // spawn armour stand
            stand = (ArmorStand) VehicleUtility.spawnStand(spawn).getBukkitEntity();
        }
        stand.setRotation(bd.getDirection().getYaw(), 0.0f);
        TARDISBuilderUtility.setPoliceBoxHelmet(plugin, preset, bd, stand);
        // set a light block
        Levelled levelled = TARDISConstants.LIGHT;
        // set light level from exterior lamp control
        levelled.setLevel(bd.getExteriorLampLevel());
        block.getRelative(BlockFace.UP, 2).setBlockData(levelled);
        // add an interaction entity
        TARDISDisplayItemUtils.setInteraction(stand, bd.getTardisID());
    }
}
