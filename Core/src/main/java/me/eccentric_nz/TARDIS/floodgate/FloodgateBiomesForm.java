package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.telepathic.EnvironmentBiomes;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisArtron;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.List;
import java.util.UUID;

public class FloodgateBiomesForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public FloodgateBiomesForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void send() {
        // get biomes
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("Telepathic Biome Finder");
        // only show biomes for the environment the TARDIS is currently in
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (rsc.resultSet()) {
            World.Environment environment = rsc.getWorld().getEnvironment();
            // biome finder
            List<Biome> biomes;
            switch (environment) {
                case NETHER -> biomes = EnvironmentBiomes.NETHER;
                case THE_END -> biomes = EnvironmentBiomes.END;
                default -> biomes = EnvironmentBiomes.OVERWORLD;
            }
            for (Biome biome : biomes) {
                String path = FloodgateBiomes.BIOME_BLOCKS.get(biome);
                builder.button(TARDISStringUtils.capitalise(biome.toString()), FormImage.Type.PATH, String.format("textures/%s", path));
            }
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        // get tardis artron level
        ResultSetTardisArtron rs = new ResultSetTardisArtron(plugin);
        if (!rs.fromID(id)) {
            return;
        }
        int level = rs.getArtronLevel();
        int travel = plugin.getArtronConfig().getInt("travel");
        if (level < travel) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_ENOUGH_ENERGY");
            return;
        }
        String enumStr = TARDISStringUtils.toEnumUppercase(response.clickedButton().text());
        player.performCommand("tardistravel biome " + enumStr);
    }
}
