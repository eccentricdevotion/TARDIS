package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISInfo;
import me.eccentric_nz.TARDIS.info.TISRecipe;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateIndexEntryForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final TARDISInfoMenu tardisInfoMenu;
    public FloodgateIndexEntryForm(TARDIS plugin, UUID uuid, TARDISInfoMenu tardisInfoMenu) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.tardisInfoMenu = tardisInfoMenu;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Info: " + TARDISStringUtils.capitalise(tardisInfoMenu.toString()));
        for (String key : TARDISInfoMenu.getChildren(tardisInfoMenu.toString()).keySet()) {
            builder.button(key, FormImage.Type.PATH, "textures/items/book.png");
        }
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        String label = response.clickedButton().text();
        String entry = plugin.getTrackerKeeper().getFlight().get(uuid);
        String name = TARDISStringUtils.toEnumUppercase(entry + "_" + TARDISStringUtils.toEnumUppercase(label));
        try {
            TARDISInfoMenu tim = TARDISInfoMenu.valueOf(name);
            if (label.equals("Recipe")) {
                new TISRecipe(plugin).show(player, tim);
            } else {
                new TISInfo(plugin).show(player, tim);
            }
        } catch (IllegalArgumentException ignored) {
        }
        plugin.getTrackerKeeper().getFlight().remove(uuid);
    }
}
