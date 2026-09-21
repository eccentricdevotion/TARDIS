/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels.nms;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.custommodels.keys.JudoonVariant;
import net.minecraft.SharedConstants;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueOutput;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;

import java.util.Map;

public class TWAJudoon extends TWAFollower {

    private static final String entityId = "judoon";

    private int ammo;
    private boolean guard;

    public TWAJudoon(Level world) {
        super(EntityTypes.HUSK, world);
        this.guard = false;
    }

    public TWAJudoon(EntityType<Entity> entityType, Level level) {
        super(EntityTypes.HUSK, level);
        this.guard = false;
    }

    public static void injectEntity(Identifier mcKey) throws NoSuchFieldException, IllegalAccessException {
        Registry<EntityType<?>> entityReg = ((CraftServer) Bukkit.getServer()).getServer().registryAccess().lookup(Registries.ENTITY_TYPE).orElseThrow(NoSuchFieldException::new);
        EntityRegistry.unfreeze();
        @SuppressWarnings("unchecked")
        Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().dataVersion().version())).findChoiceType(References.ENTITY).types();
        types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(EntityTypes.HUSK).toString()));
        ResourceKey<EntityType<?>> resourceKey = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace(entityId));
        EntityType<?> type = EntityType.Builder.of(TWAJudoon::new, MobCategory.MONSTER).noSummon().build(resourceKey);
        entityReg.createIntrusiveHolder(type);
        Registry.register(entityReg, entityId, type);
    }

    @Override
    public void aiStep() {
        if (hasItemInSlot(EquipmentSlot.MAINHAND) && tickCount % 10 == 0) {
            ItemStack is = getItemBySlot(EquipmentSlot.MAINHAND);
            org.bukkit.inventory.ItemStack bukkit = CraftItemStack.asBukkitCopy(is);
            bukkit.setData(DataComponentTypes.ITEM_MODEL, this.guard ? JudoonVariant.JUDOON_WEAPON_ACTIVE.getKey() : JudoonVariant.JUDOON_WEAPON_RESTING.getKey());
            setItemSlot(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(bukkit));
        }
        super.aiStep();
    }

    @Override
    public void addAdditionalSaveData(ValueOutput nbttagcompound) {
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
