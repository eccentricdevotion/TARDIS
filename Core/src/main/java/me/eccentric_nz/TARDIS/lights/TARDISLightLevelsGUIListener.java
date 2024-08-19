package me.eccentric_nz.TARDIS.lights;

import com.mojang.datafixers.util.Pair;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.LightLevel;
import me.eccentric_nz.TARDIS.control.actions.ConsoleLampAction;
import me.eccentric_nz.TARDIS.control.actions.LightLevelAction;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.listeners.TARDISMenuListener;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class TARDISLightLevelsGUIListener extends TARDISMenuListener {

    private final TARDIS plugin;

    public TARDISLightLevelsGUIListener(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    /**
     * Listens for player clicking inside an inventory. If the inventory is a TARDIS GUI, then the click is processed
     * accordingly.
     *
     * @param event a player clicking an inventory slot
     */
    @EventHandler(ignoreCancelled = true)
    public void onLightLevelsMenuClick(InventoryClickEvent event) {
        InventoryView view = event.getView();
        if (!view.getTitle().equals(ChatColor.DARK_RED + "TARDIS Light Levels")) {
            return;
        }
        event.setCancelled(true);
        int slot = event.getRawSlot();
        Player player = (Player) event.getWhoClicked();
        UUID uuid = player.getUniqueId();
        if (slot >= 0 && slot < 54) {
            // get selection
            ItemStack is = view.getItem(slot);
            if (is != null) {
                // get TARDIS id
                int id = -1;
                ResultSetTardisID rst = new ResultSetTardisID(plugin);
                if (rst.fromUUID(uuid.toString())) {
                    id = rst.getTardisId();
                }
                switch (slot) {
                    case 9 -> {
                        // interior minus
                        Pair<Boolean, Integer> setLevel = getNewState(view, 10, false);
                        if (setLevel.getFirst()) {
                            // update indicator
                            setState(view, 10, setLevel.getSecond());
                            // set light level
                            setLightLevel(setLevel.getSecond(), 50, id);
                            // TODO update control?
                        }
                    }
                    case 11 -> {
                        // interior plus
                        Pair<Boolean, Integer> setLevel = getNewState(view, 10, true);
                        if (setLevel.getFirst()) {
                            // update indicator
                            setState(view, 10, setLevel.getSecond());
                            // set light level
                            setLightLevel(setLevel.getSecond(), 50, id);
                            // TODO update control?
                        }
                    }
                    case 15 -> {
                        // exterior minus
                        Pair<Boolean, Integer> setLevel = getNewState(view, 16, false);
                        if (setLevel.getFirst()) {
                            // update indicator
                            setState(view, 16, setLevel.getSecond());
                            // set light level
                            setLightLevel(setLevel.getSecond(), 49, id);
                            // TODO update control?
                        }
                    }
                    case 17 -> {
                        // exterior plus
                        Pair<Boolean, Integer> setLevel = getNewState(view, 16, true);
                        if (setLevel .getFirst()) {
                            // update indicator
                            setState(view, 16, setLevel.getSecond());
                            // set light level
                            setLightLevel(setLevel.getSecond(), 49, id);
                            // TODO update control?
                        }
                    }
                    case 30 -> {
                        // console minus
                        Pair<Boolean, Integer> setLevel = getNewState(view, 31, false);
                        if (setLevel.getFirst()) {
                            // update indicator
                            setState(view, 31, setLevel.getSecond());
                            // set light level
                            setLightLevel(setLevel.getSecond(), 56, id);
                            // TODO update control?
                        }
                    }
                    case 32 -> {
                        // console plus
                        Pair<Boolean, Integer> setLevel = getNewState(view, 31, true);
                        if (setLevel.getFirst()) {
                            // update indicator
                            setState(view, 31, setLevel.getSecond());
                            // set light level
                            setLightLevel(setLevel.getSecond(), 56, id);
                            // TODO update control?
                        }
                    }
                    case 45 -> {
                        ItemStack[] lightStacks = new TARDISLightsInventory(plugin, id).getGUI();
                        Inventory lightGUI = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "TARDIS Lights");
                        lightGUI.setContents(lightStacks);
                        player.openInventory(lightGUI);
                    }
                    case 53 -> close(player); // close
                }
            }
        }
    }

    private Pair<Boolean, Integer> getNewState(InventoryView view, int slot, boolean next) {
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        String lore = im.getLore().get(0);
        int state = TARDISNumberParsers.parseInt(lore);
        int index;
        if (slot == 16) {
            index = ArrayUtils.indexOf(LightLevel.exterior_level, state);
            if (next && index - 1 >= 0) {
                return new Pair<>(true, LightLevel.exterior_level[index - 1]);
            } else if (index + 1 < LightLevel.exterior_level.length) {
                return new Pair<>(true, LightLevel.exterior_level[index + 1]);
            }
        } else {
            index = ArrayUtils.indexOf(LightLevel.interior_level, state);
            if (next && index - 1 >= 0) {
                return new Pair<>(true, LightLevel.interior_level[index - 1]);
            } else if (index + 1 < LightLevel.interior_level.length) {
                return new Pair<>(true, LightLevel.interior_level[index + 1]);
            }
        }
        return new Pair<>(false, state);
    }

    private void setState(InventoryView view, int slot, int level) {
        ItemStack is = view.getItem(slot);
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();
        lore.set(0, "" + level);
        im.setLore(lore);
        is.setItemMeta(im);
    }

    private void setLightLevel(int setLevel, int which, int id) {
        // get control
        ResultSetLightLevel rs = new ResultSetLightLevel(plugin);
        if (rs.fromTypeAndID(which, id)) {
            if (which > 50) {
                new ConsoleLampAction(plugin).illuminate(id, setLevel, rs.getControlId());
            } else {
                new LightLevelAction(plugin).illuminate(setLevel - 1, rs.getControlId(), rs.isPowered(), which, rs.isPoliceBox(), id, rs.isLightsOn());
            }
        }
    }
}
