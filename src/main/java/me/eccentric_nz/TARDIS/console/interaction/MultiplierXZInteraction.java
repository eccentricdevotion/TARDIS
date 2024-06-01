package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.ConsoleInteraction;
import me.eccentric_nz.TARDIS.console.models.WXYZModel;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MultiplierXZInteraction {

    private final TARDIS plugin;

    public MultiplierXZInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void setRange(ConsoleInteraction ci, int state, Interaction interaction, int id, Player player) {
        if (plugin.getTrackerKeeper().getFlight().containsKey(player.getUniqueId())) {
            return;
        }
        int next = state + 1;
        boolean missing = false;
        if (plugin.getConfig().getBoolean("difficulty.system_upgrades") && ci == ConsoleInteraction.MULTIPLIER) {
            if (next == 2 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.DISTANCE_1)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 1");
                missing = true;
            }
            if (next == 3 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.DISTANCE_2)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 2");
                missing = true;
            }
            if (next == 4 && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.DISTANCE_3)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Distance 3");
                missing = true;
            }
        }
        if (next > 4 || missing) {
            next = 1;
        }
        if (!missing) {
            // set custom model data for lamp level switch item display
            UUID uuid = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
            if (uuid != null) {
                ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(uuid);
                int which = switch (ci) {
                    case X -> 3;
                    case Z -> 4;
                    default -> 2;
                };
                new WXYZModel().setState(display, plugin, which);
            }
            new InteractionStateSaver(plugin).write(ci.toString(), next, id);
        }
        plugin.getMessenger().announceRepeater(player, ci + ": " + next);
    }
}
