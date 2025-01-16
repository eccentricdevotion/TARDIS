package me.eccentric_nz.TARDIS.skins;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

public interface SkinChanger {

    void set(Player player, Skin skin);

    void set(Player player, JsonObject properties);

    void remove(Player player);
}
