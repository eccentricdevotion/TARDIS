package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTransmat;
import me.eccentric_nz.TARDIS.desktop.PreviewData;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.rooms.debug.DebugPopulator;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.UUID;

public class DebugCommand {

    private final TARDIS plugin;

    public DebugCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean process(CommandSender sender, String[] args) {
        String param = args[1].toLowerCase(Locale.ROOT);
        switch (param) {
            case "enter" -> enter(sender);
            case "exit" -> exit(sender);
            default -> {
                if (param.equals("create") || param.equals("update")) {
                    create(param.equals("update"));
                }
            }
        }
        return true;
    }

    private void enter(CommandSender sender) {
        if (sender instanceof Player player) {
            // get the transmat location
            ResultSetTransmat rst = new ResultSetTransmat(plugin, -50, "debug_preview");
            if (rst.resultSet()) {
                Location transmat = rst.getLocation();
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_DEBUG");
                transmat.setYaw(rst.getYaw());
                transmat.setPitch(player.getLocation().getPitch());
                // start tracking player
                plugin.getTrackerKeeper().getPreviewers().put(player.getUniqueId(), new PreviewData(player.getLocation().clone(), player.getGameMode()));
                // transmat to preview desktop
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    // set gamemode
                    player.setGameMode(GameMode.ADVENTURE);
                    player.playSound(transmat, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    player.teleport(transmat);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "PREVIEW_DEBUG");
                }, 10L);
            }
        }
    }

    private void exit(CommandSender sender) {
        if (sender instanceof Player player) {
            UUID uuid = player.getUniqueId();
            PreviewData data = plugin.getTrackerKeeper().getPreviewers().get(uuid);
            if (data != null) {
                Location transmat = data.location();
                if (transmat != null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
                    plugin.getTrackerKeeper().getPreviewers().remove(uuid);
                    // transmat to preview desktop
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        // set gamemode
                        player.playSound(transmat, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.teleport(transmat);
                        player.setGameMode(data.gamemode());
                    }, 10L);
                }
            }
        }
    }

    private void create(boolean update) {
        // get default world
        if (plugin.getConfig().getBoolean("creation.default_world")) {
            String dn = plugin.getConfig().getString("creation.default_world_name", "TARDIS_TimeVortex");
            World world = plugin.getServer().getWorld(dn);
            if (world != null) {
                DebugPopulator populator = new DebugPopulator(plugin, world);
                populator.createBase(update);
                populator.items();
                populator.blocks();
                populator.monsters();
                populator.chameleon();
                populator.doors();
                populator.rotors();
                populator.gui();
                populator.consoles();
                populator.chemistry();
                populator.sonicAndKeys();
                populator.regeneration();
                populator.handles();
                populator.lazarus();
            }
        }
    }
}
