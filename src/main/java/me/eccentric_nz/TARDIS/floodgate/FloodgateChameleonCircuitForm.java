package me.eccentric_nz.TARDIS.floodgate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitDamager;
import me.eccentric_nz.TARDIS.chameleon.TARDISChameleonFrame;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.Adaption;
import me.eccentric_nz.TARDIS.enumeration.Control;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.DiskCircuit;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

public class FloodgateChameleonCircuitForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;
    private final PRESET preset;

    public FloodgateChameleonCircuitForm(TARDIS plugin, UUID uuid, int id, PRESET preset) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
        this.preset = preset;
    }

    public void send() {
        SimpleForm form = SimpleForm.builder()
                .title("TARDIS Chameleon Circuit")
                .button("Apply", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/apply_button.png")
                .button("Chameleon Circuit", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/chameleon_button.png")
                .button("Adaptive Biome", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/adapt_button.png")
                .button("Adaptive Block", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/adapt_button.png")
                .button("Invisible", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/invisible_button.png")
                .button("Shorted out", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/shorted_button.png")
                .button("Construct", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/construct_button.png")
                .button("Lock", FormImage.Type.URL, "https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/lock_button.png")
                .validResultHandler(response -> handleResponse(response))
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
                if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.chameleon") > 0) {
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
                    tcf.updateChameleonFrame(PRESET.FACTORY, rsf.getLocation());
                }
                TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Factory Fresh");
            }
            case "Adaptive Biome", "Adaptive Block" -> {
                PRESET adaptive = (preset.equals(PRESET.SUBMERGED)) ? PRESET.SUBMERGED : PRESET.FACTORY;
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
                if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                    if (!plugin.getUtils().inGracePeriod(player, false) && !tcc.hasInvisibility()) {
                        TARDISMessage.send(player, "INVISIBILITY_MISSING");
                        break;
                    }
                }
                if (plugin.getConfig().getBoolean("circuits.damage") && !plugin.getDifficulty().equals(Difficulty.EASY) && plugin.getConfig().getInt("circuits.uses.invisibility") > 0) {
                    // decrement uses
                    int uses_left = tcc.getInvisibilityUses();
                    new TARDISCircuitDamager(plugin, DiskCircuit.INVISIBILITY, uses_left, id, player).damage();
                }
                set.put("chameleon_preset", "INVISIBLE");
                if (hasChameleonSign) {
                    updateChameleonSign(rsc.getData(), "INVISIBLE", player);
                }
                if (hasFrame) {
                    tcf.updateChameleonFrame(PRESET.INVISIBLE, rsf.getLocation());
                }
                TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + "Invisibility");
            }
            case "Shorted out" -> new FloodgateChameleonPresetForm(plugin, uuid).send();
            case "Construct" -> TARDISMessage.send(player, "CONSTRUCT_USE_SHELL");
            case "Lock" -> {
                // set the current adaptive preset as shorted out - this
                // will allow locking in a usually unavailable biome preset
                // ONLY if the Chameleon Circuit is set to Adaptive BIOME
                if (isBiomeAdaptive(id)) {
                    // get current location's biome
                    HashMap<String, Object> wherel = new HashMap<>();
                    wherel.put("tardis_id", id);
                    ResultSetCurrentLocation rsl = new ResultSetCurrentLocation(plugin, wherel);
                    if (rsl.resultSet()) {
                        Location current = new Location(rsl.getWorld(), rsl.getX(), rsl.getY(), rsl.getZ());
                        Biome biome = current.getBlock().getBiome();
                        // get which preset
                        PRESET which = getAdaption(biome);
                        if (which != null) {
                            set.put("adapti_on", 0);
                            set.put("chameleon_preset", which.toString());
                            if (hasFrame) {
                                tcf.updateChameleonFrame(which, rsf.getLocation());
                            }
                            updateChameleonSign(rsc.getData(), which.toString(), player);
                            TARDISMessage.send(player, "CHAM_SET", ChatColor.AQUA + which.getDisplayName());
                        }
                    }
                } else {
                    TARDISMessage.send(player, "CHAM_LOCK");
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
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        return rs.resultSet() && rs.getTardis().getAdaption() == Adaption.BIOME;
    }

    private PRESET getAdaption(Biome biome) {
        try {
            return PRESET.valueOf(plugin.getAdaptiveConfig().getString(biome.toString()));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
