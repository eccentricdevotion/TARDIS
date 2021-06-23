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

import me.eccentric_nz.tardis.TardisConstants;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.travel.TardisDoorLocation;
import me.eccentric_nz.tardis.utility.TardisBlockSetters;
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

public class TardisInstantPoliceBox {

    private final TardisPlugin plugin;
    private final BuildData bd;
    private final Preset preset;

    public TardisInstantPoliceBox(TardisPlugin plugin, BuildData bd, Preset preset) {
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
        if (plugin.getTrackerKeeper().getRescue().containsKey(bd.getTardisId())) {
            UUID playerUUID = plugin.getTrackerKeeper().getRescue().get(bd.getTardisId());
            Player saved = plugin.getServer().getPlayer(playerUUID);
            if (saved != null) {
                TardisDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, bd.getTardisId());
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
        TardisBuilderUtility.saveDoorLocation(bd);
        TardisBuilderUtility.updateChameleonDemat(preset.toString(), bd.getTardisId());
        plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisId());
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
        if (!found) {
            Block block = bd.getLocation().getBlock();
            Block under = block.getRelative(BlockFace.DOWN);
            block.setBlockData(TardisConstants.AIR);
            TardisBlockSetters.setUnderDoorBlock(world, under.getX(), under.getY(), under.getZ(), bd.getTardisId(), false);
            // spawn item frame
            frame = (ItemFrame) world.spawnEntity(bd.getLocation(), EntityType.ITEM_FRAME);
        }
        frame.setFacingDirection(BlockFace.UP);
        frame.setRotation(bd.getDirection().getRotation());
        Material dye = TardisBuilderUtility.getMaterialForItemFrame(preset);
        ItemStack is = new ItemStack(dye, 1);
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setCustomModelData(1001);
        if (bd.shouldAddSign()) {
            String pb = (preset.equals(Preset.WEEPING_ANGEL)) ? "Weeping Angel" : "Police Box";
            im.setDisplayName(bd.getPlayer().getName() + "'s " + pb);
        }
        is.setItemMeta(im);
        frame.setItem(is, false);
        frame.setFixed(true);
        frame.setVisible(false);
    }
}
