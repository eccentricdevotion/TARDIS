package me.eccentric_nz.plugins.TARDIS;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.getspout.spoutapi.SpoutManager;

public class TARDISSounds {

    private static TARDISDatabase service = TARDISDatabase.getInstance();
    private static Random rand = new Random();
    private static Statement statement;
    private static ResultSet rs;

    public static void randomTARDISSound() {
        if (TARDIS.plugin.getConfig().getBoolean("sfx") == true) {
            try {
                String queryTravellers = "SELECT player FROM travellers";
                Connection connection = service.getConnection();
                statement = connection.createStatement();
                rs = statement.executeQuery(queryTravellers);
                while (rs.next()) {
                    String playerNameStr = rs.getString("player");
                    String querySFX = "SELECT sfx_on FROM player_prefs WHERE player = '" + playerNameStr + "'";
                    ResultSet rsSFX = statement.executeQuery(querySFX);
                    boolean userSFX;
                    if (rsSFX.next()) {
                        userSFX = rsSFX.getBoolean("sfx_on");
                    } else {
                        userSFX = true;
                    }
                    rsSFX.close();
                    final Player player = Bukkit.getServer().getPlayer(playerNameStr);
                    if (player != null) {
                        if (SpoutManager.getPlayer(player).isSpoutCraftEnabled() && userSFX) {
                            int i = rand.nextInt(28);
                            final String sfx = "https://dl.dropbox.com/u/53758864/soundeffects/drwho" + i + ".mp3";
                            final Location location = player.getLocation();
                            SpoutManager.getSoundManager().playCustomSoundEffect(TARDIS.plugin, SpoutManager.getPlayer(player), sfx, false, location, 9, 75);
                        }
                    }
                }
            } catch (SQLException e) {
                TARDIS.plugin.console.sendMessage(TARDIS.plugin.pluginName + " SFX error: " + e);
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                    }
                }
                try {
                    statement.close();
                } catch (Exception e) {
                }
            }
        }
    }
}