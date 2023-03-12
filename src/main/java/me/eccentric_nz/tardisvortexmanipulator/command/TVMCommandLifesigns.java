package me.eccentric_nz.tardisvortexmanipulator.command;

import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.tardisvortexmanipulator.TARDISVortexManipulator;
import me.eccentric_nz.tardisvortexmanipulator.TVMUtils;
import me.eccentric_nz.tardisvortexmanipulator.database.TVMQueryFactory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TVMCommandLifesigns implements CommandExecutor {

    private final TARDISVortexManipulator plugin;

    public TVMCommandLifesigns(TARDISVortexManipulator plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("vml")) {
            Player p = null;
            if (sender instanceof Player) {
                p = (Player) sender;
            }
            if (p == null) {
                sender.sendMessage(plugin.getPluginName() + "That command cannot be used from the console!");
                return true;
            }
            if (!p.hasPermission("vm.lifesigns")) {
                p.sendMessage(plugin.getPluginName() + "You don't have permission to use that command!");
                return true;
            }
            ItemStack is = p.getItemInHand();
            if (is != null && is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equals("Vortex Manipulator")) {
                int required = plugin.getConfig().getInt("tachyon_use.lifesigns");
                if (!TVMUtils.checkTachyonLevel(p.getUniqueId().toString(), required)) {
                    p.sendMessage(plugin.getPluginName() + "You don't have enough tachyons to use the lifesigns scanner!");
                    return true;
                }
                // remove tachyons
                new TVMQueryFactory(plugin).alterTachyons(p.getUniqueId().toString(), -required);
                if (args.length == 0) {
                    p.sendMessage(plugin.getPluginName() + "Nearby entities:");
                    // scan nearby entities
                    double d = plugin.getConfig().getDouble("lifesign_scan_distance");
                    List<Entity> ents = p.getNearbyEntities(d, d, d);
                    if (ents.size() > 0) {
                        // record nearby entities
                        HashMap<EntityType, Integer> scannedentities = new HashMap<>();
                        List<String> playernames = new ArrayList<>();
                        for (Entity k : ents) {
                            EntityType et = k.getType();
                            if (TARDISConstants.ENTITY_TYPES.contains(et)) {
                                Integer entity_count = (scannedentities.containsKey(et)) ? scannedentities.get(et) : 0;
                                boolean visible = true;
                                if (et.equals(EntityType.PLAYER)) {
                                    Player entPlayer = (Player) k;
                                    if (p.canSee(entPlayer)) {
                                        playernames.add(entPlayer.getName());
                                    } else {
                                        visible = false;
                                    }
                                }
                                if (visible) {
                                    scannedentities.put(et, entity_count + 1);
                                }
                            }
                        }
                        for (Map.Entry<EntityType, Integer> entry : scannedentities.entrySet()) {
                            String message = "";
                            StringBuilder buf = new StringBuilder();
                            if (entry.getKey().equals(EntityType.PLAYER) && playernames.size() > 0) {
                                playernames.forEach((pn) -> {
                                    buf.append(", ").append(pn);
                                });
                                message = " (" + buf.toString().substring(2) + ")";
                            }
                            p.sendMessage("    " + entry.getKey() + ": " + entry.getValue() + message);
                        }
                        scannedentities.clear();
                    } else {
                        p.sendMessage("No nearby entities.");
                    }
                }
                if (args.length < 1) {
                    p.sendMessage(plugin.getPluginName() + "You need to specify a player name!");
                    return true;
                }
                Player scanned = plugin.getServer().getPlayer(args[0]);
                if (scanned == null) {
                    p.sendMessage(plugin.getPluginName() + "Could not find a player with that name!");
                    return true;
                }
                if (!scanned.isOnline()) {
                    p.sendMessage(plugin.getPluginName() + args[0] + " is not online!");
                    return true;
                }
                // getHealth() / getMaxHealth() * getHealthScale()
                double health = scanned.getHealth() / scanned.getMaxHealth() * scanned.getHealthScale();
                float hunger = (scanned.getFoodLevel() / 20F) * 100;
                int air = scanned.getRemainingAir();
                p.sendMessage(plugin.getPluginName() + args[0] + "'s lifesigns:");
                p.sendMessage("Has been alive for: " + TVMUtils.convertTicksToTime(scanned.getTicksLived()));
                p.sendMessage("Health: " + String.format("%.1f", health / 2) + " hearts");
                p.sendMessage("Hunger bar: " + String.format("%.2f", hunger) + "%");
                p.sendMessage("Air: ~" + (air / 20) + " seconds remaining");
                return true;
            } else {
                p.sendMessage(plugin.getPluginName() + "You don't have a Vortex Manipulator in your hand!");
                return true;
            }
        }
        return false;
    }
}
