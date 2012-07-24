package me.eccentric_nz.plugins.TARDIS;

import java.io.*;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TARDIS extends JavaPlugin implements Listener {

    public PluginDescriptionFile pdfFile;
    public FileConfiguration config = null;
    public FileConfiguration timelords = null;
    public File schematicfile = null;
    public File myconfigfile = null;
    public File timelordsfile = null;
    private TARDISexecutor tardisExecutor;
    public String[][][] schematic;
    protected static TARDIS plugin;
    TARDISListener tardisListener = new TARDISListener(this);
    PluginManager pm = Bukkit.getServer().getPluginManager();

    @Override
    public void onEnable() {
        plugin = this;
        pm.registerEvents(tardisListener, this);

        pdfFile = getDescription();
        Constants.MY_PLUGIN_NAME = "[" + pdfFile.getName() + "]";

        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) {
                System.out.println(Constants.MY_PLUGIN_NAME + " could not create directory!");
                System.out.println(Constants.MY_PLUGIN_NAME + " requires you to manually make the TARDIS/ directory!");
            }
            getDataFolder().setWritable(true);
            getDataFolder().setExecutable(true);
        }

        File dir = new File(plugin.getDataFolder() + File.separator + "chunks");
        if (!dir.exists()) {
            dir.mkdirs();
            dir.setWritable(true);
            dir.setExecutable(true);
        }

        List<World> worldList = plugin.getServer().getWorlds();
        for (World w : worldList) {
            String strWorldName = w.getName();
            File file = new File(plugin.getDataFolder() + File.separator + "chunks" + File.separator + strWorldName + ".chunks");
            if (!file.exists() && w.getEnvironment() == Environment.NORMAL) {
                try {
                    file.createNewFile();
                } catch (IOException io) {
                    System.out.println(Constants.MY_PLUGIN_NAME + " could not create [" + strWorldName + "] world chunk file!");
                }
            }
        }

        if (config == null) {
            loadConfig();
        }
        Constants.TARDIS_KEY = config.getString("key");

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

            myconfigfile = new File(getDataFolder(), Constants.CONFIG_FILE_NAME);
            if (!myconfigfile.exists()) {
                // load the default values into file
                copy(getResource(Constants.CONFIG_FILE_NAME), myconfigfile);
            }

            timelordsfile = new File(getDataFolder(), Constants.TIMELORDS_FILE_NAME);
            if (!timelordsfile.exists()) {
                copy(getResource(Constants.TIMELORDS_FILE_NAME), timelordsfile);
            }
        } catch (Exception e) {
            System.out.println(Constants.MY_PLUGIN_NAME + " failed to retrieve files from directory. Using defaults.");
        }
        config = YamlConfiguration.loadConfiguration(myconfigfile);
        timelords = YamlConfiguration.loadConfiguration(timelordsfile);

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
            System.err.println(Constants.MY_PLUGIN_NAME + " could not save the config file.");
        }
    }

    public void saveCustomConfig() {
        if (config == null || myconfigfile == null) {
            return;
        }
        try {
            config.save(myconfigfile);
        } catch (IOException ex) {
            System.err.println(Constants.MY_PLUGIN_NAME + "Could not save config to " + myconfigfile);
        }
    }
}