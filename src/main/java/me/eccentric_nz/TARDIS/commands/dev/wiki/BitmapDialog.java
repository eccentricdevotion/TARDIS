package me.eccentric_nz.TARDIS.commands.dev.wiki;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BitmapDialog {

    public Dialog create() {
        ItemStack blue = ItemStack.of(Material.BLUE_DYE);
        ItemMeta im = blue.getItemMeta();
        im.setItemModel(ChameleonVariant.BLUE_CLOSED.getKey());
        blue.setItemMeta(im);
        List<DialogBody> body = List.of(
                DialogBody.item(blue, DialogBody.plainMessage(Component.text("ㇺ")), false, false, 16, 139),
                DialogBody.plainMessage(Component.text("Other text"), 200)
        );
        DialogBase dialogData = DialogBase.create(Component.text("Bitmap"), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, List.of());
        ActionButton button = ActionButton.create(Component.text("Done"), null, 150, null);
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.notice(button))
        );
    }
}
