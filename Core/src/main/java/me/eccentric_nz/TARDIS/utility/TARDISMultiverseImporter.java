package me.eccentric_nz.TARDIS.utility;

import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.enums.AllowedPortalType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.io.IOException;

public class TARDISMultiverseImporter {

    private final TARDIS plugin;
    private final MultiverseCore mvc;
    private final CommandSender sender;

    public TARDISMultiverseImporter(TARDIS plugin, MultiverseCore mvc, CommandSender sender) {
        this.plugin = plugin;
        this.mvc = mvc;
        this.sender = sender;
    }

    /**
     * Transfer Multiverse world values to planets.yml
     */
    public void transfer() {
        int i = 0;
        for (MultiverseWorld mvw : mvc.getMVWorldManager().getMVWorlds()) {
            // only import if the world doesn't have an entry in planets.yml
            if (!plugin.getPlanetsConfig().contains("planets." + mvw.getName())) {
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".enabled", mvw.getAutoLoad());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".resource-pack", "default");
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamemode", mvw.getGameMode().toString());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".time_travel", false);
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".world_type", mvw.getWorldType().toString());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".environment", mvw.getEnvironment().toString());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".generator", mvw.getGenerator());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".spawn_chunk_radius", mvw.isKeepingSpawnInMemory() ? 2 : 0);
                if (!mvw.canAnimalsSpawn() || !mvw.canMonstersSpawn()) {
                    plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamerules.doMobSpawning", false);
                }
                if (!mvw.isWeatherEnabled()) {
                    plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamerules.doWeatherCycle", false);
                }
                if (!mvw.isPVPEnabled()) {
                    plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".gamerules.pvp", false);
                }
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".allow_portals", mvw.getAllowedPortals() != AllowedPortalType.NONE);
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".alias", mvw.getAlias());
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".helmic_regultor_order", -1);
                String icon;
                switch (mvw.getEnvironment()) {
                    case NETHER -> icon = "NETHERRACK";
                    case THE_END -> icon = "END_STONE";
                    default -> icon = "STONE";
                }
                plugin.getPlanetsConfig().set("planets." + mvw.getName() + ".icon", icon);
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_TRANSFER", mvw.getName());
                i++;
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_EXISTS", mvw.getName());
            }
        }
        if (i > 0) {
            // save the config
            try {
                String planetsPath = plugin.getDataFolder() + File.separator + "planets.yml";
                plugin.getPlanetsConfig().save(new File(planetsPath));
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " new items to planets.yml");
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "MULTIVERSE_IMPORT", "" + i);
            } catch (IOException io) {
                plugin.debug("Could not save planets.yml, " + io.getMessage());
            }
        }
    }
}
