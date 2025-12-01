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

import io.papermc.paper.world.WeatheringCopperState;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorDisguiseEvent;
import me.eccentric_nz.TARDIS.api.event.TARDISGeneticManipulatorUndisguiseEvent;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.lazarus.disguise.AGE;
import me.eccentric_nz.TARDIS.lazarus.disguise.FOX;
import me.eccentric_nz.TARDIS.lazarus.disguise.GENE;
import me.eccentric_nz.TARDIS.lazarus.disguise.MUSHROOM_COW;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.skins.Skin;
import me.eccentric_nz.TARDIS.skins.SkinUtils;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
    private final HashMap<UUID, Integer> golems = new HashMap<>();
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
        InventoryHolder holder = event.getInventory().getHolder(false);
        if (!(holder instanceof TARDISLazarusPassiveInventory)
                && !(holder instanceof TARDISLazarusNeutralInventory)
                && !(holder instanceof TARDISLazarusHostileInventory)
                && !(holder instanceof TARDISLazarusAdjacentInventory)
                && !(holder instanceof TARDISLazarusMonstersInventory)) {
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
        InventoryView view = event.getView();
        if (slot >= 0 && slot <= ((LazarusGUI) holder).getMaxSlot()) {
            // get selection
            ItemStack is = view.getItem(slot);
            if (is != null) {
                ItemMeta im = is.getItemMeta();
                // remember selection
                String display = ComponentUtils.stripColour(im.displayName());
                if (twaOnly.contains(display) && !plugin.getConfig().getBoolean("modules.weeping_angels")) {
                    im.lore(List.of(Component.text("Genetic modification not available!")));
                    is.setItemMeta(im);
                } else {
                    if (is.getType() == Material.PLAYER_HEAD) {
                        display = "PLAYER";
                    }
                    plugin.getTrackerKeeper().getDisguises().put(uuid, display);
                    setSlotFortyEight(view, display, uuid);
                }
            } else {
                plugin.getTrackerKeeper().getDisguises().put(uuid, "PLAYER");
            }
        } else {
            InventoryHolder ih;
            switch (slot) {
                // previous / next
                case 36, 44 -> {
                    LazarusUtils.pagers.add(uuid);
                    ItemStack pageButton = view.getItem(slot);
                    ItemMeta pageMeta = pageButton.getItemMeta();
                    // check the lore
                    String which = ComponentUtils.stripColour(pageMeta.lore().getFirst());
                    plugin.debug(which);
                    switch (which) {
                        case "Passive Mobs" -> ih = new TARDISLazarusPassiveInventory(plugin);
                        case "Neutral Mobs" -> ih = new TARDISLazarusNeutralInventory(plugin);
                        case "Hostile Mobs" -> ih = new TARDISLazarusHostileInventory(plugin);
                        case "Hostile Adjacent Mobs" -> ih = new TARDISLazarusAdjacentInventory(plugin);
                        case "Doctors" -> ih = new TARDISLazarusDoctorInventory(plugin);
                        case "Companions" -> ih = new TARDISLazarusCompanionInventory(plugin);
                        case "Characters" -> ih = new TARDISLazarusCharacterInventory(plugin);
                        case "TARDIS Monsters" -> ih = new TARDISLazarusMonstersInventory(plugin);
                        default -> ih = new TARDISLazarusInventory(plugin);
                    }
                    player.openInventory(ih.getInventory());
                }
                // back
                case 40 -> {
                    LazarusUtils.pagers.add(uuid);
                    ih = new TARDISLazarusInventory(plugin);
                    player.openInventory(ih.getInventory());
                }
                case 47 -> { // adult / baby
                    ItemStack ageButton = view.getItem(slot);
                    ItemMeta ageMeta = ageButton.getItemMeta();
                    String onoff = (ComponentUtils.stripColour(ageMeta.lore().getFirst()).equals("ADULT")) ? "BABY" : "ADULT";
                    ageMeta.lore(List.of(Component.text(onoff)));
                    ageButton.setItemMeta(ageMeta);
                }
                case 48 -> { // type / colour
                    if (plugin.getTrackerKeeper().getDisguises().containsKey(uuid)) {
                        setSlotFortyEight(view, plugin.getTrackerKeeper().getDisguises().get(uuid), uuid);
                    }
                }
                case 49 -> { // Tamed / Flying / Blazing / Powered / Beaming / Aggressive / Decorated / Chest carrying : TRUE | FALSE
                    ItemStack optionsButton = view.getItem(slot);
                    ItemMeta optionsMeta = optionsButton.getItemMeta();
                    List<Component> lore = optionsMeta.lore();
                    int pos = lore.size() - 1;
                    Component truefalse = (ComponentUtils.stripColour(lore.get(pos)).equals("FALSE")) ? Component.text("TRUE", NamedTextColor.GREEN) : Component.text("FALSE", NamedTextColor.RED);
                    lore.set(pos, truefalse);
                    optionsMeta.lore(lore);
                    optionsButton.setItemMeta(optionsMeta);
                }
                case 51 -> { // remove disguise
                    LazarusUtils.pagers.remove(uuid);
                    plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                    close(player);
                    // if the Master Switch is ON turn it off and restore all players
                    if (!plugin.getTrackerKeeper().getImmortalityGate().isEmpty()) {
                        plugin.getServer().getOnlinePlayers().forEach((p) -> {
                            if (plugin.isDisguisesOnServer()) {
                                TARDISLazarusLibs.removeDisguise(p);
                            } else {
                                TARDISLazarusDisguise.removeDisguise(p);
                            }
                        });
                        plugin.getTrackerKeeper().setImmortalityGate("");
                        plugin.getPM().callEvent(new TARDISGeneticManipulatorUndisguiseEvent(player));
                    }
                    // animate the manipulator walls
                    TARDISLazarusRunnable runnable = new TARDISLazarusRunnable(plugin, b);
                    int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                    runnable.setTaskID(taskId);
                    TARDISSounds.playTARDISSound(player.getLocation(), "lazarus_machine");
                    // undisguise the player
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        if (twaMonsters.contains(plugin.getTrackerKeeper().getDisguises().get(uuid))) {
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
                        if (plugin.getTrackerKeeper().getDisguises().containsKey(uuid)) {
                            String disguise = plugin.getTrackerKeeper().getDisguises().get(uuid);
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
                                    case COPPER_GOLEM -> {
                                        if (plugin.isDisguisesOnServer()) {
                                            new TARDISLazarusLibs(player, disguise, getGolemState(view), getBoolean(view), getBaby(view)).createDisguise();
                                        } else {
                                            options = new Object[]{getGolemState(view), getBoolean(view), AGE.getFromBoolean(getBaby(view))};
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

    private void untrack(UUID uuid, boolean remove) {
        if (!LazarusUtils.pagers.contains(uuid)) {
            // stop tracking player
            plugin.getTrackerKeeper().getLazarus().remove(uuid);
        }
        if (remove) {
            plugin.getTrackerKeeper().getDisguises().remove(uuid);
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
            case "COPPER_GOLEM" -> {
                if (golems.containsKey(uuid)) {
                    o = (golems.get(uuid) + 1 < 4) ? golems.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = WeatheringCopperState.values()[o].toString();
                golems.put(uuid, o);
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
            im.lore(List.of(Component.text(t)));
            is.setItemMeta(im);
        }
    }

    private DyeColor getColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return DyeColor.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return DyeColor.WHITE;
        }
    }

    private Horse.Color getHorseColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Horse.Color.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return Horse.Color.WHITE;
        }
    }

    private MushroomCow.Variant getMushroomCowVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return MushroomCow.Variant.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return MushroomCow.Variant.RED;
        }
    }

    private Llama.Color getLlamaColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Llama.Color.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return org.bukkit.entity.Llama.Color.CREAMY;
        }
    }

    private Axolotl.Variant getAxolotlVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Axolotl.Variant.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return Axolotl.Variant.WILD;
        }
    }

    private Frog.Variant getFrogVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.FROG_VARIANTS.getOrDefault(ComponentUtils.stripColour(im.lore().getFirst()), Frog.Variant.TEMPERATE);
    }

    private Cat.Type getCatType(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.CAT_VARIANTS.getOrDefault(ComponentUtils.stripColour(im.lore().getFirst()), Cat.Type.TABBY);
    }

    private Fox.Type getFoxType(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Fox.Type.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return Fox.Type.RED;
        }
    }

    private Panda.Gene getGene(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Panda.Gene.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return Panda.Gene.NORMAL;
        }
    }

    private WeatheringCopperState getGolemState(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return WeatheringCopperState.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return WeatheringCopperState.UNAFFECTED;
        }
    }

    private Parrot.Variant getParrotVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Parrot.Variant.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return Parrot.Variant.GRAY;
        }
    }

    private Rabbit.Type getRabbitType(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        try {
            return Rabbit.Type.valueOf(ComponentUtils.stripColour(im.lore().getFirst()));
        } catch (IllegalArgumentException e) {
            return Rabbit.Type.BROWN;
        }
    }

    private Wolf.Variant getWolfVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.WOLF_VARIANTS.getOrDefault(ComponentUtils.stripColour(im.lore().getFirst()), Wolf.Variant.PALE);
    }

    private Chicken.Variant getChickenVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.CHICKEN_VARIANTS.getOrDefault(ComponentUtils.stripColour(im.lore().getFirst()), Chicken.Variant.TEMPERATE);
    }

    private Cow.Variant getCowVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.COW_VARIANTS.getOrDefault(ComponentUtils.stripColour(im.lore().getFirst()), Cow.Variant.TEMPERATE);
    }

    private Pig.Variant getPigVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.PIG_VARIANTS.getOrDefault(ComponentUtils.stripColour(im.lore().getFirst()), Pig.Variant.TEMPERATE);
    }

    private Villager.Profession getProfession(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        return LazarusVariants.PROFESSIONS.getOrDefault(ComponentUtils.stripColour(im.lore().getFirst()), Villager.Profession.FARMER);
    }

    private int getSlimeSize(InventoryView i) {
        ItemStack is = i.getItem(48);
        ItemMeta im = is.getItemMeta();
        int size = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(im.lore().getFirst()));
        return (size == 0) ? 2 : size;
    }

    private boolean getBaby(InventoryView i) {
        ItemStack is = i.getItem(47);
        ItemMeta im = is.getItemMeta();
        return ComponentUtils.stripColour(im.lore().getFirst()).equals("BABY");
    }

    private boolean getBoolean(InventoryView i) {
        ItemStack is = i.getItem(49);
        ItemMeta im = is.getItemMeta();
        List<Component> lore = im.lore();
        int pos = lore.size() - 1;
        return ComponentUtils.stripColour(lore.get(pos)).equals("TRUE");
    }
}
