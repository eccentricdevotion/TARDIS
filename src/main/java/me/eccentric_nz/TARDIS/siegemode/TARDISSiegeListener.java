/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
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

/**
 *
 * @author eccentric_nz
 */
public class TARDISSiegeListener implements Listener {

    private final TARDIS plugin;

    public TARDISSiegeListener(TARDIS plugin) {
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
        HashMap<String, Object> where = new HashMap<String, Object>();
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
        HashMap<String, Object> wheret = new HashMap<String, Object>();
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
                TARDISMessage.send(event.getPlayer(), "SIEGE_COMPANION");
                return;
            }
        }
        String tl = tardis.getOwner();
        ItemStack is = new ItemStack(Material.HUGE_MUSHROOM_1, 1, (byte) 14);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Siege Cube");
        List<String> lore = new ArrayList<String>();
        lore.add("Time Lord: " + tl);
        lore.add("ID: " + id);
        // get occupants
        HashMap<String, Object> wherec = new HashMap<String, Object>();
        wherec.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherec, true);
        if (rst.resultSet()) {
            for (UUID tuuid : rst.getData()) {
                Player p = plugin.getServer().getPlayer(tuuid);
                if (p != null && tuuid != tluuid) {
                    String c = p.getName();
                    lore.add("Companion: " + c);
                }
            }
        }
        im.setLore(lore);
        is.setItemMeta(im);
        // set block to AIR
        b.setType(Material.AIR);
        Item item = b.getWorld().dropItemNaturally(b.getLocation(), is);
        if (plugin.isHelperOnServer()) {
            plugin.getTardisHelper().protect(item);
        }
        // track it
        plugin.getTrackerKeeper().getIsSiegeCube().add(id);
        // track the player as well
        plugin.getTrackerKeeper().getSiegeCarrying().put(puuid, id);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDropSiegeCube(final PlayerDropItemEvent event) {
        final Item item = event.getItemDrop();
        final Player p = event.getPlayer();
        final UUID uuid = p.getUniqueId();
        // only if we're tracking this player
        if (!plugin.getTrackerKeeper().getSiegeCarrying().containsKey(uuid)) {
            return;
        }
        final ItemStack is = item.getItemStack();
        if (!isSiegeCube(is)) {
            return;
        }
        if (!hasSiegeCubeName(is)) {
            return;
        }
        if (plugin.getUtils().inTARDISWorld(p)) {
            event.setCancelled(true);
            TARDISMessage.send(p, "SIEGE_NO_TARDIS");
            return;
        }
        if (plugin.isHelperOnServer()) {
            plugin.getTardisHelper().protect(item);
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            @SuppressWarnings("deprecation")
            public void run() {
                // check there is space
                Location loc = item.getLocation();
                COMPASS d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(p, false));
                int[] start = TARDISTimeTravel.getStartLocation(loc, d);
                int count = TARDISTimeTravel.safeLocation(start[0], loc.getBlockY(), start[2], start[1], start[3], loc.getWorld(), d);
                if (count > 0) {
                    TARDISMessage.send(p, "SIEGE_NO_SPACE");
                    return;
                }
                List<String> lore = is.getItemMeta().getLore();
                if (lore == null || lore.size() < 2) {
                    TARDISMessage.send(p, "SIEGE_NO_ID");
                    return;
                }
                String[] line2 = lore.get(1).split(": ");
                int id = TARDISNumberParsers.parseInt(line2[1]);
                // turn the drop into a block
                item.remove();
                loc.getBlock().setType(Material.HUGE_MUSHROOM_1);
                loc.getBlock().setData((byte) 14, true);
                // remove trackers
                plugin.getTrackerKeeper().getIsSiegeCube().remove(Integer.valueOf(id));
                plugin.getTrackerKeeper().getSiegeCarrying().remove(uuid);
                // update the current location
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("world", loc.getWorld().getName());
                set.put("x", loc.getBlockX());
                set.put("y", loc.getBlockY());
                set.put("z", loc.getBlockZ());
                set.put("direction", d.toString());
                new QueryFactory(plugin).doUpdate("current", set, where);
            }
        }, 10L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSiegeCubePlace(final BlockPlaceEvent event) {
        Block b = event.getBlockPlaced();
        if (!isSiegeCube(b)) {
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
            TARDISMessage.send(p, "SIEGE_NO_TARDIS");
            return;
        }
        // check there is room to expand to a police box
        COMPASS d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(p, false));
        int[] start = TARDISTimeTravel.getStartLocation(loc, d);
        int count = TARDISTimeTravel.safeLocation(start[0], loc.getBlockY(), start[2], start[1], start[3], loc.getWorld(), d);
        if (count > 0) {
            event.setCancelled(true);
            TARDISMessage.send(p, "SIEGE_NO_SPACE");
            return;
        }
        // update the current location
        int id = plugin.getTrackerKeeper().getSiegeCarrying().get(uuid);
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("world", loc.getWorld().getName());
        set.put("x", loc.getBlockX());
        set.put("y", loc.getBlockY());
        set.put("z", loc.getBlockZ());
        set.put("direction", d.toString());
        new QueryFactory(plugin).doUpdate("current", set, where);
        // remove trackers
        plugin.getTrackerKeeper().getIsSiegeCube().remove(Integer.valueOf(id));
        plugin.getTrackerKeeper().getSiegeCarrying().remove(uuid);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSiegeCubeInteract(final PlayerInteractEvent event) {
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
            TARDISMessage.send(p, "SIEGE_NO_TARDIS");
        }
        UUID uuid = p.getUniqueId();
        // check location
        HashMap<String, Object> wherec = new HashMap<String, Object>();
        wherec.put("world", b.getWorld().getName());
        wherec.put("x", b.getX());
        wherec.put("y", b.getY());
        wherec.put("z", b.getZ());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
        if (!rsc.resultSet()) {
            return;
        }
        // must be the Time Lord or companion of this TARDIS
        HashMap<String, Object> wheret = new HashMap<String, Object>();
        wheret.put("tardis_id", rsc.getTardis_id());
        ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
        if (!rst.resultSet()) {
            return;
        }
        Tardis tardis = rst.getTardis();
        int id = tardis.getTardis_id();
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
                TARDISMessage.send(p, "SIEGE_COMPANION");
                return;
            }
        }
        int min = (plugin.getArtronConfig().getInt("full_charge") / 100) * plugin.getArtronConfig().getInt("siege_transfer");
        QueryFactory qf = new QueryFactory(plugin);
        if (!p.isSneaking()) {
            // attempt to transfer Time Lord energy to the TARDIS
            // check player has a prefs record
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("uuid", uuid.toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            if (!rsp.resultSet()) {
                return;
            }
            // check player has enough Time Lord energy - default 10% of full_charge
            int level = rsp.getArtronLevel();
            if (min > level) {
                TARDISMessage.send(p, "SIEGE_MIN", String.format("%s", min));
                return;
            }
            // transfer min
            HashMap<String, Object> wheretl = new HashMap<String, Object>();
            wheretl.put("uuid", uuid.toString());
            HashMap<String, Object> wherea = new HashMap<String, Object>();
            wherea.put("tardis_id", id);
            qf.alterEnergyLevel("player_prefs", -min, wheretl, p);
            qf.alterEnergyLevel("tardis", min, wherea, p);
            TARDISMessage.send(p, "SIEGE_TRANSFER", String.format("%s", min));
        } else {
            // attempt to unsiege the TARDIS
            // check TARDIS has minimum energy level
            if (min > tardis.getArtron_level()) {
                TARDISMessage.send(p, "SIEGE_POWER");
                return;
            }
            // rebuild the TARDIS
            Location current = b.getLocation();
            final TARDISMaterialisationData pbd = new TARDISMaterialisationData(plugin, p.getUniqueId().toString());
            pbd.setChameleon(tardis.isChamele_on());
            pbd.setDirection(rsc.getDirection());
            pbd.setLocation(current);
            pbd.setMalfunction(false);
            pbd.setOutside(false);
            pbd.setPlayer(p);
            pbd.setRebuild(true);
            pbd.setSubmarine(rsc.isSubmarine());
            pbd.setTardisID(id);
            pbd.setBiome(rsc.getBiome());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getPresetBuilder().buildPreset(pbd);
                }
            }, 10L);
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("siege_on", 0);
            HashMap<String, Object> wheres = new HashMap<String, Object>();
            wheres.put("tardis_id", id);
            // update the database
            qf.doUpdate("tardis", set, wheres);
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                plugin.getTrackerKeeper().getInSiegeMode().remove(Integer.valueOf(id));
            }
            if (plugin.getConfig().getBoolean("siege.texture")) {
                new TARDISSiegeMode(plugin).changeTextures(tardis.getUuid().toString(), tardis.getSchematic(), p, false);
            }
            TARDISMessage.send(p, "SIEGE_OFF");
        }
    }

    @SuppressWarnings("deprecation")
    private boolean isSiegeCube(ItemStack is) {
//        return (is.getType().equals(Material.HUGE_MUSHROOM_1) && is.getData().getData() == (byte) 14);
        return is.getType().equals(Material.HUGE_MUSHROOM_1);
    }

    @SuppressWarnings("deprecation")
    private boolean isSiegeCube(Block b) {
        return (b.getType().equals(Material.HUGE_MUSHROOM_1) && b.getData() == (byte) 14);
    }

    private boolean hasSiegeCubeName(ItemStack is) {
        return (is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("TARDIS Siege Cube"));
    }
}
