package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.move.TARDISDoorListener;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
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
        builder.button("Console");
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
        Player player = plugin.getServer().getPlayer(uuid);
        String label = response.clickedButton().text();
        String mat = TARDISARS.valueOf(label).getMaterial().toString();
        Location location = getTransmatLocation(mat);
        if (location != null) {
            TARDISMessage.send(player, "TRANSMAT");
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.playSound(location, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                player.teleport(location);
            }, 10L);
        }
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
                            if (!room.equals("RENDERER")) {
                                data.add(room);
                            }
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

    private Location getTransmatLocation(String room) {
        // get map data
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetARS rs = new ResultSetARS(plugin, where);
        if (rs.resultSet()) {
            String[][][] json = TARDISARSMethods.getGridFromJSON(rs.getJson());
            for (int l = 0; l < 3; l++) {
                for (int r = 0; r < 9; r++) {
                    for (int c = 0; c < 9; c++) {
                        if (room.equals("Console")) {
                            if (Consoles.getBY_MATERIALS().containsKey(json[l][r][c])) {
                                // get inner door tp location
                                TARDISDoorLocation idl = TARDISDoorListener.getDoor(1, id);
                                return idl.getL();
                            }
                        }
                        if (json[l][r][c].equals(room)) {
                            // will always get the first room of this type on this level
                            TARDISARSSlot a = new TARDISARSSlot();
                            a.setChunk(plugin.getLocationUtils().getTARDISChunk(id));
                            a.setY(l);
                            a.setX(r);
                            a.setZ(c);
                            return new Location(a.getChunk().getWorld(), a.getX(), a.getY(), a.getZ()).add(3.5d, 5.0d, 8.5d);
                        }
                    }
                }
            }
        }
        return null;
    }
}
