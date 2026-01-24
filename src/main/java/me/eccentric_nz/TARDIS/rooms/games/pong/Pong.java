package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeData;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeTracker;
import me.eccentric_nz.TARDIS.rooms.games.GameState;
import me.eccentric_nz.TARDIS.rooms.games.GameUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Input;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Pong implements Listener {

    private final TARDIS plugin;
    private final Player player;
    private List<TextDisplay> displayList;
    private Location playerLocation;
    private int id = -1;
    private char[][] CANVAS;
    private GameState state = GameState.INITIALIZING;
    private int tickTask = -1;
    private boolean hasMoved = false;
    private int paddleY = 8;
    private int tardisY = 8;
    private Ball ball;

    public Pong(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        startGame();
    }

    public void startGame() {
        // teleport to ARCADE room
        getRoom(player);
        ArcadeTracker.PLAYERS.put(player.getUniqueId(), new ArcadeData(GameUtils.centre(player.getLocation()), player.getAllowFlight(), this, id));
        // load the text displays chunk
        Chunk displayChunk = playerLocation.getWorld().getChunkAt(playerLocation.getBlock().getRelative(BlockFace.NORTH, 8));
        while (!displayChunk.isLoaded()) {
            displayChunk.load();
        }
        player.setFallDistance(0.0f);
        player.teleport(playerLocation);
        // get boat and put player in it
        for (Entity e : playerLocation.getChunk().getEntities()) {
            if (e instanceof Boat boat) {
                boat.addPassenger(player);
            }
        }
        player.setFallDistance(0.0f);
        if (state == GameState.INITIALIZING) {
            // get the text displays
            load();
            // reset the canvas to new game
            reset();
            state = GameState.PLAYING;
        }
    }

    private void getRoom(Player player) {
        // get the id of the TARDIS the player is in
        id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
        if (id != -1) {
            // get game record
            ResultSetGames rsg = new ResultSetGames(plugin);
            if (rsg.fromId(id)) {
                // get the player location
                String location = rsg.getPlayerLocation();
                playerLocation = TARDISStaticLocationGetters.getLocationFromBukkitString(location);
                playerLocation.setYaw(180f);
            }
        }
    }

//    @EventHandler
//    public void onClick(PlayerInteractEvent e) {
//        if (e.getPlayer() == player) {
//            switch (e.getAction()) {
//                case RIGHT_CLICK_AIR, RIGHT_CLICK_BLOCK -> startTick();
//                case LEFT_CLICK_AIR, LEFT_CLICK_BLOCK -> pauseTick();
//                default -> {
//                }
//            }
//        }
//    }

//    @EventHandler
//    public void onMove(PlayerMoveEvent e) {
//        if (e.getPlayer() == player) {
//            double dz = e.getTo().getZ() - e.getFrom().getZ();
//            // if not holding down
//            if (!((dz > 0.1425 && dz < 0.2) || (dz < -0.1425 && dz > -0.2))) {
//                hasMoved = false;
//            }
//            if (!hasMoved) {
//                if (dz > 0) {
//                    paddleDown();
//                }
//                if (dz < 0) {
//                    paddleUp();
//                }
//                hasMoved = true;
//            } else {
//                hasMoved = false;
//            }
//            player.teleport(playerLocation);
//        }
//    }

//    @EventHandler
//    public void onGameSneak(PlayerToggleSneakEvent event) {
//        if (ArcadeTracker.PLAYERS.containsKey(event.getPlayer().getUniqueId()) && event.isSneaking()) {
//            abort();
//        }
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        if (!ArcadeTracker.PLAYERS.containsKey(event.getPlayer().getUniqueId()) ) {
            return;
        }
        Entity entity = player.getVehicle();
        if (entity instanceof Boat) {
            Input input = event.getInput();
            if (input.isSneak()) {
                abort();
            }
            if (input.isJump()) {
                if (tickTask == -1) {
                    startTick();
                } else {
                    pauseTick();
                }
            }
            if (input.isForward()) {
                paddleUp();
            }
            if (input.isBackward()) {
                paddleDown();
            }
        }
    }

    private void startTick() {
        tickTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ball.move();
            draw();
        }, 15L, 15L);
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

    private void tardisUp() {
        // upper bound = 1
        tardisY--;
        if (tardisY < 1) {
            tardisY = 1;
        }
        CANVAS[tardisY - 1][27] = GameChar.paddle;
        CANVAS[tardisY + 2][27] = GameChar.space;
    }

    private void tardisDown() {
        // lower bound = 14
        tardisY++;
        if (tardisY > 14) {
            tardisY = 14;
        }
        CANVAS[tardisY + 1][27] = GameChar.paddle;
        CANVAS[tardisY - 2][27] = GameChar.space;
    }

    private void draw() {
        if (state == GameState.PLAYING) {
            ball.move();
            tardisPaddle();
        }
        for (int i = 0; i < 16; i++) {
            char[] chars = CANVAS[i];
            String line = new String(chars);
            if (state == GameState.INITIALIZING) {
                plugin.debug(line);
            }
            TextDisplay display = displayList.get(i);
            display.text(Component.text(line));
        }
    }

    private void tardisPaddle() {
        // move paddle towards ball
        if (tardisY < ball.by) {
            tardisDown();
        } else {
            tardisUp();
        }
    }

    private void reset() {
        CANVAS = new char[16][29];
        for (int i = 0; i < 16; i++) {
            char[] line = Lines.CANVAS[i];
            CANVAS[i] = line;
        }
        int row = TARDISConstants.RANDOM.nextInt(2, 14);
        CANVAS[row][14] = GameChar.ball;
        paddleY = 8;
        tardisY = 8;
        ball = new Ball(CANVAS, row);
        draw();
    }

    private void load() {
        // get the pong_ids
        ResultSetGames rsg = new ResultSetGames(plugin);
        if (rsg.fromId(id)) {
            displayList = new ArrayList<>(16);
            for (UUID uuid : rsg.getPongUUIDs()) {
                TextDisplay display = (TextDisplay) playerLocation.getWorld().getEntity(uuid);
                displayList.add(display);
                if (display == null) {
                    plugin.debug("Could not get text display -> " + uuid);
                    abort();
                    return;
                }
            }
        } else {
            // couldn't get text displays
            // return player to TARDIS
            abort();
        }
    }

    private void abort() {
        state = GameState.GAME_OVER;
        plugin.getServer().getScheduler().cancelTask(tickTask);
        reset();
        UUID uuid = player.getUniqueId();
        ArcadeData data = ArcadeTracker.PLAYERS.get(uuid);
        // teleport player back to games room
        player.setFallDistance(0.0f);
        player.teleport(data.backup());
        player.setFallDistance(0.0f);
        // reset flight status
        player.setAllowFlight(data.allowFlight());
        HandlerList.unregisterAll(this);
        int id = data.id();
        ArcadeTracker.PLAYERS.remove(uuid);
        // put player back in travellers table
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("uuid", uuid.toString());
        plugin.getQueryFactory().doInsert("travellers", where);
    }
}
