/*
 * Copyright (C) 2025 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels.nms;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class EntityRegistry {

    private final String[] all = new String[]{"judoon", "ood", "k9"};

    public static void unfreeze() throws NoSuchFieldException, IllegalAccessException {
        Field unregisteredIntrusiveHolders = MappedRegistry.class.getDeclaredField("m");
        unregisteredIntrusiveHolders.setAccessible(true);
        unregisteredIntrusiveHolders.set(BuiltInRegistries.ENTITY_TYPE, new IdentityHashMap<EntityType<?>, Holder.Reference<EntityType<?>>>());
        Field frozen = MappedRegistry.class.getDeclaredField("l");
        frozen.setAccessible(true);
        frozen.set(BuiltInRegistries.ENTITY_TYPE, false);
    }

//    public void init() {
//        try {
//            unfreezeEntityRegistry();
//            for (String ENTITY_ID : all) {
//                ResourceLocation mcKey = ResourceLocation.parse(ENTITY_ID);
//                if (BuiltInRegistries.ENTITY_TYPE.getOptional(mcKey).isEmpty()) {
//                    @SuppressWarnings("unchecked") Map<String, Type<?>> types = (Map<String, Type<?>>) DataFixers.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getCurrentVersion().getDataVersion().getVersion())).findChoiceType(References.ENTITY).types();
//                    types.put(mcKey.toString(), types.get(BuiltInRegistries.ENTITY_TYPE.getKey(EntityType.HUSK).toString()));
//                    Registry.register(BuiltInRegistries.ENTITY_TYPE, ENTITY_ID, EntityType.Builder.of(EntityType::create, MobCategory.MONSTER).noSummon().build(ENTITY_ID));
//                }
//            }
//            BuiltInRegistries.ENTITY_TYPE.freeze();
//        } catch (NoSuchFieldException | IllegalAccessException ignore) {
//        }
//    }
}
