/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.siegemode;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.builders.exterior.BuildData;
import me.eccentric_nz.TARDIS.customblocks.TARDISBlockDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.customblocks.TARDISMushroomBlockData;
import me.eccentric_nz.TARDIS.custommodels.keys.Whoniverse;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class SiegeListener implements Listener {

    private final TARDIS plugin;

    public SiegeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onSiegeCubeDespawn(ItemDespawnEvent event) {
        ItemStack is = event.getEntity().getItemStack();
        if (!isSiegeCube(is)) {
            return;
        }
        if (!hasSiegeCubeName(is)) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSiegeCubeBreak(BlockBreakEvent event) {
        Block b = event.getBlock();
        if (!isSiegeCube(b)) {
            return;
        }
        // check location
        HashMap<String, Object> where = new HashMap<>();
        where.put("world", b.getWorld().getName());
        where.put("x", b.getX());
        where.put("y", b.getY());
        where.put("z", b.getZ());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
        if (!rsc.resultSet()) {
            return;
        }
        event.setCancelled(true);
        int id = rsc.getTardis_id();
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
        if (!rs.resultSet()) {
            return;
        }
        Tardis tardis = rs.getTardis();
        // only break if player is owner or companion
        UUID puuid = event.getPlayer().getUniqueId();
        UUID tluuid = tardis.getUuid();
        if (!puuid.equals(tluuid)) {
            boolean isCompanion = false;
            if (!tardis.getCompanions().isEmpty()) {
                // check if they are a companion
                for (String cuuid : tardis.getCompanions().split(":")) {
                    if (cuuid.equals(puuid.toString())) {
                        isCompanion = true;
                        break;
                    }
                }
            }
            if (!isCompanion) {
                plugin.getMessenger().send(event.getPlayer(), TardisModule.TARDIS, "SIEGE_COMPANION");
                return;
            }
        }
        String tl = tardis.getOwner();
        ItemStack is = ItemStack.of(TARDISBlockDisplayItem.SIEGE_CUBE.getMaterial(), 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(ComponentUtils.toWhite("TARDIS Siege Cube"));
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, TARDISBlockDisplayItem.SIEGE_CUBE.getCustomModel().getKey());
        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Time Lord: " + tl));
        lore.add(Component.text("ID: " + id));
        // get occupants
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherec, true);
        if (rst.resultSet()) {
            rst.getData().forEach((tuuid) -> {
                Player p = plugin.getServer().getPlayer(tuuid);
                if (p != null && tuuid != tluuid) {
                    String c = p.getName();
                    lore.add(Component.text("Companion: " + c));
                }
            });
        }
        im.lore(lore);
        is.setItemMeta(im);
        // set block to AIR
        b.setBlockData(TARDISConstants.AIR);
        Item item = b.getWorld().dropItemNaturally(b.getLocation(), is);
        item.setInvulnerable(true);
        // track it
        plugin.getTrackerKeeper().getIsSiegeCube().add(id);
        // track the player as well
        plugin.getTrackerKeeper().getSiegeCarrying().put(puuid, id);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDropSiegeCube(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        // only if we're tracking this player
        if (!plugin.getTrackerKeeper().getSiegeCarrying().containsKey(uuid)) {
            return;
        }
        ItemStack is = item.getItemStack();
        if (!isSiegeCube(is)) {
            return;
        }
        if (!hasSiegeCubeName(is)) {
            return;
        }
        if (plugin.getUtils().inTARDISWorld(p)) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_TARDIS");
            return;
        }
        if (!plugin.getPlanetsConfig().getBoolean("planets." + p.getLocation().getWorld().getName() + ".time_travel")) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_WORLD");
            return;
        }
        item.setInvulnerable(true);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // check there is space
            Location loc = item.getLocation();
            COMPASS d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(p, false));
            int[] start = TARDISTimeTravel.getStartLocation(loc, d);
            int count = TARDISTimeTravel.safeLocation(start[0], loc.getBlockY(), start[2], start[1], start[3], loc.getWorld(), d);
            if (count > 0) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_SPACE");
                return;
            }
            List<Component> lore = is.getItemMeta().lore();
            if (lore == null || lore.size() < 2) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_ID");
                return;
            }
            String[] line2 = ComponentUtils.stripColour(lore.get(1)).split(": ");
            int id = TARDISNumberParsers.parseInt(line2[1]);
            // turn the drop into a block
            item.remove();
            Block siege = loc.getBlock();
            TARDISDisplayItemUtils.set(TARDISBlockDisplayItem.SIEGE_CUBE, siege, id);
            // remove trackers
            plugin.getTrackerKeeper().getIsSiegeCube().remove(id);
            plugin.getTrackerKeeper().getSiegeCarrying().remove(uuid);
            // update the current location
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            HashMap<String, Object> set = new HashMap<>();
            set.put("world", loc.getWorld().getName());
            set.put("x", loc.getBlockX());
            set.put("y", loc.getBlockY());
            set.put("z", loc.getBlockZ());
            set.put("direction", d.toString());
            plugin.getQueryFactory().doUpdate("current", set, where);
        }, 10L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSiegeCubePlace(BlockPlaceEvent event) {
        ItemStack is = event.getItemInHand();
        if (!isSiegeCube(is)) {
            return;
        }
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        // only if we're tracking this player
        if (!plugin.getTrackerKeeper().getSiegeCarrying().containsKey(uuid)) {
            return;
        }
        Location loc = event.getBlock().getLocation();
        if (plugin.getUtils().inTARDISWorld(loc)) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_TARDIS");
            return;
        }
        String w = p.getLocation().getWorld().getName();
        if (!plugin.getPlanetsConfig().getBoolean("planets." + w + ".time_travel")) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_WORLD");
            return;
        }
        // check there is room to expand to a police box
        COMPASS d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(p, false));
        int[] start = TARDISTimeTravel.getStartLocation(loc, d);
        int count = TARDISTimeTravel.safeLocation(start[0], loc.getBlockY(), start[2], start[1], start[3], loc.getWorld(), d);
        if (count > 0) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_SPACE");
            return;
        }
        // update the current location
        int id = plugin.getTrackerKeeper().getSiegeCarrying().get(uuid);
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", loc.getWorld().getName());
        set.put("x", loc.getBlockX());
        set.put("y", loc.getBlockY());
        set.put("z", loc.getBlockZ());
        set.put("direction", d.toString());
        plugin.getQueryFactory().doUpdate("current", set, where);
        // remove trackers
        plugin.getTrackerKeeper().getIsSiegeCube().remove(id);
        plugin.getTrackerKeeper().getSiegeCarrying().remove(uuid);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSiegeCubeInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (!isSiegeCube(b)) {
            return;
        }
        Player p = event.getPlayer();
        if (plugin.getUtils().inTARDISWorld(p)) {
            event.setCancelled(true);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_NO_TARDIS");
        }
        UUID uuid = p.getUniqueId();
        // check location
        HashMap<String, Object> wherec = new HashMap<>();
        wherec.put("world", b.getWorld().getName());
        wherec.put("x", b.getX());
        wherec.put("y", b.getY());
        wherec.put("z", b.getZ());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
        if (!rsc.resultSet()) {
            return;
        }
        // must be the Time Lord or companion of this TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("tardis_id", rsc.getTardis_id());
        ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
        if (!rst.resultSet()) {
            return;
        }
        Tardis tardis = rst.getTardis();
        int id = tardis.getTardisId();
        if (!uuid.equals(tardis.getUuid())) {
            boolean isCompanion = false;
            if (!tardis.getCompanions().isEmpty()) {
                for (String cuuid : tardis.getCompanions().split(":")) {
                    if (cuuid.equals(uuid.toString())) {
                        isCompanion = true;
                        break;
                    }
                }
            }
            if (!isCompanion) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_COMPANION");
                return;
            }
        }
        int min = (plugin.getArtronConfig().getInt("full_charge") / 100) * plugin.getArtronConfig().getInt("siege_transfer");
        if (!p.isSneaking()) {
            // attempt to transfer Time Lord energy to the TARDIS
            // check player has a prefs record
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
            if (!rsp.resultSet()) {
                return;
            }
            // check player has enough Time Lord energy - default 10% of full_charge
            int level = rsp.getArtronLevel();
            if (min > level) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_MIN", String.format("%s", min));
                return;
            }
            // transfer min
            HashMap<String, Object> wheretl = new HashMap<>();
            wheretl.put("uuid", uuid.toString());
            HashMap<String, Object> wherea = new HashMap<>();
            wherea.put("tardis_id", id);
            plugin.getQueryFactory().alterEnergyLevel("player_prefs", -min, wheretl, p);
            plugin.getQueryFactory().alterEnergyLevel("tardis", min, wherea, p);
            plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_TRANSFER", String.format("%s", min));
        } else {
            // attempt to unsiege the TARDIS
            // check TARDIS has minimum energy level
            if (min > tardis.getArtronLevel()) {
                plugin.getMessenger().send(p, TardisModule.TARDIS, "SIEGE_POWER");
                return;
            }
            // rebuild the TARDIS
            Location current = b.getLocation();
            BuildData bd = new BuildData(p.getUniqueId().toString());
            bd.setDirection(rsc.getDirection());
            bd.setLocation(current);
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(p);
            bd.setRebuild(true);
            bd.setSubmarine(rsc.isSubmarine());
            bd.setTardisID(id);
            bd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), 10L);
            HashMap<String, Object> set = new HashMap<>();
            set.put("siege_on", 0);
            HashMap<String, Object> wheres = new HashMap<>();
            wheres.put("tardis_id", id);
            // update the database
            plugin.getQueryFactory().doUpdate("tardis", set, wheres);
            plugin.getTrackerKeeper().getInSiegeMode().remove(id);
            if (plugin.getConfig().getBoolean("siege.texture")) {
                new SiegeMode(plugin).changeTextures(tardis.getUuid().toString(), tardis.getSchematic(), p, false);
            }
            plugin.getMessenger().sendStatus(p, "SIEGE_OFF");
        }
    }

    public static boolean isSiegeCube(ItemStack is) {
        Material m = is.getType();
        if (!m.equals(Material.BROWN_MUSHROOM_BLOCK) && !m.equals(Material.CYAN_CONCRETE)) {
            return false;
        }
        ItemMeta im = is.getItemMeta();
        if (im != null) {
            return (im.hasDisplayName() && ComponentUtils.endsWith(im.displayName(), "Siege Cube")) || (im.hasItemModel() && Whoniverse.SIEGE_CUBE.getKey().equals(im.getItemModel()));
        }
        return false;
    }

    private boolean isSiegeCube(Block b) {
        Material m = b.getType();
        if (m.equals(Material.BROWN_MUSHROOM_BLOCK) || m.equals(Material.BARRIER)) {
            BlockData blockData = b.getBlockData();
            if (blockData instanceof MultipleFacing mf) {
                return mf.getAsString().equals(TARDISMushroomBlockData.BROWN_MUSHROOM_DATA.get(2));
            } else {
                ItemDisplay tdi = TARDISDisplayItemUtils.get(b);
                return (tdi != null && tdi.getItemStack().getType() == Material.CYAN_CONCRETE);
            }
        }
        return false;
    }

    private boolean hasSiegeCubeName(ItemStack is) {
        return (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && ComponentUtils.endsWith(is.getItemMeta().displayName(), "Siege Cube"));
    }
}
