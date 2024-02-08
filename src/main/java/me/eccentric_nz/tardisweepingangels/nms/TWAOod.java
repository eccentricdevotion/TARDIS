package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;

public class TWAOod extends TWAFollower {

    private static final String ENTITY_ID = "ood";

    static {
        ResourceLocation mcKey = new ResourceLocation(ENTITY_ID);
        try {
            if (BuiltInRegistries.ENTITY_TYPE.getOptional(mcKey).isEmpty()) {
                unfreezeEntityRegistry();
                @SuppressWarnings("unchecked")
                Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())).findChoiceType(References.ENTITY).types();
                types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HUSK).toString()));
                Registry.register(BuiltInRegistries.ENTITY_TYPE, ENTITY_ID, EntityType.Builder.of(EntityType::create, MobCategory.MONSTER).noSummon().build(ENTITY_ID));
                BuiltInRegistries.ENTITY_TYPE.freeze();
            }
        } catch (NoSuchFieldException | IllegalAccessException ignore) {
        }
    }

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
