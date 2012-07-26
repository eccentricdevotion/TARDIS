package me.eccentric_nz.plugins.TARDIS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
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
                    // check if the chunk already contains a TARDIS
                    // get this chunk co-ords
                    Chunk chunk = blockBottom.getChunk();
                    String cw;
                    World chunkworld;
                    // check config to see whether we are using a default world to store TARDII
                    if (plugin.config.getBoolean("default_world") == Boolean.valueOf("true")) {
                        cw = plugin.config.getString("default_world_name");
                        chunkworld = plugin.getServer().getWorld(cw);
                    } else {
                        chunkworld = chunk.getWorld();
                        cw = chunkworld.getName();
                    }
                    int cx = chunk.getX();
                    int cz = chunk.getZ();
                    Schematic s = new Schematic(plugin);
                    if (!s.checkChunk(cw, cx, cz)) {
                        try {
                            File file = new File(plugin.getDataFolder() + File.separator + "chunks" + File.separator + cw + ".chunks");
                            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                            bw.write(cw + ":" + cx + ":" + cz);
                            bw.newLine();
                            bw.close();
                        } catch (IOException io) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Could not save the time lords file!");
                        }
                        float pyaw = player.getLocation().getYaw();
                        if (pyaw >= 0) {
                            pyaw = (pyaw % 360);
                        } else {
                            pyaw = (360 + (pyaw % 360));
                        }
                        Location block_loc = blockBottom.getLocation();
                        // determine direction player is facing
                        String d = "";
                        if (pyaw >= 315 || pyaw < 45) {
                            d = "SOUTH";
                        }
                        if (pyaw >= 225 && pyaw < 315) {
                            d = "EAST";
                        }
                        if (pyaw >= 135 && pyaw < 225) {
                            d = "NORTH";
                        }
                        if (pyaw >= 45 && pyaw < 135) {
                            d = "WEST";
                        }
                        // turn the block stack into a TARDIS
                        s.buildOuterTARDIS(player, block_loc, Constants.COMPASS.valueOf(d));
                        // save data to timelords file
                        plugin.timelords.set(configPath + ".home", block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ());
                        plugin.timelords.set(configPath + ".chunk", cw + ":" + cx + ":" + cz);
                        plugin.timelords.set(configPath + ".save", block_loc.getWorld().getName() + ":" + block_loc.getBlockX() + ":" + block_loc.getBlockY() + ":" + block_loc.getBlockZ());
                        plugin.timelords.set(configPath + ".direction", d);
                        try {
                            plugin.timelords.save(plugin.timelordsfile);
                        } catch (IOException io) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Could not save the time lords file!");
                        }
                        s.buildInnerTARDIS(plugin.schematic, player, chunkworld, Constants.COMPASS.valueOf(d));
                    } else {
                        player.sendMessage("A TARDIS already exists at this location, please try another chunk!");
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
        if (blockType == Material.WALL_SIGN) {
            // check the text on the sign
            Sign sign = (Sign) block.getState();
            String line1 = sign.getLine(1);
            String line2 = sign.getLine(2);
            if (line1.equals("¤fPOLICE") && line2.equals("¤fBOX")) {
                if (!plugin.timelords.contains(configPath + ".direction")) {
                    event.setCancelled(true);
                    sign.update();
                    player.sendMessage("Don't grief the TARDIS!");
                } else {
                    // check the sign location
                    Location sign_loc = block.getLocation();
                    Location bb_loc = Constants.getLocationFromFile(configPath, "save", yaw, pitch, plugin.timelords);
                    // get TARDIS direction
                    Constants.COMPASS d = Constants.COMPASS.valueOf(plugin.timelords.getString(configPath + ".direction"));
                    switch (d) {
                        case EAST:
                            signx = -2;
                            signz = 0;
                            break;
                        case SOUTH:
                            signx = 0;
                            signz = -2;
                            break;
                        case WEST:
                            signx = 2;
                            signz = 0;
                            break;
                        case NORTH:
                            signx = 0;
                            signz = 2;
                            break;
                    }
                    // if the sign was on the TARDIS destroy the TARDIS!
                    if (sign_loc.getBlockX() == bb_loc.getBlockX() + signx && sign_loc.getBlockY() == bb_loc.getBlockY() && sign_loc.getBlockZ() == bb_loc.getBlockZ() + signz) {
                        int cwx = 0, cwz = 0;
                        // don't drop the sign
                        //block.getDrops().clear();
                        Schematic s = new Schematic(plugin);
                        // clear the torch
                        s.destroyTorch(bb_loc);
                        s.destroySign(bb_loc, d);
                        // also remove the location of the chunk from file
                        String chunkLoc = plugin.timelords.getString(configPath + ".chunk");
                        String[] chunkworld = chunkLoc.split(":");
                        World cw = plugin.getServer().getWorld(chunkworld[0]);
                        World world = bb_loc.getWorld();

                        File chunkfile = new File(plugin.getDataFolder() + File.separator + "chunks" + File.separator + chunkworld[0] + ".chunks");
                        Constants c = new Constants();
                        c.removeLineFromFile(chunkfile, chunkLoc);
                        s.destroyTARDIS(player, cw, d, 1);
                        if (cw.getWorldType() == WorldType.FLAT) {
                            // replace stone blocks with AIR
                            s.destroyTARDIS(player, cw, d, 0);
                        }
                        s.destroyBlueBox(bb_loc, d, configPath);
                        // remove player from timelords
                        plugin.timelords.set(configPath, null);
                        try {
                            plugin.timelords.save(plugin.timelordsfile);
                        } catch (IOException io) {
                            System.err.println(Constants.MY_PLUGIN_NAME + " Could not save timelords file!");
                        }
                    } else {
                        // cancel the event because it's not the player's TARDIS
                        event.setCancelled(true);
                        sign.update();
                        player.sendMessage(Constants.NOT_OWNER);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        String configPath = player.getName();
        int savedx = 0, savedy = 0, savedz = 0, cx = 0, cz = 0;

        Block block = event.getClickedBlock();
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            Material blockType = block.getType();
            ItemStack stack = player.getItemInHand();
            Material material = stack.getType();
            // get key material from config
            Material key = Material.getMaterial(plugin.config.getString("key"));
            // only proceed if they are clicking an iron door with a redstone torch!
            if (blockType == Material.IRON_DOOR_BLOCK) {
                if (material == key) {
                    if (block != null) {
                        Location block_loc = block.getLocation();
                        if (player.hasPermission("TARDIS.enter")) {
                            if (plugin.timelords.contains(configPath + ".direction")) {
                                String d = plugin.timelords.getString(configPath + ".direction");
                                float yaw = player.getLocation().getYaw();
                                float pitch = player.getLocation().getPitch();
                                // get last known BLUEBOX location
                                Location door_loc = Constants.getLocationFromFile(configPath, "save", yaw, pitch, plugin.timelords);
                                // get INNER TARDIS location
                                Location tmp_loc = null;
                                String chunkstr = plugin.timelords.getString(configPath + ".chunk");
                                String[] split = chunkstr.split(":");
                                World cw = plugin.getServer().getWorld(split[0]);
                                try {
                                    cx = Integer.parseInt(split[1]);
                                    cz = Integer.parseInt(split[2]);
                                } catch (NumberFormatException nfe) {
                                    System.err.println(Constants.MY_PLUGIN_NAME + " Could not convert to number!");
                                }
                                Chunk chunk = cw.getChunkAt(cx, cz);
                                World w = player.getWorld();
                                double x = door_loc.getX();
                                double z = door_loc.getZ();
                                switch (Constants.COMPASS.valueOf(d)) {
                                    case NORTH:
                                        tmp_loc = chunk.getBlock(9, 19, 13).getLocation();
                                        door_loc.setZ(z + 1);
                                        break;
                                    case EAST:
                                        tmp_loc = chunk.getBlock(3, 19, 9).getLocation();
                                        door_loc.setX(x - 1);
                                        break;
                                    case SOUTH:
                                        tmp_loc = chunk.getBlock(6, 19, 3).getLocation();
                                        door_loc.setZ(z - 1);
                                        break;
                                    case WEST:
                                        tmp_loc = chunk.getBlock(13, 19, 6).getLocation();
                                        door_loc.setX(x + 1);
                                        break;
                                }
                                if (plugin.timelords.getBoolean(configPath + ".travelling") == Boolean.valueOf("true")) {
                                    // player is in the TARDIS
                                    // get location from timelords file
                                    final Location exitTardis = Constants.getLocationFromFile(configPath, "save", yaw, pitch, plugin.timelords);
                                    // make location safe ie. outside of the bluebox
                                    double ex = exitTardis.getX();
                                    double ez = exitTardis.getZ();
                                    double ey = exitTardis.getY();
                                    switch (Constants.COMPASS.valueOf(d)) {
                                        case NORTH:
                                            exitTardis.setZ(ez + 2.5);
                                            break;
                                        case EAST:
                                            exitTardis.setX(ex - 2.5);
                                            break;
                                        case SOUTH:
                                            exitTardis.setZ(ez - 2.5);
                                            break;
                                        case WEST:
                                            exitTardis.setX(ex + 2.5);
                                            break;
                                    }
                                    exitTardis.setY(ey - 1.75);
                                    // destroy current TARDIS location
                                    String sl = plugin.timelords.getString(configPath + ".save");
                                    String cl = plugin.timelords.getString(configPath + ".current");
                                    Boolean rebuild = false;
                                    Location newl = null;
                                    Schematic s = new Schematic(plugin);
                                    if (!sl.equals(cl)) {
                                        Location l = Constants.getLocationFromFile(configPath, "current", 0, 0, plugin.timelords);
                                        newl = Constants.getLocationFromFile(configPath, "save", 0, 0, plugin.timelords);
                                        double old_y = newl.getY();
                                        newl.setY(old_y - 2);
                                        // remove torch
                                        s.destroyTorch(l);
                                        // remove sign
                                        s.destroySign(l, Constants.COMPASS.valueOf(d));
                                        // remove blue box
                                        s.destroyBlueBox(l, Constants.COMPASS.valueOf(d), configPath);
                                        //rebuild = true;
                                    }
                                    // try preloading destination chunk
                                    while (!w.getChunkAt(exitTardis).isLoaded()) {
                                        w.getChunkAt(exitTardis).load();
                                    }
                                    // rebuild blue box
                                    if (newl != null) {
                                        s.buildOuterTARDIS(player, newl, Constants.COMPASS.valueOf(d));
                                    }
                                    // exit TARDIS!
                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                                        public void run() {
                                            player.teleport(exitTardis);
                                            player.setFlying(true);
                                        }
                                    }, 20L);

                                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                                        public void run() {
                                            player.teleport(exitTardis);
                                            player.setFlying(false);
                                        }
                                    }, 20L);
                                    plugin.timelords.set(configPath + ".travelling", false);
                                    try {
                                        plugin.timelords.save(plugin.timelordsfile);
                                    } catch (IOException io) {
                                        System.err.println(Constants.MY_PLUGIN_NAME + " could not save timelords file!");
                                    }
                                } else {
                                    // needs to be either top or bottom of door! This is the current bluebox door location
                                    if (block_loc.getBlockX() == door_loc.getBlockX() && block_loc.getBlockZ() == door_loc.getBlockZ() && (block_loc.getBlockY() == door_loc.getBlockY() - 2 || block_loc.getBlockY() == door_loc.getBlockY() - 1)) {
                                        // enter TARDIS!
                                        w.getChunkAt(tmp_loc).load();
                                        tmp_loc.setPitch(pitch);
                                        tmp_loc.setYaw(yaw);
                                        final Location tardis_loc = tmp_loc;
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                                            public void run() {
                                                player.teleport(tardis_loc);
                                                player.setFlying(true);
                                            }
                                        }, 20L);
                                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                                            public void run() {
                                                player.teleport(tardis_loc);
                                                player.setFlying(false);
                                            }
                                        }, 20L);
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
                    player.sendMessage(Constants.WRONG_MATERIAL + Constants.TARDIS_KEY + ". You have a " + material + " in your hand!");
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
                        player.sendMessage("Destination world: " + rand.getWorld().getName());
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
