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
import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.watchers.AgeableWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.BatWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.BlazeWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.CreeperWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.EndermanWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.OcelotWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PigWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SheepWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.VillagerWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ZombieWatcher;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;
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
        final Inventory inv = event.getInventory();
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
                    setSlotFourtyOne(inv, display, playerNameStr);
                }
            }
            if (slot == 37) { // The Master Switch : ON | OFF
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                if (player.hasPermission("tardis.themaster")) {
                    if (plugin.getTrackerKeeper().getTrackImmortalityGate().equals("")) {
                        String onoff = (im.getLore().get(0).equals("OFF")) ? "ON" : "OFF";
                        im.setLore(Arrays.asList(new String[]{onoff}));
                    } else {
                        im.setLore(Arrays.asList(new String[]{"The Master Race is already", " set to " + plugin.getTrackerKeeper().getTrackImmortalityGate() + "!", "Try again later."}));
                    }
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
                if (disguises.containsKey(playerNameStr)) {
                    setSlotFourtyOne(inv, disguises.get(playerNameStr), playerNameStr);
                }
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
                plugin.getUtils().playTARDISSound(player.getLocation(), player, "lazarus_machine");
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
                plugin.getUtils().playTARDISSound(player.getLocation(), player, "lazarus_machine");
                // disguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (DisguiseAPI.isDisguised(player)) {
                            DisguiseAPI.undisguiseToAll(player);
                        }
                        if (isReversedPolarity(inv)) {
                            plugin.getTrackerKeeper().setTrackImmortalityGate(playerNameStr);
                            PlayerDisguise playerDisguise = new PlayerDisguise(playerNameStr);
                            for (Player p : plugin.getServer().getOnlinePlayers()) {
                                if (!p.getName().equals(playerNameStr)) {
                                    DisguiseAPI.disguiseToAll(p, playerDisguise);
                                }
                            }
                            plugin.getServer().broadcastMessage(plugin.getPluginName() + "The Master has cloned his genetic template to all players. Behold the Master Race!");
                            // schedule a delayed task to remove the disguise
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    for (Player p : plugin.getServer().getOnlinePlayers()) {
                                        if (DisguiseAPI.isDisguised(p)) {
                                            DisguiseAPI.undisguiseToAll(p);
                                        }
                                    }
                                    plugin.getServer().broadcastMessage(plugin.getPluginName() + "Lord Rassilon has reset the Master Race back to human form.");
                                    plugin.getTrackerKeeper().setTrackImmortalityGate("");
                                }
                            }, 3600L);
                        } else {
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
                                    switch (dt) {
                                        case SHEEP:
                                            SheepWatcher sw = (SheepWatcher) mobDisguise.getWatcher();
                                            sw.setColor(getColor(inv));
                                            sw.setBaby(getBaby(inv));
                                            if (getBoolean(inv)) {
                                                sw.setCustomName("jeb_");
                                                sw.setCustomNameVisible(true);
                                            }
                                            break;
                                        case HORSE:
                                            HorseWatcher hw = (HorseWatcher) mobDisguise.getWatcher();
                                            hw.setColor(getHorseColor(inv));
                                            hw.setBaby(getBaby(inv));
                                            break;
                                        case OCELOT:
                                            OcelotWatcher ow = (OcelotWatcher) mobDisguise.getWatcher();
                                            ow.setType(getCatType(inv));
                                            ow.setBaby(getBaby(inv));
                                            break;
                                        case PIG:
                                            PigWatcher pw = (PigWatcher) mobDisguise.getWatcher();
                                            pw.setSaddled(getBoolean(inv));
                                            pw.setBaby(getBaby(inv));
                                            break;
                                        case VILLAGER:
                                            VillagerWatcher vw = (VillagerWatcher) mobDisguise.getWatcher();
                                            vw.setProfession(getProfession(inv));
                                            vw.setBaby(getBaby(inv));
                                            break;
                                        case WOLF:
                                            WolfWatcher ww = (WolfWatcher) mobDisguise.getWatcher();
                                            if (getBoolean(inv)) {
                                                ww.setTamed(true);
                                                ww.setCollarColor(getColor(inv));
                                            }
                                            ww.setBaby(getBaby(inv));
                                            break;
                                        case SLIME:
                                        case MAGMA_CUBE:
                                            SlimeWatcher lw = (SlimeWatcher) mobDisguise.getWatcher();
                                            lw.setSize(getSlimeSize(inv));
                                            break;
                                        case BAT:
                                            BatWatcher bw = (BatWatcher) mobDisguise.getWatcher();
                                            bw.setFlying(getBoolean(inv));
                                            break;
                                        case BLAZE:
                                            BlazeWatcher bbw = (BlazeWatcher) mobDisguise.getWatcher();
                                            bbw.setBlazing(getBoolean(inv));
                                            break;
                                        case CREEPER:
                                            CreeperWatcher cw = (CreeperWatcher) mobDisguise.getWatcher();
                                            cw.setPowered(getBoolean(inv));
                                            break;
                                        case ENDERMAN:
                                            EndermanWatcher ew = (EndermanWatcher) mobDisguise.getWatcher();
                                            ew.setAgressive(getBoolean(inv));
                                            break;
                                        case COW:
                                        case DONKEY:
                                        case MULE:
                                        case SKELETON_HORSE:
                                        case UNDEAD_HORSE:
                                            AgeableWatcher aw = (AgeableWatcher) mobDisguise.getWatcher();
                                            aw.setBaby(getBaby(inv));
                                            break;
                                        case ZOMBIE:
                                        case ZOMBIE_VILLAGER:
                                            ZombieWatcher zw = (ZombieWatcher) mobDisguise.getWatcher();
                                            zw.setBaby(getBaby(inv));
                                            break;
                                        default:
                                            break;
                                    }
                                    DisguiseAPI.disguiseToAll(player, mobDisguise);
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

    private void setSlotFourtyOne(Inventory i, String d, String p) {
        String t = null;
        int o;
        if (d.equals("SHEEP") || d.equals("WOLF")) {
            if (sheep.containsKey(p)) {
                o = (sheep.get(p) + 1 < Integer.valueOf(16)) ? sheep.get(p) + 1 : 0;
            } else {
                o = 0;
            }
            t = DyeColor.values()[o].toString();
            sheep.put(p, o);
        }
        if (d.equals("HORSE")) {
            if (horses.containsKey(p)) {
                o = (horses.get(p) + 1 < Integer.valueOf(7)) ? horses.get(p) + 1 : 0;
            } else {
                o = 0;
            }
            t = Color.values()[o].toString();
            horses.put(p, o);
        }
        if (d.equals("OCELOT")) {
            if (cats.containsKey(p)) {
                o = (cats.get(p) + 1 < Integer.valueOf(4)) ? cats.get(p) + 1 : 0;
            } else {
                o = 0;
            }
            t = Type.values()[o].toString();
            cats.put(p, o);
        }
        if (d.equals("VILLAGER")) {
            if (professions.containsKey(p)) {
                o = (professions.get(p) + 1 < Integer.valueOf(5)) ? professions.get(p) + 1 : 0;
            } else {
                o = 0;
            }
            t = Profession.values()[o].toString();
            professions.put(p, o);
        }
        if (d.equals("SLIME") || d.equals("MAGMA_CUBE")) {
            if (slimes.containsKey(p)) {
                o = (slimes.get(p) + 1 < Integer.valueOf(3)) ? slimes.get(p) + 1 : 0;
            } else {
                o = 0;
            }
            t = slimeSizes.get(o).toString();
            slimes.put(p, o);
        }
        if (t != null) {
            ItemStack is = i.getItem(41);
            ItemMeta im = is.getItemMeta();
            im.setLore(Arrays.asList(new String[]{t}));
            is.setItemMeta(im);
        }
    }

    private boolean isReversedPolarity(Inventory i) {
        ItemStack is = i.getItem(37);
        ItemMeta im = is.getItemMeta();
        return im.getLore().get(0).equals("ON");
    }

    private AnimalColor getColor(Inventory i) {
        ItemStack is = i.getItem(41);
        ItemMeta im = is.getItemMeta();
        try {
            return AnimalColor.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return AnimalColor.WHITE;
        }
    }

    private Color getHorseColor(Inventory i) {
        ItemStack is = i.getItem(41);
        ItemMeta im = is.getItemMeta();
        try {
            return Color.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return Color.WHITE;
        }
    }

    private Type getCatType(Inventory i) {
        ItemStack is = i.getItem(41);
        ItemMeta im = is.getItemMeta();
        try {
            return Type.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return Type.WILD_OCELOT;
        }
    }

    private Profession getProfession(Inventory i) {
        ItemStack is = i.getItem(41);
        ItemMeta im = is.getItemMeta();
        try {
            return Profession.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return Profession.FARMER;
        }
    }

    private int getSlimeSize(Inventory i) {
        ItemStack is = i.getItem(41);
        ItemMeta im = is.getItemMeta();
        int size = plugin.getUtils().parseInt(im.getLore().get(0));
        return (size == 0) ? 2 : size;
    }

    private boolean getBaby(Inventory i) {
        ItemStack is = i.getItem(39);
        ItemMeta im = is.getItemMeta();
        return im.getLore().get(0).equals("BABY");
    }

    private boolean getBoolean(Inventory i) {
        ItemStack is = i.getItem(43);
        ItemMeta im = is.getItemMeta();
        return im.getLore().get(0).equals("TRUE");
    }
}
