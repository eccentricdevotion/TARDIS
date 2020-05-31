package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISDematerialisePoliceBox implements Runnable {

    private final TARDIS plugin;
    private final DestroyData dd;
    private final int loops;
    private final PRESET preset;
    private int task;
    private int i;
    private ItemFrame frame;
    private ItemStack is;
    private ItemMeta im;
    private Material dye;

    public TARDISDematerialisePoliceBox(TARDIS plugin, DestroyData dd, int loops, PRESET preset) {
        this.plugin = plugin;
        this.dd = dd;
        this.loops = loops;
        this.preset = preset;
    }

    @Override
    public void run() {
        World world = dd.getLocation().getWorld();
        if (i < loops) {
            i++;
            int cmd;
            switch (i % 3) {
                case 2: // stained
                    cmd = 1003;
                    break;
                case 1: // glass
                    cmd = 1004;
                    break;
                default: // preset
                    cmd = 1001;
                    break;
            }
            // first run - play sound
            if (i == 1) {
                boolean found = false;
                for (Entity e : world.getNearbyEntities(dd.getLocation(), 1.0d, 1.0d, 1.0d)) {
                    if (e instanceof ItemFrame) {
                        frame = (ItemFrame) e;
                        found = true;
                    }
                }
                if (!found) {
                    // spawn item frame
                    frame = (ItemFrame) world.spawnEntity(dd.getLocation(), EntityType.ITEM_FRAME);
                }
                frame.setFacingDirection(BlockFace.UP);
                frame.setRotation(dd.getDirection().getRotation());
                dye = getDyeMaterial(preset);
                is = new ItemStack(dye, 1);
                // only play the sound if the player is outside the TARDIS
                if (dd.isOutside()) {
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, dd.getPlayer().getUniqueId().toString());
                    boolean minecart = false;
                    if (rsp.resultSet()) {
                        minecart = rsp.isMinecartOn();
                    }
                    if (!minecart) {
                        String sound = (preset.equals(PRESET.JUNK_MODE)) ? "junk_takeoff" : "tardis_takeoff";
                        TARDISSounds.playTARDISSound(dd.getLocation(), sound);
                    } else {
                        world.playSound(dd.getLocation(), Sound.ENTITY_MINECART_INSIDE, 1.0F, 0.0F);
                    }
                }
            }
            im = is.getItemMeta();
            im.setCustomModelData(cmd);
            is.setItemMeta(im);
            frame.setItem(is);
        } else {
            plugin.getServer().getScheduler().cancelTask(task);
            task = 0;
            new TARDISDeinstantPreset(plugin).instaDestroyPreset(dd, false, preset);
        }
    }

    private Material getDyeMaterial(PRESET preset) {
        String split = preset.toString().replace("POLICE_BOX_", "");
        String dye = split + "_DYE";
        return Material.valueOf(dye);
    }

    public void setTask(int task) {
        this.task = task;
    }
}
