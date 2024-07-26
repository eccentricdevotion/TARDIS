package me.eccentric_nz.tardisregeneration;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ElixirOfLifeListener implements Listener {

    private final TARDIS plugin;

    public ElixirOfLifeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrinkElixir(PlayerItemConsumeEvent event) {
        ItemStack goblet = event.getItem();
        if (!ElixirOfLife.is(goblet)) {
            return;
        }
        new Regenerator().processPlayer(plugin, event.getPlayer());
    }
}
