package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISWorldBorderChecker;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;

public class TARDISBiomeFinder implements Runnable {

    private final TARDIS plugin;
    private final Player p;
    private final int startx;
    private final int startz;
    private final int step = 64;
    private int plus = 0;
    private int taskid, limite, limits, limitw, limitn, i = 0;
    private final Biome b;
    private final World w;
    Integer[] directions = new Integer[]{0, 1, 2, 3};
    private Location location = null;

    public TARDISBiomeFinder(TARDIS plugin, Player p, int startx, int startz, Biome b, World w) {
        this.plugin = plugin;
        this.p = p;
        this.startx = startx;
        this.startz = startz;
        this.b = b;
        this.w = w;
        Collections.shuffle(Arrays.asList(directions));
        limite = startx + 30000;
        limits = startz + 30000;
        limitw = startx - 30000;
        limitn = startz - 30000;
        if (plugin.getPM().isPluginEnabled("WorldBorder")) {
            // get the border limit for this world
            TARDISWorldBorderChecker wb = new TARDISWorldBorderChecker(plugin);
            int[] data = wb.getBorderDistance(w.getName());
            limite = data[0];
            limits = data[1];
            limitw = -data[0];
            limitn = -data[1];
        }
    }

    @Override
    public void run() {
        if (location == null) {
            switch (directions[i]) {
                case 0:
                    // east
                    int east = startx + plus;
                    Biome chkbe = w.getBiome(east, w.getHighestBlockYAt(east, startz), startz);
                    if (chkbe.equals(b)) {
                        TARDISMessage.send(p, "BIOME_E", b.toString());
                        location = new Location(w, east, TARDISStaticLocationGetters.getHighestYin3x3(w, east, startz), startz);
                    }
                    plus += step;
                    if (east >= limite) {
                        i++;
                        plus = 0;
                    }
                    break;
                case 1:
                    // south
                    int south = startz + plus;
                    Biome chkbs = w.getBiome(startx, w.getHighestBlockYAt(startx, south), south);
                    if (chkbs.equals(b)) {
                        TARDISMessage.send(p, "BIOME_S", b.toString());
                        location = new Location(w, startx, TARDISStaticLocationGetters.getHighestYin3x3(w, startx, south), south);
                    }
                    plus += step;
                    if (south >= limits) {
                        i++;
                        plus = 0;
                    }
                    break;
                case 2:
                    // west
                    int west = startx - plus;
                    Biome chkbw = w.getBiome(west, w.getHighestBlockYAt(west, startz), startz);
                    if (chkbw.equals(b)) {
                        TARDISMessage.send(p, "BIOME_W", b.toString());
                        location = new Location(w, west, TARDISStaticLocationGetters.getHighestYin3x3(w, west, startz), startz);
                    }
                    plus -= step;
                    if (west <= limitw) {
                        i++;
                        plus = 0;
                    }
                    break;
                case 3:
                    // north
                    int north = startz - plus;
                    Biome chkbn = w.getBiome(startx, w.getHighestBlockYAt(startx, north), north);
                    if (chkbn.equals(b)) {
                        TARDISMessage.send(p, "BIOME_N", b.toString());
                        location = new Location(w, startx, TARDISStaticLocationGetters.getHighestYin3x3(w, startx, north), north);
                    }
                    plus -= step;
                    if (north <= limitn) {
                        i++;
                        plus = 0;
                    }
                    break;
            }
        }
    }

    public void setTaskid(int taskid) {
        this.taskid = taskid;
    }

    public int getTaskid() {
        return taskid;
    }

    public boolean poll() {
        return location != null;
    }

    public Location getLocation() {
        return location;
    }
}
