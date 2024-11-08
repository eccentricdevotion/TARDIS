package me.eccentric_nz.TARDIS.flight.vehicle;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class VehicleLoader {

    public void injectEntity() {
        ResourceLocation key = ResourceLocation.parse("flight_vehicle");
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
