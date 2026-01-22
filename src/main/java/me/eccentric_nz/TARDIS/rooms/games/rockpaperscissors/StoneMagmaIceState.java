package me.eccentric_nz.TARDIS.rooms.games.rockpaperscissors;

import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

public enum StoneMagmaIceState {
    DRAW(
        new ItemStack[]{
            null,
            null,
            Letters.D(DyeColor.CYAN, DyeColor.WHITE),
            Letters.R(DyeColor.CYAN, DyeColor.WHITE),
            Letters.A(DyeColor.CYAN, DyeColor.WHITE),
            Letters.W(DyeColor.CYAN, DyeColor.WHITE),
            Letters.exclamation(DyeColor.CYAN, DyeColor.WHITE),
            null,
            null
        },
        Sound.ENTITY_SNIFFER_SEARCHING
    ),
    WIN(
        new ItemStack[]{
            Letters.Y(DyeColor.LIME, DyeColor.WHITE),
            Letters.O(DyeColor.LIME, DyeColor.WHITE),
            Letters.U(DyeColor.LIME, DyeColor.WHITE),
            null,
            Letters.W(DyeColor.LIME, DyeColor.WHITE),
            Letters.I(DyeColor.LIME, DyeColor.WHITE),
            Letters.N(DyeColor.LIME, DyeColor.WHITE),
            Letters.exclamation(DyeColor.LIME, DyeColor.WHITE),
            null
        },
        Sound.ITEM_GOAT_HORN_SOUND_0
    ),
    LOSE(
        new ItemStack[]{
            Letters.Y(DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.O(DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.U(DyeColor.MAGENTA, DyeColor.WHITE),
            null,
            Letters.L(DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.O(DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.S(DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.E(DyeColor.MAGENTA, DyeColor.WHITE),
            Letters.exclamation(DyeColor.MAGENTA, DyeColor.WHITE)
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
