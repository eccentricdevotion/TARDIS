package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISRandomButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class RandomAction {

    private final TARDIS plugin;

    public RandomAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void process(Set<UUID> cooldown, Player player, int id, Tardis tardis, int secondary) {
        if (cooldown.contains(player.getUniqueId())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "SPAM_WAIT");
            return;
        }
        cooldown.add(player.getUniqueId());
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> cooldown.remove(player.getUniqueId()), 60L);
        if (plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id) || (!tardis.isHandbrakeOn() && !plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) || plugin.getTrackerKeeper().getHasRandomised().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_TRAVELLING");
            return;
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            plugin.getTrackerKeeper().getHasRandomised().add(id);
        }
        new TARDISRandomButton(plugin, player, id, tardis.getArtronLevel(), secondary, tardis.getCompanions(), tardis.getUuid()).clickButton();
    }
}
