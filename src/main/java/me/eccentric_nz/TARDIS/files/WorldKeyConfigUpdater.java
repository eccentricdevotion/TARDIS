package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class WorldKeyConfigUpdater {

    private final TARDIS plugin;

    public WorldKeyConfigUpdater(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean convert() {
        try {
            // backup files
            Set<String> configFiles = Set.of("config", "planets", "monsters");
            for (String configFile : configFiles) {
                File current = new File(plugin.getDataFolder(), configFile + ".yml");
                File backup = new File(plugin.getDataFolder(), configFile + "_" + System.currentTimeMillis() + ".yml");
                FileUtil.copy(current, backup);
            }
            // get default world name e.g. world -> key becomes minecraft:overworld
            String defaultWorld = plugin.getServer().getLevelDirectory().toString().substring(2);
            // config - creation.default_world_name
            plugin.getConfig().set("creation.default_world_name", plugin.getConfig().getString("creation.default_world_name", "tardis_timevortex").toLowerCase(Locale.ROOT));
            // travel.terminal:
            //    nether: world
            //    the_end: world
            String tn = plugin.getConfig().getString("travel.terminal.nether", defaultWorld);
            plugin.getConfig().set("travel.terminal.nether", tn.equalsIgnoreCase(defaultWorld) ? "overworld" : tn.toLowerCase(Locale.ROOT));
            String en = plugin.getConfig().getString("travel.terminal.the_end", defaultWorld);
            plugin.getConfig().set("travel.terminal.the_end", en.equalsIgnoreCase(defaultWorld) ? "overworld" : en.toLowerCase(Locale.ROOT));
            // rechargers: []
            for (String r : plugin.getConfig().getConfigurationSection("rechargers").getKeys(false)) {
                String w = plugin.getConfig().getString("rechargers." + r + ".world", defaultWorld);
                String set;
                if (w.equalsIgnoreCase(defaultWorld)) {
                    set = "overworld";
                } else if (w.equalsIgnoreCase(defaultWorld + "_nether")) {
                    set = "the_nether";
                } else if (w.equalsIgnoreCase(defaultWorld + "_the_end")) {
                    set = "the_end";
                } else {
                    set = w.toLowerCase(Locale.ROOT);
                }
                plugin.getConfig().set("rechargers." + r + ".world", set);
            }
            // save config
            plugin.saveConfig();
            // planets
            ConfigurationSection ttv = plugin.getPlanetsConfig().getConfigurationSection("planets.TARDIS_TimeVortex");
            if (ttv != null) {
                Map<String, Object> ttvValues = ttv.getValues(true);
                plugin.getPlanetsConfig().set("planets.tardis_timevortex", ttvValues);
                plugin.getPlanetsConfig().set("planets.TARDIS_TimeVortex", null);
            }
            ConfigurationSection tzr = plugin.getPlanetsConfig().getConfigurationSection("planets.TARDIS_Zero_Room");
            if (tzr != null) {
                Map<String, Object> tzrValues = tzr.getValues(true);
                plugin.getPlanetsConfig().set("planets.tardis_zero_room", tzrValues);
                plugin.getPlanetsConfig().set("planets.TARDIS_Zero_Room", null);
            }
            ConfigurationSection end = plugin.getPlanetsConfig().getConfigurationSection("planets." + defaultWorld + "_the_end");
            if (end != null) {
                Map<String, Object> endValues = end.getValues(true);
                plugin.getPlanetsConfig().set("planets.the_end", endValues);
                plugin.getPlanetsConfig().set("planets." + defaultWorld + "_the_end", null);
            }
            ConfigurationSection nether = plugin.getPlanetsConfig().getConfigurationSection("planets." + defaultWorld + "_nether");
            if (nether != null) {
                Map<String, Object> netherValues = nether.getValues(true);
                plugin.getPlanetsConfig().set("planets.the_nether", netherValues);
                plugin.getPlanetsConfig().set("planets." + defaultWorld + "_nether", null);
            }
            // check other planet keys are lowercase
            for (String w : plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
                if (w.chars().anyMatch(Character::isUpperCase)) {
                    // copy configuration section to lowercase version
                    ConfigurationSection cs = plugin.getPlanetsConfig().getConfigurationSection("planets." + w);
                    if (cs != null) {
                        Map<String, Object> csValues = cs.getValues(true);
                        plugin.getPlanetsConfig().set("planets." + w.toLowerCase(Locale.ROOT), csValues);
                        plugin.getPlanetsConfig().set("planets." + w, null);
                    }
                }
            }
            // save planets
            plugin.getPlanetsConfig().save(new File(plugin.getDataFolder(), "planets.yml"));
            // monsters
            // **monster**.worlds
            Set<String> not_these = Set.of("config_version", "spawn_rate", "custom_spawners");
            if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                for (String monster : plugin.getMonstersConfig().getKeys(false)) {
                    if (!not_these.contains(monster)) {
                        for (String w : plugin.getMonstersConfig().getConfigurationSection(monster + ".worlds").getKeys(false)) {
                            String set;
                            if (w.equalsIgnoreCase(defaultWorld)) {
                                set = "overworld";
                            } else if (w.equalsIgnoreCase(defaultWorld + "_nether")) {
                                set = "the_nether";
                            } else if (w.equalsIgnoreCase(defaultWorld + "_the_end")) {
                                set = "the_end";
                            } else {
                                set = w.toLowerCase(Locale.ROOT);
                            }
                            plugin.getMonstersConfig().set(monster + ".worlds." + w, set);
                        }
                    }
                }
                // angels.teleport_worlds
                List<String> teleport = new ArrayList<>();
                for (String w : plugin.getMonstersConfig().getStringList("angels.teleport_worlds")) {
                    String set;
                    if (w.equalsIgnoreCase(defaultWorld)) {
                        set = "overworld";
                    } else if (w.equalsIgnoreCase(defaultWorld + "_nether")) {
                        set = "the_nether";
                    } else if (w.equalsIgnoreCase(defaultWorld + "_the_end")) {
                        set = "the_end";
                    } else {
                        set = w.toLowerCase(Locale.ROOT);
                    }
                    teleport.add(set);
                }
                plugin.getMonstersConfig().set("angels.teleport_worlds", teleport);
                // angels.teleport_locations
                List<String> locations = new ArrayList<>();
                for (String w : plugin.getMonstersConfig().getStringList("angels.teleport_locations")) {
                    String set;
                    if (w.equalsIgnoreCase(defaultWorld)) {
                        set = w.replace(defaultWorld, "overworld");
                    } else if (w.equalsIgnoreCase(defaultWorld + "_nether")) {
                        set = w.replace(defaultWorld + "_nether", "the_nether");
                    } else if (w.equalsIgnoreCase(defaultWorld + "_the_end")) {
                        set = w.replace(defaultWorld + "_the_end", "the_end");
                    } else {
                        set = w.toLowerCase(Locale.ROOT);
                    }
                    locations.add(set);
                }
                plugin.getMonstersConfig().set("angels.teleport_locations", locations);
                // save monsters
                plugin.getMonstersConfig().save(new File(plugin.getDataFolder(), "monsters.yml"));
            } else {
                // just overwrite with the default monsters.yml from the jar
                String filepath = plugin.getDataFolder() + File.separator + "monsters.yml";
                InputStream in = plugin.getResource("monsters.yml");
                if (in != null) {
                    FileCopier.copy(filepath, in, true);
                }
            }
        } catch (IOException ex) {
            plugin.getMessenger().message(plugin.getConsole(), TardisModule.SEVERE, "Could not convert world key config files!");
        }
        return true;
    }
}
