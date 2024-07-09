package me.eccentric_nz.TARDIS.artron;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.ArtronFurnaceUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPoweredFurnaces;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.block.Furnace;

public class ArtronPoweredRunnable implements Runnable {

    private final TARDIS plugin;
    private final int cookTime;

    public ArtronPoweredRunnable(TARDIS plugin) {
        this.plugin = plugin;
        cookTime = 200 * this.plugin.getArtronConfig().getInt("artron_furnace.cook_time");
    }

    @Override
    public void run() {
        // get all TARDIS powered furnaces
        // TODO there should probably be a limit per TARDIS
        ResultSetPoweredFurnaces rs = new ResultSetPoweredFurnaces(plugin);
        rs.fetchAsync((hasResult, resultSetBlocks) -> {
            if (hasResult) {
                for (Pair<String, Integer> loc : rs.getData()) {
                    Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(loc.getFirst());
                    if (location != null && location.getBlock() instanceof Furnace furnace) {
                        if (plugin.getTardisHelper().isArtronFurnace(furnace.getBlock())) {
                            // power the furnace
                            furnace.setCookTimeTotal(cookTime);
                            TARDISArtronFurnaceListener.setLit(furnace.getBlock(), true);
                            // drain power from tardis
                            ArtronFurnaceUtils.drain(loc.getSecond(), plugin);
                        }
                    }
                }
            }
        });
    }
}
