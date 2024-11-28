package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.TARDIS.custommodeldata.keys.RottenFlesh;
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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.craftbukkit.v1_21_R2.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TWAOod extends TWAFollower {

    private static final String entityId = "ood";
    private final NamespacedKey[] framesBlack = new NamespacedKey[]{
            RottenFlesh.OOD_BLACK_0.getKey(),
            RottenFlesh.OOD_BLACK_1.getKey(),
            RottenFlesh.OOD_BLACK_2.getKey(),
            RottenFlesh.OOD_BLACK_1.getKey(),
            RottenFlesh.OOD_BLACK_0.getKey(),
            RottenFlesh.OOD_BLACK_3.getKey(),
            RottenFlesh.OOD_BLACK_4.getKey(),
            RottenFlesh.OOD_BLACK_3.getKey()
    };
    private final NamespacedKey[] framesBlackRedeye = new NamespacedKey[]{
            RottenFlesh.OOD_REDEYE_BLACK_0.getKey(),
            RottenFlesh.OOD_REDEYE_BLACK_1.getKey(),
            RottenFlesh.OOD_REDEYE_BLACK_2.getKey(),
            RottenFlesh.OOD_REDEYE_BLACK_1.getKey(),
            RottenFlesh.OOD_REDEYE_BLACK_0.getKey(),
            RottenFlesh.OOD_REDEYE_BLACK_3.getKey(),
            RottenFlesh.OOD_REDEYE_BLACK_4.getKey(),
            RottenFlesh.OOD_REDEYE_BLACK_3.getKey()
    };
    private final NamespacedKey[] framesBlue = new NamespacedKey[]{
            RottenFlesh.OOD_BLUE_0.getKey(),
            RottenFlesh.OOD_BLUE_1.getKey(),
            RottenFlesh.OOD_BLUE_2.getKey(),
            RottenFlesh.OOD_BLUE_1.getKey(),
            RottenFlesh.OOD_BLUE_0.getKey(),
            RottenFlesh.OOD_BLUE_3.getKey(),
            RottenFlesh.OOD_BLUE_4.getKey(),
            RottenFlesh.OOD_BLUE_3.getKey()
    };
    private final NamespacedKey[] framesBlueRedeye = new NamespacedKey[]{
            RottenFlesh.OOD_REDEYE_BLUE_0.getKey(),
            RottenFlesh.OOD_REDEYE_BLUE_1.getKey(),
            RottenFlesh.OOD_REDEYE_BLUE_2.getKey(),
            RottenFlesh.OOD_REDEYE_BLUE_1.getKey(),
            RottenFlesh.OOD_REDEYE_BLUE_0.getKey(),
            RottenFlesh.OOD_REDEYE_BLUE_3.getKey(),
            RottenFlesh.OOD_REDEYE_BLUE_4.getKey(),
            RottenFlesh.OOD_REDEYE_BLUE_3.getKey()
    };
    private final NamespacedKey[] framesBrown = new NamespacedKey[]{
            RottenFlesh.OOD_BROWN_0.getKey(),
            RottenFlesh.OOD_BROWN_1.getKey(),
            RottenFlesh.OOD_BROWN_2.getKey(),
            RottenFlesh.OOD_BROWN_1.getKey(),
            RottenFlesh.OOD_BROWN_0.getKey(),
            RottenFlesh.OOD_BROWN_3.getKey(),
            RottenFlesh.OOD_BROWN_4.getKey(),
            RottenFlesh.OOD_BROWN_3.getKey()
    };
    private final NamespacedKey[] framesBrownRedeye = new NamespacedKey[]{
            RottenFlesh.OOD_REDEYE_BROWN_0.getKey(),
            RottenFlesh.OOD_REDEYE_BROWN_1.getKey(),
            RottenFlesh.OOD_REDEYE_BROWN_2.getKey(),
            RottenFlesh.OOD_REDEYE_BROWN_1.getKey(),
            RottenFlesh.OOD_REDEYE_BROWN_0.getKey(),
            RottenFlesh.OOD_REDEYE_BROWN_3.getKey(),
            RottenFlesh.OOD_REDEYE_BROWN_4.getKey(),
            RottenFlesh.OOD_REDEYE_BROWN_3.getKey()
    };
    private boolean redeye;
    private OodColour colour;
    
    public TWAOod(Level world) {
        super(EntityType.HUSK, world);
        this.redeye = false;
        this.colour = OodColour.BLACK;
    }

    public TWAOod(EntityType<? extends Husk> entityType, Level level) {
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
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 3 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (oldX == getX() && oldZ == getZ()) {
                NamespacedKey still = switch (colour) {
                    case BLACK -> (redeye) ? RottenFlesh.OOD_REDEYE_BLACK_STATIC.getKey() : RottenFlesh.OOD_BLACK_STATIC.getKey();
                    case BLUE -> (redeye) ? RottenFlesh.OOD_REDEYE_BLUE_STATIC.getKey() : RottenFlesh.OOD_BLUE_STATIC.getKey();
                    case BROWN -> (redeye) ? RottenFlesh.OOD_REDEYE_BROWN_STATIC.getKey() : RottenFlesh.OOD_BROWN_STATIC.getKey();
                };
                im.setItemModel(still);
                i = 0;
            } else {
                // play move animation
                switch (colour) {
                    case BLACK -> im.setItemModel((redeye) ? framesBlackRedeye[i] : framesBlack[i]);
                    case BLUE -> im.setItemModel((redeye) ? framesBlueRedeye[i] : framesBlue[i]);
                    case BROWN -> im.setItemModel((redeye) ? framesBrownRedeye[i] : framesBrown[i]);
                }
                i++;
                if (i == framesBlack.length) {
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
