/*
 * Copyright (C) 2013 eccentric_nz
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
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAchievements;
import org.bukkit.Material;
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
    private Player player;
    private String name;
    int size;

    public TARDISAchievementFactory(TARDIS plugin, Player player, String name, int size) {
        this.plugin = plugin;
        this.player = player;
        this.name = name;
        this.size = size;
    }

    public void doAchievement(Object obj) {
        QueryFactory qf = new QueryFactory(plugin);
        // have they started the achievement?
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("player", player.getName());
        wherea.put("name", name);
        wherea.put("completed", 0);
        ResultSetAchievements rsa = new ResultSetAchievements(plugin, wherea, false);
        HashMap<String, Object> seta = new HashMap<String, Object>();
        if (rsa.resultSet()) {
            HashMap<String, Object> wherem = new HashMap<String, Object>();
            wherem.put("a_id", rsa.getA_id());
            boolean achieved = false;
            // check if the achievement has been reached
            List<String> data = null;
            if (obj.getClass().equals(String.class)) {
                // farm mobs & rooms - have they got this type before?
                data = Arrays.asList(rsa.getAmount().split(":"));
                if ((data.size() + 1) == size) {
                    achieved = true;
                }
            } else {
                int req = plugin.getAchivementConfig().getInt(name + ".required");
                int have = plugin.utils.parseNum(rsa.getAmount());
                int sum = have + (Integer) obj;
                if (sum >= req) {
                    achieved = true;
                }
            }
            if (achieved) {
                // award achievement!
                int reward_amount = plugin.getAchivementConfig().getInt(name + ".reward_amount");
                String reward_type = plugin.getAchivementConfig().getString(name + ".reward_type");
                TARDISAchievementNotify tan = new TARDISAchievementNotify(plugin);
                tan.sendAchievement(player, plugin.getAchivementConfig().getString(name + ".message"), Material.valueOf(plugin.getAchivementConfig().getString(name + ".icon")));
                if (reward_type.equalsIgnoreCase("XP")) {
                    new TARDISXPRewarder(player).changeExp(reward_amount);
                } else {
                    ItemStack is = new ItemStack(Material.valueOf(reward_type), reward_amount);
                    Inventory inv = player.getInventory();
                    HashMap<Integer, ItemStack> excess = inv.addItem(is);
                    for (Map.Entry<Integer, ItemStack> me : excess.entrySet()) {
                        player.getWorld().dropItem(player.getLocation(), me.getValue());
                    }
                }
                // set achievement as done
                seta.put("completed", 1);
                qf.doUpdate("achievements", seta, wherem);
            } else {
                if (obj.getClass().equals(String.class)) {
                    if (data != null && !data.contains((String) obj)) {
                        seta.put("amount", rsa.getAmount() + ":" + obj);
                        qf.doUpdate("achievements", seta, wherem);
                    }
                } else {
                    seta.put("amount", plugin.utils.parseNum(rsa.getAmount()) + (Integer) obj);
                    qf.doUpdate("achievements", seta, wherem);
                }
            }
        } else {
            // is it an auto achievement?
            if (plugin.getAchivementConfig().getBoolean(name + ".auto")) {
                // insert a new record
                seta.put("player", player.getName());
                seta.put("name", name);
                seta.put("amount", obj);
                qf.doInsert("achievements", seta);
            }
        }
    }
}
