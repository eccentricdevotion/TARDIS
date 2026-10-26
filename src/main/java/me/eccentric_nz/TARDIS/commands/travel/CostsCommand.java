package me.eccentric_nz.TARDIS.commands.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;

public class CostsCommand {

    private final TARDIS plugin;
    private final List<String> costs = List.of("random", "random_circuit", "travel", "comehere", "hide", "rebuild", "autonomous", "backdoor");

    public CostsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void show(Player player) {
        plugin.getMessenger().send(player, TardisModule.TARDIS, "TRAVEL_COSTS");
        for (String s : costs) {
            String c = (s.equals("rebuild")) ? plugin.getArtronConfig().getString("random") : plugin.getArtronConfig().getString(s);
            plugin.getMessenger().messageWithNamedColour(player, "    " + s + ": ", c, NamedTextColor.AQUA);
        }
    }
}
