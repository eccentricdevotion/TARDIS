package me.eccentric_nz.TARDIS.rooms.games.tetris;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetGames;
import me.eccentric_nz.TARDIS.rooms.games.*;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInputEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class Game implements Listener {

    private final TARDIS plugin;
    private final Board board;
    private final NextPiece nextPiece;
    private final int startLevel;
    private final Player player;
    private final Location boardLocation;
    private final Location signLocation;
    private final World world;
    private GameState state = GameState.INITIALIZING;
    private Pieces current;
    private int level, linesClearedLevel;
    private long score, linesClearedTotal;

    public Game(TARDIS plugin, Player player, int startLevel) {
        this.plugin = plugin;
        this.player = player;
        world = plugin.getServer().getWorld("TARDIS_Zero_Room");
        GameLocations locations = getRoom(player);
        ArcadeTracker.PLAYERS.put(player.getUniqueId(), new ArcadeData(GameUtils.centre(player.getLocation()), player.getAllowFlight(), this, locations.id()));
        player.setFallDistance(0.0f);
        player.teleport(locations.teleport());
        // set as armour stand passenger
        for (Entity e : locations.teleport().getChunk().getEntities()) {
            if (e instanceof ArmorStand stand) {
                stand.setRotation(0,0);
                stand.addPassenger(player);
            }
        }
        player.setFallDistance(0.0f);
        boardLocation = locations.board();
        signLocation = locations.sign();
        board = new Board(10, 20, this);
        // reset board
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                Block block = world.getBlockAt(boardLocation.getBlockX() - x, boardLocation.getBlockY() - y, boardLocation.getBlockZ());
                block.setType(Material.BLACK_WOOL);
            }
        }
        nextPiece = new NextPiece();
        this.startLevel = startLevel;
        level = startLevel;
        updateScoreboard();
        start();
    }

    private GameLocations getRoom(Player player) {
        // get the id of the TARDIS the player is in
        int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player.getUniqueId());
        if (id != -1) {
            // get game record
            ResultSetGames rsg = new ResultSetGames(plugin);
            if (rsg.fromId(id)) {
                // get the player location
                String playerLocation = rsg.getPlayerLocation();
                Location first = TARDISStaticLocationGetters.getLocationFromBukkitString(playerLocation);
                String tetrisLocation = rsg.getTetrisBoard();
                Location second = TARDISStaticLocationGetters.getLocationFromBukkitString(tetrisLocation);
                String tetrisSign = rsg.getTetrisSign();
                Location sign = TARDISStaticLocationGetters.getLocationFromBukkitString(tetrisSign);
                return new GameLocations(first, second, sign, id);
            }
        }
        return null;
    }

    private void updateScoreboard() {
        // use a sign for scores
        Sign sign = (Sign) signLocation.getBlock().getState();
        SignSide front = sign.getSide(Side.FRONT);
        front.line(0, Component.text("Level: " + level));
        front.line(1, Component.text("Lines: " + linesClearedTotal));
        front.line(2, Component.text("Score:"));
        front.line(3, Component.text(score));
        sign.update(true);
    }

    public void drawBoard() {
        if (board == null) {
            return;
        }
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                drawBlock(board.get(x, y), x, y);
            }
        }
    }

    public void drawCurrent() {
        if (current == null) {
            return;
        }
        for (int y = 0; y < current.getShape().length; y++) {
            for (int x = 0; x < current.getShape()[y].length; x++) {
                byte b = current.getShape()[y][x];
                if (b != 0) {
                    drawBlock(b, x + current.getXOffset(), y + current.getYOffset());
                }
            }
        }
    }

    private void drawBlock(int id, int x, int y) {
        world.getBlockAt(boardLocation.getBlockX() - x, boardLocation.getBlockY() - y, boardLocation.getBlockZ()).setType(makeColor(id));
    }

    private Material makeColor(int id) {
        return (switch (id) {
            case 1 -> Material.WHITE_WOOL;
            case 2 -> switch (level % 10) {
                case 1 -> Material.LIME_WOOL;
                case 2 -> Material.PINK_WOOL;
                case 3, 4 -> Material.GREEN_WOOL;
                case 5 -> Material.CYAN_WOOL;
                case 6 -> Material.YELLOW_WOOL;
                case 7, 8 -> Material.RED_WOOL;
                case 9 -> Material.ORANGE_WOOL;
                default -> Material.LIGHT_BLUE_WOOL;
            };
            case 3 -> switch (level % 10) {
                case 1 -> Material.GREEN_WOOL;
                case 2, 4 -> Material.MAGENTA_WOOL;
                case 5 -> Material.LIME_WOOL;
                case 6 -> Material.RED_WOOL;
                case 7 -> Material.PURPLE_WOOL;
                case 9 -> Material.ORANGE_WOOL;
                default -> Material.BLUE_WOOL;
            };
            default -> Material.GRAY_WOOL;
        });
    }

    public void start() {
        if (state == GameState.INITIALIZING) {
            state = GameState.PLAYING;
            updateCurrent();
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (state == GameState.PLAYING) {
                        drawBoard();
                        drawCurrent();
                    } else {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
            startGravity();
        }
    }

    private void updateCurrent() {
        if (state == GameState.GAME_OVER) {
            return;
        }
        current = nextPiece.getAndUpdate();
        current.reset();
        current.move(board.getWidth() / 2, 0);
        if (board.isBlocked(current)) {
            gameOver();
        }
        drawCurrent();
    }

    private void startGravity() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (state == GameState.PLAYING) {
                    moveDown();
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 100, getGravityDelay());
    }

    private long getGravityDelay() {
        return switch (level) {
            case 0 -> 48;
            case 1 -> 43;
            case 2 -> 38;
            case 3 -> 33;
            case 4 -> 28;
            case 5 -> 23;
            case 6 -> 18;
            case 7 -> 13;
            case 8 -> 8;
            case 9 -> 6;
            default -> {
                if (10 <= level && level < 13) {
                    yield 5;
                } else if (13 <= level && level < 16) {
                    yield 4;
                } else if (16 <= level && level < 19) {
                    yield 3;
                } else if (19 <= level && level < 29) {
                    yield 2;
                } else {
                    yield 1;
                }
            }
        };
    }

    public void rotateClockwise() {
        if (state == GameState.PLAYING && current != null) {
            current.rotateClockwise();
            if (board.isBlocked(current)) {
                current.rotateCounterClockwise();
            } else {
                playSound(GameSound.ROTATE);
            }
        }
    }

    public void rotateCounterClockwise() {
        if (state == GameState.PLAYING && current != null) {
            current.rotateCounterClockwise();
            if (board.isBlocked(current)) {
                current.rotateClockwise();
            } else {
                playSound(GameSound.ROTATE);
            }
        }
    }

    public void moveLeft() {
        if (state == GameState.PLAYING && current != null) {
            current.move(-1, 0);
            if (board.isBlocked(current)) {
                current.move(1, 0);
            } else {
                playSound(GameSound.MOVE);
            }
        }
    }

    public void moveRight() {
        if (state == GameState.PLAYING && current != null) {
            current.move(1, 0);
            if (board.isBlocked(current)) {
                current.move(-1, 0);
            } else {
                playSound(GameSound.MOVE);
            }
        }
    }

    public void moveDown() {
        if (state == GameState.PLAYING && current != null) {
            current.move(0, 1);
            if (board.isBlocked(current)) {
                current.move(0, -1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (current != null && board.isPlacable(current)) {
                            placeCurrent();
                        }
                    }
                }.runTaskLater(plugin, 10);
            }
        }
    }

    private void placeCurrent() {
        board.place(current);
        current = null;
        updateScoreboard();
        int cleared = board.clearFullLines();
        linesClearedLevel += cleared;
        linesClearedTotal += cleared;
        updateScoreboard();
        checkTransition();
        score += switch (cleared) {
            case 1 -> 40L;
            case 2 -> 100L;
            case 3 -> 300L;
            case 4 -> 1200L;
            default -> 0;
        } * (level + 1);
        updateScoreboard();
        switch (cleared) {
            case 1, 2, 3 -> playSound(GameSound.LINE_CLEAR);
            case 4 -> playSound(GameSound.TETRIS);
        }
        updateCurrent();
    }

    private void checkTransition() {
        if (startLevel == level) {
            if (linesClearedLevel >= startLevel * 10L + 10 || linesClearedLevel >= Math.max(100, startLevel * 10 - 50)) {
                transition();
            }
        } else if (linesClearedLevel >= 10) {
            transition();
        }
    }

    private void transition() {
        if (state == GameState.PLAYING) {
            playSound(GameSound.LEVEL_UP);
            level++;
            updateScoreboard();
            linesClearedLevel = 0;
            // clear top
            for (int y = 1; y < 4; y++) {
                for (int x = 0; x < board.getWidth(); x++) {
                    Block block = world.getBlockAt(boardLocation.getBlockX() - x, boardLocation.getBlockY() + y, boardLocation.getBlockZ());
                    block.setType(Material.AIR);
                }
            }
        }
    }

    public void gameOver() {
        state = GameState.GAME_OVER;
        new BukkitRunnable() {
            @Override
            public void run() {
                current = null;
                playSound(GameSound.GAME_OVER);
                board.close();
                // tell player to sneak to exit
                plugin.getMessenger().message(player, "Game Over! Sneak to exit.");
            }
        }.runTaskLater(plugin, 20);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInput(PlayerInputEvent event) {
        Player player = event.getPlayer();
        if (!ArcadeTracker.PLAYERS.containsKey(player.getUniqueId()) ) {
            return;
        }
        Entity entity = player.getVehicle();
        if (entity instanceof ArmorStand) {
            Input input = event.getInput();
            if (input.isSneak()) {
                abort();
            }
            if (input.isJump()) {
                rotateCounterClockwise();
            }
            if (input.isLeft()) {
                moveLeft();
            }
            if (input.isRight()) {
                moveRight();
            }
            if (input.isForward()) {
                rotateClockwise();
            }
            if (input.isBackward()) {
                moveDown();
            }
        }
    }

    public void abort() {
        if (ArcadeTracker.PLAYERS.containsKey(player.getUniqueId())) {
            UUID uuid = player.getUniqueId();
            ArcadeData data = ArcadeTracker.PLAYERS.get(uuid);
            // teleport player back to games room
            player.setFallDistance(0.0f);
            player.teleport(data.backup());
            player.setFallDistance(0.0f);
            // reset flight status
            player.setAllowFlight(data.allowFlight());
            HandlerList.unregisterAll(this);
            ArcadeTracker.PLAYERS.remove(uuid);
            // put player back in travellers table
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", data.id());
            where.put("uuid", uuid.toString());
            plugin.getQueryFactory().doInsert("travellers", where);
        }
    }

    public void playSound(GameSound gameSound) {
        switch (gameSound) {
            case MOVE -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.2f, 2f);
            case DROP -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 0.2f, 0f);
            case ROTATE -> {
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 0.1f, 0.9f);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_SNARE, 0.1f, 0.9f);
                    }
                }.runTaskLater(plugin, 3);
            }
            case LINE_CLEAR -> {
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
            }
            case TETRIS -> new BukkitRunnable() {
                int i = 0;

                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.2f, 1.4f + (i % 2) * 0.4f);
                    i++;
                    if (i > 6) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
            case LEVEL_UP -> {
                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.3f, 1f + (i % 2) * 0.4f);
                        i++;
                        if (i == 6) {
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0, 3);
                new BukkitRunnable() {
                    int i = 1;

                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 0.3f, 1.4f + (i % 2) * 0.4f);
                        i++;
                        if (i == 5) {
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin, 18, 3);
            }
            case GAME_OVER -> new BukkitRunnable() {
                int i = 0;
                float v = 0.2f;

                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_DEATH, v, 0f);
                    player.playSound(player.getLocation(), Sound.BLOCK_TUFF_BREAK, v, 0f);
                    i++;
                    v *= 0.6f;
                    if (i > 10) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 3);
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
