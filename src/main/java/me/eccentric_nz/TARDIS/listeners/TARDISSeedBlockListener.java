/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants.SCHEMATIC;
import me.eccentric_nz.TARDIS.builders.TARDISBuildData;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISSeedBlockListener implements Listener {

    private final TARDIS plugin;
    private HashMap<Location, TARDISBuildData> trackTARDISSeed = new HashMap<Location, TARDISBuildData>();

    public TARDISSeedBlockListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSeedBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        ItemStack is = player.getItemInHand();
        if (!is.hasItemMeta()) {
            return;
        }
        ItemMeta im = is.getItemMeta();
        if (!im.hasDisplayName() || !im.hasLore()) {
            return;
        }
        if (im.getDisplayName().equals("§6TARDIS Seed Block")) {
            List<String> lore = im.getLore();
            SCHEMATIC schm = SCHEMATIC.valueOf(lore.get(0));
            TwoValues wall_data = getValuesFromString(lore.get(1));
            TwoValues floor_data = getValuesFromString(lore.get(2));
            TwoValues cham_data = getValuesFromString(lore.get(3));
            TwoValues lamp_data = getValuesFromString(lore.get(4));
            TARDISBuildData seed = new TARDISBuildData();
            seed.setSchematic(schm);
            seed.setWall_id(wall_data.getId());
            seed.setWall_data(wall_data.getData());
            seed.setFloor_id(floor_data.getId());
            seed.setFloor_data(floor_data.getData());
            seed.setBox_id(cham_data.getId());
            seed.setBox_data(cham_data.getData());
            seed.setLamp(lamp_data.getId());
//            String playerNameStr = player.getName();
            Location l = event.getBlockPlaced().getLocation();
            trackTARDISSeed.put(l, seed);
            player.sendMessage(plugin.pluginName + "You placed a TARDIS seed block!");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onseedBlockBreak(BlockBreakEvent event) {
        Location l = event.getBlock().getLocation();
        if (trackTARDISSeed.containsKey(l)) {
            // get the Seed block data
            TARDISBuildData data = trackTARDISSeed.get(l);
            // set the sign block to AIR
            event.getBlock().setType(Material.AIR);
            // drop a TARDIS Seed Block
            World w = l.getWorld();
            ItemStack is = new ItemStack(event.getBlock().getType(), 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("§6TARDIS Seed Block");
            List<String> lore = new ArrayList<String>();
            lore.add(data.getSchematic().toString());
            lore.add("Walls: " + ((data.getWall_id() == 35 || data.getWall_id() == 170) ? DyeColor.getByWoolData(data.getWall_data()) + " " : "") + Material.getMaterial(data.getWall_id()).toString());
            lore.add("Floors: " + ((data.getFloor_id() == 35 || data.getFloor_id() == 170) ? DyeColor.getByWoolData(data.getFloor_data()) + " " : "") + Material.getMaterial(data.getFloor_id()).toString());
            lore.add("Chameleon block: " + ((data.getBox_id() == 35 || data.getBox_id() == 170) ? DyeColor.getByWoolData(data.getBox_data()) + " " : "") + Material.getMaterial(data.getBox_id()).toString());
            lore.add("Lamp: " + Material.getMaterial(data.getLamp()).toString());
            im.setLore(lore);
            is.setItemMeta(im);
            w.dropItemNaturally(l, is);
        }
    }

    private TwoValues getValuesFromString(String str) {
        TwoValues data = new TwoValues();
        String[] split1 = str.split(": ");
        String[] split2 = split1[1].split(" ");
        if (split2.length > 1) {
            data.setId(Material.getMaterial(plugin.utils.parseNum(split2[1])).getId());
            data.setData(DyeColor.valueOf(split2[0]).getWoolData());
        } else {
            data.setId(Material.getMaterial(plugin.utils.parseNum(split1[1])).getId());
            data.setData((byte) 0);
        }
        return data;
    }

    public class TwoValues {

        public TwoValues() {
        }
        private int id;
        private byte data;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public byte getData() {
            return data;
        }

        public void setData(byte data) {
            this.data = data;
        }
    }
}
