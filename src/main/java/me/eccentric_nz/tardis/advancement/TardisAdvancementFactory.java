/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.advancement;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetAdvancements;
import me.eccentric_nz.tardis.enumeration.Advancement;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Rassilon was the founder of Time Lord civilisation and perhaps the single greatest figure of Gallifreyan history. He
 * was generally considered the first Time Lord. Rassilon brought the Eye of Harmony, actually the singularity of a
 * black hole, to Gallifrey. He invented TARDISes.
 *
 * @author eccentric_nz
 */
public class TardisAdvancementFactory {

    private final TardisPlugin plugin;
    private final Player player;
    private final Advancement advancement;
    private final int size;

    public TardisAdvancementFactory(TardisPlugin plugin, Player player, Advancement advancement, int size) {
        this.plugin = plugin;
        this.player = player;
        this.advancement = advancement;
        this.size = size;
    }

    public static boolean checkAdvancement(String adv) {
        NamespacedKey nsk = new NamespacedKey(TardisPlugin.plugin, adv.toLowerCase(Locale.ENGLISH));
        org.bukkit.advancement.Advancement a = TardisPlugin.plugin.getServer().getAdvancement(nsk);
        if (a != null) {
            TardisPlugin.plugin.debug("Advancement 'tardis:" + adv + "' exists :)");
            return true;
        } else {
            TardisPlugin.plugin.debug("There is no advancement with that key; try running /minecraft:reload");
            return false;
        }
    }

    public static void grantAdvancement(Advancement adv, Player player) {
        NamespacedKey nsk = new NamespacedKey(TardisPlugin.plugin, adv.getConfigName());
        org.bukkit.advancement.Advancement a = TardisPlugin.plugin.getServer().getAdvancement(nsk);
        if (a != null) {
            AdvancementProgress avp = player.getAdvancementProgress(a);
            if (!avp.isDone()) {
                TardisPlugin.plugin.getServer().dispatchCommand(TardisPlugin.plugin.getConsole(), "advancement grant " + player.getName() + " only tardis:" + adv.getConfigName());
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "Advancement Made!");
            player.sendMessage(ChatColor.WHITE + TardisPlugin.plugin.getAdvancementConfig().getString(adv.getConfigName() + ".message"));
        }
    }

    public void doAdvancement(Object obj) {
        if (plugin.getConfig().getBoolean("allow.advancements")) {
            // have they started the advancement?
            HashMap<String, Object> whereA = new HashMap<>();
            whereA.put("uuid", player.getUniqueId().toString());
            whereA.put("name", advancement.getConfigName());
            whereA.put("completed", 0);
            ResultSetAdvancements rsa = new ResultSetAdvancements(plugin, whereA, false);
            HashMap<String, Object> setA = new HashMap<>();
            if (rsa.resultSet()) {
                HashMap<String, Object> whereM = new HashMap<>();
                whereM.put("a_id", rsa.getaId());
                boolean achieved = false;
                // check if the advancement has been reached
                List<String> data = null;
                String amount = (rsa.getAmount().isEmpty()) ? "0" : rsa.getAmount();
                if (obj.getClass().equals(String.class)) {
                    // farm mobs & rooms - have they got this type before?
                    data = Arrays.asList(amount.split(":"));
                    if ((data.size() + 1) == size) {
                        achieved = true;
                    }
                } else {
                    int req = plugin.getAdvancementConfig().getInt(advancement + ".required");
                    int have = TardisNumberParsers.parseInt(amount);
                    int sum = have + (Integer) obj;
                    if (sum >= req) {
                        achieved = true;
                    }
                }
                if (achieved) {
                    // award advancement!
                    int reward_amount = plugin.getAdvancementConfig().getInt(advancement.getConfigName() + ".reward_amount");
                    String reward_type = plugin.getAdvancementConfig().getString(advancement.getConfigName() + ".reward_type");
                    // display a proper advancement if possible
                    grantAdvancement(advancement, player);
                    assert reward_type != null;
                    if (reward_type.equalsIgnoreCase("XP")) {
                        new TardisXpRewarder(player).changeExp(reward_amount);
                    } else {
                        ItemStack is = new ItemStack(Material.valueOf(reward_type), reward_amount);
                        Inventory inv = player.getInventory();
                        HashMap<Integer, ItemStack> excess = inv.addItem(is);
                        excess.forEach((key, value) -> player.getWorld().dropItem(player.getLocation(), value));
                    }
                    // set advancement as done
                    setA.put("completed", 1);
                    plugin.getQueryFactory().doUpdate("advancements", setA, whereM);
                } else {
                    if (obj.getClass().equals(String.class)) {
                        if (!data.contains(obj)) {
                            setA.put("amount", amount + ":" + obj);
                            plugin.getQueryFactory().doUpdate("advancements", setA, whereM);
                        }
                    } else {
                        setA.put("amount", TardisNumberParsers.parseInt(amount) + (Integer) obj);
                        plugin.getQueryFactory().doUpdate("advancements", setA, whereM);
                    }
                }
            } else {
                // is it an auto advancement?
                if (plugin.getAdvancementConfig().getBoolean(advancement.getConfigName() + ".auto")) {
                    // insert a new record
                    setA.put("uuid", player.getUniqueId().toString());
                    setA.put("name", advancement.getConfigName());
                    setA.put("amount", obj);
                    setA.put("completed", 0);
                    plugin.getQueryFactory().doInsert("advancements", setA);
                }
            }
        }
    }
}
