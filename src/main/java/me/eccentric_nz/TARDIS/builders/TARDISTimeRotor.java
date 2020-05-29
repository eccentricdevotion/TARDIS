package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.UUID;

public class TARDISTimeRotor {

    public static void setItemFrame(SCHEMATIC schm, Location location, int id) {
        location.getBlock().setBlockData(TARDISConstants.VOID_AIR);
        ItemFrame itemFrame = (ItemFrame) location.getWorld().spawnEntity(location, EntityType.ITEM_FRAME);
        itemFrame.setFacingDirection(BlockFace.UP);
        setRotorStatic(schm, itemFrame);
        // save itemFrame UUID
        updateRotorRecord(id, itemFrame.getUniqueId().toString());
    }

    public static void updateRotorRecord(int id, String uuid) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("rotor", uuid);
        TARDIS.plugin.getQueryFactory().doUpdate("tardis", set, where);
    }

    public static void setRotorStatic(SCHEMATIC schm, ItemFrame itemFrame) {
        String displayName;
        int which;
        switch (schm.getPermission()) {
            case "default":
                displayName = "Time Rotor 1 Off";
                which = 63;
                break;
            case "rotor":
                displayName = "Time Rotor 2 Off";
                which = 64;
                break;
            default: // early
                displayName = "Time Rotor 3 Off";
                which = 65;
                break;
        }
        ItemStack is = new ItemStack(Material.MUSHROOM_STEM, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(displayName);
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which);
        im.setCustomModelData(10000000 + which);
        is.setItemMeta(im);
        itemFrame.setItem(is);
    }

    public static void setRotorAnimated(SCHEMATIC schm, ItemFrame itemFrame) {
        String displayName;
        int which;
        switch (schm.getPermission()) {
            case "default":
                displayName = "Time Rotor 1";
                which = 60;
                break;
            case "rotor":
                displayName = "Time Rotor 2";
                which = 61;
                break;
            default: // early
                displayName = "Time Rotor 3";
                which = 62;
                break;
        }
        ItemStack is = new ItemStack(Material.MUSHROOM_STEM, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(displayName);
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, which);
        im.setCustomModelData(10000000 + which);
        is.setItemMeta(im);
        itemFrame.setItem(is);
    }

    public static ItemFrame getItemFrame(UUID uuid) {
        ItemFrame itemFrame = (ItemFrame) Bukkit.getEntity(uuid);
        return itemFrame;
    }
}
