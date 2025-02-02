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

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.TARDISShellBuilder;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonPreset;
import me.eccentric_nz.TARDIS.chameleon.shell.ShellLoaderProblemBlocks;
import me.eccentric_nz.TARDIS.chameleon.shell.TARDISShellRoomConstructor;
import me.eccentric_nz.TARDIS.chameleon.shell.TARDISShellScanner;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonColumn;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChameleon;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class FloodgateShellLoaderForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final Player player;

    public FloodgateShellLoaderForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.player = this.plugin.getServer().getPlayer(this.uuid);
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Shell Loader");
        builder.button("Current preset", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/chameleon_button.png");
        builder.button("Saved construct", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/construct_button.png");
        for (ChameleonPreset preset : ChameleonPreset.values()) {
            if (!ChameleonPreset.NOT_THESE.contains(preset.getCraftMaterial()) && !preset.usesArmourStand()) {
                if (TARDISPermission.hasPermission(player, "tardis.preset." + preset.toString().toLowerCase(Locale.ROOT))) {
                    String path = String.format("textures/blocks/%s.png", preset.getGuiDisplay().toString().toLowerCase(Locale.ROOT));
                    builder.button(preset.getDisplayName(), FormImage.Type.PATH, path);
                }
            }
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        // get the TARDIS the player is in
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("uuid", uuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
        if (rst.resultSet()) {
            int id = rst.getTardis_id();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                TARDISChameleonColumn chameleonColumn = null;
                ChameleonPreset preset;
                if (label.equals("Current preset")) {
                    // load current preset
                    preset = rs.getTardis().getPreset();
                    if (!rs.getTardis().getAdaption().equals(Adaption.OFF)) {
                        // get the actual preset blocks being used
                        chameleonColumn = TARDISShellScanner.scan(plugin, id, preset);
                    } else {
                        chameleonColumn = plugin.getPresets().getColumn(preset, COMPASS.EAST);
                    }
                } else if (label.equals("Saved construct")) {
                    // load shell
                    preset = ChameleonPreset.CONSTRUCT;
                    // load saved construct
                    HashMap<String, Object> wherec = new HashMap<>();
                    wherec.put("tardis_id", id);
                    ResultSetChameleon rsc = new ResultSetChameleon(plugin, wherec);
                    if (rsc.resultSet()) {
                        // convert to String[][] array
                        String data = rsc.getData().get("blueprintData");
                        if (data != null) {
                            JsonArray json = JsonParser.parseString(data).getAsJsonArray();
                            String[][] strings = new String[10][4];
                            for (int i = 0; i < 10; i++) {
                                JsonArray inner = json.get(i).getAsJsonArray();
                                for (int j = 0; j < 4; j++) {
                                    String block = inner.get(j).getAsString();
                                    strings[i][j] = block;
                                }
                            }
                            chameleonColumn = TARDISChameleonPreset.buildTARDISChameleonColumn(COMPASS.EAST, strings);
                        }
                    }
                } else {
                    // load preset
                    preset = ChameleonPreset.valueOf(label);
                    chameleonColumn = plugin.getPresets().getColumn(preset, COMPASS.EAST);
                }
                if (chameleonColumn != null) {
                    // get the Shell room button
                    HashMap<String, Object> whereb = new HashMap<>();
                    whereb.put("tardis_id", id);
                    whereb.put("type", 25);
                    ResultSetControls rsc = new ResultSetControls(plugin, whereb, false);
                    if (rsc.resultSet()) {
                        // get the start location
                        Location button = TARDISStaticLocationGetters.getLocationFromBukkitString(rsc.getLocation());
                        World w = button.getWorld();
                        int fx = button.getBlockX() + 2;
                        int fy = button.getBlockY() + 1;
                        int fz = button.getBlockZ() - 1;
                        // always clear the platform first
                        // do problem blocks first
                        for (int c = 0; c < 10; c++) {
                            for (int y = fy; y < fy + 4; y++) {
                                Block block = w.getBlockAt(fx + TARDISShellRoomConstructor.orderx[c], y, fz + TARDISShellRoomConstructor.orderz[c]);
                                if (ShellLoaderProblemBlocks.DO_FIRST.contains(block.getType())) {
                                    block.setBlockData(TARDISConstants.AIR);
                                }
                            }
                        }
                        // set to AIR
                        for (int c = 0; c < 10; c++) {
                            for (int y = fy; y < fy + 4; y++) {
                                w.getBlockAt(fx + TARDISShellRoomConstructor.orderx[c], y, fz + TARDISShellRoomConstructor.orderz[c]).setBlockData(TARDISConstants.AIR);
                            }
                        }
                        // build shell in the shell room
                        Location centre = button.clone().add(3, 1, 0);
                        new TARDISShellBuilder(plugin, preset, chameleonColumn, centre, -1).buildPreset();
                    }
                }
            }
        }
    }
}
