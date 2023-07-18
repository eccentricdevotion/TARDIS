package me.eccentric_nz.TARDIS.builders;

import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TimeRotorAnimation implements Runnable {

    private final ItemFrame frame;
    private final int[] frames;
    private int i = 1;

    public TimeRotorAnimation(ItemFrame frame, int[] frames) {
        this.frame = frame;
        this.frames = frames;
    }

    @Override
    public void run() {
        ItemStack is = frame.getItem();
        ItemMeta im = is.getItemMeta();
        im.setCustomModelData(1021 + frames[i]);
        is.setItemMeta(im);
        frame.setItem(is, false);
        i++;
        if (i == frames.length) {
            i = 0;
        }
    }
}
