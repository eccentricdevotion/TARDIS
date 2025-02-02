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
import me.eccentric_nz.TARDIS.database.data.SystemUpgrade;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetSystemUpgrades;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.upgrades.SystemTree;
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
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class FloodgateSystemUpgradesForm {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;
    private SystemUpgrade sysData;
    private List<SystemTree> notThese = List.of(SystemTree.UPGRADE_TREE, SystemTree.EXTERIOR_FLIGHT, SystemTree.MONITOR);

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
        for (SystemTree g : SystemTree.values()) {
            if (g.getSlot() != -1 && !notThese.contains(g)) {
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
                        cost = plugin.getSystemUpgradesConfig().getString(g.getBranch() + "." + g.toString().toLowerCase(Locale.ROOT));
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
        SystemTree clicked;
        switch (button) {
            case 0 -> clicked = SystemTree.ARCHITECTURE;
            case 1 -> clicked = SystemTree.CHAMELEON_CIRCUIT;
            case 2 -> clicked = SystemTree.ROOM_GROWING;
            case 3 -> clicked = SystemTree.DESKTOP_THEME;
            case 4 -> clicked = SystemTree.FEATURE;
            case 5 -> clicked = SystemTree.SAVES;
            case 6 -> clicked = SystemTree.FORCE_FIELD;
            case 7 -> clicked = SystemTree.TOOLS;
            case 8 -> clicked = SystemTree.TARDIS_LOCATOR;
            case 9 -> clicked = SystemTree.TELEPATHIC_CIRCUIT;
            case 10 -> clicked = SystemTree.STATTENHEIM_REMOTE;
            case 11 -> clicked = SystemTree.NAVIGATION;
            case 12 -> clicked = SystemTree.DISTANCE_1;
            case 13 -> clicked = SystemTree.DISTANCE_2;
            case 14 -> clicked = SystemTree.DISTANCE_3;
            case 15 -> clicked = SystemTree.INTER_DIMENSIONAL_TRAVEL;
            case 16 -> clicked = SystemTree.THROTTLE;
            case 17 -> clicked = SystemTree.FASTER;
            case 18 -> clicked = SystemTree.RAPID;
            case 19 -> clicked = SystemTree.WARP;
            default -> clicked = SystemTree.UPGRADE_TREE;
        }
        try {
            SystemTree required = SystemTree.valueOf(clicked.getRequired());
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
                    cost = plugin.getSystemUpgradesConfig().getInt(clicked.getBranch() + "." + clicked.toString().toLowerCase(Locale.ROOT));
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
