/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISBuilderInstanceKeeper;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * The Silurians, also known as Earth Reptiles, Eocenes, Homo reptilia and
 * Psionosauropodomorpha, are a species of Earth reptile. Technologically
 * advanced, they live alongside their aquatic cousins, the Sea Devils.
 *
 * @author eccentric_nz
 */
public class TARDISBlockBreakListener implements Listener {

    private final TARDIS plugin;

    public TARDISBlockBreakListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for the TARDIS Police Box sign being broken. If the sign is
     * broken, then the TARDIS is destroyed, the database records removed and
     * the TARDIS world deleted.
     *
     * @param event a player breaking a block
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onSignBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            // prevent TARDIS block breakage
            Block b = event.getBlock();
            String l = b.getLocation().toString();
            HashMap<String, Object> where = new HashMap<>();
            where.put("location", l);
            where.put("police_box", 1);
            ResultSetBlocks rsb = new ResultSetBlocks(plugin, where, false);
            if (rsb.resultSet()) {
                plugin.getMessenger().sendStatus(player, "TARDIS_BREAK");
                event.setCancelled(true);
                return;
            }
        }
        if (plugin.getTrackerKeeper().getZeroRoomOccupants().contains(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_ZERO");
            return;
        }
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (player.getGameMode().equals(GameMode.CREATIVE) && TARDISBuilderInstanceKeeper.getPrecious().contains(blockType)) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rs = new ResultSetTravellers(plugin, where, false);
            if (rs.resultSet()) {
                // remove protection
                HashMap<String, Object> wherep = new HashMap<>();
                String loc = block.getLocation().toString();
                wherep.put("location", loc);
                wherep.put("police_box", 0);
                plugin.getQueryFactory().doDelete("blocks", wherep);
                plugin.getGeneralKeeper().getProtectBlockMap().remove(loc);
            }
            return;
        }
        if (Tag.WALL_SIGNS.isTagged(blockType)) {
            // check the text on the sign
            Sign sign = (Sign) block.getState();
            SignSide front = sign.getSide(Side.FRONT);
            String line0 = ChatColor.stripColor(front.getLine(0));
            String line1 = ChatColor.stripColor(front.getLine(1));
            String line2 = ChatColor.stripColor(front.getLine(2));
            if (isPresetSign(line0, line1, line2)) {
                event.setCancelled(true);
                sign.update();
            }
        }
        if (blockType == Material.BEACON) {
            Location loc = event.getBlock().getLocation();
            if (loc.getWorld().getName().startsWith("TARDIS")) {
                return;
            }
            String b = loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
            // check if it is a rift manipulator
            for (String r : plugin.getConfig().getConfigurationSection("rechargers").getKeys(false)) {
                if (r.startsWith("rift")) {
                    // get the location
                    World w = TARDISAliasResolver.getWorldFromAlias(plugin.getConfig().getString("rechargers." + r + ".world"));
                    int x = plugin.getConfig().getInt("rechargers." + r + ".x");
                    int y = plugin.getConfig().getInt("rechargers." + r + ".y");
                    int z = plugin.getConfig().getInt("rechargers." + r + ".z");
                    String l = w.getName() + "," + x + "," + y + "," + z;
                    if (l.equals(b)) {
                        if (plugin.getConfig().getString("rechargers." + r + ".uuid").equals(player.getUniqueId().toString())) {
                            plugin.getConfig().set("rechargers." + r, null);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "RIFT_REMOVED");
                            event.setCancelled(true);
                            // drop Rift Manipulator
                            event.getBlock().setBlockData(TARDISConstants.AIR);
                            ItemStack rm = new ItemStack(Material.BEACON, 1);
                            ItemMeta im = rm.getItemMeta();
                            im.setDisplayName("Rift Manipulator");
                            rm.setItemMeta(im);
                            w.dropItem(loc, rm);
                            break;
                        } else {
                            event.setCancelled(true);
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "RIFT_PLAYER");
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean isPresetSign(String l0, String l1, String l2) {
        if (l0.equalsIgnoreCase("WEEPING") || l0.equalsIgnoreCase("$50,000")) {
            return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l0) && l1.equals(plugin.getGeneralKeeper().getSign_lookup().get(l0)));
        } else {
            return (plugin.getGeneralKeeper().getSign_lookup().containsKey(l1) && l2.equals(plugin.getGeneralKeeper().getSign_lookup().get(l1)));
        }
    }
}
