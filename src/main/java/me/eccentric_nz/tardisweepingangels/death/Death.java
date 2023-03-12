/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.ArrayList;
import java.util.List;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngelSpawnEvent;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.equip.Equipper;
import me.eccentric_nz.tardisweepingangels.utils.HeadBuilder;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.Location;
import org.bukkit.Material;
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
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

public class Death implements Listener {

    private final TARDISWeepingAngels plugin;
    private final List<Material> angel_drops = new ArrayList<>();
    private final List<Material> cyber_drops = new ArrayList<>();
    private final List<Material> dalek_drops = new ArrayList<>();
    private final List<Material> dalek_sec_drops = new ArrayList<>();
    private final List<Material> davros_drops = new ArrayList<>();
    private final List<Material> devil_drops = new ArrayList<>();
    private final List<Material> empty_drops = new ArrayList<>();
    private final List<Material> hath_drops = new ArrayList<>();
    private final List<Material> headless_drops = new ArrayList<>();
    private final List<Material> ice_drops = new ArrayList<>();
    private final List<Material> mire_drops = new ArrayList<>();
    private final List<Material> silent_drops = new ArrayList<>();
    private final List<Material> silurian_drops = new ArrayList<>();
    private final List<Material> slitheen_drops = new ArrayList<>();
    private final List<Material> sontaran_drops = new ArrayList<>();
    private final List<Material> vashta_drops = new ArrayList<>();
    private final List<Material> zygon_drops = new ArrayList<>();

