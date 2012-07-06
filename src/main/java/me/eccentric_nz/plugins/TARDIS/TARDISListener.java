package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import org.bukkit.Bukkit;
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
                    plugin.timelords.set(configPath + ".location.world", block_loc.getWorld().getName());
                    plugin.timelords.set(configPath + ".location.x", block_loc.getBlockX());
                    plugin.timelords.set(configPath + ".location.uppery", block_loc.getBlockY());
                    plugin.timelords.set(configPath + ".location.lowery", 19);
                    plugin.timelords.set(configPath + ".location.z", block_loc.getBlockZ());
                    plugin.timelords.set(configPath + ".direction", d);
                    try {
                        plugin.timelords.save(plugin.timelordsfile);
                    } catch (IOException io) {
                        System.err.println("[TARDIS] Could not save the time lords file!");
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
                        //System.out.println("block_loc: " + block_loc);
                        String configPath = player.getName();
                        plugin.timelords = YamlConfiguration.loadConfiguration(plugin.timelordsfile);

                        if (player.hasPermission("TARDIS.enter")) {
                            if (plugin.timelords.contains(configPath)) {
                                // check location
                                String tworld = plugin.timelords.getString(configPath + ".location.world");
                                World w = Bukkit.getServer().getWorld(tworld);
                                int x = plugin.timelords.getInt(configPath + ".location.x");
                                int tpx = x;
                                int y = plugin.timelords.getInt(configPath + ".location.uppery");
                                int ly = plugin.timelords.getInt(configPath + ".location.lowery");
                                int z = plugin.timelords.getInt(configPath + ".location.z");
                                int tpz = z;
                                String d = plugin.timelords.getString(configPath + ".direction");
                                switch (Constants.COMPASS.valueOf(d)) {
                                    case NORTH:
                                        z = z - 1;
                                        break;
                                    case EAST:
                                        x = x + 1;
                                        break;
                                    case SOUTH:
                                        z = z + 1;
                                        break;
                                    case WEST:
                                        x = x - 1;
                                        break;
                                }
                                Location bluebox_loc = new Location(w, x, y - 1, z);
                                //System.out.println("bluebox_loc: " + bluebox_loc);
                                if (block_loc.getBlockX() == bluebox_loc.getBlockX() && block_loc.getBlockZ() == bluebox_loc.getBlockZ()) {
                                    // get player location, yaw and pitch
                                    int px = player.getLocation().getBlockX();
                                    int py = player.getLocation().getBlockY();
                                    int pz = player.getLocation().getBlockZ();
                                    float yaw = player.getLocation().getYaw();
                                    float pitch = player.getLocation().getPitch();
                                    Location tardis_loc = new Location(w, tpx, ly, tpz, yaw, pitch);
                                    //System.out.println("tardis_loc: " + tardis_loc);
                                    // needs to be either top or bottom of door! This is the inside tardis location
                                    if (block_loc.getBlockY() == tardis_loc.getBlockY() || block_loc.getBlockY() == tardis_loc.getBlockY() + 1) {
                                        // get location from hash map
                                        String saved_loc = plugin.timelords.getString(player.getName() + ".save");
                                        String[] data = saved_loc.split(":");
                                        //System.out.println("saved world: " + data[0]);
                                        World savedw = Bukkit.getServer().getWorld(data[0]);
                                        try {
                                            savedx = Integer.parseInt(data[1]);
                                            savedy = Integer.parseInt(data[2]);
                                            savedz = Integer.parseInt(data[3]);
                                        } catch (NumberFormatException n) {
                                            System.err.println("Could not convert to number");
                                        }
                                        Location exitTardis = new Location(savedw, savedx, savedy, savedz, yaw, pitch);
                                       // System.out.println("exitTardis: " + exitTardis);
                                        w.getChunkAt(exitTardis).load();
                                        // exit TARDIS!
                                        player.teleport(exitTardis);
                                    } else {
                                        // needs to be either top or bottom of door! This is the bluebox location
                                        if (block_loc.getBlockY() == bluebox_loc.getBlockY() || block_loc.getBlockY() == bluebox_loc.getBlockY() - 1) {
                                            plugin.timelords.set(player.getName() + ".save", w.getName() + ":" + px + ":" + py + ":" + pz);
                                            try {
                                                plugin.timelords.save(plugin.timelordsfile);
                                            } catch (IOException io) {
                                                System.err.println("[TARDIS] Could not save the time lords file!");
                                            }
                                            // enter TARDIS!
                                            //Location enterTardis = new Location(w, tpx, ly, tpz, yaw, pitch);
                                            //w.getChunkAt(enterTardis).load();
                                            //player.teleport(enterTardis);
                                            w.getChunkAt(tardis_loc).load();
                                            player.teleport(tardis_loc);
                                        } else {
                                            player.sendMessage(Constants.NOT_OWNER);
                                        }
                                    }
                                }
                            } else {
                                player.sendMessage(Constants.NOT_OWNER);
                            }
                        } else {
                            player.sendMessage(Constants.NO_PERMS_MESSAGE);
                        }
                    } else {
                        System.err.println("Could not get block");
                    }
                } else {
                    player.sendMessage(Constants.WRONG_MATERIAL + " You have a " + material + " in our hand!");
                }
            }
        }
    }
}