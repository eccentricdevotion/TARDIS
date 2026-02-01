package me.eccentric_nz.TARDIS.rooms.games.connect_four;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.rooms.games.tictactoe.MatchState;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ConnectFourListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final List<List<Integer>> COLUMNS = new ArrayList<>();
    private final ItemStack hole;
    private final ItemStack red;
    private final ItemStack yellow;
    private final Robot tardis;
    private Board board;
    private MatchState state;

    public ConnectFourListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
        COLUMNS.add(List.of(0, 9, 18, 27, 36, 45));
        COLUMNS.add(List.of(1, 10, 19, 28, 37, 46));
        COLUMNS.add(List.of(2, 11, 20, 29, 38, 47));
        COLUMNS.add(List.of(3, 12, 21, 30, 39, 48));
        COLUMNS.add(List.of(4, 13, 22, 31, 40, 49));
        COLUMNS.add(List.of(5, 14, 23, 32, 41, 50));
        COLUMNS.add(List.of(6, 15, 24, 33, 42, 51));
        hole = ItemStack.of(Material.BLUE_CONCRETE_POWDER);
        ItemMeta holeMeta = hole.getItemMeta();
        holeMeta.displayName(Component.text(" "));
        hole.setItemMeta(holeMeta);
        red = ItemStack.of(Material.RED_CONCRETE_POWDER);
        ItemMeta redMeta = red.getItemMeta();
        redMeta.displayName(Component.text("Red"));
        red.setItemMeta(redMeta);
        yellow = ItemStack.of(Material.YELLOW_CONCRETE_POWDER);
        ItemMeta yellowMeta = yellow.getItemMeta();
        yellowMeta.displayName(Component.text("Yellow"));
        yellow.setItemMeta(yellowMeta);
        state = MatchState.PLAYER_TURN;
        board = new Board();
        tardis = new Robot(board);
    }

    @EventHandler
    public void onConnectFourClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ConnectFourInventory)) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 54) {
            return;
        }
        InventoryView view = event.getView();
        if (view.getItem(slot) == null) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        switch (slot) {
            // reset
            case 35 -> {
                view.getTopInventory().setContents(new ConnectFourInventory(plugin).getInventory().getContents());
                board = new Board();
                state = MatchState.PLAYER_TURN;
            }
            // close
            case 53 -> close(player);
            // drop a block
            case 0, 1, 2, 3, 4, 5, 6 -> {
                if (state != MatchState.PLAYER_TURN || view.getItem(slot).getType() != Material.BLUE_CONCRETE_POWDER) {
                    return;
                }
                state = MatchState.TARDIS_TURN;
                List<Integer> column = COLUMNS.get(slot);
                new BukkitRunnable() {
                    int start = 0;

                    @Override
                    public void run() {
                        if (start < 5 && view.getItem(column.get(start + 1)).getType() == Material.BLUE_CONCRETE_POWDER) {
                            view.setItem(column.get(start), hole);
                            view.setItem(column.get(start + 1), red);
                            start++;
                        } else {
                            view.setItem(column.get(start), red);
                            // update board
                            int[] coords = getXY(column.get(start));
                            board.setToken(coords[0], coords[1], Material.RED_CONCRETE_POWDER);
                            cancel();
                            if (isGameOver()) {
                                // player won
                                state = MatchState.PLAYER_WON;
                            } else if (isDraw()) {
                                // show something
                                state = MatchState.DRAW;
                            } else {
                                // start tardis turn
                                int c = tardis.chooseColumn();
                                plugin.debug("tardis.chooseColumn -> " + c);
                                List<Integer> column = COLUMNS.get(c);
                                new BukkitRunnable() {
                                    int start = 0;

                                    @Override
                                    public void run() {
                                        if (start < 5 && view.getItem(column.get(start + 1)).getType() == Material.BLUE_CONCRETE_POWDER) {
                                            view.setItem(column.get(start), hole);
                                            view.setItem(column.get(start + 1), yellow);
                                            start++;
                                        } else {
                                            view.setItem(column.get(start), yellow);
                                            // update board
                                            int[] coords = getXY(column.get(start));
                                            board.setToken(coords[0], coords[1], Material.YELLOW_CONCRETE_POWDER);
                                            cancel();
                                            if (isGameOver()) {
                                                // tardis won
                                                state = MatchState.TARDIS_WON;
                                            } else if (isDraw()) {
                                                // show something
                                                state = MatchState.DRAW;
                                            } else {
                                                // start player turn
                                                state = MatchState.PLAYER_TURN;
                                            }
                                        }
                                    }
                                }.runTaskTimer(plugin, 2, 5);
                            }
                        }
                    }
                }.runTaskTimer(plugin, 2, 5);
            }
            default -> { }
        }
    }

    private int[] getXY(int slot) {
        int y = 0;
        int x = 0;
        if (slot < 7) {
            x = slot;
        } else if (slot < 16) {
            x = slot - 9;
            y = 1;
        } else if (slot < 25) {
            x = slot - 18;
            y = 2;
        } else if (slot < 34) {
            x = slot - 27;
            y = 3;
        } else if (slot < 43) {
            x = slot - 36;
            y = 4;
        } else if (slot < 52) {
            x = slot - 45;
            y = 5;
        }
        return new int[]{x, y};
    }

    public boolean isGameOver() {
        return board.checkHorizontally() || board.checkVertically() || board.checkDiagonally();
    }

    public boolean isDraw() {
        return (board.areAllColumnsFull() && !isGameOver());
    }
}
