/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TVMGUIListener extends TARDISMenuListener {

    final List<Integer> letters = List.of(0, 4, 5);
    final char[] two = new char[]{'2', 'a', 'b', 'c'};
    final char[] three = new char[]{'3', 'd', 'e', 'f'};
    final char[] four = new char[]{'4', 'g', 'h', 'i'};
    final char[] five = new char[]{'5', 'j', 'k', 'l'};
    final char[] six = new char[]{'6', 'm', 'n', 'o'};
    final char[] seven = new char[]{'7', 'p', 'q', 'r', 's'};
    final char[] eight = new char[]{'8', 't', 'u', 'v'};
    final char[] nine = new char[]{'9', 'w', 'x', 'y', 'z'};
    final char[] star = new char[]{'*', ' '};
    final char[] hash = new char[]{'#', '~', '_', '-'};
    final int[] pos;
    final TVMQueryFactory qf;
    private final TARDIS plugin;
    List<String> components = List.of("", "", "", "", "", "");
    int which = 0;
    int t2 = 0;
    int t3 = 0;
    int t4 = 0;
    int t5 = 0;
    int t6 = 0;
    int t7 = 0;
    int t8 = 0;
    int t9 = 0;
    int ts = 0;
    int th = 0;

    public TVMGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        // init string positions
        pos = new int[6];
        for (int i = 0; i < 6; i++) {
            pos[i] = 0;
        }
        qf = new TVMQueryFactory(this.plugin);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGUIClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "Vortex Manipulator")) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            return;
        }
        switch (slot) {
            case 6 -> usePredictive(view); // predictive world
            case 11 -> {
                // world
                which = 0;
                resetTrackers();
            }
            case 12 -> updateDisplay(view, '1'); // one
            case 13 -> {
                // two
                if (letters.contains(which)) {
                    updateDisplay(view, two[t2]);
                    t2++;
                    if (t2 == two.length) {
                        t2 = 0;
                    }
                } else {
                    updateDisplay(view, '2');
                }
            }
            case 14 -> {
                // three
                if (letters.contains(which)) {
                    updateDisplay(view, three[t3]);
                    t3++;
                    if (t3 == three.length) {
                        t3 = 0;
                    }
                } else {
                    updateDisplay(view, '3');
                }
            }
            case 16 -> {
                // save
                which = 4;
                resetTrackers();
            }
            case 18 -> {
                // lifesigns
                which = 5;
                resetTrackers();
            }
            case 20 -> {
                // x
                which = 1;
                resetTrackers();
            }
            case 21 -> {
                // four
                if (letters.contains(which)) {
                    updateDisplay(view, four[t4]);
                    t4++;
                    if (t4 == four.length) {
                        t4 = 0;
                    }
                } else {
                    updateDisplay(view, '4');
                }
            }
            case 22 -> {
                // five
                if (letters.contains(which)) {
                    updateDisplay(view, five[t5]);
                    t5++;
                    if (t5 == five.length) {
                        t5 = 0;
                    }
                } else {
                    updateDisplay(view, '5');
                }
            }
            case 23 -> {
                // six
                if (letters.contains(which)) {
                    updateDisplay(view, six[t6]);
                    t6++;
                    if (t6 == six.length) {
                        t6 = 0;
                    }
                } else {
                    updateDisplay(view, '6');
                }
            }
            case 25 -> loadSaves(player); // open saves GUI
            case 29 -> {
                // y
                which = 2;
                resetTrackers();
            }
            case 30 -> {
                // seven
                if (letters.contains(which)) {
                    updateDisplay(view, seven[t7]);
                    t7++;
                    if (t7 == seven.length) {
                        t7 = 0;
                    }
                } else {
                    updateDisplay(view, '7');
                }
            }
            case 31 -> {
                // eight
                if (letters.contains(which)) {
                    updateDisplay(view, eight[t8]);
                    t8++;
                    if (t8 == eight.length) {
                        t8 = 0;
                    }
                } else {
                    updateDisplay(view, '8');
                }
            }
            case 32 -> {
                // nine
                if (letters.contains(which)) {
                    updateDisplay(view, nine[t9]);
                    t9++;
                    if (t9 == nine.length) {
                        t9 = 0;
                    }
                } else {
                    updateDisplay(view, '9');
                }
            }
            case 34 -> message(player); // message
            case 38 -> {
                // z
                which = 3;
                resetTrackers();
            }
            case 39 -> {
                // star
                updateDisplay(view, star[ts]);
                ts++;
                if (ts == star.length) {
                    ts = 0;
                }
            }
            case 40 -> updateDisplay(view, '0'); //zero
            case 41 -> {
                // hash
                if (letters.contains(which) || components.getFirst().startsWith("~")) {
                    updateDisplay(view, hash[th]);
                    th++;
                    if (th == hash.length) {
                        th = 0;
                    }
                } else {
                    updateDisplay(view, '-');
                }
            }
            case 43 -> setBeacon(player); // beacon
            case 45 -> {
                // close
                close(player);
                components = List.of("", "", "", "", "", "");
            }
            case 48 -> {
                // previous cursor
                if (pos[which] > 0) {
                    pos[which]--;
                }
                resetTrackers();
            }
            case 50 -> {
                // next cursor
                int next = components.get(which).length() + 1;
                if (pos[which] < next) {
                    pos[which]++;
                }
                resetTrackers();
            }
            case 53 -> {
                switch (which) {
                    case 4 -> saveCurrentLocation(player, view); // save
                    case 5 -> scanLifesigns(player, view); // scan
                    default -> doWarp(player, view); // warp
                }
            }
            default -> {
            }
        }
    }

    private void usePredictive(InventoryView view) {
        ItemStack is = view.getItem(6);
        ItemMeta im = is.getItemMeta();
        String world = im.getLore().getFirst();
        components.set(0, world);
        ItemStack display = view.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = List.of(world + " " + components.get(1) + " " + components.get(2) + " " + components.get(3));
        dim.setLore(lore);
        display.setItemMeta(dim);
        // move the cursor to the end of the string
        which = 1;
    }

    private void setPredictive(String stub, InventoryView view) {
        ItemStack is = view.getItem(6);
        ItemMeta im = is.getItemMeta();
        for (World w : plugin.getServer().getWorlds()) {
            String world = w.getName();
            if (w.getName().toLowerCase(Locale.ROOT).startsWith(stub)) {
                List<String> lore = List.of(world);
                im.setLore(lore);
                is.setItemMeta(im);
                break;
            }
        }
    }

    private void updateDisplay(InventoryView view, char s) {
        ItemStack display = view.getItem(4);
        ItemMeta dim = display.getItemMeta();
        char[] chars = (components.get(which).isEmpty()) ? new char[1] : components.get(which).toCharArray();
        if (pos[which] >= chars.length) {
            char[] tmp = chars.clone();
            chars = new char[pos[which] + 1];
            int i = 0;
            for (char ignored : tmp) {
                chars[i] = tmp[i];
                i++;
            }
        }
        chars[pos[which]] = s;
        String comp = new String(chars);
        String combined;
        switch (which) {
            case 0 -> {
                setPredictive(comp.toLowerCase(Locale.ROOT), view);
                combined = comp + " " + components.get(1) + " " + components.get(2) + " " + components.get(3);
            }
            case 1 -> combined = components.getFirst() + " " + comp + " " + components.get(2) + " " + components.get(3);
            case 2 -> combined = components.getFirst() + " " + components.get(1) + " " + comp + " " + components.get(3);
            case 3 -> combined = components.getFirst() + " " + components.get(1) + " " + components.get(2) + " " + comp;
            default -> combined = comp;
        }
        components.set(which, comp);
        List<String> dlore = List.of(combined);
        dim.setLore(dlore);
        display.setItemMeta(dim);
    }

    private void resetTrackers() {
        t2 = 0;
        t3 = 0;
        t4 = 0;
        t5 = 0;
        t6 = 0;
        t7 = 0;
        t8 = 0;
        t9 = 0;
        ts = 0;
        th = 0;
    }

    private void saveCurrentLocation(Player player, InventoryView view) {
        ItemStack display = view.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = dim.getLore();
        String name = lore.getFirst();
        if (name.isEmpty()) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NEED");
            return;
        }
        Location l = player.getLocation();
        HashMap<String, Object> set = new HashMap<>();
        set.put("uuid", player.getUniqueId().toString());
        set.put("save_name", lore.getFirst());
        set.put("world", l.getWorld().getName());
        set.put("x", l.getX());
        set.put("y", l.getY());
        set.put("z", l.getZ());
        set.put("yaw", l.getYaw());
        set.put("pitch", l.getPitch());
        plugin.getQueryFactory().doInsert("saves", set);
        close(player);
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_CURRENT");
    }

    private void scanLifesigns(Player player, InventoryView view) {
        close(player);
        if (!TARDISPermission.hasPermission(player, "vm.lifesigns")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_LIFESIGNS");
            return;
        }
        int required = plugin.getVortexConfig().getInt("tachyon_use.lifesigns");
        if (!TVMUtils.checkTachyonLevel(player.getUniqueId().toString(), required)) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_LIFESIGNS_TACHYON");
            return;
        }
        // remove tachyons
        qf.alterTachyons(player.getUniqueId().toString(), -required);
        // process GUI
        ItemStack display = view.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = dim.getLore();
        String pname = lore.getFirst().trim();
        if (pname.isEmpty()) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "SCAN_ENTS");
            // scan nearby entities
            double d = plugin.getVortexConfig().getDouble("lifesign_scan_distance");
            List<Entity> ents = player.getNearbyEntities(d, d, d);
            if (!ents.isEmpty()) {
                // record nearby entities
                HashMap<EntityType, Integer> scannedentities = new HashMap<>();
                List<String> playernames = new ArrayList<>();
                ents.forEach((k) -> {
                    EntityType et = k.getType();
                    if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                        int entity_count = scannedentities.getOrDefault(et, 0);
                        boolean visible = true;
                        if (et.equals(EntityType.PLAYER)) {
                            Player entPlayer = (Player) k;
                            if (player.canSee(entPlayer)) {
                                playernames.add(entPlayer.getName());
                            } else {
                                visible = false;
                            }
                        }
                        if (visible) {
                            scannedentities.put(et, entity_count + 1);
                        }
                    }
                });
                scannedentities.forEach((key, value) -> {
                    String message = "";
                    StringBuilder buf = new StringBuilder();
                    if (key.equals(EntityType.PLAYER) && !playernames.isEmpty()) {
                        playernames.forEach((pn) -> buf.append(", ").append(pn));
                        message = " (" + buf.substring(2) + ")";
                    }
                    player.sendMessage("    " + key + ": " + value + message);
                });
                scannedentities.clear();
            } else {
                player.sendMessage("SCAN_NONE");
            }
        } else {
            Player scanned = plugin.getServer().getPlayer(pname);
            if (scanned == null) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "COULD_NOT_FIND_NAME");
                return;
            }
            if (!scanned.isOnline()) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NOT_ONLINE", pname);
                return;
            }
            // getHealth() / getMaxHealth() * getHealthScale()
            double mh = scanned.getAttribute(Attribute.MAX_HEALTH).getValue();
            double health = scanned.getHealth() / mh * scanned.getHealthScale();
            float hunger = (scanned.getFoodLevel() / 20F) * 100;
            int air = scanned.getRemainingAir();
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_LIFESIGNS", pname);
            player.sendMessage("Has been alive for: " + TVMUtils.convertTicksToTime(scanned.getTicksLived()));
            player.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
            player.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
            player.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
        }
    }

    private void loadSaves(Player player) {
        close(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMSavesGUI tvms = new TVMSavesGUI(plugin, 0, 44, player.getUniqueId().toString());
            ItemStack[] gui = tvms.getGUI();
            Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "VM Saves");
            vmg.setContents(gui);
            player.openInventory(vmg);
        }, 2L);
    }

    private void message(Player player) {
        close(player);
        if (!TARDISPermission.hasPermission(player, "vm.message")) {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_MSGS");
            return;
        }
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            TVMMessageGUI tvmm = new TVMMessageGUI(plugin, 0, 44, player.getUniqueId().toString());
            ItemStack[] gui = tvmm.getGUI();
            Inventory vmg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "VM Messages");
            vmg.setContents(gui);
            player.openInventory(vmg);
        }, 2L);
    }

    private void setBeacon(Player player) {
        if (!TARDISPermission.hasPermission(player, "vm.beacon")) {
            close(player);
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PERM_BEACON");
            return;
        }
        UUID uuid = player.getUniqueId();
        String message = "VM_BEACON_TACHYON";
        int required = plugin.getVortexConfig().getInt("tachyon_use.beacon");
        if (TVMUtils.checkTachyonLevel(uuid.toString(), required)) {
            String ustr = uuid.toString();
            Location l = player.getLocation();
            // potential griefing, we need to check the location first!
            List<Flag> flags = new ArrayList<>();
            if (plugin.getConfig().getBoolean("preferences.respect_griefprevention")) {
                flags.add(Flag.RESPECT_GRIEFPREVENTION);
            }
            if (plugin.getConfig().getBoolean("preferences.respect_towny")) {
                flags.add(Flag.RESPECT_TOWNY);
            }
            if (plugin.getConfig().getBoolean("preferences.respect_worldborder")) {
                flags.add(Flag.RESPECT_WORLDBORDER);
            }
            if (plugin.getConfig().getBoolean("preferences.respect_worldguard")) {
                flags.add(Flag.RESPECT_WORLDGUARD);
            }
            Parameters params = new Parameters(player, flags);
            if (!plugin.getTardisAPI().getRespect().getRespect(l, params)) {
                close(player);
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_BEACON_PERMIT");
                return;
            }
            Block b = l.getBlock().getRelative(BlockFace.DOWN);
            qf.saveBeaconBlock(ustr, b);
            b.setBlockData(Material.BEACON.createBlockData());
            Block down = b.getRelative(BlockFace.DOWN);
            qf.saveBeaconBlock(ustr, down);
            BlockData iron = Material.IRON_BLOCK.createBlockData();
            down.setBlockData(iron);
            plugin.getGeneralKeeper().getSurrounding().forEach((f) -> {
                qf.saveBeaconBlock(ustr, down.getRelative(f));
                down.getRelative(f).setBlockData(iron);
            });
            plugin.getTvmSettings().getBeaconSetters().add(uuid);
            message = "VM_BEACON_MOVE";
            // remove tachyons
            qf.alterTachyons(player.getUniqueId().toString(), -required);
        }
        close(player);
        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, message);
    }

    private void doWarp(Player player, InventoryView view) {
        ItemStack display = view.getItem(4);
        ItemMeta dim = display.getItemMeta();
        List<String> lore = dim.getLore();
        List<String> dest;
        if (!lore.getFirst().trim().isEmpty()) {
            dest = List.of(lore.getFirst().trim().split(" "));
        } else {
            dest = new ArrayList<>();
        }
        List<String> worlds = new ArrayList<>();
        Location l;
        // set parameters
        List<Flag> flags = new ArrayList<>();
        flags.add(Flag.PERMS_AREA);
        flags.add(Flag.PERMS_NETHER);
        flags.add(Flag.PERMS_THEEND);
        flags.add(Flag.PERMS_WORLD);
        if (plugin.getConfig().getBoolean("preferences.respect_griefprevention")) {
            flags.add(Flag.RESPECT_GRIEFPREVENTION);
        }
        if (plugin.getConfig().getBoolean("preferences.respect_towny")) {
            flags.add(Flag.RESPECT_TOWNY);
        }
        if (plugin.getConfig().getBoolean("preferences.respect_worldborder")) {
            flags.add(Flag.RESPECT_WORLDBORDER);
        }
        if (plugin.getConfig().getBoolean("preferences.respect_worldguard")) {
            flags.add(Flag.RESPECT_WORLDGUARD);
        }
        Parameters params = new Parameters(player, flags);
        int required;
        switch (dest.size()) {
            case 1, 2, 3 -> {
                required = plugin.getVortexConfig().getInt("tachyon_use.travel.world");
                // only world specified (or incomplete setting)
                // check world is an actual world
                if (plugin.getServer().getWorld(dest.getFirst()) == null) {
                    close(player);
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_WORLD");
                    return;
                }
                // check world is enabled for travel
                if (!plugin.getTardisAPI().getWorlds().contains(dest.getFirst())) {
                    close(player);
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
                    return;
                }
                worlds.add(dest.getFirst());
                l = plugin.getTardisAPI().getRandomLocation(worlds, null, params);
            }
            case 4 -> {
                required = plugin.getVortexConfig().getInt("tachyon_use.travel.coords");
                // world, x, y, z specified
                World w;
                if (dest.getFirst().contains("~")) {
                    // relative location
                    w = player.getLocation().getWorld();
                } else {
                    w = plugin.getServer().getWorld(dest.getFirst());
                    if (w == null) {
                        close(player);
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_WORLD");
                        return;
                    }
                    // check world is enabled for travel
                    if (!plugin.getTardisAPI().getWorlds().contains(dest.getFirst())) {
                        close(player);
                        plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NO_TRAVEL");
                        return;
                    }
                }
                double x;
                double y;
                double z;
                try {
                    if (dest.get(1).startsWith("~")) {
                        // get players current location
                        Location tl = player.getLocation();
                        double tx = tl.getX();
                        double ty = tl.getY();
                        double tz = tl.getZ();
                        // strip off the initial "~" and add to current position
                        x = tx + Double.parseDouble(dest.get(1).substring(1));
                        y = ty + Double.parseDouble(dest.get(2).substring(1));
                        z = tz + Double.parseDouble(dest.get(3).substring(1));
                    } else {
                        x = Double.parseDouble(dest.get(1));
                        y = Double.parseDouble(dest.get(2));
                        z = Double.parseDouble(dest.get(3));
                    }
                } catch (NumberFormatException e) {
                    close(player);
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_COORDS");
                    return;
                }
                l = new Location(w, x, y, z);
                // check block has space for player
                if (!l.getBlock().getType().equals(Material.AIR)) {
                    plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_ADJUST");
                    // get the highest block at these coords
                    int highest = l.getWorld().getHighestBlockYAt(l);
                    l.setY(highest);
                }
            }
            default -> {
                required = plugin.getVortexConfig().getInt("tachyon_use.travel.random");
                // random
                l = plugin.getTardisAPI().getRandomLocation(plugin.getTardisAPI().getWorlds(), null, params);
            }
        }
        UUID uuid = player.getUniqueId();
        if (!TVMUtils.checkTachyonLevel(uuid.toString(), required)) {
            close(player);
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", required);
            return;
        }
        if (l != null) {
            close(player);
            List<Player> players = new ArrayList<>();
            players.add(player);
            if (plugin.getVortexConfig().getBoolean("allow.multiple")) {
                player.getNearbyEntities(0.5d, 0.5d, 0.5d).forEach((e) -> {
                    if (e instanceof Player && !e.getUniqueId().equals(uuid)) {
                        players.add((Player) e);
                    }
                });
            }
            int actual = required * players.size();
            if (!TVMUtils.checkTachyonLevel(uuid.toString(), actual)) {
                plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_NEED_TACHYON", actual);
                return;
            }
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_STANDBY");
            while (!l.getChunk().isLoaded()) {
                l.getChunk().load();
            }
            TVMUtils.movePlayers(players, l, player.getLocation().getWorld());
            // remove tachyons
            qf.alterTachyons(uuid.toString(), -actual);
        } else {
            plugin.getMessenger().send(player, TardisModule.VORTEX_MANIPULATOR, "VM_PARAMETERS");
        }
    }
}
