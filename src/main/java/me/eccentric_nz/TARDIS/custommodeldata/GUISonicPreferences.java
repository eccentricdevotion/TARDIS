package me.eccentric_nz.TARDIS.custommodeldata;

import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GUISonicPreferences {

    // Sonic Prefs Menu
    MARK_I(10000001, 0, Material.BLAZE_ROD, ChatColor.DARK_GRAY),
    MARK_II(10000002, 1, Material.BLAZE_ROD, ChatColor.YELLOW),
    MARK_III(10000003, 2, Material.BLAZE_ROD, ChatColor.DARK_PURPLE),
    MARK_IV(10000004, 3, Material.BLAZE_ROD, ChatColor.GRAY),
    EIGHTH_DOCTOR(10000008, 4, Material.BLAZE_ROD, ChatColor.BLUE),
    NINTH_DOCTOR(10000009, 5, Material.BLAZE_ROD, ChatColor.GREEN),
    TENTH_DOCTOR(10000010, 6, Material.BLAZE_ROD, ChatColor.AQUA),
    ELEVENTH_DOCTOR(10000011, 7, Material.BLAZE_ROD, ChatColor.RESET),
    WAR_DOCTOR(10000085, 8, Material.BLAZE_ROD, ChatColor.DARK_RED),
    THIRTEENTH_DOCTOR(10000013, 9, Material.BLAZE_ROD, ChatColor.BLACK),
    MASTER(10000032, 10, Material.BLAZE_ROD, ChatColor.DARK_BLUE),
    SARAH_JANE(10000033, 11, Material.BLAZE_ROD, ChatColor.RED),
    RIVER_SONG(10000031, 12, Material.BLAZE_ROD, ChatColor.GOLD),
    NINTH_DOCTOR_OPEN(12000009, 14, Material.BLAZE_ROD, ChatColor.DARK_GREEN),
    TENTH_DOCTOR_OPEN(12000010, 15, Material.BLAZE_ROD, ChatColor.DARK_AQUA),
    ELEVENTH_DOCTOR_OPEN(12000011, 16, Material.BLAZE_ROD, ChatColor.LIGHT_PURPLE),
    TWELFTH_DOCTOR(10000012, 17, Material.BLAZE_ROD, ChatColor.UNDERLINE),
    INSTRUCTIONS(1, 22, Material.BOOK, ChatColor.RESET),
    CLOSE(1, 26, Material.BOWL, ChatColor.RESET);

    private final int customModelData;
    private final int slot;
    private final Material material;
    private final ChatColor chatColor;

    GUISonicPreferences(int customModelData, int slot, Material material, ChatColor chatColor) {
        this.customModelData = customModelData;
        this.slot = slot;
        this.material = material;
        this.chatColor = chatColor;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        String s = toString();
        return TARDISStringUtils.capitalise(s);
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}
