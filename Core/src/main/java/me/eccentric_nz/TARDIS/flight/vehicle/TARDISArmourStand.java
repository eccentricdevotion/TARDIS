package me.eccentric_nz.TARDIS.flight.vehicle;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import me.eccentric_nz.tardisweepingangels.nms.EntityRegistry;
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
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_21_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Map;

public class TARDISArmourStand extends ArmorStand {

    private static final String entityId = "flight_vehicle";
    private Player player;
    private boolean stationary = false;

    public TARDISArmourStand(EntityType<? extends ArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
        this.player = null;
        this.setNoGravity(false);
        this.setInvisible(true);
        this.setInvulnerable(true);
    }

    public static void injectEntity(ResourceLocation mcKey) throws NoSuchFieldException, IllegalAccessException {
        Registry<EntityType<?>> entityReg = ((CraftServer) Bukkit.getServer()).getServer().registryAccess().lookup(Registries.ENTITY_TYPE).orElseThrow(NoSuchFieldException::new);
        EntityRegistry.unfreeze();
        @SuppressWarnings("unchecked")
        Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())).findChoiceType(References.ENTITY).types();
        types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.ARMOR_STAND).toString()));
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("tardis", entityId));
        EntityType<?> type = EntityType.Builder.of(TARDISArmourStand::new, MobCategory.MISC).noSummon().build(resourceKey);
        entityReg.createIntrusiveHolder(type);
        Registry.register(entityReg, entityId, type);
    }

    @Override
    public void travel(Vec3 vec3d) {
        if (super.isNoGravity()) {
            super.travel(vec3d);
        } else {
            Vector direction;
            if (player == null || stationary || (this.onGround() && player.getLocation().getDirection().getY() < 0)) {
                direction = new Vector(0, 0, 0);
            } else {
                direction = player.getLocation().getDirection();
            }
            move(MoverType.SELF, new Vec3(direction.getX() / 3.0d, direction.getY() / 3.0d, direction.getZ() / 3.0d));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        nbttagcompound.putString("id", "minecraft:flight_vehicle");
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isStationary() {
        return stationary;
    }

    public void setStationary(boolean stationary) {
        this.stationary = stationary;
    }
}
