package me.eccentric_nz.TARDIS.console;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.console.models.ColourType;
import me.eccentric_nz.TARDIS.console.models.ConsoleColourChanger;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetScreenInteraction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.rotors.Rotor;
import me.eccentric_nz.TARDIS.rotors.TimeRotor;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class CustomiseConsoleListener extends TARDISMenuListener {

    private final TARDIS plugin;
    private final HashMap<UUID, Integer> selectedRotors = new HashMap<>();
    private final HashMap<UUID, Integer> scrollRotor = new HashMap<>();
    private final HashMap<UUID, Integer> selectedConsoles = new HashMap<>();
    private final HashMap<UUID, Integer> scrollConsole = new HashMap<>();
    private final List<Map.Entry<String, Rotor>> rotorKeys = new ArrayList<>(Rotor.byName.entrySet());
    private final List<Map.Entry<Material, NamespacedKey>> consoleKeys = new ArrayList<>(ColourType.BY_MATERIAL.entrySet());

    public CustomiseConsoleListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onCustomiseGuiClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof CustomiseConsoleInventory)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        int slot = event.getRawSlot();
        if (slot < 0 || slot > 53) {
            ClickType click = event.getClick();
            if (click.equals(ClickType.SHIFT_RIGHT) || click.equals(ClickType.SHIFT_LEFT) || click.equals(ClickType.DOUBLE_CLICK)) {
                event.setCancelled(true);
            }
            return;
        }
        event.setCancelled(true);
        InventoryView view = event.getView();
        switch (slot) {
            // rotor selection
            case 9, 10, 11, 12, 13, 14, 15, 16, 17 -> selectedRotors.put(playerUUID, slot);
            // previous rotor
            case 18 -> {
                int start;
                int max = rotorKeys.size() - 9;
                if (scrollRotor.containsKey(playerUUID)) {
                    start = scrollRotor.get(playerUUID) + 1;
                    if (start >= max) {
                        start = max;
                    }
                } else {
                    start = 1;
                }
                scrollRotor.put(playerUUID, start);
                for (int i = 0; i < 9; i++) {
                    setRotorSlot(view, (9 + i), rotorKeys.get(start + i));
                }
            }
            // next rotor
            case 26 -> {
                int start;
                if (scrollRotor.containsKey(playerUUID)) {
                    start = scrollRotor.get(playerUUID) - 1;
                    if (start <= 0) {
                        start = 0;
                    }
                } else {
                    start = 0;
                }
                scrollRotor.put(playerUUID, start);
                for (int i = 0; i < 9; i++) {
                    setRotorSlot(view, (9 + i), rotorKeys.get(start + i));
                }
            }
            // console selection
            case 27, 28, 29, 30, 31, 32, 33, 34, 35 -> selectedConsoles.put(playerUUID, slot);
            // previous console
            case 36 -> {
                int start;
                int max = consoleKeys.size() - 9;
                if (scrollConsole.containsKey(playerUUID)) {
                    start = scrollConsole.get(playerUUID) + 1;
                    if (start >= max) {
                        start = max;
                    }
                } else {
                    start = 1;
                }
                scrollConsole.put(playerUUID, start);
                for (int i = 0; i < 9; i++) {
                    setConsoleSlot(view, (27 + i), consoleKeys.get(start + i));
                }
            }
            // next console
            case 44 -> {
                int start;
                if (scrollConsole.containsKey(playerUUID)) {
                    start = scrollConsole.get(playerUUID) - 1;
                    if (start <= 0) {
                        start = 0;
                    }
                } else {
                    start = 0;
                }
                scrollConsole.put(playerUUID, start);
                for (int i = 0; i < 9; i++) {
                    setConsoleSlot(view, (27 + i), consoleKeys.get(start + i));
                }
            }
            // save
            case 49 -> {
                int cost = 0;
                // get TARDIS id
                HashMap<String, Object> where = new HashMap<>();
                where.put("uuid", playerUUID.toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    int id = rs.getTardis().getTardisId();
                    int artron = rs.getTardis().getArtronLevel();
                    if (selectedRotors.containsKey(playerUUID)) {
                        cost += plugin.getArtronConfig().getInt("customise.time_rotor");
                    }
                    if (selectedConsoles.containsKey(playerUUID)) {
                        cost += plugin.getArtronConfig().getInt("customise.console");
                    }
                    if (cost > artron) {
                        resetAndClose(player);
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ENERGY_NOT_ENOUGH");
                        return;
                    }
                    if (selectedRotors.containsKey(playerUUID)) {
                        // must have an existing rotor
                        ItemFrame itemFrame = TimeRotor.getItemFrame(rs.getTardis().getRotor());
                        // only if entity still exists
                        if (itemFrame != null) {
                            // change rotor
                            Rotor rotor = getRotorType(view, playerUUID);
                            if (rotor != null) {
                                TimeRotor.setRotor(rotor.offModel(), itemFrame);
                            }
                        }
                    }
                    if (selectedConsoles.containsKey(playerUUID)) {
                        // must have an existing custom console
                        ResultSetScreenInteraction rssi = new ResultSetScreenInteraction(plugin, id);
                        if (rssi.resultSet() && rssi.getUuid() != null) {
                            Entity screen = plugin.getServer().getEntity(rssi.getUuid());
                            if (screen != null && screen.getPersistentDataContainer().has(plugin.getUnaryKey(), PersistentDataType.STRING)) {
                                String uuids = screen.getPersistentDataContainer().get(plugin.getUnaryKey(), PersistentDataType.STRING);
                                if (uuids != null) {
                                    // get selected console
                                    String colour = getConsoleColour(view, playerUUID);
                                    // change console
                                    new ConsoleColourChanger(plugin, screen.getLocation(), uuids, colour).paint();
                                }
                            }
                        }
                    }
                    if (cost > 0) {
                        HashMap<String, Object> wheret = new HashMap<>();
                        wheret.put("tardis_id", id);
                        plugin.getQueryFactory().alterEnergyLevel("tardis", -cost, wheret, player);
                    }
                    resetAndClose(player);
                }
            }
            // close
            case 53 -> resetAndClose(player);
            default -> { }
        }
    }

    private Rotor getRotorType(InventoryView view, UUID playerUUID) {
        int slot = selectedRotors.get(playerUUID);
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        String r = TARDISStringUtils.toEnumUppercase(ComponentUtils.stripColour(im.displayName()));
        return Rotor.byName.get(r);
    }

    private String getConsoleColour(InventoryView view, UUID playerUUID) {
        int slot = selectedConsoles.get(playerUUID);
        ItemStack is = view.getItem(slot);
        Material material = is.getType();
        return ColourType.BY_MATERIAL.get(material).getKey().replace("console_", "");
    }

    void setRotorSlot(InventoryView view, int slot, Map.Entry<String, Rotor> rotor) {
        ItemStack is = ItemStack.of(Material.LIGHT_GRAY_DYE, 1);
        ItemMeta im = is.getItemMeta();
        im.setItemModel(rotor.getValue().offModel());
        im.displayName(Component.text(TARDISStringUtils.capitalise(rotor.getKey())));
        is.setItemMeta(im);
        view.setItem(slot, is);
    }

    void setConsoleSlot(InventoryView view, int slot, Map.Entry<Material, NamespacedKey> colour) {
        String name = colour.getValue().getKey().replace("console_", "");
        ItemStack is = ItemStack.of(colour.getKey(), 1);
        ItemMeta im = is.getItemMeta();
        String dn = TARDISStringUtils.capitalise(name) + " Console";
        im.displayName(ComponentUtils.toWhite(dn));
        is.setItemMeta(im);
        view.setItem(slot, is);
    }

    void resetAndClose(Player player) {
        selectedRotors.remove(player.getUniqueId());
        selectedConsoles.remove(player.getUniqueId());
        close(player);
    }
}
