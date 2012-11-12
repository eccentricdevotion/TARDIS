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

    static TARDISDatabase service = TARDISDatabase.getInstance();
    Statement statement;
    private static Random rand = new Random();

    public static void randomTARDISSound() {
        if (TARDIS.plugin.config.getBoolean("sfx") == true) {
            try {
                String queryTravellers = "SELECT player FROM travellers";
                Connection connection = service.getConnection();
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(queryTravellers);
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
                rs.close();
                statement.close();
            } catch (SQLException e) {
                System.err.println(Constants.MY_PLUGIN_NAME + " SFX error: " + e);
            }
        }
    }
}
