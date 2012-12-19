package me.eccentric_nz.plugins.TARDIS;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class TARDISWorldGuardChecker {

    private TARDIS plugin;
    private WorldGuardPlugin wg;

    public TARDISWorldGuardChecker(TARDIS plugin) {
        this.plugin = plugin;
        if (plugin.worldGuardOnServer) {
        wg = (WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        }
    }

    public boolean cantBuild(Player p, Location l) {
        return (plugin.worldGuardOnServer) && (!wg.canBuild(p, l));
    }

    public void addWGProtection(Player p, Location one, Location two) {
        RegionManager rm = wg.getRegionManager(one.getWorld());
        BlockVector b1 = makeBlockVector(one);
        BlockVector b2 = makeBlockVector(two);
        ProtectedCuboidRegion region = new ProtectedCuboidRegion("tardis_" + p.getName(), b1, b2);
        DefaultDomain dd = new DefaultDomain();
        dd.addPlayer(p.getName());
        region.setOwners(dd);
        HashMap<Flag<?>, Object> flags = new HashMap<Flag<?>, Object>();
        flags.put(DefaultFlag.TNT, State.DENY);
        flags.put(DefaultFlag.CREEPER_EXPLOSION, State.DENY);
        flags.put(DefaultFlag.FIRE_SPREAD, State.DENY);
        flags.put(DefaultFlag.LAVA_FIRE, State.DENY);
        flags.put(DefaultFlag.LAVA_FLOW, State.DENY);
        flags.put(DefaultFlag.LIGHTER, State.DENY);
        flags.put(DefaultFlag.MOB_SPAWNING, State.DENY);
        flags.put(DefaultFlag.MOB_DAMAGE, State.DENY);
        flags.put(DefaultFlag.CONSTRUCT, RegionGroup.OWNERS);
        flags.put(DefaultFlag.CHEST_ACCESS, State.ALLOW);
        region.setFlags(flags);
        rm.addRegion(region);
        try {
            rm.save();
        } catch (ProtectionDatabaseException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " could not create WorldGuard Protection! " + e);
        }
    }

    public void removeRegion(World w, String p) {
        RegionManager rm = wg.getRegionManager(w);
        rm.removeRegion("tardis_" + p);
        try {
            rm.save();
        } catch (ProtectionDatabaseException e) {
            System.err.println(Constants.MY_PLUGIN_NAME + " could not remove WorldGuard Protection! " + e);
        }
    }

    public BlockVector makeBlockVector(Location location) {
        return new BlockVector(location.getX(), location.getY(), location.getZ());
    }
}