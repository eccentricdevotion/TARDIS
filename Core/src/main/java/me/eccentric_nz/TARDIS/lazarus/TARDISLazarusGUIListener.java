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
package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorDisguiseEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorUndisguiseEvent;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lazarus.disguise.AGE;
import me.eccentric_nz.TARDIS.lazarus.disguise.FOX;
import me.eccentric_nz.TARDIS.lazarus.disguise.GENE;
import me.eccentric_nz.TARDIS.lazarus.disguise.MUSHROOM_COW;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISLazarusGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Boolean> snowmen = new HashMap<>();
    private final HashMap<UUID, Integer> axolotls = new HashMap<>();
    private final HashMap<UUID, Integer> cats = new HashMap<>();
    private final HashMap<UUID, Integer> foxes = new HashMap<>();
    private final HashMap<UUID, Integer> frogs = new HashMap<>();
    private final HashMap<UUID, Integer> genes = new HashMap<>();
    private final HashMap<UUID, Integer> horses = new HashMap<>();
    private final HashMap<UUID, Integer> llamas = new HashMap<>();
    private final HashMap<UUID, Integer> moos = new HashMap<>();
    private final HashMap<UUID, Integer> parrots = new HashMap<>();
    private final HashMap<UUID, Integer> professions = new HashMap<>();
    private final HashMap<UUID, Integer> puffers = new HashMap<>();
    private final HashMap<UUID, Integer> rabbits = new HashMap<>();
    private final HashMap<UUID, Integer> sheep = new HashMap<>();
    private final HashMap<UUID, Integer> slimes = new HashMap<>();
    private final HashMap<UUID, Integer> tropics = new HashMap<>();
    private final HashMap<UUID, Integer> variants = new HashMap<>();
    private final HashMap<UUID, Integer> wolves = new HashMap<>();
    private final HashMap<UUID, String> disguises = new HashMap<>();
    private final List<Integer> slimeSizes = List.of(1, 2, 4);
    private final List<Integer> pufferStates = List.of(0, 1, 2);
    private final List<String> twaMonsters = new ArrayList<>();
    private final List<String> twaOnly = List.of("DALEK", "DAVROS", "K9", "SATURNYNIAN", "TOCLAFANE");

    public TARDISLazarusGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        for (Monster m : Monster.values()) {
            twaMonsters.add(m.toString());
        }
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onLazarusClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Genetic Manipulator")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        Block b = plugin.getTrackerKeeper().getLazarus().get(uuid);
        if (b == null) {
            return;
        }
        int max_slot = 40;
        if (slot >= 0 && slot <= max_slot) {
            // get selection
            ItemStack is = view.getItem(slot);
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                // remember selection
                String display = im.getDisplayName();
                if (twaOnly.contains(display) && !plugin.getConfig().getBoolean("modules.weeping_angels")) {
                    im.setLore(List.of("Genetic modification not available!"));
                    is.setItemMeta(im);
                } else {
                    if (is.getType() == Material.PLAYER_HEAD) {
                        display = "PLAYER";
                    }
                    disguises.put(uuid, display);
                    setSlotFortyEight(view, display, uuid);
                }
            } else {
                disguises.put(uuid, "PLAYER");
            }
        } else {
            switch (slot) {
                case 42 -> {
                    LazarusUtils.pagers.add(uuid);
                    ItemStack pageButton = view.getItem(slot);
                    ItemMeta pageMeta = pageButton.getItemMeta();
                    // go to page one or two
                    Inventory inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Manipulator");
                    if (pageMeta.getDisplayName().equals(plugin.getLanguage().getString("BUTTON_PAGE_1"))) {
                        inv.setContents(new TARDISLazarusInventory(plugin).getPageOne());
                    } else {
                        inv.setContents(new TARDISLazarusPageTwoInventory(plugin).getPageTwo());
                    }
                    player.openInventory(inv);
                }
                case 43 -> {
                    LazarusUtils.pagers.add(uuid);
                    ItemStack skinsButton = view.getItem(slot);
                    ItemMeta skinsMeta = skinsButton.getItemMeta();
                    // go to skins or page two
                    Inventory inv;
                    if (skinsMeta.getDisplayName().equals(plugin.getLanguage().getString("BUTTON_PAGE_2"))) {
                        inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Manipulator");
                        inv.setContents(new TARDISLazarusPageTwoInventory(plugin).getPageTwo());
                    } else {
                        inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Skins");
                        inv.setContents(new TARDISTelevisionInventory(plugin).getSkins());
                    }
                    player.openInventory(inv);
                }
                case 44 -> {
                    LazarusUtils.pagers.add(uuid);
                    ItemStack monstersButton = view.getItem(slot);
                    ItemMeta monstersMeta = monstersButton.getItemMeta();
                    // go to monsters or page two
                    Inventory inv;
                    if (monstersMeta.getDisplayName().equals("TARDIS Television")) {
                        inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Skins");
                        inv.setContents(new TARDISTelevisionInventory(plugin).getSkins());
                    } else {
                        inv = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Genetic Manipulator");
                        inv.setContents(new TARDISWeepingAngelsMonstersInventory(plugin).getMonsters());
                    }
                    player.openInventory(inv);
                }
                case 45 -> { // The Master Switch : ON | OFF
                    ItemStack masterButton = view.getItem(slot);
                    ItemMeta masterMeta = masterButton.getItemMeta();
                    if (TARDISPermission.hasPermission(player, "tardis.themaster")) {
                        if (plugin.getTrackerKeeper().getImmortalityGate().isEmpty()) {
                            boolean isOff = masterMeta.getLore().getFirst().equals(plugin.getLanguage().getString("SET_OFF"));
                            String onoff = isOff ? plugin.getLanguage().getString("SET_ON", "ON") : plugin.getLanguage().getString("SET_OFF", "OFF");
                            masterMeta.setLore(List.of(onoff));
                            CustomModelDataComponent component = masterMeta.getCustomModelDataComponent();
                            component.setFloats(isOff ? List.of(252f) : List.of(152f));
                            masterMeta.setCustomModelDataComponent(component);
                        } else {
                            masterMeta.setLore(List.of("The Master Race is already", " set to " + plugin.getTrackerKeeper().getImmortalityGate() + "!", "Try again later."));
                        }
                    } else {
                        masterMeta.setLore(List.of("You do not have permission", "to be The Master!"));
                    }
                    masterButton.setItemMeta(masterMeta);
                }
                case 47 -> { // adult / baby
                    ItemStack ageButton = view.getItem(slot);
                    ItemMeta ageMeta = ageButton.getItemMeta();
                    String onoff = (ChatColor.stripColor(ageMeta.getLore().getFirst()).equals("ADULT")) ? "BABY" : "ADULT";
                    ageMeta.setLore(List.of(onoff));
                    ageButton.setItemMeta(ageMeta);
                }
                case 48 -> { // type / colour
                    if (disguises.containsKey(uuid)) {
                        setSlotFortyEight(view, disguises.get(uuid), uuid);
                    }
                }
                case 49 -> { // Tamed / Flying / Blazing / Powered / Beaming / Aggressive / Decorated / Chest carrying : TRUE | FALSE
                    ItemStack optionsButton = view.getItem(slot);
                    ItemMeta optionsMeta = optionsButton.getItemMeta();
                    List<String> lore = optionsMeta.getLore();
                    int pos = lore.size() - 1;
                    String truefalse = (ChatColor.stripColor(lore.get(pos)).equals("FALSE")) ? ChatColor.GREEN + "TRUE" : ChatColor.RED + "FALSE";
                    lore.set(pos, truefalse);
                    optionsMeta.setLore(lore);
                    optionsButton.setItemMeta(optionsMeta);
                }
                case 51 -> { // remove disguise
                    LazarusUtils.pagers.remove(uuid);
                    plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                    close(player);
                    // animate the manipulator walls
                    TARDISLazarusRunnable runnable = new TARDISLazarusRunnable(plugin, b);
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                    runnable.setTaskID(taskId);
                    TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                    // undisguise the player
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (twaMonsters.contains(disguises.get(uuid))) {
                            LazarusUtils.twaOff(player);
                        } else if (plugin.isDisguisesOnServer()) {
                            TARDISLazarusLibs.removeDisguise(player);
                        } else {
                            TARDISLazarusDisguise.removeDisguise(player);
                        }
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "GENETICS_RESTORED");
                        plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                    }, 80L);
                    // open the door
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        LazarusUtils.openDoor(b);
                        untrack(uuid, true);
                        plugin.getTrackerKeeper().getGeneticallyModified().remove(uuid);
                    }, 100L);
                }
                case 52 -> { // add disguise
                    LazarusUtils.pagers.remove(uuid);
                    plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                    close(player);
                    // animate the manipulator walls
                    TARDISLazarusRunnable runnable = new TARDISLazarusRunnable(plugin, b);
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                    runnable.setTaskID(taskId);
                    TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                    // disguise the player
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (plugin.isDisguisesOnServer()) {
                            TARDISLazarusLibs.removeDisguise(player);
                        } else {
                            TARDISLazarusDisguise.removeDisguise(player);
                        }
                        if (isReversedPolarity(view)) {
                            plugin.getTrackerKeeper().setImmortalityGate(player.getName());
                            if (plugin.isDisguisesOnServer()) {
                                TARDISLazarusLibs.runImmortalityGate(player);
                            } else {
                                TARDISLazarusDisguise.runImmortalityGate(player);
                            }
                            plugin.getMessenger().broadcast(TardisModule.TARDIS, "The Master (aka " + player.getName() + ") has cloned his genetic template to all players. Behold the Master Race!");
                            plugin.getPM().callEvent(new TARDISGeneticManipulatorDisguiseEvent(player, player.getName()));
                            // schedule a delayed task to remove the disguise
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                plugin.getServer().getOnlinePlayers().forEach((p) -> {
                                    if (plugin.isDisguisesOnServer()) {
                                        TARDISLazarusLibs.removeDisguise(p);
                                    } else {
                                        TARDISLazarusDisguise.removeDisguise(p);
                                    }
                                });
                                plugin.getMessenger().broadcast(TardisModule.TARDIS, "Lord Rassilon has reset the Master Race back to human form.");
                                plugin.getTrackerKeeper().setImmortalityGate("");
                                plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                            }, 3600L);
                        } else if (disguises.containsKey(uuid)) {
                            String disguise = disguises.get(uuid);
                            // undisguise first
                            LazarusUtils.twaOff(player);
                            if (twaMonsters.contains(disguise)) {
                                if (twaOnly.contains(disguise)) {
                                    plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise " + disguise + " on " + player.getUniqueId());
                                } else {
                                    // put on a skin
                                    Skin skin = Monster.valueOf(disguise).getSkin();
                                    plugin.getSkinChanger().set(player, skin);
                                    SkinUtils.setExtras(player, skin);
                                    SkinUtils.SKINNED.put(uuid, skin);
                                }
                            } else {
                                EntityType dt = EntityType.valueOf(disguise);
                                Object[] options = null;
                                switch (dt) {
                                    case AXOLOTL -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getAxolotlVariant(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getAxolotlVariant(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case FROG -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getFrogVariant(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getFrogVariant(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case CAT -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getCatType(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getCatType(view), getBoolean(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case PANDA -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getGene(view), false, getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{GENE.getFromPandaGene(getGene(view)), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case DONKEY, MULE -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, null, getBoolean(view), (!getBoolean(view) && getBaby(view))).createDisguise();
                                        } else {
                                            options = new Object[]{getBoolean(view), AGE.getFromBoolean(!getBoolean(view) && getBaby(view))};
                                        }
                                    }
                                    case PILLAGER, BAT, CREEPER, ENDERMAN, BLAZE -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, null, getBoolean(view), false).createDisguise();
                                        } else {
                                            options = new Object[]{getBoolean(view)};
                                        }
                                    }
                                    case SHEEP -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getColor(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getColor(view), getBoolean(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case WOLF -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getWolfVariant(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getWolfVariant(view), getBoolean(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case CHICKEN -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getChickenVariant(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getChickenVariant(view), getBoolean(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case COW -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getCowVariant(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getCowVariant(view), getBoolean(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case PIG -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getPigVariant(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getPigVariant(view), getBoolean(view), AGE.getFromBoolean(!getBoolean(view) && getBaby(view))};
                                        }
                                    }
                                    case HORSE -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getHorseColor(view), false, getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getHorseColor(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case LLAMA -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getLlamaColor(view), getBoolean(view), (!getBoolean(view) && getBaby(view))).createDisguise();
                                        } else {
                                            options = new Object[]{getLlamaColor(view), getBoolean(view), AGE.getFromBoolean(!getBoolean(view) && getBaby(view))};
                                        }
                                    }
                                    case OCELOT -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, null, getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getBoolean(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case PARROT -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getParrotVariant(view), false, getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getParrotVariant(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case RABBIT -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getRabbitType(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getRabbitType(view), getBoolean(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case VILLAGER, ZOMBIE_VILLAGER -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getProfession(view), false, getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getProfession(view), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case SLIME, MAGMA_CUBE -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getSlimeSize(view), false, false).createDisguise();
                                        } else {
                                            options = new Object[]{getSlimeSize(view)};
                                        }
                                    }
                                    case TURTLE, ZOMBIE, BEE -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, null, false, getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case SNOW_GOLEM -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, snowmen.get(uuid), false, false).createDisguise();
                                        } else {
                                            options = new Object[]{snowmen.get(uuid)};
                                        }
                                    }
                                    case PUFFERFISH -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, puffers.get(uuid), false, false).createDisguise();
                                        } else {
                                            options = new Object[]{puffers.get(uuid)};
                                        }
                                    }
                                    case TROPICAL_FISH -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, TropicalFish.Pattern.values()[tropics.get(uuid)], false, false).createDisguise();
                                        } else {
                                            options = new Object[]{TropicalFish.Pattern.values()[tropics.get(uuid)]};
                                        }
                                    }
                                    case MOOSHROOM -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getMushroomCowVariant(view), false, getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{MUSHROOM_COW.getFromMushroomCowType(getMushroomCowVariant(view)), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    case FOX -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getFoxType(view), false, getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{FOX.getFromFoxType(getFoxType(view)), AGE.getFromBoolean(getBaby(view))};
                                        }
                                    }
                                    default -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, null, false, false).createDisguise();
                                        }
                                    }
                                }
                                if (!plugin.isDisguisesOnServer()) {
                                    new TARDISLazarusDisguise(plugin, player, dt, options).createDisguise();
                                }
                            }
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "GENETICS_MODIFIED", disguise);
                            plugin.getPM().callEvent(new TARDISGeneticManipulatorDisguiseEvent(player, disguise));
                        }
                    }, 80L);
                    // open the door
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        LazarusUtils.openDoor(b);
                        untrack(uuid, false);
                        plugin.getTrackerKeeper().getGeneticallyModified().add(uuid);
                    }, 100L);
                }
                case 53 -> {
                    LazarusUtils.pagers.remove(uuid);
                    close(player);
                    LazarusUtils.openDoor(b);
                    untrack(uuid, false);
                }
                default -> { // do nothing
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLazarusClose(InventoryCloseEvent event) {
        String name = event.getView().getTitle();
        UUID uuid = event.getPlayer().getUniqueId();
        if ((name.equals(ChatColor.DARK_RED + "Genetic Manipulator") || name.equals(ChatColor.DARK_RED + "Genetic Skins")) && !plugin.getTrackerKeeper().getGeneticManipulation().contains(uuid)) {
            Block b = plugin.getTrackerKeeper().getLazarus().get(uuid);
            if (b != null && b.getRelative(BlockFace.SOUTH).getType().equals(Material.COBBLESTONE_WALL)) {
                b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
                b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
            }
            untrack(uuid, false);
            LazarusUtils.pagers.remove(uuid);
        }
    }

    private void untrack(UUID uuid, boolean remove) {
        if (!LazarusUtils.pagers.contains(uuid)) {
            // stop tracking player
            plugin.getTrackerKeeper().getLazarus().remove(uuid);
        }
        if (remove) {
            disguises.remove(uuid);
        }
        sheep.remove(uuid);
        horses.remove(uuid);
        cats.remove(uuid);
        professions.remove(uuid);
        slimes.remove(uuid);
        plugin.getTrackerKeeper().getGeneticManipulation().remove(uuid);
    }

    private void setSlotFortyEight(InventoryView i, String d, UUID uuid) {
        String t = null;
        int o;
        switch (d) {
            case "FROG" -> {
                if (frogs.containsKey(uuid)) {
                    o = (frogs.get(uuid) + 1 < 3) ? frogs.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = LazarusVariants.NAMES.get(o);
                frogs.put(uuid, o);
            }
            case "AXOLOTL" -> {
                if (axolotls.containsKey(uuid)) {
                    o = (axolotls.get(uuid) + 1 < 5) ? axolotls.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Axolotl.Variant.values()[o].toString();
                axolotls.put(uuid, o);
            }
            case "SNOWMAN" -> {
                boolean derp;
                if (snowmen.containsKey(uuid)) {
                    derp = !snowmen.get(uuid);
                } else {
                    derp = true;
                }
                snowmen.put(uuid, derp);
                t = (derp) ? "Pumpkin head" : "Derp face";
            }
            case "SHEEP" -> {
                if (sheep.containsKey(uuid)) {
                    o = (sheep.get(uuid) + 1 < 16) ? sheep.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = DyeColor.values()[o].toString();
                sheep.put(uuid, o);
            }
            case "WOLF" -> {
                if (wolves.containsKey(uuid)) {
                    o = (wolves.get(uuid) + 1 < 9) ? wolves.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = LazarusVariants.WOLF_NAMES.get(o);
                wolves.put(uuid, o);
            }
            case "CHICKEN", "COW", "PIG" -> {
                if (variants.containsKey(uuid)) {
                    o = (variants.get(uuid) + 1 < 3) ? variants.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = LazarusVariants.NAMES.get(o);
                variants.put(uuid, o);
            }
            case "HORSE" -> {
                if (horses.containsKey(uuid)) {
                    o = (horses.get(uuid) + 1 < 7) ? horses.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Horse.Color.values()[o].toString();
                horses.put(uuid, o);
            }
            case "LLAMA" -> {
                if (llamas.containsKey(uuid)) {
                    o = (llamas.get(uuid) + 1 < 4) ? llamas.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Llama.Color.values()[o].toString();
                llamas.put(uuid, o);
            }
            case "CAT" -> {
                if (cats.containsKey(uuid)) {
                    o = (cats.get(uuid) + 1 < 11) ? cats.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = LazarusVariants.CAT_NAMES.get(o);
                cats.put(uuid, o);
            }
            case "FOX" -> {
                if (foxes.containsKey(uuid)) {
                    o = (foxes.get(uuid) + 1 < 2) ? foxes.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Fox.Type.values()[o].toString();
                foxes.put(uuid, o);
            }
            case "RABBIT" -> {
                if (rabbits.containsKey(uuid)) {
                    o = (rabbits.get(uuid) + 1 < 7) ? rabbits.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Rabbit.Type.values()[o].toString();
                rabbits.put(uuid, o);
            }
            case "PARROT" -> {
                if (parrots.containsKey(uuid)) {
                    o = (parrots.get(uuid) + 1 < 5) ? parrots.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Parrot.Variant.values()[o].toString();
                parrots.put(uuid, o);
            }
            case "VILLAGER", "ZOMBIE_VILLAGER" -> {
                if (professions.containsKey(uuid)) {
                    o = (professions.get(uuid) + 1 < 6) ? professions.get(uuid) + 1 : 1;
                } else {
                    o = 1;
                }
                t = LazarusVariants.PROFESSION_NAMES.get(o);
                professions.put(uuid, o);
            }
            case "SLIME", "MAGMA_CUBE" -> {
                if (slimes.containsKey(uuid)) {
                    o = (slimes.get(uuid) + 1 < 3) ? slimes.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = slimeSizes.get(o).toString();
                slimes.put(uuid, o);
            }
            case "MUSHROOM_COW" -> {
                if (moos.containsKey(uuid)) {
                    o = (moos.get(uuid) + 1 < 2) ? moos.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = MushroomCow.Variant.values()[o].toString();
                moos.put(uuid, o);
            }
            case "PUFFERFISH" -> {
                if (puffers.containsKey(uuid)) {
                    o = (puffers.get(uuid) + 1 < 3) ? puffers.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = pufferStates.get(o).toString();
                puffers.put(uuid, o);
            }
            case "TROPICAL_FISH" -> {
                if (tropics.containsKey(uuid)) {
                    o = (tropics.get(uuid) + 1 < 12) ? tropics.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = TropicalFish.Pattern.values()[o].toString();
                tropics.put(uuid, o);
            }
            case "PANDA" -> {
                if (genes.containsKey(uuid)) {
                    o = (genes.get(uuid) + 1 < 7) ? genes.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Panda.Gene.values()[o].toString();
                genes.put(uuid, o);
            }
            default -> {
            }
        }
        if (t != null) {
            ItemStack is = i.getItem(48);
            ItemMeta im = is.getItemMeta();
            im.setLore(List.of(t));
            is.setItemMeta(im);
        }
    }

    private boolean isReversedPolarity(InventoryView i) {
        ItemStack is = i.getItem(45);
        ItemMeta im = is.getItemMeta();
        return im.getLore().getFirst().equals(plugin.getLanguage().getString("SET_ON", "ON"));
    }

    private DyeColor getColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return DyeColor.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return DyeColor.WHITE;
        }
    }

    private Horse.Color getHorseColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Horse.Color.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return Horse.Color.WHITE;
        }
    }

    private MushroomCow.Variant getMushroomCowVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return MushroomCow.Variant.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return MushroomCow.Variant.RED;
        }
    }

    private Llama.Color getLlamaColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Llama.Color.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return org.bukkit.entity.Llama.Color.CREAMY;
        }
    }

    private Axolotl.Variant getAxolotlVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Axolotl.Variant.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return Axolotl.Variant.WILD;
        }
    }

    private Frog.Variant getFrogVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.FROG_VARIANTS.getOrDefault(im.getLore().getFirst(), Frog.Variant.TEMPERATE);
    }

    private Cat.Type getCatType(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.CAT_VARIANTS.getOrDefault(im.getLore().getFirst(), Cat.Type.TABBY);
    }

    private Fox.Type getFoxType(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Fox.Type.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return Fox.Type.RED;
        }
    }

    private Panda.Gene getGene(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Panda.Gene.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return Panda.Gene.NORMAL;
        }
    }

    private Parrot.Variant getParrotVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Parrot.Variant.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return Parrot.Variant.GRAY;
        }
    }

    private Rabbit.Type getRabbitType(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Rabbit.Type.valueOf(im.getLore().getFirst());
        } catch (IllegalArgumentException e) {
            return Rabbit.Type.BROWN;
        }
    }

    private Wolf.Variant getWolfVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.WOLF_VARIANTS.getOrDefault(im.getLore().getFirst(), Wolf.Variant.PALE);
    }

    private Chicken.Variant getChickenVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.CHICKEN_VARIANTS.getOrDefault(im.getLore().getFirst(), Chicken.Variant.TEMPERATE);
    }

    private Cow.Variant getCowVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.COW_VARIANTS.getOrDefault(im.getLore().getFirst(), Cow.Variant.TEMPERATE);
    }

    private Pig.Variant getPigVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.PIG_VARIANTS.getOrDefault(im.getLore().getFirst(), Pig.Variant.TEMPERATE);
    }

    private Villager.Profession getProfession(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.PROFESSIONS.getOrDefault(im.getLore().getFirst(), Villager.Profession.FARMER);
    }

    private int getSlimeSize(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        int size = TARDISNumberParsers.parseInt(im.getLore().getFirst());
        return (size == 0) ? 2 : size;
    }

    private boolean getBaby(InventoryView i) {
        ItemStack is = i.getItem(47);
        ItemMeta im = is.getItemMeta();
        return im.getLore().getFirst().equals("BABY");
    }

    private boolean getBoolean(InventoryView i) {
        ItemStack is = i.getItem(49);
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();
        int pos = lore.size() - 1;
        return ChatColor.stripColor(lore.get(pos)).equals("TRUE");
    }
}
