package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
                TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, bd.getTardisID());
                Location l = idl.getL();
                plugin.getGeneralKeeper().getDoorListener().movePlayer(saved, l, false, world, false, 0, bd.useMinecartSounds());
                // put player into travellers table
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", bd.getTardisID());
                set.put("uuid", playerUUID.toString());
                plugin.getQueryFactory().doInsert("travellers", set);
            }
            plugin.getTrackerKeeper().getRescue().remove(bd.getTardisID());
        }
        TARDISBuilderUtility.saveDoorLocation(bd);
        plugin.getGeneralKeeper().getProtectBlockMap().put(bd.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation().toString(), bd.getTardisID());
        ItemFrame frame = null;
        boolean found = false;
        for (Entity e : world.getNearbyEntities(bd.getLocation(), 1.0d, 1.0d, 1.0d)) {
            if (e instanceof ItemFrame) {
                frame = (ItemFrame) e;
                found = true;
            }
        }
        if (!found) {
            // spawn item frame
            frame = (ItemFrame) world.spawnEntity(bd.getLocation(), EntityType.ITEM_FRAME);
        }
        frame.setFacingDirection(BlockFace.UP);
        frame.setRotation(bd.getDirection().getRotation());
        Material dye = TARDISBuilderUtility.getDyeMaterial(preset);
        ItemStack is = new ItemStack(dye, 1);
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(10001);
        if (bd.shouldAddSign()) {
            im.setDisplayName(bd.getPlayer().getName() + "'s Police Box");
        }
        is.setItemMeta(im);
        frame.setItem(is);
    }
}
