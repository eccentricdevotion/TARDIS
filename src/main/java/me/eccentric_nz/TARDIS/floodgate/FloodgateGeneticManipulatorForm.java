package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FloodgateGeneticManipulatorForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final List<Material> disguises = new ArrayList<>();
    private final String path = "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/spawn_eggs/%s.png";

    public FloodgateGeneticManipulatorForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Genetic Manipulator");
        for (Material m : FloodgateSpawnEgg.disguises) {
            builder.button(m.toString().replace("_SPAWN_EGG", ""), FormImage.Type.PATH, String.format(path, m.toString().toLowerCase(Locale.ROOT)));
        }
        // TODO add other buttons
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        // TODO disguise player etc
    }
}
