package me.eccentric_nz.TARDIS.mapping;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class TARDISServerListener implements Listener {
    private final String plugin;
    private final TARDISMapper mapper;

    public TARDISServerListener(String plugin, TARDISMapper mapper) {
        this.plugin = plugin;
        this.mapper = mapper;
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin p = event.getPlugin();
        String name = p.getDescription().getName();
        if (name.equals(plugin)) {
            mapper.activate();
        }
    }
}
