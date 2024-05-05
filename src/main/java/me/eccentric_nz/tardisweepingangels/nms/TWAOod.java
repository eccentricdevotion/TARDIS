package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class TWAOod extends TWAFollower {

    private boolean redeye;
    private OodColour colour;

    public TWAOod(Level world, UUID owner) {
        super(world, owner);
        this.redeye = false;
        this.colour = OodColour.BLACK;
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD)) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (!isPathFinding()) {
                Bukkit.getScheduler().cancelTask(task);
                int cmd = 405 + colour.getStep();
                if (redeye) {
                    cmd += 18;
                }
                im.setCustomModelData(cmd);
                bukkit.setItemMeta(im);
                isAnimating = false;
            } else if (!isAnimating) {
                // play move animation
                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    int cmd = 400 + colour.getStep();
                    if (redeye) {
                        cmd += 18;
                    }
                    im.setCustomModelData(cmd + frames[i]);
                    i++;
                    if (i == frames.length) {
                        i = 0;
                    }
                }, 1L, 3L);
                isAnimating = true;
            }
            setItemSlot(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(bukkit));
        }
        super.aiStep();
    }

    public boolean isRedeye() {
        return redeye;
    }

    public void setRedeye(boolean redeye) {
        this.redeye = redeye;
    }

    public OodColour getColour() {
        return colour;
    }

    public void setColour(OodColour colour) {
        this.colour = colour;
    }
}
