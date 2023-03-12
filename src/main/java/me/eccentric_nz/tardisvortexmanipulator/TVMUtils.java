/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetOutbox;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TVMUtils {

    public static void movePlayers(List<Player> players, Location l, World from) {

        // try loading chunk
        World world = l.getWorld();
        Chunk chunk = world.getChunkAt(l);
        while (!world.isChunkLoaded(chunk)) {
            world.loadChunk(chunk);
        }
        // set location to centre of block
        l.setX(l.getBlockX() + 0.5);
        l.setY(l.getY() + 0.2);
        l.setZ(l.getBlockZ() + 0.5);
        Location theLocation = l;
        World to = theLocation.getWorld();
        boolean crossWorlds = from != to;

        players.stream().map((p) -> {
            Player thePlayer = p;
            TARDISVortexManipulator.plugin.getTravellers().add(p.getUniqueId());
            boolean allowFlight = thePlayer.getAllowFlight();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDISVortexManipulator.plugin, () -> {
                thePlayer.teleport(theLocation);
                thePlayer.getWorld().playSound(theLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDISVortexManipulator.plugin, () -> {
                thePlayer.teleport(theLocation);
                thePlayer.setNoDamageTicks(200);
                if (thePlayer.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds)) {
                    thePlayer.setAllowFlight(true);
                }
            }, 15L);
            return thePlayer;
        }).forEachOrdered((thePlayer) -> {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDISVortexManipulator.plugin, () -> {
                if (TARDISVortexManipulator.plugin.getTravellers().contains(thePlayer.getUniqueId())) {
                    TARDISVortexManipulator.plugin.getTravellers().remove(thePlayer.getUniqueId());
                }
            }, 100L);
        });
    }

    /**
     * Get world protection flags
     *
     * @return List of flags with parameters
     */
    public static List<Flag> getProtectionFlags() {
        List<Flag> flags = new ArrayList<>();
        flags.add(Flag.PERMS_AREA);
        flags.add(Flag.PERMS_NETHER);
        flags.add(Flag.PERMS_THEEND);
        flags.add(Flag.PERMS_WORLD);
        if (TARDISVortexManipulator.plugin.getConfig().getBoolean("respect.factions")) {
            flags.add(Flag.RESPECT_FACTIONS);
        }
        if (TARDISVortexManipulator.plugin.getConfig().getBoolean("respect.griefprevention")) {
            flags.add(Flag.RESPECT_GRIEFPREVENTION);
        }
        if (TARDISVortexManipulator.plugin.getConfig().getBoolean("respect.towny")) {
            flags.add(Flag.RESPECT_TOWNY);
        }
        if (TARDISVortexManipulator.plugin.getConfig().getBoolean("respect.worldborder")) {
            flags.add(Flag.RESPECT_WORLDBORDER);
        }
        if (TARDISVortexManipulator.plugin.getConfig().getBoolean("respect.worldguard")) {
            flags.add(Flag.RESPECT_WORLDGUARD);
        }
        return flags;
    }

    /**
     * Check they have enough tachyons.
     *
     * @param uuid     the String UUID of the player to check
     * @param required the minimum amount of Tachyon required
     * @return true if the player has enough energy
     */
    public static boolean checkTachyonLevel(String uuid, int required) {
        TVMResultSetManipulator rs = new TVMResultSetManipulator(TARDISVortexManipulator.plugin, uuid);
        if (!rs.resultSet()) {
            return false;
        }
        return rs.getTachyonLevel() >= required;
    }

    /**
     * Send a list of saves to a player.
     *
     * @param p    the player to message
     * @param rss  the ResultSet containing the save information
     * @param page the page number of this list
     */
    public static void sendSaveList(Player p, TVMResultSetSaves rss, int page) {
        p.sendMessage(TARDISVortexManipulator.plugin.getPluginName() + ChatColor.AQUA + "Saves (page " + page + ":");
        rss.getSaves().forEach((s) -> {
            p.sendMessage(s.getName() + " - " + s.getWorld() + ":" + s.getX() + ":" + s.getY() + ":" + s.getZ());
        });
    }

    /**
     * Send a list of received messages to a player.
     *
     * @param p    the player to message
     * @param rsi  the ResultSet containing the message information
     * @param page the page number of this list
     */
    public static void sendInboxList(Player p, TVMResultSetInbox rsi, int page) {
        p.sendMessage(TARDISVortexManipulator.plugin.getPluginName() + ChatColor.AQUA + "Inbox (page " + page + "):");
        rsi.getMail().forEach((m) -> {
            ChatColor colour = (m.isRead()) ? ChatColor.DARK_GRAY : ChatColor.GRAY;
            p.sendMessage(colour + "" + m.getId() + ": " + m.getDate() + " - " + m.getMessage().substring(0, 12));
        });
    }

    /**
     * Send a list of sent messages to a player.
     *
     * @param p    the player to message
     * @param rso  the ResultSet containing the message information
     * @param page the page number of this list
     */
    public static void sendOutboxList(Player p, TVMResultSetOutbox rso, int page) {
        p.sendMessage(TARDISVortexManipulator.plugin.getPluginName() + ChatColor.AQUA + "Outbox (page " + page + "):");
        rso.getMail().forEach((m) -> {
            p.sendMessage(m.getId() + " - " + m.getDate() + " - " + m.getMessage().substring(0, 12));
        });
    }

    /**
     * Show a message to a player.
     *
     * @param p the player to message
     * @param m the message to read
     */
    public static void readMessage(Player p, TVMMessage m) {
        p.sendMessage(TARDISVortexManipulator.plugin.getPluginName() + ChatColor.AQUA + TARDISVortexManipulator.plugin.getServer().getOfflinePlayer(m.getWho()).getName() + " - " + m.getDate());
        p.sendMessage(m.getMessage());
    }

    /**
     * Convert ticks to human readable time.
     *
     * @param time the time in ticks to convert
     * @return the human readable time
     */
    public static String convertTicksToTime(int time) {
        // convert to seconds
        int seconds = time / 20;
        int h = seconds / 3600;
        int remainder = seconds - (h * 3600);
        int m = remainder / 60;
        int s = remainder - (m * 60);
        String gh = (h > 1 || h == 0) ? " hours " : " hour ";
        String gm = (m > 1 || m == 0) ? " minutes " : " minute ";
        String gs = (s > 1 || s == 0) ? " seconds" : " second";
        return h + gh + m + gm + s + gs;
    }
}
