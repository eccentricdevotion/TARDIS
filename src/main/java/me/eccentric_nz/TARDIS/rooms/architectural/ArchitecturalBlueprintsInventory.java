package me.eccentric_nz.TARDIS.rooms.architectural;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.BlueprintRoom;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.companionGUI.VanishChecker;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlueprint;
import me.eccentric_nz.TARDIS.planets.GallifreyBlueprintTrade;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class ArchitecturalBlueprintsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;
    private final List<BlueprintRoom> notThese = List.of(BlueprintRoom.ARCHITECTURAL, BlueprintRoom.JETTISON, BlueprintRoom.ZERO);

    public ArchitecturalBlueprintsInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Architectural Blueprints", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the ArchitecturalBlueprints Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] items = new ItemStack[54];
        int i = 0;
        ResultSetBlueprint rsb = new ResultSetBlueprint(plugin);
        for (String bp : rsb.getRoomBlueprints(player.getUniqueId().toString())) {
            BlueprintRoom blueprint = BlueprintRoom.PERMS.get(bp);
            ItemStack is = plugin.getTardisAPI().getTARDISBlueprintItem("BLUEPRINT_ROOM_" + blueprint.toString(), player);
            items[i] = is;
            i++;
        }
        // scroll up
        ItemStack scroll_up = ItemStack.of(GUIChemistry.SCROLL_UP.material(), 1);
        ItemMeta uim = scroll_up.getItemMeta();
        uim.displayName(Component.text("Scroll up"));
        uim.setItemModel(GUIChemistry.SCROLL_UP.key());
        scroll_up.setItemMeta(uim);
        items[27] = scroll_up;
        // scroll down
        ItemStack scroll_down = ItemStack.of(GUIChemistry.SCROLL_DOWN.material(), 1);
        ItemMeta dim = scroll_down.getItemMeta();
        dim.displayName(Component.text("Scroll down"));
        dim.setItemModel(GUIChemistry.SCROLL_DOWN.key());
        scroll_down.setItemMeta(dim);
        items[28] = scroll_down;
        // 36-44 online players
        i = 36;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            if (i < 45 && uuid != player.getUniqueId() && VanishChecker.canSee(player, p)) {
                ItemStack head = ItemStack.of(Material.PLAYER_HEAD, 1);
                SkullMeta skull = (SkullMeta) head.getItemMeta();
                skull.setOwningPlayer(p);
                skull.displayName(Component.text(p.getName()));
                skull.lore(List.of(Component.text(p.getUniqueId().toString())));
                head.setItemMeta(skull);
                items[i] = head;
                i++;
            }
        }
        // scroll left
        ItemStack scroll_left = ItemStack.of(GUIArs.BUTTON_SCROLL_L.material(), 1);
        ItemMeta nim = scroll_left.getItemMeta();
        nim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_L", "Scroll left")));
        scroll_left.setItemMeta(nim);
        items[45] = scroll_left;
        // scroll right
        ItemStack scroll_right = ItemStack.of(GUIArs.BUTTON_SCROLL_R.material(), 1);
        ItemMeta pim = scroll_right.getItemMeta();
        pim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_R", "Scroll right")));
        scroll_right.setItemMeta(pim);
        items[46] = scroll_right;
        // 49 give
        ItemStack give = ItemStack.of(GUIArs.BUTTON_SCROLL_R.material(), 1);
        ItemMeta gim = give.getItemMeta();
        gim.displayName(Component.text("Give blueprint disc"));
        give.setItemMeta(gim);
        items[46] = give;
        // close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(gui);
        items[53] = close;
        return items;
    }
}
