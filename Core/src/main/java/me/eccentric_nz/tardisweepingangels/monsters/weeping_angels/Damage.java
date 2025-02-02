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
package me.eccentric_nz.tardisweepingangels.monsters.weeping_angels;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import me.eccentric_nz.tardisweepingangels.utils.MonsterTargetListener;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class Damage implements Listener {

    private final TARDIS plugin;
    private final Material mat;
    private final List<World> angel_tp_worlds = new ArrayList<>();

    public Damage(TARDIS plugin) {
        this.plugin = plugin;
        mat = Material.valueOf(plugin.getMonstersConfig().getString("angels.weapon"));
        plugin.getMonstersConfig().getStringList("angels.teleport_worlds").forEach((w) -> {
            World world = plugin.getServer().getWorld(w);
            if (w != null) {
                angel_tp_worlds.add(world);
            }
        });
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBeatUpAngel(EntityDamageByEntityEvent event) {
        EntityType et = event.getEntityType();
        if (et.equals(EntityType.SKELETON)) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            Entity e = event.getDamager();
            if (entity.getPersistentDataContainer().has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                if (e instanceof AbstractArrow) {
                    event.setCancelled(true);
                }
                if (e instanceof Player player) {
                    if (!player.getInventory().getItemInMainHand().getType().equals(mat)) {
                        event.setCancelled(true);
                    }
                }
                return;
            }
            if (entity.getPersistentDataContainer().has(TARDISWeepingAngels.DALEK, PersistentDataType.INTEGER) && (e instanceof Player player)) {
                player.playSound(entity.getLocation(), "dalek_hit", 0.5f, 1.0f);
            }
        }
        if (et.equals(EntityType.PLAYER)) {
            Entity e = event.getDamager();
            if (e instanceof Monster monster && MonsterTargetListener.monsterShouldIgnorePlayer(e, (Player) event.getEntity())) {
                event.setCancelled(true);
                monster.setTarget(null);
                return;
            }
            if (e instanceof Skeleton) {
                if (e.getPersistentDataContainer().has(TARDISWeepingAngels.ANGEL, PersistentDataType.INTEGER)) {
                    Entity t = event.getEntity();
                    Player p = (Player) t;
                    Location l = null;
                    if (plugin.getMonstersConfig().getBoolean("angels.teleport_to_location")) {
                        l = getSpecificLocation();
                    } else {
                        l = getRandomLocation(t.getWorld());
                    }
                    if (l != null) {
                        final Location tpLoc = l;
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> p.teleport(tpLoc), 1L);
                    }
                    p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 300, 5, true, false));
                    if (TARDISWeepingAngels.angelsCanSteal()) {
                        stealKey(p);
                    }
                }
            }
        }
    }

    private Location getRandomLocation(World w) {
        // is this world an allowable world? - we don't want Nether or TARDIS worlds
        if (!angel_tp_worlds.contains(w)) {
            // get a random teleport world
            w = angel_tp_worlds.get(TARDISConstants.RANDOM.nextInt(angel_tp_worlds.size()));
        }
        Chunk[] chunks = w.getLoadedChunks();
        Chunk c = chunks[TARDISConstants.RANDOM.nextInt(chunks.length)];
        int x = c.getX() * 16 + TARDISConstants.RANDOM.nextInt(16);
        int z = c.getZ() * 16 + TARDISConstants.RANDOM.nextInt(16);
        int y = w.getHighestBlockYAt(x, z);
        return new Location(w, x, y + 1, z).add(0.5d, 0, 0.5d);
    }

    private Location getSpecificLocation() {
        // get a random location from the lst
        List<String> locations = plugin.getMonstersConfig().getStringList("angels.teleport_locations");
        String l = locations.get(TARDISConstants.RANDOM.nextInt(locations.size()));
        String[] split = l.split(",");
        World w = plugin.getServer().getWorld(split[0]);
        // use the middle of the block
        double x = TARDISNumberParsers.parseDouble(split[1]) + 0.5d;
        double z = TARDISNumberParsers.parseDouble(split[3]) + 0.5d;
        double y = TARDISNumberParsers.parseDouble(split[2]);
        return new Location(w, x, y, z);
    }

    private void stealKey(Player p) {
        // only works if the item is named "TARDIS Key"
        PlayerInventory inv = p.getInventory();
        for (ItemStack stack : inv.getContents()) {
            if (stack != null) {
                if (stack.hasItemMeta()) {
                    ItemMeta im = stack.getItemMeta();
                    if (im.hasDisplayName() && im.getDisplayName().endsWith("TARDIS Key")) {
                        int amount = stack.getAmount();
                        if (amount > 1) {
                            stack.setAmount(amount - 1);
                        } else {
                            int slot = inv.first(stack);
                            inv.setItem(slot, new ItemStack(Material.AIR));
                        }
                        p.updateInventory();
                        plugin.getMessenger().message(p, TardisModule.MONSTERS, "The Weeping Angels stole your TARDIS Key");
                        break;
                    }
                }
            }
        }
    }
}
