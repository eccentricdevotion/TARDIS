package me.eccentric_nz.TARDIS.rooms.loader;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetArtronLevelID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetChunkTickets;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;

public class TicketRunnable implements Runnable {

    private final TARDIS plugin;
    private final int amount;

    public TicketRunnable(TARDIS plugin) {
        this.plugin = plugin;
        this.amount = plugin.getArtronConfig().getInt("chunk_tickets", 10);
    }

    @Override
    public void run() {
        // get chunk tickets
        ResultSetChunkTickets rsc = new ResultSetChunkTickets(plugin);
        List<Integer> ids = rsc.getIds();
        if (!ids.isEmpty()) {
            for (int id : ids) {
                ResultSetChunkTickets rsct = new ResultSetChunkTickets(plugin);
                int count = rsct.count(id);
                if (count > 0) {
                    int remove = amount * count;
                    // check TARDIS has energy to recharge
                    ResultSetArtronLevelID rsa = new ResultSetArtronLevelID(plugin);
                    // must always have at least 500 Artron energy remaining
                    if (!rsa.fromId(id) || rsa.getArtronLevel() < remove + 500) {
                        // remove chunk tickets
                        clearTickets(id);
                    } else {
                        // remove some energy
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        plugin.getQueryFactory().alterEnergyLevel("tardis", -remove, where, null);
                    }
                }
            }
        }
    }

    private void clearTickets(int id) {
        // remove chunk tickets
        ResultSetChunkTickets rsct = new ResultSetChunkTickets(plugin);
        World world = null;
        if (rsct.fromId(id)) {
            for (Ticket t : rsct.getData()) {
                if (world == null) {
                    world = t.world();
                }
                Chunk chunk = world.getChunkAt(t.x(), t.z());
                chunk.removePluginChunkTicket(plugin);
            }
        }
//        // delete chunk records
//        HashMap<String, Object> where = new HashMap<>();
//        where.put("tardis_id", id);
//        where.put("ticket", 1);
//        plugin.getQueryFactory().doDelete("chunks", where);
    }
}
