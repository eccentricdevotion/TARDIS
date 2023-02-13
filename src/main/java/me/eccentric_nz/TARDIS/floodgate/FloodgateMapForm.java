package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Pattern;

public class FloodgateMapForm {

    private static final Pattern JSON_FLUFF = Pattern.compile("[\"\\[\\]]");
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getINSTANCE();
    private final Connection connection = service.getConnection();
    private final TARDIS plugin;
    private final String prefix;
    private final List<String> STONE = Collections.singletonList("STONE");
    private final UUID uuid;
    private final int id;
    private final String path = "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/room/%s.png";

    public FloodgateMapForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
        this.prefix = this.plugin.getPrefix();
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Map");
        builder.content("Click a room name to transmat to that room.");
        // get rooms from ARS
        for (String room : getRooms(id)) {
            builder.button(room, FormImage.Type.URL, String.format(path, room.toLowerCase()));
        }
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
    }

    private List<String> getRooms(int id) {
        List<String> data = new ArrayList<>();
        Statement statement = null;
        ResultSet rs = null;
        String query = "SELECT json FROM " + prefix + "ars WHERE tardis_id = " + id;
        try {
            service.testConnection(connection);
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    String json = JSON_FLUFF.matcher(rs.getString("json")).replaceAll("");
                    List<String> materials = new ArrayList<>(Arrays.asList(json.split(",")));
                    materials.removeAll(STONE);
                    for (String material : materials) {
                        // only count if not a console block
                        if (!Consoles.getBY_MATERIALS().containsKey(material)) {
                            String room = TARDISARS.ARSFor(material).toString();
                            data.add(room);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            plugin.debug("ResultSet error for ars getting floodgate rooms! " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                plugin.debug("Error closing ars table getting floodgate rooms! " + e.getMessage());
            }
        }
        return data;
    }
}
