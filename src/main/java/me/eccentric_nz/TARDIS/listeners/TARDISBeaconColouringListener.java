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
package me.eccentric_nz.TARDIS.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * The time rotor is a component in the central column of the TARDIS console.
 * While the TARDIS is in flight, the rotor rises and falls, stopping when the
 * TARDIS has reached a destination. It is associated with the 'whooshing' noise
 * heard when the TARDIS is in flight.
 *
 * @author eccentric_nz
 */
public class TARDISBeaconColouringListener implements Listener {

    private final TARDIS plugin;
    private final List<Material> glass = new ArrayList<Material>();

    public TARDISBeaconColouringListener(TARDIS plugin) {
        this.plugin = plugin;
        this.glass.add(Material.GLASS);
        this.glass.add(Material.STAINED_GLASS);
        this.glass.add(Material.STAINED_GLASS_PANE);
        this.glass.add(Material.THIN_GLASS);
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        final Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!plugin.getTrackerKeeper().getBeaconColouring().contains(uuid)) {
            return;
        }
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.INK_SACK)) {
            TARDISMessage.send(player, "COLOUR_DYE");
            return;
        }
        if (!glass.contains(event.getClickedBlock().getType())) {
            TARDISMessage.send(player, "COLOUR_GLASS");
            return;
        }
        Block b = event.getClickedBlock();
        Material original = b.getType();
        byte block_data = b.getData();
        // check there is a beacon block below the clicked glass block
        boolean found = false;
        for (int i = 1; i < b.getLocation().getBlockY() - 63; i++) {
            Material mat = b.getRelative(BlockFace.DOWN, i).getType();
            if (mat.equals(Material.BEACON)) {
                found = true;
                break;
            }
        }
        if (!found) {
            TARDISMessage.send(player, "COLOUR_BEACON");
            plugin.getTrackerKeeper().getBeaconColouring().remove(uuid);
            return;
        }
        ItemStack dye = player.getInventory().getItemInMainHand();
        int amount = dye.getAmount();
        byte dye_data = dye.getData().getData();
        byte new_data = (byte) (15 - dye_data);
        // don't do anything if it is the same colour
        if (new_data == block_data) {
            plugin.getTrackerKeeper().getBeaconColouring().remove(uuid);
            return;
        }
        int u = 1;
        Material change;
        switch (original) {
            case GLASS:
                change = Material.STAINED_GLASS;
                break;
            case THIN_GLASS:
                change = Material.STAINED_GLASS_PANE;
                break;
            default:
                change = original;
                break;
        }
        List<Block> candidates = new ArrayList<Block>();
        candidates.add(b);
        while (b.getRelative(BlockFace.UP, u).getType().equals(original)) {
            candidates.add(b.getRelative(BlockFace.UP, u));
            u++;
        }
        int needed = candidates.size();
        if (amount < needed) {
            TARDISMessage.send(player, "COLOUR_NOT_ENOUGH", String.format("%d", needed));
            plugin.getTrackerKeeper().getBeaconColouring().remove(uuid);
            return;
        }
        if (amount > needed) {
            player.getInventory().getItemInMainHand().setAmount(amount - needed);
        } else {
            player.getInventory().removeItem(new ItemStack(dye.getType(), needed, dye.getDurability()));
        }
        player.updateInventory();
        for (Block bb : candidates) {
            bb.setType(change);
            bb.setData(new_data, true);
        }
        // take the Artron Energy
        HashMap<String, Object> wherea = new HashMap<String, Object>();
        wherea.put("uuid", uuid.toString());
        int energy = plugin.getCondensables().get("GLASS") * needed;
        new QueryFactory(plugin).alterEnergyLevel("tardis", -energy, wherea, player);
        plugin.getTrackerKeeper().getBeaconColouring().remove(uuid);
    }
}
