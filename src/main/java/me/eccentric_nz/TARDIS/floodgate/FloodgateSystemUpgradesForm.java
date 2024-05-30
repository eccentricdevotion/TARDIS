package me.eccentric_nz.TARDIS.floodgate;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISystemTree;
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSystemUpgrades;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemUpgradeUpdate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.geysermc.cumulus.response.SimpleFormResponse;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;

import java.util.HashMap;
import java.util.UUID;

public class FloodgateSystemUpgradesForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;
    private SystemUpgrade sysData;

    public FloodgateSystemUpgradesForm(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void send() {
        Player p = plugin.getServer().getPlayer(uuid);
        // get TARDIS player is in
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (!rst.resultSet()) {
            return;
        }
        int id = rst.getTardis_id();
        // must be the owner of the TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        wheret.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false, 2);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "NOT_OWNER");
            return;
        }
        // get player's artron energy level
        ResultSetSystemUpgrades rsp = new ResultSetSystemUpgrades(plugin, id, uuid.toString());
        if (!rsp.resultset()) {
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SYS_TRAVEL_FIRST");
            return;
        }
        sysData = rsp.getData();
        SimpleForm.Builder builder = SimpleForm.builder();
        builder.title("TARDIS System Upgrades");
        builder.content("Artron Level: " + sysData.getArtronLevel());
        // add buttons
        for (GUISystemTree g : GUISystemTree.values()) {
            if (g.getSlot() != -1 && g != GUISystemTree.UPGRADE_TREE) {
                boolean has = sysData.getUpgrades().get(g);
                boolean branch = g.getBranch().equals("branch");
                String prefix = (branch) ? ChatColor.GOLD + "" + ChatColor.ITALIC : "";
                String suffix;
                String image = "sys_locked";
                // does the player have this system upgrade?
                if (!has) {
                    String cost;
                    if (branch) {
                        cost = plugin.getSystemUpgradesConfig().getString("branch");
                        image = "sys_branch_locked";
                    } else {
                        cost = plugin.getSystemUpgradesConfig().getString(g.getBranch() + "." + g.toString().toLowerCase());
                    }
                    suffix = " " + ChatColor.BLUE + ChatColor.ITALIC + "Cost: " + cost;
                } else {
                    suffix = " " + ChatColor.GREEN + ChatColor.ITALIC + "Unlocked";
                    image = (branch) ? "sys_branch_unlocked" : "sys_unlocked";
                }
                builder.button(prefix + g.getName() + suffix, FormImage.Type.URL, String.format("https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/circuit/%s.png", image));
            }
        }
        builder.validResultHandler(this::handleResponse);
        SimpleForm form = builder.build();
        FloodgatePlayer player = FloodgateApi.getInstance().getPlayer(uuid);
        player.sendForm(form);
    }

    private void handleResponse(SimpleFormResponse response) {
        Player player = Bukkit.getPlayer(uuid);
        int button = response.clickedButtonId();
        GUISystemTree clicked;
        switch (button) {
            case 0 -> clicked = GUISystemTree.ARCHITECTURE;
            case 1 -> clicked = GUISystemTree.CHAMELEON_CIRCUIT;
            case 2 -> clicked = GUISystemTree.ROOM_GROWING;
            case 3 -> clicked = GUISystemTree.DESKTOP_THEME;
            case 4 -> clicked = GUISystemTree.NAVIGATION;
            case 5 -> clicked = GUISystemTree.SAVES;
            case 6 -> clicked = GUISystemTree.DISTANCE_1;
            case 7 -> clicked = GUISystemTree.DISTANCE_2;
            case 8 -> clicked = GUISystemTree.DISTANCE_3;
            case 9 -> clicked = GUISystemTree.INTER_DIMENSIONAL_TRAVEL;
            case 10 -> clicked = GUISystemTree.TOOLS;
            case 11 -> clicked = GUISystemTree.TARDIS_LOCATOR;
            case 12 -> clicked = GUISystemTree.BIOME_READER;
            case 13 -> clicked = GUISystemTree.FORCE_FIELD;
            case 14 -> clicked = GUISystemTree.STATTENHEIM_REMOTE;
            default -> clicked = GUISystemTree.UPGRADE_TREE;
        }
        try {
            GUISystemTree required = GUISystemTree.valueOf(clicked.getRequired());
            if (!sysData.getUpgrades().get(required)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_REQUIRED", required.getName());
                player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_TENDRIL_CLICKS, 1.0f, 1.0f);
            } else {
                // check if they have upgrade already
                if (sysData.getUpgrades().get(clicked)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_HAS", clicked.getName());
                    player.playSound(player.getLocation(), Sound.ENTITY_WARDEN_STEP, 1.0f, 1.0f);
                    return;
                }
                // check artron
                int cost;
                if (clicked.getBranch().equals("branch")) {
                    cost = plugin.getSystemUpgradesConfig().getInt("branch");
                } else {
                    cost = plugin.getSystemUpgradesConfig().getInt(clicked.getBranch() + "." + clicked.toString().toLowerCase());
                }
                if (cost > sysData.getArtronLevel()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_COST", clicked.getName());
                    player.playSound(player.getLocation(), Sound.ENTITY_CAT_EAT, 1.0f, 1.0f);
                    return;
                }
                // debit
                HashMap<String, Object> wheretl = new HashMap<>();
                wheretl.put("uuid", uuid);
                plugin.getQueryFactory().alterEnergyLevel("player_prefs", -cost, wheretl, player);
                // play sound
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                // message
                plugin.getMessenger().send(player, TardisModule.TARDIS, "SYS_SUCCESS", clicked.getName());
                // update system upgrade record
                new SystemUpgradeUpdate(plugin).set(uuid.toString(), id, clicked);
            }
        } catch (IllegalArgumentException e) {
            // clicked upgrade tree
            plugin.debug("IllegalArgumentException for " + clicked.getRequired());
        }
    }
}
