package me.eccentric_nz.TARDIS.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;

public class TARDISHandlesUpdater {

    private final TARDIS plugin;
    private final FileConfiguration handlesConfig;
    private final HashMap<String, String> stringOptions = new HashMap<>();

    public TARDISHandlesUpdater(TARDIS plugin, FileConfiguration handlesConfig) {
        this.plugin = plugin;
        this.handlesConfig = handlesConfig;
    }

    public void checkHandles() {
        if (!handlesConfig.contains("core-commands.brake")) {
            handlesConfig.set("core-commands.brake", "\\b(?:(?:hand)*brake|park)\\b");
            try {
                String handlesPath = plugin.getDataFolder() + File.separator + "handles.yml";
                handlesConfig.save(new File(handlesPath));
                plugin.getLogger().log(Level.INFO, "Updated handles.yml");
            } catch (IOException io) {
                plugin.debug("Could not save handles.yml, " + io.getMessage());
            }
        }
    }
}
