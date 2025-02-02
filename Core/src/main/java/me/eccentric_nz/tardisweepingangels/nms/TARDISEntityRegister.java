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
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardisweepingangels.nms;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class TARDISEntityRegister {

    public void inject() {
        ResourceLocation oodKey = ResourceLocation.parse("ood");
        ResourceLocation judoonKey = ResourceLocation.parse("judoon");
        ResourceLocation k9Key = ResourceLocation.parse("k9");
        try {
            if (BuiltInRegistries.ENTITY_TYPE.getOptional(oodKey).isEmpty()) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Injecting Ood into ENTITY_TYPE registry.");
                TWAOod.injectEntity(oodKey);
            }
            if (BuiltInRegistries.ENTITY_TYPE.getOptional(judoonKey).isEmpty()) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Injecting Judoon into ENTITY_TYPE registry.");
                TWAJudoon.injectEntity(judoonKey);
            }
            if (BuiltInRegistries.ENTITY_TYPE.getOptional(k9Key).isEmpty()) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Injecting K9 into ENTITY_TYPE registry.");
                TWAK9.injectEntity(k9Key);
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.MONSTERS, "Failed to inject custom entities! The plugin will still work, but TWA followers might not persist.");
        }
    }
}
