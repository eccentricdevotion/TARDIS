/*
 *  Copyright 2015 eccentric_nz.
 */
package me.eccentric_nz.TARDIS.junk;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISJunkTime {

    private final TARDIS plugin;

    public TARDISJunkTime(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean elapsed(CommandSender sender) {
        long conf = plugin.getConfig().getLong("junk.return");
        if (conf > 0) {
            long waitTime = conf * 1000;
            long lastUsed = plugin.getGeneralKeeper().getJunkTime();
            long now = System.currentTimeMillis();
            long returnTime = (waitTime - (now - lastUsed)) / 1000;
            long mins = returnTime / 60;
            long secs = returnTime - (mins * 60);
            String sub = String.format("%d minutes %d seconds", mins, secs);
            TARDISMessage.send(sender, "JUNK_RETURN_TIME", sub);
        } else {
            TARDISMessage.send(sender, "JUNK_NO_RETURN");
        }
        return true;
    }
}
