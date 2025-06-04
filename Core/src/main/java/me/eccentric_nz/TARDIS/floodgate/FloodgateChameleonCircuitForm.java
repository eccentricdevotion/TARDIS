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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISChameleonFrame;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.*;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FloodgateChameleonCircuitForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;
    private final ChameleonPreset preset;

    public FloodgateChameleonCircuitForm(TARDIS plugin, UUID uuid, int id, ChameleonPreset preset) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
        this.preset = preset;
    }

    public void send() {
        SimpleForm form = SimpleForm.builder().title("TARDIS Chameleon Circuit")
                .button("Apply", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/apply_button.png")
                .button("Chameleon Circuit", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/chameleon_button.png")
                .button("Adaptive Biome", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/adapt_button.png")
                .button("Adaptive Block", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/adapt_button.png")
                .button("Invisible", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/invisible_button.png")
                .button("Shorted out", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/shorted_button.png")
                .button("Construct", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/construct_button.png")
                .button("Lock", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/lock_button.png")
                .validResultHandler(this::handleResponse)
                .build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        String label = response.clickedButton().text();
        Player player = plugin.getServer().getPlayer(uuid);
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        TARDISChameleonFrame tcf = new TARDISChameleonFrame();
        // set the Chameleon Circuit sign(s)
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        whereh.put("type", Control.CHAMELEON.getId());
        ResultSetControls rsc = new ResultSetControls(plugin, whereh, true);
        boolean hasChameleonSign = rsc.resultSet();
        HashMap<String, Object> wheref = new HashMap<>();
        wheref.put("tardis_id", id);
        wheref.put("type", Control.FRAME.getId());
        ResultSetControls rsf = new ResultSetControls(plugin, wheref, false);
        boolean hasFrame = rsf.resultSet();
        switch (label) {
            case "Apply" -> {
                // rebuild
                player.performCommand("tardis rebuild");
                // damage the circuit if configured
                if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
                    TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                    // decrement uses
                    int uses_left = tcc.getChameleonUses();
                    new TARDISCircuitDamager(plugin, DiskCircuit.CHAMELEON, uses_left, id, player).damage();
                }
            }
            case "Chameleon Circuit" -> {
                // factory
                set.put("adapti_on", 0);
                set.put("chameleon_preset", "FACTORY");
                if (hasChameleonSign) {
                    updateChameleonSign(rsc.getData(), "FACTORY", player);
                }
                if (hasFrame) {
                    tcf.updateChameleonFrame(ChameleonPreset.FACTORY, rsf.getLocation());
                }
                plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", "Factory Fresh", plugin);
            }
            case "Adaptive Biome", "Adaptive Block" -> {
                ChameleonPreset adaptive = (preset.equals(ChameleonPreset.SUBMERGED)) ? ChameleonPreset.SUBMERGED : ChameleonPreset.FACTORY;
                if (hasFrame) {
                    tcf.updateChameleonFrame(adaptive, rsf.getLocation());
                }
                set.put("chameleon_preset", adaptive.toString());
                set.put("adapti_on", label.equals("Adaptive Biome") ? 1 : 2);
            }
            case "Invisible" -> {
                // check they have an Invisibility Circuit
                TARDISCircuitChecker tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
                if (plugin.getConfig().getBoolean("difficulty.circuits") && !plugin.getUtils().inGracePeriod(player, false) && !tcc.hasInvisibility()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "INVISIBILITY_MISSING");
                    break;
                }
                if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.invisibility") > 0) {
                    // decrement uses
                    int uses_left = tcc.getInvisibilityUses();
                    new TARDISCircuitDamager(plugin, DiskCircuit.INVISIBILITY, uses_left, id, player).damage();
                }
                set.put("chameleon_preset", "INVISIBLE");
                if (hasChameleonSign) {
                    updateChameleonSign(rsc.getData(), "INVISIBLE", player);
                }
                if (hasFrame) {
                    tcf.updateChameleonFrame(ChameleonPreset.INVISIBLE, rsf.getLocation());
                }
                plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", "Invisibility", plugin);
            }
            case "Shorted out" -> new FloodgateChameleonPresetForm(plugin, uuid).send();
            case "Construct" -> plugin.getMessenger().send(player, TardisModule.TARDIS, "CONSTRUCT_USE_SHELL");
            case "Lock" -> {
                // set the current adaptive preset as shorted out - this
                // will allow locking in a usually unavailable biome preset
                // ONLY if the Chameleon Circuit is set to Adaptive BIOME
                if (isBiomeAdaptive(id)) {
                    // get current location's biome
                    Current current = TARDISCache.CURRENT.get(id);
                    if (current != null) {
                        Biome biome = current.location().getBlock().getBiome();
                        // get which preset
                        ChameleonPreset which = getAdaption(biome);
                        if (which != null) {
                            set.put("adapti_on", 0);
                            set.put("chameleon_preset", which.toString());
                            if (hasFrame) {
                                tcf.updateChameleonFrame(which, rsf.getLocation());
                            }
                            updateChameleonSign(rsc.getData(), which.toString(), player);
                            plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", which.getDisplayName(), plugin);
                        }
                    }
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CHAM_LOCK");
                }
            }
            default -> {
                // do nothing
            }
        }
        if (!set.isEmpty()) {
            plugin.getQueryFactory().doUpdate("tardis", set, wherec);
        }
    }

    private void updateChameleonSign(ArrayList<HashMap<String, String>> map, String preset, Player player) {
        for (HashMap<String, String> entry : map) {
            TARDISStaticUtils.setSign(entry.get("location"), 3, preset, player);
        }
    }

    private boolean isBiomeAdaptive(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        return rs.resultSet() && rs.getTardis().getAdaption() == Adaption.BIOME;
    }

    private ChameleonPreset getAdaption(Biome biome) {
        try {
            return ChameleonPreset.valueOf(plugin.getAdaptiveConfig().getString(biome.toString()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
