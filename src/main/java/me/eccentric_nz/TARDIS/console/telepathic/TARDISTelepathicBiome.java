package me.eccentric_nz.TARDIS.console.telepathic;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TARDISTelepathicBiome {

    private final TARDIS plugin;
    private final Player player;

    public TARDISTelepathicBiome(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public ItemStack[] getButtons() {
        // TODO build buttons
        // toggling telepathic circuit on/off
        // cave finder
        // structure finder
        // biome finder
        return new ItemStack[54];
    }
}
