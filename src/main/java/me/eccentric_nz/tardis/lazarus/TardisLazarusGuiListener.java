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
package me.eccentric_nz.tardis.lazarus;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.api.event.TardisGeneticManipulatorDisguiseEvent;
import me.eccentric_nz.tardis.api.event.TardisGeneticManipulatorUndisguiseEvent;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.disguise.Fox;
import me.eccentric_nz.tardis.disguise.*;
import me.eccentric_nz.tardis.listeners.TardisMenuListener;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import me.eccentric_nz.tardis.utility.TardisSounds;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.entity.Cat.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TardisLazarusGuiListener extends TardisMenuListener implements Listener {

    private final TardisPlugin plugin;
    private final HashMap<UUID, Boolean> snowmen = new HashMap<>();
    private final HashMap<UUID, Integer> axolotls = new HashMap<>();
    private final HashMap<UUID, Integer> cats = new HashMap<>();
    private final HashMap<UUID, Integer> foxes = new HashMap<>();
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
    private final HashMap<UUID, String> disguises = new HashMap<>();
    private final List<Integer> slimeSizes = Arrays.asList(1, 2, 4);
    private final List<Integer> pufferStates = Arrays.asList(0, 1, 2);
    private final List<String> twaMonsters = Arrays.asList("WEEPING ANGEL", "CYBERMAN", "DALEK", "EMPTY CHILD", "ICE WARRIOR", "JUDOON", "K9", "OOD", "SILENT", "SILURIAN", "SONTARAN", "STRAX", "TOCLAFANE", "VASHTA NERADA", "ZYGON");
    private final List<String> twaHelmets = Arrays.asList("Weeping Angel Head", "Cyberman Head", "Dalek Head", "Empty Child Head", "Ice Warrior Head", "Judoon Head", "K9 Head", "Ood Head", "Silent Head", "Silurian Head", "Sontaran Head", "Strax Head", "Toclafane", "Vashta Nerada Head", "Zygon Head");

    public TardisLazarusGuiListener(TardisPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
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
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "Genetic Manipulator")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();
            Block b = plugin.getTrackerKeeper().getLazarus().get(uuid);
            if (b == null) {
                return;
            }
            int max_slot = 45;
            if (slot >= 0 && slot < max_slot) {
                // get selection
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    ItemMeta im = is.getItemMeta();
                    // remember selection
                    assert im != null;
                    String display = im.getDisplayName();
                    if (twaMonsters.contains(display) && !plugin.checkTwa()) {
                        im.setLore(Collections.singletonList("Genetic modification not available!"));
                        is.setItemMeta(im);
                    } else {
                        if (display.equals("HEROBRINE")) {
                            display = "PLAYER";
                        }
                        disguises.put(uuid, display);
                        setSlotFourtyEight(view, display, uuid);
                    }
                } else {
                    disguises.put(uuid, "PLAYER");
                }
            }
            if (slot == 45) { // The Master Switch : ON | OFF
                ItemStack is = view.getItem(slot);
                assert is != null;
                ItemMeta im = is.getItemMeta();
                if (TardisPermission.hasPermission(player, "tardis.themaster")) {
                    assert im != null;
                    if (plugin.getTrackerKeeper().getImmortalityGate().equals("")) {
                        boolean isOff = Objects.requireNonNull(im.getLore()).get(0).equals(plugin.getLanguage().getString("SET_OFF"));
                        String onOff = isOff ? plugin.getLanguage().getString("SET_ON") : plugin.getLanguage().getString("SET_OFF");
                        im.setLore(Collections.singletonList(onOff));
                        int cmd = isOff ? 2 : 3;
                        im.setCustomModelData(cmd);
                    } else {
                        im.setLore(Arrays.asList("The Master Race is already", " set to " + plugin.getTrackerKeeper().getImmortalityGate() + "!", "Try again later."));
                    }
                } else {
                    assert im != null;
                    im.setLore(Arrays.asList("You do not have permission", "to be The Master!"));
                }
                is.setItemMeta(im);
            }
            if (slot == 47) { // adult / baby
                ItemStack is = view.getItem(slot);
                assert is != null;
                ItemMeta im = is.getItemMeta();
                assert im != null;
                String onOff = (Objects.requireNonNull(im.getLore()).get(0).equals("ADULT")) ? "BABY" : "ADULT";
                im.setLore(Collections.singletonList(onOff));
                is.setItemMeta(im);
            }
            if (slot == 48) { // type / colour
                if (disguises.containsKey(uuid)) {
                    setSlotFourtyEight(view, disguises.get(uuid), uuid);
                }
            }
            if (slot == 49) { // Tamed / Flying / Blazing / Powered / Beaming / Aggressive / Decorated / Chest carrying : TRUE | FALSE
                ItemStack is = view.getItem(slot);
                assert is != null;
                ItemMeta im = is.getItemMeta();
                assert im != null;
                List<String> lore = im.getLore();
                assert lore != null;
                int pos = lore.size() - 1;
                String trueFalse = (ChatColor.stripColor(lore.get(pos)).equals("FALSE")) ? ChatColor.GREEN + "TRUE" : ChatColor.RED + "FALSE";
                lore.set(pos, trueFalse);
                im.setLore(lore);
                is.setItemMeta(im);
            }
            if (slot == 51) { //remove disguise
                plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                close(player);
                // animate the manipulator walls
                TardisLazarusRunnable runnable = new TardisLazarusRunnable(plugin, b);
                int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                runnable.setTaskID(taskId);
                TardisSounds.playTardisSound(player.getLocation(), "lazarus_machine");
                // undisguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (twaMonsters.contains(disguises.get(uuid))) {
                        twaOff(player);
                    } else if (plugin.isDisguisesOnServer()) {
                        TardisLazarusLibs.removeDisguise(player);
                    } else {
                        TardisLazarusDisguise.removeDisguise(player);
                    }
                    TardisMessage.send(player, "GENETICS_RESTORED");
                    plugin.getPluginManager().callEvent(new TardisGeneticManipulatorUndisguiseEvent(player));
                }, 80L);
                // open the door
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    openDoor(b);
                    untrack(uuid, true);
                    plugin.getTrackerKeeper().getGeneticallyModified().remove(uuid);
                }, 100L);
            }
            if (slot == 52) { // add disguise
                plugin.getTrackerKeeper().getGeneticManipulation().add(uuid);
                close(player);
                // animate the manipulator walls
                TardisLazarusRunnable runnable = new TardisLazarusRunnable(plugin, b);
                int taskId = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 6L, 6L);
                runnable.setTaskID(taskId);
                TardisSounds.playTardisSound(player.getLocation(), "lazarus_machine");
                // disguise the player
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (plugin.isDisguisesOnServer()) {
                        TardisLazarusLibs.removeDisguise(player);
                    } else {
                        TardisLazarusDisguise.removeDisguise(player);
                    }
                    if (isReversedPolarity(view)) {
                        plugin.getTrackerKeeper().setImmortalityGate(player.getName());
                        if (plugin.isDisguisesOnServer()) {
                            TardisLazarusLibs.runImmortalityGate(player);
                        } else {
                            TardisLazarusDisguise.runImmortalityGate(player);
                        }
                        plugin.getServer().broadcastMessage(plugin.getPluginName() + "The Master (aka " + player.getName() + ") has cloned his genetic template to all players. Behold the Master Race!");
                        plugin.getPluginManager().callEvent(new TardisGeneticManipulatorDisguiseEvent(player, player.getName()));
                        // schedule a delayed task to remove the disguise
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            plugin.getServer().getOnlinePlayers().forEach((p) -> {
                                if (plugin.isDisguisesOnServer()) {
                                    TardisLazarusLibs.removeDisguise(p);
                                } else {
                                    TardisLazarusDisguise.removeDisguise(p);
                                }
                            });
                            plugin.getServer().broadcastMessage(plugin.getPluginName() + "Lord Rassilon has reset the Master Race back to human form.");
                            plugin.getTrackerKeeper().setImmortalityGate("");
                            plugin.getPluginManager().callEvent(new TardisGeneticManipulatorUndisguiseEvent(player));
                        }, 3600L);
                    } else if (disguises.containsKey(uuid)) {
                        String disguise = disguises.get(uuid);
                        // undisguise first
                        twaOff(player);
                        if (twaMonsters.contains(disguise)) {
                            if (disguise.equals("WEEPING ANGEL")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise WEEPING_ANGEL on " + player.getUniqueId());
                            }
                            if (disguise.equals("CYBERMAN")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise CYBERMAN on " + player.getUniqueId());
                            }
                            if (disguise.equals("DALEK")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise DALEK on " + player.getUniqueId());
                            }
                            if (disguise.equals("EMPTY CHILD")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise EMPTY_CHILD on " + player.getUniqueId());
                            }
                            if (disguise.equals("ICE WARRIOR")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise ICE_WARRIOR on " + player.getUniqueId());
                            }
                            if (disguise.equals("JUDOON")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise JUDOON on " + player.getUniqueId());
                            }
                            if (disguise.equals("K9")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise K9 on " + player.getUniqueId());
                            }
                            if (disguise.equals("OOD")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise OOD on " + player.getUniqueId());
                            }
                            if (disguise.equals("SILENT")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise SILENT on " + player.getUniqueId());
                            }
                            if (disguise.equals("SILURIAN")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise SILURIAN on " + player.getUniqueId());
                            }
                            if (disguise.equals("SONTARAN")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise SONTARAN on " + player.getUniqueId());
                            }
                            if (disguise.equals("STRAX")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise STRAX on " + player.getUniqueId());
                            }
                            if (disguise.equals("TOCLAFANE")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise TOCLAFANE on " + player.getUniqueId());
                            }
                            if (disguise.equals("VASHTA NERADA")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise VASHTA on " + player.getUniqueId());
                            }
                            if (disguise.equals("ZYGON")) {
                                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise ZYGON on " + player.getUniqueId());
                            }
                        } else {
                            EntityType dt = EntityType.valueOf(disguise);
                            Object[] options = null;
                            switch (dt) {
                                case AXOLOTL:
                                    if (!plugin.isDisguisesOnServer()) {
                                        options = new Object[]{getAxolotlVariant(view), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case CAT:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getCatType(view), getBoolean(view), getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{getCatType(view), getBoolean(view), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case PANDA:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getGene(view), false, getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{Gene.getFromPandaGene(getGene(view)), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case DONKEY:
                                case MULE:
                                case PIG:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, null, getBoolean(view), (!getBoolean(view) && getBaby(view))).createDisguise();
                                    } else {
                                        options = new Object[]{getBoolean(view), Age.getFromBoolean(!getBoolean(view) && getBaby(view))};
                                    }
                                    break;
                                case PILLAGER:
                                case BAT:
                                case CREEPER:
                                case ENDERMAN:
                                case BLAZE:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, null, getBoolean(view), false).createDisguise();
                                    } else {
                                        options = new Object[]{getBoolean(view)};
                                    }
                                    break;
                                case SHEEP:
                                case WOLF:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getColor(view), getBoolean(view), getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{getColor(view), getBoolean(view), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case HORSE:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getHorseColor(view), false, getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{getHorseColor(view), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case LLAMA:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getLlamaColor(view), getBoolean(view), (!getBoolean(view) && getBaby(view))).createDisguise();
                                    } else {
                                        options = new Object[]{getLlamaColor(view), getBoolean(view), Age.getFromBoolean(!getBoolean(view) && getBaby(view))};
                                    }
                                    break;
                                case OCELOT:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, null, getBoolean(view), getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{getBoolean(view), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case PARROT:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getParrotVariant(view), false, getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{getParrotVariant(view), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case RABBIT:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getRabbitType(view), getBoolean(view), getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{getRabbitType(view), getBoolean(view), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case VILLAGER:
                                case ZOMBIE_VILLAGER:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getProfession(view), false, getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{Profession.getFromVillagerProfession(getProfession(view)), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case SLIME:
                                case MAGMA_CUBE:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getSlimeSize(view), false, false).createDisguise();
                                    } else {
                                        options = new Object[]{getSlimeSize(view)};
                                    }
                                    break;
                                case COW:
                                case TURTLE:
                                case ZOMBIE:
                                case BEE:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, null, false, getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case SNOWMAN:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, snowmen.get(uuid), false, false).createDisguise();
                                    } else {
                                        options = new Object[]{snowmen.get(uuid)};
                                    }
                                    break;
                                case PUFFERFISH:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, puffers.get(uuid), false, false).createDisguise();
                                    } else {
                                        options = new Object[]{puffers.get(uuid)};
                                    }
                                    break;
                                case TROPICAL_FISH:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, TropicalFish.Pattern.values()[tropics.get(uuid)], false, false).createDisguise();
                                    } else {
                                        options = new Object[]{TropicalFish.Pattern.values()[tropics.get(uuid)]};
                                    }
                                    break;
                                case MUSHROOM_COW:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getCowVariant(view), false, getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{Mooshroom.getFromMushroomCowType(getCowVariant(view)), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                case FOX:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, getFoxType(view), false, getBaby(view)).createDisguise();
                                    } else {
                                        options = new Object[]{Fox.getFromFoxType(getFoxType(view)), Age.getFromBoolean(getBaby(view))};
                                    }
                                    break;
                                default:
                                    if (plugin.isDisguisesOnServer()) {
                                        new TardisLazarusLibs(player, disguise, null, false, false).createDisguise();
                                    }
                                    break;
                            }
                            if (!plugin.isDisguisesOnServer()) {
                                new TardisLazarusDisguise(plugin, player, dt, options).createDisguise();
                            }
                        }
                        TardisMessage.send(player, "GENETICS_MODIFIED", disguise);
                        plugin.getPluginManager().callEvent(new TardisGeneticManipulatorDisguiseEvent(player, disguise));
                    }
                }, 80L);
                // open the door
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    openDoor(b);
                    untrack(uuid, false);
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
        String name = event.getView().getTitle();
        UUID uuid = event.getPlayer().getUniqueId();
        if (name.equals(ChatColor.DARK_RED + "Genetic Manipulator") && !plugin.getTrackerKeeper().getGeneticManipulation().contains(uuid)) {
            Block b = plugin.getTrackerKeeper().getLazarus().get(event.getPlayer().getUniqueId());
            if (b.getRelative(BlockFace.SOUTH).getType().equals(Material.COBBLESTONE_WALL)) {
                b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
                b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
            }
            untrack(uuid, false);
        }
    }

    private void untrack(UUID uuid, boolean remove) {
        // stop tracking player
        plugin.getTrackerKeeper().getLazarus().remove(uuid);
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

    private void openDoor(Block b) {
        b.getRelative(BlockFace.SOUTH).setType(Material.AIR);
        b.getRelative(BlockFace.SOUTH).getRelative(BlockFace.UP).setType(Material.AIR);
    }

    private void setSlotFourtyEight(InventoryView i, String d, UUID uuid) {
        String t = null;
        int o;
        switch (d) {
            case "AXOLOTL":
                if (axolotls.containsKey(uuid)) {
                    o = (axolotls.get(uuid) + 1 < 5) ? axolotls.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Axolotl.Variant.values()[o].toString();
                axolotls.put(uuid, o);
                break;
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
                t = Horse.Color.values()[o].toString();
                horses.put(uuid, o);
                break;
            case "LLAMA":
                if (llamas.containsKey(uuid)) {
                    o = (llamas.get(uuid) + 1 < 4) ? llamas.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Llama.Color.values()[o].toString();
                llamas.put(uuid, o);
                break;
            case "CAT":
                if (cats.containsKey(uuid)) {
                    o = (cats.get(uuid) + 1 < 11) ? cats.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Type.values()[o].toString();
                cats.put(uuid, o);
                break;
            case "FOX":
                if (foxes.containsKey(uuid)) {
                    o = (foxes.get(uuid) + 1 < 2) ? foxes.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = org.bukkit.entity.Fox.Type.values()[o].toString();
                foxes.put(uuid, o);
                break;
            case "RABBIT":
                if (rabbits.containsKey(uuid)) {
                    o = (rabbits.get(uuid) + 1 < 7) ? rabbits.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Rabbit.Type.values()[o].toString();
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
                t = Villager.Profession.values()[o].toString();
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
            case "MUSHROOM_COW":
                if (moos.containsKey(uuid)) {
                    o = (moos.get(uuid) + 1 < 2) ? moos.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = MushroomCow.Variant.values()[o].toString();
                moos.put(uuid, o);
                break;
            case "PUFFERFISH":
                if (puffers.containsKey(uuid)) {
                    o = (puffers.get(uuid) + 1 < 3) ? puffers.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = pufferStates.get(o).toString();
                puffers.put(uuid, o);
                break;
            case "TROPICAL_FISH":
                if (tropics.containsKey(uuid)) {
                    o = (tropics.get(uuid) + 1 < 12) ? tropics.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = TropicalFish.Pattern.values()[o].toString();
                tropics.put(uuid, o);
                break;
            case "PANDA":
                if (genes.containsKey(uuid)) {
                    o = (genes.get(uuid) + 1 < 7) ? genes.get(uuid) + 1 : 0;
                } else {
                    o = 0;
                }
                t = Panda.Gene.values()[o].toString();
                genes.put(uuid, o);
                break;
            default:
                break;
        }
        if (t != null) {
            ItemStack is = i.getItem(48);
            assert is != null;
            ItemMeta im = is.getItemMeta();
            assert im != null;
            im.setLore(Collections.singletonList(t));
            is.setItemMeta(im);
        }
    }

    private boolean isReversedPolarity(InventoryView i) {
        ItemStack is = i.getItem(45);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        assert im != null;
        return Objects.requireNonNull(im.getLore()).get(0).equals(plugin.getLanguage().getString("SET_ON"));
    }

    private DyeColor getColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return DyeColor.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return DyeColor.WHITE;
        }
    }

    private Horse.Color getHorseColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Horse.Color.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return Horse.Color.WHITE;
        }
    }

    private MushroomCow.Variant getCowVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return MushroomCow.Variant.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return MushroomCow.Variant.RED;
        }
    }

    private Llama.Color getLlamaColor(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Llama.Color.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return org.bukkit.entity.Llama.Color.CREAMY;
        }
    }

    private Axolotl.Variant getAxolotlVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Axolotl.Variant.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return Axolotl.Variant.WILD;
        }
    }

    private Type getCatType(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Type.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return Type.TABBY;
        }
    }

    private org.bukkit.entity.Fox.Type getFoxType(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return org.bukkit.entity.Fox.Type.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return org.bukkit.entity.Fox.Type.RED;
        }
    }

    private Panda.Gene getGene(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Panda.Gene.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return Panda.Gene.NORMAL;
        }
    }

    private Parrot.Variant getParrotVariant(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Parrot.Variant.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return Parrot.Variant.GRAY;
        }
    }

    private Rabbit.Type getRabbitType(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Rabbit.Type.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return Rabbit.Type.BROWN;
        }
    }

    private Villager.Profession getProfession(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        try {
            assert im != null;
            return Villager.Profession.valueOf(Objects.requireNonNull(im.getLore()).get(0));
        } catch (IllegalArgumentException e) {
            return Villager.Profession.FARMER;
        }
    }

    private int getSlimeSize(InventoryView i) {
        ItemStack is = i.getItem(48);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        assert im != null;
        int size = TardisNumberParsers.parseInt(Objects.requireNonNull(im.getLore()).get(0));
        return (size == 0) ? 2 : size;
    }

    private boolean getBaby(InventoryView i) {
        ItemStack is = i.getItem(47);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        assert im != null;
        return Objects.requireNonNull(im.getLore()).get(0).equals("BABY");
    }

    private boolean getBoolean(InventoryView i) {
        ItemStack is = i.getItem(49);
        assert is != null;
        ItemMeta im = is.getItemMeta();
        assert im != null;
        List<String> lore = im.getLore();
        assert lore != null;
        int pos = lore.size() - 1;
        return ChatColor.stripColor(lore.get(pos)).equals("TRUE");
    }

    private void twaOff(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && helmet.hasItemMeta() && Objects.requireNonNull(helmet.getItemMeta()).hasDisplayName()) {
            String metaName = helmet.getItemMeta().getDisplayName();
            if (twaHelmets.contains(metaName)) {
                plugin.getServer().dispatchCommand(plugin.getConsole(), "twa disguise WEEPING_ANGEL off " + player.getUniqueId());
            }
        }
    }
}
