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
package me.eccentric_nz.TARDIS.rotors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.RotorVariant;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
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

    public static void setRotor(NamespacedKey key, ItemFrame itemFrame) {
        ItemStack is = new ItemStack(Material.LIGHT_GRAY_DYE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Time Rotor");
        im.setItemModel(key);
        is.setItemMeta(im);
        itemFrame.setItem(is, false);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
        itemFrame.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, key.getKey());
    }

    public static void setRotor(Rotor which, ItemFrame itemFrame) {
        ItemStack is = new ItemStack(which.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Time Rotor");
        im.setItemModel(which.getOffModel());
        is.setItemMeta(im);
        itemFrame.setItem(is, false);
        itemFrame.setFixed(true);
        itemFrame.setVisible(false);
        itemFrame.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.STRING, which.getOffModel().getKey());
        if (!which.getName().equals("twelfth")) {
            // start repeating animation task
            int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, new TimeRotorAnimation(itemFrame, which.getFrames(), which.getName()), which.getFrameTick(), which.getFrameTick());
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

    public static NamespacedKey getRotorModel(ItemFrame itemFrame) {
        ItemStack is = itemFrame.getItem();
        if (is.hasItemMeta()) {
            ItemMeta im = is.getItemMeta();
            if (im.hasItemModel()) {
                return im.getItemModel();
            } else {
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                List<Float> floats = component.getFloats();
                if (floats.size() > 0) {
                    int cmd = floats.getFirst().intValue();
                    switch (cmd) {
                        case 10000002 -> { return RotorVariant.TIME_ROTOR_EARLY_OFF.getKey(); }
                        case 10000003 -> { return RotorVariant.TIME_ROTOR_TENNANT_OFF.getKey(); }
                        case 10000004 -> { return RotorVariant.TIME_ROTOR_ELEVENTH_OFF.getKey(); }
                        case 10000005 -> { return RotorVariant.TIME_ROTOR_TWELFTH_OFF.getKey(); }
                        case 10000006 -> { return RotorVariant.TIME_ROTOR_DELTA_OFF.getKey(); }
                        case 10000007 -> { return RotorVariant.ENGINE_OFF.getKey(); }
                        case 10000008 -> { return RotorVariant.ENGINE_ROTOR_OFF.getKey(); }
                        case 10000009 -> { return RotorVariant.HOSPITAL_OFF.getKey(); }
                        case 10000100 -> { return RotorVariant.TIME_ROTOR_CONSOLE_OFF.getKey(); }
                        case 10000101 -> { return RotorVariant.TIME_ROTOR_RUSTIC_OFF.getKey(); }
                    }
                }
            }
        }
        return RotorVariant.TIME_ROTOR_EARLY_OFF.getKey();
    }

    public static NamespacedKey getRotorOffModel(ItemFrame itemFrame) {
        ItemStack is = itemFrame.getItem();
        Material material = is.getType();
        Rotor rotor = Rotor.getByMaterial(material);
        return rotor.getOffModel();
    }
}
