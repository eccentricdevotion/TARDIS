package me.eccentric_nz.TARDIS.rotors;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TimeRotorAnimation implements Runnable {

    private final ItemFrame frame;
    private final int[] frames;
    private final String which;
    private int i = 1;

    public TimeRotorAnimation(ItemFrame frame, int[] frames, String which) {
        this.frame = frame;
        this.frames = frames;
        this.which = which;
    }

    @Override
    public void run() {
        ItemStack is = frame.getItem();
        if (!is.getType().isAir()) {
            ItemMeta im = is.getItemMeta();
            if (im != null) {
                im.setItemModel(new NamespacedKey(TARDIS.plugin, "time_rotor/" + which + "/" + which + "_" + frames[i]));
                is.setItemMeta(im);
                frame.setItem(is, false);
                i++;
                if (i == frames.length) {
                    i = 0;
                }
            }
        }
    }
}
