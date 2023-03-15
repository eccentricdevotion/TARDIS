package me.eccentric_nz.TARDIS.enumeration;

import org.bukkit.ChatColor;

public enum MODULE {
    BLUEPRINTS("TARDIS Blueprints", ChatColor.BLUE),
    DEBUG("TARDIS", ChatColor.LIGHT_PURPLE),
    DYNMAP("TARDIS Dynmap", ChatColor.GREEN),
    HANDLES("HANDLES", ChatColor.DARK_AQUA),
    HELPER("TARDIS Helper", ChatColor.AQUA),
    MONSTERS("TARDIS Weeping Angels", ChatColor.RED),
    SHOP("TARDIS Shop", ChatColor.YELLOW),
    TARDIS("TARDIS", ChatColor.GOLD),
    VORTEX_MANIPULATOR("Vortex Manipulator", ChatColor.DARK_PURPLE);

    private String name;
    private ChatColor colour;

    MODULE(String name, ChatColor colour) {
        this.name = name;
        this.colour = colour;
    }

    public String getName() {
        return colour + "[" + name + "]" + ChatColor.RESET + " ";
    }
}
