package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;

public class TISRecipe {

    private final TARDIS plugin;

    public TISRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Displays the workbench recipe for an item or component.
     *
     * @param player the player to show the recipe to
     * @param item   the recipe to display
     */
    public void show(Player player, TARDISInfoMenu item) {
        String recipe;
        if (item == TARDISInfoMenu.THREE_D_GLASSES_RECIPE) {
            recipe = "3-d-glasses";
        } else {
            // remove "TARDIS_" and "_RECIPE" from the string and replace underscores with dashes
            String[] find = new String[]{"TARDIS_", "_RECIPE", "_"};
            String[] repl = new String[]{"", "", "-"};
            recipe = StringUtils.replaceEach(item.toString(), find, repl).toLowerCase();
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> player.performCommand("tardisrecipe " + recipe));
    }
}
