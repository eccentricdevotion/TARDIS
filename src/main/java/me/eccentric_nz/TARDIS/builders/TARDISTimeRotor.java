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
import me.eccentric_nz.TARDIS.enumeration.Rotor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

/**
 * The time rotor, sometimes called the time column is a component in the central column of the TARDIS console. While
 * the TARDIS is in flight, the rotor rises and falls, stopping when the TARDIS handbrake is engaged. It is associated
 * with the 'whooshing' noise heard when the TARDIS is in flight.
 */
public class TARDISTimeRotor {

    public static final HashMap<UUID, Integer> ANIMATED_ROTORS = new HashMap<>();

    public static void updateRotorRecord(int id, String uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("rotor", uuid);
        TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, where);
    }

    public static void setRotor(int which, ItemFrame itemFrame) {
        ItemStack is = new ItemStack(Material.LIGHT_GRAY_DYE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Time Rotor");
        im.setCustomModelData(which);
        is.setItemMeta(im);
        itemFrame.setItem(is, false);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
        itemFrame.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which);
    }

    public static void setRotor(Rotor which, ItemFrame itemFrame) {
        ItemStack is = new ItemStack(which.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Time Rotor");
        im.setCustomModelData(1021);
        is.setItemMeta(im);
        itemFrame.setItem(is, false);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
        itemFrame.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which.getOffModelData());
        if (which != Rotor.TWELFTH) {
            // start repeating animation task
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, new TimeRotorAnimation(itemFrame, which.getFrames()), which.getFrameTick(), which.getFrameTick());
            // add item frame uuid and task id to map for tracking
            ANIMATED_ROTORS.put(itemFrame.getUniqueId(), task);
        }
    }

    public static void unlockItemFrame(ItemFrame itemFrame) {
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
            if (im.hasCustomModelData()) {
                return im.getCustomModelData();
            }
        }
        return 10000002;
    }

    public static int getRotorOffModelData(ItemFrame itemFrame) {
        ItemStack is = itemFrame.getItem();
        Material material = is.getType();
        Rotor rotor = Rotor.getByMaterial(material);
        return rotor.getOffModelData();
    }
}
