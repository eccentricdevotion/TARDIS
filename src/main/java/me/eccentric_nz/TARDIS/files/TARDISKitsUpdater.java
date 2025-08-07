package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TARDISKitsUpdater {

    private final TARDIS plugin;
    private final FileConfiguration kitsConfig;

    public TARDISKitsUpdater(TARDIS plugin, FileConfiguration kitsConfig) {
        this.plugin = plugin;
        this.kitsConfig = kitsConfig;
    }

    public void checkKits() {
        if (!kitsConfig.contains("kits.blueprints")) {
            kitsConfig.set("kits.blueprints", List.of(
                    "BLUEPRINT_BASE_UPDATE",
                    "BLUEPRINT_CONSOLE_ROTOR",
                    "BLUEPRINT_FEATURE_FORCEFIELD",
                    "BLUEPRINT_PRESET_ANGEL",
                    "BLUEPRINT_ROOM_VAULT",
                    "BLUEPRINT_SONIC_KNOCKBACK",
                    "BLUEPRINT_TRAVEL_VILLAGE"
            ));
            try {
                String kitsPath = plugin.getDataFolder() + File.separator + "kits.yml";
                kitsConfig.save(new File(kitsPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Updated kits.yml");
            } catch (IOException io) {
                plugin.debug("Could not save kits.yml, " + io.getMessage());
            }
        }
    }
}
