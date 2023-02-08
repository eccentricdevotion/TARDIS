package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.companionGUI.TARDISCompanionAddGUIListener;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateAddCompanionsForm {

    private final TARDIS plugin;
    private final UUID uuid;

    public FloodgateAddCompanionsForm(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
    }

    public void send() {
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS Add Companion");
        builder.content("To ADD a companion select a player button.");
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            builder.button(p.getName());
        }
        builder.button("Everyone");
        builder.validResultHandler(response -> handleResponse(response));
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        String label = response.clickedButton().text();
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rsa = new ResultSetTardis(plugin, wherea, "", false, 0);
        if (rsa.resultSet()) {
            Tardis tardis = rsa.getTardis();
            int id = tardis.getTardis_id();
            String comps = tardis.getCompanions();
            if (label.equals("Everyone")) {
                TARDISCompanionAddGUIListener.addCompanion(id, comps, "everyone");
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    // remove all members
                    String[] data = tardis.getChunk().split(":");
                    plugin.getWorldGuardUtils().removeAllMembersFromRegion(TARDISAliasResolver.getWorldFromAlias(data[0]), player.getName(), player.getUniqueId());
                    // set entry and exit flags to allow
                    plugin.getWorldGuardUtils().setEntryExitFlags(data[0], player.getName(), true);
                }
                TARDISMessage.send(player, "COMPANIONS_ADD", ChatColor.GREEN + "everyone" + ChatColor.RESET);
                TARDISMessage.send(player, "COMPANIONS_EVERYONE");
            } else {
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(label);
                if (op != null) {
                    String u = op.getUniqueId().toString();
                    TARDISCompanionAddGUIListener.addCompanion(id, comps, u);
                    if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                        String[] data = tardis.getChunk().split(":");
                        TARDISCompanionAddGUIListener.addToRegion(data[0], tardis.getOwner(), label);
                        // set entry and exit flags to deny
                        plugin.getWorldGuardUtils().setEntryExitFlags(data[0], label, false);
                    }
                    TARDISMessage.send(player, "COMPANIONS_ADD", ChatColor.GREEN + label + ChatColor.RESET);
                }
            }
        }
    }
}
