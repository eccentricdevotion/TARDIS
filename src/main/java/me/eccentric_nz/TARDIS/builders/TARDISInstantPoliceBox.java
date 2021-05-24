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
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.TARDISConstants;
import me.eccentric_nz.tardis.enumeration.PRESET;
import me.eccentric_nz.tardis.travel.TARDISDoorLocation;
import me.eccentric_nz.tardis.utility.TARDISBlockSetters;
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

	private final TARDISPlugin plugin;
	private final BuildData bd;
	private final PRESET preset;

	public TARDISInstantPoliceBox(TARDISPlugin plugin, BuildData bd, PRESET preset) {
		this.plugin = plugin;
		this.bd = bd;
		this.preset = preset;
	}

	/**
	 * Builds the tardis Preset.
	 */
	public void buildPreset() {
		World world = bd.getLocation().getWorld();
		// rescue player?
		if (plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisId())) {
			UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisId());
			Player saved = plugin.getServer().getPlayer(playerUUID);
			if (saved != null) {
				TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, bd.getTardisId());
				Location l = idl.getL();
				plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds());
				// put player into travellers table
				HashMap<String, Object> set = new HashMap<>();
				set.put("tardis_id", bd.getTardisId());
				set.put("uuid", playerUUID.toString());
				plugin.getQueryFactory().doInsert("travellers", set);
			}
			plugin.getTrackerKeeper().getRescue().remove(bd.getTardisId());
		}
		TARDISBuilderUtility.saveDoorLocation(bd);
		plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisId());
		ItemFrame frame = null;
		boolean found = false;
		for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
			if (e instanceof ItemFrame) {
				frame = (ItemFrame) e;
				found = true;
				break;
			}
		}
		if (!found) {
			Block block = bd.getLocation().getBlock();
			Block under = block.getRelative(BlockFace.DOWN);
			block.setBlockData(TARDISConstants.AIR);
			TARDISBlockSetters.setUnderDoorBlock(world, under.getX(), under.getY(), under.getZ(), bd.getTardisId(), false);
			// spawn item frame
			frame = (ItemFrame) world.spawnEntity(bd.getLocation(), EntityType.ITEM_FRAME);
		}
		frame.setFacingDirection(BlockFace.UP);
		frame.setRotation(bd.getDirection().getRotation());
		Material dye = TARDISBuilderUtility.getDyeMaterial(preset);
		ItemStack is = new ItemStack(dye, 1);
		ItemMeta im = is.getItemMeta();
		im.setCustomModelData(1001);
		if (bd.shouldAddSign()) {
			im.setDisplayName(bd.getPlayer().getName() + "'s Police Box");
		}
		is.setItemMeta(im);
		frame.setItem(is, false);
		frame.setFixed(true);
		frame.setVisible(false);
	}
}
