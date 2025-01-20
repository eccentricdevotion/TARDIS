package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.TARDIS.custommodels.keys.OodVariant;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TWAOod extends TWAFollower {

    private static final String entityId = "ood";
    private boolean redeye;
    private OodColour colour;
    
    public TWAOod(Level world) {
        super(EntityType.HUSK, world);
        this.redeye = false;
        this.colour = OodColour.BLACK;
    }

    public TWAOod(EntityType<Entity> entityType, Level level) {
        super(EntityType.HUSK, level);
        this.redeye = false;
        this.colour = OodColour.BLACK;
    }

    public static void injectEntity(ResourceLocation mcKey) throws NoSuchFieldException, IllegalAccessException {
        Registry<EntityType<?>> entityReg = ((CraftServer) Bukkit.getServer()).getServer().registryAccess().lookup(Registries.ENTITY_TYPE).orElseThrow(NoSuchFieldException::new);
        EntityRegistry.unfreeze();
        @SuppressWarnings("unchecked")
        Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())).findChoiceType(References.ENTITY).types();
        types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HUSK).toString()));
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace(entityId));
        EntityType<?> type = EntityType.Builder.of(TWAOod::new, MobCategory.MONSTER).noSummon().build(resourceKey);
        entityReg.createIntrusiveHolder(type);
        Registry.register(entityReg, entityId, type);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 10 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            im.setItemModel((redeye) ?OodVariant.OOD_REDEYE_HEAD.getKey() : OodVariant.OOD_HEAD.getKey());
            bukkit.setItemMeta(im);
            setItemSlot(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(bukkit));
        }
        super.aiStep();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:ood");
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
