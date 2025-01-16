package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;

public class Microscope {

    private final TARDIS plugin;

    public Microscope(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        plugin.getPM().registerEvents(new MicroscopeInteractListener(plugin), plugin);
        plugin.getPM().registerEvents(new MicroscopeItemFrameListener(plugin), plugin);
        plugin.getPM().registerEvents(new MicroscopeSlotChangeListener(plugin), plugin);
        plugin.getPM().registerEvents(new MicroscopeDamageListener(plugin), plugin);
        plugin.getPM().registerEvents(new MicroscopeGUIListener(plugin), plugin);
        plugin.getPM().registerEvents(new MicroscopePlaceListener(plugin), plugin);
        new MicroscopeRecipes(plugin).addRecipes();
    }
}
