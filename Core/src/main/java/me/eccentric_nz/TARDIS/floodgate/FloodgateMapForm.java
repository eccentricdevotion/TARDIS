/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.ARS.TARDISARS;
import me.eccentric_nz.TARDIS.ARS.TARDISARSMethods;
import me.eccentric_nz.TARDIS.ARS.TARDISARSSlot;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
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
    private final List<String> STONE = List.of("STONE");
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
            builder.button(room, FormImage.Type.URL, String.format(path, room.toLowerCase(Locale.ROOT)));
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = plugin.getServer().getPlayer(uuid);
        String label = response.clickedButton().text();
        String mat;
        if (label.equals("Console")) {
            mat = getConsoleMaterial(id);
        } else {
            mat = TARDISARS.valueOf(label).getMaterial().toString();
        }
        Location location = getTransmatLocation(mat);
        if (location != null) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
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
                    List<String> materials = new ArrayList<>(List.of(json.split(",")));
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

    private String getConsoleMaterial(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Schematic console = rs.getTardis().getSchematic();
            return console.getSeedMaterial().toString();
        }
        return "IRON_BLOCK"; // budget
    }
}
