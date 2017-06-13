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
package me.eccentric_nz.TARDIS.lazarus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorDisguiseEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorUndisguiseEvent;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import me.libraryaddict.disguise.disguisetypes.RabbitType;
import me.libraryaddict.disguise.disguisetypes.watchers.AgeableWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.BatWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.BlazeWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ChestedHorseWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.CreeperWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.EndermanWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.LlamaWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.OcelotWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ParrotWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PigWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.RabbitWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SheepWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SnowmanWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.VillagerWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ZombieVillagerWatcher;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
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
public class TARDISLazarusGUIListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<UUID, String> disguises = new HashMap<>();
    private final HashMap<UUID, Integer> horses = new HashMap<>();
    private final HashMap<UUID, Integer> llamas = new HashMap<>();
    private final HashMap<UUID, Integer> sheep = new HashMap<>();
    private final HashMap<UUID, Integer> cats = new HashMap<>();
    private final HashMap<UUID, Integer> rabbits = new HashMap<>();
    private final HashMap<UUID, Integer> parrots = new HashMap<>();
    private final HashMap<UUID, Integer> professions = new HashMap<>();
    private final HashMap<UUID, Integer> slimes = new HashMap<>();
    private final HashMap<UUID, Boolean> snowmen = new HashMap<>();
    private final List<Integer> slimeSizes = Arrays.asList(1, 2, 4);
    private final List<String> twaMonsters = Arrays.asList("WEEPING ANGEL", "CYBERMAN", "ICE WARRIOR", "EMPTY CHILD", "SILURIAN", "SONTARAN", "STRAX", "VASHTA NERADA", "ZYGON");
    private final List<String> twaChests = Arrays.asList("Weeping Angel Chest", "Cyberman Chest", "Ice Warrior Chest", "Empty Child Chest", "Silurian Chest", "Sontaran Chest", "Strax Chest", "Vashta Nerada Chest", "Zygon Chest");
    private int max_slot = 36;

    public TARDISLazarusGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a
     * TARDIS GUI, then the click is processed accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onLazarusClick(InventoryClickEvent event) {
        final Inventory inv = event.getInventory();
        String name = inv.getTitle();
        if (name.equals("§4Genetic Manipulator")) {
            event.setCancelled(true);
            if (plugin.checkTWA()) {
                max_slot = 43;
            }
            int slot = event.getRawSlot();
            final Player player = (Player) event.getWhoClicked();
            final UUID uuid = player.getUniqueId();
            final Block b = plugin.getTrackerKeeper().getLazarus().get(uuid);
            if (b == null) {
                return;
            }
            if (slot >= 0 && slot < max_slot) {
                // get selection
                ItemStack is = inv.getItem(slot);
                if (is != null) {
                    ItemMeta im = is.getItemMeta();
                    // remember selection
                    String display = im.getDisplayName();
                    if (twaMonsters.contains(display) && !plugin.checkTWA()) {
                        im.setLore(Arrays.asList("Genetic modification not available!"));
                        is.setItemMeta(im);
                    } else {
                        disguises.put(uuid, display);
                        setSlotFourtyEight(inv, display, uuid);
                    }
                } else {
                    disguises.put(uuid, "PLAYER");
                }
            }
            if (slot == 45) { // The Master Switch : ON | OFF
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                if (player.hasPermission("tardis.themaster")) {
                    if (plugin.getTrackerKeeper().getImmortalityGate().equals("")) {
                        String onoff = (im.getLore().get(0).equals(plugin.getLanguage().getString("SET_OFF"))) ? plugin.getLanguage().getString("SET_ON") : plugin.getLanguage().getString("SET_OFF");
                        im.setLore(Arrays.asList(onoff));
                    } else {
                        im.setLore(Arrays.asList("The Master Race is already", " set to " + plugin.getTrackerKeeper().getImmortalityGate() + "!", "Try again later."));
                    }
                } else {
                    im.setLore(Arrays.asList("You do not have permission", "to be The Master!"));
                }
                is.setItemMeta(im);
            }
            if (slot == 47) { // adult / baby
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                String onoff = (im.getLore().get(0).equals("ADULT")) ? "BABY" : "ADULT";
                im.setLore(Arrays.asList(onoff));
                is.setItemMeta(im);
            }
            if (slot == 48) { // type / colour
                if (disguises.containsKey(uuid)) {
                    setSlotFourtyEight(inv, disguises.get(uuid), uuid);
                }
            }
            if (slot == 49) { // Tamed / Flying / Blazing / Powered / Beaming / Aggressive / Decorated / Chest carrying : TRUE | FALSE
                ItemStack is = inv.getItem(slot);
                ItemMeta im = is.getItemMeta();
                List<String> lore = im.getLore();
                int pos = lore.size() - 1;
                String truefalse = (ChatColor.stripColor(lore.get(pos)).equals("FALSE")) ? ChatColor.GREEN + "TRUE" : ChatColor.RED + "FALSE";
                lore.set(pos, truefalse);
                im.setLore(lore);
                is.setItemMeta(im);
            }
            if (slot == 51) { //remove disguise
                plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                close(player);
                // animate the manipulator walls
                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISLazarusRunnable(plugin, b), 6L, 6L);
                TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                // undisguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (DisguiseAPI.isDisguised(player)) {
                        DisguiseAPI.undisguiseToAll(player);
                    } else {
                        twaOff(player);
                    }
                    TARDISMessage.send(player, "GENETICS_RESTORED");
                    plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                }, 80L);
                // open the door
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    openDoor(b);
                    untrack(uuid);
                    plugin.getTrackerKeeper().getGeneticallyModified().remove(uuid);
                }, 100L);
            }
            if (slot == 52) { // add disguise
                plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                close(player);
                // animate the manipulator walls
                plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TARDISLazarusRunnable(plugin, b), 6L, 6L);
                TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                // disguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (DisguiseAPI.isDisguised(player)) {
                        DisguiseAPI.undisguiseToAll(player);
                    }
                    if (isReversedPolarity(inv)) {
                        plugin.getTrackerKeeper().setImmortalityGate(player.getName());
                        PlayerDisguise playerDisguise = new PlayerDisguise(player.getName());
                        plugin.getServer().getOnlinePlayers().forEach((p) -> {
                            if (!p.getUniqueId().equals(uuid)) {
                                DisguiseAPI.disguiseToAll(p, playerDisguise);
                            }
                        });
                        plugin.getServer().broadcastMessage(plugin.getPluginName() + "The Master has cloned his genetic template to all players. Behold the Master Race!");
                        plugin.getPM().callEvent(new TARDISGeneticManipulatorDisguiseEvent(player, player.getName()));
                        // schedule a delayed task to remove the disguise
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            plugin.getServer().getOnlinePlayers().forEach((p) -> {
                                if (DisguiseAPI.isDisguised(p)) {
                                    DisguiseAPI.undisguiseToAll(p);
                                }
                            });
                            plugin.getServer().broadcastMessage(plugin.getPluginName() + "Lord Rassilon has reset the Master Race back to human form.");
                            plugin.getTrackerKeeper().setImmortalityGate("");
                            plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                        }, 3600L);
                    } else if (disguises.containsKey(uuid)) {
                        String disguise = disguises.get(uuid);
                        // undisguise first
                        twaOff(player);
                        if (twaMonsters.contains(disguise)) {
                            if (disguise.equals("WEEPING ANGEL")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad ANGEL on " + player.getUniqueId());
                            }
                            if (disguise.equals("CYBERMAN")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad CYBERMAN on " + player.getUniqueId());
                            }
                            if (disguise.equals("ICE WARRIOR")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad ICE on " + player.getUniqueId());
                            }
                            if (disguise.equals("EMPTY CHILD")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad EMPTY on " + player.getUniqueId());
                            }
                            if (disguise.equals("SILURIAN")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad SILURIAN on " + player.getUniqueId());
                            }
                            if (disguise.equals("SONTARAN")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad SONTARAN on " + player.getUniqueId());
                            }
                            if (disguise.equals("STRAX")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad STRAX on " + player.getUniqueId());
                            }
                            if (disguise.equals("VASHTA NERADA")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad VASHTA on " + player.getUniqueId());
                            }
                            if (disguise.equals("ZYGON")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad ZYGON on " + player.getUniqueId());
                            }
                        } else {
                            DisguiseType dt = DisguiseType.valueOf(disguise);
                            if (dt.equals(DisguiseType.PLAYER)) {
                                PlayerDisguise playerDisguise = new PlayerDisguise("Herobrine");
                                DisguiseAPI.disguiseToAll(player, playerDisguise);
                            } else {
                                MobDisguise mobDisguise = new MobDisguise(dt);
                                LivingWatcher livingWatcher;
                                try {
                                    livingWatcher = mobDisguise.getWatcher();
                                } catch (NoSuchMethodError e) {
                                    TARDISMessage.message(player, "LIBS");
                                    return;
                                }
                                switch (dt) {
                                    case DONKEY:
                                    case MULE:
                                        ChestedHorseWatcher hcw = (ChestedHorseWatcher) livingWatcher;
                                        hcw.setCarryingChest(getBoolean(inv));
                                        break;
                                    case SHEEP:
                                        SheepWatcher sw = (SheepWatcher) livingWatcher;
                                        sw.setColor(getColor(inv));
                                        sw.setBaby(getBaby(inv));
                                        if (getBoolean(inv)) {
                                            sw.setCustomName("jeb_");
                                            sw.setCustomNameVisible(true);
                                        }
                                        break;
                                    case HORSE:
                                        HorseWatcher hw = (HorseWatcher) livingWatcher;
                                        hw.setColor(getHorseColor(inv));
                                        hw.setBaby(getBaby(inv));
                                        break;
                                    case LLAMA:
                                        LlamaWatcher llw = (LlamaWatcher) livingWatcher;
                                        llw.setColor(getLlamaColor(inv));
                                        if (getBoolean(inv)) {
                                            Random rand = new Random();
                                            llw.setCarpet(AnimalColor.values()[rand.nextInt(16)]);
                                        }
                                        break;
                                    case OCELOT:
                                        OcelotWatcher ow = (OcelotWatcher) livingWatcher;
                                        ow.setType(getCatType(inv));
                                        ow.setBaby(getBaby(inv));
                                        break;
                                    case PARROT:
                                        ParrotWatcher tw = (ParrotWatcher) livingWatcher;
                                        tw.setVariant(getParrotVariant(inv));
                                        tw.setBaby(getBaby(inv));
                                        break;
                                    case PIG:
                                        PigWatcher pw = (PigWatcher) livingWatcher;
                                        pw.setSaddled(getBoolean(inv));
                                        pw.setBaby(getBaby(inv));
                                        break;
                                    case RABBIT:
                                        RabbitWatcher rw = (RabbitWatcher) livingWatcher;
                                        rw.setType(getRabbitType(inv));
                                        rw.setBaby(getBaby(inv));
                                        break;
                                    case VILLAGER:
                                        VillagerWatcher vw = (VillagerWatcher) livingWatcher;
                                        vw.setProfession(getProfession(inv));
                                        vw.setBaby(getBaby(inv));
                                        break;
                                    case WOLF:
                                        WolfWatcher ww = (WolfWatcher) livingWatcher;
                                        if (getBoolean(inv)) {
                                            ww.setTamed(true);
                                            ww.setCollarColor(getColor(inv));
                                        }
                                        ww.setBaby(getBaby(inv));
                                        break;
                                    case SLIME:
                                    case MAGMA_CUBE:
                                        SlimeWatcher slw = (SlimeWatcher) livingWatcher;
                                        slw.setSize(getSlimeSize(inv));
                                        break;
                                    case BAT:
                                        BatWatcher bw = (BatWatcher) livingWatcher;
                                        bw.setHanging(!getBoolean(inv));
                                        break;
                                    case BLAZE:
                                        BlazeWatcher bbw = (BlazeWatcher) livingWatcher;
                                        bbw.setBlazing(getBoolean(inv));
                                        break;
                                    case CREEPER:
                                        CreeperWatcher cw = (CreeperWatcher) livingWatcher;
                                        cw.setPowered(getBoolean(inv));
                                        break;
                                    case ENDERMAN:
                                        EndermanWatcher ew = (EndermanWatcher) livingWatcher;
                                        ew.setAggressive(getBoolean(inv));
                                        break;
                                    case COW:
                                    case SKELETON_HORSE:
                                    case ZOMBIE_HORSE:
                                    case ZOMBIE:
                                        AgeableWatcher aw = (AgeableWatcher) livingWatcher;
                                        aw.setBaby(getBaby(inv));
                                        break;
                                    case ZOMBIE_VILLAGER:
                                        ZombieVillagerWatcher zvw = (ZombieVillagerWatcher) livingWatcher;
                                        zvw.setBaby(getBaby(inv));
                                        zvw.setProfession(getZombieProfession(inv));
                                        break;
                                    case SNOWMAN:
                                        SnowmanWatcher snw = (SnowmanWatcher) livingWatcher;
                                        snw.setDerp(!snowmen.get(uuid));
                                        break;
                                    default:
                                        break;
                                }
                                DisguiseAPI.disguiseToAll(player, mobDisguise);
                            }
                        }
                        TARDISMessage.send(player, "GENETICS_MODIFIED", disguise);
                        plugin.getPM().callEvent(new TARDISGeneticManipulatorDisguiseEvent(player, disguise));
                    }
                }, 80L);
                // open the door
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    openDoor(b);
                    untrack(uuid);
                    plugin.getTrackerKeeper().getGeneticallyModified().add(uuid);
                }, 100L);
            }
            if (slot == 53) {
                close(player);
                openDoor(b);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLazarusClose(InventoryCloseEvent event) {
        String name = event.getInventory().getTitle();
        UUID uuid = event.getPlayer().getUniqueId();
        if (name.equals("§4Genetic Manipulator") && !plugin.getTrackerKeeper().getGeneticManipulation().contains(uuid)) {
            Block b = plugin.getTrackerKeeper().getLazarus().get(event.getPlayer().getUniqueId());
            if (b.getRelative(BlockFace.SOUTH).getType().equals(Material.COBBLE_WALL)) {
                b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
                b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
            }
            untrack(uuid);
        }
    }

    private void untrack(UUID uuid) {
        // stop tracking player
        if (plugin.getTrackerKeeper().getLazarus().containsKey(uuid)) {
            plugin.getTrackerKeeper().getLazarus().remove(uuid);
        }
        if (disguises.containsKey(uuid)) {
            disguises.remove(uuid);
        }
        if (sheep.containsKey(uuid)) {
            sheep.remove(uuid);
        }
        if (horses.containsKey(uuid)) {
            horses.remove(uuid);
        }
        if (cats.containsKey(uuid)) {
            cats.remove(uuid);
        }
        if (professions.containsKey(uuid)) {
            professions.remove(uuid);
        }
        if (slimes.containsKey(uuid)) {
            slimes.remove(uuid);
        }
        if (plugin.getTrackerKeeper().getGeneticManipulation().contains(uuid)) {
            plugin.getTrackerKeeper().getGeneticManipulation().remove(uuid);
        }
    }

    private void openDoor(Block b) {
        b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
        b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
    }

    private void setSlotFourtyEight(Inventory i, String d, UUID uuid) {
        String t = null;
        int o;
        switch (d) {
            case "SNOWMAN":
                boolean derp;
                if (snowmen.containsKey(uuid)) {
                    derp = !snowmen.get(uuid);
                } else {
                    derp = true;
                }
                snowmen.put(uuid, derp);
                t = (derp) ? "Pumpkin head" : "Derp face";
                break;
            case "SHEEP":
            case "WOLF":
            case "SHULKER":
                if (sheep.containsKey(uuid)) {
                    o = (sheep.get(uuid) + 1 < 16) ? sheep.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = DyeColor.values()[o].toString();
                sheep.put(uuid, o);
                break;
            case "HORSE":
                if (horses.containsKey(uuid)) {
                    o = (horses.get(uuid) + 1 < 7) ? horses.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = org.bukkit.entity.Horse.Color.values()[o].toString();
                horses.put(uuid, o);
                break;
            case "LLAMA":
                if (llamas.containsKey(uuid)) {
                    o = (llamas.get(uuid) + 1 < 4) ? llamas.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = org.bukkit.entity.Llama.Color.values()[o].toString();
                llamas.put(uuid, o);
                break;
            case "OCELOT":
                if (cats.containsKey(uuid)) {
                    o = (cats.get(uuid) + 1 < 4) ? cats.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Type.values()[o].toString();
                cats.put(uuid, o);
                break;
            case "RABBIT":
                if (rabbits.containsKey(uuid)) {
                    o = (rabbits.get(uuid) + 1 < 7) ? rabbits.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = RabbitType.values()[o].toString();
                rabbits.put(uuid, o);
                break;
            case "PARROT":
                if (parrots.containsKey(uuid)) {
                    o = (parrots.get(uuid) + 1 < 5) ? parrots.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Parrot.Variant.values()[o].toString();
                parrots.put(uuid, o);
                break;
            case "VILLAGER":
            case "ZOMBIE_VILLAGER":
                if (professions.containsKey(uuid)) {
                    o = (professions.get(uuid) + 1 < 6) ? professions.get(uuid) + 1 : 1;
                } else {
                    o = 1;
                }
                t = Profession.values()[o].toString();
                professions.put(uuid, o);
                break;
            case "SLIME":
            case "MAGMA_CUBE":
                if (slimes.containsKey(uuid)) {
                    o = (slimes.get(uuid) + 1 < 3) ? slimes.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = slimeSizes.get(o).toString();
                slimes.put(uuid, o);
                break;
            default:
                break;
        }
        if (t != null) {
            ItemStack is = i.getItem(48);
            ItemMeta im = is.getItemMeta();
            im.setLore(Arrays.asList(t));
            is.setItemMeta(im);
        }
    }

    private boolean isReversedPolarity(Inventory i) {
        ItemStack is = i.getItem(45);
        ItemMeta im = is.getItemMeta();
        return im.getLore().get(0).equals(plugin.getLanguage().getString("SET_ON"));
    }

    private AnimalColor getColor(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return AnimalColor.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return AnimalColor.WHITE;
        }
    }

    private org.bukkit.entity.Horse.Color getHorseColor(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return org.bukkit.entity.Horse.Color.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return org.bukkit.entity.Horse.Color.WHITE;
        }
    }

    private Llama.Color getLlamaColor(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Llama.Color.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return org.bukkit.entity.Llama.Color.CREAMY;
        }
    }

    private Type getCatType(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Type.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return Type.WILD_OCELOT;
        }
    }

    private Parrot.Variant getParrotVariant(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Parrot.Variant.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return Parrot.Variant.GRAY;
        }
    }

    private RabbitType getRabbitType(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return RabbitType.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return RabbitType.BROWN;
        }
    }

    private Profession getProfession(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Profession.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return Profession.FARMER;
        }
    }

    private Profession getZombieProfession(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Profession.valueOf(im.getLore().get(0));
        } catch (IllegalArgumentException e) {
            return Profession.NORMAL;
        }
    }

    private int getSlimeSize(Inventory i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        int size = TARDISNumberParsers.parseInt(im.getLore().get(0));
        return (size == 0) ? 2 : size;
    }

    private boolean getBaby(Inventory i) {
        ItemStack is = i.getItem(47);
        ItemMeta im = is.getItemMeta();
        return im.getLore().get(0).equals("BABY");
    }

    private boolean getBoolean(Inventory i) {
        ItemStack is = i.getItem(49);
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();
        int pos = lore.size() - 1;
        return ChatColor.stripColor(lore.get(pos)).equals("TRUE");
    }

    private void twaOff(Player player) {
        ItemStack chest = player.getInventory().getChestplate();
        if (chest != null && chest.hasItemMeta() && chest.getItemMeta().hasDisplayName()) {
            String metaName = chest.getItemMeta().getDisplayName();
            if (twaChests.contains(metaName)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "twad ANGEL off " + player.getUniqueId());
            }
        }
    }
}
