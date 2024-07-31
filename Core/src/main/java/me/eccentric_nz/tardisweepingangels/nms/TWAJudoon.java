package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R1.CraftServer;
import org.bukkit.craftbukkit.v1_21_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class TWAJudoon extends TWAFollower {

    private static final String entityId = "judoon";

    static {
        ResourceLocation mcKey = ResourceLocation.parse(entityId);
        try {
            if (!BuiltInRegistries.ENTITY_TYPE.getOptional(mcKey).isPresent()) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Injecting Judoon into ENTITY_TYPE registry.");
                injectEntity(mcKey);
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Failed to inject custom Judoon entity! The plugin will still work, but Judoon guards might not persist.");
        }
    }

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

        entityReg.createIntrusiveHolder(EntityType.Builder.of(TWAJudoon::new, MobCategory.MONSTER).noSummon().build(entityId));
        Registry.register(entityReg, entityId, EntityType.Builder.of(TWAJudoon::new, MobCategory.MONSTER).noSummon().build(entityId));
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
