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
package me.eccentric_nz.tardisweepingangels.death;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.nms.MonsterSpawner;
import me.eccentric_nz.tardisweepingangels.nms.TWAJudoon;
import me.eccentric_nz.tardisweepingangels.utils.HeadBuilder;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.citizensnpcs.api.CitizensAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Death implements Listener {

    private final TARDIS plugin;
    private final List<Material> angel_drops = new ArrayList<>();
    private final List<Material> angel_of_liberty_drops = new ArrayList<>();
    private final List<Material> beast_drops = new ArrayList<>();
    private final List<Material> cyber_drops = new ArrayList<>();
    private final List<Material> clockwork_drops = new ArrayList<>();
    private final List<Material> dalek_drops = new ArrayList<>();
    private final List<Material> dalek_sec_drops = new ArrayList<>();
    private final List<Material> davros_drops = new ArrayList<>();
    private final List<Material> devil_drops = new ArrayList<>();
    private final List<Material> empty_drops = new ArrayList<>();
    private final List<Material> hath_drops = new ArrayList<>();
    private final List<Material> headless_drops = new ArrayList<>();
    private final List<Material> heavenly_host_drops = new ArrayList<>();
    private final List<Material> ice_drops = new ArrayList<>();
    private final List<Material> mire_drops = new ArrayList<>();
    private final List<Material> nimon_drops = new ArrayList<>();
    private final List<Material> omega_drops = new ArrayList<>();
    private final List<Material> ossified_drops = new ArrayList<>();
    private final List<Material> scarecrow_drops = new ArrayList<>();
    private final List<Material> silent_drops = new ArrayList<>();
    private final List<Material> silurian_drops = new ArrayList<>();
    private final List<Material> slitheen_drops = new ArrayList<>();
    private final List<Material> smiler_drops = new ArrayList<>();
    private final List<Material> sontaran_drops = new ArrayList<>();
    private final List<Material> sutekh_drops = new ArrayList<>();
    private final List<Material> sycorax_drops = new ArrayList<>();
    private final List<Material> vampire_drops = new ArrayList<>();
    private final List<Material> vashta_drops = new ArrayList<>();
    private final List<Material> zygon_drops = new ArrayList<>();

    public Death(TARDIS plugin) {
        this.plugin = plugin;
        plugin.getMonstersConfig().getStringList("angels.drops").forEach((a) -> angel_drops.add(Material.valueOf(a)));
        plugin.getMonstersConfig().getStringList("angel_of_liberty.drops").forEach((l) -> angel_of_liberty_drops.add(Material.valueOf(l)));
        plugin.getMonstersConfig().getStringList("the_beast.drops").forEach((b) -> beast_drops.add(Material.valueOf(b)));
        plugin.getMonstersConfig().getStringList("clockwork_droids.drops").forEach((c) -> clockwork_drops.add(Material.valueOf(c)));
        plugin.getMonstersConfig().getStringList("cybermen.drops").forEach((c) -> cyber_drops.add(Material.valueOf(c)));
        plugin.getMonstersConfig().getStringList("daleks.drops").forEach((d) -> dalek_drops.add(Material.valueOf(d)));
        plugin.getMonstersConfig().getStringList("daleks.dalek_sec_drops").forEach((d) -> dalek_sec_drops.add(Material.valueOf(d)));
        plugin.getMonstersConfig().getStringList("daleks.davros_drops").forEach((d) -> davros_drops.add(Material.valueOf(d)));
        plugin.getMonstersConfig().getStringList("empty_child.drops").forEach((e) -> empty_drops.add(Material.valueOf(e)));
        plugin.getMonstersConfig().getStringList("hath.drops").forEach((h) -> hath_drops.add(Material.valueOf(h)));
        plugin.getMonstersConfig().getStringList("headless_monks.drops").forEach((h) -> headless_drops.add(Material.valueOf(h)));
        plugin.getMonstersConfig().getStringList("heavenly_hosts.drops").forEach((h) -> heavenly_host_drops.add(Material.valueOf(h)));
        plugin.getMonstersConfig().getStringList("ice_warriors.drops").forEach((i) -> ice_drops.add(Material.valueOf(i)));
        plugin.getMonstersConfig().getStringList("nimon.drops").forEach((n) -> nimon_drops.add(Material.valueOf(n)));
        plugin.getMonstersConfig().getStringList("omega.drops").forEach((o) -> omega_drops.add(Material.valueOf(o)));
        plugin.getMonstersConfig().getStringList("ossified.drops").forEach((o) -> ossified_drops.add(Material.valueOf(o)));
        plugin.getMonstersConfig().getStringList("scarecrows.drops").forEach((s) -> scarecrow_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("sea_devils.drops").forEach((s) -> devil_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("silent.drops").forEach((s) -> silent_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("silurians.drops").forEach((s) -> silurian_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("slitheen.drops").forEach((s) -> slitheen_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("smilers.drops").forEach((s) -> smiler_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("sontarans.drops").forEach((s) -> sontaran_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("sutekh.drops").forEach((s) -> sutekh_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("sycorax.drops").forEach((s) -> sycorax_drops.add(Material.valueOf(s)));
        plugin.getMonstersConfig().getStringList("the_mire.drops").forEach((t) -> mire_drops.add(Material.valueOf(t)));
        plugin.getMonstersConfig().getStringList("vampires.drops").forEach((v) -> vampire_drops.add(Material.valueOf(v)));
        plugin.getMonstersConfig().getStringList("vashta_nerada.drops").forEach((v) -> vashta_drops.add(Material.valueOf(v)));
        plugin.getMonstersConfig().getStringList("zygons.drops").forEach((z) -> zygon_drops.add(Material.valueOf(z)));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        PersistentDataContainer pdc = event.getEntity().getPersistentDataContainer();
        EntityType type = event.getEntityType();
        switch (type) {
            case SKELETON -> {
                if (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.WEEPING_ANGEL);
                    } else {
                        stack = ItemStack.of(angel_drops.get(TARDISConstants.RANDOM.nextInt(angel_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SILURIAN);
                    } else {
                        stack = ItemStack.of(silurian_drops.get(TARDISConstants.RANDOM.nextInt(silurian_drops.size())), TARDISConstants.RANDOM.nextInt(2) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                    if (pdc.has(TARDISWeepingAngels.HEADLESS_TASK, PersistentDataType.INTEGER)) {
                        plugin.getServer().getScheduler().cancelTask(pdc.get(TARDISWeepingAngels.HEADLESS_TASK, PersistentDataType.INTEGER));
                    }
                    if (pdc.has(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER)) {
                        plugin.getServer().getScheduler().cancelTask(pdc.get(TARDISWeepingAngels.FLAME_TASK, PersistentDataType.INTEGER));
                    }
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.HEADLESS_MONK);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.IRON_SWORD, 1);
                    } else {
                        stack = ItemStack.of(headless_drops.get(TARDISConstants.RANDOM.nextInt(headless_drops.size())), TARDISConstants.RANDOM.nextInt(2) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.MIRE);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.NETHERITE_SCRAP, 2);
                    } else {
                        stack = ItemStack.of(mire_drops.get(TARDISConstants.RANDOM.nextInt(mire_drops.size())), TARDISConstants.RANDOM.nextInt(2) + 1);
                        if (stack.getType() == Material.POTION) {
                            // make it a strength potion
                            PotionMeta potionMeta = (PotionMeta) stack.getItemMeta();
                            potionMeta.setBasePotionType(PotionType.STRENGTH);
                            stack.setItemMeta(potionMeta);
                        }
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.OMEGA, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.OMEGA);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.NETHERITE_SCRAP, 2);
                    } else {
                        stack = ItemStack.of(omega_drops.get(TARDISConstants.RANDOM.nextInt(omega_drops.size())), TARDISConstants.RANDOM.nextInt(2) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.DALEK);
                    } else {
                        stack = ItemStack.of(dalek_drops.get(TARDISConstants.RANDOM.nextInt(dalek_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.DALEK_SEC);
                    } else {
                        stack = ItemStack.of(dalek_sec_drops.get(TARDISConstants.RANDOM.nextInt(dalek_sec_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.DAVROS);
                    } else {
                        stack = ItemStack.of(davros_drops.get(TARDISConstants.RANDOM.nextInt(davros_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SLITHEEN);
                    } else {
                        stack = ItemStack.of(slitheen_drops.get(TARDISConstants.RANDOM.nextInt(slitheen_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)) {
                    // remove the guardian as well
                    Entity guardian = (!event.getEntity().getPassengers().isEmpty()) ? event.getEntity().getPassengers().getFirst() : null;
                    if (guardian != null) {
                        guardian.remove();
                    }
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SILENT);
                    } else {
                        stack = ItemStack.of(silent_drops.get(TARDISConstants.RANDOM.nextInt(silent_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                }
            }
            case ZOMBIFIED_PIGLIN -> {
                if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.ICE_WARRIOR);
                    } else {
                        stack = ItemStack.of(ice_drops.get(TARDISConstants.RANDOM.nextInt(ice_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.HATH);
                    } else {
                        stack = ItemStack.of(hath_drops.get(TARDISConstants.RANDOM.nextInt(hath_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                }
            }
            case ZOMBIE -> {
                ItemStack stack;
                if (pdc.has(TARDISWeepingAngels.ANGEL_OF_LIBERTY, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.ANGEL_OF_LIBERTY);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.CLOCK, 1);
                    } else {
                        stack = ItemStack.of(angel_of_liberty_drops.get(TARDISConstants.RANDOM.nextInt(angel_of_liberty_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.BEAST, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.THE_BEAST);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.IRON_INGOT, 1);
                    } else {
                        stack = ItemStack.of(beast_drops.get(TARDISConstants.RANDOM.nextInt(beast_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.CLOCKWORK_DROID, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.CLOCKWORK_DROID);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.CLOCK, 1);
                    } else {
                        stack = ItemStack.of(clockwork_drops.get(TARDISConstants.RANDOM.nextInt(clockwork_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER) || pdc.has(TARDISWeepingAngels.CYBERSHADE, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.CYBERMAN);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.IRON_INGOT, 1);
                    } else {
                        stack = ItemStack.of(cyber_drops.get(TARDISConstants.RANDOM.nextInt(cyber_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.EMPTY_CHILD);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.POTION);
                        PotionMeta potionMeta = (PotionMeta) stack.getItemMeta();
                        potionMeta.setBasePotionType(PotionType.REGENERATION);
                        stack.setItemMeta(potionMeta);
                    } else {
                        stack = ItemStack.of(empty_drops.get(TARDISConstants.RANDOM.nextInt(empty_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.HEAVENLY_HOST, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.HEAVENLY_HOST);
                    } else {
                        stack = ItemStack.of(heavenly_host_drops.get(TARDISConstants.RANDOM.nextInt(heavenly_host_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.OSSIFIED, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.OSSIFIED);
                    } else {
                        stack = ItemStack.of(ossified_drops.get(TARDISConstants.RANDOM.nextInt(ossified_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.NIMON, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.NIMON);
                    } else {
                        stack = ItemStack.of(nimon_drops.get(TARDISConstants.RANDOM.nextInt(nimon_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.SCARECROW, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SCARECROW);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.HAY_BLOCK, 1);
                    } else {
                        stack = ItemStack.of(scarecrow_drops.get(TARDISConstants.RANDOM.nextInt(scarecrow_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.SMILER, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SMILER);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.IRON_INGOT, 1);
                    } else {
                        stack = ItemStack.of(smiler_drops.get(TARDISConstants.RANDOM.nextInt(smiler_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SONTARAN);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.MILK_BUCKET, 1);
                    } else {
                        stack = ItemStack.of(sontaran_drops.get(TARDISConstants.RANDOM.nextInt(sontaran_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.SYCORAX, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SYCORAX);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.IRON_SWORD, 1);
                    } else {
                        stack = ItemStack.of(sycorax_drops.get(TARDISConstants.RANDOM.nextInt(sycorax_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }
                if (pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.VASHTA_NERADA);
                    } else {
                        stack = ItemStack.of(vashta_drops.get(TARDISConstants.RANDOM.nextInt(vashta_drops.size())), TARDISConstants.RANDOM.nextInt(2) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                }
                if (pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.ZYGON);
                    } else {
                        stack = ItemStack.of(zygon_drops.get(TARDISConstants.RANDOM.nextInt(zygon_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                }
            }
            case VILLAGER, PLAYER -> {
                if (!plugin.getMonstersConfig().getBoolean("cybermen.can_upgrade")) {
                    return;
                }
                if (TARDISWeepingAngels.isCitizensEnabled() && CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) {
                    return;
                }
                EntityDamageEvent damage = event.getEntity().getLastDamageCause();
                if (damage != null && damage.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                    Entity attacker = (((EntityDamageByEntityEvent) damage).getDamager());
                    PersistentDataContainer apdc = attacker.getPersistentDataContainer();
                    if (attacker instanceof Zombie && apdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                        Location l = event.getEntity().getLocation();
                        LivingEntity e = new MonsterSpawner().create(l, Monster.CYBERMAN);
                        new Equipper(Monster.CYBERMAN, e, false).setHelmetAndInvisibility();
                        plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, EntityType.ZOMBIE, Monster.CYBERMAN, l));
                        if (event.getEntity() instanceof Player) {
                            String name = event.getEntity().getName();
                            e.customName(Component.text(name));
                            e.setCustomNameVisible(true);
                        }
                        return;
                    }
                    if (apdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                        if (event.getEntity() instanceof Player player) {
                            TARDISWeepingAngels.getEmpty().add(player.getUniqueId());
                        }
                    }
                }
            }
            case DROWNED -> {
                if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SEA_DEVIL);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.TRIDENT, 1);
                    } else {
                        stack = ItemStack.of(devil_drops.get(TARDISConstants.RANDOM.nextInt(devil_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                    return;
                }

                if (pdc.has(TARDISWeepingAngels.VAMPIRE, PersistentDataType.INTEGER) || pdc.has(TARDISWeepingAngels.SATURNYNIAN, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    ItemStack stack;
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(pdc.has(TARDISWeepingAngels.VAMPIRE, PersistentDataType.INTEGER) ? Monster.VAMPIRE_OF_VENICE : Monster.SATURNYNIAN);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.TRIDENT, 1);
                    } else {
                        stack = ItemStack.of(vampire_drops.get(TARDISConstants.RANDOM.nextInt(vampire_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                }
            }
            case HUSK -> {
                if (pdc.has(TARDISWeepingAngels.OWNER_UUID, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                    UUID uuid = null;
                    if (pdc.has(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        uuid = pdc.get(TARDISWeepingAngels.OOD, TARDISWeepingAngels.PersistentDataTypeUUID);
                    }
                    if (pdc.has(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        uuid = pdc.get(TARDISWeepingAngels.JUDOON, TARDISWeepingAngels.PersistentDataTypeUUID);
                        if (((CraftEntity) event.getEntity()).getHandle() instanceof TWAJudoon judoon) {
                            if (judoon.isGuard()) {
                                // remove guard trackers
                                TARDISWeepingAngels.getGuards().remove(event.getEntity().getUniqueId());
                                UUID owner = judoon.getOwnerUUID();
                                TARDISWeepingAngels.getPlayersWithGuards().remove(owner);
                            }
                        }
                    }
                    if (pdc.has(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID)) {
                        uuid = pdc.get(TARDISWeepingAngels.K9, TARDISWeepingAngels.PersistentDataTypeUUID);
                    }
                    if (uuid != null) {
                        // remove database entry
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("uuid", uuid.toString());
                        plugin.getQueryFactory().doDelete("followers", where);
                    }
                }
            }
            case STRAY -> {
                ItemStack stack;
                if (pdc.has(TARDISWeepingAngels.SUTEKH, PersistentDataType.INTEGER)) {
                    event.getDrops().clear();
                    if (TARDISConstants.RANDOM.nextInt(100) < 3) {
                        stack = HeadBuilder.getItemStack(Monster.SUTEKH);
                    } else if (TARDISConstants.RANDOM.nextInt(100) < 6) {
                        stack = ItemStack.of(Material.IRON_INGOT, 1);
                    } else {
                        stack = ItemStack.of(sutekh_drops.get(TARDISConstants.RANDOM.nextInt(sutekh_drops.size())), TARDISConstants.RANDOM.nextInt(1) + 1);
                    }
                    event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                }
            }
            default -> {
            }
        }
    }
}
