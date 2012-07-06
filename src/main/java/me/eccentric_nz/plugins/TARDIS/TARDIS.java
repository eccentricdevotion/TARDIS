package me.eccentric_nz.plugins.TARDIS;

import java.io.*;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDIS extends JavaPlugin implements Listener {

    private static Logger log;
    public PluginDescriptionFile pdfFile;
    public FileConfiguration config = null;
    public FileConfiguration timelords = null;
    public HashMap<String, Boolean> PlayerTARDISMap = new HashMap<String, Boolean>();
    public File schematicfile = null;
    public File myconfigfile = null;
    public File timelordsfile = null;
    private Material t;
    private Material r;
    private Material s;
    private TARDISexecutor tardisExecutor;
    public String[][][] schematic;
    private boolean profession = false;
    public static TARDIS plugin;

    @Override
    public void onEnable() {
        plugin = this;
        new TARDISListener(this);

        pdfFile = getDescription();
        Constants.MY_PLUGIN_NAME = "[" + pdfFile.getName() + "]";

        try {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }
        } catch (Exception e) {
            System.out.println("TARDIS 1.0 could not create directory!");
            System.out.println("TARDIS 1.0 requires you to manually make the TARDIS/ directory!");
        }

        getDataFolder().setWritable(true);
        getDataFolder().setExecutable(true);

        if (config == null) {
            loadConfig();
        }

        tardisExecutor = new TARDISexecutor(this);
        getCommand("TARDIS").setExecutor(tardisExecutor);

    }

    @Override
    public void onDisable() {
        saveCustomConfig();
    }

    public FileConfiguration loadConfig() {
        try {
            schematicfile = new File(getDataFolder(), Constants.SCHEMATIC_FILE_NAME);
            if (!schematicfile.exists()) {
                copy(getResource(Constants.SCHEMATIC_FILE_NAME), schematicfile);
            }
            schematic = Schematic.schematic(schematicfile);
        } catch (Exception e) {
            System.out.println(Constants.MY_PLUGIN_NAME + " failed to retrieve schematic from directory. Using defaults.");
        }
        try {
            myconfigfile = new File(getDataFolder(), Constants.CONFIG_FILE_NAME);
            if (!myconfigfile.exists()) {
                // load the default values into file
                copy(getResource(Constants.CONFIG_FILE_NAME), myconfigfile);
            }
        } catch (Exception e) {
            System.out.println(Constants.MY_PLUGIN_NAME + " failed to retrieve configuration from directory. Using defaults.");
        }
        try {
            timelordsfile = new File(getDataFolder(), Constants.TIMELORDS_FILE_NAME);
            if (!timelordsfile.exists()) {
                copy(getResource(Constants.TIMELORDS_FILE_NAME), timelordsfile);
            }
        } catch (Exception e) {
            System.out.println(Constants.MY_PLUGIN_NAME + " failed to retrieve timelords from directory. Using defaults.");
        }
        config = YamlConfiguration.loadConfiguration(myconfigfile);
        timelords = YamlConfiguration.loadConfiguration(timelordsfile);

        // read the values we need and convert them to ENUM
        Set<String> block_set = config.getConfigurationSection("TARDIS_blocks").getKeys(false);
        String r_str = config.getString("remind_material");
        String s_str = config.getString("select_material");


        return config;
    }

    private void copy(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            log.log(Level.INFO, "{0} could not save the config file.", Constants.MY_PLUGIN_NAME);
        }
    }

    public void saveCustomConfig() {
        if (config == null || myconfigfile == null) {
            return;
        }
        try {
            config.save(myconfigfile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save config to " + myconfigfile, ex);
        }
    }
}