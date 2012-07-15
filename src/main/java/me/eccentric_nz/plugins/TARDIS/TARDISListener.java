package me.eccentric_nz.plugins.TARDIS;

import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.getspout.spoutapi.SpoutManager;

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
                String configPath = player.getName();
                // check to see if they already have a TARDIS
                if (!plugin.timelords.contains(configPath)) {
                    float pyaw = player.getLocation().getYaw();
                    if (pyaw >= 0) {
                        pyaw = (pyaw % 360);
                    } else {
                        pyaw = (360 + (pyaw % 360));
                    }
                    Location block_loc = blockBottom.getLocation();
                    // turn the block stack into a TARDIS
                    Constants c = new Constants(plugin);
                    c.buildOuterTARDIS(player, block_loc, pyaw);
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
                    String leftLoc = plugin.timelords.getString(configPath + ".save");
                    String[] leftData = leftLoc.split(":");
                    player.sendMessage("You already have a TARDIS, you left it in " + leftData[0] + " at x:" + leftData[1] + " y:" + leftData[2] + " z:" + leftData[3]);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent event) {
        int signx = 0, signz = 0;
        Player player = event.getPlayer();
        String configPath = player.getName();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        Block block = event.getBlock();
        Material blockType = block.getType();
        if (blockType == Material.WALL_SIGN && plugin.timelords.contains(configPath + ".direction")) {
            // check the sign location
            Location sign_loc = block.getLocation();
            Location bb_loc = Constants.getLocationFromFile(configPath, "save", yaw, pitch, plugin.timelords);
            // get TARDIS direction
            Constants.COMPASS d = Constants.COMPASS.valueOf(plugin.timelords.getString(configPath + ".direction"));
            switch (d) {
                case EAST:
                    signx = 2;
                    signz = 0;
                    break;
                case SOUTH:
                    signx = 0;
                    signz = 2;
                    break;
                case WEST:
                    signx = -2;
                    signz = 0;
                    break;
                case NORTH:
                    signx = 0;
                    signz = -2;
                    break;
            }
            // if the sign was on the TARDIS destroy the TARDIS!
            if (sign_loc.getBlockX() == bb_loc.getBlockX() + signx && sign_loc.getBlockY() == bb_loc.getBlockY() && sign_loc.getBlockZ() == bb_loc.getBlockZ() + signz) {
                // don't drop the sign
                //block.getDrops().clear();
                Schematic s = new Schematic(plugin);
                // clear the torch
                s.destroyTorch(bb_loc);
                s.destroySign(bb_loc, d);
                s.destroyTARDIS(player, bb_loc, d);
            } else {
                // cancel the event because it's not the player's TARDIS
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        String configPath = player.getName();
        int savedx = 0, savedy = 0, savedz = 0;

        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Material blockType = block.getType();
            ItemStack stack = player.getItemInHand();
            Material material = stack.getType();
            //player.sendMessage("The material ID is: " + blockType.getId() + ", the data value is: " + block.getData()); // returns 71 if an IRON_DOOR_BLOCK
            if (blockType == Material.IRON_DOOR_BLOCK) {
                if (material == Material.REDSTONE_TORCH_ON) {
                    if (block != null) {
                        Location block_loc = block.getLocation();
                        //plugin.timelords = YamlConfiguration.loadConfiguration(plugin.timelordsfile);
                        if (player.hasPermission("TARDIS.enter")) {
                            if (plugin.timelords.contains(configPath + ".direction")) {
                                String d = plugin.timelords.getString(configPath + ".direction");
                                float yaw = player.getLocation().getYaw();
                                float pitch = player.getLocation().getPitch();
                                // get last known BLUEBOX location
                                Location door_loc = Constants.getLocationFromFile(configPath, "save", yaw, pitch, plugin.timelords);
                                // get INNER TARDIS location
                                Location tardis_loc = Constants.getLocationFromFile(configPath, "home", yaw, pitch, plugin.timelords);
                                tardis_loc.setY(19.5);
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
                                if (plugin.timelords.getBoolean(configPath + ".travelling") == Boolean.valueOf("true")) {
                                    // player is in the TARDIS
                                    // get location from timelords file
                                    Location exitTardis = Constants.getLocationFromFile(configPath, "save", yaw, pitch, plugin.timelords);
                                    // should only get this message if exiting the TARDIS
                                    //player.sendMessage("Exiting the TARDIS");
                                    // make location safe ie. outside of the bluebox
                                    double ex = exitTardis.getX();
                                    double ez = exitTardis.getZ();
                                    double ey = exitTardis.getY();
                                    float pyaw = 0;
                                    switch (Constants.COMPASS.valueOf(d)) {
                                        case NORTH:
                                            exitTardis.setZ(ez - 2);
                                            pyaw = 0;
                                            break;
                                        case EAST:
                                            exitTardis.setX(ex + 2);
                                            pyaw = 90;
                                            break;
                                        case SOUTH:
                                            exitTardis.setZ(ez + 2);
                                            pyaw = 180;
                                            break;
                                        case WEST:
                                            exitTardis.setX(ex - 2);
                                            pyaw = 270;
                                            break;
                                    }
                                    exitTardis.setY(ey - 0.5);
                                    // destroy current TARDIS location
                                    String sl = plugin.timelords.getString(configPath + ".save");
                                    String cl = plugin.timelords.getString(configPath + ".current");
                                    Boolean rebuild = false;
                                    Location newl = null;
                                    if (!sl.equals(cl)) {
                                        Location l = Constants.getLocationFromFile(configPath, "current", 0, 0, plugin.timelords);
                                        newl = Constants.getLocationFromFile(configPath, "save", 0, 0, plugin.timelords);
                                        double old_y = newl.getY();
                                        newl.setY(old_y - 2);
                                        Schematic s = new Schematic(plugin);
                                        // remove torch
                                        s.destroyTorch(l);
                                        // remove sign
                                        s.destroySign(l, Constants.COMPASS.valueOf(d));
                                        // remove blue box
                                        s.destroyBlueBox(l, w, Constants.COMPASS.valueOf(d));
                                        //rebuild = true;
                                    }
                                    // try preloading destination chunk
                                    w.getChunkAt(exitTardis).load();
                                    w.getChunkAt(exitTardis).load(true);
                                    while (!w.getChunkAt(exitTardis).isLoaded()) {
                                        w.getChunkAt(exitTardis).load();
                                    }
                                    // rebuild blue box
                                    if (newl != null) {
                                        Constants c = new Constants(plugin);
                                        c.buildOuterTARDIS(player, newl, pyaw);
                                    }
                                    // exit TARDIS!
                                    player.teleport(exitTardis);
                                    plugin.timelords.set(configPath + ".travelling", false);
                                    try {
                                        plugin.timelords.save(plugin.timelordsfile);
                                    } catch (IOException io) {
                                        System.err.println(Constants.MY_PLUGIN_NAME + " could not save timelords file!");
                                    }
                                } else {
                                    // needs to be either top or bottom of door! This is the current bluebox door location
                                    if (block_loc.getBlockX() == door_loc.getBlockX() && block_loc.getBlockZ() == door_loc.getBlockZ() && (block_loc.getBlockY() == door_loc.getBlockY() - 2 || block_loc.getBlockY() == door_loc.getBlockY() - 1)) {
                                        // should only get this message if entering the TARDIS
                                        //player.sendMessage("Entering the TARDIS");
                                        // enter TARDIS!
                                        w.getChunkAt(tardis_loc).load();
                                        player.teleport(tardis_loc);
                                        plugin.timelords.set(configPath + ".travelling", true);
                                        // update current TARDIS location
                                        plugin.timelords.set(configPath + ".current", plugin.timelords.getString(configPath + ".save"));
                                        try {
                                            plugin.timelords.save(plugin.timelordsfile);
                                        } catch (IOException io) {
                                            System.err.println(Constants.MY_PLUGIN_NAME + " could not save timelords file!");
                                        }
                                        if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled()) {
                                            SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_hum.mp3", false, tardis_loc, 9, 25);
                                        }
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
            if (blockType == Material.STONE_BUTTON && plugin.timelords.getBoolean(configPath + ".travelling") == Boolean.valueOf("true")) {
                // get saved button location
                Location pp = Constants.getLocationFromFile(configPath, "button", 0, 0, plugin.timelords);
                // get clicked block location
                Location b = block.getLocation();
                if (b.getBlockX() == pp.getBlockX() && b.getBlockY() == pp.getBlockY() && b.getBlockZ() == pp.getBlockZ()) {
                    // get repeater settings
                    Location r0_loc = Constants.getLocationFromFile(configPath, "repeater0", 0, 0, plugin.timelords);
                    Block r0 = r0_loc.getBlock();
                    byte r0_data = r0.getData();
                    Location r1_loc = Constants.getLocationFromFile(configPath, "repeater1", 0, 0, plugin.timelords);
                    Block r1 = r1_loc.getBlock();
                    byte r1_data = r1.getData();
                    Location r2_loc = Constants.getLocationFromFile(configPath, "repeater2", 0, 0, plugin.timelords);
                    Block r2 = r2_loc.getBlock();
                    byte r2_data = r2.getData();
                    Location r3_loc = Constants.getLocationFromFile(configPath, "repeater3", 0, 0, plugin.timelords);
                    Block r3 = r3_loc.getBlock();
                    byte r3_data = r3.getData();
                    boolean playSound = true;
                    //player.sendMessage("0:" + r0_data + ", 1:" + r1_data + ", 2:" + r2_data + ", 3:" + r3_data);
                    if (r0_data <= 3 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // first position
                        player.sendMessage("Home destination selected!");
                        // always teleport to home location
                        String d = plugin.timelords.getString(configPath + ".home");
                        plugin.timelords.set(configPath + ".save", d);
                    }
                    if (r0_data >= 4 && r0_data <= 7 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // second position
                        if (plugin.timelords.contains(configPath + ".dest1")) {
                            String d = plugin.timelords.getString(configPath + ".dest1.location");
                            String n = plugin.timelords.getString(configPath + ".dest1.name");
                            plugin.timelords.set(configPath + ".save", d);
                            player.sendMessage("Destination 1 [" + n + "] selected!");
                        } else {
                            player.sendMessage("There is no destination saved to slot 1!");
                            playSound = false;
                        }
                    }
                    if (r0_data >= 8 && r0_data <= 11 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // third position
                        if (plugin.timelords.contains(configPath + ".dest2")) {
                            String d = plugin.timelords.getString(configPath + ".dest2.location");
                            String n = plugin.timelords.getString(configPath + ".dest2.name");
                            plugin.timelords.set(configPath + ".save", d);
                            player.sendMessage("Destination 2 [" + n + "] selected!");
                        } else {
                            player.sendMessage("There is no destination saved to slot 2!");
                            playSound = false;
                        }
                    }
                    if (r0_data >= 12 && r0_data <= 15 && r1_data <= 3 && r2_data <= 3 && r3_data <= 3) { // last position
                        if (plugin.timelords.contains(configPath + ".dest3")) {
                            String d = plugin.timelords.getString(configPath + ".dest3.location");
                            String n = plugin.timelords.getString(configPath + ".dest3.name");
                            plugin.timelords.set(configPath + ".save", d);
                            player.sendMessage("Destination 3 [" + n + "] selected!");
                        } else {
                            player.sendMessage("There is no destination saved to slot 3!");
                            playSound = false;
                        }
                    }
                    if (r1_data > 3 || r2_data > 3 || r3_data > 3) {
                        // create a random destination
                        TARDISTimetravel tt = new TARDISTimetravel(plugin);
                        Location rand = tt.randomDestination(player, player.getWorld(), r1_data, r2_data, r3_data);
                        String d = rand.getWorld().getName() + ":" + rand.getBlockX() + ":" + rand.getBlockY() + ":" + rand.getBlockZ();
                        //player.sendMessage(d);
                        plugin.timelords.set(configPath + ".save", d);
                    }
                    try {
                        plugin.timelords.save(plugin.timelordsfile);
                    } catch (IOException io) {
                        System.err.println(Constants.MY_PLUGIN_NAME + " Could not save timelords file!");
                    }
                    if (plugin.getServer().getPluginManager().getPlugin("Spout") != null && SpoutManager.getPlayer(player).isSpoutCraftEnabled() && playSound == true) {
                        SpoutManager.getSoundManager().playCustomSoundEffect(plugin, SpoutManager.getPlayer(player), "https://dl.dropbox.com/u/53758864/tardis_takeoff.mp3", false, pp, 9, 75);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (plugin.config.getBoolean("protect_blocks") == Boolean.valueOf("true")) {
            String[] set = {"EAST", "SOUTH", "WEST", "NORTH", "UP", "DOWN"};
            for (String f : set) {
                int id = event.getBlock().getRelative(BlockFace.valueOf(f)).getTypeId();
                byte d = event.getBlock().getRelative(BlockFace.valueOf(f)).getData();
                if (id == 35 && (d == 1 || d == 7 || d == 8 || d == 11)) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
}
