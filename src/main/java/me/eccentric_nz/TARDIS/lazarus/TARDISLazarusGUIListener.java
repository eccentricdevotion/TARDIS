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
package me.eccentric_nz.TARDIS.lazarus;

import java.util.Arrays;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISLazarusGUIListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<String, String> disguises = new HashMap<String, String>();

    public TARDISLazarusGUIListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onAreaTerminalClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Genetic Manipulator")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            final String playerNameStr = player.getName();
            final Block b = plugin.getTrackerKeeper().getTrackLazarus().get(playerNameStr);
            if (slot >= 0 && slot < 45) {
                // get selection
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                // remember selection
                String display = im.getDisplayName();
                if ((display.equals("WEEPING ANGEL") || display.equals("CYBERMAN") || display.equals("ICE WARRIOR")) && !plugin.getPM().isPluginEnabled("TARDISWeepingAngels")) {
                    im.setLore(Arrays.asList(new String[]{"Genetic modification not available!"}));
                    is.setItemMeta(im);
                } else {
                    disguises.put(playerNameStr, display);
                }
            }
            if (slot == 47) {
                close(player);
                // animate the manipulator walls
                plugin.getTrackerKeeper().getTrackGeneticmanipulation().add(playerNameStr);
                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISLazarusRunnable(plugin, b), 6L, 6L);
                // undisguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (DisguiseAPI.isDisguised(player)) {
                            DisguiseAPI.undisguiseToAll(player);
                        } else {
                            ItemStack chest = player.getInventory().getChestplate();
                            if (chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName()) {
                                String metaName = chest.getItemMeta().getDisplayName();
                                if (metaName.equals("Weeping Angel Chest")) {
                                    player.performCommand("angeldisguise off");
                                }
                                if (metaName.equals("Cyberman Chest")) {
                                    player.performCommand("cyberdisguise off");
                                }
                                if (metaName.equals("Ice Warrior Chest")) {
                                    player.performCommand("icedisguise off");
                                }
                            }
                        }

                    }
                }, 80L);
                // open the door
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        openDoor(b);
                        untrack(player.getName());
                    }
                }, 100L);
            }
            if (slot == 49) {
                close(player);
                // animate the manipulator walls
                plugin.getTrackerKeeper().getTrackGeneticmanipulation().add(playerNameStr);
                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISLazarusRunnable(plugin, b), 6L, 6L);
                // disguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (DisguiseAPI.isDisguised(player)) {
                            DisguiseAPI.undisguiseToAll(player);
                        }
                        if (disguises.containsKey(playerNameStr)) {
                            String disguise = disguises.get(playerNameStr);
                            if (disguise.equals("WEEPING ANGEL") || disguise.equals("CYBERMAN") || disguise.equals("ICE WARRIOR")) {
                                if (disguise.equals("WEEPING ANGEL")) {
                                    player.performCommand("angeldisguise on");
                                }
                                if (disguise.equals("CYBERMAN")) {
                                    player.performCommand("cyberdisguise on");
                                }
                                if (disguise.equals("ICE WARRIOR")) {
                                    player.performCommand("icedisguise on");
                                }
                            } else {
                                DisguiseType dt = DisguiseType.valueOf(disguise);
                                MobDisguise mobDisguise = new MobDisguise(dt);
                                DisguiseAPI.disguiseToAll(player, mobDisguise);
                            }
                        }
                    }
                }, 80L);
                // open the door
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        openDoor(b);
                        untrack(player.getName());
                    }
                }, 100L);
            }
            if (slot == 51) {
                close(player);
                openDoor(b);
                untrack(player.getName());
            }
        }
    }

    /**
     * Closes the inventory.
     *
     * @param p the player using the GUI
     */
    private void close(final Player p) {
        final String n = p.getName();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
            }
        }, 1L);
    }

    private void untrack(String n) {
        // stop tracking player
        plugin.getTrackerKeeper().getTrackLazarus().remove(n);
        if (disguises.containsKey(n)) {
            disguises.remove(n);
        }
        if (plugin.getTrackerKeeper().getTrackGeneticmanipulation().contains(n)) {
            plugin.getTrackerKeeper().getTrackGeneticmanipulation().remove(n);
        }
    }

    private void openDoor(Block b) {
        b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
        b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
    }
}
