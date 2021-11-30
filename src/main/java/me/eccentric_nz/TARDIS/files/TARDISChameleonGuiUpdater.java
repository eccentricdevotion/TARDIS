/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * @author eccentric_nz
 */
public class TARDISChameleonGuiUpdater {

    private final TARDIS plugin;
    private final FileConfiguration chameleon_config;
    private final HashMap<String, String> chameleonOptions = new HashMap<>();
    private final HashMap<String, List<String>> chameleonListOptions = new HashMap<>();

    public TARDISChameleonGuiUpdater(TARDIS plugin, FileConfiguration chameleoon_config) {
        this.plugin = plugin;
        chameleon_config = chameleoon_config;
        chameleonListOptions.put("ADAPT_LORE", Arrays.asList("The Chameleon Circuit", "will choose a preset", "that blends in with", "the environment.", "Use BIOME or BLOCK mode."));
        chameleonListOptions.put("APPLY_LORE", Arrays.asList("Rebuild the TARDIS", "exterior with the", "current settings."));
        chameleonListOptions.put("CONSTRUCT_LORE", Arrays.asList("Build your own", "Chameleon preset."));
        chameleonListOptions.put("DISABLED_LORE", Arrays.asList("Disable the Chameleon", "Circuit and revert", "to the FACTORY preset."));
        chameleonListOptions.put("INVISIBLE_LORE", Arrays.asList("Engages the TARDIS", "Invisiblity Circuit."));
        chameleonListOptions.put("SHORT_LORE", Arrays.asList("Make the Chameleon", "Circuit malfunction and", "always choose the", "same appearance."));
        chameleonListOptions.put("INFO_TRANSMAT", Arrays.asList("Click on a transmat", "location, then choose", "an action button."));
        chameleonOptions.put("ADAPT", "Adaptive");
        chameleonOptions.put("APPLY", "Apply");
        chameleonOptions.put("BACK_CHAM_OPTS", "Back to Chameleon Circuit");
        chameleonOptions.put("CONSTRUCT", "Construct");
        chameleonOptions.put("DISABLED", "DISABLED");
        chameleonOptions.put("INVISIBLE", "Invisible");
        chameleonOptions.put("SHORT", "Shorted out");
        chameleonOptions.put("USE_PREV", "Use last saved construct");
    }

    public void checkChameleonConfig() {
        if (chameleon_config.getString("SAVE").equals("Save construction")) {
            chameleon_config.set("SAVE", "Save construct");
        }
        int i = 0;
        for (Map.Entry<String, String> entry : chameleonOptions.entrySet()) {
            if (!chameleon_config.contains(entry.getKey())) {
                chameleon_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        for (Map.Entry<String, List<String>> entry : chameleonListOptions.entrySet()) {
            if (!chameleon_config.contains(entry.getKey())) {
                chameleon_config.set(entry.getKey(), entry.getValue());
                i++;
            }
        }
        try {
            String chameleonPath = plugin.getDataFolder() + File.separator + "language" + File.separator + "chameleon_guis.yml";
            chameleon_config.save(new File(chameleonPath));
            if (i > 0) {
                plugin.getLogger().log(Level.INFO, "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to chameleon_guis.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save chameleon_guis.yml, " + io.getMessage());
        }
    }
}
