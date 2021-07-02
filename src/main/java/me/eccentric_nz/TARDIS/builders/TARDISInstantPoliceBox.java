/*
 * Copyright (C) 2021 eccentric_nz
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
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

public class TARDISInstantPoliceBox {

    private final TARDIS plugin;
    private final BuildData bd;
    private final PRESET preset;

    public TARDISInstantPoliceBox(TARDIS plugin, BuildData bd, PRESET preset) {
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
        boolean found = false;
        assert world != null;
        for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
            if (e instanceof ItemFrame) {
                frame = (ItemFrame) e;
                found = true;
                break;
            }
        }
        Block block = bd.getLocation().getBlock();
        if (!found) {
            Block under = block.getRelative(BlockFace.DOWN);
            block.setBlockData(TARDISConstants.AIR);
            TARDISBlockSetters.setUnderDoorBlock(world, under.getX(), under.getY(), under.getZ(), bd.getTardisID(), false);
            // spawn item frame
            frame = (ItemFrame) world.spawnEntity(bd.getLocation(), EntityType.ITEM_FRAME);
        }
        frame.setFacingDirection(BlockFace.UP);
        frame.setRotation(bd.getDirection().getRotation());
        Material dye = TARDISBuilderUtility.getMaterialForItemFrame(preset);
        ItemStack is = new ItemStack(dye, 1);
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setCustomModelData(1001);
        if (bd.shouldAddSign()) {
            String pb = (preset.equals(PRESET.WEEPING_ANGEL)) ? "Weeping Angel" : "Police Box";
            im.setDisplayName(bd.getPlayer().getName() + "'s " + pb);
        }
        is.setItemMeta(im);
        frame.setItem(is, false);
        frame.setFixed(true);
        frame.setVisible(false);
        // set a light block
        block.getRelative(BlockFace.UP, 2).setBlockData(TARDISConstants.LIGHT);
    }
}
