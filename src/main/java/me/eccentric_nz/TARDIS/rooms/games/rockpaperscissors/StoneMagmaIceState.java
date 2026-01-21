package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public enum StoneMagmaIceState {
    DRAW(
        new ItemStack[]{
            null,
            null,
            Letters.D(Material.CYAN_BANNER, DyeColor.CYAN, DyeColor.WHITE),
            Letters.R(Material.CYAN_BANNER, DyeColor.CYAN, DyeColor.WHITE),
            Letters.A(Material.CYAN_BANNER, DyeColor.CYAN, DyeColor.WHITE),
            Letters.W(Material.CYAN_BANNER, DyeColor.CYAN, DyeColor.WHITE),
            Letters.exclamation(Material.CYAN_BANNER, DyeColor.CYAN, DyeColor.WHITE),
            null,
            null
        },
        Sound.ENTITY_SNIFFER_SEARCHING
    ),
    WIN(
        new ItemStack[]{
            Letters.Y(Material.LIME_BANNER, DyeColor.LIME, DyeColor.WHITE),
            Letters.O(Material.LIME_BANNER, DyeColor.LIME, DyeColor.WHITE),
            Letters.U(Material.LIME_BANNER, DyeColor.LIME, DyeColor.WHITE),
            null,
            Letters.W(Material.LIME_BANNER, DyeColor.LIME, DyeColor.WHITE),
            Letters.I(Material.LIME_BANNER, DyeColor.LIME, DyeColor.WHITE),
            Letters.N(Material.LIME_BANNER, DyeColor.LIME, DyeColor.WHITE),
            Letters.exclamation(Material.LIME_BANNER, DyeColor.LIME, DyeColor.WHITE),
            null
        },
        Sound.ITEM_GOAT_HORN_SOUND_0
    ),
    LOSE(
        new ItemStack[]{
            Letters.Y(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.O(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.U(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE),
            null,
            Letters.L(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.O(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.S(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.E(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.exclamation(Material.MAGENTA_BANNER, DyeColor.MAGENTA, DyeColor.WHITE)
        },
        Sound.ENTITY_RAVAGER_CELEBRATE
    );

    private final ItemStack[] banners;
    private final Sound sound;

    StoneMagmaIceState(ItemStack[] banners, Sound sound) {
        this.banners = banners;
        this.sound = sound;
    }

    public ItemStack[] getBanners() {
        return banners;
    }

    public Sound getSound() {
        return sound;
    }
}
