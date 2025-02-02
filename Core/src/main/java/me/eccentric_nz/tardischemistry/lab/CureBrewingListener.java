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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardischemistry.lab;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.keys.CureVariant;
import org.bukkit.*;
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
import org.bukkit.potion.PotionType;

import java.util.*;

public class CureBrewingListener implements Listener {

    private final TARDIS plugin;
    private final List<String> elements = List.of("Silver", "Bismuth", "Calcium", "Cobalt");
    private final List<PotionType> cures = List.of(PotionType.AWKWARD, PotionType.MUNDANE, PotionType.THICK, PotionType.WATER);
    private final HashMap<PotionType, List<String>> potions = new HashMap<>();
    private final Set<UUID> noPickUps = new HashSet<>();

    public CureBrewingListener(TARDIS plugin) {
        this.plugin = plugin;
        potions.put(PotionType.FIRE_RESISTANCE, List.of("BLAZE_POWDER", "GLASS_BOTTLE", "MAGMA_CREAM", "NETHER_WART"));
        potions.put(PotionType.HEALING, List.of("BLAZE_POWDER", "GLASS_BOTTLE", "GLISTERING_MELON", "NETHER_WART"));
        potions.put(PotionType.INVISIBILITY, List.of("BLAZE_POWDER", "FERMENTED_SPIDER_EYE", "GLASS_BOTTLE", "GOLDEN_CARROT", "NETHER_WART"));
        potions.put(PotionType.LEAPING, List.of("BLAZE_POWDER", "GLASS_BOTTLE", "NETHER_WART", "RABBIT_FOOT"));
        potions.put(PotionType.NIGHT_VISION, List.of("BLAZE_POWDER", "CARROT", "GLASS_BOTTLE", "GOLDEN_CARROT", "NETHER_WART"));
        potions.put(PotionType.REGENERATION, List.of("BLAZE_POWDER", "GHAST_TEAR", "GLASS_BOTTLE", "NETHER_WART"));
        potions.put(PotionType.SWIFTNESS, List.of("BLAZE_POWDER", "GLASS_BOTTLE", "NETHER_WART", "SUGAR"));
        potions.put(PotionType.STRENGTH, List.of("BLAZE_POWDER", "GLASS_BOTTLE", "IRON_INGOT", "NETHER_WART"));
        potions.put(PotionType.WATER_BREATHING, List.of("BLAZE_POWDER", "GLASS_BOTTLE", "NETHER_WART", "PUFFERFISH"));
        potions.put(PotionType.AWKWARD, List.of("BLAZE_POWDER", "FEATHER:Silver", "GLASS_BOTTLE", "NETHER_WART"));
        potions.put(PotionType.MUNDANE, List.of("BLAZE_POWDER", "FEATHER:Cobalt", "GLASS_BOTTLE", "NETHER_WART"));
        potions.put(PotionType.THICK, List.of("BLAZE_POWDER", "FEATHER:Calcium", "GLASS_BOTTLE", "NETHER_WART"));
        potions.put(PotionType.WATER, List.of("BLAZE_POWDER", "FEATHER:Bismuth", "GLASS_BOTTLE", "NETHER_WART"));
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
                if (cauldron.getType() == Material.WATER_CAULDRON) {
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
                            location.getWorld().spawnParticle(Particle.SPLASH, particles, 5);
                            player.playSound(player.getLocation(), Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 1.0F, 1.0F);
                            List<String> items = new ArrayList<>();
                            // add the current item
                            items.add(item.getItemStack().getType().toString());
                            // get entities around current item
                            for (Entity e : item.getNearbyEntities(1.25d, 1.25d, 1.25d)) {
                                if (e instanceof Item i) {
                                    ItemStack is = i.getItemStack();
                                    Material type = is.getType();
                                    if (type.equals(Material.FEATHER) && is.hasItemMeta()) {
                                        ItemMeta im = is.getItemMeta();
                                        if (im.hasDisplayName() && im.hasItemModel()) {
                                            String dn = im.getDisplayName();
                                            items.add(type + (elements.contains(dn) ? ":" + dn : ""));
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
                                        location.getWorld().spawnParticle(Particle.SPLASH, particles, 5);
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
                                                case AWKWARD -> {
                                                    im.setDisplayName(ChatColor.WHITE + "Antidote");
                                                    im.setItemModel(CureVariant.ANTIDOTE.getKey());
                                                }
                                                case MUNDANE -> {
                                                    im.setDisplayName(ChatColor.WHITE + "Elixir");
                                                    im.setItemModel(CureVariant.ELIXIR.getKey());
                                                }
                                                case THICK -> {
                                                    im.setDisplayName(ChatColor.WHITE + "Eye drops");
                                                    im.setItemModel(CureVariant.EYEDROPS.getKey());
                                                }
                                                default -> { // UNCRAFTABLE
                                                    im.setDisplayName(ChatColor.WHITE + "Tonic");
                                                    im.setItemModel(CureVariant.TONIC.getKey());
                                                }
                                            }
                                            is.setItemMeta(im);
                                        } else {
                                            PotionMeta pm = (PotionMeta) is.getItemMeta();
                                            PotionType potionName;
                                            if (extend && upgrade) {
                                                potionName = PotionType.valueOf("STRONG_" + map.getKey().toString());
                                            } else if (extend) {
                                                potionName = PotionType.valueOf("LONG_" + map.getKey().toString());
                                            } else {
                                                potionName = potionType;
                                            }
                                            pm.setBasePotionType(potionName);
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
