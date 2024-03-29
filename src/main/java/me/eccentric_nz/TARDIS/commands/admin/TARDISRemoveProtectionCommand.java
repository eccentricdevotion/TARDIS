package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;

import java.util.HashMap;

public class TARDISRemoveProtectionCommand {

    private final TARDIS plugin;

    public TARDISRemoveProtectionCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean remove(String[] args) {
        // remove database record
        int id = TARDISNumberParsers.parseInt(args[1]);
        HashMap<String, Object> where = new HashMap<>();
        where.put("b_id", id);
        plugin.getQueryFactory().doDelete("blocks", where);
        // remove from protection map
        plugin.getGeneralKeeper().getProtectBlockMap().remove(args[2]);
        return true;
    }
}
