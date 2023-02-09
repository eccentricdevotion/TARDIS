package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.custommodeldata.GUITemporalLocator;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateTemporalForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateTemporalForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Temporal Locator");
        for (GUITemporalLocator clock : GUITemporalLocator.values()) {
            builder.button(clock.getName()+" ~"+clock.getLore(), FormImage.Type.PATH, "textures/items/clock_item.png");
        }
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        String label = response.clickedButton().text();
        long ticks = getTime(label);
        plugin.getTrackerKeeper().getSetTime().put(player.getUniqueId(), ticks);
        TARDISMessage.send(player, "TEMPORAL_SET", String.format("%d", ticks));
        // damage the circuit if configured
        if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.temporal") > 0) {
            int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
            TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
            tcc.getCircuits();
            // decrement uses
            int uses_left = tcc.getTemporalUses();
            new TARDISCircuitDamager(plugin, DiskCircuit.TEMPORAL, uses_left, id, player).damage();
        }
    }

    private long getTime(String label) {
        String[] data = label.split("~");
        return TARDISNumberParsers.parseLong(data[1]);
    }
}
