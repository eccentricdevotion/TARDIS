package me.eccentric_nz.TARDIS.console.interaction;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.HelmicRegulatorModel;
import me.eccentric_nz.TARDIS.database.InteractionStateSaver;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeChecker;
import org.bukkit.World;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * A TARDIS leaves temporary wakes and time ripples in the Vortex during travel. To ensure safe travel the TARDIS uses
 * the helmic regulator to set up the proper Boolean constraints to regulate the Planck-Collapse within the Vortex, and
 * stabilize the chronon beam to avoid complete overload of the Time Spiral's polyhelixes on the macrotransablative
 * level. While the regulator normally works automatically there is a manual control on the console which can override
 * the navigational instruments. This control is quite sensitive and rotating it will change the timeship's course
 * through the Vortex, sending it thousands of years off course.
 */
public class HelmicRegulatorInteraction {

    private final TARDIS plugin;

    public HelmicRegulatorInteraction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void selectWorld(int state, int id, Player player, Interaction interaction) {
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getFlight().containsKey(uuid)) {
            if (interaction.getLocation().toString().equals(plugin.getTrackerKeeper().getFlight().get(uuid))) {
                plugin.getTrackerKeeper().getCount().put(uuid, plugin.getTrackerKeeper().getCount().getOrDefault(uuid, 0) + 1);
            }
            plugin.getTrackerKeeper().getFlight().remove(uuid);
        } else {
            int next = state + 1;
            if (next > 8 || player.isSneaking()) {
                next = 0;
            }
            String which = "OFF";
            if (next > 0) {
                // get world name
                which = getWorldFromState(next);
            }
            if (which.equals("OFF")) {
                next = 0;
            } else if (plugin.getConfig().getBoolean("difficulty.system_upgrades")) {
                World world = plugin.getServer().getWorld(which);
                if (world != null && (world.getEnvironment() == World.Environment.NETHER || world.getEnvironment() == World.Environment.THE_END) && !new SystemUpgradeChecker(plugin).has(player.getUniqueId().toString(), SystemTree.INTER_DIMENSIONAL_TRAVEL)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_NEED", "Inter Dimensional Travel");
                    next = 0;
                }
            }
            // show title
            plugin.getMessenger().announceRepeater(player, which);
            // save state
            new InteractionStateSaver(plugin).write("HELMIC_REGULATOR", next, id);
            // set custom model data for helmic regulator item display
            UUID model = interaction.getPersistentDataContainer().get(plugin.getModelUuidKey(), plugin.getPersistentDataTypeUUID());
            if (model != null) {
                ItemDisplay display = (ItemDisplay) plugin.getServer().getEntity(model);
                new HelmicRegulatorModel().setState(display, next);
            }
        }
    }

    private String getWorldFromState(int state) {
        for (String w : plugin.getPlanetsConfig().getConfigurationSection("planets").getKeys(false)) {
            if (plugin.getPlanetsConfig().getBoolean("planets." + w + ".enabled") && plugin.getPlanetsConfig().getBoolean("planets." + w + ".time_travel") && plugin.getPlanetsConfig().getInt("planets." + w + ".helmic_regulator_order") == state) {
                return w;
            }
        }
        return "OFF";
    }
}
