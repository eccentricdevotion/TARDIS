package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.TARDIS.TARDIS;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.UUID;

public class TWAK9 extends TWAFollower {

    private static final String ENTITY_ID = "k9";

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

    public TWAK9(Level world, UUID owner) {
        super(world, owner);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD)) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (!isPathFinding()) {
                Bukkit.getScheduler().cancelTask(task);
                im.setCustomModelData(400);
                bukkit.setItemMeta(im);
                isAnimating = false;
            } else if (!isAnimating) {
                // play move animation
                task = Bukkit.getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    im.setCustomModelData(400 + frames[i]);
                    bukkit.setItemMeta(im);
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
}
