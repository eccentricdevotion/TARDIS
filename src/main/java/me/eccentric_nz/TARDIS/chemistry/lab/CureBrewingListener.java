/*
 * Copyright (C) 2020 eccentric_nz
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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.chemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;

public class CureBrewingListener implements Listener {

    private final TARDIS plugin;
    private final List<String> fireResistancePotion = Arrays.asList("BLAZE_POWDER", "GLASS_BOTTLE", "MAGMA_CREAM", "NETHER_WART");
    private final List<String> healingPotion = Arrays.asList("BLAZE_POWDER", "GLASS_BOTTLE", "GLISTERING_MELON", "NETHER_WART");
    private final List<String> invisiblePotion = Arrays.asList("BLAZE_POWDER", "FERMENTED_SPIDER_EYE", "GLASS_BOTTLE", "GOLDEN_CARROT", "NETHER_WART");
    private final List<String> jumpingPotion = Arrays.asList("BLAZE_POWDER", "GLASS_BOTTLE", "NETHER_WART", "RABBIT_FOOT");
    private final List<String> nightVisionPotion = Arrays.asList("BLAZE_POWDER", "CARROT", "GLASS_BOTTLE", "GOLDEN_CARROT", "NETHER_WART");
    private final List<String> regenerationPotion = Arrays.asList("BLAZE_POWDER", "GHAST_TEAR", "GLASS_BOTTLE", "NETHER_WART");
    private final List<String> strengthPotion = Arrays.asList("BLAZE_POWDER", "GLASS_BOTTLE", "IRON_INGOT", "NETHER_WART");
    private final List<String> swiftnessPotion = Arrays.asList("BLAZE_POWDER", "GLASS_BOTTLE", "NETHER_WART", "SUGAR");
    private final List<String> waterBreathingPotion = Arrays.asList("BLAZE_POWDER", "GLASS_BOTTLE", "NETHER_WART", "PUFFERFISH");
    private final List<String> antidote = Arrays.asList("BLAZE_POWDER", "FEATHER:Silver", "GLASS_BOTTLE", "NETHER_WART");
    private final List<String> elixir = Arrays.asList("BLAZE_POWDER", "FEATHER:Cobalt", "GLASS_BOTTLE", "NETHER_WART");
    private final List<String> eyedrops = Arrays.asList("BLAZE_POWDER", "FEATHER:Calcium", "GLASS_BOTTLE", "NETHER_WART");
    private final List<String> tonic = Arrays.asList("BLAZE_POWDER", "FEATHER:Bismuth", "GLASS_BOTTLE", "NETHER_WART");
    private final List<String> elements = Arrays.asList("Silver", "Bismuth", "Calcium", "Cobalt");
    private final List<PotionType> cures = Arrays.asList(PotionType.AWKWARD, PotionType.MUNDANE, PotionType.THICK, PotionType.UNCRAFTABLE);
    private final HashMap<PotionType, List<String>> potions = new HashMap<>();
    private final Set<UUID> noPickUps = new HashSet<>();

    public CureBrewingListener(TARDIS plugin) {
        this.plugin = plugin;
        potions.put(PotionType.FIRE_RESISTANCE, fireResistancePotion);
        potions.put(PotionType.INSTANT_HEAL, healingPotion);
        potions.put(PotionType.INVISIBILITY, invisiblePotion);
        potions.put(PotionType.JUMP, jumpingPotion);
        potions.put(PotionType.NIGHT_VISION, nightVisionPotion);
        potions.put(PotionType.REGEN, regenerationPotion);
        potions.put(PotionType.SPEED, swiftnessPotion);
        potions.put(PotionType.STRENGTH, strengthPotion);
        potions.put(PotionType.WATER_BREATHING, waterBreathingPotion);
        potions.put(PotionType.AWKWARD, antidote);
        potions.put(PotionType.MUNDANE, elixir);
        potions.put(PotionType.THICK, eyedrops);
        potions.put(PotionType.UNCRAFTABLE, tonic);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getUtils().inTARDISWorld(player)) {
            return;
        }
        Item item = event.getItemDrop();
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            Location location = item.getLocation();
            Block cauldron = location.getBlock();
            if (item.isOnGround()) {
                if (cauldron.getType() == Material.CAULDRON) {
                    if (TARDISPermission.hasPermission(player, "tardis.chemistry.brew")) {
                        // cauldron must have water in it
                        Levelled levelled = (Levelled) cauldron.getBlockData();
                        if (levelled.getLevel() > 0) {
                            // must have campfire under it
                            if (!cauldron.getRelative(BlockFace.DOWN).getType().equals(Material.CAMPFIRE)) {
                                return;
                            }
                            noPickUps.add(player.getUniqueId());
                            Location particles = cauldron.getLocation().add(0.5, 1.25, 0.5);
                            location.getWorld().spawnParticle(Particle.WATER_SPLASH, particles, 5);
                            player.playSound(player.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1.0F, 1.0F);
                            List<String> items = new ArrayList<>();
                            // add the current item
                            items.add(item.getItemStack().getType().toString());
                            // get entities around current item
                            for (Entity e : item.getNearbyEntities(1.25d, 1.25d, 1.25d)) {
                                if (e instanceof Item) {
                                    ItemStack is = ((Item) e).getItemStack();
                                    Material type = is.getType();
                                    if (type.equals(Material.FEATHER) && is.hasItemMeta()) {
                                        ItemMeta im = is.getItemMeta();
                                        if (im.hasDisplayName() && im.hasCustomModelData()) {
                                            String dn = im.getDisplayName();
                                            items.add(type.toString() + (elements.contains(dn) ? ":" + dn : ""));
                                        } else {
                                            items.add(type.toString());
                                        }
                                    } else {
                                        items.add(type.toString());
                                    }
                                }
                            }
                            boolean isStronger = false;
                            boolean isLonger = false;
                            boolean isSplash = false;
                            boolean isLingering = false;
                            // remove redstone, glowstone, gunpowder, dragon's breath
                            if (items.contains("GLOWSTONE_DUST")) {
                                items.remove("GLOWSTONE_DUST");
                                isStronger = true;
                            }
                            if (items.contains("REDSTONE")) {
                                items.remove("REDSTONE");
                                isLonger = true;
                            }
                            if (items.contains("GUNPOWDER")) {
                                items.remove("GUNPOWDER");
                                isSplash = true;
                            }
                            if (items.contains("DRAGON_BREATH")) {
                                items.remove("DRAGON_BREATH");
                                isLingering = true;
                            }
                            Collections.sort(items);
                            for (Map.Entry<PotionType, List<String>> map : potions.entrySet()) {
                                if (items.equals(map.getValue())) {
                                    // remove items
                                    for (Entity e : item.getNearbyEntities(1.25d, 1.25d, 1.25d)) {
                                        if (e instanceof Item) {
                                            e.remove();
                                        }
                                    }
                                    item.remove();
                                    // start bubble particles
                                    int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                                        location.getWorld().spawnParticle(Particle.WATER_SPLASH, particles, 5);
                                        player.playSound(player.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1.0F, 1.0F);
                                    }, 1L, 20L);
                                    // wait 20 seconds then give potion
                                    ItemStack is;
                                    PotionType potionType = map.getKey();
                                    boolean milk = cures.contains(potionType);
                                    if (milk) {
                                        is = new ItemStack(Material.MILK_BUCKET);
                                    } else if (isSplash && isLingering) {
                                        is = new ItemStack(Material.LINGERING_POTION);
                                    } else if (isSplash) {
                                        is = new ItemStack(Material.SPLASH_POTION);
                                    } else {
                                        is = new ItemStack(Material.POTION);
                                    }
                                    boolean extend = isLonger && potionType.isExtendable();
                                    if (isLonger && !extend) {
                                        // give back the redstone
                                        location.getWorld().dropItem(location.add(0, 1.0d, 0), new ItemStack(Material.REDSTONE, 1));
                                    }
                                    boolean upgrade = isStronger && potionType.isUpgradeable();
                                    if (isStronger && !upgrade) {
                                        // give back the glowstone dust
                                        location.getWorld().dropItem(location.add(0, 1.0d, 0), new ItemStack(Material.GLOWSTONE_DUST, 1));
                                    }
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                        plugin.getServer().getScheduler().cancelTask(task);
                                        if (milk) {
                                            ItemMeta im = is.getItemMeta();
                                            switch (potionType) {
                                                case AWKWARD:
                                                    im.setDisplayName("Antidote");
                                                    im.setCustomModelData(1);
                                                    break;
                                                case MUNDANE:
                                                    im.setDisplayName("Elixir");
                                                    im.setCustomModelData(2);
                                                    break;
                                                case THICK:
                                                    im.setDisplayName("Eye drops");
                                                    im.setCustomModelData(3);
                                                    break;
                                                default: // UNCRAFTABLE
                                                    im.setDisplayName("Tonic");
                                                    im.setCustomModelData(4);
                                                    break;
                                            }
                                            is.setItemMeta(im);
                                        } else {
                                            PotionMeta pm = (PotionMeta) is.getItemMeta();
                                            PotionData potionData = new PotionData(map.getKey(), extend, upgrade);
                                            pm.setBasePotionData(potionData);
                                            is.setItemMeta(pm);
                                        }
                                        location.getWorld().dropItem(location.add(0, 1.0d, 0), is);
                                        noPickUps.remove(player.getUniqueId());
                                    }, 400L);
                                    break;
                                }
                            }
                            noPickUps.remove(player.getUniqueId());
                        }
                    }
                }
            }
        }, 20L);
    }

    @EventHandler(ignoreCancelled = true)
    public void noPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (noPickUps.contains(event.getEntity().getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
