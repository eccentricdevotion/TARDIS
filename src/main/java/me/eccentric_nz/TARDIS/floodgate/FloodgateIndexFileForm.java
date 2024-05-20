package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.UUID;

public class FloodgateIndexFileForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateIndexFileForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Index File");
        for (TISCategory category : TISCategory.values()) {
            builder.button(category.getName(), FormImage.Type.PATH, "textures/items/bookshelf.png");
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = TARDISStringUtils.toEnumUppercase(response.clickedButton().text());
        // open the section GUI
        try {
            TISCategory category = TISCategory.valueOf(label);
            new FloodgateIndexSectionForm(plugin, uuid, category).send();
        } catch (IllegalArgumentException ignored) {
        }
    }
}
