package me.eccentric_nz.TARDIS.rooms.games.pong;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeData;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeTracker;
import me.eccentric_nz.TARDIS.rooms.games.GameUtils;
import me.eccentric_nz.TARDIS.rooms.games.tetris.GameState;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Pong implements Listener {

    private final TARDIS plugin;
    private List<UUID> uuids;
    private List<TextDisplay> displayList;
    private Location first;
    private char[][] CANVAS;
    private GameState state = GameState.INITIALIZING;
    private final Player player;
    private int tickTask = -1;
    private boolean hasMoved = false;
    private int paddleY = 8;
    private Ball ball;

    public Pong(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        startGame();
    }

    public void startGame() {
        // teleport to ARCADE room
        Pair<Location, Integer> arcade = getRoom(player);
        ArcadeTracker.PLAYERS.put(player.getUniqueId(), new ArcadeData(GameUtils.centre(player.getLocation()), player.getAllowFlight(), this, arcade.getSecond()));
        player.setAllowFlight(true);
        player.teleport(arcade.getFirst());
        if (state == GameState.INITIALIZING) {
            // get the text displays
            load();
            // reset the canvas to new game
            reset();
            state = GameState.PLAYING;
        }
    }

    private Pair<Location, Integer> getRoom(Player player) {
        // get the id of the TARDIS the player is in
        int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
        if (id != -1) {
            // get game record
            ResultSetGames rsg = new ResultSetGames(plugin);
            if (rsg.fromId(id)) {
                // get the pong_ids
                uuids = rsg.getPongUUIDs();
                // get the player location
                String playerLocation = rsg.getPlayerLocation();
                first = TARDISStaticLocationGetters.getLocationFromBukkitString(playerLocation);
                first.setYaw(180f);
                return new Pair<>(first, id);
            }
        }
        return null;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getPlayer() == player) {
            switch (e.getAction()) {
                case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> startTick();
                case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> pauseTick();
                default -> {
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer() == player) {
            double dz = e.getTo().getZ() - e.getFrom().getZ();
            // if not holding down
            if (!((dz > 0.1425 && dz < 0.2) || (dz < -0.1425 && dz > -0.2))) {
                hasMoved = false;
            }
            if (!hasMoved) {
                if (dz > 0) {
                    paddleDown();
                }
                if (dz < 0) {
                    paddleUp();
                }
                hasMoved = true;
            } else {
                hasMoved = false;
            }
            player.teleport(first);
        }
    }

    private void startTick() {
        tickTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ball.move();
            draw();
        }, 5L, 5L);
    }

    private void pauseTick() {
        plugin.getServer().getScheduler().cancelTask(tickTask);
        tickTask = -1;
    }

    private void paddleUp() {
        // upper bound = 1
        paddleY--;
        if (paddleY < 1) {
            paddleY = 1;
        }
        CANVAS[paddleY - 1][1] = GameChar.paddle;
        CANVAS[paddleY + 2][1] = GameChar.space;
    }

    private void paddleDown() {
        // lower bound = 14
        paddleY++;
        if (paddleY > 14) {
            paddleY = 14;
        }
        CANVAS[paddleY + 1][1] = GameChar.paddle;
        CANVAS[paddleY - 2][1] = GameChar.space;
    }

    private void draw() {
        ball();
        tardisPaddle();
        for (int i = 0; i < 16; i++) {
            char[] chars = CANVAS[i];
            String line = new String(chars);
            TextDisplay display = displayList.get(i);
            display.text(Component.text(line));
        }
    }

    private void ball() {
        // move ball
        ball.move();
    }

    private void tardisPaddle() {
        // get ball vector
        // move paddle towards ball
    }

    private void reset() {
        CANVAS = new char[16][29];
        for (int i = 0; i < 16; i++) {
            char[] line = Lines.CANVAS[i];
            CANVAS[i] = line;
        }
        int row = TARDISConstants.RANDOM.nextInt(2,14);
        CANVAS[row][14] = GameChar.ball;
        ball = new Ball(CANVAS, row);
        draw();
    }

    private void load() {
        displayList = new ArrayList<>(16);
        for (UUID uuid : uuids) {
            TextDisplay display = (TextDisplay) first.getWorld().getEntity(uuid);
            displayList.add(display);
            if (display == null) {
                plugin.debug("Could not get text display -> " + uuid);
            }
        }
    }
}
