package me.eccentric_nz.TARDIS.mobfarming;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIFarming;
import me.eccentric_nz.TARDIS.database.data.FarmPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetFarmingPrefs;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TARDISFarmingInventory {

    private final TARDIS plugin;
    private final UUID uuid;
    private final ItemStack[] gui;
    private final ItemStack on;
    private final ItemStack off;
    public TARDISFarmingInventory(TARDIS plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;
        off = new ItemStack(GUIFarming.OFF.getMaterial(), 1);
        ItemMeta offMeta = off.getItemMeta();
        offMeta.setDisplayName(ChatColor.RED + plugin.getLanguage().getString("SET_OFF"));
        off.setItemMeta(offMeta);
        on = new ItemStack(GUIFarming.ON.getMaterial(), 1);
        ItemMeta onMeta = on.getItemMeta();
        onMeta.setDisplayName(ChatColor.RED + plugin.getLanguage().getString("SET_ON"));
        on.setItemMeta(offMeta);
        gui = getItemStack();
    }

    /**
     * Constructs an inventory for the Farming GUI
     *
     * @return an array of ItemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[36];
        // allay, apiary, aquarium, bamboo, birdcage, farm, geode, hutch, igloo, iistubil, lava, mangrove, pen, stable, stall, village
        ResultSetFarmingPrefs rs = new ResultSetFarmingPrefs(plugin, uuid.toString());
        // set farming status
        if (rs.resultSet()) {
            FarmPrefs farmPrefs = rs.getData();
            stack[9] = farmPrefs.shouldFarmAllay() ? on : off;
            stack[10] = farmPrefs.shouldFarmBees() ? on : off;
            stack[11] = farmPrefs.shouldFarmFish() ? on : off;
            stack[12] = farmPrefs.shouldFarmPandas() ? on : off;
            stack[13] = farmPrefs.shouldFarmParrots() ? on : off;
            stack[14] = farmPrefs.shouldFarmLivestock() ? on : off;
            stack[15] = farmPrefs.shouldFarmAxolotls() ? on : off;
            stack[16] = farmPrefs.shouldFarmRabbits() ? on : off;
            stack[17] = farmPrefs.shouldFarmPolarBears() ? on : off;
            stack[27] = farmPrefs.shouldFarmCamels() ? on : off;
            stack[28] = farmPrefs.shouldFarmStriders() ? on : off;
            stack[29] = farmPrefs.shouldFarmFrogs() ? on : off;
            stack[30] = farmPrefs.shouldFarmSniffers() ? on : off;
            stack[31] = farmPrefs.shouldFarmHorses() ? on : off;
            stack[32] = farmPrefs.shouldFarmLlamas() ? on : off;
            stack[33] = farmPrefs.shouldFarmVillagers() ? on : off;
        } else {
            // insert a new record
            HashMap<String, Object> set = new HashMap<>();
            set.put("uuid", uuid.toString());
            plugin.getQueryFactory().doSyncInsert("farming_prefs", set);
            // set all to on (the default)
            stack[9] = on;
            stack[10] = on;
            stack[11] = on;
            stack[12] = on;
            stack[13] = on;
            stack[14] = on;
            stack[15] = on;
            stack[16] = on;
            stack[17] = on;
            stack[27] = on;
            stack[28] = on;
            stack[29] = on;
            stack[30] = on;
            stack[31] = on;
            stack[32] = on;
            stack[33] = on;
        }
        // set GUI buttons
        for (GUIFarming f : GUIFarming.values()) {
            if (f != GUIFarming.ON && f != GUIFarming.OFF) {
                ItemStack is = new ItemStack(f.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(f.getMob());
                if (f.getCustomModelData() != -1) {
                    im.setCustomModelData(f.getCustomModelData());
                }
                if (f != GUIFarming.CLOSE) {
                    im.setLore(List.of(f.getRoomName()));
                }
                is.setItemMeta(im);
                stack[f.getSlot()] = is;
            }
        }
        return stack;
    }

    public ItemStack[] getGui() {
        return gui;
    }
}
