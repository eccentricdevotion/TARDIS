/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.listeners;

import java.util.*;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.gui.TVMGUI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @author eccentric_nz
 */
public class TVMEquipListener implements Listener {

    private final TARDIS plugin;
    private final HashSet<Material> transparent = new HashSet<>();

    public TVMEquipListener(TARDIS plugin) {
        this.plugin = plugin;
        transparent.add(Material.AIR);
        transparent.add(Material.ALLIUM);
        transparent.add(Material.AZURE_BLUET);
        transparent.add(Material.BLUE_ORCHID);
        transparent.add(Material.BROWN_MUSHROOM);
        transparent.add(Material.DANDELION);
        transparent.add(Material.DEAD_BUSH);
        transparent.add(Material.FERN);
        transparent.add(Material.GRASS);
        transparent.add(Material.LARGE_FERN);
        transparent.add(Material.LILAC);
        transparent.add(Material.ORANGE_TULIP);
        transparent.add(Material.OXEYE_DAISY);
        transparent.add(Material.PEONY);
        transparent.add(Material.PINK_TULIP);
        transparent.add(Material.POPPY);
        transparent.add(Material.REDSTONE_WIRE);
        transparent.add(Material.RED_MUSHROOM);
        transparent.add(Material.RED_TULIP);
        transparent.add(Material.ROSE_BUSH);
        transparent.add(Material.SNOW);
        transparent.add(Material.SUNFLOWER);
        transparent.add(Material.TALL_GRASS);
        transparent.add(Material.VINE);
        transparent.add(Material.WHITE_TULIP);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_AIR) && !action.equals(Action.LEFT_CLICK_AIR)) {
            return;
        }
        Player player = event.getPlayer();
        if (!TARDISPermission.hasPermission(player, "vm.teleport")) {
            return;
        }
        ItemStack is = player.getInventory().getItemInMainHand();
        if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
            // get tachyon level
            TVMResultSetManipulator rs = new TVMResultSetManipulator(plugin, player.getUniqueId().toString());
            if (rs.resultSet()) {
                if (action.equals(Action.RIGHT_CLICK_AIR)) {
                    // open gui
                    ItemStack[] gui = new TVMGUI(plugin, rs.getTachyonLevel()).getGUI();
                    Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "Vortex Manipulator");
                    vmg.setContents(gui);
                    player.openInventory(vmg);
                } else if (action.equals(Action.LEFT_CLICK_AIR) && plugin.getVortexConfig().getBoolean("allow.look_at_block") && TARDISPermission.hasPermission(player, "vm.lookatblock")) {
                    UUID uuid = player.getUniqueId();
                    int maxDistance = plugin.getVortexConfig().getInt("max_look_at_distance");
                    Location bl = player.getTargetBlock(transparent, maxDistance).getLocation();
                    bl.add(0.0d, 1.0d, 0.0d);
                    List<Player> players = new ArrayList<>();
                    players.add(player);
                    if (plugin.getVortexConfig().getBoolean("allow.multiple")) {
                        player.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((e) -> {
                            if (e instanceof Player && !e.getUniqueId().equals(uuid)) {
                                players.add((Player) e);
                            }
                        });
                    }
                    int required = plugin.getVortexConfig().getInt("tachyon_use.travel.to_block");
                    int actual = required * players.size();
                    if (!TVMUtils.checkTachyonLevel(uuid.toString(), actual)) {
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", actual);
                        return;
                    }
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_STANDY");
                    // Random malfunction
                    Random rnd = new Random();
                    if (rnd.nextInt(100) < plugin.getVortexConfig().getInt("block_travel_malfunction_chance")) {
                        plugin.debug(player.getDisplayName() + " has malfunctioned");
                        Parameters params = new Parameters(player, Flag.getAPIFlags());
                        Location _bl = null;
                        // since the TARDIS api is a little funky at times, retry up to ten times if a location isn't found
                        // this will exponentially increase the accuracy of the configured chance
                        int retries = 0;
                        while (_bl == null) {
                            _bl = plugin.getTardisAPI().getRandomLocation(plugin.getTardisAPI().getWorlds(), null, params);
                            retries++;
                            if (retries >= 10) {
                                break;
                            }
                        }
                        // check to ensure we have a valid alternate location before triggering the malfunction
                        // for this reason the actual malfunction rate may be lower than configured
                        if (_bl != null) {
                            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_MALFUNCTION");
                            bl = _bl;
                        }
                    }
                    TVMUtils.movePlayers(players, bl, player.getLocation().getWorld());
                    // remove tachyons
                    new TVMQueryFactory(plugin).alterTachyons(uuid.toString(), -actual);
                }
            }
        }
    }
}
