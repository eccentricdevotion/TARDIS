package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.TardisDoorVariant;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DoorAnimator {

    private final TARDIS plugin;
    private final ItemDisplay display;

    private int taskID;
    private NamespacedKey model = null;
    private int frame = 0;

    public DoorAnimator(TARDIS plugin, ItemDisplay display) {
        this.plugin = plugin;
        this.display = display;
    }

    public void animate(boolean close) {
        Location location = display.getLocation();
        ItemStack is = display.getItemStack();
        Material material = is.getType();
        DoorAnimationData data = close ? Door.getCloseData(material) : Door.getOpenData(material);
        TARDISSounds.playTARDISSound(location, data.getSound());
        taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (frame > data.getAnimation().length - 1) {
                plugin.getServer().getScheduler().cancelTask(taskID);
                taskID = 0;
                return;
            }
            if (material == Material.IRON_DOOR && !plugin.getConfig().getBoolean("police_box.animated_door")) {
                model = close ? TardisDoorVariant.TARDIS_DOOR_CLOSED.getKey() : TardisDoorVariant.TARDIS_DOOR_OPEN.getKey();
                frame = data.getAnimation().length - 1;
            } else {
                model = data.getAnimation()[frame];
            }
            ItemMeta im = is.getItemMeta();
            im.setItemModel(model);
            is.setItemMeta(im);
            display.setItemStack(is);
            frame++;
        }, 2L, data.getTicks());
    }
}
