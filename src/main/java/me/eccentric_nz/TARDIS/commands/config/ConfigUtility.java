package me.eccentric_nz.TARDIS.commands.config;

import me.eccentric_nz.TARDIS.TARDIS;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtility {

    public static List<String> combineLists() {
        TARDISConfigCommand cmd = TARDIS.plugin.getGeneralKeeper().getTardisConfigCommand();
        List<String> newList = new ArrayList<>(
                cmd.firstsStr.size()
                        + cmd.firstsBool.size()
                        + cmd.firstsInt.size()
                        + cmd.firstsStrArtron.size()
                        + cmd.firstsIntArtron.size()
        );
        newList.addAll(cmd.firstsStr.keySet());
        newList.addAll(cmd.firstsBool.keySet());
        newList.addAll(cmd.firstsInt.keySet());
        newList.addAll(cmd.firstsStrArtron);
        newList.addAll(cmd.firstsIntArtron);
        return newList;
    }
}
