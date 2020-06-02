package me.eccentric_nz.TARDIS.commands.utils;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TARDISWeatherListener extends TARDISMenuListener implements Listener {

    private final TARDIS plugin;

    public TARDISWeatherListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onWeatherMenuInteract(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String name = view.getTitle();
        if (name.equals(ChatColor.DARK_RED + "TARDIS Weather Menu")) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            Player player = (Player) event.getWhoClicked();
            if (slot >= 0 && slot < 9) {
                ItemStack is = view.getItem(slot);
                if (is != null) {
                    // get the TARDIS the player is in
                    HashMap<String, Object> wheres = new HashMap<>();
                    wheres.put("uuid", player.getUniqueId().toString());
                    ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
                    if (rst.resultSet()) {
                        int id = rst.getTardis_id();
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            // check they initialised
                            if (!tardis.isTardis_init()) {
                                TARDISMessage.send(player, "ENERGY_NO_INIT");
                                return;
                            }
                            if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered_on() && slot != 6 && slot != 13 && slot != 20) {
                                TARDISMessage.send(player, "POWER_DOWN");
                                return;
                            }
                            if (!tardis.isHandbrake_on()) {
                                TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
                                return;
                            }
                            // get current location
                            HashMap<String, Object> wherec = new HashMap<>();
                            wherec.put("tardis_id", tardis.getTardis_id());
                            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
                            if (!rsc.resultSet()) {
                                TARDISMessage.send(player, "CURRENT_NOT_FOUND");
                                close(player);
                            }
                            switch (slot) {
                                case 0:
                                    // clear / sun
                                    if (player.hasPermission("tardis.weather.clear")) {
                                        TARDISWeather.setClear(rsc.getWorld());
                                        TARDISMessage.send(player, "WEATHER_SET", "clear");
                                    } else {
                                        TARDISMessage.send(player, "NO_PERMS");
                                    }
                                    close(player);
                                    break;
                                case 1:
                                    // rain
                                    if (player.hasPermission("tardis.weather.rain")) {
                                        TARDISWeather.setRain(rsc.getWorld());
                                        TARDISMessage.send(player, "WEATHER_SET", "rain");
                                    } else {
                                        TARDISMessage.send(player, "NO_PERMS");
                                    }
                                    close(player);
                                    break;
                                case 2:
                                    // thunderstorm
                                    if (player.hasPermission("tardis.weather.thunder")) {
                                        TARDISWeather.setThunder(rsc.getWorld());
                                        TARDISMessage.send(player, "WEATHER_SET", "thunder");
                                    } else {
                                        TARDISMessage.send(player, "NO_PERMS");
                                    }
                                    close(player);
                                    break;
                                case 5:
                                    // atmospheric excitation
                                    if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
                                        TARDISMessage.send(player, "CMD_EXCITE");
                                        return;
                                    }
                                    new TARDISAtmosphericExcitation(plugin).excite(tardis.getTardis_id(), player);
                                    plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
                                    close(player);
                                    break;
                                case 8:
                                    // close
                                    close(player);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
