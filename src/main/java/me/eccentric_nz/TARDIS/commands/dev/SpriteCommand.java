package me.eccentric_nz.TARDIS.commands.dev;

import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.object.ObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import org.bukkit.entity.Player;

public class SpriteCommand {

    public void send(Player player, boolean b) {
        // create the sprite
        SpriteObjectContents contents;
        if (b) {
            contents = ObjectContents.sprite(
                    Key.key("minecraft:items"),
                    Key.key("minecraft:item/lapis_lazuli")
            );
        } else {
            contents = ObjectContents.sprite(
                    Key.key("tardis:tardis"),
                    Key.key("tardis:gui/icons/tardis")
            );
        }
        // use the component
        Component star = Component.object(contents).append(Component.text(" TARDIS", NamedTextColor.BLUE));
        player.sendMessage(star);
        String atlasKey = contents.atlas().asString();
        String spriteKey = contents.sprite().asString();
        player.sendMessage(atlasKey);
        player.sendMessage(spriteKey);
    }
}
