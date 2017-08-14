/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.achievement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import me.eccentric_nz.TARDIS.enumeration.ADVANCEMENT;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Rassilon was the founder of Time Lord civilisation and perhaps the single
 * greatest figure of Gallifreyan history. He was generally considered the first
 * Time Lord. Rassilon brought the Eye of Harmony, actually the singularity of a
 * black hole, to Gallifrey. He invented TARDISes.
 *
 * @author eccentric_nz
 */
public class TARDISAchievementFactory {

    private final TARDIS plugin;
    private final Player player;
    private final ADVANCEMENT advancement;
    int size;

    public TARDISAchievementFactory(TARDIS plugin, Player player, ADVANCEMENT advancement, int size) {
        this.plugin = plugin;
        this.player = player;
        this.advancement = advancement;
        this.size = size;
    }

    public void doAchievement(Object obj) {
        QueryFactory qf = new QueryFactory(plugin);
        // have they started the achievement?
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("uuid", player.getUniqueId().toString());
        wherea.put("name", advancement.getConfigName());
        wherea.put("completed", 0);
        ResultSetAchievements rsa = new ResultSetAchievements(plugin, wherea, false);
        HashMap<String, Object> seta = new HashMap<>();
        if (rsa.resultSet()) {
            HashMap<String, Object> wherem = new HashMap<>();
            wherem.put("a_id", rsa.getA_id());
            boolean achieved = false;
            // check if the achievement has been reached
            List<String> data = null;
            String amount = (rsa.getAmount().isEmpty()) ? "0" : rsa.getAmount();
            if (obj.getClass().equals(String.class)) {
                // farm mobs & rooms - have they got this type before?
                data = Arrays.asList(amount.split(":"));
                if ((data.size() + 1) == size) {
                    achieved = true;
                }
            } else {
                int req = plugin.getAchievementConfig().getInt(advancement + ".required");
                int have = TARDISNumberParsers.parseInt(amount);
                int sum = have + (Integer) obj;
                if (sum >= req) {
                    achieved = true;
                }
            }
            if (achieved) {
                // award achievement!
                int reward_amount = plugin.getAchievementConfig().getInt(advancement.getConfigName() + ".reward_amount");
                String reward_type = plugin.getAchievementConfig().getString(advancement.getConfigName() + ".reward_type");
                // display a proper advancement if possible
                grantAdvancement(advancement, player);
                if (reward_type.equalsIgnoreCase("XP")) {
                    new TARDISXPRewarder(player).changeExp(reward_amount);
                } else {
                    ItemStack is = new ItemStack(Material.valueOf(reward_type), reward_amount);
                    Inventory inv = player.getInventory();
                    HashMap<Integer, ItemStack> excess = inv.addItem(is);
                    excess.entrySet().forEach((me) -> {
                        player.getWorld().dropItem(player.getLocation(), me.getValue());
                    });
                }
                // set achievement as done
                seta.put("completed", 1);
                qf.doUpdate("achievements", seta, wherem);
            } else {
                if (obj.getClass().equals(String.class)) {
                    if (data != null && !data.contains((String) obj)) {
                        seta.put("amount", amount + ":" + obj);
                        qf.doUpdate("achievements", seta, wherem);
                    }
                } else {
                    seta.put("amount", TARDISNumberParsers.parseInt(amount) + (Integer) obj);
                    qf.doUpdate("achievements", seta, wherem);
                }
            }
        } else {
            // is it an auto achievement?
            if (plugin.getAchievementConfig().getBoolean(advancement.getConfigName() + ".auto")) {
                // insert a new record
                seta.put("uuid", player.getUniqueId().toString());
                seta.put("name", advancement.getConfigName());
                seta.put("amount", obj);
                seta.put("completed", 0);
                qf.doInsert("achievements", seta);
            }
        }
    }

    public static boolean checkAdvancement(String adv) {
        NamespacedKey nsk = new NamespacedKey(TARDIS.plugin, "drwho/" + adv);
        Advancement a = TARDIS.plugin.getServer().getAdvancement(nsk);
        if (a != null) {
            TARDIS.plugin.debug("Advancement 'tardis:drwho/" + adv + "' exists :)");
            return true;
        } else {
            TARDIS.plugin.debug("There is no advancement with that key, try reloading - /minecraft:reload");
            return false;
        }
    }

    public static void grantAdvancement(ADVANCEMENT adv, Player player) {
        NamespacedKey nsk = new NamespacedKey(TARDIS.plugin, "drwho/" + adv.getConfigName());
        Advancement a = TARDIS.plugin.getServer().getAdvancement(nsk);
        if (a != null) {
            AdvancementProgress avp = player.getAdvancementProgress(a);
            if (!avp.isDone()) {
                TARDIS.plugin.getServer().dispatchCommand(TARDIS.plugin.getConsole(), "advancement grant " + player.getName() + " only tardis:drwho/" + adv.getConfigName());
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "Advancement Made!");
            player.sendMessage(ChatColor.WHITE + TARDIS.plugin.getAchievementConfig().getString(adv.getConfigName() + ".message"));
        }
    }
}
