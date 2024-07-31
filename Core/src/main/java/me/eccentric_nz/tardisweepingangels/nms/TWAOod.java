package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisweepingangels.monsters.ood.OodColour;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
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
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TWAOod extends TWAFollower {

    private static final String entityId = "ood";

    static {
        ResourceLocation mcKey = ResourceLocation.parse(entityId);
        try {
            if (!BuiltInRegistries.ENTITY_TYPE.getOptional(mcKey).isPresent()) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Injecting Ood into ENTITY_TYPE registry.");
                injectEntity(mcKey);
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Failed to inject custom Ood entity! The plugin will still work, but Ood followers might not persist.");
        }
    }

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

    private static void injectEntity(ResourceLocation mcKey) throws NoSuchFieldException, IllegalAccessException {
        Registry<EntityType<?>> entityReg = ((CraftServer) Bukkit.getServer()).getServer().registryAccess().registry(Registries.ENTITY_TYPE).orElseThrow(NoSuchFieldException::new);
        EntityRegistry.unfreeze();

        try {
            // Paper wants this, Spigot this causes a crash
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            @SuppressWarnings("unchecked") Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())).findChoiceType(References.ENTITY).types();
            types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HUSK).toString()));
        } catch (ClassNotFoundException ignored) {
        }

        entityReg.createIntrusiveHolder(EntityType.Builder.of(TWAOod::new, MobCategory.MONSTER).noSummon().build(entityId));
        Registry.register(entityReg, entityId, EntityType.Builder.of(TWAOod::new, MobCategory.MONSTER).noSummon().build(entityId));
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.HEAD) && tickCount % 3 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.HEAD);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            ItemMeta im = bukkit.getItemMeta();
            if (oldX == getX() && oldZ == getZ()) {
                int cmd = 405 + colour.getStep();
                if (redeye) {
                    cmd += 18;
                }
                im.setCustomModelData(cmd);
                i = 0;
            } else {
                // play move animation
                int cmd = 400 + colour.getStep();
                if (redeye) {
                    cmd += 18;
                }
                im.setCustomModelData(cmd + frames[i]);
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
