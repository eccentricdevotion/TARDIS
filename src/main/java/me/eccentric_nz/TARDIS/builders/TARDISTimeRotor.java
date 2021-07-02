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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * The time rotor, sometimes called the time column is a component in the central column of the TARDIS console. While
 * the TARDIS is in flight, the rotor rises and falls, stopping when the TARDIS handbrake is engaged. It is associated
 * with the 'whooshing' noise heard when the TARDIS is in flight.
 */
public class TARDISTimeRotor {

    private static final HashMap<String, Integer> BY_NAME = new HashMap<>() {
        {
            put("early", 10000002);
            put("rotor", 10000003);
            put("copper", 10000004);
            put("round", 10000005);
            put("delta", 10000006);
        }
    };

    public static void setItemFrame(String schm, Location location, int id) {
        location.getBlock().setBlockData(TARDISConstants.VOID_AIR);
        ItemFrame itemFrame = (ItemFrame) Objects.requireNonNull(location.getWorld()).spawnEntity(location, EntityType.ITEM_FRAME);
        itemFrame.setFacingDirection(BlockFace.UP);
        setRotor(BY_NAME.get(schm), itemFrame, false);
        // save itemFrame UUID
        UUID uuid = itemFrame.getUniqueId();
        updateRotorRecord(id, uuid.toString());
        TARDIS.plugin.getGeneralKeeper().getTimeRotors().add(uuid);
    }

    public static void updateRotorRecord(int id, String uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("rotor", uuid);
        TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, where);
    }

    public static void setRotor(int which, ItemFrame itemFrame, boolean animated) {
        Material material = (animated) ? Material.LIGHT_BLUE_DYE : Material.LIGHT_GRAY_DYE;
        ItemStack is = new ItemStack(material, 1);
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setDisplayName("Time Rotor");
        im.setCustomModelData(which);
        is.setItemMeta(im);
        itemFrame.setItem(is, false);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
    }

    public static void unlockRotor(ItemFrame itemFrame) {
        itemFrame.setFixed(false);
        itemFrame.setVisible(true);
    }

    public static ItemFrame getItemFrame(UUID uuid) {
        return (ItemFrame) Bukkit.getEntity(uuid);
    }

    public static int getRotorModelData(ItemFrame itemFrame) {
        ItemStack is = itemFrame.getItem();
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            assert im != null;
            if (im.hasCustomModelData()) {
                return im.getCustomModelData();
            }
        }
        return 10000002;
    }
}
