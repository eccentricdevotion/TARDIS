package me.eccentric_nz.TARDIS.info.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class InfoDialog {

    public Dialog create(TARDIS plugin, TARDISInfoMenu tardisInfoMenu) {
        try {
            List<DialogBody> body = new ArrayList<>();
            if (ItemLookup.ITEMS.containsKey(tardisInfoMenu)) {
                InfoIcon infoIcon = ItemLookup.ITEMS.get(tardisInfoMenu);
                ItemStack icon = ItemStack.of(infoIcon.item());
                ItemMeta im = icon.getItemMeta();
                im.setItemModel(infoIcon.model());
                icon.setItemMeta(im);
                // set custom name
                body.add(DialogBody.item(icon, null, false, false, 16, 16));
            }
            String description = TARDISDescription.valueOf(tardisInfoMenu.toString()).getDesc();
            body.add(DialogBody.plainMessage(Component.text(description), 200));
            String title = TARDISStringUtils.capitalise(tardisInfoMenu.toString().replace("_INFO", ""));
            DialogBase dialogData = DialogBase.create(Component.text(title), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, List.of());
            DialogAction action = DialogAction.staticAction(ClickEvent.callback(
                    audience -> audience.showDialog(new CategoryDialog().create())
            ));
            ActionButton yesButton = ActionButton.create(Component.text("Back"), null, 150, action);
            ActionButton noButton = ActionButton.create(Component.text("Done"), null, 150, null);
            return Dialog.create(builder -> builder.empty()
                    .base(dialogData)
                    .type(DialogType.confirmation(yesButton, noButton))
            );
        } catch (IllegalArgumentException e) {
            plugin.debug(tardisInfoMenu.toString());
        }
        return null;
    }
}
