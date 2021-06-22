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

    public static boolean checkAdvancement(String advancementName) {
        NamespacedKey namespacedKey = new NamespacedKey(TardisPlugin.plugin, advancementName.toLowerCase(Locale.ENGLISH));
        org.bukkit.advancement.Advancement advancement = TardisPlugin.plugin.getServer().getAdvancement(namespacedKey);
        if (advancement != null) {
            TardisPlugin.plugin.debug("Advancement 'tardis:" + advancementName + "' exists :)");
            return true;
        } else {
            TardisPlugin.plugin.debug("There is no advancement with that key; try running /minecraft:reload");
            return false;
        }
    }

    public static void grantAdvancement(Advancement advancement, Player player) {
        NamespacedKey namespacedKey = new NamespacedKey(TardisPlugin.plugin, advancement.getConfigName());
        org.bukkit.advancement.Advancement advancement1 = TardisPlugin.plugin.getServer().getAdvancement(namespacedKey);
        if (advancement1 != null) {
            AdvancementProgress advancementProgress = player.getAdvancementProgress(advancement1);
            if (!advancementProgress.isDone()) {
                TardisPlugin.plugin.getServer().dispatchCommand(TardisPlugin.plugin.getConsole(), "advancement grant " + player.getName() + " only tardis:" + advancement.getConfigName());
            }
        } else {
            player.sendMessage(ChatColor.YELLOW + "Advancement Made!");
            player.sendMessage(ChatColor.WHITE + TardisPlugin.plugin.getAdvancementConfig().getString(advancement.getConfigName() + ".message"));
        }
    }

    public void doAdvancement(Object object) {
        if (plugin.getConfig().getBoolean("allow.advancements")) {
            // have they started the advancement?
            HashMap<String, Object> whereAdvancement = new HashMap<>();
            whereAdvancement.put("uuid", player.getUniqueId().toString());
            whereAdvancement.put("name", advancement.getConfigName());
            whereAdvancement.put("completed", 0);
            ResultSetAdvancements resultSetAdvancements = new ResultSetAdvancements(plugin, whereAdvancement, false);
            HashMap<String, Object> setAdvancement = new HashMap<>();
            if (resultSetAdvancements.resultSet()) {
                HashMap<String, Object> whereM = new HashMap<>(); // TODO Clarify this name.
                whereM.put("a_id", resultSetAdvancements.getAdvancementId());
                boolean achieved = false;
                // check if the advancement has been reached
                List<String> data = null;
                String amount = (resultSetAdvancements.getAmount().isEmpty()) ? "0" : resultSetAdvancements.getAmount();
                if (object.getClass().equals(String.class)) {
                    // farm mobs & rooms - have they got this type before?
                    data = Arrays.asList(amount.split(":"));
                    if ((data.size() + 1) == size) {
                        achieved = true;
                    }
                } else {
                    int required = plugin.getAdvancementConfig().getInt(advancement + ".required");
                    int currentAmount = TardisNumberParsers.parseInt(amount);
                    int sum = currentAmount + (Integer) object;
                    if (sum >= required) {
                        achieved = true;
                    }
                }
                if (achieved) {
                    // award advancement!
                    int rewardAmount = plugin.getAdvancementConfig().getInt(advancement.getConfigName() + ".reward_amount");
                    String rewardType = plugin.getAdvancementConfig().getString(advancement.getConfigName() + ".reward_type");
                    // display a proper advancement if possible
                    grantAdvancement(advancement, player);
                    assert rewardType != null;
                    if (rewardType.equalsIgnoreCase("XP")) {
                        new TardisExperienceRewarder(player).changeExp(rewardAmount);
                    } else {
                        ItemStack itemStack = new ItemStack(Material.valueOf(rewardType), rewardAmount);
                        Inventory inventory = player.getInventory();
                        HashMap<Integer, ItemStack> excess = inventory.addItem(itemStack);
                        excess.forEach((key, value) -> player.getWorld().dropItem(player.getLocation(), value));
                    }
                    // set advancement as done
                    setAdvancement.put("completed", 1);
                    plugin.getQueryFactory().doUpdate("advancements", setAdvancement, whereM);
                } else {
                    if (object.getClass().equals(String.class)) {
                        if (!data.contains(object)) {
                            setAdvancement.put("amount", amount + ":" + object);
                            plugin.getQueryFactory().doUpdate("advancements", setAdvancement, whereM);
                        }
                    } else {
                        setAdvancement.put("amount", TardisNumberParsers.parseInt(amount) + (Integer) object);
                        plugin.getQueryFactory().doUpdate("advancements", setAdvancement, whereM);
                    }
                }
            } else {
                // is it an auto advancement?
                if (plugin.getAdvancementConfig().getBoolean(advancement.getConfigName() + ".auto")) {
                    // insert a new record
                    setAdvancement.put("uuid", player.getUniqueId().toString());
                    setAdvancement.put("name", advancement.getConfigName());
                    setAdvancement.put("amount", object);
                    setAdvancement.put("completed", 0);
                    plugin.getQueryFactory().doInsert("advancements", setAdvancement);
                }
            }
        }
    }
}
