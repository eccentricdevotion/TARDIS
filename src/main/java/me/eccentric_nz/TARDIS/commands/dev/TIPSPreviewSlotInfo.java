package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;

public class TIPSPreviewSlotInfo {

    private final TARDIS plugin;

    public TIPSPreviewSlotInfo(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean display() {
        for (Schematic schematic : Consoles.getBY_NAMES().values()) {
            if (schematic.getPreview() < 0) {
                int slot = schematic.getPreview();
                int row = slot / 20;
                int col = slot % 20;
                int centreX = row * 1024 + 496;
                int centreZ = col * 1024 + 496;
                plugin.debug("slot = " + slot + ", row = " + row + ", col = " + col + ", X = " + centreX + ", Z = " + centreZ);
            }
        }
        return true;
    }
}
