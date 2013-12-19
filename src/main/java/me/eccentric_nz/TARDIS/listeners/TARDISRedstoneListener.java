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
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.material.PistonBaseMaterial;
import org.bukkit.material.PistonExtensionMaterial;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRedstoneListener implements Listener {

    private final TARDIS plugin;
    private final List<String> wires = new ArrayList<String>();
    private final List<String> lamps = new ArrayList<String>();
    private final List<String> rails = new ArrayList<String>();
    private final List<String> pistons = new ArrayList<String>();

    public TARDISRedstoneListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRedstoneChange(BlockRedstoneEvent event) {
        plugin.debug("Redstone changed block: " + event.getBlock().getType().toString());
        final String block = event.getBlock().getLocation().toString();
        if (wires.contains(block) || lamps.contains(block) || rails.contains(block)) {
            event.setNewCurrent(event.getOldCurrent());
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        final String block = event.getBlock().getLocation().toString();
        if (pistons.contains(block)) {
            event.setCancelled(true);
            setExtension(event.getBlock());
        }
    }

    public List<String> getWires() {
        return wires;
    }

    public List<String> getLamps() {
        return lamps;
    }

    public List<String> getRails() {
        return rails;
    }

    public List<String> getPistons() {
        return pistons;
    }

    public void setExtension(Block b) {
        Block l = b.getRelative(((PistonBaseMaterial) b.getState().getData()).getFacing());
        l.setType(Material.PISTON_EXTENSION);
        if (b.getType().equals(Material.PISTON_BASE)) {
            l.setData((byte) (b.getData() - 8));
        } else {
            l.setData((byte) b.getData());
        }
        PistonExtensionMaterial extension = (PistonExtensionMaterial) l.getState().getData();
        l.setData(extension.getData());
        l.getState().update();
    }
}
