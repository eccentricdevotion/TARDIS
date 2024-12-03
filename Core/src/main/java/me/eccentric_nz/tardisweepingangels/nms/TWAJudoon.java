package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.TARDIS.custommodels.keys.JudoonVariant;
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
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R3.CraftServer;
import org.bukkit.craftbukkit.v1_21_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TWAJudoon extends TWAFollower {

    private static final String entityId = "judoon";
    private final NamespacedKey[] frames = new NamespacedKey[]{
            JudoonVariant.JUDOON_0.getKey(),
            JudoonVariant.JUDOON_1.getKey(),
            JudoonVariant.JUDOON_2.getKey(),
            JudoonVariant.JUDOON_1.getKey(),
            JudoonVariant.JUDOON_0.getKey(),
            JudoonVariant.JUDOON_3.getKey(),
            JudoonVariant.JUDOON_4.getKey(),
            JudoonVariant.JUDOON_3.getKey()
    };
    private final NamespacedKey[] framesGuard = new NamespacedKey[]{
            JudoonVariant.JUDOON_GUARD_0.getKey(),
            JudoonVariant.JUDOON_GUARD_1.getKey(),
            JudoonVariant.JUDOON_GUARD_2.getKey(),
            JudoonVariant.JUDOON_GUARD_1.getKey(),
            JudoonVariant.JUDOON_GUARD_0.getKey(),
            JudoonVariant.JUDOON_GUARD_3.getKey(),
            JudoonVariant.JUDOON_GUARD_4.getKey(),
            JudoonVariant.JUDOON_GUARD_3.getKey()
    };
    private int ammo;
    private boolean guard;

    public TWAJudoon(Level world) {
        super(EntityType.HUSK, world);
        this.guard = false;
    }

    public TWAJudoon(EntityType<? extends Husk> entityType, Level level) {
        super(EntityType.HUSK, level);
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
                im.setItemModel(this.guard ? JudoonVariant.JUDOON_STATIC.getKey() : JudoonVariant.JUDOON_GUARD.getKey());
                i = 0;
            } else {
                // play move animation
                im.setItemModel(this.guard ? framesGuard[i] : frames[i]);
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
