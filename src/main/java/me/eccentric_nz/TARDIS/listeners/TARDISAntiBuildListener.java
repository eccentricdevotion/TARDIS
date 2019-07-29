/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.listeners;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.ResultSetAntiBuild;
import me.eccentric_nz.TARDIS.utility.TARDISAntiBuild;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISAntiBuildListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> no_place = new ArrayList<>();
    private final List<Material> allow_interact = new ArrayList<>();
    private final List<Material> no_flower_pot = new ArrayList<>();

    public TARDISAntiBuildListener(TARDIS plugin) {
        this.plugin = plugin;
        no_place.add(Material.ACACIA_BOAT);
        no_place.add(Material.BAT_SPAWN_EGG);
        no_place.add(Material.BIRCH_BOAT);
        no_place.add(Material.BLAZE_SPAWN_EGG);
        no_place.add(Material.BUCKET);
        no_place.add(Material.CAVE_SPIDER_SPAWN_EGG);
        no_place.add(Material.CAT_SPAWN_EGG);
        no_place.add(Material.CHEST_MINECART);
        no_place.add(Material.CHICKEN_SPAWN_EGG);
        no_place.add(Material.COD_SPAWN_EGG);
        no_place.add(Material.COW_SPAWN_EGG);
        no_place.add(Material.CREEPER_SPAWN_EGG);
        no_place.add(Material.DARK_OAK_BOAT);
        no_place.add(Material.DONKEY_SPAWN_EGG);
        no_place.add(Material.DROWNED_SPAWN_EGG);
        no_place.add(Material.EGG);
        no_place.add(Material.ELDER_GUARDIAN_SPAWN_EGG);
        no_place.add(Material.ENDERMAN_SPAWN_EGG);
        no_place.add(Material.ENDERMITE_SPAWN_EGG);
        no_place.add(Material.EVOKER_SPAWN_EGG);
        no_place.add(Material.FLINT_AND_STEEL);
        no_place.add(Material.FOX_SPAWN_EGG);
        no_place.add(Material.FURNACE_MINECART);
        no_place.add(Material.GHAST_SPAWN_EGG);
        no_place.add(Material.GUARDIAN_SPAWN_EGG);
        no_place.add(Material.HOPPER_MINECART);
        no_place.add(Material.HORSE_SPAWN_EGG);
        no_place.add(Material.HUSK_SPAWN_EGG);
        no_place.add(Material.ITEM_FRAME);
        no_place.add(Material.JUNGLE_BOAT);
        no_place.add(Material.LAVA_BUCKET);
        no_place.add(Material.LLAMA_SPAWN_EGG);
        no_place.add(Material.MAGMA_CUBE_SPAWN_EGG);
        no_place.add(Material.MINECART);
        no_place.add(Material.MOOSHROOM_SPAWN_EGG);
        no_place.add(Material.MULE_SPAWN_EGG);
        no_place.add(Material.OAK_BOAT);
        no_place.add(Material.OCELOT_SPAWN_EGG);
        no_place.add(Material.PAINTING);
        no_place.add(Material.PANDA_SPAWN_EGG);
        no_place.add(Material.PARROT_SPAWN_EGG);
        no_place.add(Material.PHANTOM_SPAWN_EGG);
        no_place.add(Material.PIG_SPAWN_EGG);
        no_place.add(Material.PILLAGER_SPAWN_EGG);
        no_place.add(Material.POLAR_BEAR_SPAWN_EGG);
        no_place.add(Material.PUFFERFISH_SPAWN_EGG);
        no_place.add(Material.RABBIT_SPAWN_EGG);
        no_place.add(Material.RAVAGER_SPAWN_EGG);
        no_place.add(Material.SALMON_SPAWN_EGG);
        no_place.add(Material.SHEARS);
        no_place.add(Material.SHEEP_SPAWN_EGG);
        no_place.add(Material.SHULKER_SPAWN_EGG);
        no_place.add(Material.SILVERFISH_SPAWN_EGG);
        no_place.add(Material.SKELETON_HORSE_SPAWN_EGG);
        no_place.add(Material.SKELETON_SPAWN_EGG);
        no_place.add(Material.SLIME_SPAWN_EGG);
        no_place.add(Material.SPIDER_SPAWN_EGG);
        no_place.add(Material.SPRUCE_BOAT);
        no_place.add(Material.SQUID_SPAWN_EGG);
        no_place.add(Material.STRAY_SPAWN_EGG);
        no_place.add(Material.TNT_MINECART);
        no_place.add(Material.TRADER_LLAMA_SPAWN_EGG);
        no_place.add(Material.TROPICAL_FISH_SPAWN_EGG);
        no_place.add(Material.TURTLE_SPAWN_EGG);
        no_place.add(Material.VEX_SPAWN_EGG);
        no_place.add(Material.VILLAGER_SPAWN_EGG);
        no_place.add(Material.VINDICATOR_SPAWN_EGG);
        no_place.add(Material.WANDERING_TRADER_SPAWN_EGG);
        no_place.add(Material.WATER_BUCKET);
        no_place.add(Material.WITCH_SPAWN_EGG);
        no_place.add(Material.WITHER_SKELETON_SPAWN_EGG);
        no_place.add(Material.WOLF_SPAWN_EGG);
        no_place.add(Material.ZOMBIE_HORSE_SPAWN_EGG);
        no_place.add(Material.ZOMBIE_PIGMAN_SPAWN_EGG);
        no_place.add(Material.ZOMBIE_SPAWN_EGG);
        no_place.add(Material.ZOMBIE_VILLAGER_SPAWN_EGG);
        allow_interact.add(Material.ACACIA_BUTTON);
        allow_interact.add(Material.ACACIA_DOOR);
        allow_interact.add(Material.ACACIA_PRESSURE_PLATE);
        allow_interact.add(Material.BIRCH_BUTTON);
        allow_interact.add(Material.BIRCH_DOOR);
        allow_interact.add(Material.BIRCH_PRESSURE_PLATE);
        allow_interact.add(Material.COMPARATOR);
        allow_interact.add(Material.DARK_OAK_BUTTON);
        allow_interact.add(Material.DARK_OAK_DOOR);
        allow_interact.add(Material.DARK_OAK_PRESSURE_PLATE);
        allow_interact.add(Material.IRON_DOOR);
        allow_interact.add(Material.JUNGLE_BUTTON);
        allow_interact.add(Material.JUNGLE_DOOR);
        allow_interact.add(Material.JUNGLE_PRESSURE_PLATE);
        allow_interact.add(Material.LEVER);
        allow_interact.add(Material.OAK_BUTTON);
        allow_interact.add(Material.OAK_DOOR);
        allow_interact.add(Material.OAK_PRESSURE_PLATE);
        allow_interact.add(Material.REPEATER);
        allow_interact.add(Material.SPRUCE_BUTTON);
        allow_interact.add(Material.SPRUCE_DOOR);
        allow_interact.add(Material.SPRUCE_PRESSURE_PLATE);
        allow_interact.add(Material.STONE_BUTTON);
        allow_interact.add(Material.STONE_PRESSURE_PLATE);
        no_flower_pot.addAll(TARDISMaterials.plants);
        no_flower_pot.addAll(TARDISMaterials.saplings);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionBuild(BlockPlaceEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, uuid);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getBlock().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), "ANTIBUILD_TIMELORD", tab.getTimelord());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionBreak(BlockBreakEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, uuid);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getBlock().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), "ANTIBUILD_TIMELORD", tab.getTimelord());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionPlace(PlayerInteractEvent event) {
        // ignore non-hand click actions
        if (event.getAction().equals(Action.PHYSICAL)) {
            return;
        }
        UUID uuid = event.getPlayer().getUniqueId();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, uuid);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        EquipmentSlot hand = event.getHand();
        if (hand == null) {
            return;
        }
        ItemStack t = (hand.equals(EquipmentSlot.HAND)) ? event.getPlayer().getInventory().getItemInMainHand() : event.getPlayer().getInventory().getItemInOffHand();
        Material m;
        if (t != null) {
            m = t.getType();
        } else {
            m = Material.AIR;
        }
        if ((hand.equals(EquipmentSlot.HAND) && no_place.contains(m)) || (hand.equals(EquipmentSlot.OFF_HAND) && no_place.contains(m)) && !allow_interact.contains(event.getClickedBlock().getType())) {
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), "ANTIBUILD");
        }
        if (event.getClickedBlock().getType().equals(Material.FLOWER_POT) && (hand.equals(EquipmentSlot.HAND) && no_flower_pot.contains(m)) || (hand.equals(EquipmentSlot.OFF_HAND) && no_flower_pot.contains(m))) {
            event.setUseItemInHand(Result.DENY);
            event.setCancelled(true);
            Block b = event.getClickedBlock();
            b.getState().update();
            for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
                if (b.getRelative(f).getType().equals(Material.AIR)) {
                    b.getRelative(f).setBlockData(TARDISConstants.GLASS, true);
                    b.getRelative(f).setBlockData(TARDISConstants.AIR, true);
                    break;
                }
            }
            TARDISMessage.send(event.getPlayer(), "ANTIBUILD");
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionEntityClick(PlayerInteractEntityEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, uuid);
        if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
            return;
        }
        Vector v = event.getRightClicked().getLocation().toVector();
        TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
        if (v.isInAABB(tab.getMin(), tab.getMax())) {
            event.setCancelled(true);
            TARDISMessage.send(event.getPlayer(), "ANTIBUILD_TIMELORD", tab.getTimelord());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
            if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
                return;
            }
            Vector v = event.getEntity().getLocation().toVector();
            TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
            if (v.isInAABB(tab.getMin(), tab.getMax())) {
                event.setCancelled(true);
                TARDISMessage.send(p, "ANTIBUILD_TIMELORD", tab.getTimelord());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onCompanionPaint(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player p = (Player) event.getRemover();
            ResultSetAntiBuild rs = new ResultSetAntiBuild(plugin, p.getUniqueId());
            if (!rs.resultSet() || !plugin.getTrackerKeeper().getAntiBuild().containsKey(rs.getTardis_id())) {
                return;
            }
            Vector v = event.getEntity().getLocation().toVector();
            TARDISAntiBuild tab = plugin.getTrackerKeeper().getAntiBuild().get(rs.getTardis_id());
            if (v.isInAABB(tab.getMin(), tab.getMax())) {
                event.setCancelled(true);
                TARDISMessage.send(p, "ANTIBUILD_TIMELORD", tab.getTimelord());
            }
        }
    }
}
