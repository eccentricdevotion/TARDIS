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
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
    private final HashMap<String, Integer> horses = new HashMap<String, Integer>();
    private final HashMap<String, Integer> sheep = new HashMap<String, Integer>();
    private final HashMap<String, Integer> cats = new HashMap<String, Integer>();
    private final HashMap<String, Integer> professions = new HashMap<String, Integer>();
    private final HashMap<String, Integer> slimes = new HashMap<String, Integer>();
    private final List<Integer> slimeSizes = Arrays.asList(new Integer[]{1, 2, 4});

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
            if (slot >= 0 && slot < 36) {
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
                    // TODO switch slot 41 depending on type
                }
            }
            if (slot == 37) { // The Master Switch : ON | OFF
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                if (player.hasPermission("tardis.themaster")) {
                    String onoff = (im.getLore().get(0).equals("OFF")) ? "ON" : "OFF";
                    im.setLore(Arrays.asList(new String[]{onoff}));
                } else {
                    im.setLore(Arrays.asList(new String[]{"You do not have permission", "to be The Master!"}));
                }
                is.setItemMeta(im);
            }
            if (slot == 39) { // adult / baby
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                String onoff = (im.getLore().get(0).equals("ADULT")) ? "BABY" : "ADULT";
                im.setLore(Arrays.asList(new String[]{onoff}));
                is.setItemMeta(im);
            }
            if (slot == 41) { // type / colour
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                String type = "WHITE";
                if (!disguises.containsKey(playerNameStr) || disguises.get(playerNameStr).equals("SHEEP")) {
                    int ordinal;
                    if (sheep.containsKey(playerNameStr)) {
                        ordinal = (sheep.get(playerNameStr) + 1 < Integer.valueOf(16)) ? sheep.get(playerNameStr) + 1 : 0;
                    } else {
                        ordinal = 1;
                    }
                    type = DyeColor.values()[ordinal].toString();
                    sheep.put(playerNameStr, ordinal);
                } else {
                    if (disguises.get(playerNameStr).equals("HORSE")) {
                        int ordinal;
                        if (horses.containsKey(playerNameStr)) {
                            ordinal = (horses.get(playerNameStr) + 1 < Integer.valueOf(7)) ? horses.get(playerNameStr) + 1 : 0;
                        } else {
                            ordinal = 0;
                        }
                        type = Horse.Color.values()[ordinal].toString();
                        horses.put(playerNameStr, ordinal);
                    }
                    if (disguises.get(playerNameStr).equals("OCELOT")) {
                        int ordinal;
                        if (cats.containsKey(playerNameStr)) {
                            ordinal = (cats.get(playerNameStr) + 1 < Integer.valueOf(4)) ? cats.get(playerNameStr) + 1 : 0;
                        } else {
                            ordinal = 0;
                        }
                        type = Ocelot.Type.values()[ordinal].toString();
                        cats.put(playerNameStr, ordinal);
                    }
                    if (disguises.get(playerNameStr).equals("VILLAGER")) {
                        int ordinal;
                        if (professions.containsKey(playerNameStr)) {
                            ordinal = (professions.get(playerNameStr) + 1 < Integer.valueOf(5)) ? professions.get(playerNameStr) + 1 : 0;
                        } else {
                            ordinal = 0;
                        }
                        type = Villager.Profession.values()[ordinal].toString();
                        professions.put(playerNameStr, ordinal);
                    }
                    if (disguises.get(playerNameStr).equals("SLIME")) {
                        int ordinal;
                        if (slimes.containsKey(playerNameStr)) {
                            ordinal = (slimes.get(playerNameStr) + 1 < Integer.valueOf(3)) ? slimes.get(playerNameStr) + 1 : 0;
                        } else {
                            ordinal = 0;
                        }
                        type = slimeSizes.get(ordinal).toString();
                        slimes.put(playerNameStr, ordinal);
                    }
                }
                im.setLore(Arrays.asList(new String[]{type}));
                is.setItemMeta(im);
            }
            if (slot == 43) { // Tamed / Flying / Blazing / Powered / Agressive : TRUE | FALSE
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                String truefalse = (im.getLore().get(0).equals("FALSE")) ? "TRUE" : "FALSE";
                im.setLore(Arrays.asList(new String[]{truefalse}));
                is.setItemMeta(im);
            }
            if (slot == 47) {
                close(player);
                // animate the manipulator walls
                plugin.getTrackerKeeper().getTrackGeneticManipulation().add(playerNameStr);
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
                plugin.getTrackerKeeper().getTrackGeneticManipulation().add(playerNameStr);
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

    @EventHandler
    public void onLazarusClose(InventoryCloseEvent event) {
        String name = event.getInventory().getTitle();
        String playerNameStr = event.getPlayer().getName();
        if (name.equals("§4Genetic Manipulator") && !plugin.getTrackerKeeper().getTrackGeneticManipulation().contains(playerNameStr)) {
            Block b = plugin.getTrackerKeeper().getTrackLazarus().get(event.getPlayer().getName());
            if (b.getRelative(BlockFace.SOUTH).getType().equals(Material.COBBLE_WALL)) {
                b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
                b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
            }
            untrack(playerNameStr);
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
        if (plugin.getTrackerKeeper().getTrackLazarus().containsKey(n)) {
            plugin.getTrackerKeeper().getTrackLazarus().remove(n);
        }
        if (disguises.containsKey(n)) {
            disguises.remove(n);
        }
        if (sheep.containsKey(n)) {
            sheep.remove(n);
        }
        if (horses.containsKey(n)) {
            horses.remove(n);
        }
        if (cats.containsKey(n)) {
            cats.remove(n);
        }
        if (professions.containsKey(n)) {
            professions.remove(n);
        }
        if (slimes.containsKey(n)) {
            slimes.remove(n);
        }
        if (plugin.getTrackerKeeper().getTrackGeneticManipulation().contains(n)) {
            plugin.getTrackerKeeper().getTrackGeneticManipulation().remove(n);
        }
    }

    private void openDoor(Block b) {
        b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
        b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
    }
}
