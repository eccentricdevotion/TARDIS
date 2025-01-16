package me.eccentric_nz.TARDIS.artron;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISClaimEvent;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISAbandonCommand;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.doors.DoorCloserAction;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.Player;

public class ArtronAbandoned {

    private final TARDIS plugin;

    public ArtronAbandoned(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean claim(Player player, int id, Location location, Tardis tardis) {
            // transfer ownership to the player who clicked
            boolean pu = plugin.getQueryFactory().claimTARDIS(player, id);
            // make sure player is added as owner of interior WorldGuard region
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                plugin.getWorldGuardUtils().updateRegionForClaim(location, player.getUniqueId());
            }
            ResultSetCurrentFromId rscl = new ResultSetCurrentFromId(plugin, id);
            if (rscl.resultSet()) {
                Location current = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
                if (pu) {
                    new DoorCloserAction(plugin, player.getUniqueId(), id).closeDoors();
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDON_CLAIMED");
                    plugin.getPM().callEvent(new TARDISClaimEvent(player, tardis, current));
                }
                if (plugin.getConfig().getBoolean("police_box.name_tardis")) {
                    ChameleonPreset preset = tardis.getPreset();
                    Sign sign = TARDISAbandonCommand.getSign(current, rscl.getDirection(), preset);
                    if (sign != null) {
                        SignSide front = sign.getSide(Side.FRONT);
                        String player_name = TARDISStaticUtils.getNick(player);
                        String owner;
                        if (preset.equals(ChameleonPreset.GRAVESTONE) || preset.equals(ChameleonPreset.PUNKED) || preset.equals(ChameleonPreset.ROBOT)) {
                            owner = (player_name.length() > 14) ? player_name.substring(0, 14) : player_name;
                        } else {
                            owner = (player_name.length() > 14) ? player_name.substring(0, 12) + "'s" : player_name + "'s";
                        }
                        switch (preset) {
                            case GRAVESTONE -> front.setLine(3, owner);
                            case ANGEL, JAIL -> front.setLine(2, owner);
                            default -> front.setLine(0, owner);
                        }
                        sign.update();
                    }
                }
            }
            return pu;
    }
}
