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
package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.interaction.ArtronRightClick;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Ninth Doctor used the Cardiff rift to "re-charge" his TARDIS. The process took 2 days.
 *
 * @author eccentric_nz
 */
public class TARDISArtronCapacitorListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> validBlocks = new ArrayList<>();

    public TARDISArtronCapacitorListener(TARDIS plugin) {
        this.plugin = plugin;
        validBlocks.addAll(Tag.BUTTONS.getValues());
        validBlocks.add(Material.COMPARATOR);
        validBlocks.add(Material.LEVER);
    }

    /**
     * Listens for player interaction with the button on the Artron Energy Capacitor. If the button is right-clicked,
     * then the Artron levels are updated. Clicking with a Nether Star puts the capacitor to maximum, clicking with the
     * TARDIS key initialises the capacitor by spawning a charged creeper inside it and sets the level to 50%. Clicking
     * while sneaking transfers player Artron Energy into the capacitor.
     * <p>
     * If the button is just right-clicked, it displays the current capacitor level as percentage of full.
     *
     * @param event the player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCapacitorInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        if (block != null) {
            Material blockType = block.getType();
            Action action = event.getAction();
            // only proceed if they are clicking a button!
            if (validBlocks.contains(blockType)) {
                // we need to get this block's location and then get the tardis_id from it
                String buttonloc = block.getLocation().toString();
                HashMap<String, Object> where = new HashMap<>();
                where.put("type", 6);
                where.put("location", buttonloc);
                ResultSetControls rsc = new ResultSetControls(plugin, where, false);
                if (rsc.resultSet()) {
                    int id = rsc.getTardis_id();
                    if (action == Action.RIGHT_CLICK_BLOCK) {
                        new ArtronRightClick(plugin).process(id, player, block.getLocation());
                    } else if (action == Action.LEFT_CLICK_BLOCK && player.isSneaking()) {
                        event.setCancelled(true);
                        // check if the charged creeper in the TARDIS Artron Energy Capacitor is still there
                        new TARDISCreeperChecker(plugin, id).checkCreeper();
                    }
                }
            }
        }
    }
}
