package me.eccentric_nz.TARDIS.rooms.games;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.rooms.games.pong.Init;
import me.eccentric_nz.TARDIS.rooms.games.pong.Pong;
import me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors.StoneMagmaIceInventory;
import me.eccentric_nz.TARDIS.rooms.games.tetris.Game;
import me.eccentric_nz.TARDIS.rooms.games.tetris.Play;
import me.eccentric_nz.TARDIS.rooms.games.tictactoe.NoughtsAndCrossesInventory;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GamesListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public GamesListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onGamesGUIClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof GamesInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 8) {
            return;
        }
        InventoryView view = event.getView();
        if (view.getItem(slot) == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        switch (slot) {
            case 2 -> {
                // start pong game
                close(player);
                new Init(plugin).startGame(player);
            }
            // start Stone Magma Ice game
            case 3 -> player.openInventory(new StoneMagmaIceInventory(plugin).getInventory());
            // start tic tac toe game
            case 4 -> player.openInventory(new NoughtsAndCrossesInventory(plugin).getInventory());
            case 5 -> {
                // check the player has nothing in slot 0
                PlayerInventory playerInventory = player.getInventory();
                if (playerInventory.getItem(0) != null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "GAME_ITEM_SLOT");
                    return;
                }
                // get level
                ItemStack startLevel = view.getItem(6);
                ItemMeta sim = startLevel.getItemMeta();
                List<Component> lore = sim.lore();
                int level = (lore != null) ? TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.getFirst())) : 0;
                if (level < 0) {
                    level = 0;
                }
                // start tetris game
                close(player);
                new Play(plugin).startGame(player, level);
            }
            case 6 -> {
                // set level
                ItemStack startLevel = view.getItem(6);
                ItemMeta sim = startLevel.getItemMeta();
                List<Component> lore = sim.lore();
                int level = (lore != null) ? TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.getFirst())) : 0;
                level++;
                if (level > 30) {
                    level = 0;
                }
                lore.set(0, Component.text(level));
                sim.lore(lore);
                startLevel.setItemMeta(sim);
                view.setItem(6, startLevel);
            }
            case 8 -> close(player);
            default -> { }
        }
    }

    @EventHandler
    public void onGameSneak(PlayerToggleSneakEvent event) {
        if (ArcadeTracker.PLAYERS.containsKey(event.getPlayer().getUniqueId()) && event.isSneaking()) {
            Player player = event.getPlayer();
            ArcadeData data = ArcadeTracker.PLAYERS.get(player.getUniqueId());
            // teleport player back to games room
            player.setAllowFlight(true);
            player.setFlying(true);
            player.teleport(data.backup());
            // reset flight status
            player.setAllowFlight(data.allowFlight());
            player.setFlying(false);
            if (data.listener() instanceof Game game) {
                HandlerList.unregisterAll(game);
            }
            if (data.listener() instanceof Pong pong) {
                HandlerList.unregisterAll(pong);
            }
            UUID uuid = player.getUniqueId();
            int id = data.id();
            ArcadeTracker.PLAYERS.remove(uuid);
            // put player back in travellers table
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("uuid", uuid.toString());
            plugin.getQueryFactory().doInsert("travellers", where);
        }
    }
}