    public Death(TARDISWeepingAngels plugin) {
        this.plugin = plugin;
        plugin.getConfig().getStringList("angels.drops").forEach((a) -> angel_drops.add(Material.valueOf(a)));
        plugin.getConfig().getStringList("cybermen.drops").forEach((c) -> cyber_drops.add(Material.valueOf(c)));
        plugin.getConfig().getStringList("daleks.drops").forEach((d) -> dalek_drops.add(Material.valueOf(d)));
        plugin.getConfig().getStringList("daleks.dalek_sec_drops").forEach((d) -> dalek_sec_drops.add(Material.valueOf(d)));
        plugin.getConfig().getStringList("daleks.davros_drops").forEach((d) -> davros_drops.add(Material.valueOf(d)));
        plugin.getConfig().getStringList("sea_devils.drops").forEach((d) -> devil_drops.add(Material.valueOf(d)));
        plugin.getConfig().getStringList("empty_child.drops").forEach((e) -> empty_drops.add(Material.valueOf(e)));
        plugin.getConfig().getStringList("hath.drops").forEach((e) -> hath_drops.add(Material.valueOf(e)));
        plugin.getConfig().getStringList("headless_monks.drops").forEach((e) -> headless_drops.add(Material.valueOf(e)));
        plugin.getConfig().getStringList("ice_warriors.drops").forEach((i) -> ice_drops.add(Material.valueOf(i)));
        plugin.getConfig().getStringList("silent.drops").forEach((m) -> silent_drops.add(Material.valueOf(m)));
        plugin.getConfig().getStringList("silurians.drops").forEach((s) -> silurian_drops.add(Material.valueOf(s)));
        plugin.getConfig().getStringList("slitheen.drops").forEach((s) -> slitheen_drops.add(Material.valueOf(s)));
        plugin.getConfig().getStringList("sontarans.drops").forEach((o) -> sontaran_drops.add(Material.valueOf(o)));
        plugin.getConfig().getStringList("the_mire.drops").forEach((e) -> mire_drops.add(Material.valueOf(e)));
        plugin.getConfig().getStringList("vashta_nerada.drops").forEach((v) -> vashta_drops.add(Material.valueOf(v)));
        plugin.getConfig().getStringList("zygons.drops").forEach((z) -> zygon_drops.add(Material.valueOf(z)));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        PersistentDataContainer pdc = event.getEntity().getPersistentDataContainer();
        if (event.getEntityType().equals(EntityType.SKELETON)) {
            if (pdc.has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.WEEPING_ANGEL);
                } else {
                    stack = new ItemStack(angel_drops.get(TARDISWeepingAngels.random.nextInt(angel_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.SILURIAN, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.SILURIAN);
                } else {
                    stack = new ItemStack(silurian_drops.get(TARDISWeepingAngels.random.nextInt(silurian_drops.size())), TARDISWeepingAngels.random.nextInt(2) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.MONK, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.HEADLESS_MONK);
                } else if (TARDISWeepingAngels.random.nextInt(100) < 6) {
                    stack = new ItemStack(Material.IRON_SWORD, 1);
                } else {
                    stack = new ItemStack(headless_drops.get(TARDISWeepingAngels.random.nextInt(headless_drops.size())), TARDISWeepingAngels.random.nextInt(2) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.MIRE, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.MIRE);
                } else if (TARDISWeepingAngels.random.nextInt(100) < 6) {
                    stack = new ItemStack(Material.NETHERITE_SCRAP, 2);
                } else {
                    stack = new ItemStack(mire_drops.get(TARDISWeepingAngels.random.nextInt(mire_drops.size())), TARDISWeepingAngels.random.nextInt(2) + 1);
                    if (stack.getType() == Material.POTION) {
                        // make it a strength potion
                        PotionMeta potionMeta = (PotionMeta)stack.getItemMeta();
                        potionMeta.setBasePotionData(new PotionData(PotionType.STRENGTH));
                        stack.setItemMeta(potionMeta);
                    }
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.DALEK);
                } else {
                    stack = new ItemStack(dalek_drops.get(TARDISWeepingAngels.random.nextInt(dalek_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.DALEK_SEC, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.DALEK_SEC);
                } else {
                    stack = new ItemStack(dalek_sec_drops.get(TARDISWeepingAngels.random.nextInt(dalek_sec_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.DAVROS, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.DAVROS);
                } else {
                    stack = new ItemStack(davros_drops.get(TARDISWeepingAngels.random.nextInt(davros_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.DEVIL, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.SEA_DEVIL);
                } else if (TARDISWeepingAngels.random.nextInt(100) < 6) {
                    stack = new ItemStack(Material.TRIDENT, 1);
                } else {
                    stack = new ItemStack(devil_drops.get(TARDISWeepingAngels.random.nextInt(devil_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.SLITHEEN, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.SLITHEEN);
                } else {
                    stack = new ItemStack(slitheen_drops.get(TARDISWeepingAngels.random.nextInt(slitheen_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)) {
                // remove the guardian as well
                Entity guardian = (event.getEntity().getPassengers().size() > 0) ? event.getEntity().getPassengers().get(0) : null;
                if (guardian != null) {
                    guardian.remove();
                }
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.SILENT);
                } else {
                    stack = new ItemStack(silent_drops.get(TARDISWeepingAngels.random.nextInt(silent_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
            }
        }
        if (event.getEntityType().equals(EntityType.ZOMBIFIED_PIGLIN)) {
            if (pdc.has(TARDISWeepingAngels.WARRIOR, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.ICE_WARRIOR);
                } else {
                    stack = new ItemStack(ice_drops.get(TARDISWeepingAngels.random.nextInt(ice_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.HATH, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                ItemStack stack;
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.HATH);
                } else {
                    stack = new ItemStack(hath_drops.get(TARDISWeepingAngels.random.nextInt(hath_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
        }
        if (event.getEntityType().equals(EntityType.ZOMBIE)) {
            ItemStack stack;
            if (pdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.CYBERMAN);
                } else if (TARDISWeepingAngels.random.nextInt(100) < 6) {
                    stack = new ItemStack(Material.IRON_INGOT, 1);
                } else {
                    stack = new ItemStack(cyber_drops.get(TARDISWeepingAngels.random.nextInt(cyber_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.EMPTY_CHILD);
                } else if (TARDISWeepingAngels.random.nextInt(100) < 6) {
                    stack = new ItemStack(Material.POTION);
                    PotionMeta potionMeta = (PotionMeta) stack.getItemMeta();
                    potionMeta.setBasePotionData(new PotionData(PotionType.REGEN));
                    stack.setItemMeta(potionMeta);
                } else {
                    stack = new ItemStack(empty_drops.get(TARDISWeepingAngels.random.nextInt(empty_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.SONTARAN, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.SONTARAN);
                } else if (TARDISWeepingAngels.random.nextInt(100) < 6) {
                    stack = new ItemStack(Material.MILK_BUCKET, 1);
                } else {
                    stack = new ItemStack(sontaran_drops.get(TARDISWeepingAngels.random.nextInt(sontaran_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
            if (pdc.has(TARDISWeepingAngels.VASHTA, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.VASHTA_NERADA);
                } else {
                    stack = new ItemStack(vashta_drops.get(TARDISWeepingAngels.random.nextInt(vashta_drops.size())), TARDISWeepingAngels.random.nextInt(2) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
            }
            if (pdc.has(TARDISWeepingAngels.ZYGON, PersistentDataType.INTEGER)) {
                event.getDrops().clear();
                if (TARDISWeepingAngels.random.nextInt(100) < 3) {
                    stack = HeadBuilder.getItemStack(Monster.ZYGON);
                } else {
                    stack = new ItemStack(zygon_drops.get(TARDISWeepingAngels.random.nextInt(zygon_drops.size())), TARDISWeepingAngels.random.nextInt(1) + 1);
                }
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                return;
            }
        }
        if (event.getEntityType().equals(EntityType.VILLAGER) || event.getEntityType().equals(EntityType.PLAYER)) {
            if (!plugin.getConfig().getBoolean("cybermen.can_upgrade")) {
                return;
            }
            if (plugin.isCitizensEnabled() && CitizensAPI.getNPCRegistry().isNPC(event.getEntity())) {
                return;
            }
            EntityDamageEvent damage = event.getEntity().getLastDamageCause();
            if (damage != null && damage.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                Entity attacker = (((EntityDamageByEntityEvent) damage).getDamager());
                PersistentDataContainer apdc = attacker.getPersistentDataContainer();
                if (attacker instanceof Zombie && apdc.has(TARDISWeepingAngels.CYBERMAN, PersistentDataType.INTEGER)) {
                    Location l = event.getEntity().getLocation();
                    LivingEntity e = (LivingEntity) l.getWorld().spawnEntity(l, EntityType.ZOMBIE);
                    e.setSilent(true);
                    new Equipper(Monster.CYBERMAN, e, false, false).setHelmetAndInvisibilty();
                    plugin.getServer().getPluginManager().callEvent(new TARDISWeepingAngelSpawnEvent(e, EntityType.ZOMBIE, Monster.CYBERMAN, l));
                    if (event.getEntity() instanceof Player) {
                        String name = event.getEntity().getName();
                        e.setCustomName(name);
                        e.setCustomNameVisible(true);
                    }
                    return;
                }
                if (apdc.has(TARDISWeepingAngels.EMPTY, PersistentDataType.INTEGER)) {
                    if (event.getEntity() instanceof Player player) {
                        plugin.getEmpty().add(player.getUniqueId());
                    }
                }
            }
        }
    }
}
