package me.eccentric_nz.TARDIS.info.processors;

import io.papermc.paper.dialog.Dialog;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISRecipe;
import me.eccentric_nz.TARDIS.info.dialog.InfoDialog;
import net.kyori.adventure.audience.Audience;
import org.bukkit.entity.Player;

public class EntryProcessor {

    private final TARDIS plugin;
    private final Player player;

    public EntryProcessor(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void showInfoOrRecipe(String entry) {
        try {
            TARDISInfoMenu tardisInfoMenu = TARDISInfoMenu.valueOf(entry);
            if (entry.endsWith("RECIPE")) {
                new TISRecipe(plugin).show(player, tardisInfoMenu);
            } else {
                Dialog dialog = new InfoDialog().create(plugin, tardisInfoMenu);
                if (dialog != null) {
                    Audience.audience(player).showDialog(dialog);
                }
            }
        } catch (IllegalArgumentException e) {
            plugin.debug("[showInfoOrRecipe] '" + entry + "' is not a valid TARDISInfoMenu!");
        }
    }
}
