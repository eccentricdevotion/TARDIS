/*
 * Copyright (C) 2018 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * The time rotor is a component in the central column of the TARDIS console. While the TARDIS is in flight, the rotor
 * rises and falls, stopping when the TARDIS has reached a destination. It is associated with the 'whooshing' noise
 * heard when the TARDIS is in flight.
 *
 * @author eccentric_nz
 */
public class TARDISBeaconColouringListener implements Listener {

    private final TARDIS plugin;

    public TARDISBeaconColouringListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!plugin.getTrackerKeeper().getBeaconColouring().contains(uuid)) {
            return;
        }
        ItemStack dye = player.getInventory().getItemInMainHand();
        if (dye == null || !TARDISMaterials.dyes.contains(player.getInventory().getItemInMainHand().getType())) {
            TARDISMessage.send(player, "COLOUR_DYE");
            return;
        }
        if (!TARDISMaterials.glass.contains(event.getClickedBlock().getType())) {
            TARDISMessage.send(player, "COLOUR_GLASS");
            return;
        }
        Block b = event.getClickedBlock();
        Material original = b.getType();
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
        int amount = dye.getAmount();
        // don't do anything if it is the same colour
        switch (original) {
            case BLACK_STAINED_GLASS:
            case BLACK_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.INK_SAC)) {
                    return;
                }
                break;
            case BLUE_STAINED_GLASS:
            case BLUE_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.LAPIS_LAZULI)) {
                    return;
                }
                break;
            case BROWN_STAINED_GLASS:
            case BROWN_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.COCOA_BEANS)) {
                    return;
                }
                break;
            case CYAN_STAINED_GLASS:
            case CYAN_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.CYAN_DYE)) {
                    return;
                }
                break;
            case GRAY_STAINED_GLASS:
            case GRAY_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.GRAY_DYE)) {
                    return;
                }
                break;
            case GREEN_STAINED_GLASS:
            case GREEN_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.CACTUS_GREEN)) {
                    return;
                }
                break;
            case LIGHT_BLUE_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.LIGHT_BLUE_DYE)) {
                    return;
                }
                break;
            case LIGHT_GRAY_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.LIGHT_GRAY_DYE)) {
                    return;
                }
                break;
            case LIME_STAINED_GLASS:
            case LIME_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.LIME_DYE)) {
                    return;
                }
                break;
            case MAGENTA_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.MAGENTA_DYE)) {
                    return;
                }
                break;
            case ORANGE_STAINED_GLASS:
            case ORANGE_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.ORANGE_DYE)) {
                    return;
                }
                break;
            case PINK_STAINED_GLASS:
            case PINK_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.PINK_DYE)) {
                    return;
                }
                break;
            case PURPLE_STAINED_GLASS:
            case PURPLE_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.PURPLE_DYE)) {
                    return;
                }
                break;
            case RED_STAINED_GLASS:
            case RED_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.ROSE_RED)) {
                    return;
                }
                break;
            case WHITE_STAINED_GLASS:
            case WHITE_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.BONE_MEAL)) {
                    return;
                }
                break;
            case YELLOW_STAINED_GLASS:
            case YELLOW_STAINED_GLASS_PANE:
                if (dye.getType().equals(Material.DANDELION_YELLOW)) {
                    return;
                }
                break;
            default:
                break;
        }
        int u = 1;
        List<Block> candidates = new ArrayList<>();
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
            player.getInventory().removeItem(new ItemStack(dye.getType(), needed));
        }
        player.updateInventory();
        candidates.forEach((bb) -> changeColour(bb, dye));
        // take the Artron Energy
        HashMap<String, Object> wherea = new HashMap<>();
        wherea.put("uuid", uuid.toString());
        int energy = plugin.getCondensables().get("GLASS") * needed;
        new QueryFactory(plugin).alterEnergyLevel("tardis", -energy, wherea, player);
        plugin.getTrackerKeeper().getBeaconColouring().remove(uuid);
    }

    private void changeColour(Block block, ItemStack dye) {
        // determine colour
        String[] split = block.getType().toString().split("_");
        if (split.length == 1) {
            split = new String[]{"", "STAINED", "GLASS"};
        } else if (split.length == 2) {
            split = new String[]{"", "STAINED", "GLASS", "PANE"};
        }
        switch (dye.getType()) {
            case BONE_MEAL:
                split[0] = "WHITE";
                break;
            case CACTUS_GREEN:
                split[0] = "GREEN";
                break;
            case COCOA_BEANS:
                split[0] = "BROWN";
                break;
            case CYAN_DYE:
                split[0] = "CYAN";
                break;
            case DANDELION_YELLOW:
                split[0] = "YELLOW";
                break;
            case GRAY_DYE:
                split[0] = "GRAY";
                break;
            case INK_SAC:
                split[0] = "BLACK";
                break;
            case LAPIS_LAZULI:
                split[0] = "BLUE";
                break;
            case LIGHT_BLUE_DYE:
                split[0] = "LIGHT_BLUE";
                break;
            case LIGHT_GRAY_DYE:
                split[0] = "LIGHT_GRAY";
                break;
            case LIME_DYE:
                split[0] = "LIME";
                break;
            case MAGENTA_DYE:
                split[0] = "MAGENTA";
                break;
            case ORANGE_DYE:
                split[0] = "ORANGE";
                break;
            case PINK_DYE:
                split[0] = "PINK";
                break;
            case PURPLE_DYE:
                split[0] = "PURPLE";
                break;
            case ROSE_RED:
                split[0] = "RED";
                break;
            default:
                break;
        }
        String joined = String.join("_", split);
        BlockData data = Material.valueOf(joined).createBlockData();
        block.setBlockData(data, true);
    }
}
