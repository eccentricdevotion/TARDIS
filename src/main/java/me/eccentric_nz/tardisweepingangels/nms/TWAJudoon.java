package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;

import java.util.UUID;

public class TWAJudoon extends TWAFollower {

    private int ammo;
    private boolean guard;

    public TWAJudoon(Level world, UUID owner) {
        super(world, owner);
        this.guard = false;
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD)) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            CompoundTag nbt = is.getTag();
            if (!isPathFinding()) {
                Bukkit.getScheduler().cancelTask(task);
                nbt.putInt("CustomModelData", 405 + (this.guard ? 6 : 0));
                isAnimating = false;
            } else if (!isAnimating) {
                // play move animation
                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    nbt.putInt("CustomModelData", 400 + frames[i] + (this.guard ? 6 : 0));
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

    public int getAmmo() {
        return ammo;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public boolean isGuard() {
        return guard;
    }

    public void setGuard(boolean guard) {
        this.guard = guard;
    }
}
