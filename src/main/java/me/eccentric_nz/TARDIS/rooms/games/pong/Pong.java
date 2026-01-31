package me.eccentric_nz.TARDIS.rooms.games.pong;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeData;
import me.eccentric_nz.TARDIS.rooms.games.ArcadeTracker;
import me.eccentric_nz.TARDIS.rooms.games.GameState;
import me.eccentric_nz.TARDIS.rooms.games.GameUtils;
import me.eccentric_nz.TARDIS.rooms.games.tetris.GameSound;
import me.eccentric_nz.TARDIS.rooms.games.tictactoe.MatchState;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Chunk;
import org.bukkit.Input;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Pong implements Listener {

    private final TARDIS plugin;
    private final Player player;
    private final boolean[] dirtyRows = new boolean[16];
    int p = 0;
    int t = 0;
    private List<TextDisplay> displayList;
    private Location playerLocation;
    private int id = -1;
    private char[][] CANVAS;
    private GameState state = GameState.INITIALIZING;
    private int tickTask = -1;
    private int paddleY = 8;
    private Ball ball;
    private double period = 58.0d;

    public Pong(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        startGame();
    }

    public char[][] getCANVAS() {
        return CANVAS;
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
        player.playSound(playerLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        player.teleport(playerLocation);
        // get armour stand and put player in it
        for (Entity e : playerLocation.getChunk().getEntities()) {
            if (e instanceof ArmorStand stand) {
                stand.setRotation(180.0f, 0);
                stand.addPassenger(player);
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
                playerLocation.setYaw(180.0f);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        if (!ArcadeTracker.PLAYERS.containsKey(player.getUniqueId())) {
            return;
        }
        Entity entity = player.getVehicle();
        if (entity instanceof ArmorStand) {
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
        long p = Math.max((long) Math.ceil(period / 10.0d), 1);
        tickTask = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            ball.update();
            drawDirty();
        }, 10L, p);
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
        for (int p = 0; p < 16; p++) {
            setChar(p, 1, GameChar.space);
        }
        setChar(paddleY - 1, 1, GameChar.paddle);
        setChar(paddleY, 1, GameChar.paddle);
        setChar(paddleY + 1, 1, GameChar.paddle);
    }

    private void paddleDown() {
        // lower bound = 14
        paddleY++;
        if (paddleY > 14) {
            paddleY = 14;
        }
        for (int p = 0; p < 16; p++) {
            setChar(p, 1, GameChar.space);
        }
        setChar(paddleY + 1, 1, GameChar.paddle);
        setChar(paddleY, 1, GameChar.paddle);
        setChar(paddleY - 1, 1, GameChar.paddle);
    }

    private void drawDirty() {
        if (state == GameState.PLAYING) {
            tardisPaddle();
        }
        for (int i = 0; i < CANVAS.length; i++) {
            if (dirtyRows[i]) {
                displayList.get(i).text(Component.text(new String(CANVAS[i])));
                dirtyRows[i] = false;
            }
        }
    }

    public void setChar(int y, int x, char c) {
        if (CANVAS[y][x] != c) {
            CANVAS[y][x] = c;
            dirtyRows[y] = true;
        }
    }

    private void tardisPaddle() {
        // move paddle towards ball
        int targetY = predictBallY(ball);
        int imperfection = 3; // lower = stronger AI
        if (TARDISConstants.RANDOM.nextInt(imperfection) == 0) {
            targetY += TARDISConstants.RANDOM.nextBoolean() ? 1 : -1;
        }
        int clamped = Math.clamp(targetY, 1, 14);
        for (int p = 0; p < 16; p++) {
            setChar(p, 27, GameChar.space);
        }
        setChar(clamped + 1, 27, GameChar.paddle);
        setChar(clamped, 27, GameChar.paddle);
        setChar(clamped - 1, 27, GameChar.paddle);
    }

    private int predictBallY(Ball ball) {
        // ball moving away → dumb tracking
        if (ball.vx < 0 && ball.x < 27) {
            return ball.y;
        }
        int simY = ball.y;
        int vy = ball.vy;
        simY += vy;
        return simY;
    }

    protected void reset() {
        if (state != GameState.INITIALIZING) {
            pauseTick();
        }
        this.CANVAS = new char[16][29];
        for (int i = 0; i < 16; i++) {
            char[] line = Lines.newCanvas()[i];
            this.CANVAS[i] = line;
        }
        int row = TARDISConstants.RANDOM.nextInt(2, 14);
        this.CANVAS[row][14] = GameChar.ball;
        paddleY = 8;
        if (state == GameState.INITIALIZING) {
            ball = new Ball(row, this);
            updateScore(MatchState.PLAYER_WON);
        }
        ball.y = row;
        ball.x = 14;
        int[] d = GameUtils.DIRECTIONS[TARDISConstants.RANDOM.nextInt(GameUtils.DIRECTIONS.length)];
        ball.vx = d[0];
        ball.vy = d[1];
        Arrays.fill(dirtyRows, true);
        drawDirty();
        if (state != GameState.INITIALIZING && state != GameState.GAME_OVER) {
            startTick();
        }
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
        state = GameState.INITIALIZING;
        plugin.getServer().getScheduler().cancelTask(tickTask);
        reset();
        UUID uuid = player.getUniqueId();
        ArcadeData data = ArcadeTracker.PLAYERS.get(uuid);
        // teleport player back to games room
        player.setFallDistance(0.0f);
        player.playSound(playerLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
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

    public void updateScore(MatchState point) {
        String score = ComponentUtils.stripColour(displayList.getLast().text());
        if (!score.equals("Score") && this.state != GameState.INITIALIZING) {
            String[] split = score.split(" \\| ");
            p = TARDISNumberParsers.parseInt(split[0].split(" ")[1]);
            t = TARDISNumberParsers.parseInt(split[1].split(" ")[1]);
            if (point == MatchState.PLAYER_WON) {
                p++;
                if (p == 11) {
                    state = GameState.GAME_OVER;
                    endGame(MatchState.PLAYER_WON);
                }
            }
            if (point == MatchState.TARDIS_WON) {
                t++;
                if (t == 11) {
                    state = GameState.GAME_OVER;
                    endGame(MatchState.TARDIS_WON);
                }
            }
        }
        displayList.getLast().text(Component.text(String.format("Player %s | TARDIS %s", p, t), NamedTextColor.GOLD));
    }

    private void endGame(MatchState match) {
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.3f, 0.7f);
        new BukkitRunnable() {
            float p = 1.8f;

            @Override
            public void run() {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.2f, p);
                p *= 0.9f;
                if (p < 0.5f) {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 3, 1);
        String who = match == MatchState.TARDIS_WON ? "TARDIS" : "You";
        plugin.getMessenger().message(player, "Game Over! " + who + " won. Sneak to exit.");
    }

    public void updatePaddle(PaddlePosition position) {
        switch (position) {
            case PLAYER_TOP, PLAYER_MIDDLE, PLAYER_BOTTOM -> {
                setChar(paddleY + 1, 1, GameChar.paddle);
                setChar(paddleY, 1, GameChar.paddle);
                setChar(paddleY - 1, 1, GameChar.paddle);
            }
            default -> { }
        }
    }

    public void playSound(GameSound gameSound) {
        float volume;
        float pitch;
        switch (gameSound) {
            case PADDLE -> {
                volume = 0.2f;
                pitch = 0.5f;
            }
            case POINT -> {
                volume = 0.3f;
                pitch = 1.0f;
            }
            case BOUNCE -> {
                volume = 0.1f;
                pitch = 1.6f;
            }
            default -> {
                volume = 0;
                pitch = 0;
            }
        }
        if (volume > 0) {
            player.playSound(playerLocation, gameSound.getSound(), volume, pitch);
        }
    }

    public void reducePeriod() {
        period -= 3.333d;
    }
}
