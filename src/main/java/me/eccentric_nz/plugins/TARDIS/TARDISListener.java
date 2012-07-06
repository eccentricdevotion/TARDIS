package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TARDISListener implements Listener {

    private TARDIS plugin;

    public TARDISListener(TARDIS plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerBlockPlace(BlockPlaceEvent event) {

        Block block = event.getBlockPlaced();
        // only listen for redstone torches
        if (block.getType() == Material.REDSTONE_TORCH_ON) {
            Block blockBelow = block.getRelative(BlockFace.DOWN);
            Block blockBottom = blockBelow.getRelative(BlockFace.DOWN);
            // only continue if the redstone torch is placed on top of a LAPIS_BLOCK on top of an IRON_BLOCK
            if (blockBelow.getType() == Material.LAPIS_BLOCK && blockBottom.getType() == Material.IRON_BLOCK) {
                Player player = event.getPlayer();
                // check to see if they already have a TARDIS
                if (!plugin.timelords.contains(player.getName())) {
                    float pyaw = player.getLocation().getYaw();
                    if (pyaw >= 0) {
                        pyaw = (pyaw % 360);
                    } else {
                        pyaw = (360 + (pyaw % 360));
                    }
                    Location block_loc = blockBottom.getLocation();
                    String configPath = player.getName();
                    // turn the block stack into a TARDIS
                    Constants.buildOuterTARDIS(player, block_loc, pyaw);
                    // determine direction player is facing
                    String d = "";
                    if (pyaw >= 315 || pyaw < 45) {
                        d = "NORTH";
                    }
                    if (pyaw >= 225 && pyaw < 315) {
                        d = "WEST";
                    }
                    if (pyaw >= 135 && pyaw < 225) {
                        d = "SOUTH";
                    }
                    if (pyaw >= 45 && pyaw < 135) {
                        d = "EAST";
                    }
                    Schematic s = new Schematic(plugin);
                    s.buildInnerTARDIS(plugin.schematic, player, block_loc, Constants.COMPASS.valueOf(d));
                    // save data to timelords file
                    plugin.timelords.set(configPath + ".home", block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ());
                    plugin.timelords.set(configPath + ".save", block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ());
                    plugin.timelords.set(configPath + ".direction", d);
                    try {
                        plugin.timelords.save(plugin.timelordsfile);
                    } catch (IOException io) {
                        System.err.println(Constants.MY_PLUGIN_NAME + " Could not save the time lords file!");
                    }
                } else {
                    String leftLoc = plugin.timelords.getString(player.getName() + ".save");
                    String[] leftData = leftLoc.split(":");
                    player.sendMessage("You already have a TARDIS, you left it in " + leftData[0] + " at x:" + leftData[1] + " y:" + leftData[2] + " z:" + leftData[3]);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        int savedx = 0, savedy = 0, savedz = 0;

        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Material blockType = block.getType();
            ItemStack stack = player.getItemInHand();
            Material material = stack.getType();
            player.sendMessage("The material ID is: " + blockType.getId() + ", the data value is: " + block.getData()); // returns 71 if an IRON_DOOR_BLOCK
            if (blockType == Material.IRON_DOOR_BLOCK) {
                if (material == Material.REDSTONE_TORCH_ON) {
                    player.sendMessage("You clicked the iron door with a redstone torch.");
                    if (block != null) {
                        Location block_loc = block.getLocation();
                        System.out.println("block_loc: " + block_loc);
                        String configPath = player.getName();
                        plugin.timelords = YamlConfiguration.loadConfiguration(plugin.timelordsfile);
                        if (player.hasPermission("TARDIS.enter")) {
                            if (plugin.timelords.contains(configPath)) {
                                String d = plugin.timelords.getString(configPath + ".direction");
                                float yaw = player.getLocation().getYaw();
                                float pitch = player.getLocation().getPitch();
                                // get last known BLUEBOX location
                                Location door_loc = Constants.getLocationFromFile(player.getName(), "save", yaw, pitch, plugin.timelords);
                                // get INNER TARDIS location
                                Location tardis_loc = Constants.getLocationFromFile(player.getName(), "home", yaw, pitch, plugin.timelords);
                                tardis_loc.setY(19);
                                //System.out.println("tardis_loc: " + tardis_loc);
                                World w = player.getWorld();
                                double x = door_loc.getX();
                                double z = door_loc.getZ();
                                switch (Constants.COMPASS.valueOf(d)) {
                                    case NORTH:
                                        door_loc.setZ(z - 1);
                                        break;
                                    case EAST:
                                        door_loc.setX(x + 1);
                                        break;
                                    case SOUTH:
                                        door_loc.setZ(z + 1);
                                        break;
                                    case WEST:
                                        door_loc.setX(x - 1);
                                        break;
                                }
                                System.out.println("door_loc: " + door_loc);
                                if (plugin.PlayerTARDISMap.containsKey(configPath)) {
                                    // player is in the TARDIS
                                    // get location from timelords file
                                    Location exitTardis = Constants.getLocationFromFile(player.getName(), "save", yaw, pitch, plugin.timelords);
                                    // make location safe ie. outside of the bluebox
                                    double ex = exitTardis.getX();
                                    double ez = exitTardis.getZ();
                                    double ey = exitTardis.getY();
                                    switch (Constants.COMPASS.valueOf(d)) {
                                        case NORTH:
                                            exitTardis.setZ(ez - 2);
                                            break;
                                        case EAST:
                                            exitTardis.setX(ex + 2);
                                            break;
                                        case SOUTH:
                                            exitTardis.setZ(ez + 2);
                                            break;
                                        case WEST:
                                            exitTardis.setX(ex - 2);
                                            break;
                                    }
                                    exitTardis.setY(ey - 2);
                                    System.out.println("exitTardis: " + exitTardis);
                                    w.getChunkAt(exitTardis).load();
                                    // exit TARDIS!
                                    player.teleport(exitTardis);
                                    plugin.PlayerTARDISMap.remove(player.getName());
                                } else {
                                    // needs to be either top or bottom of door! This is the current bluebox door location
                                    if (block_loc.getBlockX() == door_loc.getBlockX() && block_loc.getBlockZ() == door_loc.getBlockZ() && (block_loc.getBlockY() == door_loc.getBlockY() || block_loc.getBlockY() == door_loc.getBlockY() - 1)) {
                                        // enter TARDIS!
                                        w.getChunkAt(tardis_loc).load();
                                        player.teleport(tardis_loc);
                                        plugin.PlayerTARDISMap.put(player.getName(), Boolean.valueOf("true"));
                                    } else {
                                        player.sendMessage(Constants.NOT_OWNER);
                                    }
                                }
                            } else {
                                player.sendMessage(Constants.NO_TARDIS);
                            }
                        } else {
                            player.sendMessage(Constants.NO_PERMS_MESSAGE);
                        }
                    } else {
                        System.err.println(Constants.MY_PLUGIN_NAME + "Could not get block");
                    }
                } else {
                    player.sendMessage(Constants.WRONG_MATERIAL + " You have a " + material + " in our hand!");
                }
            }
        }
    }
}