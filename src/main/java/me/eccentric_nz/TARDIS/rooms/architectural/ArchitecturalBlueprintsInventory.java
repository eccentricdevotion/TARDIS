package me.eccentric_nz.TARDIS.rooms.architectural;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.companionGUI.VanishChecker;
import me.eccentric_nz.TARDIS.custommodels.GUIArs;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetBlueprint;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class ArchitecturalBlueprintsInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;

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
        List<String> perms = rsb.getRoomBlueprints(player.getUniqueId().toString());
        ItemStack[][] disks = TreeBlueprints.getBlueprints();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 9; c++) {
                ItemStack is = disks[r][c];
                String perm = is.getPersistentDataContainer().get(TARDIS.plugin.getBlueprintKey(), PersistentDataType.STRING);
                if (!perms.contains(perm)) {
                    items[i] = is.withType(Material.MUSIC_DISC_RELIC);
                } else {
                    items[i] = is;
                }
                i++;
            }
        }
        // scroll up
        ItemStack scroll_up = ItemStack.of(GUIChemistry.SCROLL_UP.material(), 1);
        scroll_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Scroll up"));
        scroll_up.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.SCROLL_UP.key());
        items[27] = scroll_up;
        // scroll down
        ItemStack scroll_down = ItemStack.of(GUIChemistry.SCROLL_DOWN.material(), 1);
        scroll_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Scroll down"));
        scroll_down.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.SCROLL_DOWN.key());
        items[28] = scroll_down;
        // 36-44 online players
        i = 36;
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            UUID uuid = p.getUniqueId();
            if (i < 45 && uuid != player.getUniqueId() && VanishChecker.canSee(player, p)) {
                ItemStack head = ItemStack.of(Material.PLAYER_HEAD, 1);
                head.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile(p.getPlayerProfile()));
                head.setData(DataComponentTypes.CUSTOM_NAME, Component.text(p.getName()));
                head.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(p.getUniqueId().toString())).build());
                items[i] = head;
                i++;
            }
        }
        if (i == 36) {
            // no players online
            ItemStack players = ItemStack.of(Material.GRAY_TERRACOTTA, 1);
            players.setData(DataComponentTypes.CUSTOM_NAME, Component.text("No players online"));
            items[36] = players;
        }
        // scroll left
        ItemStack scroll_left = ItemStack.of(GUIArs.BUTTON_SCROLL_L.material(), 1);
        scroll_left.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_L", "Scroll left")));
        items[45] = scroll_left;
        // scroll right
        ItemStack scroll_right = ItemStack.of(GUIArs.BUTTON_SCROLL_R.material(), 1);
        scroll_right.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_SCROLL_R", "Scroll right")));
        items[46] = scroll_right;
        // give
        ItemStack give = ItemStack.of(Material.GILDED_BLACKSTONE, 1);
        give.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Give blueprint disc"));
        items[49] = give;
        // close
        items[53] = GUIItemFactory.close();
        return items;
    }
}
