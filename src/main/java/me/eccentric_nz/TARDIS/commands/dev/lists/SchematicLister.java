package me.eccentric_nz.TARDIS.commands.dev.lists;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintConsole;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;

public class SchematicLister {

    private final TARDIS plugin;

    public SchematicLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void list() {
        for (BlueprintConsole bpc : BlueprintConsole.values()) {
            String perm = bpc.getPermission().split("\\.")[1];
            Schematic console = Desktops.getBY_PERMS().get(perm);
            if (console == null) {
                plugin.debug("Schematic by perm {" + perm + "} was null");
            } else {
                plugin.debug("Schematic by perm {" + perm + "} has material " + console.getSeedMaterial());
            }
        }
    }
}
