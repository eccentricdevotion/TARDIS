/*
 * Copyright (C) 2025 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.dev;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

public class HeadCommand {

    private final TARDIS plugin;

    public HeadCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void giveAPIHead(Player player) {
        // -534922148,-1465496919,-2021441615,2044066305
        // thenosefairy 27974553,854216283,-1579200332,1832878819
        String result = String.format("%08x", -534922148) + String.format("%08x", -1465496919) + String.format("%08x", -2021441615) + String.format("%08x", 2044066305);
        String withDashes = result.replaceFirst(
                "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
        );
        plugin.debug(withDashes);
        try {
            UUID uuid = UUID.fromString(withDashes);
            PlayerProfile profile = plugin.getServer().createProfile(uuid);
            PlayerTextures textures = profile.getTextures();
            URL url = URI.create("http://textures.minecraft.net/texture/3583ce755f1fc238393e11f64b7214d9602075c214b9ed99cec4e35cf1e24c4").toURL();
            textures.setSkin(url);
            profile.setTextures(textures);
            ItemStack is = ItemStack.of(Material.PLAYER_HEAD);
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setPlayerProfile(profile);
            is.setItemMeta(im);
            player.getInventory().addItem(is);
        } catch (IllegalArgumentException | MalformedURLException e) {
            plugin.debug("Bad UUID or URL");
        }
    }
}
