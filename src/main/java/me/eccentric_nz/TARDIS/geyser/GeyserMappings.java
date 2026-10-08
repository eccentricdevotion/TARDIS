package me.eccentric_nz.TARDIS.geyser;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.files.FileCopier;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;

public class GeyserMappings {

    private final TARDIS plugin;
    private final List<String> jsonMappings = List.of(
            "chameleon_item_mappings.json",
            "door_mappings.json",
            "monster_mappings.json",
            "tardis_item_mappings.json",
            "time_rotor_item_mappings.json",
            "circuit_mappings.json",
            "key_mappings.json",
            "sonic_mappings.json",
            "tardis_model_mappings.json"
    );

    public GeyserMappings(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void install(CommandSender sender) {
        // get Geyser plugin
        Plugin geyser = plugin.getServer().getPluginManager().getPlugin("Geyser-Spigot");
        if (geyser == null) {
            return;
        }
        if (!geyser.isEnabled()) {
            return;
        }
        // get the geyser mappings folder
        File file = new File(geyser.getDataFolder() + File.separator + "custom_mappings");
        if (!file.exists()) {
            return;
        }
        // check for tardis mappings file
        File mappings = new File(geyser.getDataFolder() + File.separator + "custom_mappings" + File.separator + "tardis_item_mappings.json");
        if (!mappings.exists()) {
            // copy files to folder
            for (String jm : jsonMappings) {
                FileCopier.copy(mappings + File.separator + jm, plugin.getResource("geyser" + jm), true);
            }
        }
        plugin.getMessenger().message(sender, TardisModule.TARDIS, "GEYSER_MAPPINGS");
        // check if TARDISBedrockResourcePack is installed
        File pack = new File(geyser.getDataFolder() + File.separator + "packs" + File.separator + "TARDISBedrockResourcePack.mcpack");
        if (!pack.exists()) {
            // download and install pack from GitHub
            new DownloadBedrockPack(plugin).fetchFromGitHub(sender);
        }
    }
}
