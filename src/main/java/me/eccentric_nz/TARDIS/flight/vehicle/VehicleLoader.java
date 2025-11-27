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
package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class VehicleLoader {

    public void injectEntity() {
        Identifier key = Identifier.parse("flight_vehicle");
        try {
            if (BuiltInRegistries.ENTITY_TYPE.getOptional(key).isEmpty()) {
                TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.TARDIS, "Injecting exterior flight vehicle into ENTITY_TYPE registry.");
                TARDISArmourStand.injectEntity(key);
            }
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.TARDIS, "Failed to inject custom exterior flight vehicle! The plugin will still work, but TARDIS exteriors will need to be rebuilt.");
        }
    }
}
