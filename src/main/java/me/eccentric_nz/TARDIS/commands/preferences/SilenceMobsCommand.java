package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.TARDISAliasResolver;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SilenceMobsCommand {

    private final TARDIS plugin;

    public SilenceMobsCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean toggle(Player player, String[] args) {
        // get the player's TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", player.getUniqueId().toString());
        ResultSetTardis rst = new ResultSetTardis(plugin, where, "", false);
        if (rst.resultSet()) {
            boolean on_off = args[1].equalsIgnoreCase("on");
            // get ARS chunks
            String[] tc = rst.getTardis().getChunk().split(":");
            int cx = TARDISNumberParsers.parseInt(tc[1]);
            int cz = TARDISNumberParsers.parseInt(tc[2]);
            World world = TARDISAliasResolver.getWorldFromAlias(tc[0]);
            if (world != null) {
                // get ARS chunks - 3 high x 9 wide x 9 deep
                for (int x = -4; x < 5; x++) {
                    for (int z = -4; z < 5; z++) {
                        Chunk c = world.getChunkAt(cx + x, cz + z);
                        // get entities in the chunk
                        for (Entity e : c.getEntities()) {
                            if (e instanceof Creature creature) {
                                creature.setSilent(on_off);
                            }
                        }
                    }
                }
            }
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_TARDIS");
        }
        return true;
    }
}
