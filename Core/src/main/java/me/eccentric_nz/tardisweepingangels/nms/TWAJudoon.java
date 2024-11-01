package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TWAJudoon extends TWAFollower {

    private static final String entityId = "judoon";
    private int ammo;
    private boolean guard;

    public TWAJudoon(Level world) {
        super(EntityType.HUSK, world);
        this.guard = false;
    }

    public TWAJudoon(EntityType entityType, Level level) {
        super(entityType, level);
        this.guard = false;
    }

    public static void injectEntity(ResourceLocation mcKey) throws NoSuchFieldException, IllegalAccessException {
        Registry<EntityType<?>> entityReg = ((CraftServer) Bukkit.getServer()).getServer().registryAccess().lookup(Registries.ENTITY_TYPE).orElseThrow(NoSuchFieldException::new);
        EntityRegistry.unfreeze();
        @SuppressWarnings("unchecked")
        Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())).findChoiceType(References.ENTITY).types();
        types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HUSK).toString()));
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace(entityId));
        EntityType<?> type = EntityType.Builder.of(TWAJudoon::new, MobCategory.MONSTER).noSummon().build(resourceKey);
        entityReg.createIntrusiveHolder(type);
        Registry.register(entityReg, entityId, type);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 3 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (oldX == getX() && oldZ == getZ()) {
                im.setCustomModelData(405 + (this.guard ? 6 : 0));
                i = 0;
            } else {
                // play move animation
                im.setCustomModelData(400 + frames[i] + (this.guard ? 6 : 0));
                i++;
                if (i == frames.length) {
                    i = 0;
                }
            }
            bukkit.setItemMeta(im);
            setItemSlot(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(bukkit));
            oldX = getX();
            oldZ = getZ();
        }
        super.aiStep();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:judoon");
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
