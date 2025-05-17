/*
 *  Copyright 2014 eccentric_nz.
 */
package me.eccentric_nz.tardisvortexmanipulator;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetInbox;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetManipulator;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetOutbox;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMResultSetSaves;
import me.eccentric_nz.tardisvortexmanipulator.storage.TVMMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;

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
        World to = l.getWorld();
        boolean crossWorlds = from != to;

        players.stream().peek((p) -> {
            TARDIS.plugin.getTvmSettings().getTravellers().add(p.getUniqueId());
            boolean allowFlight = p.getAllowFlight();
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                p.teleport(l);
                p.getWorld().playSound(l, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }, 10L);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> {
                p.teleport(l);
                p.setNoDamageTicks(200);
                if (p.getGameMode() == GameMode.CREATIVE || (allowFlight && crossWorlds)) {
                    p.setAllowFlight(true);
                }
            }, 15L);
        }).forEachOrdered((thePlayer) -> {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TARDIS.plugin, () -> TARDIS.plugin.getTvmSettings().getTravellers().remove(thePlayer.getUniqueId()), 100L);
        });
    }

    /**
     * Check they have enough tachyons.
     *
     * @param uuid the String UUID of the player to check
     * @param required the minimum amount of Tachyon required
     * @return true if the player has enough energy
     */
    public static boolean checkTachyonLevel(String uuid, int required) {
        TVMResultSetManipulator rs = new TVMResultSetManipulator(TARDIS.plugin, uuid);
        if (!rs.resultSet()) {
            return false;
        }
        return rs.getTachyonLevel() >= required;
    }

    /**
     * Send a list of saves to a player.
     *
     * @param p the player to message
     * @param rss the ResultSet containing the save information
     * @param page the page number of this list
     */
    public static void sendSaveList(Player p, TVMResultSetSaves rss, int page) {
        TARDIS.plugin.getMessenger().sendWithColour(p, TardisModule.VORTEX_MANIPULATOR, "Saves (page " + page + ":", "#55FFFF");
        rss.getSaves().forEach((s) -> p.sendMessage(s.getName() + " - " + s.getWorld() + ":" + s.getX() + ":" + s.getY() + ":" + s.getZ()));
    }

    /**
     * Send a list of received messages to a player.
     *
     * @param p the player to message
     * @param rsi the ResultSet containing the message information
     * @param page the page number of this list
     */
    public static void sendInboxList(Player p, TVMResultSetInbox rsi, int page) {
        TARDIS.plugin.getMessenger().sendWithColour(p, TardisModule.VORTEX_MANIPULATOR, "Inbox (page " + page + "):", "#55FFFF");
        rsi.getMail().forEach((m) -> {
            String colour = (m.isRead()) ? "#555555" : "#AAAAAA";
            int len = Math.min(m.getMessage().length(), 12);
            TARDIS.plugin.getMessenger().messageWithColour(p, m.getId() + ": " + m.getDate() + " - " + m.getMessage().substring(0, len), colour);
        });
    }

    /**
     * Send a list of sent messages to a player.
     *
     * @param p the player to message
     * @param rso the ResultSet containing the message information
     * @param page the page number of this list
     */
    public static void sendOutboxList(Player p, TVMResultSetOutbox rso, int page) {
        TARDIS.plugin.getMessenger().sendWithColour(p, TardisModule.VORTEX_MANIPULATOR, "Outbox (page " + page + "):", "#55FFFF");
        rso.getMail().forEach((m) -> {
            int len = Math.min(m.getMessage().length(), 12);
            p.sendMessage(m.getId() + " - " + m.getDate() + " - " + m.getMessage().substring(0, len));
        });
    }

    /**
     * Show a message to a player.
     *
     * @param p the player to message
     * @param m the message to read
     */
    public static void readMessage(Player p, TVMMessage m) {
        TARDIS.plugin.getMessenger().sendWithColour(p, TardisModule.VORTEX_MANIPULATOR, Bukkit.getOfflinePlayer(m.getWho()).getName() + " - " + m.getDate(), "#55FFFF");
        p.sendMessage(m.getMessage());
    }

    /**
     * Convert ticks to human-readable time.
     *
     * @param time the time in ticks to convert
     * @return the human-readable time
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
