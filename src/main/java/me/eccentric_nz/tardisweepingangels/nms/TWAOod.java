package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;

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
            CompoundTag nbt = is.getTag();
            if (!isPathFinding()) {
                Bukkit.getScheduler().cancelTask(task);
                int cmd = 405 + colour.getStep();
                if (redeye) {
                    cmd += 18;
                }
                nbt.putInt("CustomModelData", cmd);
                isAnimating = false;
            } else if (!isAnimating) {
                // play move animation
                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    int cmd = 400 + colour.getStep();
                    if (redeye) {
                        cmd += 18;
                    }
                    nbt.putInt("CustomModelData", cmd + frames[i]);
                    i++;
                    if (i == frames.length) {
                        i = 0;
                    }
                }, 1L, 3L);
                isAnimating = true;
            }
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
